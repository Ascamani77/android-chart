package com.trading.app.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0002J\u001c\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fJ$\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \t*\u0004\u0018\u00010\b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/trading/app/data/ChartCache;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "cacheDir", "Ljava/io/File;", "gson", "Lcom/google/gson/Gson;", "kotlin.jvm.PlatformType", "getCacheFile", "symbol", "", "timeframe", "loadHistory", "", "Lcom/tradingview/lightweightcharts/api/series/models/CandlestickData;", "saveHistory", "", "data", "app_debug"})
public final class ChartCache {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private final java.io.File cacheDir = null;
    
    public ChartCache(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final java.io.File getCacheFile(java.lang.String symbol, java.lang.String timeframe) {
        return null;
    }
    
    public final void saveHistory(@org.jetbrains.annotations.NotNull()
    java.lang.String symbol, @org.jetbrains.annotations.NotNull()
    java.lang.String timeframe, @org.jetbrains.annotations.NotNull()
    java.util.List<com.tradingview.lightweightcharts.api.series.models.CandlestickData> data) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.tradingview.lightweightcharts.api.series.models.CandlestickData> loadHistory(@org.jetbrains.annotations.NotNull()
    java.lang.String symbol, @org.jetbrains.annotations.NotNull()
    java.lang.String timeframe) {
        return null;
    }
}