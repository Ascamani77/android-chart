package com.trading.app.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b:\b\u0086\b\u0018\u00002\u00020\u0001B\u00b9\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\n\u0012\b\b\u0002\u0010\u0011\u001a\u00020\n\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\n\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0017J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\t\u00100\u001a\u00020\nH\u00c6\u0003J\t\u00101\u001a\u00020\nH\u00c6\u0003J\t\u00102\u001a\u00020\u0003H\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\t\u00104\u001a\u00020\u0003H\u00c6\u0003J\t\u00105\u001a\u00020\nH\u00c6\u0003J\t\u00106\u001a\u00020\u0003H\u00c6\u0003J\t\u00107\u001a\u00020\u0003H\u00c6\u0003J\t\u00108\u001a\u00020\u0003H\u00c6\u0003J\t\u00109\u001a\u00020\u0003H\u00c6\u0003J\t\u0010:\u001a\u00020\bH\u00c6\u0003J\t\u0010;\u001a\u00020\nH\u00c6\u0003J\t\u0010<\u001a\u00020\u0003H\u00c6\u0003J\t\u0010=\u001a\u00020\u0003H\u00c6\u0003J\t\u0010>\u001a\u00020\u0003H\u00c6\u0003J\u00bd\u0001\u0010?\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\n2\b\b\u0002\u0010\u0011\u001a\u00020\n2\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\n2\b\b\u0002\u0010\u0016\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010@\u001a\u00020\u00032\b\u0010A\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010B\u001a\u00020\bH\u00d6\u0001J\t\u0010C\u001a\u00020\nH\u00d6\u0001R\u0011\u0010\u0015\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0011\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001cR\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001cR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001cR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0019R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001cR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001cR\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001cR\u0011\u0010\u0010\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0019R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001cR\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001cR\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001cR\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001cR\u0011\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001cR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001c\u00a8\u0006D"}, d2 = {"Lcom/trading/app/models/TradingSettings;", "", "buySellButtons", "", "showBuySellLabels", "oneClickTrading", "executionSound", "executionSoundVolume", "", "executionSoundType", "", "rejectionNotifications", "positionsAndOrders", "reversePositionButton", "projectOrder", "profitLossValue", "positionsMode", "bracketsMode", "executionMarks", "executionLabels", "extendedPriceLines", "alignment", "screenshotVisibility", "(ZZZZILjava/lang/String;ZZZZZLjava/lang/String;Ljava/lang/String;ZZZLjava/lang/String;Z)V", "getAlignment", "()Ljava/lang/String;", "getBracketsMode", "getBuySellButtons", "()Z", "getExecutionLabels", "getExecutionMarks", "getExecutionSound", "getExecutionSoundType", "getExecutionSoundVolume", "()I", "getExtendedPriceLines", "getOneClickTrading", "getPositionsAndOrders", "getPositionsMode", "getProfitLossValue", "getProjectOrder", "getRejectionNotifications", "getReversePositionButton", "getScreenshotVisibility", "getShowBuySellLabels", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class TradingSettings {
    private final boolean buySellButtons = false;
    private final boolean showBuySellLabels = false;
    private final boolean oneClickTrading = false;
    private final boolean executionSound = false;
    private final int executionSoundVolume = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String executionSoundType = null;
    private final boolean rejectionNotifications = false;
    private final boolean positionsAndOrders = false;
    private final boolean reversePositionButton = false;
    private final boolean projectOrder = false;
    private final boolean profitLossValue = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String positionsMode = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String bracketsMode = null;
    private final boolean executionMarks = false;
    private final boolean executionLabels = false;
    private final boolean extendedPriceLines = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String alignment = null;
    private final boolean screenshotVisibility = false;
    
    public TradingSettings(boolean buySellButtons, boolean showBuySellLabels, boolean oneClickTrading, boolean executionSound, int executionSoundVolume, @org.jetbrains.annotations.NotNull()
    java.lang.String executionSoundType, boolean rejectionNotifications, boolean positionsAndOrders, boolean reversePositionButton, boolean projectOrder, boolean profitLossValue, @org.jetbrains.annotations.NotNull()
    java.lang.String positionsMode, @org.jetbrains.annotations.NotNull()
    java.lang.String bracketsMode, boolean executionMarks, boolean executionLabels, boolean extendedPriceLines, @org.jetbrains.annotations.NotNull()
    java.lang.String alignment, boolean screenshotVisibility) {
        super();
    }
    
    public final boolean getBuySellButtons() {
        return false;
    }
    
    public final boolean getShowBuySellLabels() {
        return false;
    }
    
    public final boolean getOneClickTrading() {
        return false;
    }
    
    public final boolean getExecutionSound() {
        return false;
    }
    
    public final int getExecutionSoundVolume() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getExecutionSoundType() {
        return null;
    }
    
    public final boolean getRejectionNotifications() {
        return false;
    }
    
    public final boolean getPositionsAndOrders() {
        return false;
    }
    
    public final boolean getReversePositionButton() {
        return false;
    }
    
    public final boolean getProjectOrder() {
        return false;
    }
    
    public final boolean getProfitLossValue() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPositionsMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBracketsMode() {
        return null;
    }
    
    public final boolean getExecutionMarks() {
        return false;
    }
    
    public final boolean getExecutionLabels() {
        return false;
    }
    
    public final boolean getExtendedPriceLines() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAlignment() {
        return null;
    }
    
    public final boolean getScreenshotVisibility() {
        return false;
    }
    
    public TradingSettings() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final boolean component10() {
        return false;
    }
    
    public final boolean component11() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
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
    
    public final boolean component18() {
        return false;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final int component5() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final boolean component8() {
        return false;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.TradingSettings copy(boolean buySellButtons, boolean showBuySellLabels, boolean oneClickTrading, boolean executionSound, int executionSoundVolume, @org.jetbrains.annotations.NotNull()
    java.lang.String executionSoundType, boolean rejectionNotifications, boolean positionsAndOrders, boolean reversePositionButton, boolean projectOrder, boolean profitLossValue, @org.jetbrains.annotations.NotNull()
    java.lang.String positionsMode, @org.jetbrains.annotations.NotNull()
    java.lang.String bracketsMode, boolean executionMarks, boolean executionLabels, boolean extendedPriceLines, @org.jetbrains.annotations.NotNull()
    java.lang.String alignment, boolean screenshotVisibility) {
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