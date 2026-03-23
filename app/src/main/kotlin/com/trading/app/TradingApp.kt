package com.trading.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.ai.client.generativeai.GenerativeModel
import com.trading.app.components.*
import com.trading.app.models.*
import kotlinx.coroutines.launch

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
    
    // Sidebar visibility state - Defaulted to false (hidden)
    var isSidebarVisible by remember { mutableStateOf(false) }
    
    // Recent pairs state
    val recentPairs = remember { mutableStateListOf<Pair<String, String>>() }
    
    LaunchedEffect(symbol, timeframe) {
        val newPair = symbol to timeframe
        if (recentPairs.firstOrNull() != newPair) {
            recentPairs.remove(newPair)
            recentPairs.add(0, newPair)
            if (recentPairs.size > 3) {
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
            TimeZone("America/New_York", "Exchange", ""),
            TimeZone("Pacific/Honolulu", "Honolulu", "(UTC-10)"),
            TimeZone("America/Anchorage", "Anchorage", "(UTC-9)"),
            TimeZone("America/Los_Angeles", "Los Angeles", "(UTC-8)"),
            TimeZone("America/Denver", "Denver", "(UTC-7)"),
            TimeZone("America/Chicago", "Chicago", "(UTC-6)"),
            TimeZone("America/Mexico_City", "Mexico City", "(UTC-6)"),
            TimeZone("America/New_York", "New York", "(UTC-5)"),
            TimeZone("Europe/London", "London", "(UTC+0)"),
            TimeZone("Europe/Paris", "Paris", "(UTC+1)"),
            TimeZone("Asia/Tokyo", "Tokyo", "(UTC+9)"),
            TimeZone("Australia/Sydney", "Sydney", "(UTC+11)")
        )
    }
    var selectedTz by remember { mutableStateOf(timeZones.find { it.label == "Los Angeles" } ?: timeZones[0]) }

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

    // AI Analysis State
    var analysisContent by remember { mutableStateOf("") }
    var isAnalyzing by remember { mutableStateOf(false) }

    // UI State
    var isFullscreen by remember { mutableStateOf(false) }
    var isBottomPanelVisible by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf("Trading Panel") }
    
    // Right Sidebar States
    var isRightSidebarVisible by remember { mutableStateOf(false) }
    var isWatchlistVisible by remember { mutableStateOf(false) }
    
    // Modals State
    var showSymbolSearch by remember { mutableStateOf(false) }
    var showIndicatorModal by remember { mutableStateOf(false) }
    var showGoToDateModal by remember { mutableStateOf(false) }
    var showSettingsModal by remember { mutableStateOf(false) }
    var showToolSearchModal by remember { mutableStateOf(false) }
    var showSideMenu by remember { mutableStateOf(false) }
    var showAlertModal by remember { mutableStateOf(false) }
    var showIndicatorSettingsModal by remember { mutableStateOf<String?>(null) }

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

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF131722)) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (!isFullscreen) {
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
                    onSideMenuClick = { isSidebarVisible = !isSidebarVisible },
                    onRightPanelToggle = { 
                        isWatchlistVisible = !isWatchlistVisible 
                        if (isWatchlistVisible) isRightSidebarVisible = true
                    },
                    isRightPanelVisible = isWatchlistVisible
                )
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
                        onClearDrawings = { drawings.clear() }
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
                            onDrawingUpdate = { /* Update drawing */ },
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
                            isMagnetEnabled = isMagnetEnabled,
                            isLocked = isLocked,
                            isVisible = areDrawingsVisible
                        )
                    }
                    
                    if (isBottomPanelVisible) {
                        TradingPanel(
                            activeTab = activeTab,
                            onTabChange = { activeTab = it },
                            analysisContent = analysisContent,
                            isAnalyzing = isAnalyzing,
                            onRefreshAnalysis = { refreshAnalysis() },
                            onClose = { isBottomPanelVisible = false }
                        )
                    }
                }
                
                // Right Panel (Watchlist, etc.)
                RightPanel(
                    symbol = symbol,
                    onSymbolSelect = { symbol = it },
                    isSidebarVisible = isRightSidebarVisible,
                    isWatchlistVisible = isWatchlistVisible,
                    onSidebarToggle = { isRightSidebarVisible = !isRightSidebarVisible },
                    onWatchlistToggle = { isWatchlistVisible = !isWatchlistVisible }
                )
            }

            BottomBar(
                onRangeClick = { handleRangeChange(it) },
                onGoToClick = { showGoToDateModal = true },
                onTimeZoneClick = { /* Show Timezone Modal */ },
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
                }
            )
        }

        // Modals
        if (showSymbolSearch) {
            SymbolSearchModal(
                onClose = { showSymbolSearch = false },
                onSymbolSelect = { symbol = it }
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
                onGoTo = { /* Go to date logic */ }
            )
        }
        if (showSettingsModal) {
            SettingsModal(
                settings = chartSettings,
                onUpdate = { chartSettings = it },
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
                onClose = { showSideMenu = false }
            )
        }
        if (showAlertModal) {
            AlertModal(
                symbol = symbol,
                onAlertCreate = { userAlerts.add(it) },
                onClose = { showAlertModal = false }
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
    }
}
