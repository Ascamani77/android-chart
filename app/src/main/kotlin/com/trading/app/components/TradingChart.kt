package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
        "AUDUSD" -> "Australian Dollar / U.S. Dollar"
        "USDCAD" -> "U.S. Dollar / Canadian Dollar"
        "USDCHF" -> "U.S. Dollar / Swiss Franc"
        "NZDUSD" -> "New Zealand Dollar / U.S. Dollar"
        else -> symbol
    }
}

private fun getSymbolLogo(symbol: String): String {
    val s = symbol.uppercase()
    return when {
        s.contains("BTC") -> "₿"
        s.contains("ETH") -> "Ξ"
        s.contains("EUR") -> "€"
        s.contains("GBP") -> "£"
        s.contains("JPY") -> "¥"
        else -> s.take(1)
    }
}

private fun getSymbolLogoColor(symbol: String): ComposeColor {
    val s = symbol.uppercase()
    return when {
        s.contains("BTC") -> ComposeColor(0xFFF7931A)
        s.contains("ETH") -> ComposeColor(0xFF627EEA)
        s.contains("EUR") -> ComposeColor(0xFF003399)
        s.contains("GBP") -> ComposeColor(0xFF00247D)
        else -> ComposeColor(0xFF2A2E39)
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

    var currentQuote by remember { mutableStateOf<SymbolQuote?>(
        SymbolQuote(
            name = symbol,
            lastPrice = 66486f,
            change = 165f,
            changePercent = 0.25f,
            open = 66321f,
            high = 66540f,
            low = 66280f,
            prevClose = 66321f,
            bid = 66485f,
            ask = 66487f,
            volume = 1500f
        )
    ) }

    var seriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var volumeSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }

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
        var lastClose = 66486f
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
                    val uppercaseSymbol = symbol.uppercase()
                    val isBitcoin = uppercaseSymbol.contains("BTC") || uppercaseSymbol.contains("BITCOIN")
                    val precision = if (isBitcoin) 0 else 2
                    val minMove = if (isBitcoin) 1f else 0.01f

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
                            borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                            entireTextOnly = false
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
                        localization = LocalizationOptions(
                            locale = "en-US"
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
                            wickDownColor = chartSettings.symbol.wickColorDown.toIntColor(),
                            priceFormat = PriceFormat.priceFormatBuiltIn(
                                type = PriceFormat.Type.PRICE,
                                precision = precision,
                                minMove = minMove
                            )
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
                val uppercaseSymbol = symbol.uppercase()
                val isBitcoin = uppercaseSymbol.contains("BTC") || uppercaseSymbol.contains("BITCOIN")
                val precision = if (isBitcoin) 0 else 2
                val minMove = if (isBitcoin) 1f else 0.01f

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
                        borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                        entireTextOnly = false
                    )
                    timeScale = TimeScaleOptions(
                        borderColor = chartSettings.canvas.scaleLineColor.toIntColor()
                    )
                    watermark = WatermarkOptions(
                        visible = chartSettings.canvas.watermarkVisible,
                        color = chartSettings.canvas.watermarkColor.toIntColor(),
                        text = if (chartSettings.canvas.watermarkVisible) symbol else ""
                    )
                    localization = LocalizationOptions(
                        locale = "en-US"
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
                        wickDownColor = chartSettings.symbol.wickColorDown.toIntColor(),
                        priceFormat = PriceFormat.priceFormatBuiltIn(
                            type = PriceFormat.Type.PRICE,
                            precision = precision,
                            minMove = minMove
                        )
                    )
                )

                volumeSeriesApi?.applyOptions(
                    HistogramSeriesOptions(
                        visible = chartSettings.statusLine.volume
                    )
                )
            }
        )

        // Top Right Currency Selector (as circled in red in image)
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
            // Line 1: Asset Name and Market Status Green Dot
            if (chartSettings.statusLine.symbol) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (chartSettings.statusLine.logo) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(getSymbolLogoColor(symbol)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getSymbolLogo(symbol),
                                color = ComposeColor.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (chartSettings.statusLine.titleMode == "Description") getFullSymbolName(symbol) else symbol,
                        color = ComposeColor(0xFFB2B5BE), // Muted grey to match TradingView
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Green Dot for Market Open with shadow/glow
                    if (chartSettings.statusLine.openMarketStatus) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(contentAlignment = Alignment.Center) {
                            // Outer soft glow
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(ComposeColor(0xFF089981).copy(alpha = 0.15f))
                            )
                            // Inner prominent glow
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(ComposeColor(0xFF089981).copy(alpha = 0.35f))
                            )
                            // Main Dot
                            Box(
                                modifier = Modifier
                                    .size(11.dp)
                                    .clip(CircleShape)
                                    .background(ComposeColor(0xFF089981))
                            )
                        }
                    }
                }
            }

            currentQuote?.let { quote ->
                val color = if (quote.change >= 0) ComposeColor(0xFF089981) else ComposeColor(0xFFF05252)
                
                // Line 2: Price and Change
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                    Text(
                        text = formatPrice(quote.lastPrice, symbol),
                        color = ComposeColor(0xFFD1D4DC),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (chartSettings.statusLine.barChangeValues) {
                        Spacer(modifier = Modifier.width(8.dp))
                        val formattedChange = formatPrice(quote.change, symbol)
                        val sign = if (quote.change >= 0) "+" else ""
                        Text(
                            text = String.format("%s%s (%+.2f%%)", sign, formattedChange, quote.changePercent),
                            color = color,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                // Line 3: Indicator Status (e.g. Vol · BTC)
                if (showVolume && chartSettings.statusLine.volume) {
                    Text(
                        text = "Vol · ${symbol.take(3).uppercase()}",
                        color = ComposeColor(0xFF787B86),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Line 4: Toggle indicators icon (under volume)
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

                // Additional Indicators
                if (showRsi) {
                    Text(
                        text = "RSI ($rsiPeriod)",
                        color = ComposeColor(0xFF787B86),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // OHLC Row
                if (chartSettings.statusLine.ohlc) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                        if (chartSettings.symbol.openVisible) OhlcItem("O", quote.open, symbol)
                        if (chartSettings.symbol.highVisible) OhlcItem("H", quote.high, symbol)
                        if (chartSettings.symbol.lowVisible) OhlcItem("L", quote.low, symbol)
                        if (chartSettings.symbol.closeVisible) OhlcItem("C", quote.lastPrice, symbol)
                    }
                }

                // Buy/Sell Buttons
                if (chartSettings.trading.buySellButtons) {
                    Row(modifier = Modifier.padding(top = 12.dp)) {
                        TradingButton(
                            label = if (chartSettings.trading.showBuySellLabels) "SELL" else "",
                            price = formatPrice(quote.bid, symbol),
                            backgroundColor = ComposeColor(android.graphics.Color.parseColor(chartSettings.scales.bidColor))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TradingButton(
                            label = if (chartSettings.trading.showBuySellLabels) "BUY" else "",
                            price = formatPrice(quote.ask, symbol),
                            backgroundColor = ComposeColor(android.graphics.Color.parseColor(chartSettings.scales.askColor))
                        )
                    }
                }
            }
        }
    }
}

private fun formatPrice(price: Float, symbol: String = ""): String {
    val symbols = DecimalFormatSymbols(Locale.US)
    symbols.groupingSeparator = ','
    val uppercaseSymbol = symbol.uppercase()
    val isBitcoin = uppercaseSymbol.contains("BTC") || uppercaseSymbol.contains("BITCOIN")
    val pattern = if (isBitcoin) "#,##0" else "#,##0.##"
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
