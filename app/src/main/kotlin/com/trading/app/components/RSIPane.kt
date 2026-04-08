package com.trading.app.components

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trading.app.indicators.RsiIndicator
import com.trading.app.models.OHLCData
import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.*
import com.tradingview.lightweightcharts.api.series.models.*
import com.tradingview.lightweightcharts.view.ChartsView
import java.util.Locale

internal const val RSI_SCALE_KEY = "rsi_pane"
private const val RSI_PANE_BACKGROUND_HEX = "#000000"
private const val RSI_PANE_BORDER_HEX = "#363A45"
private const val RSI_MIN = 0f
private const val RSI_MID = 50f
private const val RSI_OVERBOUGHT = 70f
private const val RSI_MAX = 100f
private const val RSI_OVERSOLD = 30f

private fun Long.toChartTime(): Time = Time.Utc(this)

private fun applyOpacity(color: Int, opacity: Int): Int {
    val alpha = (opacity / 100f * 255).toInt().coerceIn(0, 255)
    return (color and 0x00FFFFFF) or (alpha shl 24)
}

private fun buildRsiData(
    candles: List<OHLCData>,
    rsiValues: List<Float?>
): List<LineData> {
    return rsiValues.mapIndexedNotNull { index, value ->
        value?.let {
            LineData(
                time = candles[index].time.toChartTime(),
                value = it.coerceIn(RSI_MIN, RSI_MAX)
            )
        }
    }
}

private fun buildFlatLineData(
    candles: List<OHLCData>,
    value: Float
): List<LineData> = candles.map { candle ->
    LineData(
        time = candle.time.toChartTime(),
        value = value
    )
}

private fun buildFlatAreaData(
    candles: List<OHLCData>,
    value: Float,
    lineColor: IntColor,
    topColor: IntColor,
    bottomColor: IntColor
): List<AreaData> = candles.map { candle ->
    AreaData(
        lineColor = lineColor,
        topColor = topColor,
        bottomColor = bottomColor,
        time = candle.time.toChartTime(),
        value = value
    )
}

internal data class RsiChartData(
    val values: List<Float?> = emptyList(),
    val movingAverageValues: List<Float?> = emptyList()
) {
    val latestValue: Float?
        get() = values.lastOrNull { it != null }

    val latestMovingAverageValue: Float?
        get() = movingAverageValues.lastOrNull { it != null }
}

@Stable
internal class RsiPaneRefs {
    var paneBackgroundSeriesApi by mutableStateOf<SeriesApi?>(null)
    var bandFillSeriesApi by mutableStateOf<SeriesApi?>(null)
    var bandMaskSeriesApi by mutableStateOf<SeriesApi?>(null)
    var lowerBoundarySeriesApi by mutableStateOf<SeriesApi?>(null)
    var upperBoundarySeriesApi by mutableStateOf<SeriesApi?>(null)
    var upperGuideSeriesApi by mutableStateOf<SeriesApi?>(null)
    var middleGuideSeriesApi by mutableStateOf<SeriesApi?>(null)
    var lowerGuideSeriesApi by mutableStateOf<SeriesApi?>(null)
    var rsiSeriesApi by mutableStateOf<SeriesApi?>(null)
    var movingAverageSeriesApi by mutableStateOf<SeriesApi?>(null)

    fun clear() {
        paneBackgroundSeriesApi = null
        bandFillSeriesApi = null
        bandMaskSeriesApi = null
        lowerBoundarySeriesApi = null
        upperBoundarySeriesApi = null
        upperGuideSeriesApi = null
        middleGuideSeriesApi = null
        lowerGuideSeriesApi = null
        rsiSeriesApi = null
        movingAverageSeriesApi = null
    }

    fun clearData() {
        paneBackgroundSeriesApi?.setData(emptyList())
        bandFillSeriesApi?.setData(emptyList())
        bandMaskSeriesApi?.setData(emptyList())
        lowerBoundarySeriesApi?.setData(emptyList())
        upperBoundarySeriesApi?.setData(emptyList())
        upperGuideSeriesApi?.setData(emptyList())
        middleGuideSeriesApi?.setData(emptyList())
        lowerGuideSeriesApi?.setData(emptyList())
        rsiSeriesApi?.setData(emptyList())
        movingAverageSeriesApi?.setData(emptyList())
    }

    fun priceScaleOwner(): SeriesApi? {
        return rsiSeriesApi
            ?: movingAverageSeriesApi
            ?: paneBackgroundSeriesApi
            ?: bandFillSeriesApi
            ?: bandMaskSeriesApi
    }

}

