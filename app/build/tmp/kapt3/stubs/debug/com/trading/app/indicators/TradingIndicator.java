package com.trading.app.indicators;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u001e\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\r2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\rH&R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\t\u00a8\u0006\u0011"}, d2 = {"Lcom/trading/app/indicators/TradingIndicator;", "", "color", "", "getColor", "()I", "id", "", "getId", "()Ljava/lang/String;", "name", "getName", "calculate", "", "", "candles", "Lcom/trading/app/models/OHLCData;", "app_debug"})
public abstract interface TradingIndicator {
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getId();
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getName();
    
    public abstract int getColor();
    
    /**
     * Every indicator takes the OHLC data list and returns its own data points.
     * Returns a list of the same size as the input data, with nulls where values are not yet calculated.
     */
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<java.lang.Float> calculate(@org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.OHLCData> candles);
}