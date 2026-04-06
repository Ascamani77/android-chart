package com.trading.app.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0004\u0018\u0019\u001a\u001bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J&\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u00042\b\b\u0002\u0010\b\u001a\u00020\tJ&\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00042\u000e\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00042\u0006\u0010\b\u001a\u00020\tJ.\u0010\u000b\u001a\u00020\f2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u00042\u000e\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00042\b\b\u0002\u0010\u000e\u001a\u00020\tJ.\u0010\u000f\u001a\u00020\u00102\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u00042\u000e\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00042\b\b\u0002\u0010\u000e\u001a\u00020\tJ*\u0010\u0011\u001a\u00020\u00122\u000e\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00042\b\b\u0002\u0010\u0013\u001a\u00020\u00052\b\b\u0002\u0010\u0014\u001a\u00020\u0005J(\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u00042\u000e\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00042\b\b\u0002\u0010\u000e\u001a\u00020\tH\u0002\u00a8\u0006\u001c"}, d2 = {"Lcom/trading/app/utils/Indicators;", "", "()V", "calculateRsi", "", "", "data", "Lcom/trading/app/models/OHLCData;", "period", "", "calculateSma", "detectCardwellReversals", "Lcom/trading/app/utils/Indicators$CardwellResult;", "rsi", "window", "detectDivergence", "Lcom/trading/app/utils/Indicators$DivergenceResult;", "detectFailureSwings", "Lcom/trading/app/utils/Indicators$FailureSwingResult;", "overbought", "oversold", "findPivots", "Lcom/trading/app/utils/Indicators$Pivot;", "values", "CardwellResult", "DivergenceResult", "FailureSwingResult", "Pivot", "app_debug"})
public final class Indicators {
    @org.jetbrains.annotations.NotNull()
    public static final com.trading.app.utils.Indicators INSTANCE = null;
    
    private Indicators() {
        super();
    }
    
    /**
     * Calculates the Relative Strength Index (RSI) using Wilder's Smoothing Method (RMA).
     *
     * Formula:
     * RSI = 100 - (100 / (1 + RS))
     * RS = AvgGain / AvgLoss
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Float> calculateRsi(@org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.OHLCData> data, int period) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.Float> calculateSma(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Float> data, int period) {
        return null;
    }
    
    private final java.util.List<com.trading.app.utils.Indicators.Pivot> findPivots(java.util.List<java.lang.Float> values, int window) {
        return null;
    }
    
    /**
     * Detects Bullish and Bearish Divergences.
     * Bullish: Price Lower Low, RSI Higher Low
     * Bearish: Price Higher High, RSI Lower High
     */
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.utils.Indicators.DivergenceResult detectDivergence(@org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.OHLCData> data, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Float> rsi, int window) {
        return null;
    }
    
    /**
     * Detects Failure Swings.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.utils.Indicators.FailureSwingResult detectFailureSwings(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Float> rsi, float overbought, float oversold) {
        return null;
    }
    
    /**
     * Cardwell's Reversals:
     * Positive Reversal: Price Higher Low, RSI Lower Low.
     * Negative Reversal: Price Lower High, RSI Higher High.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.trading.app.utils.Indicators.CardwellResult detectCardwellReversals(@org.jetbrains.annotations.NotNull()
    java.util.List<com.trading.app.models.OHLCData> data, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Float> rsi, int window) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0006J\u000f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J)\u0010\f\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0010\u001a\u00020\u0004H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b\u00a8\u0006\u0013"}, d2 = {"Lcom/trading/app/utils/Indicators$CardwellResult;", "", "positiveReversals", "", "", "negativeReversals", "(Ljava/util/List;Ljava/util/List;)V", "getNegativeReversals", "()Ljava/util/List;", "getPositiveReversals", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
    public static final class CardwellResult {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<java.lang.Integer> positiveReversals = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<java.lang.Integer> negativeReversals = null;
        
        public CardwellResult(@org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> positiveReversals, @org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> negativeReversals) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> getPositiveReversals() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> getNegativeReversals() {
            return null;
        }
        
        public CardwellResult() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.trading.app.utils.Indicators.CardwellResult copy(@org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> positiveReversals, @org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> negativeReversals) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0006J\u000f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J)\u0010\f\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0010\u001a\u00020\u0004H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b\u00a8\u0006\u0013"}, d2 = {"Lcom/trading/app/utils/Indicators$DivergenceResult;", "", "bullish", "", "", "bearish", "(Ljava/util/List;Ljava/util/List;)V", "getBearish", "()Ljava/util/List;", "getBullish", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
    public static final class DivergenceResult {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<java.lang.Integer> bullish = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<java.lang.Integer> bearish = null;
        
        public DivergenceResult(@org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> bullish, @org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> bearish) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> getBullish() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> getBearish() {
            return null;
        }
        
        public DivergenceResult() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.trading.app.utils.Indicators.DivergenceResult copy(@org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> bullish, @org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> bearish) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0006J\u000f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J)\u0010\f\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0010\u001a\u00020\u0004H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b\u00a8\u0006\u0013"}, d2 = {"Lcom/trading/app/utils/Indicators$FailureSwingResult;", "", "bullish", "", "", "bearish", "(Ljava/util/List;Ljava/util/List;)V", "getBearish", "()Ljava/util/List;", "getBullish", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
    public static final class FailureSwingResult {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<java.lang.Integer> bullish = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<java.lang.Integer> bearish = null;
        
        public FailureSwingResult(@org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> bullish, @org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> bearish) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> getBullish() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> getBearish() {
            return null;
        }
        
        public FailureSwingResult() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<java.lang.Integer> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.trading.app.utils.Indicators.FailureSwingResult copy(@org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> bullish, @org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.Integer> bearish) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0007H\u00c6\u0003J\'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00072\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0017"}, d2 = {"Lcom/trading/app/utils/Indicators$Pivot;", "", "index", "", "value", "", "isHigh", "", "(IFZ)V", "getIndex", "()I", "()Z", "getValue", "()F", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "toString", "", "app_debug"})
    public static final class Pivot {
        private final int index = 0;
        private final float value = 0.0F;
        private final boolean isHigh = false;
        
        public Pivot(int index, float value, boolean isHigh) {
            super();
        }
        
        public final int getIndex() {
            return 0;
        }
        
        public final float getValue() {
            return 0.0F;
        }
        
        public final boolean isHigh() {
            return false;
        }
        
        public final int component1() {
            return 0;
        }
        
        public final float component2() {
            return 0.0F;
        }
        
        public final boolean component3() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.trading.app.utils.Indicators.Pivot copy(int index, float value, boolean isHigh) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}