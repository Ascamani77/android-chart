package com.example.tradingchart;

/**
 * Lightweight TradingChart host view.
 * Responsibilities:
 * - initialize the JS WebView wrapper
 * - obtain Hilt-injected TradingChartCoordinator via EntryPoint
 * - attach/detach a small ChartJsExecutor adapter
 * - forward candle data to the coordinator
 *
 * No indicator logic here — all indicators are computed inside JS.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u0010\r\u001a\u00020\u000eH\u0014J\u0016\u0010\u000f\u001a\u00020\u000e2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u0007J\u0010\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/example/tradingchart/TradingChart;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyle", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "coordinator", "Lcom/example/tradingchart/TradingChartCoordinator;", "webView", "Landroid/webkit/WebView;", "onDetachedFromWindow", "", "setCandles", "candles", "", "Lcom/example/tradingchart/Candle;", "setupWebView", "settings", "Landroid/webkit/WebSettings;", "app_debug"})
public final class TradingChart extends android.widget.FrameLayout {
    @org.jetbrains.annotations.NotNull()
    private final android.webkit.WebView webView = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.tradingchart.TradingChartCoordinator coordinator = null;
    
    @kotlin.jvm.JvmOverloads()
    public TradingChart(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs, int defStyle) {
        super(null);
    }
    
    private final void setupWebView(android.webkit.WebSettings settings) {
    }
    
    /**
     * Forward candles to the coordinator which will serialize and call JS builder.
     */
    @androidx.annotation.MainThread()
    public final void setCandles(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.tradingchart.Candle> candles) {
    }
    
    @java.lang.Override()
    protected void onDetachedFromWindow() {
    }
    
    @kotlin.jvm.JvmOverloads()
    public TradingChart(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
    
    @kotlin.jvm.JvmOverloads()
    public TradingChart(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    android.util.AttributeSet attrs) {
        super(null);
    }
}