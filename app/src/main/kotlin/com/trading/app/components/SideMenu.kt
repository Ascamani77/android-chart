package com.trading.app.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.trading.app.models.ChartSettings

@Composable
fun SideMenu(
    symbol: String,
    timeframe: String,
    chartStyle: String,
    activeIndicators: String,
    onClose: () -> Unit,
    onIndicatorClick: () -> Unit,
    onAlertClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onBarReplayClick: () -> Unit,
    onSymbolSearchClick: () -> Unit,
    onCompareClick: () -> Unit,
    onStyleChangeClick: (String) -> Unit,
    onTimeframeClick: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    canUndo: Boolean,
    canRedo: Boolean,
    onFullscreenClick: () -> Unit,
    onDownloadChartClick: () -> Unit,
    onNavigate: (String) -> Unit = {},
    settings: ChartSettings = ChartSettings()
) {
    var expandedDrawings by remember { mutableStateOf(false) }
    var showChartTypeMenu by remember { mutableStateOf(false) }
    
    val fontSize = settings.canvas.sidebarFontSize.sp
    val fontWeight = if (settings.canvas.sidebarFontBold) FontWeight.Bold else FontWeight.Medium
    val iconSize = settings.canvas.sidebarIconSize.dp

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onClose() }
        ) {
            // Sliding Menu Content
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.85f)
                    .background(Color(0xFF000000))
                    .clickable(enabled = false) { } // Prevent click-through to background
            ) {
                // Simplified Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF000000))
                        .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        "Trading App",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Divider(color = Color(0xFF1E222D), thickness = 1.dp)

                // Menu Items
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Expandable Drawings Menu
                    ExpandableMenuItem(
                        icon = Icons.Outlined.Draw,
                        label = "Drawings",
                        isExpanded = expandedDrawings,
                        onToggle = { expandedDrawings = it },
                        fontSize = fontSize,
                        fontWeight = fontWeight,
                        iconSize = iconSize
                    )
                    
                    // Drawings Sub-items
                    AnimatedVisibility(
                        visible = expandedDrawings,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Column {
                            MT5SubMenuItem("Tools", fontSize = fontSize) { onNavigate("Drawings/Tools"); onClose() }
                            MT5SubMenuItem("Trend lines", fontSize = fontSize) { onNavigate("Drawings/TrendLines"); onClose() }
                            MT5SubMenuItem("Gann and Fibonacci", fontSize = fontSize) { onNavigate("Drawings/GannFibonacci"); onClose() }
                            MT5SubMenuItem("Patterns", fontSize = fontSize) { onNavigate("Drawings/Patterns"); onClose() }
                            MT5SubMenuItem("Forecasting and measurement", fontSize = fontSize) { onNavigate("Drawings/Forecasting"); onClose() }
                            MT5SubMenuItem("Geometric shapes", fontSize = fontSize) { onNavigate("Drawings/Shapes"); onClose() }
                            MT5SubMenuItem("Annotation", fontSize = fontSize) { onNavigate("Drawings/Annotation"); onClose() }
                            MT5SubMenuItem("Visual", fontSize = fontSize) { onNavigate("Drawings/Visual"); onClose() }
                        }
                    }
                    
                    
                    // Analysis hub items
                    MT5MenuItem(Icons.Outlined.Dashboard, "Layout setup", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onNavigate("Analysis/LayoutSetup"); onClose() }
                    MT5MenuItem(Icons.Outlined.Edit, "Manage Unnamed", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onNavigate("Analysis/ManageUnnamed"); onClose() }
                    MT5MenuItem(Icons.Outlined.Add, "New", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onNavigate("Analysis/New"); onClose() }
                    MT5MenuItem(Icons.Outlined.CloudUpload, "Save", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onNavigate("Analysis/Save"); onClose() }
                    MT5MenuItem(Icons.Outlined.FolderOpen, "Open", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onNavigate("Analysis/Open"); onClose() }
                    
                    Divider(color = Color(0xFF1E222D), modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Tools Section - Synchronized with Header
                    MT5MenuItem(Icons.Default.Search, "Symbol search", trailingText = symbol, fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onSymbolSearchClick(); onClose() }
                    MT5MenuItem(Icons.Default.Schedule, "Interval", trailingText = timeframe, fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onTimeframeClick(); onClose() }
                    MT5MenuItem(Icons.Default.WaterfallChart, "Indicators", trailingText = activeIndicators.ifBlank { null }, fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onIndicatorClick(); onClose() }
                    MT5MenuItem(Icons.Outlined.CompareArrows, "Compare", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onCompareClick(); onClose() }
                    MT5MenuItem(Icons.Outlined.NotificationsNone, "Alerts", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onAlertClick(); onClose() }
                    MT5MenuItem(Icons.Default.Replay, "Bar Replay", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onBarReplayClick(); onClose() }
                    MT5MenuItem(Icons.Outlined.ViewModule, "Indicator templates", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onNavigate("Tools/IndicatorTemplates"); onClose() }
                    
                    // Chart Type with Dropdown
                    Box {
                        MT5MenuItem(getStyleIcon(chartStyle), "Chart type", trailingText = chartStyle.replace("_", " ").capitalize(), fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { showChartTypeMenu = true }
                        
                        DropdownMenu(
                            expanded = showChartTypeMenu,
                            onDismissRequest = { showChartTypeMenu = false },
                            modifier = Modifier.background(Color(0xFF1E222D)).width(240.dp)
                        ) {
                            // Group 1: Candle Types
                            StyleMenuItem("Bars", "bars", Icons.Default.Reorder, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Candles", "candles", Icons.Default.BarChart, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Hollow candles", "hollow_candles", Icons.Default.BarChart, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Volume candles", "volume_candles", Icons.Default.BarChart, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            
                            Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))
                            
                            // Group 2: Line Types
                            StyleMenuItem("Line", "line", Icons.Default.ShowChart, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Line with markers", "line_markers", Icons.Default.ShowChart, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Step line", "step_line", Icons.Default.StackedLineChart, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            
                            Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                            // Group 3: Area Types
                            StyleMenuItem("Area", "area", Icons.Default.AreaChart, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("HLC area", "hlc_area", Icons.Default.AreaChart, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Baseline", "baseline", Icons.Default.HorizontalRule, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            
                            Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                            // Group 4: Others
                            StyleMenuItem("Columns", "columns", Icons.Default.BarChart, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("High-low", "high_low", Icons.Default.VerticalAlignBottom, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            
                            Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                            StyleMenuItem("Volume footprint", "volume_footprint", Icons.Default.FormatAlignLeft, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Time price opportunity", "tpo", Icons.Default.GridView, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Session volume profile", "svp", Icons.Default.AlignHorizontalLeft, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            
                            Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                            StyleMenuItem("Heikin Ashi", "heikin_ashi", Icons.Default.BarChart, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Renko", "renko", Icons.Default.GridView, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Line break", "line_break", Icons.Default.FormatAlignLeft, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Kagi", "kagi", Icons.Default.ShowChart, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Point & figure", "point_figure", Icons.Default.Close, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                            StyleMenuItem("Range", "range", Icons.Default.Height, chartStyle, { onStyleChangeClick(it) }) { showChartTypeMenu = false }
                        }
                    }
                    
                    MT5MenuItem(Icons.Outlined.AccountTree, "Object Tree", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onNavigate("Tools/ObjectTree"); onClose() }
                    
                    Divider(color = Color(0xFF1E222D), modifier = Modifier.padding(vertical = 8.dp))

                    MT5MenuItem(Icons.Default.Fullscreen, "Fullscreen", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onFullscreenClick(); onClose() }
                    MT5MenuItem(Icons.Default.CameraAlt, "Take a snapshot", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onDownloadChartClick(); onClose() }

                    Divider(color = Color(0xFF1E222D), modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Undo/Redo shortcuts
                    MT5MenuItem(Icons.Default.Undo, "Undo", enabled = canUndo, fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { if(canUndo) onUndo(); onClose() }
                    MT5MenuItem(Icons.Default.Redo, "Redo", enabled = canRedo, fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { if(canRedo) onRedo(); onClose() }
                    
                    Divider(color = Color(0xFF1E222D), modifier = Modifier.padding(vertical = 8.dp))
                    
                    MT5MenuItem(Icons.Default.Settings, "Settings", fontSize = fontSize, fontWeight = fontWeight, iconSize = iconSize) { onSettingsClick(); onClose() }
                }
            }
        }
    }
}

@Composable
fun MT5MenuItem(
    icon: ImageVector,
    label: String,
    trailingText: String? = null,
    badge: String? = null,
    hasAds: Boolean = false,
    enabled: Boolean = true,
    fontSize: androidx.compose.ui.unit.TextUnit = 15.sp,
    fontWeight: FontWeight = FontWeight.Medium,
    iconSize: androidx.compose.ui.unit.Dp = 24.dp,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            null,
            tint = if(enabled) Color(0xFFD1D4DC) else Color(0xFF434651),
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            label,
            color = if(enabled) Color.White else Color(0xFF434651),
            fontSize = fontSize,
            fontWeight = fontWeight,
            modifier = Modifier.weight(1f)
        )

        if (trailingText != null && enabled) {
            Text(
                trailingText,
                color = Color(0xFF787B86),
                fontSize = fontSize * 0.9f,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 1
            )
        }
        
        if (badge != null && enabled) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF05252)),
                contentAlignment = Alignment.Center
            ) {
                Text(badge, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        if (hasAds && enabled) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF2962FF).copy(alpha = 0.2f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("Ads", color = Color(0xFF2962FF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ExpandableMenuItem(
    icon: ImageVector,
    label: String,
    isExpanded: Boolean,
    onToggle: (Boolean) -> Unit,
    fontSize: androidx.compose.ui.unit.TextUnit = 15.sp,
    fontWeight: FontWeight = FontWeight.Medium,
    iconSize: androidx.compose.ui.unit.Dp = 24.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isExpanded) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            null,
            tint = Color(0xFFD1D4DC),
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            label,
            color = Color.White,
            fontSize = fontSize,
            fontWeight = fontWeight,
            modifier = Modifier.weight(1f)
        )
        Icon(
            if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            null,
            tint = Color(0xFFD1D4DC),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun MT5SubMenuItem(
    label: String,
    fontSize: androidx.compose.ui.unit.TextUnit = 14.sp,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 56.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            color = Color(0xFFB0B0B0),
            fontSize = fontSize,
            fontWeight = FontWeight.Normal
        )
    }
}

private fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
