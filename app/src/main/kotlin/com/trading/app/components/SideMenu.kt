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
    onStyleChangeClick: () -> Unit,
    onTimeframeClick: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    canUndo: Boolean,
    canRedo: Boolean,
    onFullscreenClick: () -> Unit,
    onDownloadChartClick: () -> Unit,
    onNavigate: (String) -> Unit = {}
) {
    var expandedDrawings by remember { mutableStateOf(false) }
    
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
                        onToggle = { expandedDrawings = it }
                    )
                    
                    // Drawings Sub-items
                    AnimatedVisibility(
                        visible = expandedDrawings,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Column {
                            MT5SubMenuItem("Tools") { onNavigate("Drawings/Tools"); onClose() }
                            MT5SubMenuItem("Trend lines") { onNavigate("Drawings/TrendLines"); onClose() }
                            MT5SubMenuItem("Gann and Fibonacci") { onNavigate("Drawings/GannFibonacci"); onClose() }
                            MT5SubMenuItem("Patterns") { onNavigate("Drawings/Patterns"); onClose() }
                            MT5SubMenuItem("Forecasting and measurement") { onNavigate("Drawings/Forecasting"); onClose() }
                            MT5SubMenuItem("Geometric shapes") { onNavigate("Drawings/Shapes"); onClose() }
                            MT5SubMenuItem("Annotation") { onNavigate("Drawings/Annotation"); onClose() }
                            MT5SubMenuItem("Visual") { onNavigate("Drawings/Visual"); onClose() }
                        }
                    }
                    
                    
                    // Analysis hub items
                    MT5MenuItem(Icons.Outlined.Dashboard, "Layout setup") { onNavigate("Analysis/LayoutSetup"); onClose() }
                    MT5MenuItem(Icons.Outlined.Edit, "Manage Unnamed") { onNavigate("Analysis/ManageUnnamed"); onClose() }
                    MT5MenuItem(Icons.Outlined.Add, "New") { onNavigate("Analysis/New"); onClose() }
                    MT5MenuItem(Icons.Outlined.CloudUpload, "Save") { onNavigate("Analysis/Save"); onClose() }
                    MT5MenuItem(Icons.Outlined.FolderOpen, "Open") { onNavigate("Analysis/Open"); onClose() }
                    
                    Divider(color = Color(0xFF1E222D), modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Tools Section - Synchronized with Header
                    MT5MenuItem(Icons.Default.Search, "Symbol search", trailingText = symbol) { onSymbolSearchClick(); onClose() }
                    MT5MenuItem(Icons.Default.Schedule, "Interval", trailingText = timeframe) { onTimeframeClick(); onClose() }
                    MT5MenuItem(Icons.Default.WaterfallChart, "Indicators", trailingText = activeIndicators.ifBlank { null }) { onIndicatorClick(); onClose() }
                    MT5MenuItem(Icons.Outlined.CompareArrows, "Compare") { onCompareClick(); onClose() }
                    MT5MenuItem(Icons.Outlined.NotificationsNone, "Alerts") { onAlertClick(); onClose() }
                    MT5MenuItem(Icons.Default.Replay, "Bar Replay") { onBarReplayClick(); onClose() }
                    MT5MenuItem(Icons.Outlined.ViewModule, "Indicator templates") { onNavigate("Tools/IndicatorTemplates"); onClose() }
                    MT5MenuItem(getStyleIcon(chartStyle), "Chart type", trailingText = chartStyle.replace("_", " ").capitalize()) { onStyleChangeClick(); onClose() }
                    MT5MenuItem(Icons.Outlined.AccountTree, "Object Tree") { onNavigate("Tools/ObjectTree"); onClose() }
                    
                    Divider(color = Color(0xFF1E222D), modifier = Modifier.padding(vertical = 8.dp))

                    MT5MenuItem(Icons.Default.Fullscreen, "Fullscreen") { onFullscreenClick(); onClose() }
                    MT5MenuItem(Icons.Default.CameraAlt, "Take a snapshot") { onDownloadChartClick(); onClose() }

                    Divider(color = Color(0xFF1E222D), modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Undo/Redo shortcuts
                    MT5MenuItem(Icons.Default.Undo, "Undo", enabled = canUndo) { if(canUndo) onUndo(); onClose() }
                    MT5MenuItem(Icons.Default.Redo, "Redo", enabled = canRedo) { if(canRedo) onRedo(); onClose() }
                    
                    Divider(color = Color(0xFF1E222D), modifier = Modifier.padding(vertical = 8.dp))
                    
                    MT5MenuItem(Icons.Default.Settings, "Settings") { onSettingsClick(); onClose() }
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
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            label,
            color = if(enabled) Color.White else Color(0xFF434651),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        if (trailingText != null && enabled) {
            Text(
                trailingText,
                color = Color(0xFF787B86),
                fontSize = 14.sp,
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
    onToggle: (Boolean) -> Unit
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
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            label,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
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
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

private fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
