import asyncio
import websockets
import json
import MetaTrader5 as mt5
from datetime import datetime, timedelta, timezone

# 1. Initialize MT5
if not mt5.initialize():
    print("MT5 initialize failed.")
    quit()

terminal_info = mt5.terminal_info()
if terminal_info:
    if not terminal_info.trade_allowed:
        print("WARNING: Algo Trading is DISABLED in MT5 terminal. Please enable 'Algo Trading' button in the top toolbar.")
    if not terminal_info.connected:
        print("WARNING: MT5 terminal is NOT connected to the broker server.")

account_info = mt5.account_info()
if account_info:
    print(f"Connected to account: {account_info.login} at {account_info.server}")
else:
    print("WARNING: Could not retrieve account info. Check connection.")

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
                tick_volume = 0.0
                real_volume = 0.0
                try:
                    tick_volume = float(r["tick_volume"])
                except Exception:
                    try:
                        tick_volume = float(r[5])
                    except Exception:
                        tick_volume = 0.0
                try:
                    real_volume = float(r["real_volume"])
                except Exception:
                    try:
                        real_volume = float(r[7])
                    except Exception:
                        real_volume = 0.0

                history.append({
                    "time": int(r[0]),
                    "open": float(r[1]),
                    "high": float(r[2]),
                    "low": float(r[3]),
                    "close": float(r[4]),
                    "volume": real_volume if real_volume > 0 else tick_volume
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
                    action = data.get("action")
                    if action == "subscribe":
                        symbol = data.get("symbol")
                        all_symbols = [s.name for s in mt5.symbols_get()]
                        matched_symbol = next((s for s in all_symbols if s.upper() == symbol.upper() or s.upper() == (symbol + "M").upper()), symbol)
                        current_symbol = matched_symbol
                        tf_str = data.get("timeframe", "1h")
                        current_tf = TIMEFRAME_MAP.get(tf_str, mt5.TIMEFRAME_H1)
                        await send_history(current_symbol, current_tf)

                    elif action == "place_order":
                        symbol = data.get("symbol")
                        all_symbols = [s.name for s in mt5.symbols_get()]
                        matched_symbol = next((s for s in all_symbols if s.upper() == symbol.upper() or s.upper() == (symbol + "M").upper()), symbol)

                        side = data.get("type", "buy").lower()
                        order_category = data.get("orderType", "market").lower()
                        volume = float(data.get("volume", 0.01))
                        price = float(data.get("price", 0))
                        tp = float(data.get("tp", 0))
                        sl = float(data.get("sl", 0))

                        current_tick = mt5.symbol_info_tick(matched_symbol)
                        if current_tick is None:
                            await websocket.send(json.dumps({"type": "order_result", "status": "failed", "error": "No tick data"}))
                            continue

                        # Default for Market Execution
                        trade_action = mt5.TRADE_ACTION_DEAL
                        mt5_type = mt5.ORDER_TYPE_BUY if side == "buy" else mt5.ORDER_TYPE_SELL
                        exec_price = current_tick.ask if side == "buy" else current_tick.bid
                        filling_type = mt5.ORDER_FILLING_IOC

                        # Override for Pending Orders
                        if order_category == "limit":
                            trade_action = mt5.TRADE_ACTION_PENDING
                            mt5_type = mt5.ORDER_TYPE_BUY_LIMIT if side == "buy" else mt5.ORDER_TYPE_SELL_LIMIT
                            exec_price = price
                            filling_type = mt5.ORDER_FILLING_RETURN
                        elif order_category == "stop":
                            trade_action = mt5.TRADE_ACTION_PENDING
                            mt5_type = mt5.ORDER_TYPE_BUY_STOP if side == "buy" else mt5.ORDER_TYPE_SELL_STOP
                            exec_price = price
                            filling_type = mt5.ORDER_FILLING_RETURN
                        elif order_category == "stoplimit":
                            trade_action = mt5.TRADE_ACTION_PENDING
                            mt5_type = mt5.ORDER_TYPE_BUY_STOP_LIMIT if side == "buy" else mt5.ORDER_TYPE_SELL_STOP_LIMIT
                            exec_price = price
                            filling_type = mt5.ORDER_FILLING_RETURN

                        request = {
                            "action": trade_action,
                            "symbol": matched_symbol,
                            "volume": volume,
                            "type": mt5_type,
                            "price": exec_price,
                            "magic": 123456,
                            "comment": data.get("comment", "App Order"),
                            "type_time": mt5.ORDER_TIME_GTC,
                            "type_filling": filling_type,
                        }

                        if order_category == "stoplimit":
                            request["stoplimit"] = float(data.get("stopLimitPrice", 0))
                        if tp > 0: request["tp"] = tp
                        if sl > 0: request["sl"] = sl

                        result = mt5.order_send(request)
                        if result and result.retcode == mt5.TRADE_RETCODE_DONE:
                            await websocket.send(json.dumps({
                                "type": "order_result",
                                "status": "success",
                                "ticket": result.order if hasattr(result, 'order') else result.request.order,
                                "price": result.price,
                                "volume": result.volume
                            }))
                        else:
                            error_msg = result.comment if result else "Order failed"
                            await websocket.send(json.dumps({"type": "order_result", "status": "failed", "error": error_msg}))
                        print(f"Order Action: {trade_action}, Result: {result.comment if result else 'Failed'}")

                    elif action == "close_position":
                        ticket = int(data.get("ticket"))
                        positions = mt5.positions_get(ticket=ticket)
                        if positions:
                            p = positions[0]
                            order_type = mt5.ORDER_TYPE_SELL if p.type == mt5.POSITION_TYPE_BUY else mt5.ORDER_TYPE_BUY
                            tick = mt5.symbol_info_tick(p.symbol)
                            price = tick.bid if p.type == mt5.POSITION_TYPE_BUY else tick.ask
                            request = {
                                "action": mt5.TRADE_ACTION_DEAL,
                                "symbol": p.symbol,
                                "volume": float(data.get("volume", p.volume)),
                                "type": order_type,
                                "position": ticket,
                                "price": price,
                                "magic": 123456,
                                "comment": "App Close",
                                "type_time": mt5.ORDER_TIME_GTC,
                                "type_filling": mt5.ORDER_FILLING_IOC,
                            }
                            result = mt5.order_send(request)
                        else:
                            print(f"Position {ticket} not found")

                    elif action == "modify_position":
                        ticket = int(data.get("ticket"))
                        request = {
                            "action": mt5.TRADE_ACTION_SLTP,
                            "position": ticket,
                            "tp": float(data.get("tp", 0)),
                            "sl": float(data.get("sl", 0)),
                        }
                        result = mt5.order_send(request)

                except Exception as e:
                    print(f"Listen error: {e}")
        except websockets.exceptions.ConnectionClosed:
            pass

    async def stream():
        while True:
            try:
                # Tick
                tick = mt5.symbol_info_tick(current_symbol)
                if tick:
                    await websocket.send(json.dumps({
                        "type": "tick", "name": clean_symbol(current_symbol),
                        "lastPrice": float(tick.bid), "bid": float(tick.bid), "ask": float(tick.ask),
                        "time": int(tick.time), "volume": float(tick.volume)
                    }))
                # Account
                acc = mt5.account_info()
                term = mt5.terminal_info()
                if acc and term:
                    await websocket.send(json.dumps({
                        "type": "account", "balance": float(acc.balance), "equity": float(acc.equity),
                        "unrealizedPnl": float(acc.profit), "margin": float(acc.margin),
                        "availableFunds": float(acc.margin_free), "trade_allowed": term.trade_allowed
                    }))
                # Positions
                pos = mt5.positions_get()
                if pos is not None:
                    await websocket.send(json.dumps({"type": "positions", "data": [
                        {"ticket": p.ticket, "symbol": clean_symbol(p.symbol), "type": "buy" if p.type == mt5.POSITION_TYPE_BUY else "sell",
                         "price_open": float(p.price_open), "volume_current": float(p.volume), "time_setup": int(p.time)*1000,
                         "tp": float(p.tp), "sl": float(p.sl), "profit": float(p.profit)} for p in pos
                    ]}))
                # Orders
                orders = mt5.orders_get()
                if orders is not None:
                    type_map = {mt5.ORDER_TYPE_BUY_LIMIT: "Buy Limit", mt5.ORDER_TYPE_SELL_LIMIT: "Sell Limit",
                                mt5.ORDER_TYPE_BUY_STOP: "Buy Stop", mt5.ORDER_TYPE_SELL_STOP: "Sell Stop",
                                mt5.ORDER_TYPE_BUY_STOP_LIMIT: "Buy Stop Limit", mt5.ORDER_TYPE_SELL_STOP_LIMIT: "Sell Stop Limit"}
                    await websocket.send(json.dumps({"type": "orders", "data": [
                        {"ticket": o.ticket, "symbol": clean_symbol(o.symbol),
                         "type": "buy" if o.type in [0, 2, 4, 6] else "sell",
                         "type_name": type_map.get(o.type, "Pending"), "price_open": float(o.price_open),
                         "volume_initial": float(o.volume_initial), "time_setup": int(o.time_setup)*1000, "status": "Working"} for o in orders
                    ]}))

                # History, Deals omitted for brevity in this loop or keep if needed
                # (Assuming original history/deals logic remains similar)

            except Exception as e:
                if isinstance(e, websockets.exceptions.ConnectionClosed): break
                print(f"Stream error: {e}")
            await asyncio.sleep(0.5)

    try:
        await asyncio.gather(listen(), stream())
    except Exception: pass

async def main():
    async with websockets.serve(handle_client, "0.0.0.0", 8081):
        await asyncio.Future()

if __name__ == "__main__":
    try: asyncio.run(main())
    except KeyboardInterrupt: mt5.shutdown()
