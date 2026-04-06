package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\t\u001a*\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a2\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0007\u001a,\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a\u0010\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u000eH\u0003\u001a:\u0010\u0014\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u000e2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00172\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a:\u0010\u0019\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u000e2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00172\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a\"\u0010\u001a\u001a\u00020\u00012\b\b\u0002\u0010\u001b\u001a\u00020\u000e2\u000e\b\u0002\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0007\u001a\u001e\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u001e\u001a\u00020\u000e2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u00a8\u0006 "}, d2 = {"AdvancedColorPickerModal", "", "onDismiss", "Lkotlin/Function0;", "onColorAdded", "Lkotlin/Function1;", "Landroidx/compose/ui/graphics/Color;", "ScalesAndLinesSettingsModal", "settings", "Lcom/trading/app/models/ChartSettings;", "onUpdate", "onClose", "ScalesCheckboxRow", "label", "", "checked", "", "onCheckedChange", "ScalesSectionHeader", "title", "SettingsDropdownRow", "value", "options", "", "onValueChange", "SettingsLabelDropdownRow", "SymbolLinePreview", "lineColor", "onLineColorClick", "privateColorBox", "hex", "onClick", "app_debug"})
public final class ScalesAndLinesSettingsModalKt {
    
    @androidx.compose.runtime.Composable()
    public static final void ScalesAndLinesSettingsModal(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.ChartSettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.models.ChartSettings, kotlin.Unit> onUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void privateColorBox(java.lang.String hex, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ScalesSectionHeader(java.lang.String title) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SettingsDropdownRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> options, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SettingsLabelDropdownRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> options, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ScalesCheckboxRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, boolean checked, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onCheckedChange) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SymbolLinePreview(@org.jetbrains.annotations.NotNull()
    java.lang.String lineColor, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onLineColorClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AdvancedColorPickerModal(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super androidx.compose.ui.graphics.Color, kotlin.Unit> onColorAdded) {
    }
}