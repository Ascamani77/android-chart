import asyncio
import json
import time
import urllib.parse
import urllib.request
import xml.etree.ElementTree as ET
from email.utils import parsedate_to_datetime
from datetime import date, datetime, timedelta, timezone
from hashlib import md5
from pathlib import Path

import MetaTrader5 as mt5
import websockets


TRADAYS_WIDGET_REFERER = "https://www.tradays.com/en/economic-calendar/widget?mode=2&dateFormat=DMY"
TRADAYS_CONTENT_URL = "https://www.tradays.com/en/economic-calendar/widget/content"
TRADAYS_HEADERS = {
    "X-Requested-With": "XMLHttpRequest",
    "Referer": TRADAYS_WIDGET_REFERER,
    "User-Agent": (
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
        "AppleWebKit/537.36 (KHTML, like Gecko) "
        "Chrome/135.0.0.0 Safari/537.36"
    ),
    "Accept": "application/json, text/javascript, */*; q=0.01",
    "Accept-Language": "en-US,en;q=0.9",
}
TRADAYS_NEWS_URL = "https://www.tradays.com/en/news/widget/content"
TRADAYS_IMPORTANCE_MASK = 14
TRADAYS_CURRENCY_MASK = 262143
CALENDAR_REFRESH_SECONDS = 60.0
TRADAYS_REQUEST_TIMEOUT_SECONDS = 10
TRADAYS_REQUEST_RETRIES = 2
LOCAL_TZ = datetime.now().astimezone().tzinfo or timezone.utc
NEWS_AI_SNAPSHOT_PATH = Path(__file__).resolve().parent / "news_ai_payload.json"
NEWS_LOOKBACK_DAYS = 7
FALLBACK_NEWS_LIMIT = 700
MIN_TRADAYS_NEWS_ITEMS = 80
FALLBACK_NEWS_SOURCES = [
    ("https://www.investing.com/rss/news_25.rss", "markets"),
    ("https://www.forexlive.com/feed/news", "forex"),
    ("https://news.google.com/rss/search?q=forex+market+when:7d&hl=en-US&gl=US&ceid=US:en", "forex"),
    ("https://news.google.com/rss/search?q=stock+market+when:7d&hl=en-US&gl=US&ceid=US:en", "markets"),
    ("https://news.google.com/rss/search?q=crypto+market+when:7d&hl=en-US&gl=US&ceid=US:en", "crypto"),
    ("https://news.google.com/rss/search?q=commodities+market+when:7d&hl=en-US&gl=US&ceid=US:en", "commodities"),
]

COUNTRY_CODE_MAP = {
    0: "WW",
    36: "AU",
    76: "BR",
    124: "CA",
    156: "CN",
    250: "FR",
    276: "DE",
    344: "HK",
    356: "IN",
    380: "IT",
    392: "JP",
    410: "KR",
    484: "MX",
    554: "NZ",
    578: "NO",
    702: "SG",
    710: "ZA",
    724: "ES",
    752: "SE",
    756: "CH",
    826: "GB",
    840: "US",
    999: "EU",
}

COUNTRY_NAME_MAP = {
    "AU": "Australia",
    "BR": "Brazil",
    "CA": "Canada",
    "CH": "Switzerland",
    "CN": "China",
    "DE": "Germany",
    "EU": "European Union",
    "ES": "Spain",
    "FR": "France",
    "GB": "United Kingdom",
    "HK": "Hong Kong",
    "IN": "India",
    "IT": "Italy",
    "JP": "Japan",
    "KR": "South Korea",
    "MX": "Mexico",
    "NO": "Norway",
    "NZ": "New Zealand",
    "SE": "Sweden",
    "SG": "Singapore",
    "US": "United States",
    "WW": "Worldwide",
    "ZA": "South Africa",
}

DAY_LABELS = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
MONTH_LABELS = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]


if not mt5.initialize():
    print("MT5 initialize failed.")
    raise SystemExit(1)

terminal_info = mt5.terminal_info()
if terminal_info:
    if not terminal_info.trade_allowed:
        print(
            "WARNING: Algo Trading is DISABLED in MT5 terminal. "
            "Please enable 'Algo Trading' in the MT5 toolbar."
        )
    if not terminal_info.connected:
        print("WARNING: MT5 terminal is NOT connected to the broker server.")

