package com.trading.app.models

import androidx.compose.ui.graphics.vector.ImageVector

data class ColorPickerState(
    val title: String,
    val initialHex: String,
    val isCrosshair: Boolean = false,
    val initialThickness: Int = 1,
    val initialLineStyle: Int = 0,
    val onCrosshairUpdate: ((String, Int, Int) -> Unit)? = null,
    val onAddClick: (() -> Unit)? = null,
    val onColorSelect: (String) -> Unit
)

data class SymbolSettings(
    val upColor: String = "#089981",
    val downColor: String = "#f23645",
    val bodyVisible: Boolean = true,
    val borderVisible: Boolean = true,
    val borderColorUp: String = "#089981",
    val borderColorDown: String = "#f23645",
    val wickVisible: Boolean = true,
    val wickColorUp: String = "#089981",
    val wickColorDown: String = "#f23645",
    val barColorer: Boolean = false, // Color bars based on previous close
    val hlcBars: Boolean = false,
    val thinBars: Boolean = false,
    val openVisible: Boolean = true,
    val highVisible: Boolean = true,
    val lowVisible: Boolean = true,
    val closeVisible: Boolean = true,
    val precision: String = "Default",
    val timezone: String = "(UTC-7) Los Angeles"
)

data class StatusLineSettings(
    val logo: Boolean = true,
    val symbol: Boolean = true,
    val titleMode: String = "Description",
    val openMarketStatus: Boolean = true,
    val ohlc: Boolean = true,
    val barChangeValues: Boolean = true,
    val volume: Boolean = true,
    val lastDayChange: Boolean = false,
    val indicatorTitles: Boolean = true,
    val indicatorInputs: Boolean = true,
    val indicatorValues: Boolean = true,
    val indicatorBackground: Boolean = true,
    val indicatorBackgroundColor: String = "#2a2e39",
    val indicatorBackgroundOpacity: Int = 50
)

data class ScalesSettings(
    val currencyAndUnit: String = "Always visible",
    val scaleModes: String = "Visible on tap",
    val lockRatio: Boolean = false,
    val lockRatioValue: String = "17.2210131",
    val scalesPlacement: String = "Auto",
    val noOverlappingLabels: Boolean = false,
    val plusButton: Boolean = true,
    val countdown: Boolean = true,
    val symbolLabel: String = "Price",
    val symbolLineColor: String = "#FFFFFF",
    val symbolLastValueMode: String = "Value according to scale",
    val highLowMode: String = "Value, line",
    val highLowLineColor: String = "#FFFFFF",
    val indicatorsAndFinancials: String = "Value or name",
    val bidAskMode: String = "Value, line",
    val bidColor: String = "#2962FF",
    val askColor: String = "#F05252",
    val dayOfWeekOnLabels: Boolean = true,
    val dateFormat: String = "Mon 29 Sep '97",
    val timeFormat: String = "24-hours",
    val saveLeftEdge: Boolean = true
)

data class CanvasSettings(
    val backgroundType: String = "Solid",
    val background: String = "#000000",
    val backgroundGradientEnd: String = "#0c0c0d",
    val gridVisible: Boolean = true,
    val gridType: String = "Vert and horz",
    val gridColor: String = "#1f222d",
    val horzGridColor: String = "#1f222d",
    val gridOpacity: Int = 20, // 0 to 100
    val crosshairColor: String = "#758696",
    val crosshairThickness: Int = 1,
    val crosshairLineStyle: String = "Dashed", // Options: "Solid", "Dashed", "Dotted"
    val watermarkVisible: Boolean = false,
    val watermarkType: String = "Replay mode",
    val watermarkColor: String = "#662A2E39",
    val scaleTextColor: String = "#d1d4dc",
    val scaleFontSize: Int = 11,
    val scaleFontBold: Boolean = false,
    val headerFontSize: Int = 14,
    val headerFontBold: Boolean = false,
    val bottomFontSize: Int = 13,
    val bottomFontBold: Boolean = false,
    val sidebarFontSize: Int = 15,
    val sidebarFontBold: Boolean = false,
    val sidebarIconSize: Int = 24,
    val chartItemFontSize: Int = 12,
    val symbolFontSize: Int = 14,
    val scaleLineColor: String = "#2a2e39",
    val navigationButtons: String = "Visible on mouse over",
    val paneButtons: String = "Visible on mouse over",
    val marginTop: Int = 15,
    val marginBottom: Int = 1,
    val marginRight: Int = 10,
    val fullChartColor: String = "Default", // Options: "Default", "Pure Black", "Dark Blue", "OLED Black"
    val headerVisible: Boolean = true,
    val headerVisibility: String = "Always visible" // Options: "Always visible", "Auto-hide"
)

