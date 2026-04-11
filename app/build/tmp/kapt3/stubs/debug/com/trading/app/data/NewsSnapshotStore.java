package com.trading.app.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/trading/app/data/NewsSnapshotStore;", "", "()V", "latestAiPayloadJson", "", "getLatestAiPayloadJson", "()Ljava/lang/String;", "setLatestAiPayloadJson", "(Ljava/lang/String;)V", "latestPayload", "Lcom/trading/app/models/NewsPayload;", "getLatestPayload", "()Lcom/trading/app/models/NewsPayload;", "setLatestPayload", "(Lcom/trading/app/models/NewsPayload;)V", "app_debug"})
public final class NewsSnapshotStore {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.trading.app.models.NewsPayload latestPayload;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.NotNull()
    private static volatile java.lang.String latestAiPayloadJson = "";
    @org.jetbrains.annotations.NotNull()
    public static final com.trading.app.data.NewsSnapshotStore INSTANCE = null;
    
    private NewsSnapshotStore() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.trading.app.models.NewsPayload getLatestPayload() {
        return null;
    }
    
    public final void setLatestPayload(@org.jetbrains.annotations.Nullable()
    com.trading.app.models.NewsPayload p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLatestAiPayloadJson() {
        return null;
    }
    
    public final void setLatestAiPayloadJson(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
}