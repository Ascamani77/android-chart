package com.trading.app.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\b\u0010\u0005\u001a\u00020\u0004H\u0007J\b\u0010\u0006\u001a\u00020\u0004H\u0007J\b\u0010\u0007\u001a\u00020\u0004H\u0007J\b\u0010\b\u001a\u00020\u0004H\u0007J\b\u0010\t\u001a\u00020\u0004H\u0007J\b\u0010\n\u001a\u00020\u0004H\u0007J\b\u0010\u000b\u001a\u00020\u0004H\u0007\u00a8\u0006\f"}, d2 = {"Lcom/trading/app/di/IndicatorModule;", "", "()V", "provideAtr", "Lcom/trading/app/indicators/TradingIndicator;", "provideBbands", "provideEma", "provideMacd", "provideRsi", "provideStochastic", "provideVolumeSpike", "provideVwap", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class IndicatorModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.trading.app.di.IndicatorModule INSTANCE = null;
    
    private IndicatorModule() {
        super();
    }
    
    @dagger.Provides()
    @dagger.multibindings.IntoSet()
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.indicators.TradingIndicator provideVolumeSpike() {
        return null;
    }
    
    @dagger.Provides()
    @dagger.multibindings.IntoSet()
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.indicators.TradingIndicator provideRsi() {
        return null;
    }
    
    @dagger.Provides()
    @dagger.multibindings.IntoSet()
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.indicators.TradingIndicator provideEma() {
        return null;
    }
    
    @dagger.Provides()
    @dagger.multibindings.IntoSet()
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.indicators.TradingIndicator provideVwap() {
        return null;
    }
    
    @dagger.Provides()
    @dagger.multibindings.IntoSet()
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.indicators.TradingIndicator provideBbands() {
        return null;
    }
    
    @dagger.Provides()
    @dagger.multibindings.IntoSet()
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.indicators.TradingIndicator provideAtr() {
        return null;
    }
    
    @dagger.Provides()
    @dagger.multibindings.IntoSet()
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.indicators.TradingIndicator provideMacd() {
        return null;
    }
    
    @dagger.Provides()
    @dagger.multibindings.IntoSet()
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.indicators.TradingIndicator provideStochastic() {
        return null;
    }
}