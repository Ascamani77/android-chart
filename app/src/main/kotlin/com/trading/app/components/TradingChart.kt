package com.trading.app.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.trading.app.data.Mt5Service
import com.trading.app.data.Mt5ReverseBridge
import com.trading.app.models.ChartSettings
import com.trading.app.models.Drawing
import com.trading.app.models.Position
import com.trading.app.models.Order
import com.trading.app.models.BalanceRecord
import com.trading.app.models.EconomicCalendarPayload
import com.trading.app.models.SymbolInfo
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.series.common.PriceLine
import com.tradingview.lightweightcharts.api.options.enums.PriceAxisPosition
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.models.*
import com.tradingview.lightweightcharts.view.ChartsView
import java.util.*
import com.tradingview.lightweightcharts.api.series.enums.*
import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import android.graphics.Color as AndroidColor
import com.trading.app.models.OHLCData
import com.trading.app.indicators.BbandsData
import com.trading.app.indicators.VwapData
import com.trading.app.utils.Indicators
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import com.tradingview.lightweightcharts.api.series.models.Time

private const val LOG_TAG = "TradingChart"
private const val MACD_SCALE_KEY = "macd_pane"
private const val VOLUME_SCALE_KEY = "volume_pane"
private const val ATR_SCALE_KEY = "atr_pane"

fun Time.toTimestamp(): Long = (this as? Time.Utc)?.timestamp ?: 0L
private fun Long.toChartTime(): Time = Time.Utc(this)
private fun OHLCData.toCandlestickData(): CandlestickData =
    CandlestickData(time = time.toChartTime(), open = open, high = high, low = low, close = close)

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
    val spread: Float = 0.2f,
    val time: Long = 0L
)