data class TradingSettings(
    val buySellButtons: Boolean = true,
    val showBuySellLabels: Boolean = true,
    val oneClickTrading: Boolean = false,
    val executionSound: Boolean = false,
    val executionSoundVolume: Int = 50,
    val executionSoundType: String = "Alarm Clock",
    val rejectionNotifications: Boolean = false,
    val positionsAndOrders: Boolean = true,
    val reversePositionButton: Boolean = true,
    val projectOrder: Boolean = false,
    val profitLossValue: Boolean = true,
    val positionsMode: String = "Money",
    val bracketsMode: String = "Money",
    val executionMarks: Boolean = true,
    val executionLabels: Boolean = false,
    val extendedPriceLines: Boolean = true,
    val alignment: String = "Right",
    val screenshotVisibility: Boolean = false
)

data class AlertsSettings(
    val alertLines: Boolean = true,
    val alertLinesColor: String = "#26a69a",
    val onlyActiveAlerts: Boolean = true,
    val alertVolume: Boolean = true,
    val volumeLevel: Int = 80,
    val hideToasts: Boolean = true
)

data class EventsSettings(
    val ideas: Boolean = false,
    val ideasMode: String = "All ideas",
    val sessionBreaks: Boolean = false,
    val sessionBreaksColor: String = "#42a5f5",
    val economicEvents: Boolean = true,
    val onlyFutureEvents: Boolean = true,
    val eventsBreaks: Boolean = false,
    val eventsBreaksColor: String = "#363a45",
    val eventsBreaksThickness: Int = 1,
    val eventsBreaksStyle: String = "Dashed",
    val latestNews: Boolean = true,
    val newsNotification: Boolean = false
)

data class ChartSettings(
    val symbol: SymbolSettings = SymbolSettings(),
    val statusLine: StatusLineSettings = StatusLineSettings(),
    val scales: ScalesSettings = ScalesSettings(),
    val canvas: CanvasSettings = CanvasSettings(),
    val trading: TradingSettings = TradingSettings(),
    val alerts: AlertsSettings = AlertsSettings(),
    val events: EventsSettings = EventsSettings()
)

data class OHLCData(
    val time: Long,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Float = 0f
)

data class Drawing(
    val id: String = java.util.UUID.randomUUID().toString(),
    val type: String,
    val points: List<ChartPoint>,
    val color: String = "#2962FF",
    val width: Float = 2f,
    val text: String? = null,
    val isLocked: Boolean = false,
    val isVisible: Boolean = true
)

data class ChartPoint(
    val time: Long,
    val price: Float,
    val x: Float = 0f,
    val y: Float = 0f
)

data class SymbolInfo(
    val ticker: String,
    val name: String,
    val exchange: String = "",
    val type: String = "",
    val price: Float = 0f,
    val change: Float = 0f,
    val changePercent: Float = 0f
)

data class TimeZone(
    val label: String,
    val value: String,
    val offsetLabel: String
)

data class UserAlert(
    val id: String = java.util.UUID.randomUUID().toString(),
    val symbol: String,
    val condition: String,
    val price: Float,
    val message: String,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

data class Position(
    val id: String,
    val symbol: String,
    val type: String, // "buy" or "sell"
    val entryPrice: Float,
    val volume: Float,
    val time: Long
)

data class Indicator(
    val id: String,
    val name: String,
    val description: String,
    val favorite: Boolean = false
)

data class ToolItem(
    val id: String,
    val name: String,
    val icon: ImageVector
)

data class ChartSnapshot(
    val drawings: List<Drawing>,
    val activeIndicators: List<String>
)
