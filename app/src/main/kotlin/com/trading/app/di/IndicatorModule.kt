package com.trading.app.di

import com.trading.app.indicators.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object IndicatorModule {

    @Provides
    @IntoSet
    fun provideVolumeSpike(): TradingIndicator = VolumeSpikeIndicator()

    @Provides
    @IntoSet
    fun provideRsi(): TradingIndicator = RsiIndicator()

    @Provides
    @IntoSet
    fun provideEma(): TradingIndicator = EmaIndicator(14)

    @Provides
    @IntoSet
    fun provideVwap(): TradingIndicator = VwapIndicator()

    @Provides
    @IntoSet
    fun provideBbands(): TradingIndicator = BbandsIndicator()

    @Provides
    @IntoSet
    fun provideAtr(): TradingIndicator = AtrIndicator()

    @Provides
    @IntoSet
    fun provideMacd(): TradingIndicator = MacdIndicator()

    @Provides
    @IntoSet
    fun provideStochastic(): TradingIndicator = StochasticIndicator()
}