account_info = mt5.account_info()
if account_info:
    print(f"Connected to account: {account_info.login} at {account_info.server}")
else:
    print("WARNING: Could not retrieve account info. Check connection.")

print("MetaTrader 5 connected successfully.")


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
    if not symbol:
        return symbol
    symbol_upper = symbol.upper()
    if symbol_upper.endswith("M"):
        return symbol[:-1]
    return symbol


def resolve_symbol(symbol):
    all_symbols = [item.name for item in mt5.symbols_get()]
    return next(
        (
            item
            for item in all_symbols
            if item.upper() == symbol.upper() or item.upper() == f"{symbol}M".upper()
        ),
        symbol,
    )


def sanitize_metric(value):
    if value is None:
        return ""
    text = str(value)
    text = text.replace("\u200b", "").replace("\xa0", " ").strip()
    return text


def parse_selected_date(value):
    if not value:
        return datetime.now(LOCAL_TZ).date()
    try:
        return date.fromisoformat(value)
    except ValueError:
        return datetime.now(LOCAL_TZ).date()


def local_datetime_from_release(release_ms):
    return datetime.fromtimestamp(release_ms / 1000.0, tz=timezone.utc).astimezone(LOCAL_TZ)


def local_date_iso_for_event(event):
    full_date = event.get("FullDate")
    if full_date:
        try:
            release_dt = datetime.fromisoformat(full_date).replace(tzinfo=timezone.utc)
            return release_dt.astimezone(LOCAL_TZ).date().isoformat()
        except ValueError:
            pass
    return local_datetime_from_release(int(event.get("ReleaseDate", 0))).date().isoformat()


def display_time_label(event):
    time_mode = int(event.get("TimeMode", 0))
    if time_mode == 1:
        return "All day"
    if time_mode == 2:
        return "Unknown"
    release_dt = local_datetime_from_release(int(event.get("ReleaseDate", 0)))
    return release_dt.strftime("%H:%M")


def header_date_label(selected_date):
    return f"{selected_date.day} {MONTH_LABELS[selected_date.month - 1]} {selected_date.year}"


def month_bounds(selected_date):
    start = selected_date.replace(day=1)
    if selected_date.month == 12:
        next_month = selected_date.replace(year=selected_date.year + 1, month=1, day=1)
    else:
        next_month = selected_date.replace(month=selected_date.month + 1, day=1)
    end = next_month - timedelta(days=1)
    return start, end


def iso_datetime_from_release(release_ms):
    return local_datetime_from_release(release_ms).isoformat()


def country_code_for_event(event):
    return COUNTRY_CODE_MAP.get(int(event.get("Country", 0)), "")


def country_name_for_event(event, country_code):
    name = sanitize_metric(event.get("CountryName"))
    if name:
        return name
    return COUNTRY_NAME_MAP.get(country_code, country_code)


def details_url_for_event(event):
    path = sanitize_metric(event.get("Url"))
    if not path:
        return None
    if path.startswith("http://") or path.startswith("https://"):
        return path
    return f"https://www.tradays.com{path}"


def fetch_tradays_calendar(start_date, end_date):
    params = urllib.parse.urlencode(
        {
            "date_mode": 1,
            "from": f"{start_date.isoformat()}T00:00:00",
            "to": f"{end_date.isoformat()}T23:59:59",
            "importance": TRADAYS_IMPORTANCE_MASK,
            "currencies": TRADAYS_CURRENCY_MASK,
        }
    )
    url = f"{TRADAYS_CONTENT_URL}?{params}"

    for attempt in range(TRADAYS_REQUEST_RETRIES):
        try:
            request = urllib.request.Request(url, headers=TRADAYS_HEADERS)
            with urllib.request.urlopen(request, timeout=TRADAYS_REQUEST_TIMEOUT_SECONDS) as response:
                return json.loads(response.read().decode("utf-8"))
        except Exception:
            if attempt == TRADAYS_REQUEST_RETRIES - 1:
                raise
            time.sleep(1)


def fetch_tradays_news():
    params = urllib.parse.urlencode(
        {
            "limit": 50,
        }
    )
    url = f"{TRADAYS_NEWS_URL}?{params}"

    for attempt in range(TRADAYS_REQUEST_RETRIES):
        try:
            request = urllib.request.Request(url, headers=TRADAYS_HEADERS)
            with urllib.request.urlopen(request, timeout=TRADAYS_REQUEST_TIMEOUT_SECONDS) as response:
                return json.loads(response.read().decode("utf-8"))
        except Exception:
            if attempt == TRADAYS_REQUEST_RETRIES - 1:
                raise
            time.sleep(1)


