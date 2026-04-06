package com.trading.app.indicators

import com.trading.app.models.OHLCData
import android.graphics.Color
import kotlin.math.sqrt

class BbandsIndicator(private val period: Int = 20, private val stdDev: Float = 2f) : TradingIndicator {
    override val id = "BBANDS"
    override val name = "Bollinger Bands"
    override val color = Color.parseColor("#2962FF") // Blue

    override fun calculate(candles: List<OHLCData>): List<Float?> {
        if (candles.size < period) return List(candles.size) { null }

        val results = mutableListOf<Float?>()
        val closes = candles.map { it.close }

        for (i in 0 until period - 1) {
            results.add(null)
        }

        for (i in period - 1 until closes.size) {
            val window = closes.subList(i - period + 1, i + 1)
            val sma = window.average().toFloat()
            val variance = window.map { (it - sma) * (it - sma) }.average().toFloat()
            val dev = sqrt(variance)
            
            // For simplicity in this architecture, we return the Middle Band (SMA)
            // A more complex implementation would return a data class with all 3 bands
            results.add(sma)
        }

        return results
    }
}
