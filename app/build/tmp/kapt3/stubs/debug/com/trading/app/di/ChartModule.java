package com.trading.app.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\b\u0010\u0007\u001a\u00020\bH\u0007J\b\u0010\t\u001a\u00020\u0006H\u0007\u00a8\u0006\n"}, d2 = {"Lcom/trading/app/di/ChartModule;", "", "()V", "provideChartCoordinator", "Lcom/trading/app/ui/chart/ChartCoordinator;", "jsBuilder", "Lcom/trading/app/ui/chart/IndicatorJsBuilder;", "provideIndicatorCalculator", "Lcom/trading/app/ui/chart/IndicatorCalculator;", "provideIndicatorJsBuilder", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class ChartModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.trading.app.di.ChartModule INSTANCE = null;
    
    private ChartModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.ui.chart.IndicatorJsBuilder provideIndicatorJsBuilder() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.ui.chart.IndicatorCalculator provideIndicatorCalculator() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.ui.chart.ChartCoordinator provideChartCoordinator(@org.jetbrains.annotations.NotNull()
    com.trading.app.ui.chart.IndicatorJsBuilder jsBuilder) {
        return null;
    }
}