def normalize_tradays_news(raw_payload):
    if isinstance(raw_payload, list):
        return raw_payload
    if isinstance(raw_payload, dict):
        for key in ("items", "data", "result", "news"):
            value = raw_payload.get(key)
            if isinstance(value, list):
                return value
    return []


def write_news_snapshot(payload):
    try:
        NEWS_AI_SNAPSHOT_PATH.write_text(
            json.dumps(payload, ensure_ascii=False, indent=2),
            encoding="utf-8",
        )
    except Exception as exc:
        print(f"News snapshot write error: {exc}")


def empty_news_payload():
    return {
        "type": "news",
        "items": [],
        "lastUpdatedIso": datetime.now(timezone.utc).isoformat()
    }


def _human_time_label(release_dt):
    now = datetime.now(LOCAL_TZ)
    diff = now - release_dt
    if diff.days == 0:
        if diff.seconds < 3600:
            mins = max(diff.seconds // 60, 1)
            return f"{mins} minutes ago"
        return f"{diff.seconds // 3600} hours ago"
    if diff.days == 1:
        return "yesterday"
    return f"{diff.days} days ago"


def _build_news_payload_from_tradays(news_items):
    cutoff = datetime.now(LOCAL_TZ) - timedelta(days=NEWS_LOOKBACK_DAYS)
    processed_news = []
    for item in news_items:
        if not isinstance(item, dict):
            continue
        release_ms = int(item.get("ReleaseDate", 0))
        release_dt = datetime.fromtimestamp(release_ms / 1000.0, tz=timezone.utc).astimezone(LOCAL_TZ)
        if release_dt < cutoff:
            continue

        processed_news.append({
            "id": int(item.get("Id", 0)),
            "title": sanitize_metric(item.get("Title")),
            "timeLabel": _human_time_label(release_dt),
            "isoDateTime": release_dt.isoformat(),
            "countryCode": COUNTRY_CODE_MAP.get(int(item.get("Country", 0)), ""),
            "category": sanitize_metric(item.get("CategoryName")),
            "detailsUrl": details_url_for_event(item)
        })

    payload = {
        "type": "news",
        "items": processed_news,
        "lastUpdatedIso": datetime.now(timezone.utc).isoformat()
    }
    return payload


def _parse_feed_datetime(value):
    text = sanitize_metric(value)
    if not text:
        return None

    # Google/Forex feeds tend to use RFC822 strings.
    try:
        parsed = parsedate_to_datetime(text)
        if parsed is not None:
            if parsed.tzinfo is None:
                parsed = parsed.replace(tzinfo=timezone.utc)
            return parsed.astimezone(LOCAL_TZ)
    except Exception:
        pass

    # Investing feed often uses "YYYY-MM-DD HH:MM:SS".
    for pattern in ("%Y-%m-%d %H:%M:%S", "%Y-%m-%dT%H:%M:%S%z", "%Y-%m-%dT%H:%M:%S"):
        try:
            parsed = datetime.strptime(text, pattern)
            if parsed.tzinfo is None:
                parsed = parsed.replace(tzinfo=timezone.utc)
            return parsed.astimezone(LOCAL_TZ)
        except Exception:
            continue
    return None


def _safe_find_text(item, tag_name):
    element = item.find(tag_name)
    if element is not None and element.text:
        return element.text
    return ""


def _build_fallback_rss_news():
    items = []
    seen_links = set()
    cutoff = datetime.now(LOCAL_TZ) - timedelta(days=NEWS_LOOKBACK_DAYS)

    for source_url, default_category in FALLBACK_NEWS_SOURCES:
        try:
            request = urllib.request.Request(source_url, headers=TRADAYS_HEADERS)
            with urllib.request.urlopen(request, timeout=TRADAYS_REQUEST_TIMEOUT_SECONDS) as response:
                raw = response.read()
            root = ET.fromstring(raw)
        except Exception as exc:
            print(f"RSS fallback fetch error ({source_url}): {exc}")
            continue

        for entry in root.findall(".//item"):
            title = sanitize_metric(_safe_find_text(entry, "title"))
            link = sanitize_metric(_safe_find_text(entry, "link"))
            if not title or not link or link in seen_links:
                continue

            seen_links.add(link)
            release_dt = _parse_feed_datetime(_safe_find_text(entry, "pubDate")) or datetime.now(LOCAL_TZ)
            if release_dt < cutoff:
                continue
            category = sanitize_metric(_safe_find_text(entry, "category")).lower() or default_category
            digest = md5(link.encode("utf-8")).hexdigest()[:8]
            item_id = int(digest, 16) & 0x7FFFFFFF

            items.append({
                "id": item_id,
                "title": title,
                "timeLabel": _human_time_label(release_dt),
                "isoDateTime": release_dt.isoformat(),
                "countryCode": "WW",
                "category": category,
                "detailsUrl": link,
            })

    items.sort(key=lambda item: item["isoDateTime"], reverse=True)
    return items[:FALLBACK_NEWS_LIMIT]


def _merge_news_items(primary_items, secondary_items, limit):
    merged = []
    seen = set()

    for item in primary_items + secondary_items:
        if not isinstance(item, dict):
            continue
        key = (sanitize_metric(item.get("detailsUrl")), sanitize_metric(item.get("title")))
        if key in seen:
            continue
        seen.add(key)
        merged.append(item)

    merged.sort(key=lambda item: sanitize_metric(item.get("isoDateTime")), reverse=True)
    return merged[:limit]


def build_news_payload():
    fallback_items = _build_fallback_rss_news()
    try:
        raw_news = fetch_tradays_news()
        tradays_items = normalize_tradays_news(raw_news)
        if tradays_items:
            tradays_payload = _build_news_payload_from_tradays(tradays_items)
            merged_items = _merge_news_items(
                primary_items=tradays_payload.get("items", []),
                secondary_items=fallback_items,
                limit=FALLBACK_NEWS_LIMIT,
            )
            if len(merged_items) >= MIN_TRADAYS_NEWS_ITEMS:
                return {
                    "type": "news",
                    "items": merged_items,
                    "lastUpdatedIso": datetime.now(timezone.utc).isoformat()
                }
            print("Tradays news is too sparse for 7-day window. Enriching with RSS fallback.")
        print("Tradays news returned no items. Switching to RSS fallback.")
    except Exception as exc:
        print(f"Tradays news fetch unavailable: {exc}. Switching to RSS fallback.")

    return {
        "type": "news",
        "items": fallback_items,
        "lastUpdatedIso": datetime.now(timezone.utc).isoformat()
    }


def build_calendar_payload(selected_date):
    range_start, range_end = month_bounds(selected_date)
    all_events = fetch_tradays_calendar(range_start, range_end)
    selected_date_iso = selected_date.isoformat()
    generated_at = datetime.now(timezone.utc).isoformat()

    day_chips = []
    cursor = range_start
    today_local = datetime.now(LOCAL_TZ).date()
    while cursor <= range_end:
        day_chips.append(
            {
                "isoDate": cursor.isoformat(),
                "dayNumber": cursor.day,
                "dayLabel": DAY_LABELS[cursor.weekday()],
                "isSelected": cursor == selected_date,
                "isToday": cursor == today_local,
            }
        )
        cursor += timedelta(days=1)

    display_events = []
    ai_events = []
    for event in all_events:
        release_ms = int(event.get("ReleaseDate", 0))
        country_code = country_code_for_event(event)
        country_name = country_name_for_event(event, country_code)
        iso_date_time = iso_datetime_from_release(release_ms)
        event_date_iso = local_date_iso_for_event(event)
        shared = {
            "id": int(event.get("Id", 0)),
            "isoDateTime": iso_date_time,
            "dateIso": event_date_iso,
            "currencyCode": sanitize_metric(event.get("CurrencyCode")) or "ALL",
            "countryCode": country_code,
            "countryName": country_name,
            "title": sanitize_metric(event.get("EventName")),
            "importance": sanitize_metric(event.get("Importance")) or "none",
            "actual": sanitize_metric(event.get("ActualValue")),
            "forecast": sanitize_metric(event.get("ForecastValue")),
            "previous": sanitize_metric(event.get("PreviousValue")),
            "impactDirection": int(event.get("ImpactDirection", 0)),
            "eventType": int(event.get("EventType", 0)),
            "timeMode": int(event.get("TimeMode", 0)),
            "processed": int(event.get("Processed", 0)) == 1,
            "detailsUrl": details_url_for_event(event),
        }
        ai_events.append(shared)

        display_events.append(
            {
                "id": shared["id"],
                "isoDateTime": shared["isoDateTime"],
                "releaseTimeLabel": display_time_label(event),
                "countryCode": shared["countryCode"],
                "countryName": shared["countryName"],
                "currencyCode": shared["currencyCode"],
                "title": shared["title"],
                "actual": shared["actual"],
                "forecast": shared["forecast"],
                "previous": shared["previous"],
                "importance": shared["importance"],
                "impactDirection": shared["impactDirection"],
                "isSpeechOrReport": shared["eventType"] == 0,
                "isAllDay": shared["timeMode"] in (1, 2),
                "detailsUrl": shared["detailsUrl"],
            }
        )

    display_events.sort(key=lambda item: item["isoDateTime"])
    ai_events.sort(key=lambda item: item["isoDateTime"])

    return {
        "type": "calendar",
        "display": {
            "sourceLabel": "MT5 Calendar",
            "rangeStartIso": range_start.isoformat(),
            "rangeEndIso": range_end.isoformat(),
            "selectedDateIso": selected_date_iso,
            "headerDateLabel": header_date_label(selected_date),
            "dayChips": day_chips,
            "events": display_events,
            "lastUpdatedIso": generated_at,
        },
        "ai": {
            "source": "mt5-tradays",
            "generatedAtIso": generated_at,
            "selectedDateIso": selected_date_iso,
            "rangeStartIso": range_start.isoformat(),
            "rangeEndIso": range_end.isoformat(),
            "events": ai_events,
        },
    }


def build_history_payload(symbol, timeframe):
    rates = mt5.copy_rates_from_pos(symbol, timeframe, 0, 200)
    if rates is None:
        return None

    history = []
    for rate in rates:
        tick_volume = 0.0
        real_volume = 0.0
        try:
            tick_volume = float(rate["tick_volume"])
        except Exception:
            try:
                tick_volume = float(rate[5])
            except Exception:
                tick_volume = 0.0
        try:
            real_volume = float(rate["real_volume"])
        except Exception:
            try:
                real_volume = float(rate[7])
            except Exception:
                real_volume = 0.0

        history.append(
            {
                "time": int(rate[0]),
                "open": float(rate[1]),
                "high": float(rate[2]),
                "low": float(rate[3]),
                "close": float(rate[4]),
                "volume": real_volume if real_volume > 0 else tick_volume,
            }
        )

    return {
        "type": "history",
        "symbol": clean_symbol(symbol),
        "data": history,
    }


async def handle_client(websocket):
    print(f"Android connected: {websocket.remote_address}")
    current_symbol = resolve_symbol("BTCUSD")
    current_tf = mt5.TIMEFRAME_H1
    calendar_selected_date = datetime.now(LOCAL_TZ).date()
    last_calendar_refresh = 0.0
    calendar_refresh_task = None

    async def send_history(symbol, timeframe):
        payload = build_history_payload(symbol, timeframe)
        if payload is None:
            print(f"Failed to get rates for {symbol}")
            return
        await websocket.send(json.dumps(payload))

    async def send_calendar(selected_date=None, force=False):
        nonlocal calendar_selected_date, last_calendar_refresh
        if selected_date is not None:
            calendar_selected_date = selected_date

        now_monotonic = time.monotonic()
        if not force and now_monotonic - last_calendar_refresh < CALENDAR_REFRESH_SECONDS:
            return
        # Reserve refresh slot so failures do not trigger a retry storm.
        last_calendar_refresh = now_monotonic

        try:
            payload = await asyncio.to_thread(build_calendar_payload, calendar_selected_date)
            await websocket.send(json.dumps(payload, ensure_ascii=False))
        except Exception as exc:
            print(f"Calendar fetch error: {exc}")

    async def send_news():
        payload = empty_news_payload()
        try:
            payload = await asyncio.to_thread(build_news_payload)
        except Exception as exc:
            print(f"News fetch error: {exc}")
        finally:
            try:
                await websocket.send(json.dumps(payload, ensure_ascii=False))
            except Exception as send_exc:
                print(f"News send error: {send_exc}")
            await asyncio.to_thread(write_news_snapshot, payload)

    await send_history(current_symbol, current_tf)
    # Keep stream startup fast; external data fetches should not block candles/ticks.
    calendar_refresh_task = asyncio.create_task(send_calendar(force=True))
    asyncio.create_task(send_news())

    async def listen():
        nonlocal current_symbol, current_tf
        try:
            async for message in websocket:
                try:
                    data = json.loads(message)
                    action = data.get("action")

                    if action == "subscribe":
                        current_symbol = resolve_symbol(data.get("symbol", current_symbol))
                        current_tf = TIMEFRAME_MAP.get(data.get("timeframe", "1h"), mt5.TIMEFRAME_H1)
                        await send_history(current_symbol, current_tf)

                    elif action == "get_calendar":
                        await send_calendar(parse_selected_date(data.get("selectedDate")), force=True)

                    elif action == "get_news":
                        await send_news()

                    elif action == "place_order":
                        matched_symbol = resolve_symbol(data.get("symbol", ""))
                        side = data.get("type", "buy").lower()
                        order_category = data.get("orderType", "market").lower()
                        volume = float(data.get("volume", 0.01))
                        price = float(data.get("price", 0))
                        tp = float(data.get("tp", 0))
                        sl = float(data.get("sl", 0))

                        current_tick = mt5.symbol_info_tick(matched_symbol)
                        if current_tick is None:
                            await websocket.send(
                                json.dumps(
                                    {
                                        "type": "order_result",
                                        "status": "failed",
                                        "error": "No tick data",
                                    }
                                )
                            )
                            continue

                        trade_action = mt5.TRADE_ACTION_DEAL
                        mt5_type = mt5.ORDER_TYPE_BUY if side == "buy" else mt5.ORDER_TYPE_SELL
                        exec_price = current_tick.ask if side == "buy" else current_tick.bid
                        filling_type = mt5.ORDER_FILLING_IOC

                        if order_category == "limit":
                            trade_action = mt5.TRADE_ACTION_PENDING
                            mt5_type = (
                                mt5.ORDER_TYPE_BUY_LIMIT if side == "buy" else mt5.ORDER_TYPE_SELL_LIMIT
                            )
                            exec_price = price
                            filling_type = mt5.ORDER_FILLING_RETURN
                        elif order_category == "stop":
                            trade_action = mt5.TRADE_ACTION_PENDING
                            mt5_type = (
                                mt5.ORDER_TYPE_BUY_STOP if side == "buy" else mt5.ORDER_TYPE_SELL_STOP
                            )
                            exec_price = price
                            filling_type = mt5.ORDER_FILLING_RETURN
                        elif order_category == "stoplimit":
                            trade_action = mt5.TRADE_ACTION_PENDING
                            mt5_type = (
                                mt5.ORDER_TYPE_BUY_STOP_LIMIT
                                if side == "buy"
                                else mt5.ORDER_TYPE_SELL_STOP_LIMIT
                            )
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
                        if tp > 0:
                            request["tp"] = tp
                        if sl > 0:
                            request["sl"] = sl

                        result = mt5.order_send(request)
                        if result and result.retcode == mt5.TRADE_RETCODE_DONE:
                            await websocket.send(
                                json.dumps(
                                    {
                                        "type": "order_result",
                                        "status": "success",
                                        "ticket": result.order
                                        if hasattr(result, "order")
                                        else result.request.order,
                                        "price": result.price,
                                        "volume": result.volume,
                                    }
                                )
                            )
                        else:
                            error_msg = result.comment if result else "Order failed"
                            await websocket.send(
                                json.dumps(
                                    {
                                        "type": "order_result",
                                        "status": "failed",
                                        "error": error_msg,
                                    }
                                )
                            )
                        print(
                            "Order Action: "
                            f"{trade_action}, Result: {result.comment if result else 'Failed'}"
                        )

                    elif action == "close_position":
                        ticket = int(data.get("ticket"))
                        positions = mt5.positions_get(ticket=ticket)
                        if positions:
                            position = positions[0]
                            order_type = (
                                mt5.ORDER_TYPE_SELL
                                if position.type == mt5.POSITION_TYPE_BUY
                                else mt5.ORDER_TYPE_BUY
                            )
                            tick = mt5.symbol_info_tick(position.symbol)
                            price = tick.bid if position.type == mt5.POSITION_TYPE_BUY else tick.ask
                            request = {
                                "action": mt5.TRADE_ACTION_DEAL,
                                "symbol": position.symbol,
                                "volume": float(data.get("volume", position.volume)),
                                "type": order_type,
                                "position": ticket,
                                "price": price,
                                "magic": 123456,
                                "comment": "App Close",
                                "type_time": mt5.ORDER_TIME_GTC,
                                "type_filling": mt5.ORDER_FILLING_IOC,
                            }
                            mt5.order_send(request)
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
                        mt5.order_send(request)

                except Exception as exc:
                    print(f"Listen error: {exc}")
        except websockets.exceptions.ConnectionClosed:
            pass

    async def stream():
        nonlocal last_calendar_refresh, calendar_refresh_task
        while True:
            try:
                tick = mt5.symbol_info_tick(current_symbol)
                if tick:
                    await websocket.send(
                        json.dumps(
                            {
                                "type": "tick",
                                "name": clean_symbol(current_symbol),
                                "lastPrice": float(tick.bid),
                                "bid": float(tick.bid),
                                "ask": float(tick.ask),
                                "time": int(tick.time),
                                "volume": float(tick.volume),
                            }
                        )
                    )

                account = mt5.account_info()
                terminal = mt5.terminal_info()
                if account and terminal:
                    await websocket.send(
                        json.dumps(
                            {
                                "type": "account",
                                "balance": float(account.balance),
                                "equity": float(account.equity),
                                "unrealizedPnl": float(account.profit),
                                "margin": float(account.margin),
                                "availableFunds": float(account.margin_free),
                                "trade_allowed": terminal.trade_allowed,
                            }
                        )
                    )

                positions = mt5.positions_get()
                if positions is not None:
                    await websocket.send(
                        json.dumps(
                            {
                                "type": "positions",
                                "data": [
                                    {
                                        "ticket": item.ticket,
                                        "symbol": clean_symbol(item.symbol),
                                        "type": "buy"
                                        if item.type == mt5.POSITION_TYPE_BUY
                                        else "sell",
                                        "price_open": float(item.price_open),
                                        "volume_current": float(item.volume),
                                        "time_setup": int(item.time) * 1000,
                                        "tp": float(item.tp),
                                        "sl": float(item.sl),
                                        "profit": float(item.profit),
                                    }
                                    for item in positions
                                ],
                            }
                        )
                    )

                orders = mt5.orders_get()
                if orders is not None:
                    type_map = {
                        mt5.ORDER_TYPE_BUY_LIMIT: "Buy Limit",
                        mt5.ORDER_TYPE_SELL_LIMIT: "Sell Limit",
                        mt5.ORDER_TYPE_BUY_STOP: "Buy Stop",
                        mt5.ORDER_TYPE_SELL_STOP: "Sell Stop",
                        mt5.ORDER_TYPE_BUY_STOP_LIMIT: "Buy Stop Limit",
                        mt5.ORDER_TYPE_SELL_STOP_LIMIT: "Sell Stop Limit",
                    }
                    await websocket.send(
                        json.dumps(
                            {
                                "type": "orders",
                                "data": [
                                    {
                                        "ticket": item.ticket,
                                        "symbol": clean_symbol(item.symbol),
                                        "type": "buy" if item.type in [0, 2, 4, 6] else "sell",
                                        "type_name": type_map.get(item.type, "Pending"),
                                        "price_open": float(item.price_open),
                                        "volume_initial": float(item.volume_initial),
                                        "time_setup": int(item.time_setup) * 1000,
                                        "status": "Working",
                                    }
                                    for item in orders
                                ],
                            }
                        )
                    )

                if time.monotonic() - last_calendar_refresh >= CALENDAR_REFRESH_SECONDS:
                    if calendar_refresh_task is None or calendar_refresh_task.done():
                        calendar_refresh_task = asyncio.create_task(send_calendar(force=True))

            except Exception as exc:
                if isinstance(exc, websockets.exceptions.ConnectionClosed):
                    break
                print(f"Stream error: {exc}")

            await asyncio.sleep(0.5)

    try:
        await asyncio.gather(listen(), stream())
    except Exception:
        pass


async def main():
    async with websockets.serve(handle_client, "0.0.0.0", 8081):
        await asyncio.Future()


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        mt5.shutdown()
