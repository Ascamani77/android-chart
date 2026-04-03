import asyncio
import websockets
import json
import MetaTrader5 as mt5
from datetime import datetime, timedelta, timezone

# 1. Initialize MT5
if not mt5.initialize():
    print("MT5 initialize failed.")
    quit()

print("MetaTrader 5 connected successfully.")

# Map Android timeframe names to MT5 timeframes
TIMEFRAME_MAP = {
    "1m": mt5.TIMEFRAME_M1,
    "5m": mt5.TIMEFRAME_M5,
    "15m": mt5.TIMEFRAME_M15,
    "30m": mt5.TIMEFRAME_M30,
    "1h": mt5.TIMEFRAME_H1,
    "4h": mt5.TIMEFRAME_H4,
    "1d": mt5.TIMEFRAME_D1,
}

def clean_symbol(symbol):
    """Removes any suffix like 'm' or 'M' from symbols for frontend display."""
    if not symbol:
        return symbol
    # Find the position of any non-alphabetic characters or where common suffixes start
    # Specifically for brokers like Exness that use BTCUSDm, XAUUSDm, etc.
    s = symbol.upper()
    if s.endswith("M"):
        return symbol[:-1]
    return symbol

async def handle_client(websocket):
    print(f"Android connected: {websocket.remote_address}")
    current_symbol = "BTCUSDm"
    current_tf = mt5.TIMEFRAME_H1

    async def send_history(symbol, tf):
        print(f"Fetching history for {symbol}...")
        rates = mt5.copy_rates_from_pos(symbol, tf, 0, 200)
        if rates is not None:
            history = []
            for r in rates:
                history.append({
                    "time": int(r[0]),
                    "open": float(r[1]),
                    "high": float(r[2]),
                    "low": float(r[3]),
                    "close": float(r[4])
                })
            await websocket.send(json.dumps({
                "type": "history",
                "symbol": clean_symbol(symbol),
                "data": history
            }))
        else:
            print(f"Failed to get rates for {symbol}")

    # Send initial history
    await send_history(current_symbol, current_tf)

    async def listen():
        nonlocal current_symbol, current_tf
        try:
            async for message in websocket:
                try:
                    data = json.loads(message)
                    if data.get("action") == "subscribe":
                        symbol = data.get("symbol")
                        # Add 'm' back if missing for MT5 internal use (adjust if your broker uses 'M')
                        # Most Exness accounts use 'm', some might use 'M'.
                        # We'll try to find the actual symbol in MT5.
                        all_symbols = [s.name for s in mt5.symbols_get()]
                        matched_symbol = next((s for s in all_symbols if s.upper() == symbol.upper() or s.upper() == (symbol + "M").upper()), symbol)

                        current_symbol = matched_symbol
                        # If timeframe is sent, update it
                        tf_str = data.get("timeframe", "1h")
                        current_tf = TIMEFRAME_MAP.get(tf_str, mt5.TIMEFRAME_H1)
                        await send_history(current_symbol, current_tf)
                except Exception as e:
                    print(f"Listen error: {e}")
        except websockets.exceptions.ConnectionClosed:
            pass

    async def stream():
        while True:
            try:
                # 1. Send Tick Data
                tick = mt5.symbol_info_tick(current_symbol)
                if tick:
                    payload = {
                        "type": "tick",
                        "name": clean_symbol(current_symbol),
                        "lastPrice": float(tick.bid),
                        "bid": float(tick.bid),
                        "ask": float(tick.ask),
                        "time": int(tick.time),
                        "volume": float(tick.volume)
                    }
                    await websocket.send(json.dumps(payload))

                # 2. Send Account Info
                account = mt5.account_info()
                if account:
                    acc_payload = {
                        "type": "account",
                        "balance": float(account.balance),
                        "equity": float(account.equity),
                        "unrealizedPnl": float(account.profit),
                        "realizedPnl": 0.0,
                        "margin": float(account.margin),
                        "availableFunds": float(account.margin_free),
                        "ordersMargin": 0.0,
                        "marginBuffer": float(account.margin_level) if account.margin_level > 0 else 100.0
                    }
                    await websocket.send(json.dumps(acc_payload))

                # 3. Send Positions
                positions = mt5.positions_get()
                if positions is not None:
                    pos_list = []
                    for p in positions:
                        pos_list.append({
                            "ticket": p.ticket,
                            "symbol": clean_symbol(p.symbol),
                            "type": "buy" if p.type == mt5.POSITION_TYPE_BUY else "sell",
                            "price_open": float(p.price_open),
                            "volume_current": float(p.volume),
                            "time_setup": int(p.time) * 1000,
                            "tp": float(p.tp),
                            "sl": float(p.sl),
                            "profit": float(p.profit)
                        })
                    await websocket.send(json.dumps({"type": "positions", "data": pos_list}))

                # 4. Send Pending Orders
                orders = mt5.orders_get()
                if orders is not None:
                    ord_list = []
                    for o in orders:
                        type_map = {
                            mt5.ORDER_TYPE_BUY_LIMIT: "Buy Limit",
                            mt5.ORDER_TYPE_SELL_LIMIT: "Sell Limit",
                            mt5.ORDER_TYPE_BUY_STOP: "Buy Stop",
                            mt5.ORDER_TYPE_SELL_STOP: "Sell Stop",
                            mt5.ORDER_TYPE_BUY_STOP_LIMIT: "Buy Stop Limit",
                            mt5.ORDER_TYPE_SELL_STOP_LIMIT: "Sell Stop Limit",
                        }
                        type_str = type_map.get(o.type, "Pending")

                        ord_list.append({
                            "ticket": o.ticket,
                            "symbol": clean_symbol(o.symbol),
                            "type": "buy" if o.type in [mt5.ORDER_TYPE_BUY, mt5.ORDER_TYPE_BUY_LIMIT, mt5.ORDER_TYPE_BUY_STOP, mt5.ORDER_TYPE_BUY_STOP_LIMIT] else "sell",
                            "type_name": type_str,
                            "price_open": float(o.price_open),
                            "volume_initial": float(o.volume_initial),
                            "time_setup": int(o.time_setup) * 1000,
                            "status": "Working"
                        })
                    await websocket.send(json.dumps({"type": "orders", "data": ord_list}))

                # 5. Send History
                now = datetime.now(timezone.utc)
                history_orders = mt5.history_orders_get(now - timedelta(days=7), now)
                if history_orders is not None:
                    hist_list = []
                    for h in history_orders:
                        if h.state in [mt5.ORDER_STATE_STARTED, mt5.ORDER_STATE_PLACED]:
                            continue

                        status = "Filled"
                        if h.state == mt5.ORDER_STATE_CANCELED:
                            status = "Cancelled"
                        elif h.state == mt5.ORDER_STATE_REJECTED:
                            status = "Rejected"
                        elif h.state != mt5.ORDER_STATE_FILLED:
                            continue

                        reason_map = {
                            mt5.ORDER_REASON_CLIENT: "Client",
                            mt5.ORDER_REASON_MOBILE: "Mobile",
                            mt5.ORDER_REASON_WEB: "Web",
                            mt5.ORDER_REASON_EXPERT: "Expert",
                            mt5.ORDER_REASON_SL: "Stop Loss",
                            mt5.ORDER_REASON_TP: "Take Profit"
                        }
                        reason_str = reason_map.get(h.reason, "System")

                        hist_list.append({
                            "ticket": h.ticket,
                            "symbol": clean_symbol(h.symbol),
                            "type": "buy" if h.type in [mt5.ORDER_TYPE_BUY, mt5.ORDER_TYPE_BUY_LIMIT, mt5.ORDER_TYPE_BUY_STOP] else "sell",
                            "type_name": f"{reason_str} {'Market' if h.type in [mt5.ORDER_TYPE_BUY, mt5.ORDER_TYPE_SELL] else 'Limit/Stop'}",
                            "status": status,
                            "price_open": float(h.price_open),
                            "volume_initial": float(h.volume_initial),
                            "time_setup": int(h.time_setup) * 1000,
                            "time_done": int(h.time_done) * 1000,
                            "price_current": float(h.price_current)
                        })
                    hist_list.sort(key=lambda x: x['time_done'], reverse=True)
                    await websocket.send(json.dumps({"type": "order_history", "data": hist_list}))

                # 6. Send Deals
                deals = mt5.history_deals_get(now - timedelta(days=7), now)
                if deals is not None:
                    filtered_deals = [d for d in deals if d.entry == mt5.DEAL_ENTRY_OUT or d.type in [mt5.DEAL_TYPE_BALANCE, mt5.DEAL_TYPE_CREDIT]]
                    temp_list = []
                    running_balance = account.balance
                    filtered_deals.sort(key=lambda x: x.time, reverse=True)

                    for d in filtered_deals:
                        profit = float(d.profit + d.commission + d.swap)
                        balance_after = running_balance
                        balance_before = running_balance - profit

                        action_prefix = "Close" if d.entry == mt5.DEAL_ENTRY_OUT else "Balance"
                        temp_list.append({
                            "id": str(d.ticket),
                            "time": int(d.time) * 1000,
                            "balanceBefore": float(balance_before),
                            "balanceAfter": float(balance_after),
                            "profit": profit,
                            "action": f"{action_prefix} {clean_symbol(d.symbol)} ({d.comment})" if d.comment else f"{action_prefix} {clean_symbol(d.symbol)}"
                        })
                        running_balance = balance_before

                    await websocket.send(json.dumps({"type": "balance_history", "data": temp_list}))
            except Exception as stream_err:
                print(f"Streaming step error: {stream_err}")
                if isinstance(stream_err, websockets.exceptions.ConnectionClosed):
                    break

            await asyncio.sleep(0.5)

    try:
        await asyncio.gather(listen(), stream())
    except websockets.exceptions.ConnectionClosed:
        print(f"Android disconnected")
    except Exception as e:
        print(f"Error: {e}")

async def main():
    import socket
    hostname = socket.gethostname()
    local_ip = socket.gethostbyname(hostname)
    async with websockets.serve(handle_client, "0.0.0.0", 8081):
        print(f"Bridge Server running on ws://{local_ip}:8081")
        await asyncio.Future()

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        mt5.shutdown()
