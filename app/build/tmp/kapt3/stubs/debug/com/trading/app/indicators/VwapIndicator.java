package com.trading.app.indicators;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0012H\u0016J\u0014\u0010\u0015\u001a\u00020\u00162\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\fX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\fX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/trading/app/indicators/VwapIndicator;", "Lcom/trading/app/indicators/TradingIndicator;", "bandMultiplier", "", "resetOnNewSession", "", "(FZ)V", "color", "", "getColor", "()I", "id", "", "getId", "()Ljava/lang/String;", "name", "getName", "calculate", "", "candles", "Lcom/trading/app/models/OHLCData;", "calculateBands", "Lcom/trading/app/indicators/VwapData;", "app_debug"})
public final class VwapIndicator implements com.trading.app.indicators.TradingIndicator {
    private final float bandMultiplier = 0.0F;
    private final boolean resetOnNewSession = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = "VWAP";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = "VWAP";
    private final int color = 0;
    
    public VwapIndicator(float bandMultiplier, boolean resetOnNewSession) {
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
    public final com.trading.app.indicators.VwapData calculateBands(@org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.OHLCData> candles) {
        return null;
    }
    
    public VwapIndicator() {
        super();
    }
}