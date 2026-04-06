package com.trading.app.ui.chart;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\rH\u0007J\u0006\u0010\u0011\u001a\u00020\u000fJ\u0014\u0010\u0012\u001a\u00020\u000f2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000b0\nR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/trading/app/ui/chart/ChartCoordinator;", "", "jsBuilder", "Lcom/trading/app/ui/chart/IndicatorJsBuilder;", "(Lcom/trading/app/ui/chart/IndicatorJsBuilder;)V", "gson", "Lcom/google/gson/Gson;", "isJsLoaded", "", "pendingData", "", "Lcom/tradingview/lightweightcharts/api/series/models/CandlestickData;", "webView", "Landroid/webkit/WebView;", "attach", "", "view", "detach", "updateData", "data", "app_debug"})
public final class ChartCoordinator {
    @org.jetbrains.annotations.NotNull()
    private final com.trading.app.ui.chart.IndicatorJsBuilder jsBuilder = null;
    @org.jetbrains.annotations.Nullable()
    private android.webkit.WebView webView;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.Nullable()
    private java.util.List<com.tradingview.lightweightcharts.api.series.models.CandlestickData> pendingData;
    private boolean isJsLoaded = false;
    
    @javax.inject.Inject()
    public ChartCoordinator(@org.jetbrains.annotations.NotNull()
    com.trading.app.ui.chart.IndicatorJsBuilder jsBuilder) {
        super();
    }
    
    @android.annotation.SuppressLint(value = {"SetJavaScriptEnabled"})
    public final void attach(@org.jetbrains.annotations.NotNull()
    android.webkit.WebView view) {
    }
    
    public final void updateData(@org.jetbrains.annotations.NotNull()
    java.util.List<com.tradingview.lightweightcharts.api.series.models.CandlestickData> data) {
    }
    
    public final void detach() {
    }
}