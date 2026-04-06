package com.trading.app.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BU\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\u0002\u0010\u0012J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0005H\u00c6\u0003J\t\u0010%\u001a\u00020\u0007H\u00c6\u0003J\t\u0010&\u001a\u00020\tH\u00c6\u0003J\t\u0010\'\u001a\u00020\u000bH\u00c6\u0003J\t\u0010(\u001a\u00020\rH\u00c6\u0003J\t\u0010)\u001a\u00020\u000fH\u00c6\u0003J\t\u0010*\u001a\u00020\u0011H\u00c6\u0003JY\u0010+\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u00c6\u0001J\u0013\u0010,\u001a\u00020-2\b\u0010.\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010/\u001a\u000200H\u00d6\u0001J\t\u00101\u001a\u000202H\u00d6\u0001R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"\u00a8\u00063"}, d2 = {"Lcom/trading/app/models/ChartSettings;", "", "symbol", "Lcom/trading/app/models/SymbolSettings;", "statusLine", "Lcom/trading/app/models/StatusLineSettings;", "scales", "Lcom/trading/app/models/ScalesSettings;", "canvas", "Lcom/trading/app/models/CanvasSettings;", "trading", "Lcom/trading/app/models/TradingSettings;", "alerts", "Lcom/trading/app/models/AlertsSettings;", "events", "Lcom/trading/app/models/EventsSettings;", "quickActions", "Lcom/trading/app/models/QuickActionsSettings;", "(Lcom/trading/app/models/SymbolSettings;Lcom/trading/app/models/StatusLineSettings;Lcom/trading/app/models/ScalesSettings;Lcom/trading/app/models/CanvasSettings;Lcom/trading/app/models/TradingSettings;Lcom/trading/app/models/AlertsSettings;Lcom/trading/app/models/EventsSettings;Lcom/trading/app/models/QuickActionsSettings;)V", "getAlerts", "()Lcom/trading/app/models/AlertsSettings;", "getCanvas", "()Lcom/trading/app/models/CanvasSettings;", "getEvents", "()Lcom/trading/app/models/EventsSettings;", "getQuickActions", "()Lcom/trading/app/models/QuickActionsSettings;", "getScales", "()Lcom/trading/app/models/ScalesSettings;", "getStatusLine", "()Lcom/trading/app/models/StatusLineSettings;", "getSymbol", "()Lcom/trading/app/models/SymbolSettings;", "getTrading", "()Lcom/trading/app/models/TradingSettings;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class ChartSettings {
    @org.jetbrains.annotations.NotNull()
    private final com.trading.app.models.SymbolSettings symbol = null;
    @org.jetbrains.annotations.NotNull()
    private final com.trading.app.models.StatusLineSettings statusLine = null;
    @org.jetbrains.annotations.NotNull()
    private final com.trading.app.models.ScalesSettings scales = null;
    @org.jetbrains.annotations.NotNull()
    private final com.trading.app.models.CanvasSettings canvas = null;
    @org.jetbrains.annotations.NotNull()
    private final com.trading.app.models.TradingSettings trading = null;
    @org.jetbrains.annotations.NotNull()
    private final com.trading.app.models.AlertsSettings alerts = null;
    @org.jetbrains.annotations.NotNull()
    private final com.trading.app.models.EventsSettings events = null;
    @org.jetbrains.annotations.NotNull()
    private final com.trading.app.models.QuickActionsSettings quickActions = null;
    
    public ChartSettings(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.SymbolSettings symbol, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.StatusLineSettings statusLine, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.ScalesSettings scales, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.CanvasSettings canvas, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.TradingSettings trading, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.AlertsSettings alerts, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.EventsSettings events, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.QuickActionsSettings quickActions) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.SymbolSettings getSymbol() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.StatusLineSettings getStatusLine() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.ScalesSettings getScales() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.CanvasSettings getCanvas() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.TradingSettings getTrading() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.AlertsSettings getAlerts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.EventsSettings getEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.QuickActionsSettings getQuickActions() {
        return null;
    }
    
    public ChartSettings() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.SymbolSettings component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.StatusLineSettings component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.ScalesSettings component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.CanvasSettings component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.TradingSettings component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.AlertsSettings component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.EventsSettings component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.QuickActionsSettings component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.ChartSettings copy(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.SymbolSettings symbol, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.StatusLineSettings statusLine, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.ScalesSettings scales, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.CanvasSettings canvas, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.TradingSettings trading, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.AlertsSettings alerts, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.EventsSettings events, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.QuickActionsSettings quickActions) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}