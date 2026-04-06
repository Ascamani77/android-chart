package com.trading.app.indicators;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B#\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u00112\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0011H\u0016J&\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u00112\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u0017\u001a\u00020\u0003H\u0002J.\u0010\u0018\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u00112\u000e\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u00112\u000e\u0010\u001a\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0011J\u001c\u0010\u001b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u00112\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0011J\u001e\u0010\u001c\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u00112\u000e\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0011R\u0014\u0010\u0007\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000bX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u000bX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u000e\u0010\u0005\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/trading/app/indicators/MacdIndicator;", "Lcom/trading/app/indicators/TradingIndicator;", "fastPeriod", "", "slowPeriod", "signalPeriod", "(III)V", "color", "getColor", "()I", "id", "", "getId", "()Ljava/lang/String;", "name", "getName", "calculate", "", "", "candles", "Lcom/trading/app/models/OHLCData;", "calculateEma", "values", "period", "calculateHistogram", "macdLine", "signalLine", "calculateMacdLine", "calculateSignalLine", "app_debug"})
public final class MacdIndicator implements com.trading.app.indicators.TradingIndicator {
    private final int fastPeriod = 0;
    private final int slowPeriod = 0;
    private final int signalPeriod = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = "MACD";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = "MACD";
    private final int color = 0;
    
    public MacdIndicator(int fastPeriod, int slowPeriod, int signalPeriod) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getId() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getName() {
        return null;
    }
    
    @java.lang.Override()
    public int getColor() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.util.List<java.lang.Float> calculate(@org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.OHLCData> candles) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Float> calculateMacdLine(@org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.OHLCData> candles) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Float> calculateSignalLine(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Float> macdLine) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Float> calculateHistogram(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Float> macdLine, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Float> signalLine) {
        return null;
    }
    
    private final java.util.List<java.lang.Float> calculateEma(java.util.List<java.lang.Float> values, int period) {
        return null;
    }
    
    public MacdIndicator() {
        super();
    }
}