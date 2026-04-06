package com.trading.app.indicators

import com.trading.app.models.OHLCData
import android.graphics.Color

class RsiIndicator(private val period: Int = 14, private val maPeriod: Int = 14) : TradingIndicator {
    override val id = "RSI"
    override val name = "RSI"
    override val color = Color.parseColor("#7E57C2") // Purple

    override fun calculate(candles: List<OHLCData>): List<Float?> {
        if (candles.size <= period) return List(candles.size) { null }

        val rsiValues = mutableListOf<Float?>()
        val gains = mutableListOf<Float>()
        val losses = mutableListOf<Float>()

        for (i in 1 until candles.size) {
            val change = candles[i].close - candles[i - 1].close
            gains.add(if (change > 0) change else 0f)
            losses.add(if (change < 0) -change else 0f)
        }

        var avgGain = gains.take(period).average().toFloat()
        var avgLoss = losses.take(period).average().toFloat()

        for (i in 0 until period) {
            rsiValues.add(null)
        }
        
        fun calculateRsiValue(g: Float, l: Float): Float {
            if (l == 0f) return 100f
            val rs = g / l
            return 100f - (100f / (1f + rs))
        }

        rsiValues.add(calculateRsiValue(avgGain, avgLoss))

        for (i in period until gains.size) {
            avgGain = (avgGain * (period - 1) + gains[i]) / period
            avgLoss = (avgLoss * (period - 1) + losses[i]) / period
            rsiValues.add(calculateRsiValue(avgGain, avgLoss))
        }

        return rsiValues
    }

    fun calculateMa(rsiValues: List<Float?>): List<Float?> {
        val maValues = mutableListOf<Float?>()
        for (i in rsiValues.indices) {
            if (i < maPeriod - 1) {
                maValues.add(null)
                continue
            }
            
            val subList = rsiValues.subList(i - maPeriod + 1, i + 1)
            if (subList.any { it == null }) {
                maValues.add(null)
            } else {
                maValues.add(subList.filterNotNull().average().toFloat())
            }
        }
        return maValues
    }
}
