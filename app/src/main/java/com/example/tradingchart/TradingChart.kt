package com.example.tradingchart

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.annotation.MainThread
import dagger.hilt.android.EntryPointAccessors

/**
 * Lightweight TradingChart host view.
 * Responsibilities:
 *  - initialize the JS WebView wrapper
 *  - obtain Hilt-injected TradingChartCoordinator via EntryPoint
 *  - attach/detach a small ChartJsExecutor adapter
 *  - forward candle data to the coordinator
 *
 * No indicator logic here — all indicators are computed inside JS.
 */
class TradingChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val webView: WebView
    private val coordinator: TradingChartCoordinator

    init {
        // obtain Hilt-provided coordinator via entrypoint
        val entryPoint = EntryPointAccessors.fromApplication(context.applicationContext, TradingChartViewEntryPoint::class.java)
        coordinator = entryPoint.getCoordinator()

        webView = WebView(context.applicationContext)
        setupWebView(webView.settings)
        addView(webView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        // load local asset HTML that includes lightweight-charts and provides containers
        webView.loadUrl("file:///android_asset/chart_host.html")

        // attach executor adapter
        coordinator.attachExecutor(object : ChartJsExecutor {
            override fun evaluateJavascript(script: String) {
                // always run on UI thread
                post {
                    webView.evaluateJavascript(script, null)
                }
            }
        })
    }

    private fun setupWebView(settings: WebSettings) {
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.loadsImagesAutomatically = true
        // further hardening/configuration as needed
    }

    /**
     * Forward candles to the coordinator which will serialize and call JS builder.
     */
    @MainThread
    fun setCandles(candles: List<Candle>) {
        coordinator.onNewCandles(candles)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        coordinator.detachExecutor()
        webView.stopLoading()
        webView.removeAllViews()
        webView.destroy()
    }
}
