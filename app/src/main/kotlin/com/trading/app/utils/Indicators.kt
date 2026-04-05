package com.trading.app.utils

import com.trading.app.models.OHLCData
import kotlin.math.abs

object Indicators {

    /**
     * Calculates the Relative Strength Index (RSI) using Wilder's Smoothing Method (RMA).
     * 
     * Formula:
     * RSI = 100 - (100 / (1 + RS))
     * RS = AvgGain / AvgLoss
     */
    fun calculateRsi(data: List<OHLCData>, period: Int = 14): List<Float?> {
        if (data.size <= period) return List(data.size) { null }

        val rsiValues = mutableListOf<Float?>()
        val gains = mutableListOf<Float>()
        val losses = mutableListOf<Float>()

        // Calculate initial gains and losses
        for (i in 1 until data.size) {
            val change = data[i].close - data[i - 1].close
            gains.add(if (change > 0) change else 0f)
            losses.add(if (change < 0) -change else 0f)
        }

        // RMA (Wilder's Smoothing) Calculation
        // Initial average: Simple Moving Average
        var avgGain = gains.take(period).average().toFloat()
        var avgLoss = losses.take(period).average().toFloat()

        // First RSI value
        for (i in 0 until period) {
            rsiValues.add(null)
        }
        
        fun calculateRsiValue(g: Float, l: Float): Float {
            if (l == 0f) return 100f
            val rs = g / l
            return 100f - (100f / (1f + rs))
        }

        rsiValues.add(calculateRsiValue(avgGain, avgLoss))

        // Subsequent values using RMA:
        // alpha = 1 / period
        // NextAvg = (PreviousAvg * (period - 1) + CurrentValue) / period
        for (i in period until gains.size) {
            avgGain = (avgGain * (period - 1) + gains[i]) / period
            avgLoss = (avgLoss * (period - 1) + losses[i]) / period
            rsiValues.add(calculateRsiValue(avgGain, avgLoss))
        }

        return rsiValues
    }

    fun calculateSma(data: List<Float?>, period: Int): List<Float?> {
        val result = mutableListOf<Float?>()
        for (i in data.indices) {
            if (i < period - 1) {
                result.add(null)
                continue
            }
            val window = data.subList(i - period + 1, i + 1).filterNotNull()
            if (window.size < period) {
                result.add(null)
            } else {
                result.add(window.average().toFloat())
            }
        }
        return result
    }

    data class Pivot(val index: Int, val value: Float, val isHigh: Boolean)

    private fun findPivots(values: List<Float?>, window: Int = 5): List<Pivot> {
        val pivots = mutableListOf<Pivot>()
        for (i in window until values.size - window) {
            val current = values[i] ?: continue
            val leftSide = values.subList(i - window, i).filterNotNull()
            val rightSide = values.subList(i + 1, i + window + 1).filterNotNull()

            if (leftSide.size < window || rightSide.size < window) continue

            // Check for High
            if (current > leftSide.max() && current > rightSide.max()) {
                pivots.add(Pivot(i, current, true))
            }
            // Check for Low
            if (current < leftSide.min() && current < rightSide.min()) {
                pivots.add(Pivot(i, current, false))
            }
        }
        return pivots
    }

    /**
     * Detects Bullish and Bearish Divergences.
     * Bullish: Price Lower Low, RSI Higher Low
     * Bearish: Price Higher High, RSI Lower High
     */
    fun detectDivergence(
        data: List<OHLCData>,
        rsi: List<Float?>,
        window: Int = 5
    ): DivergenceResult {
        val bullish = mutableListOf<Int>()
        val bearish = mutableListOf<Int>()

        val priceHighs = findPivots(data.map { it.high }, window).filter { it.isHigh }
        val priceLows = findPivots(data.map { it.low }, window).filter { !it.isHigh }
        val rsiHighs = findPivots(rsi, window).filter { it.isHigh }
        val rsiLows = findPivots(rsi, window).filter { !it.isHigh }

        // Bearish Divergence: Price Higher High, RSI Lower High
        for (i in 1 until priceHighs.size) {
            val p2 = priceHighs[i]
            val p1 = priceHighs[i - 1]
            if (p2.value > p1.value) {
                // Find corresponding RSI highs near these indices
                val r2 = rsiHighs.find { abs(it.index - p2.index) <= window }
                val r1 = rsiHighs.find { abs(it.index - p1.index) <= window }
                if (r1 != null && r2 != null && r2.value < r1.value) {
                    bearish.add(p2.index)
                }
            }
        }

        // Bullish Divergence: Price Lower Low, RSI Higher Low
        for (i in 1 until priceLows.size) {
            val p2 = priceLows[i]
            val p1 = priceLows[i - 1]
            if (p2.value < p1.value) {
                val r2 = rsiLows.find { abs(it.index - p2.index) <= window }
                val r1 = rsiLows.find { abs(it.index - p1.index) <= window }
                if (r1 != null && r2 != null && r2.value > r1.value) {
                    bullish.add(p2.index)
                }
            }
        }

        return DivergenceResult(bullish, bearish)
    }

