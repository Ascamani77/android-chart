package com.trading.app.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.NotificationsNone
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
import com.trading.app.models.ChartSettings
import com.trading.app.models.SymbolInfo

@Composable
fun Header(
    symbol: String,
    timeframe: String,
    chartStyle: String,
    onSymbolClick: () -> Unit,
    onTimeframeClick: (String) -> Unit,
    onStyleChange: (String) -> Unit,
    onIndicatorClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAnalysisClick: () -> Unit,
    onAlertClick: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    canUndo: Boolean,
    canRedo: Boolean,
    onFullscreenClick: () -> Unit,
    onToolSearchClick: () -> Unit,
    onRightPanelToggle: () -> Unit,
    isRightPanelVisible: Boolean,
    onToggleReplay: () -> Unit = {},
    isReplayActive: Boolean = false,
    isAnalyzing: Boolean = false,
    onDownloadChart: () -> Unit = {},
    isCrosshairActive: Boolean = false,
    onCrosshairToggle: () -> Unit = {},
    backgroundColor: Color = Color(0xFF08090C),
    settings: ChartSettings = ChartSettings(),
    isAtBottom: Boolean = true,
    onGoToClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var showTimeframeMenu by remember { mutableStateOf(false) }
    var showStyleMenu by remember { mutableStateOf(false) }
    
    val fontSize = settings.canvas.headerFontSize.sp
    val fontWeight = if (settings.canvas.headerFontBold) FontWeight.Bold else FontWeight.Medium

    // Helper to determine asset info for icons
    val currentSymbolInfo = remember(symbol) {
        val type = when {
            symbol.startsWith("BTC") || symbol.startsWith("ETH") || symbol.startsWith("SOL") -> "Crypto"
            symbol.length == 6 && (symbol.contains("USD") || symbol.contains("EUR") || symbol.contains("JPY") || symbol.contains("GBP")) -> "Forex"
            symbol == "SPX" || symbol == "DJI" || symbol == "IXIC" || symbol == "NIFTY" -> "Index"
            else -> "Stock"
        }
        SymbolInfo(ticker = symbol, name = "", type = type)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (isAtBottom) {
            Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color(0xFF2A2E39))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.4.dp)
                .background(backgroundColor)
                .padding(start = 1.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. STATIC LEFT: Asset Pair Button with Flag
            Row(
                modifier = Modifier.padding(end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, Color(0xFF363A45).copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                        .clickable { onSymbolClick() }
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssetIcon(currentSymbolInfo, size = 28)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(symbol, color = Color.White, fontWeight = FontWeight.Bold, fontSize = fontSize)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(20.dp))
                }
            }

            // 2. SCROLLABLE SECTION: Everything else scrolls sideways
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(scrollState),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Crosshair Toggle Button
                IconButton(
                    onClick = onCrosshairToggle,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.FilterCenterFocus,
                        contentDescription = "Crosshair",
                        tint = if (isCrosshairActive) Color(0xFF2962FF) else Color(0xFFD1D4DC),
                        modifier = Modifier.size(26.dp)
                    )
                }

                HeaderDivider()

                // GoTo Button (Moved from Bottom)
                IconButton(onClick = onGoToClick, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Default.DateRange, "GoTo", tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
                }

                HeaderDivider()

                // Compare button
                IconButton(onClick = { /* Compare */ }, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Outlined.AddCircleOutline, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
                }

                HeaderDivider()

                // Timeframe Quick Selection
                val quickTimeframes = listOf(
                    "5m" to "5m",
                    "15m" to "15m",
                    "30m" to "30m",
                    "1h" to "1h",
                    "4h" to "4h",
                    "D" to "1D",
                    "W" to "1W"
                )

                quickTimeframes.forEach { (display, id) ->
                    val isSelected = timeframe == id || (id == "1D" && timeframe == "D") || (id == "1W" && timeframe == "W")
                    Text(
                        text = display,
                        color = if (isSelected) Color(0xFF2962FF) else Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onTimeframeClick(id) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        fontSize = fontSize,
                        fontWeight = fontWeight
                    )
                }

                // Dropdown for more timeframes
                Box {
                    IconButton(onClick = { showTimeframeMenu = true }, modifier = Modifier.size(42.dp)) {
                        Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(26.dp))
                    }
                    
                    DropdownMenu(
                        expanded = showTimeframeMenu,
                        onDismissRequest = { showTimeframeMenu = false },
                        modifier = Modifier.background(Color(0xFF1E222D)).width(220.dp)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Add, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Add custom interval...", color = Color(0xFFD1D4DC), fontSize = 14.sp)
                                }
                            },
                            onClick = { showTimeframeMenu = false }
                        )

                        TimeframeHeader("TICKS")
                        TimeframeItem("1 tick", "1t", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("10 ticks", "10t", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("100 ticks", "100t", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("1000 ticks", "1000t", timeframe, onTimeframeClick) { showTimeframeMenu = false }

                        Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                        TimeframeHeader("SECONDS")
                        TimeframeItem("1 second", "1s", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("5 seconds", "5s", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("10 seconds", "10s", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("15 seconds", "15s", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("30 seconds", "30s", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("45 seconds", "45s", timeframe, onTimeframeClick) { showTimeframeMenu = false }

                        Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                        TimeframeHeader("MINUTES")
                        TimeframeItem("1 minute", "1m", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("2 minutes", "2m", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("3 minutes", "3m", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("5 minutes", "5m", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("10 minutes", "10m", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("15 minutes", "15m", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("30 minutes", "30m", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("45 minutes", "45m", timeframe, onTimeframeClick) { showTimeframeMenu = false }

                        Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                        TimeframeHeader("HOURS")
                        TimeframeItem("1 hour", "1h", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("2 hours", "2h", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("3 hours", "3h", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("4 hours", "4h", timeframe, onTimeframeClick) { showTimeframeMenu = false }

                        Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                        TimeframeHeader("DAYS")
                        TimeframeItem("1 day", "1D", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("1 week", "1W", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("1 month", "1M", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("3 months", "3M", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("6 months", "6M", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("12 months", "12M", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("Year to date", "YTD", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("5 years", "5Y", timeframe, onTimeframeClick) { showTimeframeMenu = false }

                        Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                        TimeframeHeader("RANGES")
                        TimeframeItem("1 range", "1r", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("10 ranges", "10r", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                        TimeframeItem("100 ranges", "100r", timeframe, onTimeframeClick) { showTimeframeMenu = false }
                    }
                }

                HeaderDivider()

                // Chart Style
                Box {
                    IconButton(onClick = { showStyleMenu = true }, modifier = Modifier.size(48.dp)) {
                        Icon(getStyleIcon(chartStyle), null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
                    }
                    
                    DropdownMenu(
                        expanded = showStyleMenu,
                        onDismissRequest = { showStyleMenu = false },
                        modifier = Modifier.background(Color(0xFF1E222D)).width(240.dp)
                    ) {
                        // Group 1: Candle Types
                        StyleMenuItem("Bars", "bars", Icons.Default.Reorder, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Candles", "candles", Icons.Default.BarChart, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Hollow candles", "hollow_candles", Icons.Default.BarChart, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Volume candles", "volume_candles", Icons.Default.BarChart, chartStyle, onStyleChange) { showStyleMenu = false }
                        
                        Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))
                        
                        // Group 2: Line Types
                        StyleMenuItem("Line", "line", Icons.Default.ShowChart, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Line with markers", "line_markers", Icons.Default.ShowChart, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Step line", "step_line", Icons.Default.StackedLineChart, chartStyle, onStyleChange) { showStyleMenu = false }
                        
                        Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                        // Group 3: Area Types
                        StyleMenuItem("Area", "area", Icons.Default.AreaChart, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("HLC area", "hlc_area", Icons.Default.AreaChart, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Baseline", "baseline", Icons.Default.HorizontalRule, chartStyle, onStyleChange) { showStyleMenu = false }
                        
                        Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                        // Group 4: Others
                        StyleMenuItem("Columns", "columns", Icons.Default.BarChart, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("High-low", "high_low", Icons.Default.VerticalAlignBottom, chartStyle, onStyleChange) { showStyleMenu = false }
                        
                        Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                        StyleMenuItem("Volume footprint", "volume_footprint", Icons.Default.FormatAlignLeft, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Time price opportunity", "tpo", Icons.Default.GridView, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Session volume profile", "svp", Icons.Default.AlignHorizontalLeft, chartStyle, onStyleChange) { showStyleMenu = false }
                        
                        Divider(color = Color(0xFF2A2E39), modifier = Modifier.padding(vertical = 4.dp))

                        StyleMenuItem("Heikin Ashi", "heikin_ashi", Icons.Default.BarChart, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Renko", "renko", Icons.Default.GridView, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Line break", "line_break", Icons.Default.FormatAlignLeft, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Kagi", "kagi", Icons.Default.ShowChart, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Point & figure", "point_figure", Icons.Default.Close, chartStyle, onStyleChange) { showStyleMenu = false }
                        StyleMenuItem("Range", "range", Icons.Default.Height, chartStyle, onStyleChange) { showStyleMenu = false }
                    }
                }

                HeaderDivider()

                // Indicators
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onIndicatorClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.WaterfallChart, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Indicators", color = Color.White, fontSize = fontSize, fontWeight = fontWeight)
                }
                
                // Layout icon next to indicators
                IconButton(onClick = { }, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Default.GridView, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(24.dp))
                }

                HeaderDivider()

                // Alert
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onAlertClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.NotificationsNone, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Alert", color = Color.White, fontSize = fontSize, fontWeight = fontWeight)
                }

                // Replay
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onToggleReplay() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Replay, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Replay", color = Color.White, fontSize = fontSize, fontWeight = fontWeight)
                }

                HeaderDivider()

                // Undo/Redo
                IconButton(onClick = onUndo, enabled = canUndo, modifier = Modifier.size(42.dp)) {
                    Icon(Icons.Default.Undo, null, tint = if(canUndo) Color.White else Color(0xFF434651), modifier = Modifier.size(24.dp))
                }
                IconButton(onClick = onRedo, enabled = canRedo, modifier = Modifier.size(42.dp)) {
                    Icon(Icons.Default.Redo, null, tint = if(canRedo) Color.White else Color(0xFF434651), modifier = Modifier.size(24.dp))
                }

                HeaderDivider()

                // Layout options (Square)
                IconButton(onClick = { }, modifier = Modifier.size(42.dp)) {
                    Icon(Icons.Default.CropSquare, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(24.dp))
                }
                
                // Save Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Save", color = Color(0xFF2962FF), fontSize = fontSize, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF2962FF), modifier = Modifier.size(22.dp))
                }

                HeaderDivider()

                // Utility Icons
                IconButton(onClick = onToolSearchClick, modifier = Modifier.size(42.dp)) {
                    Icon(Icons.Default.Search, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
                }
                IconButton(onClick = onSettingsClick, modifier = Modifier.size(42.dp)) {
                    Icon(Icons.Default.Settings, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
                }
                IconButton(onClick = onFullscreenClick, modifier = Modifier.size(42.dp)) {
                    Icon(Icons.Default.Fullscreen, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
                }
                IconButton(onClick = onDownloadChart, modifier = Modifier.size(42.dp)) {
                    Icon(Icons.Default.CameraAlt, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Trade Button (Black)
                Button(
                    onClick = { /* Trade */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF08090C)),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.height(32.4.dp),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, Color(0xFF2A2E39))
                ) {
                    Text("Trade", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Chat Button (White)
                Button(
                    onClick = { /* Chat */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.height(32.4.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Chat", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        if (isAtBottom) {
            Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color(0xFF2A2E39))
        }
    }
}

@Composable
fun TimeframeHeader(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color(0xFF787B86), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Icon(Icons.Default.KeyboardArrowUp, null, tint = Color(0xFF787B86), modifier = Modifier.size(20.dp))
    }
}

@Composable
fun TimeframeItem(
    label: String,
    id: String,
    currentId: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val isSelected = currentId == id
    DropdownMenuItem(
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    label,
                    color = if (isSelected) Color.White else Color(0xFFD1D4DC),
                    fontSize = 15.sp,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                )
                Icon(
                    Icons.Default.StarOutline,
                    null,
                    tint = Color(0xFF787B86),
                    modifier = Modifier.size(22.dp)
                )
            }
        },
        onClick = {
            onSelect(id)
            onDismiss()
        },
        modifier = Modifier.background(if (isSelected) Color(0xFF2A2E39) else Color.Transparent)
    )
}

@Composable
fun HeaderDivider() {
    Divider(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .height(28.dp)
            .width(1.dp),
        color = Color(0xFF2A2E39)
    )
}

@Composable
fun StyleMenuItem(
    label: String,
    styleId: String,
    icon: ImageVector,
    currentStyle: String,
    onStyleChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val isSelected = currentStyle == styleId
    DropdownMenuItem(
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    null,
                    tint = if (isSelected) Color(0xFF2962FF) else Color(0xFFD1D4DC),
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    label,
                    color = if (isSelected) Color.White else Color(0xFFD1D4DC),
                    fontSize = 15.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        },
        onClick = {
            onStyleChange(styleId)
            onDismiss()
        },
        modifier = Modifier.background(if (isSelected) Color(0xFF2A2E39) else Color.Transparent)
    )
}
