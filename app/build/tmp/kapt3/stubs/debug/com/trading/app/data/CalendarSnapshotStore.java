package com.trading.app.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0015"}, d2 = {"Lcom/trading/app/data/CalendarSnapshotStore;", "", "()V", "latestAiPayload", "Lcom/trading/app/models/EconomicCalendarAiPayload;", "getLatestAiPayload", "()Lcom/trading/app/models/EconomicCalendarAiPayload;", "setLatestAiPayload", "(Lcom/trading/app/models/EconomicCalendarAiPayload;)V", "latestAiPayloadJson", "", "getLatestAiPayloadJson", "()Ljava/lang/String;", "setLatestAiPayloadJson", "(Ljava/lang/String;)V", "latestDisplayPayload", "Lcom/trading/app/models/EconomicCalendarDisplayPayload;", "getLatestDisplayPayload", "()Lcom/trading/app/models/EconomicCalendarDisplayPayload;", "setLatestDisplayPayload", "(Lcom/trading/app/models/EconomicCalendarDisplayPayload;)V", "app_debug"})
public final class CalendarSnapshotStore {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.trading.app.models.EconomicCalendarDisplayPayload latestDisplayPayload;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.trading.app.models.EconomicCalendarAiPayload latestAiPayload;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.NotNull()
    private static volatile java.lang.String latestAiPayloadJson = "";
    @org.jetbrains.annotations.NotNull()
    public static final com.trading.app.data.CalendarSnapshotStore INSTANCE = null;
    
    private CalendarSnapshotStore() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.trading.app.models.EconomicCalendarDisplayPayload getLatestDisplayPayload() {
        return null;
    }
    
    public final void setLatestDisplayPayload(@org.jetbrains.annotations.Nullable()
    com.trading.app.models.EconomicCalendarDisplayPayload p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.trading.app.models.EconomicCalendarAiPayload getLatestAiPayload() {
        return null;
    }
    
    public final void setLatestAiPayload(@org.jetbrains.annotations.Nullable()
    com.trading.app.models.EconomicCalendarAiPayload p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLatestAiPayloadJson() {
        return null;
    }
    
    public final void setLatestAiPayloadJson(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
}