package com.trading.app.indicators

import com.trading.app.models.OHLCData
import android.graphics.Color

class VwapIndicator : TradingIndicator {
    override val id = "VWAP"
    override val name = "VWAP"
    override val color = Color.parseColor("#F2A52C") // Orange

    override fun calculate(candles: List<OHLCData>): List<Float?> {
        if (candles.isEmpty()) return emptyList()

        val results = mutableListOf<Float?>()
        var totalPv = 0f
        var totalVolume = 0f

        for (candle in candles) {
            val typicalPrice = (candle.high + candle.low + candle.close) / 3f
            totalPv += typicalPrice * candle.volume
            totalVolume += candle.volume
            
            if (totalVolume != 0f) {
                results.add(totalPv / totalVolume)
            } else {
                results.add(typicalPrice)
            }
        }

        return results
    }
}
