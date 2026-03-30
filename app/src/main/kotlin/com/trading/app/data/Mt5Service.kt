package com.trading.app.data

import android.util.Log
import com.google.gson.Gson
import com.trading.app.components.SymbolQuote
import com.tradingview.lightweightcharts.api.series.models.CandlestickData
import com.tradingview.lightweightcharts.api.series.models.Time
import okhttp3.*
import org.json.JSONObject

class Mt5Service(
    private val pcIpAddress: String = "172.26.23.133",
    private val port: Int = 8081,
    private val onHistoryUpdate: (String, List<CandlestickData>) -> Unit,
    private val onQuoteUpdate: (SymbolQuote) -> Unit
) {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()
    private var pendingSubscription: String? = null

    companion object {
        private const val TAG = "MT5_BRIDGE"
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
                pendingSubscription?.let {
                    webSocket.send(it)
                    pendingSubscription = null
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val root = JSONObject(text)
                    val type = root.optString("type")
                    
                    if (type == "history") {
                        val symbol = root.optString("symbol", root.optString("name", ""))
                        val dataArray = root.optJSONArray("data") ?: return
                        
                        val history = mutableListOf<CandlestickData>()
                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            val timeVal = obj.optLong("time", 0L)
                            if (timeVal == 0L) continue
                            
                            history.add(CandlestickData(
                                time = Time.Utc(timeVal),
                                open = obj.optDouble("open", 0.0).toFloat(),
                                high = obj.optDouble("high", 0.0).toFloat(),
                                low = obj.optDouble("low", 0.0).toFloat(),
                                close = obj.optDouble("close", 0.0).toFloat()
                            ))
                        }
                        Log.d(TAG, "Parsed ${history.size} candles for $symbol")
                        onHistoryUpdate(symbol, history)
                    } else if (type == "tick") {
                        val symbol = root.optString("symbol", root.optString("name", ""))
                        val quote = gson.fromJson(text, SymbolQuote::class.java)
                        // Ensure name is set
                        val finalQuote = if (quote.name.isNullOrEmpty()) quote.copy(name = symbol) else quote
                        onQuoteUpdate(finalQuote)
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
        if (webSocket?.send(msg) != true) {
            pendingSubscription = msg
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "App closing")
        webSocket = null
    }
}
