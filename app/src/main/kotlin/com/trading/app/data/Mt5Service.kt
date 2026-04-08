package com.trading.app.data

import android.util.Log
import com.google.gson.Gson
import com.trading.app.components.SymbolQuote
import com.trading.app.models.EconomicCalendarAiPayload
import com.trading.app.models.EconomicCalendarDisplayPayload
import com.trading.app.models.EconomicCalendarPayload
import com.trading.app.models.OHLCData
import okhttp3.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class Mt5Service(
    private val pcIpAddress: String = "10.222.138.133",
    private val port: Int = 8081,
    private val onHistoryUpdate: (String, List<OHLCData>) -> Unit,
    private val onQuoteUpdate: (SymbolQuote) -> Unit,
    private val onAccountUpdate: (AccountInfo) -> Unit = {},
    private val onPositionsUpdate: (List<com.trading.app.models.Position>) -> Unit = {},
    private val onOrdersUpdate: (List<com.trading.app.models.Order>) -> Unit = {},
    private val onHistoryOrdersUpdate: (List<com.trading.app.models.Order>) -> Unit = {},
    private val onBalanceHistoryUpdate: (List<com.trading.app.models.BalanceRecord>) -> Unit = {},
    private val onCalendarUpdate: (EconomicCalendarPayload) -> Unit = {}
) {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()
    private val pendingMessages = mutableListOf<String>()

    data class AccountInfo(
        val balance: Double,
        val equity: Double,
        val unrealizedPnl: Double,
        val realizedPnl: Double,
        val margin: Double,
        val availableFunds: Double,
        val ordersMargin: Double,
        val marginBuffer: Double
    )

    companion object {
        private const val TAG = "MT5_BRIDGE"
    }

    private fun cleanSymbol(symbol: String): String {
        return if (symbol.endsWith("m", ignoreCase = true)) {
            symbol.dropLast(1)
        } else {
            symbol
        }
    }

    private fun parseIsoDateToEpochSeconds(value: String): Long {
        val normalized = value.trim()
        val candidates = listOf(
            normalized,
            normalized.replace("Z", "+0000"),
            normalized.replace(Regex("([+-]\\d{2}):(\\d{2})$"), "$1$2")
        )

        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd HH:mm:ssZ",
            "yyyy-MM-dd"
        )

        for (candidate in candidates) {
            for (pattern in patterns) {
                try {
                    val formatter = SimpleDateFormat(pattern, Locale.US).apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                    }
                    return formatter.parse(candidate)?.time?.div(1000L) ?: 0L
                } catch (_: Exception) {
                    // Try the next format.
                }
            }
        }

        return 0L
    }

    fun connect() {
        val url = "ws://$pcIpAddress:$port"
        Log.d(TAG, "Connecting to $url")
        
        val request = Request.Builder()
            .url(url)
            .build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.i(TAG, "WebSocket Connected")
                synchronized(pendingMessages) {
                    pendingMessages.forEach(webSocket::send)
                    pendingMessages.clear()
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val root = JSONObject(text)
                    val type = root.optString("type")
                    
                    if (type == "history") {
                        val symbol = cleanSymbol(root.optString("symbol", root.optString("name", "")))
                        val dataArray = root.optJSONArray("data") ?: return
                        
                        val history = mutableListOf<OHLCData>()
                        fun parseTime(obj: JSONObject): Long {
                            // Try multiple possible fields and formats
                            var t = 0L
                            if (obj.has("time")) {
                                try { t = obj.getLong("time") } catch (_: Exception) {
                                    try { t = obj.getString("time").toLong() } catch (_: Exception) { t = 0L }
                                }
                            }
                            if (t == 0L && obj.has("timestamp")) {
                                try { t = obj.getLong("timestamp") } catch (_: Exception) {
                                    try { t = obj.getString("timestamp").toLong() } catch (_: Exception) { t = 0L }
                                }
                            }
                            if (t == 0L && obj.has("t")) {
                                try { t = obj.getLong("t") } catch (_: Exception) {
                                    try { t = obj.getString("t").toLong() } catch (_: Exception) { t = 0L }
                                }
                            }
                            if (t == 0L && obj.has("date")) {
                                // try ISO datetime parsing
                                try {
                                    val s = obj.getString("date")
                                    t = parseIsoDateToEpochSeconds(s)
                                } catch (_: Exception) { /* ignore */ }
                            }
                            // if t looks like milliseconds (>= 1e12), convert to seconds
                            if (t > 1000000000000L) t /= 1000L
                            return t
                        }

                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            val timeVal = parseTime(obj)
                            if (timeVal == 0L) continue

                            history.add(OHLCData(
                                time = timeVal,
                                open = obj.optDouble("open", 0.0).toFloat(),
                                high = obj.optDouble("high", 0.0).toFloat(),
                                low = obj.optDouble("low", 0.0).toFloat(),
                                close = obj.optDouble("close", 0.0).toFloat(),
                                volume = obj.optDouble(
                                    "volume",
                                    obj.optDouble(
                                        "tick_volume",
                                        obj.optDouble(
                                            "real_volume",
                                            obj.optDouble("vol", 0.0)
                                        )
                                    )
                                ).toFloat()
                            ))
                        }
                        val orderedHistory = history.sortedBy(OHLCData::time)
                        Log.d(TAG, "Parsed ${orderedHistory.size} candles for $symbol")
                        onHistoryUpdate(symbol, orderedHistory)
                    } else if (type == "tick") {
                        val symbol = cleanSymbol(root.optString("symbol", root.optString("name", "")))
                        val quote = gson.fromJson(text, SymbolQuote::class.java)
                        // Ensure name is set
                        val finalQuote = (if (quote.name.isNullOrEmpty()) quote.copy(name = symbol) else quote).copy(name = symbol)
                        onQuoteUpdate(finalQuote)
                    } else if (type == "account") {
                        val accountInfo = AccountInfo(
                            balance = root.optDouble("balance", 0.0),
                            equity = root.optDouble("equity", 0.0),
                            unrealizedPnl = root.optDouble("unrealizedPnl", 0.0),
                            realizedPnl = root.optDouble("realizedPnl", 0.0),
                            margin = root.optDouble("margin", 0.0),
                            availableFunds = root.optDouble("availableFunds", 0.0),
                            ordersMargin = root.optDouble("ordersMargin", 0.0),
                            marginBuffer = root.optDouble("marginBuffer", 0.0)
                        )
                        onAccountUpdate(accountInfo)
                    } else if (type == "positions") {
                        val dataArray = root.optJSONArray("data") ?: return
                        val positions = mutableListOf<com.trading.app.models.Position>()
                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            positions.add(com.trading.app.models.Position(
                                id = obj.optString("id", obj.optString("ticket")),
                                symbol = cleanSymbol(obj.optString("symbol")),
                                type = obj.optString("type"),
                                entryPrice = obj.optDouble("entryPrice", obj.optDouble("price_open")).toFloat(),
                                volume = obj.optDouble("volume", obj.optDouble("volume_current")).toFloat(),
                                time = obj.optLong("time", obj.optLong("time_setup")),
                                tp = if (obj.has("tp")) obj.optDouble("tp").toFloat() else null,
                                sl = if (obj.has("sl")) obj.optDouble("sl").toFloat() else null,
                                leverage = obj.optString("leverage", "1:100"),
                                margin = obj.optDouble("margin", 0.0).toFloat()
                            ))
                        }
                        onPositionsUpdate(positions)
                    } else if (type == "orders") {
                        val dataArray = root.optJSONArray("data") ?: return
                        val orders = mutableListOf<com.trading.app.models.Order>()
                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            orders.add(com.trading.app.models.Order(
                                id = obj.optString("id", obj.optString("ticket")),
                                symbol = cleanSymbol(obj.optString("symbol")),
                                type = obj.optString("type"), // buy/sell
                                orderType = obj.optString("orderType", obj.optString("type_name")), // Limit/Stop/Market
                                status = obj.optString("status", "Working"),
                                price = obj.optDouble("price", obj.optDouble("price_open")).toFloat(),
                                volume = obj.optDouble("volume", obj.optDouble("volume_initial")).toFloat(),
                                time = obj.optLong("time", obj.optLong("time_setup")),
                                leverage = obj.optString("leverage", "1:100"),
                                tp = if (obj.has("tp")) obj.optDouble("tp").toFloat() else null,
                                sl = if (obj.has("sl")) obj.optDouble("sl").toFloat() else null
                            ))
                        }
                        onOrdersUpdate(orders)
                    } else if (type == "order_history") {
                        val dataArray = root.optJSONArray("data") ?: return
                        val history = mutableListOf<com.trading.app.models.Order>()
                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            history.add(com.trading.app.models.Order(
                                id = obj.optString("id", obj.optString("ticket")),
                                symbol = cleanSymbol(obj.optString("symbol")),
                                type = obj.optString("type"),
                                orderType = obj.optString("orderType", obj.optString("type_name")),
                                status = obj.optString("status", "Filled"), // Filled/Cancelled/Rejected
                                price = obj.optDouble("price", obj.optDouble("price_open")).toFloat(),
                                volume = obj.optDouble("volume", obj.optDouble("volume_initial")).toFloat(),
                                time = obj.optLong("time", obj.optLong("time_setup")),
                                closingTime = obj.optLong("closingTime", obj.optLong("time_done")),
                                averagePrice = obj.optDouble("averagePrice", obj.optDouble("price_current")).toFloat(),
                                leverage = obj.optString("leverage", "1:100")
                            ))
                        }
                        onHistoryOrdersUpdate(history)
                    } else if (type == "balance_history") {
                        val dataArray = root.optJSONArray("data") ?: return
                        val balanceHistory = mutableListOf<com.trading.app.models.BalanceRecord>()
                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            balanceHistory.add(com.trading.app.models.BalanceRecord(
                                id = obj.optString("id", obj.optString("ticket")),
                                time = obj.optLong("time"),
                                balanceBefore = obj.optDouble("balanceBefore"),
                                balanceAfter = obj.optDouble("balanceAfter"),
                                realizedPnl = obj.optDouble("realizedPnl", obj.optDouble("profit")),
                                action = obj.optString("action", "Trade")
                            ))
                        }
                        onBalanceHistoryUpdate(balanceHistory)
                    } else if (type == "calendar") {
                        val display = gson.fromJson(
                            root.getJSONObject("display").toString(),
                            EconomicCalendarDisplayPayload::class.java
                        )
                        val ai = gson.fromJson(
                            root.getJSONObject("ai").toString(),
                            EconomicCalendarAiPayload::class.java
                        )
                        onCalendarUpdate(EconomicCalendarPayload(display = display, ai = ai))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Parse error: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket Failure: ${t.message}")
            }
        })
    }

    fun subscribe(symbol: String, timeframe: String = "1h") {
        val msg = "{\"action\": \"subscribe\", \"symbol\": \"$symbol\", \"timeframe\": \"$timeframe\"}"
        Log.d(TAG, "Subscribing to $symbol ($timeframe)")
        sendOrQueue(msg)
    }

    fun sendAction(action: String, params: Map<String, Any>) {
        val json = JSONObject()
        json.put("action", action)
        params.forEach { (key, value) ->
            json.put(key, value)
        }
        val msg = json.toString()
        Log.d(TAG, "Sending action: $msg")
        sendOrQueue(msg)
    }

    fun requestCalendar(selectedDateIso: String? = null) {
        val params = mutableMapOf<String, Any>()
        if (!selectedDateIso.isNullOrBlank()) {
            params["selectedDate"] = selectedDateIso
        }
        sendAction("get_calendar", params)
    }

    fun disconnect() {
        webSocket?.close(1000, "App closing")
        webSocket = null
    }

    private fun sendOrQueue(message: String) {
        if (webSocket?.send(message) == true) {
            return
        }

        synchronized(pendingMessages) {
            pendingMessages.add(message)
        }
    }
}
