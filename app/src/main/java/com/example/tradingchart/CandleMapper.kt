package com.example.tradingchart

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class CandleMapper(private val gson: Gson) {
    fun toJsonPayload(candles: List<Candle>): String {
        // Map to JS-friendly objects: time as epoch seconds
        val payload = candles.map {
            mapOf(
                "time" to it.time,
                "open" to it.open,
                "high" to it.high,
                "low" to it.low,
                "close" to it.close,
                "volume" to it.volume
            )
        }
        return gson.toJson(mapOf("candles" to payload))
    }
}