@Composable
internal fun rememberRsiPaneRefs(): RsiPaneRefs = remember { RsiPaneRefs() }

internal fun calculateRsiChartData(
    candles: List<OHLCData>,
    enabled: Boolean,
    period: Int
): RsiChartData {
    if (!enabled || candles.size <= period) {
        return RsiChartData()
    }

    val indicator = RsiIndicator(period = period)
    val values = indicator.calculate(candles)
    val movingAverageValues = indicator.calculateMa(values)
    return RsiChartData(
        values = values,
        movingAverageValues = movingAverageValues
    )
}

internal fun createInlineRsiPaneSeries(
    chartsView: ChartsView,
    refs: RsiPaneRefs
) {
    refs.clear()
    chartsView.api.addAreaSeries(
        options = AreaSeriesOptions(
            priceScaleId = PriceScaleId(RSI_SCALE_KEY),
            lastValueVisible = false,
            priceLineVisible = false,
            lineColor = IntColor(AndroidColor.parseColor(RSI_PANE_BORDER_HEX)),
            topColor = IntColor(AndroidColor.parseColor(RSI_PANE_BACKGROUND_HEX)),
            bottomColor = IntColor(AndroidColor.parseColor(RSI_PANE_BACKGROUND_HEX)),
            crosshairMarkerVisible = false
        ),
        onSeriesCreated = { refs.paneBackgroundSeriesApi = it }
    )

    chartsView.api.addAreaSeries(
        options = AreaSeriesOptions(
            priceScaleId = PriceScaleId(RSI_SCALE_KEY),
            lastValueVisible = false,
            priceLineVisible = false,
            lineColor = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
            topColor = IntColor(applyOpacity(AndroidColor.parseColor("#7E57C2"), 18)),
            bottomColor = IntColor(applyOpacity(AndroidColor.parseColor("#7E57C2"), 6)),
            crosshairMarkerVisible = false
        ),
        onSeriesCreated = { refs.bandFillSeriesApi = it }
    )

    chartsView.api.addAreaSeries(
        options = AreaSeriesOptions(
            priceScaleId = PriceScaleId(RSI_SCALE_KEY),
            lastValueVisible = false,
            priceLineVisible = false,
            lineColor = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
            topColor = IntColor(AndroidColor.parseColor(RSI_PANE_BACKGROUND_HEX)),
            bottomColor = IntColor(AndroidColor.parseColor(RSI_PANE_BACKGROUND_HEX)),
            crosshairMarkerVisible = false
        ),
        onSeriesCreated = { refs.bandMaskSeriesApi = it }
    )

    chartsView.api.addLineSeries(
        options = LineSeriesOptions(
            color = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
            lineWidth = LineWidth.ONE,
            lastValueVisible = false,
            priceLineVisible = false,
            priceScaleId = PriceScaleId(RSI_SCALE_KEY),
            crosshairMarkerVisible = false
        ),
        onSeriesCreated = { refs.lowerBoundarySeriesApi = it }
    )

    chartsView.api.addLineSeries(
        options = LineSeriesOptions(
            color = IntColor(applyOpacity(AndroidColor.WHITE, 0)),
            lineWidth = LineWidth.ONE,
            lastValueVisible = false,
            priceLineVisible = false,
            priceScaleId = PriceScaleId(RSI_SCALE_KEY),
            crosshairMarkerVisible = false
        ),
        onSeriesCreated = { refs.upperBoundarySeriesApi = it }
    )

    chartsView.api.addLineSeries(
        options = LineSeriesOptions(
            color = IntColor(applyOpacity(AndroidColor.parseColor("#7E57C2"), 45)),
            lineWidth = LineWidth.ONE,
            lineStyle = LineStyle.DASHED,
            lastValueVisible = false,
            priceLineVisible = false,
            priceScaleId = PriceScaleId(RSI_SCALE_KEY),
            crosshairMarkerVisible = false
        ),
        onSeriesCreated = { refs.upperGuideSeriesApi = it }
    )

    chartsView.api.addLineSeries(
        options = LineSeriesOptions(
            color = IntColor(applyOpacity(AndroidColor.parseColor("#7E57C2"), 28)),
            lineWidth = LineWidth.ONE,
            lineStyle = LineStyle.DASHED,
            lastValueVisible = false,
            priceLineVisible = false,
            priceScaleId = PriceScaleId(RSI_SCALE_KEY),
            crosshairMarkerVisible = false
        ),
        onSeriesCreated = { refs.middleGuideSeriesApi = it }
    )

    chartsView.api.addLineSeries(
        options = LineSeriesOptions(
            color = IntColor(applyOpacity(AndroidColor.parseColor("#7E57C2"), 45)),
            lineWidth = LineWidth.ONE,
            lineStyle = LineStyle.DASHED,
            lastValueVisible = false,
            priceLineVisible = false,
            priceScaleId = PriceScaleId(RSI_SCALE_KEY),
            crosshairMarkerVisible = false
        ),
        onSeriesCreated = { refs.lowerGuideSeriesApi = it }
    )

    chartsView.api.addLineSeries(
        options = LineSeriesOptions(
            color = IntColor(AndroidColor.parseColor("#7E57C2")),
            lineWidth = LineWidth.TWO,
            lineStyle = LineStyle.SOLID,
            lastValueVisible = false,
            priceLineVisible = false,
            priceFormat = PriceFormat.priceFormatBuiltIn(
                type = PriceFormat.Type.PRICE,
                precision = 2,
                minMove = 0.01f
            ),
            priceScaleId = PriceScaleId(RSI_SCALE_KEY)
        ),
        onSeriesCreated = { refs.rsiSeriesApi = it }
    )

    chartsView.api.addLineSeries(
        options = LineSeriesOptions(
            color = IntColor(AndroidColor.parseColor("#F2C94C")),
            lineWidth = LineWidth.TWO,
            lineStyle = LineStyle.SOLID,
            lastValueVisible = false,
            priceLineVisible = false,
            priceFormat = PriceFormat.priceFormatBuiltIn(
                type = PriceFormat.Type.PRICE,
                precision = 2,
                minMove = 0.01f
            ),
            priceScaleId = PriceScaleId(RSI_SCALE_KEY)
        ),
        onSeriesCreated = { refs.movingAverageSeriesApi = it }
    )
}

