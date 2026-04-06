package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u009a\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u001a\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0001H\u0007\u001a\u0091\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00030\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u00162\b\b\u0002\u0010\u001a\u001a\u00020\u00182\b\b\u0002\u0010\u001b\u001a\u00020\u00162\b\b\u0002\u0010\u001c\u001a\u00020\u00182\b\b\u0002\u0010\u001d\u001a\u00020\u00162\b\b\u0002\u0010\u001e\u001a\u00020\u00182\b\b\u0002\u0010\u001f\u001a\u00020\u00162\b\b\u0002\u0010 \u001a\u00020\u00182\b\b\u0002\u0010!\u001a\u00020\u00162\b\b\u0002\u0010\"\u001a\u00020\u00162\b\b\u0002\u0010#\u001a\u00020\u00182\b\b\u0002\u0010$\u001a\u00020\u00162\b\b\u0002\u0010%\u001a\u00020\u00182\b\b\u0002\u0010&\u001a\u00020\u00162\b\b\u0002\u0010\'\u001a\u00020\u00162\u0014\b\u0002\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00030\u00112\u0014\b\u0002\u0010)\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00030\u00112\u0014\b\u0002\u0010*\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00030\u00112\b\b\u0002\u0010+\u001a\u00020\u00162\b\b\u0002\u0010,\u001a\u00020\u00162\b\b\u0002\u0010-\u001a\u00020\u00162\b\b\u0002\u0010.\u001a\u00020\u00012\u000e\b\u0002\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\b\b\u0002\u00100\u001a\u00020\u00162\u000e\b\u0002\u00101\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\n\b\u0002\u00102\u001a\u0004\u0018\u0001032\u000e\b\u0002\u00104\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\u000e\b\u0002\u00105\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\u000e\b\u0002\u00106\u001a\b\u0012\u0004\u0012\u00020\u00030\u00142\u001a\b\u0002\u00107\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002080\u000e\u0012\u0004\u0012\u00020\u00030\u00112\b\b\u0002\u00109\u001a\u00020\u00012\u0014\b\u0002\u0010:\u001a\u000e\u0012\u0004\u0012\u00020;\u0012\u0004\u0012\u00020\u00030\u00112\u000e\b\u0002\u0010<\u001a\b\u0012\u0004\u0012\u00020=0\u000e2\u0014\b\u0002\u0010>\u001a\u000e\u0012\u0004\u0012\u00020=\u0012\u0004\u0012\u00020\u00030\u00112\u0014\b\u0002\u0010?\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00030\u00112\u0014\b\u0002\u0010@\u001a\u000e\u0012\u0004\u0012\u00020A\u0012\u0004\u0012\u00020\u00030\u00112\u001a\b\u0002\u0010B\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020=0\u000e\u0012\u0004\u0012\u00020\u00030\u00112\u000e\b\u0002\u0010C\u001a\b\u0012\u0004\u0012\u00020D0\u000e2\u001a\b\u0002\u0010E\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020D0\u000e\u0012\u0004\u0012\u00020\u00030\u00112\u001a\b\u0002\u0010F\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020D0\u000e\u0012\u0004\u0012\u00020\u00030\u00112\u001a\b\u0002\u0010G\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020H0\u000e\u0012\u0004\u0012\u00020\u00030\u00112\u0014\b\u0002\u0010I\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00030\u00112\n\b\u0002\u0010J\u001a\u0004\u0018\u00010KH\u0007\u00a2\u0006\u0002\u0010L\u001a\u0018\u0010M\u001a\u00020\u00182\u0006\u0010N\u001a\u00020\u00182\u0006\u0010O\u001a\u00020\u0018H\u0002\u001a\u001c\u0010P\u001a\b\u0012\u0004\u0012\u00020Q0\u000e2\f\u0010R\u001a\b\u0012\u0004\u0012\u0002080\u000eH\u0002\u001a\u001c\u0010S\u001a\b\u0012\u0004\u0012\u00020T0\u000e2\f\u0010R\u001a\b\u0012\u0004\u0012\u0002080\u000eH\u0002\u001a\u001a\u0010U\u001a\u00020\u00012\u0006\u0010V\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u0001H\u0002\u001a\u0018\u0010W\u001a\u00020\u00182\u0006\u0010X\u001a\u00020\u00012\u0006\u0010Y\u001a\u00020\u0001H\u0002\u001a\u0010\u0010Z\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0001H\u0002\u001a\f\u0010[\u001a\u00020T*\u000208H\u0002\u001a\f\u0010\\\u001a\u00020]*\u000203H\u0002\u001a\n\u0010^\u001a\u000203*\u00020]\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006_"}, d2 = {"LOG_TAG", "", "OhlcItem", "", "label", "value", "", "symbol", "TradingChart", "timeframe", "style", "chartSettings", "Lcom/trading/app/models/ChartSettings;", "drawings", "", "Lcom/trading/app/models/Drawing;", "onDrawingUpdate", "Lkotlin/Function1;", "activeTool", "onToolReset", "Lkotlin/Function0;", "showRsi", "", "rsiPeriod", "", "showEma10", "ema10Period", "showEma20", "ema20Period", "showSma1", "sma1Period", "showSma2", "sma2Period", "showVwap", "showBb", "bbPeriod", "showAtr", "atrPeriod", "showVolume", "isCrosshairActive", "onCrosshairToggle", "onVolumeToggle", "onIndicatorSettingsClick", "isMagnetEnabled", "isLocked", "isVisible", "selectedCurrency", "onCurrencyClick", "isFullscreen", "onFullscreenExit", "scrollToTimestamp", "", "onScrollDone", "onLongPress", "onSettingsClick", "onDataLoaded", "Lcom/trading/app/models/OHLCData;", "selectedTimeZone", "onQuoteUpdate", "Lcom/trading/app/components/SymbolQuote;", "positions", "Lcom/trading/app/models/Position;", "onPositionUpdate", "onPositionDelete", "onAccountUpdate", "Lcom/trading/app/data/Mt5Service$AccountInfo;", "onPositionsUpdate", "orders", "Lcom/trading/app/models/Order;", "onOrdersUpdate", "onHistoryOrdersUpdate", "onBalanceHistoryUpdate", "Lcom/trading/app/models/BalanceRecord;", "onDoubleClick", "reverseBridge", "Lcom/trading/app/data/Mt5ReverseBridge;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/trading/app/models/ChartSettings;Ljava/util/List;Lkotlin/jvm/functions/Function1;Ljava/lang/String;Lkotlin/jvm/functions/Function0;ZIZIZIZIZIZZIZIZZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;ZZZLjava/lang/String;Lkotlin/jvm/functions/Function0;ZLkotlin/jvm/functions/Function0;Ljava/lang/Long;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lcom/trading/app/data/Mt5ReverseBridge;)V", "applyOpacity", "color", "opacity", "buildVolumeHistogramData", "Lcom/tradingview/lightweightcharts/api/series/models/HistogramData;", "data", "calculateHeikinAshi", "Lcom/tradingview/lightweightcharts/api/series/models/CandlestickData;", "formatPrice", "price", "getFullChartColor", "colorSetting", "customBg", "getFullSymbolName", "toCandlestickData", "toChartTime", "Lcom/tradingview/lightweightcharts/api/series/models/Time;", "toTimestamp", "app_debug"})
public final class TradingChartKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String LOG_TAG = "TradingChart";
    
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
    
    private static final java.lang.String getFullSymbolName(java.lang.String symbol) {
        return null;
    }
    
    private static final int applyOpacity(int color, int opacity) {
        return 0;
    }
    
    private static final int getFullChartColor(java.lang.String colorSetting, java.lang.String customBg) {
        return 0;
    }
    
    private static final java.util.List<com.tradingview.lightweightcharts.api.series.models.CandlestickData> calculateHeikinAshi(java.util.List<com.trading.app.models.OHLCData> data) {
        return null;
    }
    
    private static final java.util.List<com.tradingview.lightweightcharts.api.series.models.HistogramData> buildVolumeHistogramData(java.util.List<com.trading.app.models.OHLCData> data) {
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
    kotlin.jvm.functions.Function0<kotlin.Unit> onToolReset, boolean showRsi, int rsiPeriod, boolean showEma10, int ema10Period, boolean showEma20, int ema20Period, boolean showSma1, int sma1Period, boolean showSma2, int sma2Period, boolean showVwap, boolean showBb, int bbPeriod, boolean showAtr, int atrPeriod, boolean showVolume, boolean isCrosshairActive, @org.jetbrains.annotations.NotNull()
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
    kotlin.jvm.functions.Function1<? super java.lang.Float, kotlin.Unit> onDoubleClick, @org.jetbrains.annotations.Nullable()
    com.trading.app.data.Mt5ReverseBridge reverseBridge) {
    }
    
    private static final java.lang.String formatPrice(float price, java.lang.String symbol) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void OhlcItem(@org.jetbrains.annotations.NotNull()
    java.lang.String label, float value, @org.jetbrains.annotations.NotNull()
    java.lang.String symbol) {
    }
}