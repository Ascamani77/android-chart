package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0084\u0001\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a(\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0000\u001a\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0015H\u0002\u001a<\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00192\u0006\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u00112\u0006\u0010 \u001a\u00020\u0011H\u0002\u001a$\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\u00192\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00192\u0006\u0010\u001d\u001a\u00020\u0001H\u0002\u001a,\u0010#\u001a\b\u0012\u0004\u0012\u00020\"0\u00192\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00192\u000e\u0010$\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0019H\u0002\u001a&\u0010%\u001a\u00020&2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00192\u0006\u0010\'\u001a\u00020\u00132\u0006\u0010(\u001a\u00020\u0015H\u0000\u001a\u0018\u0010)\u001a\u00020\u000b2\u0006\u0010*\u001a\u00020+2\u0006\u0010\f\u001a\u00020\rH\u0000\u001a\u0010\u0010,\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0001H\u0002\u001a\b\u0010-\u001a\u00020\rH\u0001\u001a*\u0010.\u001a\u00020/2\u0006\u00100\u001a\u00020/2\u0006\u00101\u001a\u00020/2\u0006\u0010\u001d\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b2\u00103\u001a.\u00104\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00192\u0006\u00105\u001a\u00020&2\u0006\u0010\'\u001a\u00020\u0013H\u0000\u001aL\u00106\u001a\u00020\u000b*\u0002072\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u00105\u001a\u00020&2\u0006\u00108\u001a\u00020\u00152\u0006\u00109\u001a\u00020\u00072\u0006\u0010:\u001a\u00020\u00072\u0006\u0010;\u001a\u00020\u00152\u0006\u0010<\u001a\u00020\u0001H\u0001\u001a\f\u0010=\u001a\u00020>*\u00020?H\u0002\u001a\u0011\u0010@\u001a\u00020A*\u00020\u0007H\u0002\u00a2\u0006\u0002\u0010B\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0007X\u0080T\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006C"}, d2 = {"RSI_MAX", "", "RSI_MID", "RSI_MIN", "RSI_OVERBOUGHT", "RSI_OVERSOLD", "RSI_PANE_BACKGROUND_HEX", "", "RSI_PANE_BORDER_HEX", "RSI_SCALE_KEY", "applyInlineRsiPaneScale", "", "refs", "Lcom/trading/app/components/RsiPaneRefs;", "scaleMargins", "Lcom/tradingview/lightweightcharts/api/options/models/PriceScaleMargins;", "borderColor", "Lcom/tradingview/lightweightcharts/api/chart/models/color/IntColor;", "visible", "", "applyOpacity", "", "color", "opacity", "buildFlatAreaData", "", "Lcom/tradingview/lightweightcharts/api/series/models/AreaData;", "candles", "Lcom/trading/app/models/OHLCData;", "value", "lineColor", "topColor", "bottomColor", "buildFlatLineData", "Lcom/tradingview/lightweightcharts/api/series/models/LineData;", "buildRsiData", "rsiValues", "calculateRsiChartData", "Lcom/trading/app/components/RsiChartData;", "enabled", "period", "createInlineRsiPaneSeries", "chartsView", "Lcom/tradingview/lightweightcharts/view/ChartsView;", "formatRsiAxisValue", "rememberRsiPaneRefs", "rsiAxisOffset", "Landroidx/compose/ui/unit/Dp;", "trackHeight", "itemHeight", "rsiAxisOffset-Md-fbLM", "(FFF)F", "updateInlineRsiPaneData", "data", "RsiPaneOverlay", "Landroidx/compose/foundation/layout/BoxScope;", "rsiPeriod", "scaleTextColor", "scaleBorderColor", "scaleFontSize", "axisWidthPx", "toChartTime", "Lcom/tradingview/lightweightcharts/api/series/models/Time;", "", "toComposeColor", "Landroidx/compose/ui/graphics/Color;", "(Ljava/lang/String;)J", "app_debug"})
public final class RSIPaneKt {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String RSI_SCALE_KEY = "rsi_pane";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String RSI_PANE_BACKGROUND_HEX = "#000000";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String RSI_PANE_BORDER_HEX = "#363A45";
    private static final float RSI_MIN = 0.0F;
    private static final float RSI_MID = 50.0F;
    private static final float RSI_OVERBOUGHT = 70.0F;
    private static final float RSI_MAX = 100.0F;
    private static final float RSI_OVERSOLD = 30.0F;
    
    private static final com.tradingview.lightweightcharts.api.series.models.Time toChartTime(long $this$toChartTime) {
        return null;
    }
    
    private static final int applyOpacity(int color, int opacity) {
        return 0;
    }
    
    private static final java.util.List<com.tradingview.lightweightcharts.api.series.models.LineData> buildRsiData(java.util.List<com.trading.app.models.OHLCData> candles, java.util.List<java.lang.Float> rsiValues) {
        return null;
    }
    
    private static final java.util.List<com.tradingview.lightweightcharts.api.series.models.LineData> buildFlatLineData(java.util.List<com.trading.app.models.OHLCData> candles, float value) {
        return null;
    }
    
    private static final java.util.List<com.tradingview.lightweightcharts.api.series.models.AreaData> buildFlatAreaData(java.util.List<com.trading.app.models.OHLCData> candles, float value, com.tradingview.lightweightcharts.api.chart.models.color.IntColor lineColor, com.tradingview.lightweightcharts.api.chart.models.color.IntColor topColor, com.tradingview.lightweightcharts.api.chart.models.color.IntColor bottomColor) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    @org.jetbrains.annotations.NotNull()
    public static final com.trading.app.components.RsiPaneRefs rememberRsiPaneRefs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final com.trading.app.components.RsiChartData calculateRsiChartData(@org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.OHLCData> candles, boolean enabled, int period) {
        return null;
    }
    
    public static final void createInlineRsiPaneSeries(@org.jetbrains.annotations.NotNull()
    com.tradingview.lightweightcharts.view.ChartsView chartsView, @org.jetbrains.annotations.NotNull()
    com.trading.app.components.RsiPaneRefs refs) {
    }
    
    public static final void updateInlineRsiPaneData(@org.jetbrains.annotations.NotNull()
    com.trading.app.components.RsiPaneRefs refs, @org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.OHLCData> candles, @org.jetbrains.annotations.NotNull()
    com.trading.app.components.RsiChartData data, boolean enabled) {
    }
    
    public static final void applyInlineRsiPaneScale(@org.jetbrains.annotations.NotNull()
    com.trading.app.components.RsiPaneRefs refs, @org.jetbrains.annotations.NotNull()
    com.tradingview.lightweightcharts.api.options.models.PriceScaleMargins scaleMargins, @org.jetbrains.annotations.NotNull()
    com.tradingview.lightweightcharts.api.chart.models.color.IntColor borderColor, boolean visible) {
    }
    
    private static final long toComposeColor(java.lang.String $this$toComposeColor) {
        return 0L;
    }
    
    private static final java.lang.String formatRsiAxisValue(float value) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void RsiPaneOverlay(@org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.layout.BoxScope $this$RsiPaneOverlay, boolean visible, @org.jetbrains.annotations.NotNull()
    com.tradingview.lightweightcharts.api.options.models.PriceScaleMargins scaleMargins, @org.jetbrains.annotations.NotNull()
    com.trading.app.components.RsiChartData data, int rsiPeriod, @org.jetbrains.annotations.NotNull()
    java.lang.String scaleTextColor, @org.jetbrains.annotations.NotNull()
    java.lang.String scaleBorderColor, int scaleFontSize, float axisWidthPx) {
    }
}