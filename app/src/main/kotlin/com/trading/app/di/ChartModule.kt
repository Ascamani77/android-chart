package com.trading.app.di

import com.trading.app.ui.chart.ChartCoordinator
import com.trading.app.ui.chart.IndicatorCalculator
import com.trading.app.ui.chart.IndicatorJsBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChartModule {

    @Provides
    @Singleton
    fun provideIndicatorJsBuilder(): IndicatorJsBuilder = IndicatorJsBuilder()

    @Provides
    @Singleton
    fun provideIndicatorCalculator(): IndicatorCalculator = IndicatorCalculator()

    @Provides
    @Singleton
    fun provideChartCoordinator(
        jsBuilder: IndicatorJsBuilder
    ): ChartCoordinator = ChartCoordinator(jsBuilder)
}
