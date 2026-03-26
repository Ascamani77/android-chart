package com.trading.app.components

import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.trading.app.data.Mt5Service
import com.trading.app.models.ChartPoint
import com.trading.app.models.ChartSettings
import com.trading.app.models.Drawing
import kotlinx.coroutines.delay
import kotlin.math.hypot
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log

// Data class to match the "Quote" structure
data class SymbolQuote(
    val name: String,
    val lastPrice: Float,
    val change: Float,
    val changePercent: Float,
    val open: Float,
    val high: Float,
    val low: Float,
    val prevClose: Float,
    val bid: Float,
    val ask: Float,
    val volume: Float,
    val spread: Float = 0.2f
)

private fun safeParseColor(colorString: String?, defaultColor: Int = AndroidColor.GRAY): Int {
    if (colorString.isNullOrBlank()) return defaultColor
    return try {
        if (colorString.startsWith("rgba", ignoreCase = true)) {
            val parts = colorString.substringAfter("(").substringBefore(")").split(",")
            if (parts.size < 3) return defaultColor
            val r = parts.getOrNull(0)?.trim()?.toIntOrNull() ?: return defaultColor
            val g = parts.getOrNull(1)?.trim()?.toIntOrNull() ?: return defaultColor
            val b = parts.getOrNull(2)?.trim()?.toIntOrNull() ?: return defaultColor
            val a = (parts.getOrNull(3)?.trim()?.toFloatOrNull() ?: 1f).let { (it * 255).toInt().coerceIn(0, 255) }
            AndroidColor.argb(a, r, g, b)
        } else if (colorString.startsWith("rgb", ignoreCase = true)) {
            val parts = colorString.substringAfter("(").substringBefore(")").split(",")
            if (parts.size < 3) return defaultColor
            val r = parts.getOrNull(0)?.trim()?.toIntOrNull() ?: return defaultColor
            val g = parts.getOrNull(1)?.trim()?.toIntOrNull() ?: return defaultColor
            val b = parts.getOrNull(2)?.trim()?.toIntOrNull() ?: return defaultColor
            AndroidColor.rgb(r, g, b)
        } else {
            AndroidColor.parseColor(colorString)
        }
    } catch (e: Exception) {
        defaultColor
    }
}

private fun applyOpacity(color: Int, opacity: Int): Int {
    val alpha = (opacity / 100f * 255).toInt().coerceIn(0, 255)
    return (color and 0x00FFFFFF) or (alpha shl 24)
}

private fun getFullSymbolName(symbol: String): String {
    return when (symbol.uppercase()) {
        "BTCUSD" -> "Bitcoin / U.S. Dollar"
        "ETHUSD" -> "Ethereum / U.S. Dollar"
        "EURUSD" -> "Euro / U.S. Dollar"
        "USDJPY" -> "U.S. Dollar / Japanese Yen"
        "GBPUSD" -> "British Pound / U.S. Dollar"
        "AUDUSD" -> "Australian Dollar / U.S. Dollar"
        "USDCAD" -> "U.S. Dollar / Canadian Dollar"
        "USDCHF" -> "U.S. Dollar / Swiss Franc"
        "NZDUSD" -> "New Zealand Dollar / U.S. Dollar"
        else -> symbol
    }
}

