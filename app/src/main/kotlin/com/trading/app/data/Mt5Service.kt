package com.trading.app.data

import android.util.Log
import com.google.gson.Gson
import com.trading.app.components.SymbolQuote
import okhttp3.*
import okio.ByteString

class Mt5Service(
    private val pcIpAddress: String, // User needs to provide their PC IP
    private val port: Int = 8001,
    private val onQuoteUpdate: (SymbolQuote) -> Unit
) {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    fun connect() {
        val request = Request.Builder()
            .url("ws://$pcIpAddress:$port")
            .build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("Mt5Service", "Connected to MT5 Bridge")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val quote = gson.fromJson(text, SymbolQuote::class.java)
                    onQuoteUpdate(quote)
                } catch (e: Exception) {
                    Log.e("Mt5Service", "Error parsing MT5 data: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("Mt5Service", "Connection failure: ${t.message}")
            }
        })
    }

    fun subscribe(symbol: String) {
        // Ensure the suffix 'm' is handled
        val formattedSymbol = if (symbol.endsWith("m")) symbol else "${symbol}m"
        webSocket?.send("{\"action\": \"subscribe\", \"symbol\": \"$formattedSymbol\"}")
    }

    fun disconnect() {
        webSocket?.close(1000, "App closing")
    }
}