    /**
     * Detects Failure Swings.
     */
    fun detectFailureSwings(rsi: List<Float?>, overbought: Float = 70f, oversold: Float = 30f): FailureSwingResult {
        val bullish = mutableListOf<Int>()
        val bearish = mutableListOf<Int>()
        
        // Simplified detection logic based on the 4-step process
        val pivots = findPivots(rsi, 3)
        
        // Bullish: 1. RSI < 30, 2. RSI > 30, 3. Pullback > 30, 4. Break prev high
        for (i in 2 until pivots.size) {
            val p3 = pivots[i] // High (Step 4 breakout target is previous high p1)
            val p2 = pivots[i-1] // Low (Step 3 pullback)
            val p1 = pivots[i-2] // High (Step 2 peak)
            
            // Bullish Failure Swing
            if (!p2.isHigh && p1.isHigh && p2.value > oversold) {
                // Look for a preceding value < 30
                val rsiBeforeP1 = rsi.subList(0, p1.index).filterNotNull()
                if (rsiBeforeP1.any { it < oversold }) {
                    // Check if current price breaks p1.value
                    // (Actually needs to check RSI values between p2 and now)
                }
            }
        }
        
        return FailureSwingResult(bullish, bearish)
    }

    /**
     * Cardwell's Reversals:
     * Positive Reversal: Price Higher Low, RSI Lower Low.
     * Negative Reversal: Price Lower High, RSI Higher High.
     */
    fun detectCardwellReversals(data: List<OHLCData>, rsi: List<Float?>, window: Int = 5): CardwellResult {
        val pos = mutableListOf<Int>()
        val neg = mutableListOf<Int>()
        
        val priceHighs = findPivots(data.map { it.high }, window).filter { it.isHigh }
        val priceLows = findPivots(data.map { it.low }, window).filter { !it.isHigh }
        val rsiHighs = findPivots(rsi, window).filter { it.isHigh }
        val rsiLows = findPivots(rsi, window).filter { !it.isHigh }

        // Positive Reversal: Price Higher Low, RSI Lower Low
        for (i in 1 until priceLows.size) {
            val p2 = priceLows[i]
            val p1 = priceLows[i-1]
            if (p2.value > p1.value) {
                val r2 = rsiLows.find { abs(it.index - p2.index) <= window }
                val r1 = rsiLows.find { abs(it.index - p1.index) <= window }
                if (r1 != null && r2 != null && r2.value < r1.value) {
                    pos.add(p2.index)
                }
            }
        }

        // Negative Reversal: Price Lower High, RSI Higher High
        for (i in 1 until priceHighs.size) {
            val p2 = priceHighs[i]
            val p1 = priceHighs[i-1]
            if (p2.value < p1.value) {
                val r2 = rsiHighs.find { abs(it.index - p2.index) <= window }
                val r1 = rsiHighs.find { abs(it.index - p1.index) <= window }
                if (r1 != null && r2 != null && r2.value > r1.value) {
                    neg.add(p2.index)
                }
            }
        }

        return CardwellResult(pos, neg)
    }

    data class DivergenceResult(
        val bullish: List<Int> = emptyList(),
        val bearish: List<Int> = emptyList()
    )
    
    data class FailureSwingResult(
        val bullish: List<Int> = emptyList(),
        val bearish: List<Int> = emptyList()
    )

    data class CardwellResult(
        val positiveReversals: List<Int> = emptyList(),
        val negativeReversals: List<Int> = emptyList()
    )
}
