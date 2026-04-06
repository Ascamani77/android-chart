package com.trading.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trading.app.indicators.TradingIndicator
import com.trading.app.models.OHLCData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val indicators: Set<@JvmSuppressWildcards TradingIndicator>,
    val coordinator: com.trading.app.ui.chart.ChartCoordinator
) : ViewModel() {

    private val _indicatorResults = MutableStateFlow<Map<String, List<Float?>>>(emptyMap())
    val indicatorResults = _indicatorResults.asStateFlow()

    private val _indicatorParams = MutableStateFlow<Map<String, Any>>(emptyMap())
    val indicatorParams = _indicatorParams.asStateFlow()

    private val _activeIndicatorIds = MutableStateFlow<Set<String>>(emptySet())
    val activeIndicatorIds = _activeIndicatorIds.asStateFlow()

    private val _timestamps = MutableStateFlow<List<Long>>(emptyList())
    val timestamps = _timestamps.asStateFlow()

    fun toggleIndicator(id: String) {
        val current = _activeIndicatorIds.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        _activeIndicatorIds.value = current
    }

    fun updateIndicatorParams(id: String, params: Any) {
        val current = _indicatorParams.value.toMutableMap()
        current[id] = params
        _indicatorParams.value = current
    }

    fun calculateIndicators(candles: List<OHLCData>) {
        viewModelScope.launch {
            _timestamps.value = candles.map { it.time }
            val resultsMap = mutableMapOf<String, List<Float?>>()
            indicators.forEach { indicator ->
                val params = _indicatorParams.value[indicator.id]
                
                val result = if (indicator is com.trading.app.indicators.RsiIndicator && params is Pair<*, *>) {
                    com.trading.app.indicators.RsiIndicator(params.first as Int, params.second as Int).calculate(candles)
                } else if (indicator is com.trading.app.indicators.MacdIndicator && params is Triple<*, *, *>) {
                    com.trading.app.indicators.MacdIndicator(params.first as Int, params.second as Int, params.third as Int).calculate(candles)
                } else {
                    indicator.calculate(candles)
                }

                resultsMap[indicator.id] = result
                
                // Special handling for multi-series indicators like MACD or RSI with MA
                if (indicator is com.trading.app.indicators.MacdIndicator) {
                    val macd = if (params is Triple<*, *, *>) {
                        com.trading.app.indicators.MacdIndicator(params.first as Int, params.second as Int, params.third as Int)
                    } else indicator
                    
                    val signal = macd.calculateSignalLine(result)
                    val hist = macd.calculateHistogram(result, signal)
                    resultsMap["MACD_SIGNAL"] = signal
                    resultsMap["MACD_HIST"] = hist
                } else if (indicator is com.trading.app.indicators.RsiIndicator) {
                    val rsi = if (params is Pair<*, *>) {
                        com.trading.app.indicators.RsiIndicator(params.first as Int, params.second as Int)
                    } else indicator
                    
                    val rsiMa = rsi.calculateMa(result)
                    resultsMap["RSI_MA"] = rsiMa
                }
            }
            _indicatorResults.value = resultsMap
        }
    }
    
    fun getIndicatorMetadata() = indicators.map { 
        IndicatorMetadata(it.id, it.name, it.color)
    }

    data class IndicatorMetadata(val id: String, val name: String, val color: Int)
}
