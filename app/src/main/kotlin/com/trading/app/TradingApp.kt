package com.trading.app

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import com.trading.app.components.*
import com.trading.app.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Helper to parse color string to Compose Color
private fun parseComposeColor(colorString: String?, defaultColor: Color = Color(0xFF131722)): Color {
    if (colorString.isNullOrBlank()) return defaultColor
    return try {
        if (colorString.startsWith("rgba", ignoreCase = true)) {
            val parts = colorString.substringAfter("(").substringBefore(")").split(",")
            val r = parts.getOrNull(0)?.trim()?.toIntOrNull() ?: 0
            val g = parts.getOrNull(1)?.trim()?.toIntOrNull() ?: 0
            val b = parts.getOrNull(2)?.trim()?.toIntOrNull() ?: 0
            val a = parts.getOrNull(3)?.trim()?.toFloatOrNull() ?: 1f
            Color(android.graphics.Color.argb((a * 255).toInt(), r, g, b))
        } else if (colorString.startsWith("rgb", ignoreCase = true)) {
            val parts = colorString.substringAfter("(").substringBefore(")").split(",")
            val r = parts.getOrNull(0)?.trim()?.toIntOrNull() ?: 0
            val g = parts.getOrNull(1)?.trim()?.toIntOrNull() ?: 0
            val b = parts.getOrNull(2)?.trim()?.toIntOrNull() ?: 0
            Color(android.graphics.Color.rgb(r, g, b))
        } else {
            Color(android.graphics.Color.parseColor(colorString))
        }
    } catch (e: Exception) {
        defaultColor
    }
}

