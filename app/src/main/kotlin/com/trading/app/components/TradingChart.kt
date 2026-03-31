package com.trading.app.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.trading.app.data.Mt5Service
import com.trading.app.models.ChartSettings
import com.trading.app.models.Drawing
import com.trading.app.models.Position
import com.trading.app.models.SymbolInfo
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.series.common.PriceLine
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.models.*
import com.tradingview.lightweightcharts.view.ChartsView
import java.util.*
import com.tradingview.lightweightcharts.api.series.enums.*
import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import android.graphics.Color as AndroidColor
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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

private fun getFullSymbolName(symbol: String): String {
    return when (symbol.uppercase()) {
        "BTCUSD" -> "Bitcoin / U.S. Dollar"
        "ETHUSD" -> "Ethereum / U.S. Dollar"
        "EURUSD" -> "Euro / U.S. Dollar"
        "USDJPY" -> "U.S. Dollar / Japanese Yen"
        "GBPUSD" -> "British Pound / U.S. Dollar"
        "AUDUSD" -> "Australian Dollar / US Dollar"
        "USDCAD" -> "U.S. Dollar / Canadian Dollar"
        "USDCHF" -> "U.S. Dollar / Swiss Franc"
        "NZDUSD" -> "New Zealand Dollar / U.S. Dollar"
        else -> symbol
    }
}

private fun applyOpacity(color: Int, opacity: Int): Int {
    val alpha = (opacity / 100f * 255).toInt().coerceIn(0, 255)
    return (color and 0x00FFFFFF) or (alpha shl 24)
}

private fun getFullChartColor(colorSetting: String, customBg: String): Int {
    return when (colorSetting) {
        "Pure Black" -> android.graphics.Color.BLACK
        "Dark Blue" -> android.graphics.Color.parseColor("#0a0e27")
        "OLED Black" -> android.graphics.Color.parseColor("#0d0f1a")
        else -> try { android.graphics.Color.parseColor(customBg) } catch (e: Exception) { android.graphics.Color.BLACK }
    }
}

