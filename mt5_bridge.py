import asyncio
import websockets
import json
import MetaTrader5 as mt5

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
            await websocket.send(json.dumps({"type": "history", "data": history}))
        else:
            print(f"Failed to get rates for {symbol}")

    # Send initial history
    await send_history(current_symbol, current_tf)

    try:
        async def listen():
            nonlocal current_symbol, current_tf
            async for message in websocket:
                try:
                    data = json.loads(message)
                    if data.get("action") == "subscribe":
                        symbol = data.get("symbol")
                        if not symbol.endswith("m"): symbol += "m"
                        current_symbol = symbol
                        # If timeframe is sent, update it
                        tf_str = data.get("timeframe", "1h")
                        current_tf = TIMEFRAME_MAP.get(tf_str, mt5.TIMEFRAME_H1)
                        await send_history(current_symbol, current_tf)
                except Exception as e:
                    print(f"Listen error: {e}")

        async def stream():
            while True:
                tick = mt5.symbol_info_tick(current_symbol)
                if tick:
                    payload = {
                        "type": "tick",
                        "name": current_symbol.replace("m", ""),
                        "lastPrice": float(tick.bid),
                        "bid": float(tick.bid),
                        "ask": float(tick.ask),
                        "time": int(tick.time),
                        "volume": float(tick.volume)
                    }
                    await websocket.send(json.dumps(payload))
                await asyncio.sleep(0.5)

        await asyncio.gather(listen(), stream())
    except websockets.exceptions.ConnectionClosed:
        print(f"Android disconnected")
    except Exception as e:
        print(f"Error: {e}")

async def main():
    async with websockets.serve(handle_client, "0.0.0.0", 8081):
        print("Bridge Server running on ws://172.26.23.133:8081")
        await asyncio.Future()

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        mt5.shutdown()
