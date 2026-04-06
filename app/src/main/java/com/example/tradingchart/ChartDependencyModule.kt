package com.example.tradingchart

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChartDependencyModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideCandleMapper(gson: Gson): CandleMapper = CandleMapper(gson)

    @Provides
    @Singleton
    fun provideTradingChartJsBuilder(): TradingChartJsBuilder = TradingChartJsBuilder()

    @Provides
    @Singleton
    fun provideTradingChartCoordinator(jsBuilder: TradingChartJsBuilder, mapper: CandleMapper): TradingChartCoordinator = TradingChartCoordinator(jsBuilder, mapper)
}