fun getFullSymbolName(symbol: String): String {
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
        "USOIL" -> "CFDs on Crude Oil (WTI)"
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

private fun toPriceScaleMode(scaleType: String): PriceScaleMode {
    return when (scaleType) {
        "Percent" -> PriceScaleMode.PERCENTAGE
        "Indexed to 100" -> PriceScaleMode.INDEXED_TO_100
        "Logarithmic" -> PriceScaleMode.LOGARITHMIC
        else -> PriceScaleMode.NORMAL
    }
}

private fun normalizeEpochSeconds(timestamp: Long): Long {
    return when {
        timestamp <= 0L -> 0L
        timestamp >= 1_000_000_000_000L -> timestamp / 1000L
        else -> timestamp
    }
}

private fun timeframeToSeconds(timeframe: String): Long {
    return when (timeframe.lowercase(Locale.US)) {
        "1m" -> 60L
        "5m" -> 5 * 60L
        "15m" -> 15 * 60L
        "30m" -> 30 * 60L
        "1h" -> 60 * 60L
        "4h" -> 4 * 60 * 60L
        "1d" -> 24 * 60 * 60L
        else -> 60 * 60L
    }
}

private fun alignToTimeframeStart(timestampSeconds: Long, timeframe: String): Long {
    val interval = timeframeToSeconds(timeframe)
    if (timestampSeconds <= 0L || interval <= 0L) return timestampSeconds
    return (timestampSeconds / interval) * interval
}

private fun applyTickToCandles(
    candles: List<OHLCData>,
    timeframe: String,
    lastPrice: Float,
    tickTimestampSeconds: Long,
    tickVolume: Float
): List<OHLCData> {
    if (!lastPrice.isFinite() || lastPrice <= 0f) return candles

    if (candles.isEmpty()) {
        val seededTime = if (tickTimestampSeconds > 0L) {
            alignToTimeframeStart(tickTimestampSeconds, timeframe)
        } else {
            0L
        }
        if (seededTime <= 0L) return candles
        return listOf(
            OHLCData(
                time = seededTime,
                open = lastPrice,
                high = lastPrice,
                low = lastPrice,
                close = lastPrice,
                volume = tickVolume.coerceAtLeast(0f)
            )
        )
    }

    val orderedCandles = candles.sortedBy(OHLCData::time)
    val interval = timeframeToSeconds(timeframe)
    val lastCandle = orderedCandles.last()
    val resolvedTickTime = if (tickTimestampSeconds > 0L) {
        alignToTimeframeStart(tickTimestampSeconds, timeframe)
    } else {
        lastCandle.time
    }

    if (resolvedTickTime < lastCandle.time) {
        return orderedCandles
    }

    val updatedCandles = orderedCandles.toMutableList()
    if (resolvedTickTime == lastCandle.time) {
        updatedCandles[updatedCandles.lastIndex] = lastCandle.copy(
            high = maxOf(lastCandle.high, lastPrice),
            low = minOf(lastCandle.low, lastPrice),
            close = lastPrice,
            volume = maxOf(lastCandle.volume, tickVolume.coerceAtLeast(0f))
        )
        return updatedCandles
    }

    var previousClose = lastCandle.close
    var nextBarTime = lastCandle.time + interval
    while (nextBarTime < resolvedTickTime) {
        updatedCandles.add(
            OHLCData(
                time = nextBarTime,
                open = previousClose,
                high = previousClose,
                low = previousClose,
                close = previousClose,
                volume = 0f
            )
        )
        nextBarTime += interval
    }

    updatedCandles.add(
        OHLCData(
            time = resolvedTickTime,
            open = previousClose,
            high = maxOf(previousClose, lastPrice),
            low = minOf(previousClose, lastPrice),
            close = lastPrice,
            volume = tickVolume.coerceAtLeast(0f)
        )
    )
    return updatedCandles
}

private fun safelyRemovePriceLine(api: SeriesApi, priceLine: PriceLine?) {
    if (priceLine == null) return
    runCatching {
        api.removePriceLine(priceLine)
    }.onFailure { error ->
        Log.w(LOG_TAG, "Ignoring stale price line removal: ${error.message}")
    }
}

private fun calculateHeikinAshi(data: List<OHLCData>): List<CandlestickData> {
    if (data.isEmpty()) return emptyList()
    val haData = mutableListOf<CandlestickData>()
    var prevOpen = data[0].open
    var prevClose = data[0].close

    data.forEach { candle ->
        val close = (candle.open + candle.high + candle.low + candle.close) / 4f
        val open = (prevOpen + prevClose) / 2f
        val high = maxOf(candle.high, maxOf(open, close))
        val low = minOf(candle.low, minOf(open, close))
        
        haData.add(CandlestickData(candle.time.toChartTime(), open, high, low, close))
        
        prevOpen = open
        prevClose = close
    }
    return haData
}

private fun buildVolumeHistogramData(data: List<OHLCData>): List<HistogramData> {
    if (data.isEmpty()) return emptyList()

    val hasRealVolume = data.any { it.volume > 0f }
    val values = if (hasRealVolume) {
        data.map { it.volume.coerceAtLeast(0f) }
    } else {
        data.map {
            val body = kotlin.math.abs(it.close - it.open)
            val range = (it.high - it.low).coerceAtLeast(0f)
            maxOf(range, body, 0.0001f)
        }
    }

    return data.mapIndexed { index, candle ->
        HistogramData(
            time = candle.time.toChartTime(),
            value = values[index],
            color = if (candle.close >= candle.open) {
                IntColor(AndroidColor.parseColor("#089981"))
            } else {
                IntColor(AndroidColor.parseColor("#F23645"))
            }
        )
    }
}

private fun resolvePaneMargins(
    showVolume: Boolean,
    showRsi: Boolean,
    showMacd: Boolean,
    showAtr: Boolean = false
): Map<String, PriceScaleMargins> {
    val indicatorHeight = 0.14f
    val gap = 0.02f
    val margins = mutableMapOf<String, PriceScaleMargins>()

    val activePanes = buildList {
        if (showAtr) add(ATR_SCALE_KEY)
        if (showMacd) add(MACD_SCALE_KEY)
        if (showRsi) add(RSI_SCALE_KEY)
        if (showVolume) add(VOLUME_SCALE_KEY)
    }

    val bottomInset = if (activePanes.firstOrNull() == RSI_SCALE_KEY) 0.04f else 0.0f
    var currentBottom = bottomInset

    activePanes.forEachIndexed { index, paneKey ->
        margins[paneKey] = PriceScaleMargins(
            top = (1f - currentBottom - indicatorHeight).coerceAtLeast(0f),
            bottom = currentBottom
        )
        currentBottom += indicatorHeight
        if (index < activePanes.lastIndex) {
            currentBottom += gap
        }
    }

    val finalBottom = if (currentBottom == 0.0f) 0.04f else currentBottom
    margins["main"] = PriceScaleMargins(top = 0.06f, bottom = finalBottom)
    
    return margins
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
    bbStdDev: Float = 2f,
    showAtr: Boolean = false,
    atrPeriod: Int = 14,
    showMacd: Boolean = false,
    macdFast: Int = 12,
    macdSlow: Int = 26,
    macdSignal: Int = 9,
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
    onDataLoaded: (List<OHLCData>) -> Unit = {},
    selectedTimeZone: String = "UTC",
    onQuoteUpdate: (SymbolQuote) -> Unit = {},
    positions: List<Position> = emptyList(),
    onPositionUpdate: (Position) -> Unit = {},
    onPositionDelete: (String) -> Unit = {},
    onAccountUpdate: (Mt5Service.AccountInfo) -> Unit = {},
    onPositionsUpdate: (List<Position>) -> Unit = {},
    orders: List<Order> = emptyList(),
    onOrdersUpdate: (List<Order>) -> Unit = {},
    onHistoryOrdersUpdate: (List<Order>) -> Unit = {},
    onBalanceHistoryUpdate: (List<com.trading.app.models.BalanceRecord>) -> Unit = {},
    onCalendarUpdate: (EconomicCalendarPayload) -> Unit = {},
    isCalendarVisible: Boolean = false,
    calendarRequestDateIso: String? = null,
    calendarRequestVersion: Int = 0,
    isNewsVisible: Boolean = false,
    onNewsUpdate: (com.trading.app.models.NewsPayload) -> Unit = {},
    onDoubleClick: (Float) -> Unit = {},
    reverseBridge: Mt5ReverseBridge? = null
) {
    var ohlcData by remember { mutableStateOf<List<OHLCData>>(emptyList()) }
    val candlestickData by remember {
        derivedStateOf { ohlcData.map(OHLCData::toCandlestickData) }
    }
    var currentQuoteState by remember { mutableStateOf<SymbolQuote?>(null) }
    var mainPriceScaleWidthPx by remember { mutableFloatStateOf(0f) }
    var seriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var chartsViewApi by remember { mutableStateOf<ChartsView?>(null) }
    var showMarketStatus by remember { mutableStateOf(false) }

    // Indicator series state
    val rsiPaneRefs = rememberRsiPaneRefs()
    var ema10SeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var ema20SeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var sma1SeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var sma2SeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var vwapBandFillSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var vwapBandMaskSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var vwapUpperSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var vwapSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var vwapLowerSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var atrSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var bbBandFillSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var bbBandMaskSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var bbUpperSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var bbMiddleSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var bbLowerSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var macdLineSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var macdSignalSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var macdHistogramSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }
    var volumeSeriesApi by remember { mutableStateOf<SeriesApi?>(null) }

    // High/Low lines state (Line and Label separate for color independence)
    val highLineState = remember { mutableStateOf<PriceLine?>(null) }
    val highLabelState = remember { mutableStateOf<PriceLine?>(null) }
    val lowLineState = remember { mutableStateOf<PriceLine?>(null) }
    val lowLabelState = remember { mutableStateOf<PriceLine?>(null) }
    var highLowPriceLineOwner by remember { mutableStateOf<SeriesApi?>(null) }
    
    val bidPriceLineState = remember { mutableStateOf<PriceLine?>(null) }
    val askPriceLineState = remember { mutableStateOf<PriceLine?>(null) }
    var bidAskPriceLineOwner by remember { mutableStateOf<SeriesApi?>(null) }

    val updatedOnQuoteUpdate = rememberUpdatedState(onQuoteUpdate)
    val updatedOnDataLoaded = rememberUpdatedState(onDataLoaded)
    val updatedOnAccountUpdate = rememberUpdatedState(onAccountUpdate)
    val updatedOnPositionsUpdate = rememberUpdatedState(onPositionsUpdate)
    val updatedOnOrdersUpdate = rememberUpdatedState(onOrdersUpdate)
    val updatedOnHistoryOrdersUpdate = rememberUpdatedState(onHistoryOrdersUpdate)
    val updatedOnBalanceHistoryUpdate = rememberUpdatedState(onBalanceHistoryUpdate)
    val updatedOnCalendarUpdate = rememberUpdatedState(onCalendarUpdate)
    val updatedOnNewsUpdate = rememberUpdatedState(onNewsUpdate)

    val currentSymbol = rememberUpdatedState(symbol)
    val currentTimeframe = rememberUpdatedState(timeframe)
    val showInlineRsiPane = showRsi

    // Range-based H/L state
    var visibleRangeHighLow by remember { mutableStateOf<Pair<Float, Float>?>(null) }
    val chartBgColor = getFullChartColor(chartSettings.canvas.fullChartColor, chartSettings.canvas.background)
    val rsiDataState = remember(ohlcData, showRsi, rsiPeriod) {
        calculateRsiChartData(
            candles = ohlcData,
            enabled = showInlineRsiPane,
            period = rsiPeriod
        )
    }
    val bbDataState = remember(ohlcData, showBb, bbPeriod, bbStdDev) {
        if (showBb) {
            com.trading.app.indicators.BbandsIndicator(bbPeriod, bbStdDev).calculateBands(ohlcData)
        } else {
            BbandsData(
                upperBand = emptyList(),
                middleBand = emptyList(),
                lowerBand = emptyList()
            )
        }
    }
    val vwapDataState = remember(ohlcData, showVwap) {
        if (showVwap) {
            com.trading.app.indicators.VwapIndicator().calculateBands(ohlcData)
        } else {
            VwapData(
                vwap = emptyList(),
                upperBand = emptyList(),
                lowerBand = emptyList()
            )
        }
    }
    val paneMargins = remember(showVolume, showInlineRsiPane, showMacd, showAtr) {
        resolvePaneMargins(showVolume, showInlineRsiPane, showMacd, showAtr)
    }

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
            pcIpAddress = "10.233.78.133",
            port = 8081,
            onHistoryUpdate = { receivedSymbol, history ->
                if (receivedSymbol.isEmpty() || receivedSymbol.equals(currentSymbol.value, ignoreCase = true)) {
                    val orderedHistory = history
                        .asSequence()
                        .mapNotNull { candle ->
                            if (candle.time <= 0L) return@mapNotNull null
                            if (!candle.open.isFinite() || !candle.high.isFinite() || !candle.low.isFinite() || !candle.close.isFinite()) {
                                return@mapNotNull null
                            }
                            val high = maxOf(candle.high, candle.open, candle.close)
                            val low = minOf(candle.low, candle.open, candle.close)
                            OHLCData(
                                time = candle.time,
                                open = candle.open,
                                high = high,
                                low = low,
                                close = candle.close,
                                volume = candle.volume.coerceAtLeast(0f)
                            )
                        }
                        .sortedBy(OHLCData::time)
                        .distinctBy(OHLCData::time)
                        .toList()

                    ohlcData = orderedHistory
                    // notify host that new data has been loaded
                    updatedOnDataLoaded.value(orderedHistory)
                    Log.d(LOG_TAG, "onHistoryUpdate: received ${orderedHistory.size} candles for $receivedSymbol")
                }
            },
            onQuoteUpdate = { quote ->
                if (quote.name.equals(currentSymbol.value, ignoreCase = true)) {
                    val prevClose = ohlcData.getOrNull(ohlcData.size - 2)?.close ?: quote.lastPrice
                    val change = quote.lastPrice - prevClose
                    val changePercent = if (prevClose != 0f) (change / prevClose) * 100f else 0f
                    
                    val updatedQuote = quote.copy(
                        change = change,
                        changePercent = changePercent
                    )
                    currentQuoteState = updatedQuote
                    ohlcData = applyTickToCandles(
                        candles = ohlcData,
                        timeframe = currentTimeframe.value,
                        lastPrice = updatedQuote.lastPrice,
                        tickTimestampSeconds = normalizeEpochSeconds(updatedQuote.time),
                        tickVolume = updatedQuote.volume
                    )
                    updatedOnQuoteUpdate.value(updatedQuote)
                }
            },
            onAccountUpdate = { accountInfo ->
                updatedOnAccountUpdate.value(accountInfo)
            },
            onPositionsUpdate = { updatedOnPositionsUpdate.value(it) },
            onOrdersUpdate = { updatedOnOrdersUpdate.value(it) },
            onHistoryOrdersUpdate = { updatedOnHistoryOrdersUpdate.value(it) },
            onBalanceHistoryUpdate = { updatedOnBalanceHistoryUpdate.value(it) },
            onCalendarUpdate = { updatedOnCalendarUpdate.value(it) },
            onNewsUpdate = { updatedOnNewsUpdate.value(it) }
        )
    }

    LaunchedEffect(Unit) {
        mt5Service.connect()
        reverseBridge?.connect()
    }

    LaunchedEffect(symbol, timeframe) {
        ohlcData = emptyList()
        currentQuoteState = null
        mt5Service.subscribe(symbol, timeframe)
    }

    LaunchedEffect(isCalendarVisible, calendarRequestDateIso, calendarRequestVersion) {
        if (isCalendarVisible) {
            mt5Service.requestCalendar(calendarRequestDateIso)
        }
    }

    LaunchedEffect(isNewsVisible) {
        if (isNewsVisible) {
            mt5Service.requestNews()
        }
    }

    LaunchedEffect(ohlcData, seriesApi, style, chartBgColor,
        showRsi, rsiPeriod, showEma10, ema10Period, showEma20, ema20Period, 
        showSma1, sma1Period, showSma2, sma2Period, showVwap, showBb, bbPeriod, bbStdDev, showAtr, atrPeriod, 
        showMacd, macdFast, macdSlow, macdSignal, showVolume) {
        val mainSeriesApi = seriesApi
        val ohlcList = ohlcData
        
        if (ohlcData.isNotEmpty()) {
            when (style) {
                "bars" -> mainSeriesApi?.setData(candlestickData.map { BarData(it.time, it.open, it.high, it.low, it.close) })
                "line", "area" -> mainSeriesApi?.setData(candlestickData.map { LineData(it.time, it.close) })
                "heikin_ashi" -> mainSeriesApi?.setData(calculateHeikinAshi(ohlcData))
                else -> mainSeriesApi?.setData(candlestickData)
            }

            updateInlineRsiPaneData(
                refs = rsiPaneRefs,
                candles = ohlcData,
                data = rsiDataState,
                enabled = showInlineRsiPane
            )
            
            if (showEma10) {
                val ema10Data = com.trading.app.indicators.EmaIndicator(ema10Period).calculate(ohlcList)
                ema10SeriesApi?.setData(ema10Data.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
            }

            if (showEma20) {
                val ema20Data = com.trading.app.indicators.EmaIndicator(ema20Period).calculate(ohlcList)
                ema20SeriesApi?.setData(ema20Data.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
            }

            if (showSma1) {
                val sma1Data = Indicators.calculateSma(ohlcList.map { it.close }, sma1Period)
                sma1SeriesApi?.setData(sma1Data.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
            }

            if (showSma2) {
                val sma2Data = Indicators.calculateSma(ohlcList.map { it.close }, sma2Period)
                sma2SeriesApi?.setData(sma2Data.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
            }

            if (showVwap) {
                val vwapBandFillColor = IntColor(applyOpacity(AndroidColor.parseColor("#2B4B60"), 18))
                val vwapBandMaskColor = IntColor(chartBgColor)

                vwapBandFillSeriesApi?.setData(vwapDataState.upperBand.mapIndexedNotNull { index, value ->
                    value?.let {
                        AreaData(
                            time = candlestickData[index].time,
                            value = it,
                            lineColor = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
                            topColor = vwapBandFillColor,
                            bottomColor = vwapBandFillColor
                        )
                    }
                })
                vwapBandMaskSeriesApi?.setData(vwapDataState.lowerBand.mapIndexedNotNull { index, value ->
                    value?.let {
                        AreaData(
                            time = candlestickData[index].time,
                            value = it,
                            lineColor = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
                            topColor = vwapBandMaskColor,
                            bottomColor = vwapBandMaskColor
                        )
                    }
                })
                vwapUpperSeriesApi?.setData(vwapDataState.upperBand.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
                vwapSeriesApi?.setData(vwapDataState.vwap.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
                vwapLowerSeriesApi?.setData(vwapDataState.lowerBand.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
            } else {
                vwapBandFillSeriesApi?.setData(emptyList())
                vwapBandMaskSeriesApi?.setData(emptyList())
                vwapUpperSeriesApi?.setData(emptyList())
                vwapSeriesApi?.setData(emptyList())
                vwapLowerSeriesApi?.setData(emptyList())
            }

            if (showAtr) {
                val atrData = com.trading.app.indicators.AtrIndicator(atrPeriod).calculate(ohlcList)
                atrSeriesApi?.setData(atrData.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
            }

            if (showBb) {
                val bandFillColor = IntColor(applyOpacity(AndroidColor.parseColor("#2B4B60"), 18))
                val bandMaskColor = IntColor(chartBgColor)

                bbBandFillSeriesApi?.setData(bbDataState.upperBand.mapIndexedNotNull { index, value ->
                    value?.let {
                        AreaData(
                            time = candlestickData[index].time,
                            value = it,
                            lineColor = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
                            topColor = bandFillColor,
                            bottomColor = bandFillColor
                        )
                    }
                })
                bbBandMaskSeriesApi?.setData(bbDataState.lowerBand.mapIndexedNotNull { index, value ->
                    value?.let {
                        AreaData(
                            time = candlestickData[index].time,
                            value = it,
                            lineColor = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
                            topColor = bandMaskColor,
                            bottomColor = bandMaskColor
                        )
                    }
                })
                bbUpperSeriesApi?.setData(bbDataState.upperBand.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
                bbMiddleSeriesApi?.setData(bbDataState.middleBand.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
                bbLowerSeriesApi?.setData(bbDataState.lowerBand.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
            } else {
                bbBandFillSeriesApi?.setData(emptyList())
                bbBandMaskSeriesApi?.setData(emptyList())
                bbUpperSeriesApi?.setData(emptyList())
                bbMiddleSeriesApi?.setData(emptyList())
                bbLowerSeriesApi?.setData(emptyList())
            }

            if (showMacd) {
                val macdIndicator = com.trading.app.indicators.MacdIndicator(macdFast, macdSlow, macdSignal)
                val macdLine = macdIndicator.calculateMacdLine(ohlcList)
                val signalLine = macdIndicator.calculateSignalLine(macdLine)
                val histogram = macdIndicator.calculateHistogram(macdLine, signalLine)

                macdLineSeriesApi?.setData(macdLine.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
                macdSignalSeriesApi?.setData(signalLine.mapIndexedNotNull { index, value ->
                    value?.let { LineData(candlestickData[index].time, it) }
                })
                macdHistogramSeriesApi?.setData(histogram.mapIndexedNotNull { index, value ->
                    value?.let {
                        HistogramData(
                            time = candlestickData[index].time,
                            value = it,
                            color = if (it >= 0) IntColor(AndroidColor.parseColor("#089981")) else IntColor(AndroidColor.parseColor("#F23645"))
                        )
                    }
                })
            } else {
                macdLineSeriesApi?.setData(emptyList())
                macdSignalSeriesApi?.setData(emptyList())
                macdHistogramSeriesApi?.setData(emptyList())
            }

            if (showVolume) {
                volumeSeriesApi?.setData(buildVolumeHistogramData(ohlcData))
            }
        } else {
            mainSeriesApi?.setData(emptyList())
            rsiPaneRefs.clearData()
            ema10SeriesApi?.setData(emptyList())
            ema20SeriesApi?.setData(emptyList())
            sma1SeriesApi?.setData(emptyList())
            sma2SeriesApi?.setData(emptyList())
            vwapBandFillSeriesApi?.setData(emptyList())
            vwapBandMaskSeriesApi?.setData(emptyList())
            vwapUpperSeriesApi?.setData(emptyList())
            vwapSeriesApi?.setData(emptyList())
            vwapLowerSeriesApi?.setData(emptyList())
            atrSeriesApi?.setData(emptyList())
            bbBandFillSeriesApi?.setData(emptyList())
            bbBandMaskSeriesApi?.setData(emptyList())
            bbUpperSeriesApi?.setData(emptyList())
            bbMiddleSeriesApi?.setData(emptyList())
            bbLowerSeriesApi?.setData(emptyList())
            macdLineSeriesApi?.setData(emptyList())
            macdSignalSeriesApi?.setData(emptyList())
            macdHistogramSeriesApi?.setData(emptyList())
            volumeSeriesApi?.setData(emptyList())
        }
    }

    LaunchedEffect(
        seriesApi,
        rsiPaneRefs.rsiSeriesApi,
        volumeSeriesApi,
        macdLineSeriesApi,
        atrSeriesApi,
        showInlineRsiPane,
        showMacd,
        showVolume,
        showAtr,
        chartSettings.canvas.scaleLineColor,
        chartSettings.scales
    ) {
        val mainScaleMargins = paneMargins["main"]!!
        val rsiScaleMargins = paneMargins[RSI_SCALE_KEY] ?: PriceScaleMargins(top = 0.72f, bottom = 0.04f)
        val macdScaleMargins = paneMargins[MACD_SCALE_KEY] ?: PriceScaleMargins(0.82f, 0.02f)
        val volumeScaleMargins = paneMargins[VOLUME_SCALE_KEY] ?: PriceScaleMargins(0.82f, 0.02f)
        val atrScaleMargins = paneMargins[ATR_SCALE_KEY] ?: PriceScaleMargins(0.82f, 0.02f)
        val scaleBorderColor = chartSettings.canvas.scaleLineColor.toIntColor()
        val scales = chartSettings.scales
        val activeScaleMode = toPriceScaleMode(scales.scaleType)
        val autoScaleEnabled = scales.autoScale && !scales.lockRatio
        val scalePosition = if (scales.scalesPlacement == "Left") PriceAxisPosition.LEFT else PriceAxisPosition.RIGHT

        seriesApi?.priceScale()?.applyOptions(
            PriceScaleOptions(
                autoScale = autoScaleEnabled,
                mode = activeScaleMode,
                invertScale = scales.invertScale,
                position = scalePosition,
                scaleMargins = mainScaleMargins
            )
        )

        applyInlineRsiPaneScale(
            refs = rsiPaneRefs,
            scaleMargins = rsiScaleMargins,
            borderColor = scaleBorderColor,
            visible = showInlineRsiPane
        )
        macdLineSeriesApi?.priceScale()?.applyOptions(
            PriceScaleOptions(
                autoScale = true,
                scaleMargins = macdScaleMargins,
                visible = showMacd,
                borderVisible = false,
                borderColor = scaleBorderColor,
                entireTextOnly = true,
                alignLabels = true,
                ticksVisible = false
            )
        )

        volumeSeriesApi?.priceScale()?.applyOptions(
            PriceScaleOptions(
                autoScale = true,
                scaleMargins = volumeScaleMargins,
                visible = false,
                borderVisible = false
            )
        )

        atrSeriesApi?.priceScale()?.applyOptions(
            PriceScaleOptions(
                autoScale = true,
                scaleMargins = atrScaleMargins,
                visible = showAtr,
                borderVisible = false,
                borderColor = scaleBorderColor,
                entireTextOnly = true,
                alignLabels = true,
                ticksVisible = false
            )
        )
    }

    LaunchedEffect(seriesApi, chartSettings.canvas.scaleFontSize) {
        seriesApi?.priceScale()?.width { width ->
            if (width > 0f) {
                mainPriceScaleWidthPx = width
            }
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

    // Double-click detection on the chart
    var lastClickTime by remember { mutableLongStateOf(0L) }
    LaunchedEffect(seriesApi) {
        val api = chartsViewApi?.api ?: return@LaunchedEffect
        
        api.subscribeClick { params ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime < 400) {
                // Double click detected
                val lastPrice = currentQuoteState?.lastPrice ?: 0f
                if (lastPrice != 0f) {
                    onDoubleClick(lastPrice)
                }
            }
            lastClickTime = currentTime
        }
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
        if (highLowPriceLineOwner === api) {
            safelyRemovePriceLine(api, highLineState.value)
            safelyRemovePriceLine(api, highLabelState.value)
            safelyRemovePriceLine(api, lowLineState.value)
            safelyRemovePriceLine(api, lowLabelState.value)
        }
        
        highLineState.value = null
        highLabelState.value = null
        lowLineState.value = null
        lowLabelState.value = null
        highLowPriceLineOwner = api

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
        
        if (bidAskPriceLineOwner === api) {
            safelyRemovePriceLine(api, bidPriceLineState.value)
            safelyRemovePriceLine(api, askPriceLineState.value)
        }
        bidPriceLineState.value = null
        askPriceLineState.value = null
        bidAskPriceLineOwner = api

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

    // Manage Position Price Lines
    val positionPriceLines = remember { mutableStateListOf<PriceLine>() }
    var positionPriceLineOwner by remember { mutableStateOf<SeriesApi?>(null) }
    // Using positions.toList() to ensure the effect re-runs when the list content changes
    val positionsSnapshot = positions.toList()
    LaunchedEffect(positionsSnapshot, seriesApi, symbol, currentQuoteState) {
        val api = seriesApi ?: return@LaunchedEffect
        val lastPrice = currentQuoteState?.lastPrice ?: 0f
        
        // Remove previous position lines
        if (positionPriceLineOwner === api) {
            positionPriceLines.forEach { safelyRemovePriceLine(api, it) }
        }
        positionPriceLines.clear()
        positionPriceLineOwner = api

        positionsSnapshot.filter { it.symbol.equals(symbol, ignoreCase = true) }.forEach { position ->
            val color = if (position.type.equals("buy", ignoreCase = true)) "#089981" else "#F23645"
            val isBuy = position.type.equals("buy", ignoreCase = true)
            
            val pnl = if (lastPrice > 0) {
                (lastPrice - position.entryPrice) * position.volume * (if (isBuy) 1f else -1f)
            } else 0f
            
            val pnlText = if (lastPrice > 0) {
                " [${if (pnl >= 0) "+" else ""}${String.format("%.2f", pnl)}]"
            } else ""

            // Entry Line
            positionPriceLines.add(
                api.createPriceLine(
                    PriceLineOptions(
                        price = position.entryPrice,
                        color = IntColor(AndroidColor.parseColor(color)),
                        lineWidth = LineWidth.TWO,
                        lineStyle = LineStyle.SOLID,
                        lineVisible = true,
                        axisLabelVisible = true,
                        title = "${position.type.uppercase()} ${position.volume} $pnlText"
                    )
                )
            )

            // TP Line
            position.tp?.let { tp ->
                val tpPnl = (tp - position.entryPrice) * position.volume * (if (isBuy) 1f else -1f)
                val tpPnlText = " [${if (tpPnl >= 0) "+" else ""}${String.format("%.2f", tpPnl)}]"
                
                positionPriceLines.add(
                    api.createPriceLine(
                        PriceLineOptions(
                            price = tp,
                            color = IntColor(AndroidColor.parseColor("#089981")),
                            lineWidth = LineWidth.ONE,
                            lineStyle = LineStyle.DASHED,
                            lineVisible = true,
                            axisLabelVisible = true,
                            title = "TP$tpPnlText"
                        )
                    )
                )
            }

            // SL Line
            position.sl?.let { sl ->
                val slPnl = (sl - position.entryPrice) * position.volume * (if (isBuy) 1f else -1f)
                val slPnlText = " [${if (slPnl >= 0) "+" else ""}${String.format("%.2f", slPnl)}]"
                
                positionPriceLines.add(
                    api.createPriceLine(
                        PriceLineOptions(
                            price = sl,
                            color = IntColor(AndroidColor.parseColor("#F23645")),
                            lineWidth = LineWidth.ONE,
                            lineStyle = LineStyle.DASHED,
                            lineVisible = true,
                            axisLabelVisible = true,
                            title = "SL$slPnlText"
                        )
                    )
                )
            }
        }
    }

    // Manage Order Price Lines (Pending Orders)
    val orderPriceLines = remember { mutableStateListOf<PriceLine>() }
    var orderPriceLineOwner by remember { mutableStateOf<SeriesApi?>(null) }
    val ordersSnapshot = orders.toList()
    LaunchedEffect(ordersSnapshot, seriesApi, symbol) {
        val api = seriesApi ?: return@LaunchedEffect
        
        // Remove previous order lines
        if (orderPriceLineOwner === api) {
            orderPriceLines.forEach { safelyRemovePriceLine(api, it) }
        }
        orderPriceLines.clear()
        orderPriceLineOwner = api

        ordersSnapshot.filter { it.symbol.equals(symbol, ignoreCase = true) }.forEach { order ->
            val color = if (order.type.equals("buy", ignoreCase = true)) "#089981" else "#F23645"
            
            // Order Price Line
            orderPriceLines.add(
                api.createPriceLine(
                    PriceLineOptions(
                        price = order.price,
                        color = IntColor(AndroidColor.parseColor(color)),
                        lineWidth = LineWidth.ONE,
                        lineStyle = LineStyle.DASHED,
                        lineVisible = true,
                        axisLabelVisible = true,
                        title = "${order.orderType.uppercase()} ${order.volume}"
                    )
                )
            )

            // TP Line for Order
            order.tp?.let { tp ->
                val isBuy = order.type.equals("buy", ignoreCase = true)
                val tpPnl = (tp - order.price) * order.volume * (if (isBuy) 1f else -1f)
                val tpPnlText = " [${if (tpPnl >= 0) "+" else ""}${String.format("%.2f", tpPnl)}]"
                
                orderPriceLines.add(
                    api.createPriceLine(
                        PriceLineOptions(
                            price = tp,
                            color = IntColor(AndroidColor.parseColor("#089981")),
                            lineWidth = LineWidth.ONE,
                            lineStyle = LineStyle.DASHED,
                            lineVisible = true,
                            axisLabelVisible = true,
                            title = "TP$tpPnlText"
                        )
                    )
                )
            }

            // SL Line for Order
            order.sl?.let { sl ->
                val isBuy = order.type.equals("buy", ignoreCase = true)
                val slPnl = (sl - order.price) * order.volume * (if (isBuy) 1f else -1f)
                val slPnlText = " [${if (slPnl >= 0) "+" else ""}${String.format("%.2f", slPnl)}]"
                
                orderPriceLines.add(
                    api.createPriceLine(
                        PriceLineOptions(
                            price = sl,
                            color = IntColor(AndroidColor.parseColor("#F23645")),
                            lineWidth = LineWidth.ONE,
                            lineStyle = LineStyle.DASHED,
                            lineVisible = true,
                            axisLabelVisible = true,
                            title = "SL$slPnlText"
                        )
                    )
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mt5Service.disconnect()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeColor.Black)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
                key(style, symbol, showRsi, showEma10, showEma20, showSma1, showSma2, showVwap, showBb, showAtr, showVolume) {
                    AndroidView(
                        factory = { context ->
                            ChartsView(context).apply {
                                chartsViewApi = this
                                rsiPaneRefs.clear()
                                val uppercaseSymbol = symbol.uppercase()
                                val isBitcoin = uppercaseSymbol.contains("BTC") || uppercaseSymbol.contains("BITCOIN")
                                val isForex = uppercaseSymbol.length == 6 || uppercaseSymbol.contains("/")
                                val mainScaleMargins = paneMargins["main"]!!
                                val macdScaleMargins = paneMargins[MACD_SCALE_KEY] ?: PriceScaleMargins(0.82f, 0.02f)
                                val volumeScaleMargins = paneMargins[VOLUME_SCALE_KEY] ?: PriceScaleMargins(0.82f, 0.02f)
                                val atrScaleMargins = paneMargins[ATR_SCALE_KEY] ?: PriceScaleMargins(0.82f, 0.02f)
                                val scales = chartSettings.scales
                                val activeScaleMode = toPriceScaleMode(scales.scaleType)
                                val autoScaleEnabled = scales.autoScale && !scales.lockRatio
                                val useLeftPriceScale = scales.scalesPlacement == "Left"

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
                                autoScale = autoScaleEnabled,
                                mode = activeScaleMode,
                                invertScale = scales.invertScale,
                                position = PriceAxisPosition.RIGHT,
                                visible = !useLeftPriceScale
                            )
                            leftPriceScale = PriceScaleOptions(
                                borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                                entireTextOnly = false,
                                autoScale = autoScaleEnabled,
                                mode = activeScaleMode,
                                invertScale = scales.invertScale,
                                position = PriceAxisPosition.LEFT,
                                visible = useLeftPriceScale
                            )
                            timeScale = TimeScaleOptions(
                                borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                                visible = true,
                                timeVisible = true
                            )
                            handleScroll = HandleScrollOptions(
                                pressedMouseMove = true,
                                horzTouchDrag = true,
                                vertTouchDrag = false
                            )
                            handleScale = HandleScaleOptions(
                                mouseWheel = true,
                                pinch = true,
                                axisPressedMouseMove = AxisPressedMouseMoveOptions(
                                    time = !scales.scalePriceChartOnly,
                                    price = true
                                )
                            )
                        }

                        api.timeScale.subscribeVisibleTimeRangeChange { range ->
                            if (range != null && candlestickData.isNotEmpty()) {
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
                                        priceFormat = PriceFormat.priceFormatBuiltIn(type = PriceFormat.Type.PRICE, precision = precision, minMove = minMove.toFloat()),
                                        priceLineVisible = priceLineVisible,
                                        lastValueVisible = lastValueVisible
                                    ),
                                    onSeriesCreated = { createdSeries ->
                                        seriesApi = createdSeries
                                        createdSeries.priceScale().applyOptions(
                                            PriceScaleOptions(
                                                autoScale = true,
                                                scaleMargins = mainScaleMargins
                                            )
                                        )
                                    }
                                )
                            }
                            "line" -> {
                                api.addLineSeries(
                                    options = LineSeriesOptions(
                                        color = chartSettings.symbol.upColor.toIntColor(),
                                        priceFormat = PriceFormat.priceFormatBuiltIn(type = PriceFormat.Type.PRICE, precision = precision, minMove = minMove.toFloat()),
                                        priceLineVisible = priceLineVisible,
                                        lastValueVisible = lastValueVisible
                                    ),
                                    onSeriesCreated = { createdSeries ->
                                        seriesApi = createdSeries
                                        createdSeries.priceScale().applyOptions(
                                            PriceScaleOptions(
                                                autoScale = true,
                                                scaleMargins = mainScaleMargins
                                            )
                                        )
                                    }
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
                                        priceFormat = PriceFormat.priceFormatBuiltIn(type = PriceFormat.Type.PRICE, precision = precision, minMove = minMove.toFloat()),
                                        priceLineVisible = priceLineVisible,
                                        lastValueVisible = lastValueVisible
                                    ),
                                    onSeriesCreated = { createdSeries ->
                                        seriesApi = createdSeries
                                        createdSeries.priceScale().applyOptions(
                                            PriceScaleOptions(
                                                autoScale = true,
                                                scaleMargins = mainScaleMargins
                                            )
                                        )
                                    }
                                )
                            }
                        }

                        if (showInlineRsiPane) {
                            createInlineRsiPaneSeries(this, rsiPaneRefs)
                        }

                        // Add MACD Series
                        api.addHistogramSeries(
                            options = HistogramSeriesOptions(
                                lastValueVisible = false,
                                priceLineVisible = false,
                                base = 0f,
                                priceFormat = PriceFormat.priceFormatBuiltIn(type = PriceFormat.Type.PRICE, precision = 4, minMove = 0.0001f),
                                priceScaleId = PriceScaleId(MACD_SCALE_KEY)
                            ),
                            onSeriesCreated = { macdHistogramSeriesApi = it }
                        )

                        api.addLineSeries(
                            options = LineSeriesOptions(
                                color = IntColor(AndroidColor.parseColor("#2962FF")),
                                lineWidth = LineWidth.ONE,
                                priceScaleId = PriceScaleId(MACD_SCALE_KEY),
                                lastValueVisible = true,
                                priceLineVisible = false
                            ),
                            onSeriesCreated = {
                                macdLineSeriesApi = it
                                it.priceScale().applyOptions(
                                    PriceScaleOptions(
                                        autoScale = true,
                                        scaleMargins = macdScaleMargins,
                                        visible = showMacd,
                                        borderVisible = false,
                                        borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                                        entireTextOnly = true,
                                        alignLabels = true,
                                        ticksVisible = false
                                    )
                                )
                            }
                        )

                        api.addLineSeries(
                            options = LineSeriesOptions(
                                color = IntColor(AndroidColor.parseColor("#FF6D00")),
                                lineWidth = LineWidth.ONE,
                                priceScaleId = PriceScaleId(MACD_SCALE_KEY),
                                lastValueVisible = false,
                                priceLineVisible = false
                            ),
                            onSeriesCreated = { macdSignalSeriesApi = it }
                        )

                        if (showEma10) {
                            api.addLineSeries(
                                options = LineSeriesOptions(
                                    color = IntColor(AndroidColor.parseColor("#2962FF")),
                                    lineWidth = LineWidth.ONE
                                ),
                                onSeriesCreated = { ema10SeriesApi = it }
                            )
                        }

                        if (showEma20) {
                            api.addLineSeries(
                                options = LineSeriesOptions(
                                    color = IntColor(AndroidColor.parseColor("#FF6D00")),
                                    lineWidth = LineWidth.ONE
                                ),
                                onSeriesCreated = { ema20SeriesApi = it }
                            )
                        }

                        if (showSma1) {
                            api.addLineSeries(
                                options = LineSeriesOptions(
                                    color = IntColor(AndroidColor.parseColor("#4CAF50")),
                                    lineWidth = LineWidth.ONE
                                ),
                                onSeriesCreated = { sma1SeriesApi = it }
                            )
                        }

                        if (showSma2) {
                            api.addLineSeries(
                                options = LineSeriesOptions(
                                    color = IntColor(AndroidColor.parseColor("#F44336")),
                                    lineWidth = LineWidth.ONE
                                ),
                                onSeriesCreated = { sma2SeriesApi = it }
                            )
                        }

                        if (showVwap) {
                            api.addAreaSeries(
                                options = AreaSeriesOptions(
                                    lastValueVisible = false,
                                    priceLineVisible = false,
                                    lineColor = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
                                    topColor = IntColor(applyOpacity(AndroidColor.parseColor("#2B4B60"), 18)),
                                    bottomColor = IntColor(applyOpacity(AndroidColor.parseColor("#2B4B60"), 18)),
                                    crosshairMarkerVisible = false
                                ),
                                onSeriesCreated = { vwapBandFillSeriesApi = it }
                            )
                            api.addAreaSeries(
                                options = AreaSeriesOptions(
                                    lastValueVisible = false,
                                    priceLineVisible = false,
                                    lineColor = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
                                    topColor = IntColor(chartBgColor),
                                    bottomColor = IntColor(chartBgColor),
                                    crosshairMarkerVisible = false
                                ),
                                onSeriesCreated = { vwapBandMaskSeriesApi = it }
                            )
                            api.addLineSeries(
                                options = LineSeriesOptions(
                                    color = IntColor(AndroidColor.parseColor("#4CAF50")),
                                    lineWidth = LineWidth.ONE,
                                    lastValueVisible = true,
                                    priceLineVisible = false
                                ),
                                onSeriesCreated = { vwapUpperSeriesApi = it }
                            )
                            api.addLineSeries(
                                options = LineSeriesOptions(
                                    color = IntColor(AndroidColor.parseColor("#2962FF")),
                                    lineWidth = LineWidth.ONE,
                                    lastValueVisible = true,
                                    priceLineVisible = false
                                ),
                                onSeriesCreated = { vwapSeriesApi = it }
                            )
                            api.addLineSeries(
                                options = LineSeriesOptions(
                                    color = IntColor(AndroidColor.parseColor("#4CAF50")),
                                    lineWidth = LineWidth.ONE,
                                    lastValueVisible = true,
                                    priceLineVisible = false
                                ),
                                onSeriesCreated = { vwapLowerSeriesApi = it }
                            )
                        }

                        if (showAtr) {
                            api.addLineSeries(
                                options = LineSeriesOptions(
                                    color = IntColor(AndroidColor.parseColor("#F44336")),
                                    lineWidth = LineWidth.ONE,
                                    priceScaleId = PriceScaleId(ATR_SCALE_KEY)
                                ),
                                onSeriesCreated = { api ->
                                    atrSeriesApi = api
                                    api.priceScale().applyOptions(
                                        PriceScaleOptions(
                                            autoScale = true,
                                            scaleMargins = atrScaleMargins,
                                            visible = showAtr,
                                            borderVisible = false,
                                            borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                                            entireTextOnly = true,
                                            alignLabels = true,
                                            ticksVisible = false
                                        )
                                    )
                                }
                            )
                        }

                        if (showBb) {
                            api.addAreaSeries(
                                options = AreaSeriesOptions(
                                    lastValueVisible = false,
                                    priceLineVisible = false,
                                    lineColor = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
                                    topColor = IntColor(applyOpacity(AndroidColor.parseColor("#2B4B60"), 18)),
                                    bottomColor = IntColor(applyOpacity(AndroidColor.parseColor("#2B4B60"), 18)),
                                    crosshairMarkerVisible = false
                                ),
                                onSeriesCreated = { bbBandFillSeriesApi = it }
                            )
                            api.addAreaSeries(
                                options = AreaSeriesOptions(
                                    lastValueVisible = false,
                                    priceLineVisible = false,
                                    lineColor = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
                                    topColor = IntColor(chartBgColor),
                                    bottomColor = IntColor(chartBgColor),
                                    crosshairMarkerVisible = false
                                ),
                                onSeriesCreated = { bbBandMaskSeriesApi = it }
                            )
                            api.addLineSeries(
                                options = LineSeriesOptions(
                                    color = IntColor(AndroidColor.parseColor("#F23645")),
                                    lineWidth = LineWidth.ONE,
                                    lastValueVisible = true,
                                    priceLineVisible = false
                                ),
                                onSeriesCreated = { bbUpperSeriesApi = it }
                            )
                            api.addLineSeries(
                                options = LineSeriesOptions(
                                    color = IntColor(AndroidColor.parseColor("#2196F3")),
                                    lineWidth = LineWidth.ONE,
                                    lastValueVisible = true,
                                    priceLineVisible = false
                                ),
                                onSeriesCreated = { bbMiddleSeriesApi = it }
                            )
                            api.addLineSeries(
                                options = LineSeriesOptions(
                                    color = IntColor(AndroidColor.parseColor("#00BFA5")),
                                    lineWidth = LineWidth.ONE,
                                    lastValueVisible = true,
                                    priceLineVisible = false
                                ),
                                onSeriesCreated = { bbLowerSeriesApi = it }
                            )
                        }

                        if (showVolume) {
                            api.addHistogramSeries(
                                options = HistogramSeriesOptions(
                                    lastValueVisible = false,
                                    priceLineVisible = false,
                                    base = 0f,
                                    priceFormat = PriceFormat.priceFormatBuiltIn(type = PriceFormat.Type.VOLUME, precision = 0, minMove = 1f),
                                    priceScaleId = PriceScaleId(VOLUME_SCALE_KEY)
                                ),
                                onSeriesCreated = {
                                    volumeSeriesApi = it
                                    it.priceScale().applyOptions(
                                        PriceScaleOptions(
                                            autoScale = true,
                                            scaleMargins = volumeScaleMargins,
                                            visible = false,
                                            borderVisible = false
                                        )
                                    )
                                }
                            )
                        }

                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { chartsView ->
                    val scales = chartSettings.scales
                    val activeScaleMode = toPriceScaleMode(scales.scaleType)
                    val autoScaleEnabled = scales.autoScale && !scales.lockRatio
                    val useLeftPriceScale = scales.scalesPlacement == "Left"

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
                        rightPriceScale = PriceScaleOptions(
                            borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                            entireTextOnly = false,
                            autoScale = autoScaleEnabled,
                            mode = activeScaleMode,
                            invertScale = scales.invertScale,
                            position = PriceAxisPosition.RIGHT,
                            visible = !useLeftPriceScale
                        )
                        leftPriceScale = PriceScaleOptions(
                            borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                            entireTextOnly = false,
                            autoScale = autoScaleEnabled,
                            mode = activeScaleMode,
                            invertScale = scales.invertScale,
                            position = PriceAxisPosition.LEFT,
                            visible = useLeftPriceScale
                        )
                        timeScale = TimeScaleOptions(
                            borderColor = chartSettings.canvas.scaleLineColor.toIntColor(),
                            visible = true,
                            timeVisible = true
                        )
                        handleScroll = HandleScrollOptions(
                            pressedMouseMove = true,
                            horzTouchDrag = true,
                            vertTouchDrag = false
                        )
                        handleScale = HandleScaleOptions(
                            mouseWheel = true,
                            pinch = true,
                            axisPressedMouseMove = AxisPressedMouseMoveOptions(
                                time = !scales.scalePriceChartOnly,
                                price = true
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

                RsiPaneOverlay(
                    visible = showInlineRsiPane,
                    scaleMargins = paneMargins[RSI_SCALE_KEY] ?: PriceScaleMargins(top = 0.72f, bottom = 0.04f),
                    data = rsiDataState,
                    rsiPeriod = rsiPeriod,
                    scaleTextColor = chartSettings.canvas.scaleTextColor,
                    scaleBorderColor = chartSettings.canvas.scaleLineColor,
                    scaleFontSize = chartSettings.canvas.scaleFontSize,
                    axisWidthPx = mainPriceScaleWidthPx
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
                val statusFontSize = 16.sp
                
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                    Text(
                        text = formatPrice(quote.lastPrice, symbol),
                        color = color,
                        fontSize = statusFontSize,
                        fontWeight = FontWeight.Medium
                    )
                    if (chartSettings.statusLine.barChangeValues) {
                        Spacer(modifier = Modifier.width(8.dp))
                        val sign = if (quote.change >= 0) "+" else ""
                        Text(
                            text = String.format("%s%s (%+.2f%%)", sign, formatPrice(quote.change, symbol), quote.changePercent),
                            color = color,
                            fontSize = statusFontSize,
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

                if (showBb) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "BB $bbPeriod SMA close ${formatBandMultiplier(bbStdDev)}",
                            color = ComposeColor(0xFFB2B5BE),
                            fontSize = 13.sp
                        )
                        bbDataState.latestMiddleBand?.let { middleBand ->
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = formatPrice(middleBand, symbol),
                                color = ComposeColor(0xFF2962FF),
                                fontSize = 13.sp
                            )
                        }
                        bbDataState.latestUpperBand?.let { upperBand ->
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = formatPrice(upperBand, symbol),
                                color = ComposeColor(0xFFF23645),
                                fontSize = 13.sp
                            )
                        }
                        bbDataState.latestLowerBand?.let { lowerBand ->
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = formatPrice(lowerBand, symbol),
                                color = ComposeColor(0xFF00BFA5),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                if (showVwap) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "VWAP hlc3 Session",
                            color = ComposeColor(0xFFB2B5BE),
                            fontSize = 13.sp
                        )
                        vwapDataState.latestVwap?.let { latestVwap ->
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = formatPrice(latestVwap, symbol),
                                color = ComposeColor(0xFF2962FF),
                                fontSize = 13.sp
                            )
                        }
                        vwapDataState.latestUpperBand?.let { upperBand ->
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = formatPrice(upperBand, symbol),
                                color = ComposeColor(0xFF4CAF50),
                                fontSize = 13.sp
                            )
                        }
                        vwapDataState.latestLowerBand?.let { lowerBand ->
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = formatPrice(lowerBand, symbol),
                                color = ComposeColor(0xFF4CAF50),
                                fontSize = 13.sp
                            )
                        }
                    }
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

private fun formatBandMultiplier(multiplier: Float): String {
    return if (multiplier % 1f == 0f) {
        multiplier.toInt().toString()
    } else {
        DecimalFormat("#.##", DecimalFormatSymbols(Locale.US)).format(multiplier)
    }
}

@Composable
fun OhlcItem(label: String, value: Float, symbol: String) {
    Row(modifier = Modifier.padding(end = 8.dp)) {
        Text(text = "$label ", color = ComposeColor.Gray, fontSize = 11.sp)
        Text(text = formatPrice(value, symbol), color = ComposeColor.White, fontSize = 11.sp)
    }
}

