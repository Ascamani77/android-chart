package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a6\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a2\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000fH\u0007\u001a\u0015\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0003H\u0002\u00a2\u0006\u0002\u0010\u0013\u00a8\u0006\u0014"}, d2 = {"EventsCheckboxRow", "", "label", "", "checked", "", "modifier", "Landroidx/compose/ui/Modifier;", "onCheckedChange", "Lkotlin/Function1;", "EventsSettingsModal", "settings", "Lcom/trading/app/models/ChartSettings;", "onUpdate", "onClose", "Lkotlin/Function0;", "safeParseEventsColor", "Landroidx/compose/ui/graphics/Color;", "hex", "(Ljava/lang/String;)J", "app_debug"})
public final class EventsSettingsModalKt {
    
    @androidx.compose.runtime.Composable()
    public static final void EventsSettingsModal(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.ChartSettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.models.ChartSettings, kotlin.Unit> onUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose) {
    }
    
    private static final long safeParseEventsColor(java.lang.String hex) {
        return 0L;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void EventsCheckboxRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, boolean checked, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onCheckedChange) {
    }
}