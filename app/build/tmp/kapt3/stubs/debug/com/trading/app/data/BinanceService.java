package com.trading.app.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B;\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u0012 \b\u0002\u0010\u0006\u001a\u001a\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0004\u0012\u00020\u00050\u0007\u00a2\u0006\u0002\u0010\u000bJ\u0006\u0010\u0018\u001a\u00020\u0005J\u0006\u0010\u0019\u001a\u00020\u0005J\u0016\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\bJ\u000e\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R&\u0010\u0006\u001a\u001a\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0004\u0012\u00020\u00050\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\b0\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/trading/app/data/BinanceService;", "", "onQuoteUpdate", "Lkotlin/Function1;", "Lcom/trading/app/components/SymbolQuote;", "", "onHistoryUpdate", "Lkotlin/Function2;", "", "", "Lcom/trading/app/models/OHLCData;", "(Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)V", "client", "Lokhttp3/OkHttpClient;", "gson", "Lcom/google/gson/Gson;", "isClosing", "", "mainHandler", "Landroid/os/Handler;", "symbols", "", "webSocket", "Lokhttp3/WebSocket;", "connect", "disconnect", "fetchHistory", "symbol", "timeframe", "subscribe", "app_debug"})
public final class BinanceService {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.trading.app.components.SymbolQuote, kotlin.Unit> onQuoteUpdate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function2<java.lang.String, java.util.List<com.trading.app.models.OHLCData>, kotlin.Unit> onHistoryUpdate = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient client = null;
    @org.jetbrains.annotations.Nullable()
    private okhttp3.WebSocket webSocket;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler mainHandler = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> symbols = null;
    private boolean isClosing = false;
    
    public BinanceService(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.components.SymbolQuote, kotlin.Unit> onQuoteUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.util.List<com.trading.app.models.OHLCData>, kotlin.Unit> onHistoryUpdate) {
        super();
    }
    
    public final void connect() {
    }
    
    public final void subscribe(@org.jetbrains.annotations.NotNull()
    java.lang.String symbol) {
    }
    
    public final void fetchHistory(@org.jetbrains.annotations.NotNull()
    java.lang.String symbol, @org.jetbrains.annotations.NotNull()
    java.lang.String timeframe) {
    }
    
    public final void disconnect() {
    }
}