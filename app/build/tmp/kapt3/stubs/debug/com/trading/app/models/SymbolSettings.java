package com.trading.app.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b:\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u00b9\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0016J\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\t\u0010,\u001a\u00020\u0006H\u00c6\u0003J\t\u0010-\u001a\u00020\u0006H\u00c6\u0003J\t\u0010.\u001a\u00020\u0006H\u00c6\u0003J\t\u0010/\u001a\u00020\u0006H\u00c6\u0003J\t\u00100\u001a\u00020\u0006H\u00c6\u0003J\t\u00101\u001a\u00020\u0006H\u00c6\u0003J\t\u00102\u001a\u00020\u0006H\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\t\u00104\u001a\u00020\u0003H\u00c6\u0003J\t\u00105\u001a\u00020\u0003H\u00c6\u0003J\t\u00106\u001a\u00020\u0006H\u00c6\u0003J\t\u00107\u001a\u00020\u0006H\u00c6\u0003J\t\u00108\u001a\u00020\u0003H\u00c6\u0003J\t\u00109\u001a\u00020\u0003H\u00c6\u0003J\t\u0010:\u001a\u00020\u0006H\u00c6\u0003J\t\u0010;\u001a\u00020\u0003H\u00c6\u0003J\t\u0010<\u001a\u00020\u0003H\u00c6\u0003J\u00bd\u0001\u0010=\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00062\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00062\b\b\u0002\u0010\u000e\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00062\b\b\u0002\u0010\u0010\u001a\u00020\u00062\b\b\u0002\u0010\u0011\u001a\u00020\u00062\b\b\u0002\u0010\u0012\u001a\u00020\u00062\b\b\u0002\u0010\u0013\u001a\u00020\u00062\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010>\u001a\u00020\u00062\b\u0010?\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010@\u001a\u00020AH\u00d6\u0001J\t\u0010B\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\r\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001bR\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0018R\u0011\u0010\u0013\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0018R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001bR\u0011\u0010\u0011\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0018R\u0011\u0010\u000e\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0018R\u0011\u0010\u0012\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0018R\u0011\u0010\u0010\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0018R\u0011\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001bR\u0011\u0010\u000f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0018R\u0011\u0010\u0015\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001bR\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001bR\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001bR\u0011\u0010\n\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0018\u00a8\u0006C"}, d2 = {"Lcom/trading/app/models/SymbolSettings;", "", "upColor", "", "downColor", "bodyVisible", "", "borderVisible", "borderColorUp", "borderColorDown", "wickVisible", "wickColorUp", "wickColorDown", "barColorer", "hlcBars", "thinBars", "openVisible", "highVisible", "lowVisible", "closeVisible", "precision", "timezone", "(Ljava/lang/String;Ljava/lang/String;ZZLjava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ZZZZZZZLjava/lang/String;Ljava/lang/String;)V", "getBarColorer", "()Z", "getBodyVisible", "getBorderColorDown", "()Ljava/lang/String;", "getBorderColorUp", "getBorderVisible", "getCloseVisible", "getDownColor", "getHighVisible", "getHlcBars", "getLowVisible", "getOpenVisible", "getPrecision", "getThinBars", "getTimezone", "getUpColor", "getWickColorDown", "getWickColorUp", "getWickVisible", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class SymbolSettings {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String upColor = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String downColor = null;
    private final boolean bodyVisible = false;
    private final boolean borderVisible = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String borderColorUp = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String borderColorDown = null;
    private final boolean wickVisible = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String wickColorUp = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String wickColorDown = null;
    private final boolean barColorer = false;
    private final boolean hlcBars = false;
    private final boolean thinBars = false;
    private final boolean openVisible = false;
    private final boolean highVisible = false;
    private final boolean lowVisible = false;
    private final boolean closeVisible = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String precision = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String timezone = null;
    
    public SymbolSettings(@org.jetbrains.annotations.NotNull()
    java.lang.String upColor, @org.jetbrains.annotations.NotNull()
    java.lang.String downColor, boolean bodyVisible, boolean borderVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String borderColorUp, @org.jetbrains.annotations.NotNull()
    java.lang.String borderColorDown, boolean wickVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String wickColorUp, @org.jetbrains.annotations.NotNull()
    java.lang.String wickColorDown, boolean barColorer, boolean hlcBars, boolean thinBars, boolean openVisible, boolean highVisible, boolean lowVisible, boolean closeVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String precision, @org.jetbrains.annotations.NotNull()
    java.lang.String timezone) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUpColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDownColor() {
        return null;
    }
    
    public final boolean getBodyVisible() {
        return false;
    }
    
    public final boolean getBorderVisible() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBorderColorUp() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBorderColorDown() {
        return null;
    }
    
    public final boolean getWickVisible() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWickColorUp() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWickColorDown() {
        return null;
    }
    
    public final boolean getBarColorer() {
        return false;
    }
    
    public final boolean getHlcBars() {
        return false;
    }
    
    public final boolean getThinBars() {
        return false;
    }
    
    public final boolean getOpenVisible() {
        return false;
    }
    
    public final boolean getHighVisible() {
        return false;
    }
    
    public final boolean getLowVisible() {
        return false;
    }
    
    public final boolean getCloseVisible() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPrecision() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTimezone() {
        return null;
    }
    
    public SymbolSettings() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final boolean component10() {
        return false;
    }
    
    public final boolean component11() {
        return false;
    }
    
    public final boolean component12() {
        return false;
    }
    
    public final boolean component13() {
        return false;
    }
    
    public final boolean component14() {
        return false;
    }
    
    public final boolean component15() {
        return false;
    }
    
    public final boolean component16() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component17() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.SymbolSettings copy(@org.jetbrains.annotations.NotNull()
    java.lang.String upColor, @org.jetbrains.annotations.NotNull()
    java.lang.String downColor, boolean bodyVisible, boolean borderVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String borderColorUp, @org.jetbrains.annotations.NotNull()
    java.lang.String borderColorDown, boolean wickVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String wickColorUp, @org.jetbrains.annotations.NotNull()
    java.lang.String wickColorDown, boolean barColorer, boolean hlcBars, boolean thinBars, boolean openVisible, boolean highVisible, boolean lowVisible, boolean closeVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String precision, @org.jetbrains.annotations.NotNull()
    java.lang.String timezone) {
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