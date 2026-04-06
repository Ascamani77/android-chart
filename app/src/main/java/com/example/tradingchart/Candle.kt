package com.example.tradingchart

data class Candle(
    val time: Long, // epoch seconds (lightweight-charts expects seconds or ISO string)
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double = 0.0
)
