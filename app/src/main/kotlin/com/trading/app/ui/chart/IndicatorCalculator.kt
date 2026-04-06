package com.trading.app.ui.chart

import com.tradingview.lightweightcharts.api.series.models.CandlestickData
import com.tradingview.lightweightcharts.api.series.models.LineData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndicatorCalculator @Inject constructor() {

    fun calculateRsi(data: List<CandlestickData>, period: Int = 14): List<LineData> {
        if (data.size <= period) return emptyList()

        val results = mutableListOf<LineData>()
        val gains = mutableListOf<Float>()
        val losses = mutableListOf<Float>()

        for (i in 1 until data.size) {
            val change = data[i].close - data[i - 1].close
            gains.add(if (change > 0) change else 0f)
            losses.add(if (change < 0) -change else 0f)
        }

        var avgGain = gains.take(period).average().toFloat()
        var avgLoss = losses.take(period).average().toFloat()

        fun calculateRsiValue(g: Float, l: Float): Float {
            if (l == 0f) return 100f
            val rs = g / l
            return 100f - (100f / (1f + rs))
        }

        // Add first RSI value at index 'period'
        results.add(LineData(data[period].time, calculateRsiValue(avgGain, avgLoss)))

        for (i in period until gains.size) {
            avgGain = (avgGain * (period - 1) + gains[i]) / period
            avgLoss = (avgLoss * (period - 1) + losses[i]) / period
            results.add(LineData(data[i + 1].time, calculateRsiValue(avgGain, avgLoss)))
        }

        return results
    }

    fun calculateSma(data: List<LineData>, period: Int): List<LineData> {
        if (data.size < period) return emptyList()
        val results = mutableListOf<LineData>()
        for (i in period - 1 until data.size) {
            val sum = data.subList(i - period + 1, i + 1).map { it.value }.sum()
            results.add(LineData(data[i].time, sum / period))
        }
        return results
    }
}