private fun calculateHeikinAshi(data: List<CandlestickData>): List<CandlestickData> {
    if (data.isEmpty()) return emptyList()
    val haData = mutableListOf<CandlestickData>()
    var prevOpen = data[0].open
    var prevClose = data[0].close

    data.forEach { candle ->
        val close = (candle.open + candle.high + candle.low + candle.close) / 4f
        val open = (prevOpen + prevClose) / 2f
        val high = maxOf(candle.high, maxOf(open, close))
        val low = minOf(candle.low, minOf(open, close))
        
        haData.add(CandlestickData(candle.time, open, high, low, close))
        
        prevOpen = open
        prevClose = close
    }
    return haData
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
    onScrollDone: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    selectedTimeZone: String = "UTC",
    onQuoteUpdate: (SymbolQuote) -> Unit = {},
    positions: List<Position> = emptyList(),
    onPositionUpdate: (Position) -> Unit = {},
    onPositionDelete: (String) -> Unit = {}
) {
    var candlestickData by remember { mutableStateOf<List<CandlestickData>>(emptyList()) }
    var currentQuoteState by remember { mutableStateOf<SymbolQuote?>(null) }
    var seriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var chartsViewApi by remember { mutableStateOf<ChartsView?>(null) }
    var showMarketStatus by remember { mutableStateOf(false) }
    
    // High/Low lines state (Line and Label separate for color independence)
    val highLineState = remember { mutableStateOf<PriceLine?>(null) }
    val highLabelState = remember { mutableStateOf<PriceLine?>(null) }
    val lowLineState = remember { mutableStateOf<PriceLine?>(null) }
    val lowLabelState = remember { mutableStateOf<PriceLine?>(null) }
    
    val bidPriceLineState = remember { mutableStateOf<PriceLine?>(null) }
    val askPriceLineState = remember { mutableStateOf<PriceLine?>(null) }

    val updatedOnQuoteUpdate = rememberUpdatedState(onQuoteUpdate)
    val currentSymbol = rememberUpdatedState(symbol)

    // Range-based H/L state
    var visibleRangeHighLow by remember { mutableStateOf<Pair<Float, Float>?>(null) }

    fun String.toIntColor(): IntColor = try {
        IntColor(AndroidColor.parseColor(this))
    } catch (e: Exception) {
        IntColor(AndroidColor.GRAY)
    }
    
    fun Int.toLineWidth(): LineWidth = when (this) {
        1 -> LineWidth.ONE
        2 -> LineWidth.TWO
        3 -> LineWidth.THREE
        4 -> LineWidth.FOUR
        else -> LineWidth.ONE
    }
    
    fun String.toLineStyle(): LineStyle = when (this) {
        "Solid" -> LineStyle.SOLID
        "Dashed" -> LineStyle.DASHED
        "Dotted" -> LineStyle.DOTTED
        else -> LineStyle.SOLID
    }

    val mt5Service = remember {
        Mt5Service(
            pcIpAddress = "172.26.23.133", 
            port = 8081,
            onHistoryUpdate = { receivedSymbol, history ->
                if (receivedSymbol.isEmpty() || receivedSymbol.equals(currentSymbol.value, ignoreCase = true)) {
                    candlestickData = history
                }
            },
            onQuoteUpdate = { quote ->
                if (quote.name.equals(currentSymbol.value, ignoreCase = true)) {
                    val prevClose = candlestickData.getOrNull(candlestickData.size - 2)?.close ?: quote.lastPrice
                    val change = quote.lastPrice - prevClose
                    val changePercent = if (prevClose != 0f) (change / prevClose) * 100f else 0f
                    
                    val updatedQuote = quote.copy(
                        change = change,
                        changePercent = changePercent
                    )
                    currentQuoteState = updatedQuote
                    updatedOnQuoteUpdate.value(updatedQuote)
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        mt5Service.connect()
    }

    LaunchedEffect(symbol, timeframe) {
        candlestickData = emptyList()
        currentQuoteState = null
        mt5Service.subscribe(symbol, timeframe)
    }

    LaunchedEffect(candlestickData, seriesApi, style) {
        val api = seriesApi ?: return@LaunchedEffect
        if (candlestickData.isNotEmpty()) {
            when (style) {
                "bars" -> api.setData(candlestickData.map { BarData(it.time, it.open, it.high, it.low, it.close) })
                "line", "area" -> api.setData(candlestickData.map { LineData(it.time, it.close) })
                "heikin_ashi" -> api.setData(calculateHeikinAshi(candlestickData))
                else -> api.setData(candlestickData)
            }
        } else {
            api.setData(emptyList())
        }
    }

    LaunchedEffect(currentQuoteState) {
        val quote = currentQuoteState ?: return@LaunchedEffect
        val api = seriesApi ?: return@LaunchedEffect
        val lastCandle = candlestickData.lastOrNull() ?: return@LaunchedEffect
        
        val updatedCandle = CandlestickData(
            time = lastCandle.time,
            open = lastCandle.open,
            high = maxOf(lastCandle.high, quote.lastPrice),
            low = minOf(lastCandle.low, quote.lastPrice),
            close = quote.lastPrice
        )
        api.update(updatedCandle)
    }

    // Manage High/Low Price Lines and Labels
    LaunchedEffect(candlestickData, currentQuoteState, seriesApi, 
        chartSettings.scales.highLowPriceLabels, 
        chartSettings.scales.highLowPriceLines,
        chartSettings.scales.highLowLineColor,
        chartSettings.scales.highLowLabelColor,
        chartSettings.scales.highLowCalculationMode,
        visibleRangeHighLow) {
        
        val api = seriesApi ?: return@LaunchedEffect
        val scales = chartSettings.scales
        
        // Remove existing lines
        highLineState.value?.let { api.removePriceLine(it) }
        highLabelState.value?.let { api.removePriceLine(it) }
        lowLineState.value?.let { api.removePriceLine(it) }
        lowLabelState.value?.let { api.removePriceLine(it) }
        
        highLineState.value = null
        highLabelState.value = null
        lowLineState.value = null
        lowLabelState.value = null

        if (candlestickData.isEmpty()) return@LaunchedEffect

        val calcMode = scales.highLowCalculationMode
        
        var maxHigh: Float
        var minLow: Float

        when (calcMode) {
            "100 candles" -> {
                val subList = candlestickData.takeLast(100)
                maxHigh = subList.maxOf { it.high }
                minLow = subList.minOf { it.low }
            }
            "500 candles" -> {
                val subList = candlestickData.takeLast(500)
                maxHigh = subList.maxOf { it.high }
                minLow = subList.minOf { it.low }
            }
            "Dynamic" -> {
                if (visibleRangeHighLow != null) {
                    maxHigh = visibleRangeHighLow!!.first
                    minLow = visibleRangeHighLow!!.second
                } else {
                    maxHigh = candlestickData.maxOf { it.high }
                    minLow = candlestickData.minOf { it.low }
                }
            }
            else -> {
                maxHigh = candlestickData.maxOf { it.high }
                minLow = candlestickData.minOf { it.low }
            }
        }
        
        // Include current quote in calculation if it's the latest data
        currentQuoteState?.let {
            if (calcMode == "Dynamic" || calcMode == "100 candles" || calcMode == "500 candles") {
                maxHigh = maxOf(maxHigh, it.lastPrice)
                minLow = minOf(minLow, it.lastPrice)
            }
        }

        val showLine = scales.highLowPriceLines
        val showLabel = scales.highLowPriceLabels

        val lineColor = try { IntColor(AndroidColor.parseColor(scales.highLowLineColor)) } catch (e: Exception) { IntColor(AndroidColor.WHITE) }
        val labelColor = try { IntColor(AndroidColor.parseColor(scales.highLowLabelColor)) } catch (e: Exception) { IntColor(AndroidColor.parseColor("#2962FF")) }
        
        // High Line
        if (showLine) {
            highLineState.value = api.createPriceLine(
                PriceLineOptions(
                    price = maxHigh,
                    color = lineColor,
                    lineWidth = LineWidth.ONE,
                    lineStyle = LineStyle.DASHED,
                    lineVisible = true,
                    axisLabelVisible = false,
                    title = "High"
                )
            )
        }

        // High Label
        if (showLabel) {
            highLabelState.value = api.createPriceLine(
                PriceLineOptions(
                    price = maxHigh,
                    color = labelColor,
                    lineWidth = LineWidth.ONE,
                    lineStyle = LineStyle.DASHED,
                    lineVisible = false,
                    axisLabelVisible = true,
                    title = "High"
                )
            )
        }

        // Low Line
        if (showLine) {
            lowLineState.value = api.createPriceLine(
                PriceLineOptions(
                    price = minLow,
                    color = lineColor,
                    lineWidth = LineWidth.ONE,
                    lineStyle = LineStyle.DASHED,
                    lineVisible = true,
                    axisLabelVisible = false,
                    title = "Low"
                )
            )
        }

        // Low Label
        if (showLabel) {
            lowLabelState.value = api.createPriceLine(
                PriceLineOptions(
                    price = minLow,
                    color = labelColor,
                    lineWidth = LineWidth.ONE,
                    lineStyle = LineStyle.DASHED,
                    lineVisible = false,
                    axisLabelVisible = true,
                    title = "Low"
                )
            )
        }
    }

    // Manage Bid/Ask Price Lines
    LaunchedEffect(currentQuoteState, seriesApi, 
        chartSettings.scales.bidAskLabels, 
        chartSettings.scales.bidAskLines,
        chartSettings.scales.bidAskMode, 
        chartSettings.scales.bidColor,
        chartSettings.scales.askColor) {
        
        val api = seriesApi ?: return@LaunchedEffect
        val quote = currentQuoteState ?: return@LaunchedEffect
        val scales = chartSettings.scales
        
        bidPriceLineState.value?.let { api.removePriceLine(it) }
        askPriceLineState.value?.let { api.removePriceLine(it) }
        bidPriceLineState.value = null
        askPriceLineState.value = null

        if (!scales.bidAskLabels && !scales.bidAskLines) return@LaunchedEffect

        val showLine = scales.bidAskLines
        val showLabel = scales.bidAskLabels

        bidPriceLineState.value = api.createPriceLine(
            PriceLineOptions(
                price = quote.bid,
                color = try { IntColor(AndroidColor.parseColor(scales.bidColor)) } catch (e: Exception) { IntColor(AndroidColor.BLUE) },
                lineWidth = LineWidth.ONE,
                lineStyle = LineStyle.DASHED,
                lineVisible = showLine,
                axisLabelVisible = showLabel,
                title = "Bid"
            )
        )

        askPriceLineState.value = api.createPriceLine(
            PriceLineOptions(
                price = quote.ask,
                color = try { IntColor(AndroidColor.parseColor(scales.askColor)) } catch (e: Exception) { IntColor(AndroidColor.RED) },
                lineWidth = LineWidth.ONE,
                lineStyle = LineStyle.DASHED,
                lineVisible = showLine,
                axisLabelVisible = showLabel,
                title = "Ask"
            )
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            mt5Service.disconnect()
        }
    }

    val chartBgColor = getFullChartColor(chartSettings.canvas.fullChartColor, chartSettings.canvas.background)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeColor.Black)
            .pointerInput(symbol) { // Re-bind when symbol changes
                detectTapGestures {
                    // Deselect all positions for the current asset when clicking elsewhere on the chart
                    positions.forEach { pos ->
                        if (pos.symbol == symbol && pos.isSelected) onPositionUpdate(pos.copy(isSelected = false))
                    }
                }
            }
    ) {
        key(style, symbol) {
            AndroidView(
                factory = { context ->
                    ChartsView(context).apply {
                        chartsViewApi = this
                        val uppercaseSymbol = symbol.uppercase()
                        val isBitcoin = uppercaseSymbol.contains("BTC") || uppercaseSymbol.contains("BITCOIN")
                        val isForex = uppercaseSymbol.length == 6 || uppercaseSymbol.contains("/")
                        
                        val precision = when {
                            isBitcoin -> 0
                            isForex -> 5
                            else -> 2
                        }
                        val minMove = when {
                            isBitcoin -> 1f
                            isForex -> 0.00001f
                            else -> 0.01f
                        }

                        api.applyOptions {
                            layout = LayoutOptions(
                                background = SolidColor(color = IntColor(chartBgColor)),
                                textColor = chartSettings.canvas.scaleTextColor.toIntColor(),
                                fontSize = chartSettings.canvas.scaleFontSize
                            )
                            grid = GridOptions(
                                vertLines = GridLineOptions(
                                    color = IntColor(applyOpacity(AndroidColor.parseColor(chartSettings.canvas.gridColor), chartSettings.canvas.gridOpacity)),
                                    visible = chartSettings.canvas.gridVisible && chartSettings.canvas.gridType in listOf("Vert and horz", "Vert")
                                ),
                                horzLines = GridLineOptions(
                                    color = IntColor(applyOpacity(AndroidColor.parseColor(chartSettings.canvas.horzGridColor), chartSettings.canvas.gridOpacity)),
                                    visible = chartSettings.canvas.gridVisible && chartSettings.canvas.gridType in listOf("Vert and horz", "Horz")
                                )
                            )
                            crosshair = CrosshairOptions(
                                mode = CrosshairMode.NORMAL,
                                vertLine = CrosshairLineOptions(
                                    color = chartSettings.canvas.crosshairColor.toIntColor(),
                                    width = chartSettings.canvas.crosshairThickness.toLineWidth(),
                                    style = chartSettings.canvas.crosshairLineStyle.toLineStyle()
                                ),
                                horzLine = CrosshairLineOptions(
                                    color = chartSettings.canvas.crosshairColor.toIntColor(),
                                    width = chartSettings.canvas.crosshairThickness.toLineWidth(),
                                    style = chartSettings.canvas.crosshairLineStyle.toLineStyle()
                                )
                            )
                            rightPriceScale = PriceScaleOptions(
                                borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                                entireTextOnly = false,
                                autoScale = true
                            )
                            timeScale = TimeScaleOptions(
                                borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                                timeVisible = true
                            )
                        }

                        api.timeScale.subscribeVisibleTimeRangeChange { range ->
                            if (chartSettings.scales.highLowCalculationMode == "Dynamic" && range != null && candlestickData.isNotEmpty()) {
                                try {
                                    val start = (range.from as? Time.Utc)?.timestamp ?: 0L
                                    val end = (range.to as? Time.Utc)?.timestamp ?: Long.MAX_VALUE
                                    
                                    val visibleCandles = candlestickData.filter { (it.time as? Time.Utc)?.timestamp in start..end }
                                    if (visibleCandles.isNotEmpty()) {
                                        visibleRangeHighLow = Pair(visibleCandles.maxOf { it.high }, visibleCandles.minOf { it.low })
                                    }
                                } catch (e: Exception) {
                                    Log.e("Chart", "Error calculating visible high/low", e)
                                }
                            }
                        }

                        val priceLineVisible = chartSettings.scales.symbolLastPriceLine
                        val lastValueVisible = chartSettings.scales.symbolLastPriceLabel

                        when (style) {
                            "bars" -> {
                                api.addBarSeries(
                                    options = BarSeriesOptions(
                                        upColor = chartSettings.symbol.upColor.toIntColor(),
                                        downColor = chartSettings.symbol.downColor.toIntColor(),
                                        priceFormat = PriceFormat.priceFormatBuiltIn(type = PriceFormat.Type.PRICE, precision = precision, minMove = minMove),
                                        priceLineVisible = priceLineVisible,
                                        lastValueVisible = lastValueVisible
                                    ),
                                    onSeriesCreated = { api -> seriesApi = api }
                                )
                            }
                            "line" -> {
                                api.addLineSeries(
                                    options = LineSeriesOptions(
                                        color = chartSettings.symbol.upColor.toIntColor(),
                                        priceFormat = PriceFormat.priceFormatBuiltIn(type = PriceFormat.Type.PRICE, precision = precision, minMove = minMove),
                                        priceLineVisible = priceLineVisible,
                                        lastValueVisible = lastValueVisible
                                    ),
                                    onSeriesCreated = { api -> seriesApi = api }
                                )
                            }
                            else -> {
                                api.addCandlestickSeries(
                                    options = CandlestickSeriesOptions(
                                        upColor = chartSettings.symbol.upColor.toIntColor(),
                                        downColor = chartSettings.symbol.downColor.toIntColor(),
                                        borderVisible = chartSettings.symbol.borderVisible,
                                        borderUpColor = chartSettings.symbol.borderColorUp.toIntColor(),
                                        borderDownColor = chartSettings.symbol.borderColorDown.toIntColor(),
                                        wickVisible = chartSettings.symbol.wickVisible,
                                        wickUpColor = chartSettings.symbol.wickColorUp.toIntColor(),
                                        wickDownColor = chartSettings.symbol.wickColorDown.toIntColor(),
                                        priceFormat = PriceFormat.priceFormatBuiltIn(type = PriceFormat.Type.PRICE, precision = precision, minMove = minMove),
                                        priceLineVisible = priceLineVisible,
                                        lastValueVisible = lastValueVisible
                                    ),
                                    onSeriesCreated = { api -> seriesApi = api }
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { chartsView ->
                    chartsView.api.applyOptions {
                        layout = LayoutOptions(
                            background = SolidColor(color = IntColor(chartBgColor)),
                            textColor = chartSettings.canvas.scaleTextColor.toIntColor(),
                            fontSize = chartSettings.canvas.scaleFontSize
                        )
                        grid = GridOptions(
                            vertLines = GridLineOptions(
                                color = IntColor(applyOpacity(AndroidColor.parseColor(chartSettings.canvas.gridColor), chartSettings.canvas.gridOpacity)),
                                visible = chartSettings.canvas.gridVisible && chartSettings.canvas.gridType in listOf("Vert and horz", "Vert")
                            ),
                            horzLines = GridLineOptions(
                                color = IntColor(applyOpacity(AndroidColor.parseColor(chartSettings.canvas.horzGridColor), chartSettings.canvas.gridOpacity)),
                                visible = chartSettings.canvas.gridVisible && chartSettings.canvas.gridType in listOf("Vert and horz", "Horz")
                            )
                        )
                        crosshair = CrosshairOptions(
                            vertLine = CrosshairLineOptions(
                                color = chartSettings.canvas.crosshairColor.toIntColor(),
                                width = chartSettings.canvas.crosshairThickness.toLineWidth(),
                                style = chartSettings.canvas.crosshairLineStyle.toLineStyle()
                            ),
                            horzLine = CrosshairLineOptions(
                                color = chartSettings.canvas.crosshairColor.toIntColor(),
                                width = chartSettings.canvas.crosshairThickness.toLineWidth(),
                                style = chartSettings.canvas.crosshairLineStyle.toLineStyle()
                            )
                        )
                    }

                    // Apply series-specific options
                    val priceLineVisible = chartSettings.scales.symbolLastPriceLine
                    val lastValueVisible = chartSettings.scales.symbolLastPriceLabel
                    
                    seriesApi?.let { api ->
                        when (style) {
                            "bars" -> api.applyOptions(BarSeriesOptions(priceLineVisible = priceLineVisible, lastValueVisible = lastValueVisible))
                            "line" -> api.applyOptions(LineSeriesOptions(priceLineVisible = priceLineVisible, lastValueVisible = lastValueVisible))
                            "area" -> api.applyOptions(AreaSeriesOptions(priceLineVisible = priceLineVisible, lastValueVisible = lastValueVisible))
                            "heikin_ashi", "candles" -> api.applyOptions(CandlestickSeriesOptions(priceLineVisible = priceLineVisible, lastValueVisible = lastValueVisible))
                            else -> api.applyOptions(CandlestickSeriesOptions(priceLineVisible = priceLineVisible, lastValueVisible = lastValueVisible))
                        }
                    }
                }
            )
        }

        // Top Right Currency Selector
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 2.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(ComposeColor(0xFF131722))
                .border(1.dp, ComposeColor(0xFF363A45), RoundedCornerShape(3.dp))
                .clickable { onCurrencyClick() }
                .padding(horizontal = 4.dp, vertical = 1.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = selectedCurrency,
                    color = ComposeColor(0xFFD1D4DC),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    null,
                    tint = ComposeColor(0xFF787B86),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Overlay UI (Top Left Status Line)
        Column(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    top = chartSettings.canvas.marginTop.dp,
                    end = chartSettings.canvas.marginRight.dp,
                    bottom = chartSettings.canvas.marginBottom.dp
                )
                .align(Alignment.TopStart)
        ) {
            if (chartSettings.statusLine.symbol) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (chartSettings.statusLine.logo) {
                        val symbolInfo = remember(symbol) {
                            val type = when {
                                symbol.startsWith("BTC") || symbol.startsWith("ETH") || symbol.startsWith("SOL") -> "Crypto"
                                symbol.length == 6 && (symbol.contains("USD") || symbol.contains("EUR") || symbol.contains("JPY") || symbol.contains("GBP")) -> "Forex"
                                symbol == "SPX" || symbol == "DJI" || symbol == "IXIC" || symbol == "NIFTY" -> "Index"
                                else -> "Stock"
                            }
                            SymbolInfo(ticker = symbol, name = "", type = type)
                        }
                        AssetIcon(symbolInfo, size = 24)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (chartSettings.statusLine.titleMode == "Description") getFullSymbolName(symbol) else symbol,
                        color = ComposeColor(0xFFB2B5BE),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (chartSettings.statusLine.openMarketStatus) {
                        val isCrypto = symbol.uppercase().contains("BTC") || symbol.uppercase().contains("ETH")
                        val calendar = Calendar.getInstance()
                        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                        val isOpen = isCrypto || (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY)
                        val dotColor = if (isOpen) ComposeColor(0xFF089981) else ComposeColor(0xFF787B86)

                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.clickable { showMarketStatus = true }
                        ) {
                            Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(dotColor.copy(alpha = 0.15f)))
                            Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(dotColor.copy(alpha = 0.35f)))
                            Box(modifier = Modifier.size(11.dp).clip(CircleShape).background(dotColor))
                        }
                    }
                }
            }

            currentQuoteState?.let { quote ->
                val color = if (quote.change >= 0) ComposeColor(0xFF089981) else ComposeColor(0xFFF05252)
                
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                    Text(
                        text = formatPrice(quote.lastPrice, symbol),
                        color = ComposeColor(0xFFD1D4DC),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (chartSettings.statusLine.barChangeValues) {
                        Spacer(modifier = Modifier.width(8.dp))
                        val sign = if (quote.change >= 0) "+" else ""
                        Text(
                            text = String.format("%s%s (%+.2f%%)", sign, formatPrice(quote.change, symbol), quote.changePercent),
                            color = color,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                if (showVolume && chartSettings.statusLine.volume) {
                    Text(
                        text = "Vol · ${symbol.take(3).uppercase()}",
                        color = ComposeColor(0xFF787B86),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(ComposeColor(0xFF1E222D))
                        .border(1.dp, ComposeColor(0xFF363A45), RoundedCornerShape(4.dp))
                        .clickable { onVolumeToggle(!showVolume) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Toggle Indicators",
                        tint = ComposeColor(0xFFD1D4DC),
                        modifier = Modifier.size(16.dp)
                    )
                }

                if (chartSettings.statusLine.ohlc) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                        if (chartSettings.symbol.openVisible) OhlcItem("O", quote.open, symbol)
                        if (chartSettings.symbol.highVisible) OhlcItem("H", quote.high, symbol)
                        if (chartSettings.symbol.lowVisible) OhlcItem("L", quote.low, symbol)
                        if (chartSettings.symbol.closeVisible) OhlcItem("C", quote.lastPrice, symbol)
                    }
                }
            }
        }

        // Overlay for positions - filtered by current asset symbol
        positions.filter { it.symbol.equals(symbol, ignoreCase = true) }.forEach { position ->
            key(position.id) {
                PositionUI(
                    position = position,
                    currentPrice = currentQuoteState?.lastPrice ?: 0f,
                    symbol = symbol,
                    onUpdate = onPositionUpdate,
                    onDelete = { onPositionDelete(position.id) }
                )
            }
        }

        // Settings Button (Bottom Right)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(width = 60.dp, height = 34.dp)
                .clickable { onSettingsClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Chart Settings",
                tint = ComposeColor(0xFFD1D4DC),
                modifier = Modifier.size(24.dp)
            )
        }

        if (showMarketStatus) {
            MarketStatusModal(
                symbol = symbol,
                selectedTimeZone = selectedTimeZone,
                onDismiss = { showMarketStatus = false }
            )
        }
    }
}

private fun formatPrice(price: Float, symbol: String = ""): String {
    val symbols = DecimalFormatSymbols(Locale.US)
    symbols.groupingSeparator = ','
    val uppercaseSymbol = symbol.uppercase()
    val isBitcoin = uppercaseSymbol.contains("BTC") || uppercaseSymbol.contains("BITCOIN")
    val isForex = uppercaseSymbol.length == 6 || uppercaseSymbol.contains("/")
    
    val pattern = when {
        isBitcoin -> "#,##0"
        isForex -> "#,##0.00000"
        else -> "#,##0.##"
    }

    val df = DecimalFormat(pattern, symbols)
    return df.format(price)
}

@Composable
fun OhlcItem(label: String, value: Float, symbol: String) {
    Row(modifier = Modifier.padding(end = 8.dp)) {
        Text(text = "$label ", color = ComposeColor.Gray, fontSize = 11.sp)
        Text(text = formatPrice(value, symbol), color = ComposeColor.White, fontSize = 11.sp)
    }
}

@Composable
fun TradingButton(label: String, price: String, backgroundColor: ComposeColor) {
    @Suppress("UNUSED_VARIABLE")
    val dummy = backgroundColor // Keep the parameter used if needed
    Column(
        modifier = Modifier
            .width(85.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (label.isNotEmpty()) {
            Text(text = label, color = ComposeColor.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Text(text = price, color = ComposeColor.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PositionUI(
    position: Position,
    currentPrice: Float,
    symbol: String,
    onUpdate: (Position) -> Unit,
    onDelete: () -> Unit
) {
    val currentPosition by rememberUpdatedState(position)
    var tpOffset by remember { mutableStateOf(0f) }
    var slOffset by remember { mutableStateOf(0f) }
    var isDraggingTP by remember { mutableStateOf(false) }
    var isDraggingSL by remember { mutableStateOf(false) }
    
    val density = LocalDensity.current
    val pxToPrice = 20f // Simulated: 1dp = 20 price units

    // Sync offsets from position when not dragging
    LaunchedEffect(position.tp, isDraggingTP) {
        if (!isDraggingTP) {
            tpOffset = position.tp?.let { (position.entryPrice - it) / pxToPrice } ?: 0f
        }
    }
    LaunchedEffect(position.sl, isDraggingSL) {
        if (!isDraggingSL) {
            slOffset = position.sl?.let { (position.entryPrice - it) / pxToPrice } ?: 0f
        }
    }

    val pnl = (currentPrice - position.entryPrice) * position.volume * (if (position.type == "buy") 1 else -1)
    val pnlColor = if (pnl >= 0) ComposeColor(0xFF089981) else ComposeColor(0xFFF23645)
    
    val showTP = isDraggingTP || position.tp != null
    val showSL = isDraggingSL || position.sl != null

    val priceTagWidth = 70.dp
    val dotBoxWidth = 80.dp
    val trailingContentWidth = priceTagWidth + dotBoxWidth
    val verticalLineXOffset = priceTagWidth + (dotBoxWidth / 2)

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Entry Row
        Row(
            modifier = Modifier
                .align(Alignment.Center) 
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Entry Line (Left side)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(if (position.type == "buy") ComposeColor(0xFF2962FF) else ComposeColor(0xFFF2A52C))
            )
            
            // The grouped label + Arrow column
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures { onUpdate(currentPosition.copy(isSelected = !currentPosition.isSelected)) }
                    }
                ) {
                    // Column for TP, LotSize, SL
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (position.isSelected) {
                            // TP Box
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 2.dp)
                                    .background(ComposeColor(0xFF131722).copy(alpha = 0.8f), RoundedCornerShape(2.dp))
                                    .border(BorderStroke(1.dp, ComposeColor(0xFF089981).copy(alpha = 0.5f)), RoundedCornerShape(2.dp))
                                    .pointerInput(Unit) {
                                        detectDragGestures(
                                            onDragStart = { isDraggingTP = true },
                                            onDragEnd = {
                                                val finalPrice = currentPosition.entryPrice - (tpOffset * pxToPrice)
                                                onUpdate(currentPosition.copy(tp = finalPrice))
                                                isDraggingTP = false
                                            },
                                            onDragCancel = { isDraggingTP = false },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                val deltaDp = with(density) { dragAmount.y.toDp().value }
                                                tpOffset += deltaDp
                                            }
                                        )
                                    }
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("TP", color = ComposeColor(0xFF089981), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Lot size box
                        Box(
                            modifier = Modifier
                                .height(24.dp)
                                .width(28.dp)
                                .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                                .background(if (position.type == "buy") ComposeColor(0xFF2962FF) else ComposeColor(0xFFF2A52C)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = position.volume.toInt().toString(),
                                color = ComposeColor.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (position.isSelected) {
                            // SL Box
                            Box(
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .background(ComposeColor(0xFF131722).copy(alpha = 0.8f), RoundedCornerShape(2.dp))
                                    .border(BorderStroke(1.dp, ComposeColor(0xFFF23645).copy(alpha = 0.5f)), RoundedCornerShape(2.dp))
                                    .pointerInput(Unit) {
                                        detectDragGestures(
                                            onDragStart = { isDraggingSL = true },
                                            onDragEnd = {
                                                val finalPrice = currentPosition.entryPrice - (slOffset * pxToPrice)
                                                onUpdate(currentPosition.copy(sl = finalPrice))
                                                isDraggingSL = false
                                            },
                                            onDragCancel = { isDraggingSL = false },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                val deltaDp = with(density) { dragAmount.y.toDp().value }
                                                slOffset += deltaDp
                                            }
                                        )
                                    }
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("SL", color = ComposeColor(0xFFF23645), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // PnL and Close button box
                    Row(
                        modifier = Modifier
                            .height(24.dp)
                            .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                            .background(ComposeColor(0xFF131722))
                            .border(1.dp, if (position.type == "buy") ComposeColor(0xFF2962FF) else ComposeColor(0xFFF2A52C), RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${if (pnl >= 0) "+" else ""}${String.format("%,.2f", pnl)} USD",
                            color = pnlColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Vertical divider
                        Box(modifier = Modifier.width(1.dp).fillMaxHeight().padding(vertical = 4.dp).background(ComposeColor(0xFF363A45)))
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = ComposeColor(0xFF787B86),
                            modifier = Modifier
                                .size(14.dp)
                                .clickable { onDelete() }
                        )
                    }
                }
                
                // Blue Arrow under the box - Box with 0 height so it doesn't affect centering
                Box(modifier = Modifier.height(0.dp), contentAlignment = Alignment.TopCenter) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        tint = ComposeColor(0xFF2962FF),
                        modifier = Modifier
                            .size(18.dp)
                            .offset(y = (-4).dp)
                    )
                }
            }
            
            Row(
                modifier = Modifier.width(trailingContentWidth),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Connecting line with dot
                Box(
                    modifier = Modifier.width(dotBoxWidth),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(if (position.type == "buy") ComposeColor(0xFF2962FF) else ComposeColor(0xFFF2A52C))
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (position.type == "buy") ComposeColor(0xFF2962FF) else ComposeColor(0xFFF2A52C))
                    )
                }
                
                // Price Tag on Scale
                Box(
                    modifier = Modifier
                        .width(priceTagWidth)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (position.type == "buy") ComposeColor(0xFF2962FF) else ComposeColor(0xFFF2A52C))
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formatPrice(position.entryPrice, symbol),
                        color = ComposeColor.White,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // TP Visuals (Green Area and Persistent Line)
        if (showTP) {
            val dragColor = ComposeColor(0xFF089981)
            // Green Area - ONLY DURING DRAG
            if (isDraggingTP) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Math.abs(tpOffset).dp)
                        .align(Alignment.Center)
                        .offset(y = (tpOffset / 2).dp)
                        .background(dragColor.copy(alpha = 0.15f))
                )
            }
            // TP Line and Label
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .offset(y = tpOffset.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { isDraggingTP = true },
                            onDragEnd = {
                                val finalPrice = currentPosition.entryPrice - (tpOffset * pxToPrice)
                                onUpdate(currentPosition.copy(tp = finalPrice))
                                isDraggingTP = false
                            },
                            onDragCancel = { isDraggingTP = false },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val deltaDp = with(density) { dragAmount.y.toDp().value }
                                tpOffset += deltaDp
                            }
                        )
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f).height(1.dp).background(dragColor))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(ComposeColor(0xFF131722))
                        .border(1.dp, dragColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(18.dp).background(dragColor, RoundedCornerShape(2.dp)), contentAlignment = Alignment.Center) {
                        Text(position.volume.toInt().toString(), color = ComposeColor.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    val targetPrice = position.entryPrice - (tpOffset * pxToPrice)
                    val targetPnl = (targetPrice - position.entryPrice) * position.volume * (if (position.type == "buy") 1 else -1)
                    
                    Text("${if (targetPnl >= 0) "+" else ""}${String.format("%,.2f", targetPnl)} USD", color = dragColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.Close, null, tint = ComposeColor(0xFF787B86), modifier = Modifier.size(14.dp).clickable { onUpdate(currentPosition.copy(tp = null)) })
                }
                
                Row(
                    modifier = Modifier.width(trailingContentWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(dotBoxWidth), contentAlignment = Alignment.Center) {
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(dragColor))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(dragColor))
                    }
                    Box(
                        modifier = Modifier.width(priceTagWidth).clip(RoundedCornerShape(2.dp)).background(dragColor).padding(horizontal = 4.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val targetPrice = position.entryPrice - (tpOffset * pxToPrice)
                        Text(formatPrice(targetPrice, symbol), color = ComposeColor.White, fontSize = 11.sp)
                    }
                }
            }
        }

        // SL Visuals (Red Area and Persistent Line)
        if (showSL) {
            val dragColor = ComposeColor(0xFFF23645)
            // Red Area - ONLY DURING DRAG
            if (isDraggingSL) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Math.abs(slOffset).dp)
                        .align(Alignment.Center)
                        .offset(y = (slOffset / 2).dp)
                        .background(dragColor.copy(alpha = 0.15f))
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .offset(y = slOffset.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { isDraggingSL = true },
                            onDragEnd = {
                                val finalPrice = currentPosition.entryPrice - (slOffset * pxToPrice)
                                onUpdate(currentPosition.copy(sl = finalPrice))
                                isDraggingSL = false
                            },
                            onDragCancel = { isDraggingSL = false },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val deltaDp = with(density) { dragAmount.y.toDp().value }
                                slOffset += deltaDp
                            }
                        )
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f).height(1.dp).background(dragColor))
                Row(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(ComposeColor(0xFF131722)).border(1.dp, dragColor, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(18.dp).background(dragColor, RoundedCornerShape(2.dp)), contentAlignment = Alignment.Center) {
                        Text(position.volume.toInt().toString(), color = ComposeColor.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    val targetPrice = position.entryPrice - (slOffset * pxToPrice)
                    val targetPnl = (targetPrice - position.entryPrice) * position.volume * (if (position.type == "buy") 1 else -1)
                    
                    Text("${if (targetPnl >= 0) "+" else ""}${String.format("%,.2f", targetPnl)} USD", color = dragColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.Close, null, tint = ComposeColor(0xFF787B86), modifier = Modifier.size(14.dp).clickable { onUpdate(currentPosition.copy(sl = null)) })
                }
                
                Row(
                    modifier = Modifier.width(trailingContentWidth),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(dotBoxWidth), contentAlignment = Alignment.Center) {
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(dragColor))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(dragColor))
                    }
                    Box(
                        modifier = Modifier.width(priceTagWidth).clip(RoundedCornerShape(2.dp)).background(dragColor).padding(horizontal = 4.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val targetPrice = position.entryPrice - (slOffset * pxToPrice)
                        Text(formatPrice(targetPrice, symbol), color = ComposeColor.White, fontSize = 11.sp)
                    }
                }
            }
        }

        // Vertical connecting line (Drawn LAST with Canvas for precision)
        // Show only when selected or dragging, and if TP or SL is present
        if ((position.isSelected || isDraggingTP || isDraggingSL) && (showTP || showSL)) {
            val minLineOffset = minOf(0f, if (showTP) tpOffset else 0f, if (showSL) slOffset else 0f)
            val maxLineOffset = maxOf(0f, if (showTP) tpOffset else 0f, if (showSL) slOffset else 0f)
            val lineColor = ComposeColor(0xFF2962FF)

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val x = size.width - verticalLineXOffset.toPx()
                val centerY = size.height / 2
                
                // Main Vertical Line (DOTTED) - REDUCED SIZE
                drawLine(
                    color = lineColor,
                    start = Offset(x, centerY + minLineOffset.dp.toPx()),
                    end = Offset(x, centerY + maxLineOffset.dp.toPx()),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                )
                
                // Intersection circles - REDUCED SIZE
                drawCircle(color = lineColor, radius = 3.dp.toPx(), center = Offset(x, centerY))
                if (showTP) drawCircle(color = lineColor, radius = 3.dp.toPx(), center = Offset(x, centerY + tpOffset.dp.toPx()))
                if (showSL) drawCircle(color = lineColor, radius = 3.dp.toPx(), center = Offset(x, centerY + slOffset.dp.toPx()))
            }
        }
    }
}