@Composable
fun TradingChart(
    symbol: String,
    timeframe: String,
    style: String,
    chartSettings: ChartSettings,
    drawings: List<Drawing>,
    onDrawingUpdate: (Drawing) -> Unit,
    activeTool: String?,
    onToolReset: () -> Unit,
    showRsi: Boolean = false,
    rsiPeriod: Int = 14,
    showEma10: Boolean = false,
    ema10Period: Int = 10,
    showEma20: Boolean = false,
    ema20Period: Int = 20,
    showSma1: Boolean = false,
    sma1Period: Int = 21,
    showSma2: Boolean = false,
    sma2Period: Int = 10,
    showVwap: Boolean = false,
    showBb: Boolean = false,
    bbPeriod: Int = 20,
    showAtr: Boolean = false,
    atrPeriod: Int = 14,
    showVolume: Boolean = true,
    isCrosshairActive: Boolean = false,
    onCrosshairToggle: (Boolean) -> Unit = {},
    onVolumeToggle: (Boolean) -> Unit = {},
    onIndicatorSettingsClick: (String) -> Unit = {},
    isMagnetEnabled: Boolean = false,
    isLocked: Boolean = false,
    isVisible: Boolean = true,
    selectedCurrency: String = "USD",
    onCurrencyClick: () -> Unit = {},
    isFullscreen: Boolean = false,
    onFullscreenExit: () -> Unit = {},
    scrollToTimestamp: Long? = null,
    onScrollDone: () -> Unit = {}
) {
    var candleEntries by remember { mutableStateOf<List<CandleEntry>>(emptyList()) }
    var volumeEntries by remember { mutableStateOf<List<BarEntry>>(emptyList()) }

    // State to store live updates from MT5
    var currentQuote by remember { mutableStateOf<SymbolQuote?>(null) }

    var highlightedCandle by remember { mutableStateOf<CandleEntry?>(null) }
    var showOrderDialog by remember { mutableStateOf(false) }
    var orderType by remember { mutableStateOf("BUY") }
    var orderPrice by remember { mutableStateOf(0f) }

    var priceScaleFactor by remember { mutableFloatStateOf(1.0f) }
    var priceCenterOffset by remember { mutableFloatStateOf(0f) }
    var baseCenter by remember { mutableFloatStateOf(0f) }
    var baseRange by remember { mutableFloatStateOf(0f) }

    var lastY by remember { mutableFloatStateOf(0f) }
    var isDraggingAxis by remember { mutableStateOf(false) }
    var isManualMode by remember { mutableStateOf(false) }
    var initialZoomDone by remember { mutableStateOf(false) }

    var showCurrencyMenu by remember { mutableStateOf(false) }
    
    // Crosshair State
    var crosshairX by remember { mutableFloatStateOf(0f) }
    var crosshairY by remember { mutableFloatStateOf(0f) }
    var isTouchingChart by remember { mutableStateOf(false) }
    // Prevent duplicate drawing when creating on ACTION_DOWN
    var crosshairCreatedOnTap by remember { mutableStateOf(false) }
    // Track whether crosshair coordinates have been initialized to chart center
    var crosshairInitialized by remember { mutableStateOf(false) }
    // Track pointer move state and previous crosshair position to distinguish tap vs drag
    var pointerMoved by remember { mutableStateOf(false) }
    var prevCrossX by remember { mutableFloatStateOf(0f) }
    var prevCrossY by remember { mutableFloatStateOf(0f) }
    var crosshairPriceText by remember { mutableStateOf("") }
    var crosshairDateText by remember { mutableStateOf("") }
    var crosshairPriceValue by remember { mutableFloatStateOf(0f) }
    
    // Use updated state for the listener to avoid stale closure
    val currentIsCrosshairActive by rememberUpdatedState(isCrosshairActive)

    // We'll initialize crosshair coordinates to chart center inside the AndroidView update block
    LaunchedEffect(isCrosshairActive) {
        if (isCrosshairActive) {
            // ensure crosshair remains visible until explicitly tapped to draw
            isTouchingChart = true
            crosshairInitialized = false
        } else {
            isTouchingChart = false
            crosshairInitialized = false
        }
    }

    // MT5 Live Connection
    val mt5Service = remember {
        Mt5Service(pcIpAddress = "192.168.1.100") { quote -> // Replace with your actual PC IP
            currentQuote = quote
        }
    }

    LaunchedEffect(symbol) {
        mt5Service.connect()
        mt5Service.subscribe(symbol)
    }

    DisposableEffect(Unit) {
        onDispose {
            mt5Service.disconnect()
        }
    }

    LaunchedEffect(symbol, timeframe) {
        val newCandles = mutableListOf<CandleEntry>()
        val newVolume = mutableListOf<BarEntry>()
        val random = Random()
        var lastClose = 65045.50f
        val now = System.currentTimeMillis()
        val interval = 86400000L // 1 day interval to show date changes clearly

        for (i in 0 until 500) {
            val open = lastClose
            val close = open + (random.nextFloat() - 0.5f) * 100f
            val high = maxOf(open, close) + random.nextFloat() * 50f
            val low = minOf(open, close) - random.nextFloat() * 50f

            val entry = CandleEntry(i.toFloat(), high, low, open, close, now - (500 - i) * interval)
            newCandles.add(entry)
            val volumeColor = if (close >= open) safeParseColor("#089981") else safeParseColor("#F05252")
            newVolume.add(BarEntry(i.toFloat(), random.nextFloat() * 1000f + 200f).apply { data = volumeColor })
            lastClose = close
        }

        candleEntries = newCandles
        volumeEntries = newVolume
        initialZoomDone = false

        while(true) {
            val last = candleEntries.lastOrNull()
            if (last != null) {
                val lp = last.close + (random.nextFloat() - 0.5f) * 20f
                val bid = lp - (random.nextFloat() * 2f)
                val ask = lp + (random.nextFloat() * 2f)
                val ch = lp - last.open
                val chp = (ch / last.open) * 100f

                currentQuote = SymbolQuote(
                    name = symbol,
                    lastPrice = lp,
                    change = ch,
                    changePercent = chp,
                    open = last.open,
                    high = maxOf(last.high, lp),
                    low = minOf(last.low, lp),
                    prevClose = last.open,
                    bid = bid,
                    ask = ask,
                    volume = random.nextFloat() * 5000f
                )
            }
            delay(5000)
        }
    }

    // Chart Area Background Color
    Box(modifier = Modifier.fillMaxSize().background(ComposeColor(safeParseColor(chartSettings.canvas.background)))) {
        AndroidView(
            factory = { context ->
                val chartView = CombinedChart(context)
                val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        priceScaleFactor = 1.0f
                        priceCenterOffset = 0f
                        isManualMode = false
                        chartView.invalidate()
                        return true
                    }
                })

                chartView.apply {
                    description.isEnabled = false
                    legend.isEnabled = false
                    setTouchEnabled(true)
                    isDragEnabled = true
                    isScaleXEnabled = true
                    isScaleYEnabled = true
                    setPinchZoom(true)

                    // Unified background color for chart and axis
                    setDrawGridBackground(false)
                    setBackgroundColor(safeParseColor(chartSettings.canvas.background))

                    setDrawBorders(false)

                    // Remove all offsets to let chart touch edges completely, but add bottom offset for date axis
                    minOffset = 0f
                    setExtraOffsets(0f, 0f, 0f, 15f)

                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(chartSettings.canvas.gridVisible)
                        gridColor = applyOpacity(safeParseColor(chartSettings.canvas.gridColor), chartSettings.canvas.gridOpacity)
                        gridLineWidth = 0.7f
                        textColor = safeParseColor(chartSettings.canvas.scaleTextColor)
                        textSize = ((chartSettings.canvas.scaleFontSize.toFloat() + 6f) * 0.5f) * 1.4f
                        setLabelCount(6, false)
                        yOffset = 8f
                        valueFormatter = object : ValueFormatter() {
                            private val sdf = SimpleDateFormat("dd MMM", Locale.US)
                            override fun getFormattedValue(value: Float): String {
                                val idx = value.toInt()
                                if (idx >= 0 && idx < candleEntries.size) {
                                    val millis = candleEntries[idx].data as? Long
                                    if (millis != null) return sdf.format(Date(millis))
                                }
                                return ""
                            }
                        }
                    }

                    axisRight.apply {
                        isEnabled = true
                        setDrawGridLines(chartSettings.canvas.gridVisible)
                        gridColor = applyOpacity(safeParseColor(chartSettings.canvas.horzGridColor), chartSettings.canvas.gridOpacity)
                        gridLineWidth = 0.7f
                        textColor = safeParseColor(chartSettings.canvas.scaleTextColor)
                        textSize = ((chartSettings.canvas.scaleFontSize.toFloat() + 6f) * 0.5f) * 1.4f
                        setLabelCount(10, false)
                        setDrawAxisLine(false)
                        // Disable auto-scaling so we can control it manually
                        setDrawLabels(true)
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return String.format("%.2f", value)
                            }
                        }
                    }

                    axisLeft.apply {
                        isEnabled = false // Use right axis for price
                    }

                    // Touch listener for axis scaling and manual mode
                    setOnTouchListener { v, event ->
                        val chart = v as CombinedChart
                        if (currentIsCrosshairActive) {
                            when (event.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    Log.d("TradingChart", "ACTION_DOWN event=(${event.x},${event.y})")
                                    pointerMoved = false
                                    prevCrossX = event.x
                                    prevCrossY = event.y
                                    crosshairX = event.x
                                    crosshairY = event.y
                                    try {
                                        val trans = chart.getTransformer(YAxis.AxisDependency.RIGHT)
                                        val point = trans.getValuesByTouchPoint(crosshairX, crosshairY)
                                        crosshairPriceValue = point.y.toFloat()
                                        crosshairPriceText = String.format("%.2f", crosshairPriceValue)
                                        // compute date label from nearest candle index
                                        val idx = point.x.toInt().coerceIn(0, candleEntries.size - 1)
                                        val millis = candleEntries.getOrNull(idx)?.data as? Long
                                        if (millis != null) {
                                            val df = SimpleDateFormat("dd MMM yyyy", Locale.US)
                                            crosshairDateText = df.format(Date(millis))
                                        }
                                    } catch (e: Exception) {
                                        // ignore mapping errors
                                    }
                                    isTouchingChart = true
                                    chart.invalidate()
                                    return@setOnTouchListener true
                                }
                                MotionEvent.ACTION_MOVE -> {
                                    Log.d("TradingChart", "ACTION_MOVE event=(${event.x},${event.y})")
                                    // Update crosshair while dragging
                                    pointerMoved = true
                                    crosshairX = event.x
                                    crosshairY = event.y
                                    try {
                                        val trans = chart.getTransformer(YAxis.AxisDependency.RIGHT)
                                        val point = trans.getValuesByTouchPoint(crosshairX, crosshairY)
                                        crosshairPriceValue = point.y.toFloat()
                                        crosshairPriceText = String.format("%.2f", crosshairPriceValue)
                                        val idx = point.x.toInt().coerceIn(0, candleEntries.size - 1)
                                        val millis = candleEntries.getOrNull(idx)?.data as? Long
                                        if (millis != null) {
                                            val df = SimpleDateFormat("dd MMM yyyy", Locale.US)
                                            crosshairDateText = df.format(Date(millis))
                                        }
                                    } catch (e: Exception) {}
                                    isTouchingChart = true
                                    chart.invalidate()
                                    return@setOnTouchListener true
                                }
                                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                    Log.d("TradingChart", "ACTION_UP event=(${event.x},${event.y}) pointerMoved=$pointerMoved")
                                    // If user didn't move (a tap) and tapped near previous crosshair, create the line
                                    if (!pointerMoved) {
                                        val density = context.resources.displayMetrics.density
                                        val tapThresholdPx = 24f * density
                                        val dist = hypot(event.x - prevCrossX, event.y - prevCrossY)
                                        Log.d("TradingChart", "tap dist=$dist threshold=$tapThresholdPx prev=($prevCrossX,$prevCrossY)")
                                        if (dist <= tapThresholdPx) {
                                            val trans = chart.getTransformer(YAxis.AxisDependency.RIGHT)
                                            val point = trans.getValuesByTouchPoint(event.x, event.y)
                                            val price = point.y.toFloat()

                                            Log.d("TradingChart", "Creating horizontal line at price=$price point.x=${point.x}")

                                            val newDrawing = Drawing(
                                                type = "Horizontal Line",
                                                points = listOf(ChartPoint(time = System.currentTimeMillis(), price = price)),
                                                color = chartSettings.canvas.crosshairColor,
                                                width = chartSettings.canvas.crosshairThickness.toFloat()
                                            )
                                            // Immediately add a LimitLine to the chart for instant feedback
                                            try {
                                                val limitLine = LimitLine(price).apply {
                                                    lineColor = safeParseColor(newDrawing.color)
                                                    lineWidth = newDrawing.width
                                                    label = String.format("%.2f", price)
                                                    // show label on right side near axis
                                                    labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
                                                    textSize = (7f * 1.05f) * 1.4f
                                                }
                                                chart.axisRight.addLimitLine(limitLine)
                                            } catch (e: Exception) {}

                                            onDrawingUpdate(newDrawing)

                                            // deactivate crosshair in parent
                                            onCrosshairToggle(false)
                                            isTouchingChart = false
                                            // clear crosshair labels
                                            crosshairPriceText = String.format("%.2f", price)
                                            // leave date as-is
                                            chart.invalidate()
                                            return@setOnTouchListener true
                                        }
                                    }

                                    // otherwise just end dragging without creating a line
                                    // keep crosshair visible until user taps to create the line
                                    isTouchingChart = true
                                    chart.invalidate()
                                    return@setOnTouchListener true
                                }
                            }
                        }

                        gestureDetector.onTouchEvent(event)
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                lastY = event.y
                                isDraggingAxis = event.x > v.width * 0.88f
                                isDraggingAxis
                            }
                            MotionEvent.ACTION_MOVE -> {
                                val deltaY = event.y - lastY
                                val visibleRange = chart.axisRight.axisMaximum - chart.axisRight.axisMinimum

                                if (isDraggingAxis) {
                                    isManualMode = true
                                    val sensitivity = 0.012f
                                    val scaleFactorChange = Math.exp((-deltaY * sensitivity).toDouble()).toFloat()
                                    priceScaleFactor *= scaleFactorChange
                                    priceScaleFactor = priceScaleFactor.coerceIn(0.00001f, 10000f)
                                } else if (Math.abs(deltaY) > 10) {
                                    isManualMode = true
                                    if (visibleRange > 0) {
                                        val priceDelta = (deltaY / v.height) * visibleRange
                                        priceCenterOffset += priceDelta
                                    }
                                }
                                lastY = event.y
                                chart.invalidate()
                                isDraggingAxis
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                isDraggingAxis = false
                                false
                            }
                            else -> false
                        }
                    }
                }
                chartView
            },
            modifier = Modifier.fillMaxSize()
                .padding(
                    top = chartSettings.canvas.marginTop.dp,
                    bottom = chartSettings.canvas.marginBottom.dp,
                    end = chartSettings.canvas.marginRight.dp
                ),
            update = { chart ->
                val currentCandles = candleEntries
                if (currentCandles.isEmpty()) return@AndroidView

                // Handle Go To Date
                scrollToTimestamp?.let { target ->
                    val index = currentCandles.indexOfFirst { (it.data as? Long ?: 0L) >= target }
                    if (index != -1) {
                        chart.moveViewToX(index.toFloat())
                        onScrollDone()
                    }
                }

                // Initialize crosshair position to chart center when toggled on
                if (isCrosshairActive && !crosshairInitialized) {
                    crosshairX = chart.width / 2f
                    crosshairY = chart.height / 2f
                    isTouchingChart = true
                    crosshairInitialized = true
                } else if (!isCrosshairActive) {
                    crosshairInitialized = false
                }

                // Update chart state based on crosshair mode
                chart.isDragEnabled = !isCrosshairActive
                chart.isScaleXEnabled = !isCrosshairActive
                chart.isScaleYEnabled = !isCrosshairActive

                val combinedData = CombinedData()

                // Apply dynamic styles to match TradingView look
                // Apply background color based on fullChartColor setting or custom background
                val bgColor = when (chartSettings.canvas.fullChartColor) {
                    "Pure Black" -> "#000000"
                    "Dark Blue" -> "#0a2540"
                    "OLED Black" -> "#0a0a0a"
                    else -> chartSettings.canvas.background
                }
                chart.setBackgroundColor(safeParseColor(bgColor))
                chart.xAxis.gridColor = applyOpacity(safeParseColor(chartSettings.canvas.gridColor), chartSettings.canvas.gridOpacity)
                chart.axisRight.gridColor = applyOpacity(safeParseColor(chartSettings.canvas.horzGridColor), chartSettings.canvas.gridOpacity)

                when (style) {
                    "line", "line_markers", "step_line", "kagi" -> {
                        val lineEntries = currentCandles.map { Entry(it.x, it.close) }
                        val lineDataSet = LineDataSet(ArrayList(lineEntries), "Line").apply {
                            axisDependency = YAxis.AxisDependency.RIGHT
                            color = safeParseColor(chartSettings.symbol.upColor)
                            setDrawCircles(style == "line_markers")
                            setDrawValues(false)
                            lineWidth = 2f
                            mode = if (style == "step_line") LineDataSet.Mode.STEPPED else LineDataSet.Mode.LINEAR
                            highLightColor = if (isCrosshairActive) AndroidColor.TRANSPARENT else safeParseColor(chartSettings.canvas.crosshairColor)
                            highlightLineWidth = 0.8f
                            enableDashedHighlightLine(10f, 10f, 0f)
                        }
                        combinedData.setData(LineData(lineDataSet))
                    }
                    "area", "hlc_area", "baseline" -> {
                        val lineEntries = currentCandles.map { Entry(it.x, it.close) }
                        val areaDataSet = LineDataSet(ArrayList(lineEntries), "Area").apply {
                            axisDependency = YAxis.AxisDependency.RIGHT
                            color = safeParseColor(chartSettings.symbol.upColor)
                            setDrawCircles(false)
                            setDrawValues(false)
                            setDrawFilled(true)
                            fillColor = safeParseColor(chartSettings.symbol.upColor)
                            fillAlpha = 50
                            lineWidth = 2f
                            highLightColor = if (isCrosshairActive) AndroidColor.TRANSPARENT else safeParseColor(chartSettings.canvas.crosshairColor)
                            highlightLineWidth = 0.8f
                            enableDashedHighlightLine(10f, 10f, 0f)
                        }
                        combinedData.setData(LineData(areaDataSet))
                    }
                    "columns" -> {
                        val barEntries = currentCandles.map { BarEntry(it.x, it.close) }
                        val columnDataSet = BarDataSet(ArrayList(barEntries), "Columns").apply {
                            axisDependency = YAxis.AxisDependency.RIGHT
                            setDrawValues(false)
                            colors = currentCandles.map { if (it.close >= it.open) safeParseColor(chartSettings.symbol.upColor) else safeParseColor(chartSettings.symbol.downColor) }
                            highLightColor = if (isCrosshairActive) AndroidColor.TRANSPARENT else safeParseColor(chartSettings.canvas.crosshairColor)
                            setHighLightAlpha(255)
                        }
                        combinedData.setData(BarData(columnDataSet))
                    }
                    else -> {
                        val candleDataSet = CandleDataSet(ArrayList(currentCandles), "Candles").apply {
                            axisDependency = YAxis.AxisDependency.RIGHT
                            shadowColorSameAsCandle = true
                            setShadowWidth(0.8f) // Thin TradingView-style wicks

                            when (style) {
                                "hollow_candles" -> {
                                    setIncreasingColor(safeParseColor(chartSettings.symbol.upColor))
                                    setIncreasingPaintStyle(Paint.Style.STROKE)
                                    setDecreasingColor(safeParseColor(chartSettings.symbol.downColor))
                                    setDecreasingPaintStyle(Paint.Style.FILL)
                                }
                                "bars" -> {
                                // Render as compact candle-style bars (visible bodies with thin wicks)
                                // - use filled bodies so each bar has a visible rectangle
                                // - keep wicks slightly thicker for visibility
                                setIncreasingColor(safeParseColor(chartSettings.symbol.upColor))
                                setDecreasingColor(safeParseColor(chartSettings.symbol.downColor))
                                shadowColorSameAsCandle = true
                                setShadowWidth(1.2f)
                                // Draw bodies as filled rectangles (small width)
                                setIncreasingPaintStyle(Paint.Style.FILL)
                                setDecreasingPaintStyle(Paint.Style.FILL)
                                // Make the bodies narrow but visible (closer to TradingView look)
                                // reduce barSpace so bodies are wider and not rendered as single lines
                                barSpace = 0.05f
                                setDrawValues(false)
                                highLightColor = if (isCrosshairActive) AndroidColor.TRANSPARENT else safeParseColor(chartSettings.canvas.crosshairColor)
                                highlightLineWidth = 0.8f
                                enableDashedHighlightLine(10f, 10f, 0f)
                                    }
                                else -> {
                                    // Default "candles" style - clean TradingView look
                                    setIncreasingColor(safeParseColor(chartSettings.symbol.upColor))
                                    setIncreasingPaintStyle(Paint.Style.FILL)
                                    setDecreasingColor(safeParseColor(chartSettings.symbol.downColor))
                                    setDecreasingPaintStyle(Paint.Style.FILL)
                                    setShadowWidth(0.8f)
                                }
                            }

                            setNeutralColor(safeParseColor("#787B86"))
                            setDrawValues(false)
                            barSpace = 0.15f // Balanced spacing
                            highlightLineWidth = 0.8f
                            highLightColor = if (isCrosshairActive) AndroidColor.TRANSPARENT else safeParseColor(chartSettings.canvas.crosshairColor)
                            enableDashedHighlightLine(10f, 10f, 0f) // Dashed crosshair
                            
                            // Apply wick visibility
                            if (!chartSettings.symbol.wickVisible) {
                                setShadowWidth(0f)
                            }
                            
                            // Apply wick colors when wickVisible is true
                            if (chartSettings.symbol.wickVisible && !shadowColorSameAsCandle) {
                                shadowColor = safeParseColor(chartSettings.symbol.wickColorUp)
                            }
                        }
                        
                        // Only show candles if bodyVisible is true
                        if (chartSettings.symbol.bodyVisible) {
                            combinedData.setData(CandleData(candleDataSet))
                        } else {
                            // If body not visible, create empty data
                            combinedData.setData(CandleData(ArrayList()))
                        }
                    }
                }

                // Add Drawings (including the horizontal lines placed by crosshair)
                chart.axisRight.removeAllLimitLines()
                drawings.forEach { drawing ->
                    if (drawing.type == "Horizontal Line" && drawing.points.isNotEmpty()) {
                        val price = drawing.points[0].price
                        val limitLine = LimitLine(price).apply {
                            lineColor = safeParseColor(drawing.color)
                            lineWidth = drawing.width
                            label = String.format("%.2f", price)
                            // show label on right side near axis
                            labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
                            textSize = (7f * 1.05f) * 1.4f
                        }
                        chart.axisRight.addLimitLine(limitLine)
                    }
                }

                // Add Current Price Line (Dashed)
                currentQuote?.let { quote ->
                    val priceLine = LimitLine(quote.lastPrice).apply {
                        lineWidth = 0.8f
                        lineColor = safeParseColor(if (quote.change >= 0) chartSettings.symbol.upColor else chartSettings.symbol.downColor)
                        enableDashedLine(10f, 10f, 0f)
                        label = String.format("%.2f", quote.lastPrice)
                        labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
                        textSize = ((chartSettings.canvas.scaleFontSize.toFloat() + 6f) * 0.5f * 1.05f) * 1.4f
                    }
                    chart.axisRight.addLimitLine(priceLine)
                }

                // Attach volume as a BarDataSet so volume bars render on the chart.
                // We keep volume on the LEFT axis so it doesn't scale with price data.
                if (showVolume && volumeEntries.isNotEmpty()) {
                    try {
                        val volEntriesList = ArrayList<BarEntry>(volumeEntries.map { BarEntry(it.x, it.y).apply { data = it.data } })
                        val volColors = volEntriesList.map { (it.data as? Int) ?: safeParseColor(chartSettings.symbol.upColor) }
                        val volumeDataSet = BarDataSet(volEntriesList, "Volume").apply {
                            axisDependency = YAxis.AxisDependency.LEFT
                            setDrawValues(false)
                            colors = volColors
                            highLightColor = if (isCrosshairActive) AndroidColor.TRANSPARENT else safeParseColor(chartSettings.canvas.crosshairColor)
                        }

                        // If the CombinedData already has BarData (e.g. "columns" style), append the volume dataset to it.
                        val existingBarData = combinedData.barData
                        if (existingBarData != null) {
                            existingBarData.addDataSet(volumeDataSet)
                            existingBarData.notifyDataChanged()
                            combinedData.setData(existingBarData)
                        } else {
                            combinedData.setData(BarData(volumeDataSet))
                        }
                    } catch (e: Exception) {
                        // swallow errors so chart still renders even if volume drawing fails
                    }
                }

                chart.data = combinedData

                // Re-apply background and margin settings
                chart.setBackgroundColor(safeParseColor(bgColor))
                chart.minOffset = 0f
                chart.setExtraOffsets(0f, 0f, 0f, 15f)

                val visibleStart = chart.lowestVisibleX
                val visibleEnd = chart.highestVisibleX

                if (!isManualMode) {
                    val visibleCandles = currentCandles.filter { it.x in visibleStart..visibleEnd }
                    if (visibleCandles.isNotEmpty()) {
                        val min = visibleCandles.minOf { it.low }
                        val max = visibleCandles.maxOf { it.high }
                        baseCenter = (max + min) / 2f
                        baseRange = max - min
                    }
                }

                if (baseRange > 0) {
                    val currentCenter = baseCenter + priceCenterOffset
                    val currentHalfRange = (baseRange / 2f) / priceScaleFactor
                    chart.axisRight.axisMinimum = currentCenter - currentHalfRange
                    chart.axisRight.axisMaximum = currentCenter + currentHalfRange
                }

                val visibleVolumes = volumeEntries.filter { it.x in visibleStart..visibleEnd }
                if (visibleVolumes.isNotEmpty()) {
                    val maxVol = visibleVolumes.maxOf { it.y }
                    chart.axisLeft.axisMaximum = maxVol * 6f
                }

                // Re-apply grid settings to reflect gridType changes
                val showVertGrid = chartSettings.canvas.gridType in listOf("Vert and horz", "Vert")
                chart.xAxis.apply {
                    setDrawGridLines(chartSettings.canvas.gridVisible && showVertGrid)
                    gridColor = applyOpacity(safeParseColor(chartSettings.canvas.gridColor), chartSettings.canvas.gridOpacity)
                    gridLineWidth = 0.7f
                    yOffset = 8f
                }
                val showHorzGrid = chartSettings.canvas.gridType in listOf("Vert and horz", "Horz")
                chart.axisRight.apply {
                    setDrawGridLines(chartSettings.canvas.gridVisible && showHorzGrid)
                    gridColor = applyOpacity(safeParseColor(chartSettings.canvas.horzGridColor), chartSettings.canvas.gridOpacity)
                    gridLineWidth = 0.7f
                }

                // Re-apply Symbol settings to candlestick colors and visibility
                chart.data?.let { data ->
                    if (data is CombinedData) {
                        // Update candle data if it exists
                        data.candleData?.let { candleData ->
                            candleData.dataSets.forEach { dataSet ->
                                if (dataSet is CandleDataSet) {
                                    // Update colors
                                    dataSet.setIncreasingColor(safeParseColor(chartSettings.symbol.upColor))
                                    dataSet.setDecreasingColor(safeParseColor(chartSettings.symbol.downColor))
                                    
                                    // Update wick visibility
                                    if (!chartSettings.symbol.wickVisible) {
                                        dataSet.setShadowWidth(0f)
                                    } else {
                                        dataSet.setShadowWidth(0.8f)
                                    }
                                }
                            }
                        }
                    }
                }

                chart.notifyDataSetChanged()

                if (!initialZoomDone) {
                    val totalCandles = currentCandles.size.toFloat()
                    val viewportWidth = 50f
                    chart.xAxis.axisMaximum = totalCandles + (viewportWidth / 2f)
                    chart.setVisibleXRangeMaximum(viewportWidth)
                    chart.moveViewToX(totalCandles - (viewportWidth / 2f))
                    initialZoomDone = true
                }

                chart.invalidate()
            }
        )

        // Custom Crosshair Drawing Layer
        if (isCrosshairActive && isTouchingChart) {
            ComposeCanvas(modifier = Modifier.fillMaxSize()) {
                val crosshairColor = ComposeColor(safeParseColor(chartSettings.canvas.crosshairColor))
                val strokeWidth = chartSettings.canvas.crosshairThickness.dp.toPx()
                val pathEffect = when (chartSettings.canvas.crosshairLineStyle) {
                    "Dashed" -> PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    "Dotted" -> PathEffect.dashPathEffect(floatArrayOf(2f, 5f), 0f)
                    else -> null
                }
                val padding = 6.dp.toPx()
                val textSize = (8.sp.toPx() * 1.05f) * 1.4f

                // If no touch coordinates available yet, draw at the center
                val drawX = if (crosshairX == 0f && crosshairY == 0f) size.width / 2f else crosshairX
                val drawY = if (crosshairX == 0f && crosshairY == 0f) size.height / 2f else crosshairY

                // Calculate label positions ahead of time for line clipping
                var priceRectLeft = size.width
                var priceRectTop = drawY
                var priceRectWidth = 0f
                var priceRectHeight = textSize + padding
                
                var dateRectLeft = drawX
                var dateRectTop = size.height
                var dateRectWidth = 0f
                var dateRectHeight = textSize + padding

                val pricePaint = android.graphics.Paint().apply {
                    color = AndroidColor.WHITE
                    this.textSize = textSize
                    isAntiAlias = true
                    typeface = android.graphics.Typeface.MONOSPACE
                }

                val datePaint = android.graphics.Paint().apply {
                    color = AndroidColor.WHITE
                    this.textSize = textSize
                    isAntiAlias = true
                    typeface = android.graphics.Typeface.MONOSPACE
                }

                // Calculate price label size - position inside the price axis
                if (crosshairPriceText.isNotEmpty()) {
                    val textWidth = pricePaint.measureText(crosshairPriceText)
                    priceRectWidth = textWidth + padding * 2f
                    // Position at far right, inside the axis area
                    priceRectLeft = size.width - priceRectWidth - 2.dp.toPx()
                    priceRectTop = (drawY - priceRectHeight / 2f).coerceIn(4.dp.toPx(), size.height - priceRectHeight - 4.dp.toPx())
                }

                // Calculate date label size
                if (crosshairDateText.isNotEmpty()) {
                    val textWidth = datePaint.measureText(crosshairDateText)
                    dateRectWidth = textWidth + padding * 2f
                    dateRectLeft = (drawX - dateRectWidth / 2f).coerceIn(4.dp.toPx(), size.width - dateRectWidth - 4.dp.toPx())
                    dateRectTop = size.height - dateRectHeight - 8.dp.toPx()
                }

                // Vertical Line - stop before date label
                val verticalLineEndY = if (crosshairDateText.isNotEmpty()) dateRectTop - 4.dp.toPx() else size.height
                drawLine(
                    color = crosshairColor,
                    start = Offset(drawX, 0f),
                    end = Offset(drawX, verticalLineEndY),
                    strokeWidth = strokeWidth,
                    pathEffect = pathEffect
                )

                // Horizontal Line - extend to the right, reaching the price axis area
                drawLine(
                    color = crosshairColor,
                    start = Offset(0f, drawY),
                    end = Offset(size.width, drawY),
                    strokeWidth = strokeWidth,
                    pathEffect = pathEffect
                )

                // Center Dot
                drawCircle(
                    color = crosshairColor,
                    radius = 4.dp.toPx(),
                    center = Offset(drawX, drawY)
                )

                // Price label at right axis
                if (crosshairPriceText.isNotEmpty()) {
                    drawRoundRect(color = crosshairColor, topLeft = Offset(priceRectLeft, priceRectTop), size = androidx.compose.ui.geometry.Size(priceRectWidth, priceRectHeight), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx()))
                    drawContext.canvas.nativeCanvas.drawText(crosshairPriceText, priceRectLeft + padding, priceRectTop + priceRectHeight - (padding / 2f), pricePaint)
                }

                // Date label at bottom
                if (crosshairDateText.isNotEmpty()) {
                    drawRoundRect(color = crosshairColor, topLeft = Offset(dateRectLeft, dateRectTop), size = androidx.compose.ui.geometry.Size(dateRectWidth, dateRectHeight), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx()))
                    drawContext.canvas.nativeCanvas.drawText(crosshairDateText, dateRectLeft + padding, dateRectTop + dateRectHeight - (padding / 2f), datePaint)
                }
            }
        }

        // OHLC Overlay aligned to the left edge
        if (chartSettings.statusLine.symbol || chartSettings.statusLine.ohlc || chartSettings.statusLine.barChangeValues || chartSettings.statusLine.volume || chartSettings.statusLine.lastDayChange || chartSettings.statusLine.openMarketStatus) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            ) {
                if (chartSettings.statusLine.symbol) {
                    val fullName = getFullSymbolName(symbol)
                    val displayText = when (chartSettings.statusLine.titleMode) {
                        "Name" -> symbol
                        "Symbol" -> symbol
                        "Symbol and name" -> "$symbol · $fullName"
                        else -> symbol
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = displayText,
                            color = ComposeColor.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = timeframe,
                            color = ComposeColor.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
                
                if (chartSettings.statusLine.ohlc) {
                    val lastCandle = candleEntries.lastOrNull()
                    if (lastCandle != null) {
                        Row(modifier = Modifier.padding(top = 2.dp)) {
                            val labelColor = ComposeColor(0xFF9B9B9B)
                            val valueColor = ComposeColor.White
                            val fontSize = 11.sp
                            
                            Text("O", color = labelColor, fontSize = fontSize)
                            Text(String.format(" %.2f", lastCandle.open), color = valueColor, fontSize = fontSize)
                            Spacer(modifier = Modifier.width(6.dp))
                            
                            Text("H", color = labelColor, fontSize = fontSize)
                            Text(String.format(" %.2f", lastCandle.high), color = valueColor, fontSize = fontSize)
                            Spacer(modifier = Modifier.width(6.dp))
                            
                            Text("L", color = labelColor, fontSize = fontSize)
                            Text(String.format(" %.2f", lastCandle.low), color = valueColor, fontSize = fontSize)
                            Spacer(modifier = Modifier.width(6.dp))
                            
                            Text("C", color = labelColor, fontSize = fontSize)
                            Text(String.format(" %.2f", lastCandle.close), color = valueColor, fontSize = fontSize)
                            
                            val change = lastCandle.close - lastCandle.open
                            val changePct = (change / lastCandle.open) * 100
                            val color = if (change >= 0) ComposeColor(0xFF089981) else ComposeColor(0xFFF05252)
                            Text(
                                String.format("  %.2f (%.2f%%)", change, changePct),
                                color = color,
                                fontSize = fontSize
                            )
                        }
                    }
                }
                
                if (chartSettings.statusLine.barChangeValues) {
                    val lastCandle = candleEntries.lastOrNull()
                    if (lastCandle != null && candleEntries.size > 1) {
                        val prevCandle = candleEntries[candleEntries.size - 2]
                        val change = lastCandle.close - prevCandle.close
                        val changePct = (change / prevCandle.close) * 100
                        val color = if (change >= 0) ComposeColor(0xFF089981) else ComposeColor(0xFFF05252)
                        
                        Text(
                            String.format("Bar change: %.2f (%.2f%%)", change, changePct),
                            color = color,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                // Buy/Sell Labels
                if (chartSettings.trading.showBuySellLabels) {
                    currentQuote?.let { quote ->
                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            // Sell Label
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ComposeColor(0xFFF05252))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("SELL", color = ComposeColor.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(String.format("%.2f", quote.bid), color = ComposeColor.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            
                            Spacer(modifier = Modifier.width(4.dp))
                            
                            // Buy Label
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ComposeColor(0xFF089981))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("BUY", color = ComposeColor.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(String.format("%.2f", quote.ask), color = ComposeColor.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
