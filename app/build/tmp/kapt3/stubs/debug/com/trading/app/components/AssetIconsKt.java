package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00004\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a$\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\u001a\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\n2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\u001a\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\n2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\u0013\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\n\u00a2\u0006\u0002\u0010\u0012\u001a\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\nH\u0002\u00a8\u0006\u0016"}, d2 = {"AssetIcon", "", "symbol", "Lcom/trading/app/models/SymbolInfo;", "modifier", "Landroidx/compose/ui/Modifier;", "size", "", "CryptoLogo", "ticker", "", "ExchangeIcon", "exchange", "FlagImage", "currency", "getSymbolBackgroundColor", "Landroidx/compose/ui/graphics/Color;", "type", "(Ljava/lang/String;)J", "isFiat", "", "code", "app_debug"})
public final class AssetIconsKt {
    
    @androidx.compose.runtime.Composable()
    public static final void AssetIcon(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.SymbolInfo symbol, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, int size) {
    }
    
    private static final boolean isFiat(java.lang.String code) {
        return false;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void FlagImage(@org.jetbrains.annotations.NotNull()
    java.lang.String currency, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void CryptoLogo(@org.jetbrains.annotations.NotNull()
    java.lang.String ticker, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, int size) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ExchangeIcon(@org.jetbrains.annotations.NotNull()
    java.lang.String exchange, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    public static final long getSymbolBackgroundColor(@org.jetbrains.annotations.NotNull()
    java.lang.String type) {
        return 0L;
    }
}