internal fun updateInlineRsiPaneData(
    refs: RsiPaneRefs,
    candles: List<OHLCData>,
    data: RsiChartData,
    enabled: Boolean
) {
    if (!enabled || candles.isEmpty()) {
        refs.clearData()
        return
    }

    val transparent = IntColor(applyOpacity(AndroidColor.WHITE, 0))
    val paneBackground = IntColor(AndroidColor.parseColor(RSI_PANE_BACKGROUND_HEX))
    val paneBorder = IntColor(AndroidColor.parseColor(RSI_PANE_BORDER_HEX))
    val bandTop = IntColor(applyOpacity(AndroidColor.parseColor("#7E57C2"), 18))
    val bandBottom = IntColor(applyOpacity(AndroidColor.parseColor("#7E57C2"), 6))

    refs.paneBackgroundSeriesApi?.setData(
        buildFlatAreaData(
            candles = candles,
            value = RSI_MAX,
            lineColor = paneBorder,
            topColor = paneBackground,
            bottomColor = paneBackground
        )
    )
    refs.bandFillSeriesApi?.setData(
        buildFlatAreaData(
            candles = candles,
            value = RSI_OVERBOUGHT,
            lineColor = transparent,
            topColor = bandTop,
            bottomColor = bandBottom
        )
    )
    refs.bandMaskSeriesApi?.setData(
        buildFlatAreaData(
            candles = candles,
            value = RSI_OVERSOLD,
            lineColor = transparent,
            topColor = paneBackground,
            bottomColor = paneBackground
        )
    )
    refs.lowerBoundarySeriesApi?.setData(buildFlatLineData(candles, RSI_MIN))
    refs.upperBoundarySeriesApi?.setData(buildFlatLineData(candles, RSI_MAX))
    refs.upperGuideSeriesApi?.setData(buildFlatLineData(candles, RSI_OVERBOUGHT))
    refs.middleGuideSeriesApi?.setData(buildFlatLineData(candles, RSI_MID))
    refs.lowerGuideSeriesApi?.setData(buildFlatLineData(candles, RSI_OVERSOLD))
    refs.rsiSeriesApi?.setData(buildRsiData(candles, data.values))
    refs.movingAverageSeriesApi?.setData(
        data.movingAverageValues.mapIndexedNotNull { index, value ->
            value?.let {
                LineData(
                    time = candles[index].time.toChartTime(),
                    value = it.coerceIn(RSI_MIN, RSI_MAX)
                )
            }
        }
    )
}

