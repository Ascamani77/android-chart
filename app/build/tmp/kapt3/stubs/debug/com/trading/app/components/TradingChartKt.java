package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u00cc\u0001\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u001f\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0001H\u0007\u001a\u0099\u0007\u0010\u000b\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00060\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u00012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00060\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u001b2\b\b\u0002\u0010\u001c\u001a\u00020\u00192\b\b\u0002\u0010\u001d\u001a\u00020\u001b2\b\b\u0002\u0010\u001e\u001a\u00020\u00192\b\b\u0002\u0010\u001f\u001a\u00020\u001b2\b\b\u0002\u0010 \u001a\u00020\u00192\b\b\u0002\u0010!\u001a\u00020\u001b2\b\b\u0002\u0010\"\u001a\u00020\u00192\b\b\u0002\u0010#\u001a\u00020\u001b2\b\b\u0002\u0010$\u001a\u00020\u00192\b\b\u0002\u0010%\u001a\u00020\u00192\b\b\u0002\u0010&\u001a\u00020\u001b2\b\b\u0002\u0010\'\u001a\u00020\t2\b\b\u0002\u0010(\u001a\u00020\u00192\b\b\u0002\u0010)\u001a\u00020\u001b2\b\b\u0002\u0010*\u001a\u00020\u00192\b\b\u0002\u0010+\u001a\u00020\u001b2\b\b\u0002\u0010,\u001a\u00020\u001b2\b\b\u0002\u0010-\u001a\u00020\u001b2\b\b\u0002\u0010.\u001a\u00020\u00192\b\b\u0002\u0010/\u001a\u00020\u00192\u0014\b\u0002\u00100\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00060\u00142\u0014\b\u0002\u00101\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00060\u00142\u0014\b\u0002\u00102\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00060\u00142\b\b\u0002\u00103\u001a\u00020\u00192\b\b\u0002\u00104\u001a\u00020\u00192\b\b\u0002\u00105\u001a\u00020\u00192\b\b\u0002\u00106\u001a\u00020\u00012\u000e\b\u0002\u00107\u001a\b\u0012\u0004\u0012\u00020\u00060\u00172\b\b\u0002\u00108\u001a\u00020\u00192\u000e\b\u0002\u00109\u001a\b\u0012\u0004\u0012\u00020\u00060\u00172\n\b\u0002\u0010:\u001a\u0004\u0018\u00010;2\u000e\b\u0002\u0010<\u001a\b\u0012\u0004\u0012\u00020\u00060\u00172\u000e\b\u0002\u0010=\u001a\b\u0012\u0004\u0012\u00020\u00060\u00172\u000e\b\u0002\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00060\u00172\u001a\b\u0002\u0010?\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020@0\u0011\u0012\u0004\u0012\u00020\u00060\u00142\b\b\u0002\u0010A\u001a\u00020\u00012\u0014\b\u0002\u0010B\u001a\u000e\u0012\u0004\u0012\u00020C\u0012\u0004\u0012\u00020\u00060\u00142\u000e\b\u0002\u0010D\u001a\b\u0012\u0004\u0012\u00020E0\u00112\u0014\b\u0002\u0010F\u001a\u000e\u0012\u0004\u0012\u00020E\u0012\u0004\u0012\u00020\u00060\u00142\u0014\b\u0002\u0010G\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00060\u00142\u0014\b\u0002\u0010H\u001a\u000e\u0012\u0004\u0012\u00020I\u0012\u0004\u0012\u00020\u00060\u00142\u001a\b\u0002\u0010J\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020E0\u0011\u0012\u0004\u0012\u00020\u00060\u00142\u000e\b\u0002\u0010K\u001a\b\u0012\u0004\u0012\u00020L0\u00112\u001a\b\u0002\u0010M\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020L0\u0011\u0012\u0004\u0012\u00020\u00060\u00142\u001a\b\u0002\u0010N\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020L0\u0011\u0012\u0004\u0012\u00020\u00060\u00142\u001a\b\u0002\u0010O\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020P0\u0011\u0012\u0004\u0012\u00020\u00060\u00142\u0014\b\u0002\u0010Q\u001a\u000e\u0012\u0004\u0012\u00020R\u0012\u0004\u0012\u00020\u00060\u00142\b\b\u0002\u0010S\u001a\u00020\u00192\n\b\u0002\u0010T\u001a\u0004\u0018\u00010\u00012\b\b\u0002\u0010U\u001a\u00020\u001b2\b\b\u0002\u0010V\u001a\u00020\u00192\u0014\b\u0002\u0010W\u001a\u000e\u0012\u0004\u0012\u00020X\u0012\u0004\u0012\u00020\u00060\u00142\u0014\b\u0002\u0010Y\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00060\u00142\n\b\u0002\u0010Z\u001a\u0004\u0018\u00010[H\u0007\u00a2\u0006\u0002\u0010\\\u001a\u0018\u0010]\u001a\u00020;2\u0006\u0010^\u001a\u00020;2\u0006\u0010\f\u001a\u00020\u0001H\u0002\u001a\u0018\u0010_\u001a\u00020\u001b2\u0006\u0010`\u001a\u00020\u001b2\u0006\u0010a\u001a\u00020\u001bH\u0002\u001a<\u0010b\u001a\b\u0012\u0004\u0012\u00020@0\u00112\f\u0010c\u001a\b\u0012\u0004\u0012\u00020@0\u00112\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010d\u001a\u00020\t2\u0006\u0010e\u001a\u00020;2\u0006\u0010f\u001a\u00020\tH\u0002\u001a\u001c\u0010g\u001a\b\u0012\u0004\u0012\u00020h0\u00112\f\u0010i\u001a\b\u0012\u0004\u0012\u00020@0\u0011H\u0002\u001a\u001c\u0010j\u001a\b\u0012\u0004\u0012\u00020k0\u00112\f\u0010i\u001a\b\u0012\u0004\u0012\u00020@0\u0011H\u0002\u001a\u0010\u0010l\u001a\u00020\u00012\u0006\u0010m\u001a\u00020\tH\u0002\u001a\u001a\u0010n\u001a\u00020\u00012\u0006\u0010o\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u0001H\u0002\u001a\u0018\u0010p\u001a\u00020\u001b2\u0006\u0010q\u001a\u00020\u00012\u0006\u0010r\u001a\u00020\u0001H\u0002\u001a\u000e\u0010s\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u0001\u001a\u0010\u0010t\u001a\u00020;2\u0006\u0010u\u001a\u00020;H\u0002\u001a6\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020x0w2\u0006\u0010.\u001a\u00020\u00192\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010*\u001a\u00020\u00192\b\b\u0002\u0010(\u001a\u00020\u0019H\u0002\u001a\u001a\u0010y\u001a\u00020\u00062\u0006\u0010z\u001a\u00020{2\b\u0010|\u001a\u0004\u0018\u00010}H\u0002\u001a\u0010\u0010~\u001a\u00020;2\u0006\u0010\f\u001a\u00020\u0001H\u0002\u001a\u0012\u0010\u007f\u001a\u00030\u0080\u00012\u0007\u0010\u0081\u0001\u001a\u00020\u0001H\u0002\u001a\r\u0010\u0082\u0001\u001a\u00020k*\u00020@H\u0002\u001a\u000e\u0010\u0083\u0001\u001a\u00030\u0084\u0001*\u00020;H\u0002\u001a\f\u0010\u0085\u0001\u001a\u00020;*\u00030\u0084\u0001\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0086\u0001"}, d2 = {"ATR_SCALE_KEY", "", "LOG_TAG", "MACD_SCALE_KEY", "VOLUME_SCALE_KEY", "OhlcItem", "", "label", "value", "", "symbol", "TradingChart", "timeframe", "style", "chartSettings", "Lcom/trading/app/models/ChartSettings;", "drawings", "", "Lcom/trading/app/models/Drawing;", "onDrawingUpdate", "Lkotlin/Function1;", "activeTool", "onToolReset", "Lkotlin/Function0;", "showRsi", "", "rsiPeriod", "", "showEma10", "ema10Period", "showEma20", "ema20Period", "showSma1", "sma1Period", "showSma2", "sma2Period", "showVwap", "showBb", "bbPeriod", "bbStdDev", "showAtr", "atrPeriod", "showMacd", "macdFast", "macdSlow", "macdSignal", "showVolume", "isCrosshairActive", "onCrosshairToggle", "onVolumeToggle", "onIndicatorSettingsClick", "isMagnetEnabled", "isLocked", "isVisible", "selectedCurrency", "onCurrencyClick", "isFullscreen", "onFullscreenExit", "scrollToTimestamp", "", "onScrollDone", "onLongPress", "onSettingsClick", "onDataLoaded", "Lcom/trading/app/models/OHLCData;", "selectedTimeZone", "onQuoteUpdate", "Lcom/trading/app/components/SymbolQuote;", "positions", "Lcom/trading/app/models/Position;", "onPositionUpdate", "onPositionDelete", "onAccountUpdate", "Lcom/trading/app/data/Mt5Service$AccountInfo;", "onPositionsUpdate", "orders", "Lcom/trading/app/models/Order;", "onOrdersUpdate", "onHistoryOrdersUpdate", "onBalanceHistoryUpdate", "Lcom/trading/app/models/BalanceRecord;", "onCalendarUpdate", "Lcom/trading/app/models/EconomicCalendarPayload;", "isCalendarVisible", "calendarRequestDateIso", "calendarRequestVersion", "isNewsVisible", "onNewsUpdate", "Lcom/trading/app/models/NewsPayload;", "onDoubleClick", "reverseBridge", "Lcom/trading/app/data/Mt5ReverseBridge;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/trading/app/models/ChartSettings;Ljava/util/List;Lkotlin/jvm/functions/Function1;Ljava/lang/String;Lkotlin/jvm/functions/Function0;ZIZIZIZIZIZZIFZIZIIIZZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;ZZZLjava/lang/String;Lkotlin/jvm/functions/Function0;ZLkotlin/jvm/functions/Function0;Ljava/lang/Long;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;ZLjava/lang/String;IZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lcom/trading/app/data/Mt5ReverseBridge;)V", "alignToTimeframeStart", "timestampSeconds", "applyOpacity", "color", "opacity", "applyTickToCandles", "candles", "lastPrice", "tickTimestampSeconds", "tickVolume", "buildVolumeHistogramData", "Lcom/tradingview/lightweightcharts/api/series/models/HistogramData;", "data", "calculateHeikinAshi", "Lcom/tradingview/lightweightcharts/api/series/models/CandlestickData;", "formatBandMultiplier", "multiplier", "formatPrice", "price", "getFullChartColor", "colorSetting", "customBg", "getFullSymbolName", "normalizeEpochSeconds", "timestamp", "resolvePaneMargins", "", "Lcom/tradingview/lightweightcharts/api/options/models/PriceScaleMargins;", "safelyRemovePriceLine", "api", "Lcom/tradingview/lightweightcharts/api/interfaces/SeriesApi;", "priceLine", "Lcom/tradingview/lightweightcharts/api/series/common/PriceLine;", "timeframeToSeconds", "toPriceScaleMode", "Lcom/tradingview/lightweightcharts/api/series/enums/PriceScaleMode;", "scaleType", "toCandlestickData", "toChartTime", "Lcom/tradingview/lightweightcharts/api/series/models/Time;", "toTimestamp", "app_debug"})
public final class TradingChartKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String LOG_TAG = "TradingChart";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String MACD_SCALE_KEY = "macd_pane";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String VOLUME_SCALE_KEY = "volume_pane";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ATR_SCALE_KEY = "atr_pane";
    
    public static final long toTimestamp(@org.jetbrains.annotations.NotNull()
    com.tradingview.lightweightcharts.api.series.models.Time $this$toTimestamp) {
        return 0L;
    }
    
    private static final com.tradingview.lightweightcharts.api.series.models.Time toChartTime(long $this$toChartTime) {
        return null;
    }
    
    private static final com.tradingview.lightweightcharts.api.series.models.CandlestickData toCandlestickData(com.trading.app.models.OHLCData $this$toCandlestickData) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String getFullSymbolName(@org.jetbrains.annotations.NotNull()
    java.lang.String symbol) {
        return null;
    }
    
    private static final int applyOpacity(int color, int opacity) {
        return 0;
    }
    
    private static final int getFullChartColor(java.lang.String colorSetting, java.lang.String customBg) {
        return 0;
    }
    
    private static final com.tradingview.lightweightcharts.api.series.enums.PriceScaleMode toPriceScaleMode(java.lang.String scaleType) {
        return null;
    }
    
    private static final long normalizeEpochSeconds(long timestamp) {
        return 0L;
    }
    
    private static final long timeframeToSeconds(java.lang.String timeframe) {
        return 0L;
    }
    
    private static final long alignToTimeframeStart(long timestampSeconds, java.lang.String timeframe) {
        return 0L;
    }
    
    private static final java.util.List<com.trading.app.models.OHLCData> applyTickToCandles(java.util.List<com.trading.app.models.OHLCData> candles, java.lang.String timeframe, float lastPrice, long tickTimestampSeconds, float tickVolume) {
        return null;
    }
    
    private static final void safelyRemovePriceLine(com.tradingview.lightweightcharts.api.interfaces.SeriesApi api, com.tradingview.lightweightcharts.api.series.common.PriceLine priceLine) {
    }
    
    private static final java.util.List<com.tradingview.lightweightcharts.api.series.models.CandlestickData> calculateHeikinAshi(java.util.List<com.trading.app.models.OHLCData> data) {
        return null;
    }
    
    private static final java.util.List<com.tradingview.lightweightcharts.api.series.models.HistogramData> buildVolumeHistogramData(java.util.List<com.trading.app.models.OHLCData> data) {
        return null;
    }
    
    private static final java.util.Map<java.lang.String, com.tradingview.lightweightcharts.api.options.models.PriceScaleMargins> resolvePaneMargins(boolean showVolume, boolean showRsi, boolean showMacd, boolean showAtr) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void TradingChart(@org.jetbrains.annotations.NotNull()
    java.lang.String symbol, @org.jetbrains.annotations.NotNull()
    java.lang.String timeframe, @org.jetbrains.annotations.NotNull()
    java.lang.String style, @org.jetbrains.annotations.NotNull()
    com.trading.app.models.ChartSettings chartSettings, @org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.Drawing> drawings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.models.Drawing, kotlin.Unit> onDrawingUpdate, @org.jetbrains.annotations.Nullable()
    java.lang.String activeTool, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToolReset, boolean showRsi, int rsiPeriod, boolean showEma10, int ema10Period, boolean showEma20, int ema20Period, boolean showSma1, int sma1Period, boolean showSma2, int sma2Period, boolean showVwap, boolean showBb, int bbPeriod, float bbStdDev, boolean showAtr, int atrPeriod, boolean showMacd, int macdFast, int macdSlow, int macdSignal, boolean showVolume, boolean isCrosshairActive, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onCrosshairToggle, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onVolumeToggle, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onIndicatorSettingsClick, boolean isMagnetEnabled, boolean isLocked, boolean isVisible, @org.jetbrains.annotations.NotNull()
    java.lang.String selectedCurrency, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCurrencyClick, boolean isFullscreen, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onFullscreenExit, @org.jetbrains.annotations.Nullable()
    java.lang.Long scrollToTimestamp, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onScrollDone, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onLongPress, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSettingsClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<com.trading.app.models.OHLCData>, kotlin.Unit> onDataLoaded, @org.jetbrains.annotations.NotNull()
    java.lang.String selectedTimeZone, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.components.SymbolQuote, kotlin.Unit> onQuoteUpdate, @org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.Position> positions, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.models.Position, kotlin.Unit> onPositionUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onPositionDelete, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.data.Mt5Service.AccountInfo, kotlin.Unit> onAccountUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<com.trading.app.models.Position>, kotlin.Unit> onPositionsUpdate, @org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.Order> orders, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<com.trading.app.models.Order>, kotlin.Unit> onOrdersUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<com.trading.app.models.Order>, kotlin.Unit> onHistoryOrdersUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<com.trading.app.models.BalanceRecord>, kotlin.Unit> onBalanceHistoryUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.models.EconomicCalendarPayload, kotlin.Unit> onCalendarUpdate, boolean isCalendarVisible, @org.jetbrains.annotations.Nullable()
    java.lang.String calendarRequestDateIso, int calendarRequestVersion, boolean isNewsVisible, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.models.NewsPayload, kotlin.Unit> onNewsUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Float, kotlin.Unit> onDoubleClick, @org.jetbrains.annotations.Nullable()
    com.trading.app.data.Mt5ReverseBridge reverseBridge) {
    }
    
    private static final java.lang.String formatPrice(float price, java.lang.String symbol) {
        return null;
    }
    
    private static final java.lang.String formatBandMultiplier(float multiplier) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void OhlcItem(@org.jetbrains.annotations.NotNull()
    java.lang.String label, float value, @org.jetbrains.annotations.NotNull()
    java.lang.String symbol) {
    }
}