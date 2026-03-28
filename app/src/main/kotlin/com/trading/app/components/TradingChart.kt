package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.trading.app.data.Mt5Service
import com.trading.app.models.ChartSettings
import com.trading.app.models.Drawing
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.models.*
import com.tradingview.lightweightcharts.view.ChartsView
import java.util.*
import com.tradingview.lightweightcharts.api.series.enums.*
import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import android.graphics.Color as AndroidColor

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
        "AUDUSD" -> "Australian Dollar / U.S. Dollar"
        "USDCAD" -> "U.S. Dollar / Canadian Dollar"
        "USDCHF" -> "U.S. Dollar / Swiss Franc"
        "NZDUSD" -> "New Zealand Dollar / U.S. Dollar"
        else -> symbol
    }
}

private fun getFlagEmoji(symbol: String): String {
    return when (symbol.uppercase()) {
        "EURUSD" -> "🇪🇺🇺🇸"
        "GBPUSD" -> "🇬🇧🇺🇸"
        "USDJPY" -> "🇺🇸🇯🇵"
        "AUDUSD" -> "🇦🇺🇺🇸"
        "USDCAD" -> "🇺🇸🇨🇦"
        "USDCHF" -> "🇺🇸🇨🇭"
        "NZDUSD" -> "🇳🇿🇺🇸"
        "BTCUSD" -> "₿🇺🇸"
        "ETHUSD" -> "Ξ🇺🇸"
        else -> "🏳️"
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
    onLongPress: () -> Unit = {}
) {
    var candlestickData by remember { mutableStateOf<List<CandlestickData>>(emptyList()) }
    var volumeData by remember { mutableStateOf<List<HistogramData>>(emptyList()) }

    // Initial mock data to ensure visibility
    var currentQuote by remember { mutableStateOf<SymbolQuote?>(
        SymbolQuote(
            name = symbol,
            lastPrice = 65267.184f,
            change = -26.465f,
            changePercent = -0.04f,
            open = 65293.65f,
            high = 65341.45f,
            low = 65224.85f,
            prevClose = 65293.65f,
            bid = 65266.70f,
            ask = 65267.22f,
            volume = 1000f
        )
    ) }

    var seriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var volumeSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }

    // Helper to parse hex string to IntColor
    fun String.toIntColor(): IntColor = try {
        IntColor(AndroidColor.parseColor(this))
    } catch (e: Exception) {
        IntColor(AndroidColor.GRAY)
    }
    
    // Helper to convert thickness to LineWidth
    fun Int.toLineWidth(): LineWidth = when (this) {
        1 -> LineWidth.ONE
        2 -> LineWidth.TWO
        3 -> LineWidth.THREE
        4 -> LineWidth.FOUR
        else -> LineWidth.ONE
    }
    
    // Helper to convert string style to LineStyle
    fun String.toLineStyle(): LineStyle = when (this) {
        "Solid" -> LineStyle.SOLID
        "Dashed" -> LineStyle.DASHED
        "Dotted" -> LineStyle.DOTTED
        else -> LineStyle.SOLID
    }

    // MT5 Live Connection
    val mt5Service = remember {
        Mt5Service(pcIpAddress = "192.168.1.100") { quote ->
            currentQuote = quote
            seriesApi?.let { api ->
                val lastCandle = candlestickData.lastOrNull()
                if (lastCandle != null) {
                    val updatedCandle = CandlestickData(
                        time = lastCandle.time,
                        open = lastCandle.open,
                        high = maxOf(lastCandle.high, quote.lastPrice),
                        low = minOf(lastCandle.low, quote.lastPrice),
                        close = quote.lastPrice
                    )
                    api.update(updatedCandle)
                }
            }
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
        val newCandles = mutableListOf<CandlestickData>()
        val newVolumes = mutableListOf<HistogramData>()
        val random = Random()
        var lastClose = 65045.50f
        val now = System.currentTimeMillis() / 1000
        val interval = 86400L

        for (i in 0 until 500) {
            val open = lastClose
            val close = open + (random.nextFloat() - 0.5f) * 100f
            val high = maxOf(open, close) + random.nextFloat() * 50f
            val low = minOf(open, close) - random.nextFloat() * 50f
            val time = Time.Utc(now - (500 - i) * interval)

            newCandles.add(CandlestickData(time, open, high, low, close))
            
            val volColor = if (close >= open) "#089981" else "#F05252"
            newVolumes.add(HistogramData(time, random.nextFloat() * 1000f + 200f, color = volColor.toIntColor()))
            lastClose = close
        }

        candlestickData = newCandles
        volumeData = newVolumes
        seriesApi?.setData(newCandles)
        volumeSeriesApi?.setData(newVolumes)
        
        // Update mock currentQuote when symbol changes
        currentQuote = currentQuote?.copy(
            name = symbol,
            lastPrice = lastClose,
            open = lastClose - 10f,
            high = lastClose + 20f,
            low = lastClose - 30f,
            bid = lastClose - 0.5f,
            ask = lastClose + 0.5f
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(ComposeColor.Black)) {
        AndroidView(
            factory = { context ->
                ChartsView(context).apply {
                    api.applyOptions {
                        layout = LayoutOptions(
                            background = SolidColor(color = getFullChartColor(chartSettings.canvas.fullChartColor, chartSettings.canvas.background)),
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
                            borderColor = chartSettings.canvas.scaleLineColor.toIntColor()
                        )
                        timeScale = TimeScaleOptions(
                            borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                            timeVisible = true
                        )
                        watermark = WatermarkOptions(
                            visible = chartSettings.canvas.watermarkVisible,
                            color = chartSettings.canvas.watermarkColor.toIntColor(),
                            text = if (chartSettings.canvas.watermarkVisible) symbol else ""
                        )
                    }

                    api.addCandlestickSeries(
                        options = CandlestickSeriesOptions(
                            upColor = chartSettings.symbol.upColor.toIntColor(),
                            downColor = chartSettings.symbol.downColor.toIntColor(),
                            borderVisible = chartSettings.symbol.borderVisible,
                            borderUpColor = chartSettings.symbol.borderColorUp.toIntColor(),
                            borderDownColor = chartSettings.symbol.borderColorDown.toIntColor(),
                            wickVisible = chartSettings.symbol.wickVisible,
                            wickUpColor = chartSettings.symbol.wickColorUp.toIntColor(),
                            wickDownColor = chartSettings.symbol.wickColorDown.toIntColor()
                        ),
                        onSeriesCreated = { api ->
                            seriesApi = api
                            api.setData(candlestickData)
                        }
                    )

                    if (showVolume) {
                        api.addHistogramSeries(
                            options = HistogramSeriesOptions(
                                priceScaleId = PriceScaleId("volume")
                            ),
                            onSeriesCreated = { api ->
                                volumeSeriesApi = api
                                api.priceScale().applyOptions(
                                    PriceScaleOptions(
                                        scaleMargins = PriceScaleMargins(
                                            top = 0.9f,
                                            bottom = 0f
                                        ),
                                        visible = false
                                    )
                                )
                                api.setData(volumeData)
                            }
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { chartsView ->
                chartsView.api.applyOptions {
                    layout = LayoutOptions(
                        background = SolidColor(color = getFullChartColor(chartSettings.canvas.fullChartColor, chartSettings.canvas.background)),
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
                    rightPriceScale = PriceScaleOptions(
                        borderColor = chartSettings.canvas.scaleLineColor.toIntColor()
                    )
                    timeScale = TimeScaleOptions(
                        borderColor = chartSettings.canvas.scaleLineColor.toIntColor()
                    )
                    watermark = WatermarkOptions(
                        visible = chartSettings.canvas.watermarkVisible,
                        color = chartSettings.canvas.watermarkColor.toIntColor(),
                        text = if (chartSettings.canvas.watermarkVisible) symbol else ""
                    )
                }
                
                seriesApi?.applyOptions(
                    CandlestickSeriesOptions(
                        upColor = chartSettings.symbol.upColor.toIntColor(),
                        downColor = chartSettings.symbol.downColor.toIntColor(),
                        borderVisible = chartSettings.symbol.borderVisible,
                        borderUpColor = chartSettings.symbol.borderColorUp.toIntColor(),
                        borderDownColor = chartSettings.symbol.borderColorDown.toIntColor(),
                        wickVisible = chartSettings.symbol.wickVisible,
                        wickUpColor = chartSettings.symbol.wickColorUp.toIntColor(),
                        wickDownColor = chartSettings.symbol.wickColorDown.toIntColor()
                    )
                )

                volumeSeriesApi?.applyOptions(
                    HistogramSeriesOptions(
                        visible = chartSettings.statusLine.volume
                    )
                )
            }
        )

        // Overlay UI
        Column(
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    top = chartSettings.canvas.marginTop.dp,
                    end = chartSettings.canvas.marginRight.dp,
                    bottom = chartSettings.canvas.marginBottom.dp
                )
                .align(
                    when (chartSettings.trading.alignment) {
                        "Left" -> Alignment.TopStart
                        "Right" -> Alignment.TopEnd
                        else -> Alignment.TopStart
                    }
                )
        ) {
            // Symbol Name and Flags
            if (chartSettings.statusLine.symbol) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (chartSettings.statusLine.logo) {
                        Box(modifier = Modifier.size(32.dp).padding(end = 8.dp)) {
                            // Overlapping circles for flags
                            Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(ComposeColor.White).align(Alignment.TopStart))
                            Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(ComposeColor.Gray).align(Alignment.BottomEnd))
                            Text(
                                text = getFlagEmoji(symbol),
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    Text(
                        text = if (chartSettings.statusLine.titleMode == "Description") getFullSymbolName(symbol) else symbol,
                        color = ComposeColor.White,
                        fontSize = chartSettings.canvas.symbolFontSize.sp,
                        fontWeight = if (chartSettings.canvas.headerFontBold) FontWeight.Bold else FontWeight.Medium
                    )
                    
                    // Scale indicator showing current label
                    if (chartSettings.scales.currencyAndUnit != "Hidden") {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = chartSettings.scales.symbolLabel,
                            color = ComposeColor.Gray,
                            fontSize = 9.sp
                        )
                    }
                    
                    // Open market status if enabled
                    if (chartSettings.statusLine.openMarketStatus) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Market Open",
                            color = ComposeColor(0xFF089981),
                            fontSize = 10.sp,
                            modifier = Modifier
                                .background(ComposeColor(0x20089981), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            currentQuote?.let { quote ->
                val color = if (quote.change >= 0) ComposeColor(0xFF089981) else ComposeColor(0xFFF05252)
                
                // Price and Change
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = String.format("%.3f", quote.lastPrice),
                        color = color,
                        fontSize = chartSettings.canvas.chartItemFontSize.sp,
                        fontWeight = if (chartSettings.canvas.headerFontBold) FontWeight.Bold else FontWeight.Medium
                    )
                    if (chartSettings.statusLine.barChangeValues) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = String.format("%+.3f (%+.2f%%)", quote.change, quote.changePercent),
                            color = color,
                            fontSize = chartSettings.canvas.chartItemFontSize.sp,
                            fontWeight = if (chartSettings.canvas.headerFontBold) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
                
                // Last day change if enabled
                if (chartSettings.statusLine.lastDayChange) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                        Text(
                            text = "Prev close: ${String.format("%.2f", quote.prevClose)}",
                            color = ComposeColor.Gray,
                            fontSize = chartSettings.canvas.bottomFontSize.sp
                        )
                    }
                }

                // Bid/Ask display if enabled
                if (chartSettings.scales.bidAskMode.contains("Value", ignoreCase = true)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = "Bid: ",
                            color = ComposeColor.Gray,
                            fontSize = chartSettings.canvas.chartItemFontSize.sp
                        )
                        Text(
                            text = String.format("%.2f", quote.bid),
                            color = ComposeColor(android.graphics.Color.parseColor(chartSettings.scales.bidColor)),
                            fontSize = chartSettings.canvas.chartItemFontSize.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Ask: ",
                            color = ComposeColor.Gray,
                            fontSize = chartSettings.canvas.chartItemFontSize.sp
                        )
                        Text(
                            text = String.format("%.2f", quote.ask),
                            color = ComposeColor(android.graphics.Color.parseColor(chartSettings.scales.askColor)),
                            fontSize = chartSettings.canvas.chartItemFontSize.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // OHLC Row
                if (chartSettings.statusLine.ohlc) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                        if (chartSettings.symbol.openVisible) {
                            OhlcItem("O", quote.open, chartSettings.canvas.chartItemFontSize, chartSettings.canvas.bottomFontBold)
                        }
                        if (chartSettings.symbol.highVisible) {
                            OhlcItem("H", quote.high, chartSettings.canvas.chartItemFontSize, chartSettings.canvas.bottomFontBold)
                        }
                        if (chartSettings.symbol.lowVisible) {
                            OhlcItem("L", quote.low, chartSettings.canvas.chartItemFontSize, chartSettings.canvas.bottomFontBold)
                        }
                        if (chartSettings.symbol.closeVisible) {
                            OhlcItem("C", quote.lastPrice, chartSettings.canvas.chartItemFontSize, chartSettings.canvas.bottomFontBold)
                        }
                        
                        if (chartSettings.statusLine.barChangeValues) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = String.format("%+.2f (%+.2f%%)", quote.change, quote.changePercent),
                                color = color,
                                fontSize = chartSettings.canvas.chartItemFontSize.sp,
                                fontWeight = if (chartSettings.canvas.bottomFontBold) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Bar Change (separate line if requested or just showing more info)
                if (chartSettings.statusLine.barChangeValues && !chartSettings.statusLine.ohlc) {
                    Text(
                        text = String.format("Bar change: %+.2f (%+.2f%%)", quote.change, quote.changePercent),
                        color = color,
                        fontSize = chartSettings.canvas.chartItemFontSize.sp,
                        fontWeight = if (chartSettings.canvas.bottomFontBold) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // Buy/Sell Buttons
                if (chartSettings.trading.buySellButtons) {
                    Row(modifier = Modifier.padding(top = 12.dp)) {
                        TradingButton(
                            label = if (chartSettings.trading.showBuySellLabels) "SELL" else "",
                            price = String.format("%.2f", quote.bid),
                            backgroundColor = ComposeColor(android.graphics.Color.parseColor(chartSettings.scales.bidColor))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TradingButton(
                            label = if (chartSettings.trading.showBuySellLabels) "BUY" else "",
                            price = String.format("%.2f", quote.ask),
                            backgroundColor = ComposeColor(android.graphics.Color.parseColor(chartSettings.scales.askColor))
                        )
                        
                        // One-click trading indicator
                        if (chartSettings.trading.oneClickTrading) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(ComposeColor(0xFF4CAF50), RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("1", color = ComposeColor.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                
                // Positions and Profit/Loss if enabled
                if (chartSettings.trading.positionsAndOrders) {
                    Column(modifier = Modifier.padding(top = 12.dp).fillMaxWidth(0.5f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Positions", color = ComposeColor.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            
                            // Show position mode (Money/Contracts)
                            if (chartSettings.trading.positionsMode == "Contracts") {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("(${chartSettings.trading.positionsMode})", color = ComposeColor.Gray, fontSize = 9.sp)
                            }
                        }
                        
                        // Sample position entry
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .background(ComposeColor(0x20089981), RoundedCornerShape(4.dp))
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Long x1", color = ComposeColor(0xFF089981), fontSize = 10.sp)
                            
                            if (!chartSettings.trading.screenshotVisibility) {
                                if (chartSettings.trading.profitLossValue) {
                                    if (chartSettings.trading.positionsMode == "Contracts") {
                                        Text("100 contracts", color = ComposeColor.Gray, fontSize = 9.sp)
                                    } else {
                                        Text("+50.00 USD", color = ComposeColor(0xFF089981), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                        
                        // Reverse position button if enabled
                        if (chartSettings.trading.reversePositionButton) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp)
                                    .background(ComposeColor(0xFFEF5350), RoundedCornerShape(4.dp))
                                    .clickable { }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text("Reverse", color = ComposeColor.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                
                // Execution marks if enabled
                if (chartSettings.trading.executionMarks) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(ComposeColor(0x15FFFFFF), RoundedCornerShape(4.dp))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Execution", color = ComposeColor.Gray, fontSize = 10.sp)
                        
                        if (chartSettings.trading.executionLabels) {
                            Text("BUY @ 50.25", color = ComposeColor(0xFF089981), fontSize = 9.sp)
                        }
                    }
                }
                
                // Extended price lines indicator
                if (chartSettings.trading.extendedPriceLines) {
                    Row(modifier = Modifier.padding(top = 2.dp)) {
                        Text(
                            text = "Extended lines active",
                            color = ComposeColor(0xFFB39DDB),
                            fontSize = 9.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
                
                // Project order if enabled
                if (chartSettings.trading.projectOrder) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(ComposeColor(0x154CAF50), RoundedCornerShape(4.dp))
                            .padding(4.dp)
                    ) {
                        Text("Projected Order", color = ComposeColor(0xFF4CAF50), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Row(modifier = Modifier.padding(top = 2.dp)) {
                            Text("Entry: 50.00", color = ComposeColor.Gray, fontSize = 9.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("TP: 52.00", color = ComposeColor.Gray, fontSize = 9.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SL: 48.00", color = ComposeColor.Gray, fontSize = 9.sp)
                        }
                    }
                }
                
                // Brackets mode if not Money
                if (chartSettings.trading.bracketsMode == "Contracts") {
                    Text(
                        text = "Brackets: ${chartSettings.trading.bracketsMode}",
                        color = ComposeColor.Gray,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                // ALERTS SECTION
                // Alert Lines indicator
                if (chartSettings.alerts.alertLines) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(ComposeColor(0x15FFFFFF), RoundedCornerShape(4.dp))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Alert Lines", color = ComposeColor.Gray, fontSize = 10.sp)
                        
                        if (chartSettings.alerts.onlyActiveAlerts) {
                            Text("(Active only)", color = ComposeColor(0xFFB39DDB), fontSize = 9.sp)
                        }
                    }
                }
                
                // Alert Volume indicator
                if (chartSettings.alerts.alertVolume) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .background(ComposeColor(0x15FFA500), RoundedCornerShape(4.dp))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Volume Alert", color = ComposeColor.Gray, fontSize = 10.sp)
                        Text("${chartSettings.alerts.volumeLevel}%", color = ComposeColor(0xFFFFA500), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                // EVENTS SECTION
                // Economic Events indicator
                if (chartSettings.events.economicEvents) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(ComposeColor(0x1542A5F5), RoundedCornerShape(4.dp))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Economic Events", color = ComposeColor.Gray, fontSize = 10.sp)
                        
                        if (chartSettings.events.onlyFutureEvents) {
                            Text("(Future)", color = ComposeColor(0xFF42A5F5), fontSize = 9.sp)
                        }
                    }
                }
                
                // Session Breaks indicator
                if (chartSettings.events.sessionBreaks) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .alpha(0.7f)
                            .background(
                                ComposeColor(android.graphics.Color.parseColor(chartSettings.events.sessionBreaksColor)),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Session Breaks (${chartSettings.events.eventsBreaksThickness}px)",
                            color = ComposeColor(android.graphics.Color.parseColor(chartSettings.events.sessionBreaksColor)),
                            fontSize = 10.sp
                        )
                    }
                }
                
                // Ideas indicator
                if (chartSettings.events.ideas) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .background(ComposeColor(0x15BB86FC), RoundedCornerShape(4.dp))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ideas", color = ComposeColor.Gray, fontSize = 10.sp)
                        Text(chartSettings.events.ideasMode, color = ComposeColor(0xFFBB86FC), fontSize = 9.sp)
                    }
                }
                
                // Event Breaks indicator
                if (chartSettings.events.eventsBreaks) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .alpha(0.7f)
                            .background(
                                ComposeColor(android.graphics.Color.parseColor(chartSettings.events.eventsBreaksColor)),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Event Breaks",
                            color = ComposeColor(android.graphics.Color.parseColor(chartSettings.events.eventsBreaksColor)),
                            fontSize = 10.sp
                        )
                    }
                }
                
                // Toast notification indicator
                if (!chartSettings.alerts.hideToasts) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(ComposeColor(0x1550C878), RoundedCornerShape(4.dp))
                            .padding(4.dp)
                    ) {
                        Text("Notifications enabled", color = ComposeColor(0xFF50C878), fontSize = 9.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun OhlcItem(label: String, value: Float, fontSize: Int = 11, isBold: Boolean = false) {
    Row(modifier = Modifier.padding(end = 6.dp)) {
        Text(text = "$label ", color = ComposeColor.Gray, fontSize = fontSize.sp)
        Text(text = String.format("%.2f", value), color = ComposeColor.White, fontSize = fontSize.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun TradingButton(label: String, price: String, backgroundColor: ComposeColor) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 2.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (label.isNotEmpty()) {
            Text(text = label, color = ComposeColor.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Text(text = price, color = ComposeColor.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
