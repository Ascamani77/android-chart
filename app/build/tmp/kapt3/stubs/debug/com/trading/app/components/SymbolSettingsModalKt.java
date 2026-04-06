package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000:\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\u001a\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007H\u0003\u001aJ\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00040\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0010\u001a:\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\u00022\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00040\f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007H\u0007\u001a&\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00022\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007H\u0003\u001a&\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007H\u0003\u001a,\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u001c2\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00040\fH\u0003\u001a@\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020 2\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020\u00040\f2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007H\u0007\u001a\u001a\u0010#\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\nH\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b$\u0010%\u001a&\u0010&\u001a\u00020\n2\b\u0010\'\u001a\u0004\u0018\u00010\u00022\b\b\u0002\u0010(\u001a\u00020\nH\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b)\u0010*\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006+"}, d2 = {"TRADING_VIEW_COLORS", "", "", "ColorBox", "", "color", "onClick", "Lkotlin/Function0;", "ColorMixer", "initialColor", "Landroidx/compose/ui/graphics/Color;", "onColorChange", "Lkotlin/Function1;", "onAdd", "onBack", "ColorMixer-Iv8Zu3U", "(JLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "ColorPickerModal", "title", "onColorSelected", "onClose", "ColorSettingRow", "label", "onColorClick", "SettingsDropdown", "value", "SimpleCheckbox", "checked", "", "onCheckedChange", "SymbolSettingsModal", "settings", "Lcom/trading/app/models/ChartSettings;", "onUpdate", "onTimeZoneClick", "colorToHex", "colorToHex-8_81llA", "(J)Ljava/lang/String;", "parseColor", "colorString", "defaultColor", "parseColor-4WTKRHQ", "(Ljava/lang/String;J)J", "app_debug"})
public final class SymbolSettingsModalKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.String> TRADING_VIEW_COLORS = null;
    
    @androidx.compose.runtime.Composable()
    public static final void SymbolSettingsModal(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.ChartSettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.models.ChartSettings, kotlin.Unit> onUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onTimeZoneClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ColorSettingRow(java.lang.String label, java.lang.String color, kotlin.jvm.functions.Function0<kotlin.Unit> onColorClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SimpleCheckbox(java.lang.String label, boolean checked, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onCheckedChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ColorBox(java.lang.String color, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SettingsDropdown(java.lang.String label, java.lang.String value, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ColorPickerModal(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String initialColor, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onColorSelected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose) {
    }
}