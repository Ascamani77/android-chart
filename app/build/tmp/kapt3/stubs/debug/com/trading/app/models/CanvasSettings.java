package com.trading.app.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\bn\b\u0086\b\u0018\u00002\u00020\u0001B\u00ed\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\f\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\f\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u0016\u001a\u00020\f\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u0018\u001a\u00020\f\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u001a\u001a\u00020\f\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u001c\u001a\u00020\f\u0012\b\b\u0002\u0010\u001d\u001a\u00020\f\u0012\b\b\u0002\u0010\u001e\u001a\u00020\f\u0012\b\b\u0002\u0010\u001f\u001a\u00020\u0003\u0012\b\b\u0002\u0010 \u001a\u00020\u0003\u0012\b\b\u0002\u0010!\u001a\u00020\u0003\u0012\b\b\u0002\u0010\"\u001a\u00020\f\u0012\b\b\u0002\u0010#\u001a\u00020\f\u0012\b\b\u0002\u0010$\u001a\u00020\f\u0012\b\b\u0002\u0010%\u001a\u00020\u0003\u0012\b\b\u0002\u0010&\u001a\u00020\u0007\u0012\b\b\u0002\u0010\'\u001a\u00020\u0003\u0012\b\b\u0002\u0010(\u001a\u00020\u0007\u00a2\u0006\u0002\u0010)J\t\u0010Q\u001a\u00020\u0003H\u00c6\u0003J\t\u0010R\u001a\u00020\fH\u00c6\u0003J\t\u0010S\u001a\u00020\u0003H\u00c6\u0003J\t\u0010T\u001a\u00020\u0007H\u00c6\u0003J\t\u0010U\u001a\u00020\u0003H\u00c6\u0003J\t\u0010V\u001a\u00020\u0003H\u00c6\u0003J\t\u0010W\u001a\u00020\u0003H\u00c6\u0003J\t\u0010X\u001a\u00020\fH\u00c6\u0003J\t\u0010Y\u001a\u00020\u0007H\u00c6\u0003J\t\u0010Z\u001a\u00020\fH\u00c6\u0003J\t\u0010[\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\\\u001a\u00020\u0003H\u00c6\u0003J\t\u0010]\u001a\u00020\fH\u00c6\u0003J\t\u0010^\u001a\u00020\u0007H\u00c6\u0003J\t\u0010_\u001a\u00020\fH\u00c6\u0003J\t\u0010`\u001a\u00020\u0007H\u00c6\u0003J\t\u0010a\u001a\u00020\fH\u00c6\u0003J\t\u0010b\u001a\u00020\fH\u00c6\u0003J\t\u0010c\u001a\u00020\fH\u00c6\u0003J\t\u0010d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010g\u001a\u00020\u0003H\u00c6\u0003J\t\u0010h\u001a\u00020\fH\u00c6\u0003J\t\u0010i\u001a\u00020\fH\u00c6\u0003J\t\u0010j\u001a\u00020\fH\u00c6\u0003J\t\u0010k\u001a\u00020\u0003H\u00c6\u0003J\t\u0010l\u001a\u00020\u0007H\u00c6\u0003J\t\u0010m\u001a\u00020\u0003H\u00c6\u0003J\t\u0010n\u001a\u00020\u0007H\u00c6\u0003J\t\u0010o\u001a\u00020\u0007H\u00c6\u0003J\t\u0010p\u001a\u00020\u0003H\u00c6\u0003J\t\u0010q\u001a\u00020\u0003H\u00c6\u0003J\t\u0010r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010s\u001a\u00020\fH\u00c6\u0003J\t\u0010t\u001a\u00020\u0003H\u00c6\u0003J\u00f1\u0002\u0010u\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\f2\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00072\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\f2\b\b\u0002\u0010\u0015\u001a\u00020\u00072\b\b\u0002\u0010\u0016\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\u00072\b\b\u0002\u0010\u0018\u001a\u00020\f2\b\b\u0002\u0010\u0019\u001a\u00020\u00072\b\b\u0002\u0010\u001a\u001a\u00020\f2\b\b\u0002\u0010\u001b\u001a\u00020\u00072\b\b\u0002\u0010\u001c\u001a\u00020\f2\b\b\u0002\u0010\u001d\u001a\u00020\f2\b\b\u0002\u0010\u001e\u001a\u00020\f2\b\b\u0002\u0010\u001f\u001a\u00020\u00032\b\b\u0002\u0010 \u001a\u00020\u00032\b\b\u0002\u0010!\u001a\u00020\u00032\b\b\u0002\u0010\"\u001a\u00020\f2\b\b\u0002\u0010#\u001a\u00020\f2\b\b\u0002\u0010$\u001a\u00020\f2\b\b\u0002\u0010%\u001a\u00020\u00032\b\b\u0002\u0010&\u001a\u00020\u00072\b\b\u0002\u0010\'\u001a\u00020\u00032\b\b\u0002\u0010(\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010v\u001a\u00020\u00072\b\u0010w\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010x\u001a\u00020\fH\u00d6\u0001J\t\u0010y\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010+R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010+R\u0011\u0010\u0019\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0011\u0010\u0018\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0011\u0010\u001d\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u00101R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010+R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010+R\u0011\u0010\u000e\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u00101R\u0011\u0010%\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010+R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010+R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u00101R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010+R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010/R\u0011\u0010\u0017\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010/R\u0011\u0010\u0016\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u00101R\u0011\u0010\'\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010+R\u0011\u0010&\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010/R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010+R\u0011\u0010#\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u00101R\u0011\u0010$\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u00101R\u0011\u0010\"\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u00101R\u0011\u0010 \u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010+R\u0011\u0010!\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010+R\u0011\u0010\u0015\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u0010/R\u0011\u0010\u0014\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\bF\u00101R\u0011\u0010\u001f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bG\u0010+R\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010+R\u0011\u0010\u001b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\bI\u0010/R\u0011\u0010\u001a\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\bJ\u00101R\u0011\u0010\u001c\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\bK\u00101R\u0011\u0010(\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\bL\u0010/R\u0011\u0010\u001e\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\bM\u00101R\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u0010+R\u0011\u0010\u0011\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\bO\u0010+R\u0011\u0010\u0010\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\bP\u0010/\u00a8\u0006z"}, d2 = {"Lcom/trading/app/models/CanvasSettings;", "", "backgroundType", "", "background", "backgroundGradientEnd", "gridVisible", "", "gridType", "gridColor", "horzGridColor", "gridOpacity", "", "crosshairColor", "crosshairThickness", "crosshairLineStyle", "watermarkVisible", "watermarkType", "watermarkColor", "scaleTextColor", "scaleFontSize", "scaleFontBold", "headerFontSize", "headerFontBold", "bottomFontSize", "bottomFontBold", "sidebarFontSize", "sidebarFontBold", "sidebarIconSize", "chartItemFontSize", "symbolFontSize", "scaleLineColor", "navigationButtons", "paneButtons", "marginTop", "marginBottom", "marginRight", "fullChartColor", "headerVisible", "headerVisibility", "swapHeaderAndFooter", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;ILjava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;IZIZIZIZIIILjava/lang/String;Ljava/lang/String;Ljava/lang/String;IIILjava/lang/String;ZLjava/lang/String;Z)V", "getBackground", "()Ljava/lang/String;", "getBackgroundGradientEnd", "getBackgroundType", "getBottomFontBold", "()Z", "getBottomFontSize", "()I", "getChartItemFontSize", "getCrosshairColor", "getCrosshairLineStyle", "getCrosshairThickness", "getFullChartColor", "getGridColor", "getGridOpacity", "getGridType", "getGridVisible", "getHeaderFontBold", "getHeaderFontSize", "getHeaderVisibility", "getHeaderVisible", "getHorzGridColor", "getMarginBottom", "getMarginRight", "getMarginTop", "getNavigationButtons", "getPaneButtons", "getScaleFontBold", "getScaleFontSize", "getScaleLineColor", "getScaleTextColor", "getSidebarFontBold", "getSidebarFontSize", "getSidebarIconSize", "getSwapHeaderAndFooter", "getSymbolFontSize", "getWatermarkColor", "getWatermarkType", "getWatermarkVisible", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component30", "component31", "component32", "component33", "component34", "component35", "component36", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class CanvasSettings {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String backgroundType = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String background = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String backgroundGradientEnd = null;
    private final boolean gridVisible = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String gridType = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String gridColor = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String horzGridColor = null;
    private final int gridOpacity = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String crosshairColor = null;
    private final int crosshairThickness = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String crosshairLineStyle = null;
    private final boolean watermarkVisible = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String watermarkType = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String watermarkColor = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String scaleTextColor = null;
    private final int scaleFontSize = 0;
    private final boolean scaleFontBold = false;
    private final int headerFontSize = 0;
    private final boolean headerFontBold = false;
    private final int bottomFontSize = 0;
    private final boolean bottomFontBold = false;
    private final int sidebarFontSize = 0;
    private final boolean sidebarFontBold = false;
    private final int sidebarIconSize = 0;
    private final int chartItemFontSize = 0;
    private final int symbolFontSize = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String scaleLineColor = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String navigationButtons = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String paneButtons = null;
    private final int marginTop = 0;
    private final int marginBottom = 0;
    private final int marginRight = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String fullChartColor = null;
    private final boolean headerVisible = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String headerVisibility = null;
    private final boolean swapHeaderAndFooter = false;
    
    public CanvasSettings(@org.jetbrains.annotations.NotNull()
    java.lang.String backgroundType, @org.jetbrains.annotations.NotNull()
    java.lang.String background, @org.jetbrains.annotations.NotNull()
    java.lang.String backgroundGradientEnd, boolean gridVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String gridType, @org.jetbrains.annotations.NotNull()
    java.lang.String gridColor, @org.jetbrains.annotations.NotNull()
    java.lang.String horzGridColor, int gridOpacity, @org.jetbrains.annotations.NotNull()
    java.lang.String crosshairColor, int crosshairThickness, @org.jetbrains.annotations.NotNull()
    java.lang.String crosshairLineStyle, boolean watermarkVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String watermarkType, @org.jetbrains.annotations.NotNull()
    java.lang.String watermarkColor, @org.jetbrains.annotations.NotNull()
    java.lang.String scaleTextColor, int scaleFontSize, boolean scaleFontBold, int headerFontSize, boolean headerFontBold, int bottomFontSize, boolean bottomFontBold, int sidebarFontSize, boolean sidebarFontBold, int sidebarIconSize, int chartItemFontSize, int symbolFontSize, @org.jetbrains.annotations.NotNull()
    java.lang.String scaleLineColor, @org.jetbrains.annotations.NotNull()
    java.lang.String navigationButtons, @org.jetbrains.annotations.NotNull()
    java.lang.String paneButtons, int marginTop, int marginBottom, int marginRight, @org.jetbrains.annotations.NotNull()
    java.lang.String fullChartColor, boolean headerVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String headerVisibility, boolean swapHeaderAndFooter) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBackgroundType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBackground() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBackgroundGradientEnd() {
        return null;
    }
    
    public final boolean getGridVisible() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getGridType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getGridColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHorzGridColor() {
        return null;
    }
    
    public final int getGridOpacity() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCrosshairColor() {
        return null;
    }
    
    public final int getCrosshairThickness() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCrosshairLineStyle() {
        return null;
    }
    
    public final boolean getWatermarkVisible() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWatermarkType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWatermarkColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getScaleTextColor() {
        return null;
    }
    
    public final int getScaleFontSize() {
        return 0;
    }
    
    public final boolean getScaleFontBold() {
        return false;
    }
    
    public final int getHeaderFontSize() {
        return 0;
    }
    
    public final boolean getHeaderFontBold() {
        return false;
    }
    
    public final int getBottomFontSize() {
        return 0;
    }
    
    public final boolean getBottomFontBold() {
        return false;
    }
    
    public final int getSidebarFontSize() {
        return 0;
    }
    
    public final boolean getSidebarFontBold() {
        return false;
    }
    
    public final int getSidebarIconSize() {
        return 0;
    }
    
    public final int getChartItemFontSize() {
        return 0;
    }
    
    public final int getSymbolFontSize() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getScaleLineColor() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNavigationButtons() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPaneButtons() {
        return null;
    }
    
    public final int getMarginTop() {
        return 0;
    }
    
    public final int getMarginBottom() {
        return 0;
    }
    
    public final int getMarginRight() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFullChartColor() {
        return null;
    }
    
    public final boolean getHeaderVisible() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHeaderVisibility() {
        return null;
    }
    
    public final boolean getSwapHeaderAndFooter() {
        return false;
    }
    
    public CanvasSettings() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final int component10() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component11() {
        return null;
    }
    
    public final boolean component12() {
        return false;
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
    
    public final int component16() {
        return 0;
    }
    
    public final boolean component17() {
        return false;
    }
    
    public final int component18() {
        return 0;
    }
    
    public final boolean component19() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final int component20() {
        return 0;
    }
    
    public final boolean component21() {
        return false;
    }
    
    public final int component22() {
        return 0;
    }
    
    public final boolean component23() {
        return false;
    }
    
    public final int component24() {
        return 0;
    }
    
    public final int component25() {
        return 0;
    }
    
    public final int component26() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component27() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component28() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component29() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    public final int component30() {
        return 0;
    }
    
    public final int component31() {
        return 0;
    }
    
    public final int component32() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component33() {
        return null;
    }
    
    public final boolean component34() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component35() {
        return null;
    }
    
    public final boolean component36() {
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
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    public final int component8() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.models.CanvasSettings copy(@org.jetbrains.annotations.NotNull()
    java.lang.String backgroundType, @org.jetbrains.annotations.NotNull()
    java.lang.String background, @org.jetbrains.annotations.NotNull()
    java.lang.String backgroundGradientEnd, boolean gridVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String gridType, @org.jetbrains.annotations.NotNull()
    java.lang.String gridColor, @org.jetbrains.annotations.NotNull()
    java.lang.String horzGridColor, int gridOpacity, @org.jetbrains.annotations.NotNull()
    java.lang.String crosshairColor, int crosshairThickness, @org.jetbrains.annotations.NotNull()
    java.lang.String crosshairLineStyle, boolean watermarkVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String watermarkType, @org.jetbrains.annotations.NotNull()
    java.lang.String watermarkColor, @org.jetbrains.annotations.NotNull()
    java.lang.String scaleTextColor, int scaleFontSize, boolean scaleFontBold, int headerFontSize, boolean headerFontBold, int bottomFontSize, boolean bottomFontBold, int sidebarFontSize, boolean sidebarFontBold, int sidebarIconSize, int chartItemFontSize, int symbolFontSize, @org.jetbrains.annotations.NotNull()
    java.lang.String scaleLineColor, @org.jetbrains.annotations.NotNull()
    java.lang.String navigationButtons, @org.jetbrains.annotations.NotNull()
    java.lang.String paneButtons, int marginTop, int marginBottom, int marginRight, @org.jetbrains.annotations.NotNull()
    java.lang.String fullChartColor, boolean headerVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String headerVisibility, boolean swapHeaderAndFooter) {
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