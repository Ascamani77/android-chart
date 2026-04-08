package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000N\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a,\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001af\u0010\n\u001a\u00020\u00012\b\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0007\u001a*\u0010\u0013\u001a\u00020\u00012\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0010H\u0003\u001a\u0010\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u0006H\u0003\u001a4\u0010\u0019\u001a\u00020\u00012\b\b\u0002\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u001e\u001a\u00020\u001fH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b \u0010!\u001a,\u0010\"\u001a\u00020\u00012\u0006\u0010#\u001a\u00020\u00062\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\b2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\bH\u0003\u001a\u0012\u0010$\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0018\u001a\u00020\u0006H\u0002\u001a\u0010\u0010%\u001a\u00020\u00062\u0006\u0010&\u001a\u00020\u0006H\u0002\u001a\u0015\u0010\'\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u0006H\u0002\u00a2\u0006\u0002\u0010)\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006*"}, d2 = {"CalendarEventRow", "", "event", "Lcom/trading/app/models/EconomicCalendarDisplayEvent;", "CalendarHeader", "title", "", "onBack", "Lkotlin/Function0;", "onRefresh", "CalendarPage", "payload", "Lcom/trading/app/models/EconomicCalendarDisplayPayload;", "isLoading", "", "onSelectDate", "Lkotlin/Function1;", "onPreviousMonth", "onNextMonth", "DayStrip", "dayChips", "", "Lcom/trading/app/models/CalendarDayChip;", "FlagBadge", "countryCode", "MetricValueColumn", "modifier", "Landroidx/compose/ui/Modifier;", "value", "label", "valueColor", "Landroidx/compose/ui/graphics/Color;", "MetricValueColumn-g2O1Hgs", "(Landroidx/compose/ui/Modifier;Ljava/lang/String;Ljava/lang/String;J)V", "MonthStripHeader", "monthLabel", "countryFlagEmoji", "formatMonthLabel", "isoDate", "importanceColor", "importance", "(Ljava/lang/String;)J", "app_debug"})
public final class CalendarPageKt {
    
    @androidx.compose.runtime.Composable()
    public static final void CalendarPage(@org.jetbrains.annotations.Nullable()
    com.trading.app.models.EconomicCalendarDisplayPayload payload, boolean isLoading, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRefresh, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSelectDate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPreviousMonth, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNextMonth) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MonthStripHeader(java.lang.String monthLabel, kotlin.jvm.functions.Function0<kotlin.Unit> onPreviousMonth, kotlin.jvm.functions.Function0<kotlin.Unit> onNextMonth) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CalendarHeader(java.lang.String title, kotlin.jvm.functions.Function0<kotlin.Unit> onBack, kotlin.jvm.functions.Function0<kotlin.Unit> onRefresh) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DayStrip(java.util.List<com.trading.app.models.CalendarDayChip> dayChips, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSelectDate) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CalendarEventRow(com.trading.app.models.EconomicCalendarDisplayEvent event) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FlagBadge(java.lang.String countryCode) {
    }
    
    private static final java.lang.String countryFlagEmoji(java.lang.String countryCode) {
        return null;
    }
    
    private static final long importanceColor(java.lang.String importance) {
        return 0L;
    }
    
    private static final java.lang.String formatMonthLabel(java.lang.String isoDate) {
        return null;
    }
}