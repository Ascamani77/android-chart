package com.trading.app.components

import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.trading.app.data.Mt5Service
import com.trading.app.models.ChartSettings
import com.trading.app.models.Drawing
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

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
            val r = parts[0].trim().toInt()
            val g = parts[1].trim().toInt()
            val b = parts[2].trim().toInt()
            val a = (parts.getOrNull(3)?.trim()?.toFloat() ?: 1f).let { (it * 255).toInt() }
            AndroidColor.argb(a, r, g, b)
        } else if (colorString.startsWith("rgb", ignoreCase = true)) {
            val parts = colorString.substringAfter("(").substringBefore(")").split(",")
            val r = parts[0].trim().toInt()
            val g = parts[1].trim().toInt()
            val b = parts[2].trim().toInt()
            AndroidColor.rgb(r, g, b)
        } else {
            AndroidColor.parseColor(colorString)
        }
    } catch (e: Exception) {
        defaultColor
    }
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
    isMagnetEnabled: Boolean = false,
    isLocked: Boolean = false,
    isVisible: Boolean = true
) {
    var candleEntries by remember { mutableStateOf<List<CandleEntry>>(emptyList()) }
    var volumeEntries by remember { mutableStateOf<List<BarEntry>>(emptyList()) }
    
    // State to store live updates from MT5
    var currentQuote by remember { mutableStateOf<SymbolQuote?>(null) }
    
    var highlightedCandle by remember { mutableStateOf<CandleEntry?>(null) }
    var showOrderDialog by remember { mutableStateOf(false) }
    var orderType by remember { mutableStateOf("BUY") }
    var orderPrice by remember { mutableStateOf(0f) }

    var priceScaleFactor by remember { mutableFloatStateOf(2.5f) }
    var priceCenterOffset by remember { mutableFloatStateOf(0f) }
    var lastY by remember { mutableFloatStateOf(0f) }
    var isDraggingAxis by remember { mutableStateOf(false) }
    var isManualMode by remember { mutableStateOf(false) }
    var initialZoomDone by remember { mutableStateOf(false) }

    var selectedCurrency by remember { mutableStateOf("USD") }
    var showCurrencyMenu by remember { mutableStateOf(false) }

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

    // Chart Area Background Color - Lighter version of 0xFF08090C
    Box(modifier = Modifier.fillMaxSize().background(ComposeColor(0xFF161924))) {
        AndroidView(
            factory = { context ->
                val chart = CombinedChart(context)
                val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        priceScaleFactor = 2.5f
                        priceCenterOffset = 0f
                        isManualMode = false
                        chart.invalidate()
                        return true
                    }
                })

                chart.apply {
                    description.isEnabled = false
                    legend.isEnabled = false
                    setTouchEnabled(true)
                    isDragEnabled = true
                    isScaleXEnabled = true
                    isScaleYEnabled = false 
                    setPinchZoom(true)
                    setDrawGridBackground(false)
                    setDrawBorders(false)
                    setBackgroundColor(AndroidColor.TRANSPARENT)
                    
                    // Controlled bottom offset to match the 24dp height of pair tabs
                    minOffset = 0f
                    // Set bottom extra offset to ensure the date pane area is exactly 24dp high
                    setExtraOffsets(12f, 0f, 0f, 9f)

                    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            if (e is CandleEntry) {
                                highlightedCandle = e
                            }
                        }
                        override fun onNothingSelected() {
                            highlightedCandle = null
                        }
                    })

                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(true)
                        gridColor = safeParseColor("#363A45")
                        textColor = AndroidColor.WHITE
                        textSize = 9f
                        setLabelCount(6, false)
                        setAvoidFirstLastClipping(true)
                        yOffset = 6f
                        setDrawAxisLine(true)
                        axisLineColor = safeParseColor("#2A2E39")
                        axisLineWidth = 1f
                        
                        valueFormatter = object : ValueFormatter() {
                            private val dayFormat = SimpleDateFormat("d", Locale.US)
                            private val monthFormat = SimpleDateFormat("MMM", Locale.US)
                            
                            override fun getFormattedValue(value: Float): String {
                                val currentEntries = candleEntries
                                val index = value.toInt()
                                if (index >= 0 && index < currentEntries.size) {
                                    val millis = currentEntries[index].data as? Long ?: return ""
                                    val date = Date(millis)
                                    val cal = Calendar.getInstance().apply { timeInMillis = millis }
                                    val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
                                    
                                    return if (dayOfMonth <= 5) {
                                        monthFormat.format(date).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString() }
                                    } else {
                                        dayFormat.format(date)
                                    }
                                }
                                return ""
                            }
                        }
                    }

                    axisRight.apply {
                        isEnabled = true
                        setDrawGridLines(true)
                        gridColor = safeParseColor("#363A45")
                        textColor = safeParseColor("#D1D4DC")
                        textSize = 11f
                        setLabelCount(18, true)
                        // Position labels outside so they don't enter header or date pane content area
                        setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                        // Draw axis line so grid lines touch it
                        setDrawAxisLine(true)
                        axisLineColor = safeParseColor("#363A45")
                        // Add spacing to ensure labels don't crowd edges
                        setSpaceTop(5f)
                        setSpaceBottom(5f)
                    }

                    axisLeft.apply {
                        isEnabled = true
                        setDrawLabels(false)
                        setDrawGridLines(false)
                        setDrawAxisLine(false)
                        axisMinimum = 0f
                    }

                    setOnTouchListener { v, event ->
                        gestureDetector.onTouchEvent(event)
                        val chartView = v as CombinedChart
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                lastY = event.y
                                isDraggingAxis = event.x > v.width * 0.85f
                                isDraggingAxis
                            }
                            MotionEvent.ACTION_MOVE -> {
                                val deltaY = event.y - lastY 
                                if (isDraggingAxis) {
                                    isManualMode = true
                                    val scaleDelta = (lastY - event.y) / (v.height * 0.4f)
                                    priceScaleFactor *= (1f + scaleDelta)
                                    priceScaleFactor = priceScaleFactor.coerceIn(0.1f, 15f)
                                } else if (Math.abs(deltaY) > 20) {
                                    isManualMode = true
                                    val visibleRange = chartView.axisRight.axisMaximum - chartView.axisRight.axisMinimum
                                    if (visibleRange > 0) {
                                        val priceDelta = (deltaY / v.height) * visibleRange
                                        priceCenterOffset += priceDelta 
                                    }
                                }
                                lastY = event.y
                                chartView.invalidate()
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
            },
            modifier = Modifier.fillMaxSize(),
            update = { chart ->
                val currentCandles = candleEntries
                if (currentCandles.isEmpty()) return@AndroidView

                val combinedData = CombinedData()

                when (style) {
                    "line", "line_markers", "step_line", "kagi" -> {
                        val lineEntries = currentCandles.map { Entry(it.x, it.close) }
                        val lineDataSet = LineDataSet(ArrayList(lineEntries), "Line").apply {
                            axisDependency = YAxis.AxisDependency.RIGHT
                            color = safeParseColor("#2962FF")
                            setDrawCircles(style == "line_markers")
                            setDrawValues(false)
                            lineWidth = 2f
                            mode = if (style == "step_line") LineDataSet.Mode.STEPPED else LineDataSet.Mode.LINEAR
                        }
                        combinedData.setData(LineData(lineDataSet))
                    }
                    "area", "hlc_area", "baseline" -> {
                        val lineEntries = currentCandles.map { Entry(it.x, it.close) }
                        val areaDataSet = LineDataSet(ArrayList(lineEntries), "Area").apply {
                            axisDependency = YAxis.AxisDependency.RIGHT
                            color = safeParseColor("#2962FF")
                            setDrawCircles(false)
                            setDrawValues(false)
                            setDrawFilled(true)
                            fillColor = safeParseColor("#2962FF")
                            fillAlpha = 50
                            lineWidth = 2f
                        }
                        combinedData.setData(LineData(areaDataSet))
                    }
                    "columns" -> {
                        val barEntries = currentCandles.map { BarEntry(it.x, it.close) }
                        val columnDataSet = BarDataSet(ArrayList(barEntries), "Columns").apply {
                            axisDependency = YAxis.AxisDependency.RIGHT
                            setDrawValues(false)
                            colors = currentCandles.map { if (it.close >= it.open) safeParseColor("#089981") else safeParseColor("#F05252") }
                        }
                        combinedData.setData(BarData(columnDataSet))
                    }
                    else -> {
                        val candleDataSet = CandleDataSet(ArrayList(currentCandles), "Candles").apply {
                            axisDependency = YAxis.AxisDependency.RIGHT
                            setShadowColor(AndroidColor.WHITE)
                            shadowColorSameAsCandle = true
                            setShadowWidth(2.0f)
                            
                            when (style) {
                                "hollow_candles" -> {
                                    setIncreasingColor(safeParseColor("#089981"))
                                    setIncreasingPaintStyle(Paint.Style.STROKE)
                                    setDecreasingColor(safeParseColor("#F05252"))
                                    setDecreasingPaintStyle(Paint.Style.FILL)
                                }
                                "bars" -> {
                                    setIncreasingColor(safeParseColor("#089981"))
                                    setDecreasingColor(safeParseColor("#F05252"))
                                    setShadowWidth(1.5f)
                                    barSpace = 0.3f
                                }
                                "volume_candles", "volume_footprint", "heikin_ashi" -> {
                                    setIncreasingColor(safeParseColor("#089981"))
                                    setDecreasingColor(safeParseColor("#F05252"))
                                    setShadowWidth(2.0f)
                                }
                                else -> {
                                    setDecreasingColor(safeParseColor("#F05252"))
                                    setDecreasingPaintStyle(Paint.Style.FILL)
                                    setIncreasingColor(safeParseColor("#089981"))
                                    setIncreasingPaintStyle(Paint.Style.FILL)
                                }
                            }
                            
                            setNeutralColor(safeParseColor("#787B86"))
                            setDrawValues(false)
                            barSpace = 0.1f 
                            highlightLineWidth = 1f
                            highLightColor = AndroidColor.GRAY
                        }
                        combinedData.setData(CandleData(candleDataSet))
                    }
                }

                // Add Volume to BarData
                val volumeBarDataSet = BarDataSet(ArrayList(volumeEntries), "Volume").apply {
                    axisDependency = YAxis.AxisDependency.LEFT
                    setDrawValues(false)
                    colors = volumeEntries.map { it.data as Int }
                    setHighLightAlpha(0)
                }
                
                val currentBarData = combinedData.barData ?: BarData()
                currentBarData.addDataSet(volumeBarDataSet)
                currentBarData.barWidth = 0.8f
                combinedData.setData(currentBarData)

                chart.data = combinedData
                
                val visibleStart = chart.lowestVisibleX
                val visibleEnd = chart.highestVisibleX
                
                val visibleCandles = currentCandles.filter { it.x in visibleStart..visibleEnd }
                if (visibleCandles.isNotEmpty()) {
                    val minPrice = visibleCandles.minOf { it.low }
                    val maxPrice = visibleCandles.maxOf { it.high }
                    val priceRange = maxPrice - minPrice
                    val baseCenter = (maxPrice + minPrice) / 2f
                    val centerPrice = baseCenter + priceCenterOffset
                    val halfRange = (priceRange / 2f) / priceScaleFactor
                    
                    chart.axisRight.axisMinimum = centerPrice - (halfRange * 1.6f) 
                    chart.axisRight.axisMaximum = centerPrice + (halfRange * 1.02f)
                }

                val visibleVolumes = volumeEntries.filter { it.x in visibleStart..visibleEnd }
                if (visibleVolumes.isNotEmpty()) {
                    val maxVol = visibleVolumes.maxOf { it.y }
                    chart.axisLeft.axisMaximum = maxVol * 6f 
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

        // OHLC Overlay aligned to the left edge
        Column(
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.TopStart)
        ) {
            // Line 1: Flags and Name
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Overlapping Icons (Flags)
                Box(
                    contentAlignment = Alignment.CenterStart, 
                    modifier = Modifier.size(width = 32.dp, height = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(ComposeColor(0xFFF05252), RoundedCornerShape(9.dp))
                            .border(1.5.dp, ComposeColor(0xFF161924), RoundedCornerShape(9.dp))
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = 10.dp)
                            .size(18.dp)
                            .background(ComposeColor(0xFF2962FF), RoundedCornerShape(9.dp))
                            .border(1.5.dp, ComposeColor(0xFF161924), RoundedCornerShape(9.dp))
                    )
                }
                
                Spacer(modifier = Modifier.width(6.dp))
                
                Text(
                    text = getFullSymbolName(symbol),
                    color = ComposeColor.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Status Pill
                Row(
                    modifier = Modifier
                        .background(ComposeColor(0xFF1E222D), RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(ComposeColor(0xFF089981), RoundedCornerShape(3.dp))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.SyncAlt,
                        contentDescription = null,
                        tint = ComposeColor(0xFFF23645),
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
            
            // All aligned items start here
            val change = currentQuote?.change ?: 0f
            val changePercent = currentQuote?.changePercent ?: 0f
            val lastPrice = currentQuote?.lastPrice ?: 0f
            val changeColor = if (change >= 0f) ComposeColor(0xFF089981) else ComposeColor(0xFFF05252)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = String.format("%.3f", lastPrice),
                    color = changeColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = String.format("%+8.3f (%+8.2f%%)", change, changePercent),
                    color = changeColor,
                    fontSize = 13.sp
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            val displayData = highlightedCandle ?: currentQuote?.let { 
                CandleEntry(0f, it.high, it.low, it.open, it.lastPrice) 
            }
            Row {
                OHLCItem("O", String.format("%.2f", displayData?.open ?: 0f))
                OHLCItem("H", String.format("%.2f", displayData?.high ?: 0f))
                OHLCItem("L", String.format("%.2f", displayData?.low ?: 0f))
                OHLCItem("C", String.format("%.2f", displayData?.close ?: 0f))
            }
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Row {
                Text(text = "Bid ", color = ComposeColor(0xFF787B86), fontSize = 11.sp)
                Text(text = String.format("%.5f", currentQuote?.bid ?: 0f), color = ComposeColor.White, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Ask ", color = ComposeColor(0xFF787B86), fontSize = 11.sp)
                Text(text = String.format("%.5f", currentQuote?.ask ?: 0f), color = ComposeColor.White, fontSize = 11.sp)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier
                        .width(70.dp)
                        .height(44.dp)
                        .background(ComposeColor(0xFFF05252), RoundedCornerShape(4.dp))
                        .clickable { 
                            orderType = "SELL"
                            orderPrice = currentQuote?.bid ?: 0f
                            showOrderDialog = true
                        }
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("SELL", color = ComposeColor.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(String.format("%.2f", currentQuote?.bid ?: 0f), color = ComposeColor.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(44.dp)
                        .background(ComposeColor(0xFF1E222D))
                        .border(1.dp, ComposeColor(0xFF2A2E39)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("40", color = ComposeColor.White, fontSize = 13.sp)
                }
                
                Column(
                    modifier = Modifier
                        .width(70.dp)
                        .height(44.dp)
                        .background(ComposeColor(0xFF2962FF), RoundedCornerShape(4.dp))
                        .clickable { 
                            orderType = "BUY"
                            orderPrice = currentQuote?.ask ?: 0f
                            showOrderDialog = true
                        }
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("BUY", color = ComposeColor.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(String.format("%.2f", currentQuote?.ask ?: 0f), color = ComposeColor.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Text("Volume", color = ComposeColor(0xFF787B86), fontSize = 12.sp)
        }

        // Currency Dropdown at the top of Price Axis
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 4.dp)
        ) {
            Surface(
                color = ComposeColor(0xFF1E222D),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .clickable { showCurrencyMenu = true }
                    .padding(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        selectedCurrency,
                        color = ComposeColor(0xFFD1D4DC),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        null,
                        tint = ComposeColor(0xFF787B86),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = showCurrencyMenu,
                onDismissRequest = { showCurrencyMenu = false },
                modifier = Modifier.background(ComposeColor(0xFF1E222D))
            ) {
                listOf("USD", "EUR", "GBP", "JPY", "BTC").forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency, color = ComposeColor.White, fontSize = 12.sp) },
                        onClick = {
                            selectedCurrency = currency
                            showCurrencyMenu = false
                        }
                    )
                }
            }
        }
        
        if (showOrderDialog) {
            AlertDialog(
                onDismissRequest = { showOrderDialog = false },
                title = { Text(text = "Place Order", color = ComposeColor.White) },
                text = { 
                    Text(
                        text = "Are you sure you want to $orderType 40 units of $symbol at $orderPrice?",
                        color = ComposeColor.White
                    ) 
                },
                confirmButton = {
                    TextButton(onClick = { showOrderDialog = false }) {
                        Text("Confirm", color = ComposeColor(0xFF2962FF))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showOrderDialog = false }) {
                        Text("Cancel", color = ComposeColor(0xFF1E222D))
                    }
                },
                containerColor = ComposeColor(0xFF1E222D)
            )
        }
    }
}

@Composable
fun OHLCItem(label: String, value: String) {
    Row(modifier = Modifier.padding(end = 8.dp)) {
        Text(text = label, color = ComposeColor(0xFF787B86), fontSize = 11.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = value, color = ComposeColor.White, fontSize = 11.sp)
    }
}
