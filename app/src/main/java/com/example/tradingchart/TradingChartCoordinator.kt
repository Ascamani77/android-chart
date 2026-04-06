package com.example.tradingchart

import javax.inject.Inject

/**
 * Coordinator: orchestration layer between Android (Kotlin) and the JS chart.
 * Keeps TradingChart.kt small: it forwards candle data, asks the JS builder for script
 * and instructs the WebView executor to run the script.
 */
class TradingChartCoordinator @Inject constructor(
    private val jsBuilder: TradingChartJsBuilder,
    private val mapper: CandleMapper
) {
    @Volatile
    private var executor: ChartJsExecutor? = null

    fun attachExecutor(exec: ChartJsExecutor) {
        this.executor = exec
    }

    fun detachExecutor() {
        this.executor = null
    }

    /**
     * Called by the Android view when new candle data is available.
     * This serializes the data and asks the JS builder for the initialization script
     * (or update script). The JS executes the indicator calculations in JS.
     */
    fun onNewCandles(candles: List<Candle>) {
        val exec = executor ?: return
        val payloadJson = mapper.toJsonPayload(candles)
        val js = jsBuilder.buildInitScript(payloadJson)
        exec.evaluateJavascript(js)
    }
}
