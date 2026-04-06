package com.example.tradingchart;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\b\u0010\u0007\u001a\u00020\u0006H\u0007J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004H\u0007J\b\u0010\r\u001a\u00020\u000bH\u0007\u00a8\u0006\u000e"}, d2 = {"Lcom/example/tradingchart/ChartDependencyModule;", "", "()V", "provideCandleMapper", "Lcom/example/tradingchart/CandleMapper;", "gson", "Lcom/google/gson/Gson;", "provideGson", "provideTradingChartCoordinator", "Lcom/example/tradingchart/TradingChartCoordinator;", "jsBuilder", "Lcom/example/tradingchart/TradingChartJsBuilder;", "mapper", "provideTradingChartJsBuilder", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class ChartDependencyModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.tradingchart.ChartDependencyModule INSTANCE = null;
    
    private ChartDependencyModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.google.gson.Gson provideGson() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.tradingchart.CandleMapper provideCandleMapper(@org.jetbrains.annotations.NotNull()
    com.google.gson.Gson gson) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.tradingchart.TradingChartJsBuilder provideTradingChartJsBuilder() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.tradingchart.TradingChartCoordinator provideTradingChartCoordinator(@org.jetbrains.annotations.NotNull()
    com.example.tradingchart.TradingChartJsBuilder jsBuilder, @org.jetbrains.annotations.NotNull()
    com.example.tradingchart.CandleMapper mapper) {
        return null;
    }
}