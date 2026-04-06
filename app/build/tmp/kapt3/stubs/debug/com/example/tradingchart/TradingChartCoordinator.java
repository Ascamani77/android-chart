package com.example.tradingchart;

/**
 * Coordinator: orchestration layer between Android (Kotlin) and the JS chart.
 * Keeps TradingChart.kt small: it forwards candle data, asks the JS builder for script
 * and instructs the WebView executor to run the script.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bJ\u0006\u0010\f\u001a\u00020\nJ\u0014\u0010\r\u001a\u00020\n2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fR\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/example/tradingchart/TradingChartCoordinator;", "", "jsBuilder", "Lcom/example/tradingchart/TradingChartJsBuilder;", "mapper", "Lcom/example/tradingchart/CandleMapper;", "(Lcom/example/tradingchart/TradingChartJsBuilder;Lcom/example/tradingchart/CandleMapper;)V", "executor", "Lcom/example/tradingchart/ChartJsExecutor;", "attachExecutor", "", "exec", "detachExecutor", "onNewCandles", "candles", "", "Lcom/example/tradingchart/Candle;", "app_debug"})
public final class TradingChartCoordinator {
    @org.jetbrains.annotations.NotNull()
    private final com.example.tradingchart.TradingChartJsBuilder jsBuilder = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.tradingchart.CandleMapper mapper = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile com.example.tradingchart.ChartJsExecutor executor;
    
    @javax.inject.Inject()
    public TradingChartCoordinator(@org.jetbrains.annotations.NotNull()
    com.example.tradingchart.TradingChartJsBuilder jsBuilder, @org.jetbrains.annotations.NotNull()
    com.example.tradingchart.CandleMapper mapper) {
        super();
    }
    
    public final void attachExecutor(@org.jetbrains.annotations.NotNull()
    com.example.tradingchart.ChartJsExecutor exec) {
    }
    
    public final void detachExecutor() {
    }
    
    /**
     * Called by the Android view when new candle data is available.
     * This serializes the data and asks the JS builder for the initialization script
     * (or update script). The JS executes the indicator calculations in JS.
     */
    public final void onNewCandles(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.tradingchart.Candle> candles) {
    }
}