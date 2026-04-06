package com.example.tradingchart

/**
 * Adapter interface for executing JS inside the chart wrapper (WebView).
 */
interface ChartJsExecutor {
    fun evaluateJavascript(script: String)
}
