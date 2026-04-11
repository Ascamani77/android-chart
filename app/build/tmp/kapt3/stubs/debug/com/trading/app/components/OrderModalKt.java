package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0007\u001a\u0088\u0001\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2 \u0010\u0010\u001a\u001c\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010\b\u0012\u0004\u0012\u00020\u00010\u00112\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u0003H\u0007\u001a\u001a\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\b2\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u0002\u00a8\u0006\u0019"}, d2 = {"InfoRow", "", "label", "", "value", "OrderModal", "symbol", "bidPrice", "", "askPrice", "priceChange", "chartData", "", "Lcom/trading/app/models/OHLCData;", "onClose", "Lkotlin/Function0;", "onPlaceOrder", "Lkotlin/Function3;", "Lcom/trading/app/models/Position;", "onTradingSettingsClick", "showMarketSideButtons", "", "initialSide", "formatPriceValue", "price", "app_debug"})
public final class OrderModalKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void OrderModal(@org.jetbrains.annotations.NotNull()
    java.lang.String symbol, float bidPrice, float askPrice, float priceChange, @org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.OHLCData> chartData, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function3<? super com.trading.app.models.Position, ? super java.lang.String, ? super java.lang.Float, kotlin.Unit> onPlaceOrder, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onTradingSettingsClick, boolean showMarketSideButtons, @org.jetbrains.annotations.NotNull()
    java.lang.String initialSide) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void InfoRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    private static final java.lang.String formatPriceValue(float price, java.lang.String symbol) {
        return null;
    }
}