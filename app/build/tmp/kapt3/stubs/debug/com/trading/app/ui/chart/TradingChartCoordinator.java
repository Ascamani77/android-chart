package com.trading.app.ui.chart;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0010H\u0007J\u0006\u0010\u0014\u001a\u00020\u0012J\u000e\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\bJ\u001e\u0010\u0017\u001a\u00020\u00122\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\b\b\u0002\u0010\u0019\u001a\u00020\u000eR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/trading/app/ui/chart/TradingChartCoordinator;", "", "jsBuilder", "Lcom/trading/app/ui/chart/TradingChartJsBuilder;", "(Lcom/trading/app/ui/chart/TradingChartJsBuilder;)V", "gson", "Lcom/google/gson/Gson;", "isJsLoaded", "", "isRsiVisible", "pendingData", "", "Lcom/trading/app/models/Candle;", "pendingRsiPeriod", "", "webView", "Landroid/webkit/WebView;", "attach", "", "view", "detach", "setRsiVisible", "visible", "updateData", "data", "rsiPeriod", "app_debug"})
public final class TradingChartCoordinator {
    @org.jetbrains.annotations.NotNull()
    private final com.trading.app.ui.chart.TradingChartJsBuilder jsBuilder = null;
    @org.jetbrains.annotations.Nullable()
    private android.webkit.WebView webView;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.Nullable()
    private java.util.List<com.trading.app.models.Candle> pendingData;
    private int pendingRsiPeriod = 14;
    private boolean isRsiVisible = false;
    private boolean isJsLoaded = false;
    
    @javax.inject.Inject()
    public TradingChartCoordinator(@org.jetbrains.annotations.NotNull()
    com.trading.app.ui.chart.TradingChartJsBuilder jsBuilder) {
        super();
    }
    
    @android.annotation.SuppressLint(value = {"SetJavaScriptEnabled"})
    public final void attach(@org.jetbrains.annotations.NotNull()
    android.webkit.WebView view) {
    }
    
    /**
     * Maps LightweightCharts CandlestickData to our internal Candle model and sends to JS
     */
    public final void updateData(@org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.Candle> data, int rsiPeriod) {
    }
    
    public final void setRsiVisible(boolean visible) {
    }
    
    public final void detach() {
    }
}