package com.example.tradingchart

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * EntryPoint used to obtain Hilt-provided coordinator from a plain Android view.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface TradingChartViewEntryPoint {
    fun getCoordinator(): TradingChartCoordinator
}
