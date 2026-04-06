package com.trading.app;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = TradingApplication.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface TradingApplication_GeneratedInjector {
  void injectTradingApplication(TradingApplication tradingApplication);
}
