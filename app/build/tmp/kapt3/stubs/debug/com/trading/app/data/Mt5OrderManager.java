package com.trading.app.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bJ\'\u0010\f\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\u0002\u0010\u0010J\u000e\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0012\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/trading/app/data/Mt5OrderManager;", "", "mt5Service", "Lcom/trading/app/data/Mt5Service;", "(Lcom/trading/app/data/Mt5Service;)V", "cancelOrder", "", "order", "Lcom/trading/app/models/Order;", "closePosition", "position", "Lcom/trading/app/models/Position;", "modifyPosition", "tp", "", "sl", "(Lcom/trading/app/models/Position;Ljava/lang/Float;Ljava/lang/Float;)V", "placeOrder", "placePosition", "app_debug"})
public final class Mt5OrderManager {
    @org.jetbrains.annotations.NotNull()
    private final com.trading.app.data.Mt5Service mt5Service = null;
    
    public Mt5OrderManager(@org.jetbrains.annotations.NotNull()
    com.trading.app.data.Mt5Service mt5Service) {
        super();
    }
    
    public final void placeOrder(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.Order order) {
    }
    
    public final void placePosition(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.Position position) {
    }
    
    public final void closePosition(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.Position position) {
    }
    
    public final void cancelOrder(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.Order order) {
    }
    
    public final void modifyPosition(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.Position position, @org.jetbrains.annotations.Nullable()
    java.lang.Float tp, @org.jetbrains.annotations.Nullable()
    java.lang.Float sl) {
    }
}