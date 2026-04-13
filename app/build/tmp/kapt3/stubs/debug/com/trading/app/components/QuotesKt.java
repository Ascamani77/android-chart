package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000Z\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0004\u001a(\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00052\b\u0010\t\u001a\u0004\u0018\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\fH\u0007\u001al\u0010\r\u001a\u00020\u00072\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\f2\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00070\u00112\u0014\b\u0002\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\n0\u00132\u001a\b\u0002\u0010\u0014\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u0004\u0012\u0004\u0012\u00020\u00070\u0011H\u0007\u001a\u0018\u0010\u0015\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u0002H\u0002\u001a\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u001a\u0018\u0010\u0019\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u001bH\u0002\u001a\u0018\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u001bH\u0002\u001a\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0016\u001a\u00020\u0002H\u0002\u001a\u001a\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u001a\u0018\u0010!\u001a\u00020\"2\u0006\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u001bH\u0002\u001a\'\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00020\u00042\u0012\u0010$\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020%\"\u00020\u0002H\u0002\u00a2\u0006\u0002\u0010&\u001a&\u0010\'\u001a\u0004\u0018\u00010\n2\u0006\u0010(\u001a\u00020\u00052\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\n0\u0013H\u0002\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2 = {"allowedQuoteTickers", "", "", "defaultQuotesCatalog", "", "Lcom/trading/app/models/SymbolInfo;", "QuoteListItem", "", "quoteInfo", "quote", "Lcom/trading/app/components/SymbolQuote;", "onSelect", "Lkotlin/Function0;", "Quotes", "onClose", "quotes", "onQuoteSelect", "Lkotlin/Function1;", "quotesByTicker", "", "onVisibleSymbolsChanged", "defaultBrokerSymbolFor", "ticker", "type", "defaultQuoteSymbols", "formatQuoteValue", "value", "", "formatSignedChange", "isForexTicker", "", "mergeQuoteCatalog", "symbols", "quoteDecimalsFor", "", "quoteLookupKeys", "identifiers", "", "([Ljava/lang/String;)Ljava/util/List;", "resolveQuoteForSymbol", "symbol", "app_debug"})
public final class QuotesKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.trading.app.models.SymbolInfo> defaultQuotesCatalog = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Set<java.lang.String> allowedQuoteTickers = null;
    
    private static final java.lang.String defaultBrokerSymbolFor(java.lang.String ticker, java.lang.String type) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<com.trading.app.models.SymbolInfo> defaultQuoteSymbols() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<com.trading.app.models.SymbolInfo> mergeQuoteCatalog(@org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.SymbolInfo> symbols) {
        return null;
    }
    
    private static final boolean isForexTicker(java.lang.String ticker) {
        return false;
    }
    
    private static final int quoteDecimalsFor(java.lang.String ticker, float value) {
        return 0;
    }
    
    private static final java.lang.String formatQuoteValue(java.lang.String ticker, float value) {
        return null;
    }
    
    private static final java.lang.String formatSignedChange(java.lang.String ticker, float value) {
        return null;
    }
    
    private static final java.util.List<java.lang.String> quoteLookupKeys(java.lang.String... identifiers) {
        return null;
    }
    
    private static final com.trading.app.components.SymbolQuote resolveQuoteForSymbol(com.trading.app.models.SymbolInfo symbol, java.util.Map<java.lang.String, com.trading.app.components.SymbolQuote> quotesByTicker) {
        return null;
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void Quotes(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose, @org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.SymbolInfo> quotes, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQuoteSelect, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, com.trading.app.components.SymbolQuote> quotesByTicker, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<java.lang.String>, kotlin.Unit> onVisibleSymbolsChanged) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void QuoteListItem(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.SymbolInfo quoteInfo, @org.jetbrains.annotations.Nullable()
    com.trading.app.components.SymbolQuote quote, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSelect) {
    }
}