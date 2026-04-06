package com.example.tradingchart;

/**
 * Builds JavaScript strings to initialize / update the JS chart inside the WebView.
 * All indicator logic (RSI) lives inside the generated JS.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004\u00a8\u0006\u0006"}, d2 = {"Lcom/example/tradingchart/TradingChartJsBuilder;", "", "()V", "buildInitScript", "", "payloadJson", "app_debug"})
public final class TradingChartJsBuilder {
    
    @javax.inject.Inject()
    public TradingChartJsBuilder() {
        super();
    }
    
    /**
     * Returns a full script that will initialize the DOM (if needed), create two charts
     * (price + RSI pane), compute RSI(14) in JS from close prices and render everything.
     * The payload parameter is a JSON string with { candles: [{time,open,high,low,close,volume}, ...] }
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String buildInitScript(@org.jetbrains.annotations.NotNull()
    java.lang.String payloadJson) {
        return null;
    }
}