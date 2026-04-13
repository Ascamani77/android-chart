package com.trading.app.data

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.trading.app.components.SymbolQuote
import com.trading.app.models.EconomicCalendarAiPayload
import com.trading.app.models.EconomicCalendarDisplayPayload
import com.trading.app.models.EconomicCalendarPayload
import com.trading.app.models.OHLCData
import com.trading.app.models.SymbolInfo
import okhttp3.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class Mt5Service(
    private val pcIpAddress: String = "10.233.78.133",
    private val port: Int = 8081,
    private val onHistoryUpdate: (String, List<OHLCData>) -> Unit,
    private val onQuoteUpdate: (SymbolQuote) -> Unit,
    private val onAccountUpdate: (AccountInfo) -> Unit = {},
    private val onPositionsUpdate: (List<com.trading.app.models.Position>) -> Unit = {},
    private val onOrdersUpdate: (List<com.trading.app.models.Order>) -> Unit = {},
    private val onHistoryOrdersUpdate: (List<com.trading.app.models.Order>) -> Unit = {},
    private val onBalanceHistoryUpdate: (List<com.trading.app.models.BalanceRecord>) -> Unit = {},
    private val onCalendarUpdate: (EconomicCalendarPayload) -> Unit = {},
    private val onNewsUpdate: (com.trading.app.models.NewsPayload) -> Unit = {},
    private val onSymbolsUpdate: (List<SymbolInfo>) -> Unit = {},
    private val onConnectionStatusUpdate: (Boolean) -> Unit = {}
) {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var isConnecting = false
    private var isConnected = false
    private var isManuallyDisconnected = false
    private val endpointHosts: List<String> by lazy { buildEndpointHosts(pcIpAddress) }
    private var endpointIndex = 0
    private val reconnectHandler = Handler(Looper.getMainLooper())
    private val reconnectRunnable = Runnable {
        if (!isManuallyDisconnected) {
            Log.i(TAG, "Attempting WebSocket reconnect (host=${nextHost()})...")
            connect()
        }
    }
    private val gson = Gson()
    private val pendingMessages = mutableListOf<String>()
    private val brokerSymbolsByDisplayKey = mutableMapOf<String, String>()

    private inline fun dispatchToMain(crossinline action: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action()
        } else {
            reconnectHandler.post { action() }
        }
    }

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
        val upper = symbol.uppercase(Locale.US)
        val suffixes = listOf(".M", ".PRO", ".ECN", ".S", ".SPOT", "M", "+")
        for (suffix in suffixes) {
            if (upper.endsWith(suffix)) {
                return symbol.substring(0, symbol.length - suffix.length)
            }
        }
        return symbol
    }

    private fun rememberBrokerSymbol(rawSymbol: String, displaySymbol: String = cleanSymbol(rawSymbol)) {
        val normalizedRawSymbol = rawSymbol.trim()
        val normalizedDisplaySymbol = displaySymbol.trim()
        if (normalizedRawSymbol.isEmpty() || normalizedDisplaySymbol.isEmpty()) return

        synchronized(brokerSymbolsByDisplayKey) {
            brokerSymbolsByDisplayKey[normalizedDisplaySymbol.uppercase(Locale.US)] = normalizedRawSymbol
            brokerSymbolsByDisplayKey[cleanSymbol(normalizedRawSymbol).uppercase(Locale.US)] = normalizedRawSymbol
        }
    }

    private fun outboundSymbol(symbol: String): String {
        val normalizedSymbol = symbol.trim()
        if (normalizedSymbol.isEmpty()) return normalizedSymbol

        val key = cleanSymbol(normalizedSymbol).uppercase(Locale.US)
        synchronized(brokerSymbolsByDisplayKey) {
            return brokerSymbolsByDisplayKey[key] ?: normalizedSymbol
        }
    }

    private fun buildEndpointHosts(preferredHost: String): List<String> {
        val preferred = preferredHost.trim()
        val hosts = mutableListOf<String>()
        if (preferred.isNotBlank()) {
            hosts.add(preferred)
        }
        hosts.add("127.0.0.1")
        hosts.add("10.0.2.2")
        hosts.add("localhost")
        return hosts.distinctBy { it.lowercase(Locale.US) }
    }

    private fun nextHost(): String {
        val index = endpointIndex.mod(endpointHosts.size)
        return endpointHosts[index]
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
        if (isConnecting) return
        isManuallyDisconnected = false
        reconnectHandler.removeCallbacks(reconnectRunnable)

        val host = nextHost()
        val url = "ws://$host:$port"
        Log.d(TAG, "Connecting to $url (candidate ${endpointIndex + 1}/${endpointHosts.size})")
        
        val request = Request.Builder()
            .url(url)
            .build()
        isConnecting = true
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.i(TAG, "WebSocket Connected on $host")
                isConnecting = false
                isConnected = true
                dispatchToMain { onConnectionStatusUpdate(true) }
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
                        val rawSymbol = root.optString("symbol", root.optString("name", ""))
                        val symbol = cleanSymbol(rawSymbol)
                        rememberBrokerSymbol(rawSymbol, symbol)
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
                        dispatchToMain {
                            onHistoryUpdate(symbol, orderedHistory)
                        }
                    } else if (type == "tick") {
                        val rawSymbol = root.optString("symbol", root.optString("name", ""))
                        val symbol = cleanSymbol(rawSymbol)
                        rememberBrokerSymbol(rawSymbol, symbol)
                        val quote = gson.fromJson(text, SymbolQuote::class.java)
                        // Ensure name is set
                        val finalQuote = (if (quote.name.isNullOrEmpty()) quote.copy(name = symbol) else quote).copy(name = symbol)
                        dispatchToMain {
                            onQuoteUpdate(finalQuote)
                        }
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
                        dispatchToMain {
                            onAccountUpdate(accountInfo)
                        }
                    } else if (type == "positions") {
                        val dataArray = root.optJSONArray("data") ?: return
                        val positions = mutableListOf<com.trading.app.models.Position>()
                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            val rawSymbol = obj.optString("symbol")
                            rememberBrokerSymbol(rawSymbol)
                            positions.add(com.trading.app.models.Position(
                                id = obj.optString("id", obj.optString("ticket")),
                                symbol = cleanSymbol(rawSymbol),
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
                        dispatchToMain {
                            onPositionsUpdate(positions)
                        }
                    } else if (type == "orders") {
                        val dataArray = root.optJSONArray("data") ?: return
                        val orders = mutableListOf<com.trading.app.models.Order>()
                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            val rawSymbol = obj.optString("symbol")
                            rememberBrokerSymbol(rawSymbol)
                            orders.add(com.trading.app.models.Order(
                                id = obj.optString("id", obj.optString("ticket")),
                                symbol = cleanSymbol(rawSymbol),
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
                        dispatchToMain {
                            onOrdersUpdate(orders)
                        }
                    } else if (type == "order_history") {
                        val dataArray = root.optJSONArray("data") ?: return
                        val history = mutableListOf<com.trading.app.models.Order>()
                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            val rawSymbol = obj.optString("symbol")
                            rememberBrokerSymbol(rawSymbol)
                            history.add(com.trading.app.models.Order(
                                id = obj.optString("id", obj.optString("ticket")),
                                symbol = cleanSymbol(rawSymbol),
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
                        dispatchToMain {
                            onHistoryOrdersUpdate(history)
                        }
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
                        dispatchToMain {
                            onBalanceHistoryUpdate(balanceHistory)
                        }
                    } else if (type == "calendar") {
                        val display = gson.fromJson(
                            root.getJSONObject("display").toString(),
                            EconomicCalendarDisplayPayload::class.java
                        )
                        val ai = gson.fromJson(
                            root.getJSONObject("ai").toString(),
                            EconomicCalendarAiPayload::class.java
                        )
                        dispatchToMain {
                            onCalendarUpdate(EconomicCalendarPayload(display = display, ai = ai))
                        }
                    } else if (type == "news") {
                        val newsPayload = gson.fromJson(text, com.trading.app.models.NewsPayload::class.java)
                        dispatchToMain {
                            onNewsUpdate(newsPayload)
                        }
                    } else if (type == "symbols") {
                        val dataArray = root.optJSONArray("data") ?: root.optJSONArray("items") ?: return
                        val symbols = mutableListOf<SymbolInfo>()
                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.optJSONObject(i) ?: continue
                            val rawSymbol = obj.optString("symbol", obj.optString("ticker", ""))
                            val ticker = cleanSymbol(obj.optString("ticker", rawSymbol))
                            if (ticker.isBlank()) continue
                            rememberBrokerSymbol(rawSymbol, ticker)
                            symbols.add(
                                SymbolInfo(
                                    ticker = ticker,
                                    name = obj.optString("name", ticker),
                                    exchange = "",
                                    type = obj.optString("type", "forex"),
                                    brokerSymbol = rawSymbol.ifBlank { ticker }
                                )
                            )
                        }
                        dispatchToMain {
                            onSymbolsUpdate(symbols)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Parse error: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket Failure on $host: ${t.message}")
                this@Mt5Service.webSocket = null
                isConnecting = false
                isConnected = false
                dispatchToMain { onConnectionStatusUpdate(false) }
                scheduleReconnect()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.w(TAG, "WebSocket Closed on $host: code=$code reason=$reason")
                this@Mt5Service.webSocket = null
                isConnecting = false
                isConnected = false
                dispatchToMain { onConnectionStatusUpdate(false) }
                scheduleReconnect()
            }
        })
    }

    fun subscribe(symbol: String, timeframe: String = "1h") {
        val brokerSymbol = outboundSymbol(symbol)
        val msg = "{\"action\": \"subscribe\", \"symbol\": \"$brokerSymbol\", \"timeframe\": \"$timeframe\"}"
        Log.d(TAG, "Subscribing to $brokerSymbol ($timeframe)")
        sendOrQueue(msg)
    }

    fun updateWatchlist(symbols: List<String>) {
        val normalized = symbols
            .asSequence()
            .map { outboundSymbol(it).trim() }
            .filter { it.isNotEmpty() }
            .distinctBy { it.uppercase(Locale.US) }
            .toList()

        if (normalized.isEmpty()) return
        sendAction("watchlist_update", mapOf("symbols" to normalized))
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

    fun requestNews() {
        sendAction("get_news", emptyMap())
    }

    fun requestSymbols() {
        sendAction("get_symbols", emptyMap())
    }

    fun disconnect() {
        isManuallyDisconnected = true
        reconnectHandler.removeCallbacks(reconnectRunnable)
        webSocket?.close(1000, "App closing")
        webSocket = null
        isConnecting = false
    }

    private fun sendOrQueue(message: String) {
        if (webSocket?.send(message) == true) {
            return
        }

        synchronized(pendingMessages) {
            pendingMessages.add(message)
        }
    }

    private fun scheduleReconnect() {
        if (isManuallyDisconnected) return
        if (endpointHosts.size > 1) {
            endpointIndex = (endpointIndex + 1) % endpointHosts.size
        }
        reconnectHandler.removeCallbacks(reconnectRunnable)
        reconnectHandler.postDelayed(reconnectRunnable, 2000L)
    }
}
