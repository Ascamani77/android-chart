package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a<\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a\"\u0010\n\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u0006H\u0007\u001ab\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000e2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\t2 \u0010\u0012\u001a\u001c\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u0003\u0012\u0006\u0012\u0004\u0018\u00010\u000e\u0012\u0004\u0012\u00020\u00010\u00132\b\b\u0002\u0010\u0015\u001a\u00020\u0003H\u0007\u001a\u001a\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u000e2\b\b\u0002\u0010\f\u001a\u00020\u0003H\u0002\u00a8\u0006\u0018"}, d2 = {"ExitOptionItem", "", "label", "", "value", "isSelected", "", "hasInfo", "onClick", "Lkotlin/Function0;", "InfoRowSimple", "SimpleOrderPage", "symbol", "bidPrice", "", "askPrice", "priceChange", "onClose", "onPlaceOrder", "Lkotlin/Function3;", "Lcom/trading/app/models/Position;", "initialSide", "formatPriceValue", "price", "app_debug"})
public final class SimpleOrderPageKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void SimpleOrderPage(@org.jetbrains.annotations.NotNull()
    java.lang.String symbol, float bidPrice, float askPrice, float priceChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function3<? super com.trading.app.models.Position, ? super java.lang.String, ? super java.lang.Float, kotlin.Unit> onPlaceOrder, @org.jetbrains.annotations.NotNull()
    java.lang.String initialSide) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void InfoRowSimple(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.lang.String value, boolean hasInfo) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ExitOptionItem(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.Nullable()
    java.lang.String value, boolean isSelected, boolean hasInfo, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    private static final java.lang.String formatPriceValue(float price, java.lang.String symbol) {
        return null;
    }
}