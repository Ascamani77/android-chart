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
    private val onHistoryUpdate: (List<CandlestickData>) -> Unit,
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
        Log.e(TAG, "!!! connect() called !!!")
        val url = "ws://$pcIpAddress:$port"
        Log.e(TAG, "Attempting connection to: $url")
        
        val request = Request.Builder()
            .url(url)
            .build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.e(TAG, ">>> SUCCESS: WebSocket Opened! <<<")
                pendingSubscription?.let {
                    Log.d(TAG, "Sending queued sub: $it")
                    webSocket.send(it)
                    pendingSubscription = null
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                // Log only the first 100 chars to avoid flooding
                Log.d(TAG, "Msg received: ${text.take(100)}")
                try {
                    val root = JSONObject(text)
                    val type = root.optString("type")
                    
                    if (type == "history") {
                        val dataArray = root.getJSONArray("data")
                        Log.i(TAG, "Processing ${dataArray.length()} history candles")
                        val history = mutableListOf<CandlestickData>()
                        for (i in 0 until dataArray.length()) {
                            val obj = dataArray.getJSONObject(i)
                            history.add(CandlestickData(
                                time = Time.Utc(obj.getLong("time")),
                                open = obj.getDouble("open").toFloat(),
                                high = obj.getDouble("high").toFloat(),
                                low = obj.getDouble("low").toFloat(),
                                close = obj.getDouble("close").toFloat()
                            ))
                        }
                        onHistoryUpdate(history)
                    } else if (type == "tick") {
                        val quote = gson.fromJson(text, SymbolQuote::class.java)
                        onQuoteUpdate(quote)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing JSON: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "!!! CONNECTION FAILURE !!!: ${t.message}")
                t.printStackTrace()
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.w(TAG, "WebSocket Closing: $reason")
            }
        })
    }

    fun subscribe(symbol: String, timeframe: String = "1h") {
        val msg = "{\"action\": \"subscribe\", \"symbol\": \"$symbol\", \"timeframe\": \"$timeframe\"}"
        Log.e(TAG, "Subscribing: $msg")
        if (webSocket?.send(msg) != true) {
            Log.w(TAG, "Socket NOT READY, queuing sub")
            pendingSubscription = msg
        }
    }

    fun disconnect() {
        Log.e(TAG, "disconnect() called")
        webSocket?.close(1000, "App closing")
        webSocket = null
    }
}
