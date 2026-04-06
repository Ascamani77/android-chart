package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0007\u001a^\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2 \u0010\f\u001a\u001c\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010\b\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0007\u001a\u001a\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\b2\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u0002\u00a8\u0006\u0012"}, d2 = {"InfoRow", "", "label", "", "value", "OrderModal", "symbol", "bidPrice", "", "askPrice", "onClose", "Lkotlin/Function0;", "onPlaceOrder", "Lkotlin/Function3;", "Lcom/trading/app/models/Position;", "onTradingSettingsClick", "formatPriceValue", "price", "app_debug"})
public final class OrderModalKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void OrderModal(@org.jetbrains.annotations.NotNull()
    java.lang.String symbol, float bidPrice, float askPrice, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function3<? super com.trading.app.models.Position, ? super java.lang.String, ? super java.lang.Float, kotlin.Unit> onPlaceOrder, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onTradingSettingsClick) {
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