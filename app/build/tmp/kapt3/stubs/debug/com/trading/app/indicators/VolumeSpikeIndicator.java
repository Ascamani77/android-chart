package com.trading.app.indicators;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0010H\u0016R\u0014\u0010\u0005\u001a\u00020\u0006X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\nX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\nX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\f\u00a8\u0006\u0013"}, d2 = {"Lcom/trading/app/indicators/VolumeSpikeIndicator;", "Lcom/trading/app/indicators/TradingIndicator;", "multiplier", "", "(F)V", "color", "", "getColor", "()I", "id", "", "getId", "()Ljava/lang/String;", "name", "getName", "calculate", "", "candles", "Lcom/trading/app/models/OHLCData;", "app_debug"})
public final class VolumeSpikeIndicator implements com.trading.app.indicators.TradingIndicator {
    private final float multiplier = 0.0F;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = "VOL_SPIKE";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = "Volume Spike";
    private final int color = 0;
    
    public VolumeSpikeIndicator(float multiplier) {
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
    
    public VolumeSpikeIndicator() {
        super();
    }
}