package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000>\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0003\u001a,\u0010\n\u001a\u00020\u00052\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00012\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0010H\u0007\u001a\u0010\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\fH\u0007\u001a\u0012\u0010\u0013\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0014\u001a\u00020\u0003H\u0002\u001a\u000e\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u0003\u001a\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0018\u001a\u00020\u0003H\u0002\u001a\u0010\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u0003H\u0002\u001a\u0012\u0010\u001b\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u001c\u001a\u00020\u0003H\u0002\u001a\u0012\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\u0016\u001a\u00020\u0003H\u0002\" \u0010\u0000\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"COMPANY_KEYWORDS_TO_TICKER", "", "Lkotlin/Pair;", "", "CompanyLogoIcon", "", "symbolInfo", "Lcom/trading/app/models/SymbolInfo;", "size", "", "NewsPage", "newsItems", "Lcom/trading/app/models/NewsItem;", "isLoading", "", "onBack", "Lkotlin/Function0;", "NewsRow", "item", "deriveAssetFromHeadline", "headline", "getRelativeTime", "isoDateTime", "inferTickerFromCompanyName", "upperTitle", "isFiatCode", "code", "officialCompanyLogoUrl", "ticker", "parseIsoDate", "Ljava/util/Date;", "app_debug"})
public final class NewsPageKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<kotlin.Pair<java.lang.String, java.lang.String>> COMPANY_KEYWORDS_TO_TICKER = null;
    
    @androidx.compose.runtime.Composable()
    public static final void NewsPage(@org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.NewsItem> newsItems, boolean isLoading, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void NewsRow(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.NewsItem item) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CompanyLogoIcon(com.trading.app.models.SymbolInfo symbolInfo, int size) {
    }
    
    private static final java.lang.String officialCompanyLogoUrl(java.lang.String ticker) {
        return null;
    }
    
    private static final java.lang.String inferTickerFromCompanyName(java.lang.String upperTitle) {
        return null;
    }
    
    private static final com.trading.app.models.SymbolInfo deriveAssetFromHeadline(java.lang.String headline) {
        return null;
    }
    
    private static final boolean isFiatCode(java.lang.String code) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String getRelativeTime(@org.jetbrains.annotations.NotNull()
    java.lang.String isoDateTime) {
        return null;
    }
    
    private static final java.util.Date parseIsoDate(java.lang.String isoDateTime) {
        return null;
    }
}