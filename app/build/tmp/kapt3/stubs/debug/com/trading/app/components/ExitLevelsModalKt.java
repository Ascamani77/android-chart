package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\u001a`\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u00032\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u0018\u0010\f\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\u0004\u0012\u00020\u00010\rH\u0007\u001aR\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0007\u001a\u001a\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u00062\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0002\u00a8\u0006\u001a"}, d2 = {"ExitLevelsModal", "", "symbol", "", "orderType", "entryPrice", "", "initialUnits", "initialTp", "initialSl", "onClose", "Lkotlin/Function0;", "onConfirm", "Lkotlin/Function1;", "", "Lcom/trading/app/components/ExitLevel;", "LevelItem", "level", "index", "", "isLast", "", "onUpdate", "onDelete", "formatPrice", "price", "app_debug"})
public final class ExitLevelsModalKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ExitLevelsModal(@org.jetbrains.annotations.NotNull()
    java.lang.String symbol, @org.jetbrains.annotations.NotNull()
    java.lang.String orderType, float entryPrice, @org.jetbrains.annotations.NotNull()
    java.lang.String initialUnits, @org.jetbrains.annotations.NotNull()
    java.lang.String initialTp, @org.jetbrains.annotations.NotNull()
    java.lang.String initialSl, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<com.trading.app.components.ExitLevel>, kotlin.Unit> onConfirm) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void LevelItem(@org.jetbrains.annotations.NotNull()
    java.lang.String symbol, float entryPrice, @org.jetbrains.annotations.NotNull()
    com.trading.app.components.ExitLevel level, int index, boolean isLast, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.components.ExitLevel, kotlin.Unit> onUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
    
    private static final java.lang.String formatPrice(float price, java.lang.String symbol) {
        return null;
    }
}