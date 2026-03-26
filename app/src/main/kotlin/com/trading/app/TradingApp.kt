package com.trading.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.ai.client.generativeai.GenerativeModel
import com.trading.app.components.*
import com.trading.app.models.*
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
    val scope = rememberCoroutineScope()
    
    // Core State
    var symbol by remember { mutableStateOf("BTCUSD") }
    var timeframe by remember { mutableStateOf("D") }
    var activeRange by remember { mutableStateOf("1Y") }
    var chartStyle by remember { mutableStateOf("candles") }
    var activeTool by remember { mutableStateOf("cursor") }
    var stayInDrawingMode by remember { mutableStateOf(false) }
    var isMagnetEnabled by remember { mutableStateOf(false) }
    var isLocked by remember { mutableStateOf(false) }
    var areDrawingsVisible by remember { mutableStateOf(true) }
    var isCrosshairActive by remember { mutableStateOf(false) }
    
    // Currency State
    var selectedCurrency by remember { mutableStateOf("USD") }
    var showCurrencyModal by remember { mutableStateOf(false) }
    
    // Sidebar visibility state - Defaulted to false (hidden)
    var isSidebarVisible by remember { mutableStateOf(false) }
    
    // Recent pairs state
    val recentPairs = remember { mutableStateListOf<Pair<String, String>>() }
    
    LaunchedEffect(symbol, timeframe) {
        val newPair = symbol to timeframe
        if (!recentPairs.contains(newPair)) {
            recentPairs.add(0, newPair)
            if (recentPairs.size > 6) {
                recentPairs.removeAt(recentPairs.size - 1)
            }
        }
    }
    
    // Settings & Data
    var chartSettings by remember { mutableStateOf(ChartSettings()) }
    val drawings = remember { mutableStateListOf<Drawing>() }
    val history = remember { mutableStateListOf<ChartSnapshot>() }
    val redoStack = remember { mutableStateListOf<ChartSnapshot>() }
    val userAlerts = remember { mutableStateListOf<UserAlert>() }
    
    // Timezone
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
            TimeZone("(UTC-4) Caracas", "America/Caracas", ""),
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
    var showSideMenu by remember { mutableStateOf(false) }
    var showAlertModal by remember { mutableStateOf(false) }
    var showCaptureModal by remember { mutableStateOf(false) }
    var showIndicatorSettingsModal by remember { mutableStateOf<String?>(null) }
    var showTimeZoneModal by remember { mutableStateOf(false) }

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
        Column(modifier = Modifier.fillMaxSize()) {
            if (!isFullscreen) {
                val isHeaderHidden = chartSettings.canvas.headerVisibility == "Auto-hide" && !isSidebarVisible
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
                        onAlertClick = { showAlertModal = true },
                        onUndo = { /* Undo logic */ },
                        onRedo = { /* Redo logic */ },
                        canUndo = history.isNotEmpty(),
                        canRedo = redoStack.isNotEmpty(),
                        onFullscreenClick = { isFullscreen = true },
                        onToolSearchClick = { showToolSearchModal = true },
                        onSideMenuClick = { showSideMenu = true },
                        onRightPanelToggle = { },
                        isRightPanelVisible = false,
                        onDownloadChart = { showCaptureModal = true },
                        isCrosshairActive = isCrosshairActive,
                        onCrosshairToggle = { isCrosshairActive = !isCrosshairActive },
                        backgroundColor = appBackgroundColor
                    )
                }
            }

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
                            onScrollDone = { targetTimestamp = null }
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
                BottomBar(
                    onRangeClick = { handleRangeChange(it) },
                    onGoToClick = { showGoToDateModal = true },
                    onTimeZoneClick = { showTimeZoneModal = true },
                    selectedTimeZone = selectedTz.label,
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
                    } catch (e: Exception) {
                        android.util.Log.e("TradingApp", "Failed applying settings", e)
                    }
                },
                onClose = { showSettingsModal = false }
            )
        }
        if (showToolSearchModal) {
            ToolSearchModal(
                onToolSelect = { activeTool = it },
                onClose = { showToolSearchModal = false }
            )
        }
        if (showSideMenu) {
            SideMenu(
                symbol = symbol,
                timeframe = timeframe,
                chartStyle = chartStyle,
                activeIndicators = activeIndicatorsString,
                onClose = { showSideMenu = false },
                onIndicatorClick = { showIndicatorModal = true },
                onAlertClick = { showAlertModal = true },
                onSettingsClick = { showSettingsModal = true },
                onBarReplayClick = { /* Toggle replay */ },
                onSymbolSearchClick = { showSymbolSearch = true },
                onCompareClick = { /* showCompareModal = true */ },
                onStyleChangeClick = { /* showStyleModal = true */ },
                onTimeframeClick = { /* showTimeframeModal = true */ },
                onUndo = { /* Undo logic */ },
                onRedo = { /* Redo logic */ },
                canUndo = history.isNotEmpty(),
                canRedo = redoStack.isNotEmpty(),
                onFullscreenClick = { isFullscreen = true },
                onDownloadChartClick = { showCaptureModal = true },
                onNavigate = { destination ->
                    when(destination) {
                        "Drawings" -> isSidebarVisible = !isSidebarVisible
                        "Analysis" -> { activeTab = "AI Analysis"; isBottomPanelVisible = true }
                        "Tools" -> showToolSearchModal = true
                        "Settings" -> showSettingsModal = true
                    }
                }
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
                    showTimeZoneModal = false
                },
                onClose = { showTimeZoneModal = false }
            )
        }
    }
}
