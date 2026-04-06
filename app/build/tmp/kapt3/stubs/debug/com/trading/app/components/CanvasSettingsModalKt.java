package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a<\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u0003\u001a\u0010\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0003H\u0003\u001a2\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a.\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a,\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00182\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u001a\u0010\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0003H\u0002\u001a\u0010\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0018H\u0002\u001a\u0015\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002\u00a2\u0006\u0002\u0010 \u00a8\u0006!"}, d2 = {"CanvasColorBox", "", "hex", "", "onClick", "Lkotlin/Function0;", "CanvasDropdown", "value", "options", "", "onValueChange", "Lkotlin/Function1;", "modifier", "Landroidx/compose/ui/Modifier;", "CanvasSectionHeader", "title", "CanvasSettingsModal", "settings", "Lcom/trading/app/models/ChartSettings;", "onUpdate", "onClose", "CrosshairPreviewBox", "color", "thickness", "", "style", "MarginInputRow", "label", "getLineStyleIndex", "getLineStyleText", "parseCanvasColor", "Landroidx/compose/ui/graphics/Color;", "(Ljava/lang/String;)J", "app_debug"})
public final class CanvasSettingsModalKt {
    
    @androidx.compose.runtime.Composable()
    public static final void CanvasSettingsModal(@org.jetbrains.annotations.NotNull()
    com.trading.app.models.ChartSettings settings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.trading.app.models.ChartSettings, kotlin.Unit> onUpdate, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClose) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MarginInputRow(java.lang.String label, int value, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onValueChange) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CanvasSectionHeader(java.lang.String title) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CanvasDropdown(java.lang.String value, java.util.List<java.lang.String> options, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CanvasColorBox(java.lang.String hex, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CrosshairPreviewBox(java.lang.String color, int thickness, java.lang.String style, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    private static final java.lang.String getLineStyleText(int style) {
        return null;
    }
    
    private static final int getLineStyleIndex(java.lang.String style) {
        return 0;
    }
    
    private static final long parseCanvasColor(java.lang.String hex) {
        return 0L;
    }
}