@Composable
fun TradingApp() {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("trading_prefs", Context.MODE_PRIVATE) }
    val gson = remember { Gson() }
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val safeDrawingInsets = WindowInsets.safeDrawing

    // Core State
    var symbol by remember { mutableStateOf("BTCUSD") }
    var timeframe by remember { mutableStateOf("1h") }
    var activeRange by remember { mutableStateOf("1Y") }
    var chartStyle by remember { mutableStateOf("candles") }
    var activeTool by remember { mutableStateOf("cursor") }
    var stayInDrawingMode by remember { mutableStateOf(false) }
    var isMagnetEnabled by remember { mutableStateOf(false) }
    
    // Loaded from settings
    var chartSettings by remember { 
        mutableStateOf(
            sharedPrefs.getString("chart_settings", null)?.let {
                try { 
                    gson.fromJson(it, ChartSettings::class.java)
                } catch (e: Exception) { ChartSettings() }
            } ?: ChartSettings()
        )
    }

    var isLocked by remember { mutableStateOf(chartSettings.quickActions.isLocked) }
    var areDrawingsVisible by remember { mutableStateOf(true) }
    var isCrosshairActive by remember { mutableStateOf(false) }
    var isReplayActive by remember { mutableStateOf(false) }

    // Currency State
    var selectedCurrency by remember { mutableStateOf("USD") }
    var showCurrencyModal by remember { mutableStateOf(false) }

    // Sidebar visibility state
    var isSidebarVisible by remember { mutableStateOf(chartSettings.quickActions.isSidebarVisible) }

    // Recent pairs state
    val recentPairs = remember { 
        val saved = sharedPrefs.getString("recent_pairs", null)
        val list = if (saved != null) {
            try {
                val type = object : TypeToken<List<Pair<String, String>>>() {}.type
                gson.fromJson<List<Pair<String, String>>>(saved, type)
            } catch (e: Exception) { emptyList() }
        } else emptyList()
        mutableStateListOf<Pair<String, String>>().apply { addAll(list) }
    }

    LaunchedEffect(symbol, timeframe) {
        val newPair = symbol to timeframe
        val existingIndex = recentPairs.indexOfFirst { it.first == symbol && it.second == timeframe }
        if (existingIndex != 0) {
            if (existingIndex != -1) {
                recentPairs.removeAt(existingIndex)
            }
            recentPairs.add(0, newPair)
            if (recentPairs.size > 10) {
                recentPairs.removeAt(recentPairs.size - 1)
            }
            sharedPrefs.edit().putString("recent_pairs", gson.toJson(recentPairs.toList())).apply()
        }
    }

    // Persist settings whenever relevant parts change
    LaunchedEffect(chartSettings, isLocked, isSidebarVisible) {
        val updatedSettings = chartSettings.copy(
            quickActions = chartSettings.quickActions.copy(
                isLocked = isLocked,
                isSidebarVisible = isSidebarVisible
            )
        )
        sharedPrefs.edit().putString("chart_settings", gson.toJson(updatedSettings)).apply()
    }

    // Settings & Data
    val drawings = remember { mutableStateListOf<Drawing>() }
    val history = remember { mutableStateListOf<ChartSnapshot>() }
    val redoStack = remember { mutableStateListOf<ChartSnapshot>() }
    val userAlerts = remember { mutableStateListOf<UserAlert>() }

    // Timezone list
    val timeZones = remember {
        listOf(
            TimeZone("UTC", "UTC", ""),
            TimeZone("Exchange", "Exchange", ""),
            TimeZone("(UTC-10) Honolulu", "Pacific/Honolulu", ""),
            TimeZone("(UTC-8) Anchorage", "America/Anchorage", ""),
            TimeZone("(UTC-8) Juneau", "America/Juneau", ""),
            TimeZone("(UTC-7) Los Angeles", "America/Los_Angeles", ""),
            TimeZone("(UTC-7) Phoenix", "America/Phoenix", ""),
            TimeZone("(UTC-7) Vancouver", "America/Vancouver", ""),
            TimeZone("(UTC-6) Denver", "America/Denver", ""),
            TimeZone("(UTC-6) Mexico City", "America/Mexico_City", ""),
            TimeZone("(UTC-6) San Salvador", "America/El_Salvador", ""),
            TimeZone("(UTC-5) Bogota", "America/Bogota", ""),
            TimeZone("(UTC-5) Chicago", "America/Chicago", ""),
            TimeZone("(UTC-5) Lima", "America/Lima", ""),
            TimeZone("(UTC-4) New York", "America/New_York", ""),
            TimeZone("(UTC-4) Toronto", "America/Toronto", ""),
            TimeZone("(UTC-3) Buenos Aires", "America/Argentina/Buenos_Aires", ""),
            TimeZone("(UTC-3) Halifax", "America/Halifax", ""),
            TimeZone("(UTC-3) Santiago", "America/Santiago", ""),
            TimeZone("(UTC-3) Sao Paulo", "America/Sao_Paulo", ""),
            TimeZone("(UTC-1) Azores", "Atlantic/Azores", ""),
            TimeZone("(UTC) Dublin", "Europe/Dublin", ""),
            TimeZone("(UTC) Lisbon", "Europe/Lisbon", ""),
            TimeZone("(UTC) London", "Europe/London", ""),
            TimeZone("(UTC) Reykjavik", "Atlantic/Reykjavik", ""),
            TimeZone("(UTC+1) Amsterdam", "Europe/Amsterdam", ""),
            TimeZone("(UTC+1) Belgrade", "Europe/Belgrade", ""),
            TimeZone("(UTC+1) Berlin", "Europe/Berlin", ""),
            TimeZone("(UTC+1) Bratislava", "Europe/Bratislava", ""),
            TimeZone("(UTC+1) Brussels", "Europe/Brussels", ""),
            TimeZone("(UTC+1) Budapest", "Europe/Budapest", ""),
            TimeZone("(UTC+1) Casablanca", "Africa/Casablanca", ""),
            TimeZone("(UTC+1) Copenhagen", "Europe/Copenhagen", ""),
            TimeZone("(UTC+1) Lagos", "Africa/Lagos", ""),
            TimeZone("(UTC+1) Ljubljana", "Europe/Ljubljana", ""),
            TimeZone("(UTC+1) Luxembourg", "Europe/Luxembourg", ""),
            TimeZone("(UTC+1) Madrid", "Europe/Madrid", ""),
            TimeZone("(UTC+1) Malta", "Europe/Malta", ""),
            TimeZone("(UTC+1) Oslo", "Europe/Oslo", ""),
            TimeZone("(UTC+1) Paris", "Europe/Paris", ""),
            TimeZone("(UTC+1) Prague", "Europe/Prague", ""),
            TimeZone("(UTC+1) Rome", "Europe/Rome", ""),
            TimeZone("(UTC+1) Stockholm", "Europe/Stockholm", ""),
            TimeZone("(UTC+1) Tunis", "Africa/Tunis", ""),
            TimeZone("(UTC+1) Vienna", "Europe/Vienna", ""),
            TimeZone("(UTC+1) Warsaw", "Europe/Warsaw", ""),
            TimeZone("(UTC+1) Zagreb", "Europe/Zagreb", ""),
            TimeZone("(UTC+1) Zurich", "Europe/Zurich", ""),
            TimeZone("(UTC+2) Athens", "Europe/Athens", ""),
            TimeZone("(UTC+2) Bucharest", "Europe/Bucharest", ""),
            TimeZone("(UTC+2) Cairo", "Africa/Cairo", ""),
            TimeZone("(UTC+2) Helsinki", "Europe/Helsinki", ""),
            TimeZone("(UTC+2) Jerusalem", "Asia/Jerusalem", ""),
            TimeZone("(UTC+2) Johannesburg", "Africa/Johannesburg", ""),
            TimeZone("(UTC+2) Nicosia", "Asia/Nicosia", ""),
            TimeZone("(UTC+2) Riga", "Europe/Riga", ""),
            TimeZone("(UTC+2) Sofia", "Europe/Sofia", ""),
            TimeZone("(UTC+2) Tallinn", "Europe/Tallinn", ""),
            TimeZone("(UTC+2) Vilnius", "Europe/Vilnius", ""),
            TimeZone("(UTC+3) Bahrain", "Asia/Bahrain", ""),
            TimeZone("(UTC+3) Istanbul", "Europe/Istanbul", ""),
            TimeZone("(UTC+3) Kuwait", "Asia/Kuwait", ""),
            TimeZone("(UTC+3) Moscow", "Europe/Moscow", ""),
            TimeZone("(UTC+3) Nairobi", "Africa/Nairobi", ""),
            TimeZone("(UTC+3) Qatar", "Asia/Qatar", ""),
            TimeZone("(UTC+3) Riyadh", "Asia/Riyadh", ""),
            TimeZone("(UTC+3:30) Tehran", "Asia/Tehran", ""),
            TimeZone("(UTC+4) Dubai", "Asia/Dubai", ""),
            TimeZone("(UTC+4) Muscat", "Asia/Muscat", ""),
            TimeZone("(UTC+4:30) Kabul", "Asia/Kabul", ""),
            TimeZone("(UTC+5) Ashgabat", "Asia/Ashgabat", ""),
            TimeZone("(UTC+5) Astana", "Asia/Almaty", ""),
            TimeZone("(UTC+5) Karachi", "Asia/Karachi", ""),
            TimeZone("(UTC+5:30) Colombo", "Asia/Colombo", ""),
            TimeZone("(UTC+5:30) Kolkata", "Asia/Kolkata", ""),
            TimeZone("(UTC+5:45) Kathmandu", "Asia/Kathmandu", ""),
            TimeZone("(UTC+6) Dhaka", "Asia/Dhaka", ""),
            TimeZone("(UTC+6:30) Yangon", "Asia/Yangon", ""),
            TimeZone("(UTC+7) Bangkok", "Asia/Bangkok", ""),
            TimeZone("(UTC+7) Ho Chi Minh", "Asia/Ho_Chi_Minh", ""),
            TimeZone("(UTC+7) Jakarta", "Asia/Jakarta", ""),
            TimeZone("(UTC+8) Chongqing", "Asia/Chongqing", ""),
            TimeZone("(UTC+8) Hong Kong", "Asia/Hong_Kong", ""),
            TimeZone("(UTC+8) Kuala Lumpur", "Asia/Kuala_Lumpur", ""),
            TimeZone("(UTC+8) Manila", "Asia/Manila", ""),
            TimeZone("(UTC+8) Perth", "Australia/Perth", ""),
            TimeZone("(UTC+8) Shanghai", "Asia/Shanghai", ""),
            TimeZone("(UTC+8) Singapore", "Asia/Singapore", ""),
            TimeZone("(UTC+8) Taipei", "Asia/Taipei", ""),
            TimeZone("(UTC+9) Seoul", "Asia/Seoul", ""),
            TimeZone("(UTC+9) Tokyo", "Asia/Tokyo", ""),
            TimeZone("(UTC+10) Brisbane", "Australia/Brisbane", ""),
            TimeZone("(UTC+10:30) Adelaide", "Australia/Adelaide", ""),
            TimeZone("(UTC+11) Sydney", "Australia/Sydney", ""),
            TimeZone("(UTC+12) Norfolk Island", "Pacific/Norfolk", ""),
            TimeZone("(UTC+12) New Zealand", "Pacific/Auckland", ""),
            TimeZone("(UTC+13) New Zealand", "Pacific/Auckland", ""),
            TimeZone("(UTC+13) Tokelau", "Pacific/Tokelau", ""),
            TimeZone("(UTC+13:45) Chatham Islands", "Pacific/Chatham", "")
        )
    }
    var selectedTz by remember { mutableStateOf(timeZones.find { it.label == "(UTC-7) Los Angeles" } ?: timeZones[0]) }

    // Indicator State
    var showRsi by remember { mutableStateOf(false) }
    var rsiPeriod by remember { mutableIntStateOf(14) }
    var showEma10 by remember { mutableStateOf(false) }
    var ema10Period by remember { mutableIntStateOf(10) }
    var showEma20 by remember { mutableStateOf(false) }
    var ema20Period by remember { mutableIntStateOf(20) }
    var showSma1 by remember { mutableStateOf(false) }
    var sma1Period by remember { mutableIntStateOf(21) }
    var showSma2 by remember { mutableStateOf(false) }
    var sma2Period by remember { mutableIntStateOf(10) }
    var showVwap by remember { mutableStateOf(false) }
    var showBb by remember { mutableStateOf(false) }
    var bbPeriod by remember { mutableIntStateOf(20) }
    var showAtr by remember { mutableStateOf(false) }
    var atrPeriod by remember { mutableIntStateOf(14) }
    var showVolume by remember { mutableStateOf(true) }

    // Navigation and UI State
    var isFullscreen by remember { mutableStateOf(false) }
    var isBottomPanelVisible by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf("Trading Panel") }
    var analysisContent by remember { mutableStateOf("Click refresh to generate analysis...") }
    var isAnalyzing by remember { mutableStateOf(false) }

    // Modal Visibility
    var showSymbolSearch by remember { mutableStateOf(false) }
    var showIndicatorModal by remember { mutableStateOf(false) }
    var showGoToDateModal by remember { mutableStateOf(false) }
    var targetTimestamp by remember { mutableStateOf<Long?>(null) }
    var showSettingsModal by remember { mutableStateOf(false) }
    var showToolSearchModal by remember { mutableStateOf(false) }
    var showAlertModal by remember { mutableStateOf(false) }
    var showCaptureModal by remember { mutableStateOf(false) }
    var showIndicatorSettingsModal by remember { mutableStateOf<String?>(null) }
    var showTimeZoneModal by remember { mutableStateOf(false) }

    // Quick Actions State
    var showQuickActions by remember { mutableStateOf(false) }
    var quickActionsModalOffset by remember { 
        mutableStateOf(IntOffset(chartSettings.quickActions.modalX, chartSettings.quickActions.modalY)) 
    }
    var quickActionsButtonOffset by remember { 
        mutableStateOf(IntOffset(chartSettings.quickActions.buttonX, chartSettings.quickActions.buttonY)) 
    }
    var isTimezonePaneVisible by remember { mutableStateOf(chartSettings.quickActions.isTimezoneVisible) }

    // Responsive Reposition & Safe Area Awareness: Clamp offsets to screen boundaries
    LaunchedEffect(configuration.screenWidthDp, configuration.screenHeightDp, safeDrawingInsets) {
        val leftInset = safeDrawingInsets.getLeft(density, layoutDirection)
        val topInset = safeDrawingInsets.getTop(density)
        val rightInset = safeDrawingInsets.getRight(density, layoutDirection)
        val bottomInset = safeDrawingInsets.getBottom(density)

        val screenWidthPx = with(density) { configuration.screenWidthDp.dp.roundToPx() }
        val screenHeightPx = with(density) { configuration.screenHeightDp.dp.roundToPx() }
        
        val buttonSizePx = with(density) { 70.dp.roundToPx() }
        val modalWidthPx = with(density) { 260.dp.roundToPx() }
        val modalHeightPx = with(density) { 500.dp.roundToPx() }

        val minX = leftInset
        val maxX = (screenWidthPx - rightInset - buttonSizePx).coerceAtLeast(minX)
        val minY = topInset
        val maxY = (screenHeightPx - bottomInset - buttonSizePx).coerceAtLeast(minY)

        val clampedButtonX = quickActionsButtonOffset.x.coerceIn(minX, maxX)
        val clampedButtonY = quickActionsButtonOffset.y.coerceIn(minY, maxY)
        
        if (clampedButtonX != quickActionsButtonOffset.x || clampedButtonY != quickActionsButtonOffset.y) {
            quickActionsButtonOffset = IntOffset(clampedButtonX, clampedButtonY)
        }

        val modalMaxX = (screenWidthPx - rightInset - modalWidthPx).coerceAtLeast(minX)
        val modalMaxY = (screenHeightPx - bottomInset - modalHeightPx).coerceAtLeast(minY)

        val clampedModalX = quickActionsModalOffset.x.coerceIn(minX, modalMaxX)
        val clampedModalY = quickActionsModalOffset.y.coerceIn(minY, modalMaxY)

        if (clampedModalX != quickActionsModalOffset.x || clampedModalY != quickActionsModalOffset.y) {
            quickActionsModalOffset = IntOffset(clampedModalX, clampedModalY)
        }
    }

    // Update coordinates in persistent state
    LaunchedEffect(quickActionsButtonOffset, quickActionsModalOffset, isTimezonePaneVisible) {
        chartSettings = chartSettings.copy(
            quickActions = chartSettings.quickActions.copy(
                buttonX = quickActionsButtonOffset.x,
                buttonY = quickActionsButtonOffset.y,
                modalX = quickActionsModalOffset.x,
                modalY = quickActionsModalOffset.y,
                isTimezoneVisible = isTimezonePaneVisible
            )
        )
    }

    val generativeModel = remember {
        val apiKey = try { System.getenv("GEMINI_API_KEY") ?: "" } catch (e: Exception) { "" }
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )
    }

    fun refreshAnalysis() {
        if (generativeModel.apiKey.isEmpty()) {
            analysisContent = "API Key not found. Please set GEMINI_API_KEY."
            return
        }
        scope.launch {
            isAnalyzing = true
            try {
                val prompt = """
                    You are a professional technical analyst. Analyze the following OHLC data for ${symbol} (${timeframe}):
                    [Mock OHLC data for analysis]
                    
                    Provide a concise analysis including:
                    1. Sentiment (Bullish/Bearish/Neutral)
                    2. Key Support and Resistance levels
                    3. Potential trade setup
                """.trimIndent()
                val response = generativeModel.generateContent(prompt)
                analysisContent = response.text ?: "Analysis failed."
            } catch (e: Exception) {
                analysisContent = "Error: ${e.message}"
            } finally {
                isAnalyzing = false
            }
        }
    }

    fun handleRangeChange(range: String) {
        activeRange = range
        timeframe = when (range) {
            "1D" -> "5m"
            "5D" -> "15m"
            "1M" -> "1h"
            "3M" -> "4h"
            "5Y", "All" -> "W"
            else -> "D"
        }
    }

    fun handleIndicatorSelect(id: String) {
        when (id) {
            "rsi" -> showRsi = !showRsi
            "ema" -> {
                if (!showEma10) showEma10 = true
                else if (!showEma20) showEma20 = true
                else { showEma10 = false; showEma20 = false }
            }
            "sma" -> {
                if (!showSma1) showSma1 = true
                else if (!showSma2) showSma2 = true
                else { showSma1 = false; showSma2 = false }
            }
            "vwap" -> showVwap = !showVwap
            "bb" -> showBb = !showBb
            "vol" -> showVolume = !showVolume
            "atr" -> showAtr = !showAtr
        }
    }

    val activeIndicatorsString = remember(showRsi, showEma10, showEma20, showSma1, showSma2, showVwap, showBb, showAtr, showVolume) {
        val list = mutableListOf<String>()
        if (showRsi) list.add("RSI")
        if (showEma10) list.add("EMA10")
        if (showEma20) list.add("EMA20")
        if (showSma1) list.add("SMA1")
        if (showSma2) list.add("SMA2")
        if (showVwap) list.add("VWAP")
        if (showBb) list.add("BB")
        if (showAtr) list.add("ATR")
        if (showVolume) list.add("VOL")
        list.joinToString(", ")
    }

    val appBackgroundColor = when (chartSettings.canvas.fullChartColor) {
        "Pure Black" -> Color.Black
        "Dark Blue" -> Color(0xFF131722)
        else -> parseComposeColor(chartSettings.canvas.background)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = appBackgroundColor) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                Row(modifier = Modifier.weight(1f)) {
                    if (!isFullscreen && isSidebarVisible) {
                        Sidebar(
                            activeTool = activeTool,
                            onToolClick = { activeTool = it },
                            onToolSearchClick = { showToolSearchModal = true },
                            stayInDrawingMode = stayInDrawingMode,
                            onStayInModeToggle = { stayInDrawingMode = !stayInDrawingMode },
                            isMagnetEnabled = isMagnetEnabled,
                            onMagnetToggle = { isMagnetEnabled = !isMagnetEnabled },
                            isLocked = isLocked,
                            onLockToggle = { isLocked = !isLocked },
                            isVisible = areDrawingsVisible,
                            onVisibilityToggle = { areDrawingsVisible = !areDrawingsVisible },
                            onClearDrawings = { drawings.clear() },
                            backgroundColor = appBackgroundColor
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Box(modifier = Modifier.weight(1f)) {
                            TradingChart(
                                symbol = symbol,
                                timeframe = timeframe,
                                style = chartStyle,
                                chartSettings = chartSettings,
                                drawings = drawings,
                                onDrawingUpdate = { drawing ->
                                    val index = drawings.indexOfFirst { it.id == drawing.id }
                                    if (index != -1) drawings[index] = drawing else drawings.add(drawing)
                                },
                                activeTool = activeTool,
                                onToolReset = { if (!stayInDrawingMode) activeTool = "cursor" },
                                showRsi = showRsi,
                                rsiPeriod = rsiPeriod,
                                showEma10 = showEma10,
                                ema10Period = ema10Period,
                                showEma20 = showEma20,
                                ema20Period = ema20Period,
                                showSma1 = showSma1,
                                sma1Period = sma1Period,
                                showSma2 = showSma2,
                                sma2Period = sma2Period,
                                showVwap = showVwap,
                                showBb = showBb,
                                bbPeriod = bbPeriod,
                                showAtr = showAtr,
                                atrPeriod = atrPeriod,
                                showVolume = showVolume,
                                onVolumeToggle = { showVolume = it },
                                onIndicatorSettingsClick = { showIndicatorSettingsModal = it },
                                isMagnetEnabled = isMagnetEnabled,
                                isLocked = isLocked,
                                isVisible = areDrawingsVisible,
                                isCrosshairActive = isCrosshairActive,
                                onCrosshairToggle = { isCrosshairActive = it },
                                selectedCurrency = selectedCurrency,
                                onCurrencyClick = { showCurrencyModal = true },
                                isFullscreen = isFullscreen,
                                onFullscreenExit = { isFullscreen = false },
                                scrollToTimestamp = targetTimestamp,
                                onScrollDone = { targetTimestamp = null },
                                onLongPress = {
                                    showSettingsModal = true
                                },
                                onSettingsClick = {
                                    showSettingsModal = true
                                },
                                selectedTimeZone = selectedTz.label
                            )
                        }

                        if (!isFullscreen && isBottomPanelVisible) {
                            TradingPanel(
                                activeTab = activeTab,
                                onTabChange = { activeTab = it },
                                analysisContent = analysisContent,
                                isAnalyzing = isAnalyzing,
                                onRefreshAnalysis = { refreshAnalysis() },
                                onClose = { isBottomPanelVisible = false },
                                backgroundColor = appBackgroundColor
                            )
                        }
                    }
                }

                if (!isFullscreen) {
                    val isHeaderHidden = !chartSettings.canvas.headerVisible || (chartSettings.canvas.headerVisibility == "Auto-hide" && !isSidebarVisible)
                    AnimatedVisibility(
                        visible = !isHeaderHidden,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Header(
                            symbol = symbol,
                            timeframe = timeframe,
                            chartStyle = chartStyle,
                            onSymbolClick = { showSymbolSearch = true },
                            onTimeframeClick = { timeframe = it },
                            onStyleChange = { chartStyle = it },
                            onIndicatorClick = { showIndicatorModal = true },
                            onSettingsClick = { showSettingsModal = true },
                            onAnalysisClick = { refreshAnalysis() },
                            onUndo = { /* Undo logic */ },
                            onRedo = { /* Redo logic */ },
                            canUndo = history.isNotEmpty(),
                            canRedo = redoStack.isNotEmpty(),
                            onToolSearchClick = { showToolSearchModal = true },
                            onRightPanelToggle = { },
                            isRightPanelVisible = false,
                            onDownloadChart = { showCaptureModal = true },
                            backgroundColor = appBackgroundColor,
                            isAtBottom = true,
                            onGoToClick = { showGoToDateModal = true }
                        )
                    }
                }

                if (!isFullscreen && isTimezonePaneVisible) {
                    BottomBar(
                        onRangeClick = { handleRangeChange(it) },
                        onGoToClick = { showGoToDateModal = true },
                        onTabClick = {
                            if (activeTab == it && isBottomPanelVisible) {
                                isBottomPanelVisible = false
                            } else {
                                activeTab = it
                                isBottomPanelVisible = true
                            }
                        },
                        activeTab = if (isBottomPanelVisible) activeTab else null,
                        recentPairs = recentPairs,
                        currentSymbol = symbol,
                        currentTimeframe = timeframe,
                        onPairSelect = { s: String, t: String ->
                            symbol = s
                            timeframe = t
                        },
                        backgroundColor = appBackgroundColor
                    )
                }
            }

            // Backdrop for Quick Actions
            if (showQuickActions) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { showQuickActions = false }
                        )
                )
            }

            // Floating Quick Actions Button
            QuickActionsButton(
                onClick = { showQuickActions = !showQuickActions },
                offset = quickActionsButtonOffset,
                onOffsetChange = { 
                    quickActionsButtonOffset = it
                },
                isLocked = isLocked,
                isModalOpen = showQuickActions
            )

            // Quick Actions Modal
            if (showQuickActions) {
                QuickActionsModal(
                    isFullscreen = isFullscreen,
                    onFullscreenToggle = { isFullscreen = !isFullscreen },
                    isHeaderVisible = chartSettings.canvas.headerVisible,
                    onHeaderToggle = {
                        chartSettings = chartSettings.copy(
                            canvas = chartSettings.canvas.copy(
                                headerVisible = !chartSettings.canvas.headerVisible
                            )
                        )
                    },
                    isBottomMenuVisible = isBottomPanelVisible,
                    onBottomMenuToggle = { isBottomPanelVisible = !isBottomPanelVisible },
                    onSettingsClick = { showSettingsModal = true; showQuickActions = false },
                    onDrawingsClick = { isSidebarVisible = !isSidebarVisible; showQuickActions = false },
                    onChartTypeClick = { /* Show chart type selection or cycle types */ 
                        chartStyle = when(chartStyle) {
                            "candles" -> "bars"
                            "bars" -> "line"
                            "line" -> "area"
                            else -> "candles"
                        }
                    },
                    isTimezoneVisible = isTimezonePaneVisible,
                    onTimezoneToggle = { isTimezonePaneVisible = !isTimezonePaneVisible },
                    isCrosshairActive = isCrosshairActive,
                    onCrosshairToggle = { isCrosshairActive = !isCrosshairActive },
                    onAlertClick = { showAlertModal = true; showQuickActions = false },
                    onReplayClick = { isReplayActive = !isReplayActive; showQuickActions = false },
                    isReplayActive = isReplayActive,
                    isLocked = isLocked,
                    onLockToggle = { isLocked = !isLocked },
                    onClose = { showQuickActions = false },
                    offset = quickActionsModalOffset,
                    onOffsetChange = { quickActionsModalOffset = it }
                )
            }
        }

        // Modals
        if (showSymbolSearch) {
            SymbolSearchModal(
                onClose = { showSymbolSearch = false },
                onSymbolSelect = { symbol = it }
            )
        }
        if (showCurrencyModal) {
            CurrencySelectionModal(
                currentSymbol = symbol,
                selectedCurrency = selectedCurrency,
                onCurrencySelect = { selectedCurrency = it },
                onClose = { showCurrencyModal = false }
            )
        }
        if (showIndicatorModal) {
            IndicatorsModal(
                onClose = { showIndicatorModal = false },
                onIndicatorSelect = { handleIndicatorSelect(it) }
            )
        }
        if (showGoToDateModal) {
            GoToDateModal(
                onClose = { showGoToDateModal = false },
                onGoTo = { timestamp ->
                    targetTimestamp = timestamp
                    showGoToDateModal = false
                }
            )
        }
        if (showSettingsModal) {
            SettingsModal(
                settings = chartSettings,
                onUpdate = {
                    try {
                        chartSettings = it
                        // Update selectedTz when chartSettings.symbol.timezone changes
                        val newTz = timeZones.find { tz -> tz.label == it.symbol.timezone }
                        if (newTz != null) {
                            selectedTz = newTz
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("TradingApp", "Failed applying settings", e)
                    }
                },
                onTimeZoneClick = { showTimeZoneModal = true },
                onClose = { showSettingsModal = false }
            )
        }
        if (showToolSearchModal) {
            ToolSearchModal(
                onToolSelect = { activeTool = it },
                onClose = { showToolSearchModal = false }
            )
        }
        if (showAlertModal) {
            AlertModal(
                symbol = symbol,
                onAlertCreate = { userAlerts.add(it) },
                onClose = { showAlertModal = false }
            )
        }
        if (showCaptureModal) {
            ChartCaptureModal(
                onClose = { showCaptureModal = false },
                onDownload = { /* Implement download */ },
                onShare = { /* Implement share */ }
            )
        }
        showIndicatorSettingsModal?.let { indicatorId ->
            IndicatorSettingsModal(
                indicatorId = indicatorId,
                period = when(indicatorId) {
                    "RSI" -> rsiPeriod
                    "EMA10" -> ema10Period
                    "EMA20" -> ema20Period
                    "SMA1" -> sma1Period
                    "SMA2" -> sma2Period
                    "BB" -> bbPeriod
                    "ATR" -> atrPeriod
                    "Volume" -> 20 // Mock value for volume
                    else -> 14
                },
                onPeriodChange = {
                    when(indicatorId) {
                        "RSI" -> rsiPeriod = it
                        "EMA10" -> ema10Period = it
                        "EMA20" -> ema20Period = it
                        "SMA1" -> sma1Period = it
                        "SMA2" -> sma2Period = it
                        "BB" -> bbPeriod = it
                        "ATR" -> atrPeriod = it
                    }
                },
                onClose = { showIndicatorSettingsModal = null }
            )
        }
        if (showTimeZoneModal) {
            TimeZoneSelectionModal(
                timeZones = timeZones,
                selectedTimeZone = selectedTz,
                onTimeZoneSelect = {
                    selectedTz = it
                    // Also update chartSettings.symbol.timezone
                    chartSettings = chartSettings.copy(
                        symbol = chartSettings.symbol.copy(timezone = it.label)
                    )
                    showTimeZoneModal = false
                },
                onClose = { showTimeZoneModal = false }
            )
        }
    }
}