internal fun applyInlineRsiPaneScale(
    refs: RsiPaneRefs,
    scaleMargins: PriceScaleMargins,
    borderColor: IntColor,
    visible: Boolean
) {
    refs.priceScaleOwner()?.priceScale()?.applyOptions(
        PriceScaleOptions(
            autoScale = true,
            scaleMargins = scaleMargins,
            visible = visible,
            borderVisible = true,
            borderColor = borderColor,
            entireTextOnly = true,
            alignLabels = true,
            ticksVisible = false
        )
    )
}

private fun String.toComposeColor(): ComposeColor = try {
    ComposeColor(AndroidColor.parseColor(this))
} catch (_: Exception) {
    ComposeColor(0xFFD1D4DC)
}

private fun rsiAxisOffset(trackHeight: Dp, itemHeight: Dp, value: Float): Dp {
    val clampedValue = value.coerceIn(RSI_MIN, RSI_MAX)
    val usableHeight = (trackHeight - itemHeight).coerceAtLeast(0.dp)
    return usableHeight * (1f - (clampedValue / RSI_MAX))
}

private fun formatRsiAxisValue(value: Float): String = String.format(Locale.US, "%.2f", value)

@Composable
internal fun BoxScope.RsiPaneOverlay(
    visible: Boolean,
    scaleMargins: PriceScaleMargins,
    data: RsiChartData,
    rsiPeriod: Int,
    scaleTextColor: String,
    scaleBorderColor: String,
    scaleFontSize: Int,
    axisWidthPx: Float
) {
    if (!visible) return

    val density = LocalDensity.current
    val textColor = remember(scaleTextColor) { scaleTextColor.toComposeColor() }
    val borderColor = remember(scaleBorderColor) { scaleBorderColor.toComposeColor() }
    val axisTextSize = scaleFontSize.sp
    val axisWidth = with(density) {
        axisWidthPx.takeIf { it > 0f }?.toDp() ?: 56.dp
    }.coerceAtLeast(56.dp)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val paneTopPadding = maxHeight * (scaleMargins.top ?: 0f)
        val paneBottomPadding = maxHeight * (scaleMargins.bottom ?: 0f)
        val paneHeight = (maxHeight - paneTopPadding - paneBottomPadding).coerceAtLeast(0.dp)

        if (paneHeight <= 0.dp) {
            return@BoxWithConstraints
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = paneTopPadding + 8.dp, end = axisWidth + 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RSI $rsiPeriod close",
                color = textColor,
                fontSize = axisTextSize
            )
            data.latestValue?.let { latestValue ->
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = formatRsiAxisValue(latestValue),
                    color = ComposeColor(0xFF7E57C2),
                    fontSize = axisTextSize,
                    fontWeight = FontWeight.Bold
                )
            }
            data.latestMovingAverageValue?.let { latestMovingAverageValue ->
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = formatRsiAxisValue(latestMovingAverageValue),
                    color = ComposeColor(0xFFF2C94C),
                    fontSize = axisTextSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = paneTopPadding, bottom = paneBottomPadding)
                .width(axisWidth)
                .height(paneHeight)
                .background(ComposeColor.Black)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(borderColor)
            )

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val levelLabelHeight = with(density) { (scaleFontSize + 6).sp.toDp() }
                val lastValueLabelHeight = with(density) { (scaleFontSize + 8).sp.toDp() }

                listOf(RSI_MAX, RSI_OVERBOUGHT, RSI_MID, RSI_OVERSOLD, RSI_MIN).forEach { level ->
                    Text(
                        text = formatRsiAxisValue(level),
                        color = textColor,
                        fontSize = axisTextSize,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 6.dp)
                            .offset(y = rsiAxisOffset(maxHeight, levelLabelHeight, level))
                    )
                }

                data.latestMovingAverageValue?.let { latestMovingAverageValue ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(start = 8.dp, end = 4.dp)
                            .offset(y = rsiAxisOffset(maxHeight, lastValueLabelHeight, latestMovingAverageValue))
                            .background(ComposeColor(0xFFF2C94C))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = formatRsiAxisValue(latestMovingAverageValue),
                            color = ComposeColor.Black,
                            fontSize = axisTextSize,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                data.latestValue?.let { latestValue ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(start = 8.dp, end = 4.dp)
                            .offset(y = rsiAxisOffset(maxHeight, lastValueLabelHeight, latestValue))
                            .background(ComposeColor(0xFF7E57C2))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = formatRsiAxisValue(latestValue),
                            color = ComposeColor.White,
                            fontSize = axisTextSize,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
