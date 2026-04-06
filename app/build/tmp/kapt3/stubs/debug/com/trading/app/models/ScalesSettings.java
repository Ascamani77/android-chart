package com.trading.app.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\bp\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u00ed\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0006\u0012\b\b\u0002\u0010\n\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u001d\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u001f\u001a\u00020\u0006\u0012\b\b\u0002\u0010 \u001a\u00020\u0003\u0012\b\b\u0002\u0010!\u001a\u00020\u0003\u0012\b\b\u0002\u0010\"\u001a\u00020\u0006\u0012\b\b\u0002\u0010#\u001a\u00020\u0006\u0012\b\b\u0002\u0010$\u001a\u00020\u0006\u0012\b\b\u0002\u0010%\u001a\u00020\u0006\u0012\b\b\u0002\u0010&\u001a\u00020\u0006\u0012\b\b\u0002\u0010\'\u001a\u00020\u0006\u00a2\u0006\u0002\u0010(J\t\u0010O\u001a\u00020\u0003H\u00c6\u0003J\t\u0010P\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Q\u001a\u00020\u0003H\u00c6\u0003J\t\u0010R\u001a\u00020\u0003H\u00c6\u0003J\t\u0010S\u001a\u00020\u0003H\u00c6\u0003J\t\u0010T\u001a\u00020\u0003H\u00c6\u0003J\t\u0010U\u001a\u00020\u0003H\u00c6\u0003J\t\u0010V\u001a\u00020\u0003H\u00c6\u0003J\t\u0010W\u001a\u00020\u0003H\u00c6\u0003J\t\u0010X\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0003H\u00c6\u0003J\t\u0010[\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\\\u001a\u00020\u0006H\u00c6\u0003J\t\u0010]\u001a\u00020\u0006H\u00c6\u0003J\t\u0010^\u001a\u00020\u0006H\u00c6\u0003J\t\u0010_\u001a\u00020\u0006H\u00c6\u0003J\t\u0010`\u001a\u00020\u0006H\u00c6\u0003J\t\u0010a\u001a\u00020\u0006H\u00c6\u0003J\t\u0010b\u001a\u00020\u0006H\u00c6\u0003J\t\u0010c\u001a\u00020\u0006H\u00c6\u0003J\t\u0010d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010e\u001a\u00020\u0006H\u00c6\u0003J\t\u0010f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010g\u001a\u00020\u0006H\u00c6\u0003J\t\u0010h\u001a\u00020\u0006H\u00c6\u0003J\t\u0010i\u001a\u00020\u0006H\u00c6\u0003J\t\u0010j\u001a\u00020\u0006H\u00c6\u0003J\t\u0010k\u001a\u00020\u0006H\u00c6\u0003J\t\u0010l\u001a\u00020\u0006H\u00c6\u0003J\t\u0010m\u001a\u00020\u0003H\u00c6\u0003J\t\u0010n\u001a\u00020\u0003H\u00c6\u0003J\t\u0010o\u001a\u00020\u0006H\u00c6\u0003J\t\u0010p\u001a\u00020\u0006H\u00c6\u0003J\t\u0010q\u001a\u00020\u0006H\u00c6\u0003J\t\u0010r\u001a\u00020\u0003H\u00c6\u0003J\u00f1\u0002\u0010s\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\n\u001a\u00020\u00062\b\b\u0002\u0010\u000b\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00032\b\b\u0002\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u0017\u001a\u00020\u00062\b\b\u0002\u0010\u0018\u001a\u00020\u00062\b\b\u0002\u0010\u0019\u001a\u00020\u00062\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\u001b\u001a\u00020\u00062\b\b\u0002\u0010\u001c\u001a\u00020\u00062\b\b\u0002\u0010\u001d\u001a\u00020\u00062\b\b\u0002\u0010\u001e\u001a\u00020\u00062\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010 \u001a\u00020\u00032\b\b\u0002\u0010!\u001a\u00020\u00032\b\b\u0002\u0010\"\u001a\u00020\u00062\b\b\u0002\u0010#\u001a\u00020\u00062\b\b\u0002\u0010$\u001a\u00020\u00062\b\b\u0002\u0010%\u001a\u00020\u00062\b\b\u0002\u0010&\u001a\u00020\u00062\b\b\u0002\u0010\'\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010t\u001a\u00020\u00062\b\u0010u\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010v\u001a\u00020wH\u00d6\u0001J\t\u0010x\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0017\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0011\u0010\u001e\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010,R\u0011\u0010\u0014\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010*R\u0011\u0010\u0015\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010*R\u0011\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010,R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010*R\u0011\u0010 \u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010*R\u0011\u0010\u001f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010,R\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010*R\u0011\u0010\u0011\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010*R\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010*R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010*R\u0011\u0010\'\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010,R\u0011\u0010\u001d\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010,R\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010*R\u0011\u0010\u0018\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010,R\u0011\u0010\u0019\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010,R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010,R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010*R\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010,R\u0011\u0010\n\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010,R\u0011\u0010&\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010,R\u0011\u0010\u001c\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010,R\u0011\u0010\"\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010,R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010*R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u0010*R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bF\u0010*R\u0011\u0010$\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010,R\u0011\u0010\u001a\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010,R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010*R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bJ\u0010*R\u0011\u0010#\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u0010,R\u0011\u0010%\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\bL\u0010,R\u0011\u0010\u001b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\bM\u0010,R\u0011\u0010!\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u0010*\u00a8\u0006y"}, d2 = {"Lcom/trading/app/models/ScalesSettings;", "", "currencyAndUnit", "", "scaleModes", "lockRatio", "", "lockRatioValue", "scalesPlacement", "noOverlappingLabels", "plusButton", "countdown", "symbolLabel", "symbolLineColor", "symbolLastValueMode", "highLowMode", "highLowLineColor", "highLowLabelColor", "highLowCalculationMode", "indicatorsAndFinancials", "bidAskMode", "bidColor", "askColor", "bidAskLabels", "indicatorsAndFinancialsNameLabels", "indicatorsAndFinancialsValueLabels", "symbolLastPriceLine", "symbolPrevCloseLine", "prePostMarketPriceLine", "highLowPriceLines", "bidAskLines", "dayOfWeekOnLabels", "dateFormat", "timeFormat", "saveLeftEdge", "symbolNameLabel", "symbolLastPriceLabel", "symbolPrevCloseLabel", "prePostMarketPriceLabel", "highLowPriceLabels", "(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ZZZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZZZZZZZZLjava/lang/String;Ljava/lang/String;ZZZZZZ)V", "getAskColor", "()Ljava/lang/String;", "getBidAskLabels", "()Z", "getBidAskLines", "getBidAskMode", "getBidColor", "getCountdown", "getCurrencyAndUnit", "getDateFormat", "getDayOfWeekOnLabels", "getHighLowCalculationMode", "getHighLowLabelColor", "getHighLowLineColor", "getHighLowMode", "getHighLowPriceLabels", "getHighLowPriceLines", "getIndicatorsAndFinancials", "getIndicatorsAndFinancialsNameLabels", "getIndicatorsAndFinancialsValueLabels", "getLockRatio", "getLockRatioValue", "getNoOverlappingLabels", "getPlusButton", "getPrePostMarketPriceLabel", "getPrePostMarketPriceLine", "getSaveLeftEdge", "getScaleModes", "getScalesPlacement", "getSymbolLabel", "getSymbolLastPriceLabel", "getSymbolLastPriceLine", "getSymbolLastValueMode", "getSymbolLineColor", "getSymbolNameLabel", "getSymbolPrevCloseLabel", "getSymbolPrevCloseLine", "getTimeFormat", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component30", "component31", "component32", "component33", "component34", "component35", "component36", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class ScalesSettings {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String currencyAndUnit = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String scaleModes = null;
    private final boolean lockRatio = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String lockRatioValue = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String scalesPlacement = null;
    private final boolean noOverlappingLabels = false;
    private final boolean plusButton = false;
    private final boolean countdown = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String symbolLabel = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String symbolLineColor = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String symbolLastValueMode = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String highLowMode = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String highLowLineColor = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String highLowLabelColor = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String highLowCalculationMode = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String indicatorsAndFinancials = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String bidAskMode = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String bidColor = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String askColor = null;
    private final boolean bidAskLabels = false;
    private final boolean indicatorsAndFinancialsNameLabels = false;
    private final boolean indicatorsAndFinancialsValueLabels = false;
    private final boolean symbolLastPriceLine = false;
    private final boolean symbolPrevCloseLine = false;
    private final boolean prePostMarketPriceLine = false;
    private final boolean highLowPriceLines = false;
    private final boolean bidAskLines = false;
    private final boolean dayOfWeekOnLabels = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String dateFormat = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String timeFormat = null;
    private final boolean saveLeftEdge = false;
    private final boolean symbolNameLabel = false;
    private final boolean symbolLastPriceLabel = false;
    private final boolean symbolPrevCloseLabel = false;
    private final boolean prePostMarketPriceLabel = false;
    private final boolean highLowPriceLabels = false;
    
    public ScalesSettings(@org.jetbrains.annotations.NotNull()
    java.lang.String currencyAndUnit, @org.jetbrains.annotations.NotNull()
    java.lang.String scaleModes, boolean lockRatio, @org.jetbrains.annotations.NotNull()
    java.lang.String lockRatioValue, @org.jetbrains.annotations.NotNull()
    java.lang.String scalesPlacement, boolean noOverlappingLabels, boolean plusButton, boolean countdown, @org.jetbrains.annotations.NotNull()
    java.lang.String symbolLabel, @org.jetbrains.annotations.NotNull()
    java.lang.String symbolLineColor, @org.jetbrains.annotations.NotNull()
    java.lang.String symbolLastValueMode, @org.jetbrains.annotations.NotNull()
    java.lang.String highLowMode, @org.jetbrains.annotations.NotNull()
    java.lang.String highLowLineColor, @org.jetbrains.annotations.NotNull()
    java.lang.String highLowLabelColor, @org.jetbrains.annotations.NotNull()
    java.lang.String highLowCalculationMode, @org.jetbrains.annotations.NotNull()
    java.lang.String indicatorsAndFinancials, @org.jetbrains.annotations.NotNull()
    java.lang.String bidAskMode, @org.jetbrains.annotations.NotNull()
    java.lang.String bidColor, @org.jetbrains.annotations.NotNull()
    java.lang.String askColor, boolean bidAskLabels, boolean indicatorsAndFinancialsNameLabels, boolean indicatorsAndFinancialsValueLabels, boolean symbolLastPriceLine, boolean symbolPrevCloseLine, boolean prePostMarketPriceLine, boolean highLowPriceLines, boolean bidAskLines, boolean dayOfWeekOnLabels, @org.jetbrains.annotations.NotNull()
    java.lang.String dateFormat, @org.jetbrains.annotations.NotNull()
    java.lang.String timeFormat, boolean saveLeftEdge, boolean symbolNameLabel, boolean symbolLastPriceLabel, boolean symbolPrevCloseLabel, boolean prePostMarketPriceLabel, boolean highLowPriceLabels) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrencyAndUnit() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getScaleModes() {
        return null;
    }
    
    public final boolean getLockRatio() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLockRatioValue() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getScalesPlacement() {
        return null;
    }
    
    public final boolean getNoOverlappingLabels() {
        return false;
    }
    
    public final boolean getPlusButton() {
        return false;
    }
    
    public final boolean getCountdown() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSymbolLabel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSymbolLineColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSymbolLastValueMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHighLowMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHighLowLineColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHighLowLabelColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHighLowCalculationMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getIndicatorsAndFinancials() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBidAskMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBidColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAskColor() {
        return null;
    }
    
    public final boolean getBidAskLabels() {
        return false;
    }
    
    public final boolean getIndicatorsAndFinancialsNameLabels() {
        return false;
    }
    
    public final boolean getIndicatorsAndFinancialsValueLabels() {
        return false;
    }
    
    public final boolean getSymbolLastPriceLine() {
        return false;
    }
    
    public final boolean getSymbolPrevCloseLine() {
        return false;
    }
    
    public final boolean getPrePostMarketPriceLine() {
        return false;
    }
    
    public final boolean getHighLowPriceLines() {
        return false;
    }
    
    public final boolean getBidAskLines() {
        return false;
    }
    
    public final boolean getDayOfWeekOnLabels() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDateFormat() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTimeFormat() {
        return null;
    }
    
    public final boolean getSaveLeftEdge() {
        return false;
    }
    
    public final boolean getSymbolNameLabel() {
        return false;
    }
    
    public final boolean getSymbolLastPriceLabel() {
        return false;
    }
    
    public final boolean getSymbolPrevCloseLabel() {
        return false;
    }
    
    public final boolean getPrePostMarketPriceLabel() {
        return false;
    }
    
    public final boolean getHighLowPriceLabels() {
        return false;
    }
    
    public ScalesSettings() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component16() {
        return null;
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
    public final java.lang.String component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final boolean component20() {
        return false;
    }
    
    public final boolean component21() {
        return false;
    }
    
    public final boolean component22() {
        return false;
    }
    
    public final boolean component23() {
        return false;
    }
    
    public final boolean component24() {
        return false;
    }
    
    public final boolean component25() {
        return false;
    }
    
    public final boolean component26() {
        return false;
    }
    
    public final boolean component27() {
        return false;
    }
    
    public final boolean component28() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component29() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component30() {
        return null;
    }
    
    public final boolean component31() {
        return false;
    }
    
    public final boolean component32() {
        return false;
    }
    
    public final boolean component33() {
        return false;
    }
    
    public final boolean component34() {
        return false;
    }
    
    public final boolean component35() {
        return false;
    }
    
    public final boolean component36() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    public final boolean component6() {
        return false;
    }
    
    public final boolean component7() {
        return false;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.ScalesSettings copy(@org.jetbrains.annotations.NotNull()
    java.lang.String currencyAndUnit, @org.jetbrains.annotations.NotNull()
    java.lang.String scaleModes, boolean lockRatio, @org.jetbrains.annotations.NotNull()
    java.lang.String lockRatioValue, @org.jetbrains.annotations.NotNull()
    java.lang.String scalesPlacement, boolean noOverlappingLabels, boolean plusButton, boolean countdown, @org.jetbrains.annotations.NotNull()
    java.lang.String symbolLabel, @org.jetbrains.annotations.NotNull()
    java.lang.String symbolLineColor, @org.jetbrains.annotations.NotNull()
    java.lang.String symbolLastValueMode, @org.jetbrains.annotations.NotNull()
    java.lang.String highLowMode, @org.jetbrains.annotations.NotNull()
    java.lang.String highLowLineColor, @org.jetbrains.annotations.NotNull()
    java.lang.String highLowLabelColor, @org.jetbrains.annotations.NotNull()
    java.lang.String highLowCalculationMode, @org.jetbrains.annotations.NotNull()
    java.lang.String indicatorsAndFinancials, @org.jetbrains.annotations.NotNull()
    java.lang.String bidAskMode, @org.jetbrains.annotations.NotNull()
    java.lang.String bidColor, @org.jetbrains.annotations.NotNull()
    java.lang.String askColor, boolean bidAskLabels, boolean indicatorsAndFinancialsNameLabels, boolean indicatorsAndFinancialsValueLabels, boolean symbolLastPriceLine, boolean symbolPrevCloseLine, boolean prePostMarketPriceLine, boolean highLowPriceLines, boolean bidAskLines, boolean dayOfWeekOnLabels, @org.jetbrains.annotations.NotNull()
    java.lang.String dateFormat, @org.jetbrains.annotations.NotNull()
    java.lang.String timeFormat, boolean saveLeftEdge, boolean symbolNameLabel, boolean symbolLastPriceLabel, boolean symbolPrevCloseLabel, boolean prePostMarketPriceLabel, boolean highLowPriceLabels) {
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