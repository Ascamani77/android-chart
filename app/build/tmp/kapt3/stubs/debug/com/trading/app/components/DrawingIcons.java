package com.trading.app.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\'\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0003\b\u00a5\u0001\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J7\u0010\u00a9\u0001\u001a\u00030\u00aa\u0001*\u00030\u00ab\u00012\b\u0010\u00ac\u0001\u001a\u00030\u00ad\u00012\b\u0010\u00ae\u0001\u001a\u00030\u00ad\u00012\b\u0010\u00af\u0001\u001a\u00030\u00ad\u00012\b\u0010\u00b0\u0001\u001a\u00030\u00ad\u0001H\u0002J-\u0010\u00b1\u0001\u001a\u00030\u00aa\u0001*\u00030\u00ab\u00012\b\u0010\u00ac\u0001\u001a\u00030\u00ad\u00012\b\u0010\u00ae\u0001\u001a\u00030\u00ad\u00012\b\u0010\u00b2\u0001\u001a\u00030\u00ad\u0001H\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0011\u0010\u000b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0011\u0010\r\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006R\u0011\u0010\u000f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u0011\u0010\u0011\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0006R\u0011\u0010\u0013\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0006R\u0011\u0010\u0015\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0006R\u0011\u0010\u0017\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0006R\u0011\u0010\u0019\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0006R\u0011\u0010\u001b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0006R\u0011\u0010\u001d\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0006R\u0011\u0010\u001f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0006R\u0011\u0010!\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0006R\u0011\u0010#\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0006R\u0011\u0010%\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0006R\u0011\u0010\'\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0006R\u0011\u0010)\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0006R\u0011\u0010+\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u0006R\u0011\u0010-\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u0006R\u0011\u0010/\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010\u0006R\u0011\u00101\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\u0006R\u0011\u00103\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010\u0006R\u0011\u00105\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010\u0006R\u0011\u00107\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010\u0006R\u0011\u00109\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010\u0006R\u0011\u0010;\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010\u0006R\u0011\u0010=\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010\u0006R\u0011\u0010?\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010\u0006R\u0011\u0010A\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010\u0006R\u0011\u0010C\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010\u0006R\u0011\u0010E\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bF\u0010\u0006R\u0011\u0010G\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bH\u0010\u0006R\u0011\u0010I\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bJ\u0010\u0006R\u0011\u0010K\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bL\u0010\u0006R\u0011\u0010M\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bN\u0010\u0006R\u0011\u0010O\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bP\u0010\u0006R\u0011\u0010Q\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bR\u0010\u0006R\u0011\u0010S\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bT\u0010\u0006R\u0011\u0010U\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bV\u0010\u0006R\u0011\u0010W\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bX\u0010\u0006R\u0011\u0010Y\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bZ\u0010\u0006R\u0011\u0010[\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\\\u0010\u0006R\u0011\u0010]\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b^\u0010\u0006R\u0011\u0010_\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b`\u0010\u0006R\u0011\u0010a\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bb\u0010\u0006R\u0011\u0010c\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bd\u0010\u0006R\u0011\u0010e\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bf\u0010\u0006R\u0011\u0010g\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bh\u0010\u0006R\u0011\u0010i\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bj\u0010\u0006R\u0011\u0010k\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bl\u0010\u0006R\u0011\u0010m\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bn\u0010\u0006R\u0011\u0010o\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bp\u0010\u0006R\u0011\u0010q\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\br\u0010\u0006R\u0011\u0010s\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bt\u0010\u0006R\u0011\u0010u\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bv\u0010\u0006R\u0011\u0010w\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bx\u0010\u0006R\u0011\u0010y\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bz\u0010\u0006R\u0011\u0010{\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b|\u0010\u0006R\u0011\u0010}\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b~\u0010\u0006R\u0012\u0010\u007f\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u0080\u0001\u0010\u0006R\u0013\u0010\u0081\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u0082\u0001\u0010\u0006R\u0013\u0010\u0083\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u0084\u0001\u0010\u0006R\u0013\u0010\u0085\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u0086\u0001\u0010\u0006R\u0013\u0010\u0087\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u0088\u0001\u0010\u0006R\u0013\u0010\u0089\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u008a\u0001\u0010\u0006R\u0013\u0010\u008b\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u008c\u0001\u0010\u0006R\u0013\u0010\u008d\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u008e\u0001\u0010\u0006R\u0013\u0010\u008f\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u0090\u0001\u0010\u0006R\u0013\u0010\u0091\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u0092\u0001\u0010\u0006R\u0013\u0010\u0093\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u0094\u0001\u0010\u0006R\u0013\u0010\u0095\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u0096\u0001\u0010\u0006R\u0013\u0010\u0097\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u0098\u0001\u0010\u0006R\u0013\u0010\u0099\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u009a\u0001\u0010\u0006R\u0013\u0010\u009b\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u009c\u0001\u0010\u0006R\u0013\u0010\u009d\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u009e\u0001\u0010\u0006R\u0013\u0010\u009f\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u00a0\u0001\u0010\u0006R\u0013\u0010\u00a1\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u00a2\u0001\u0010\u0006R\u0013\u0010\u00a3\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u00a4\u0001\u0010\u0006R\u0013\u0010\u00a5\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u00a6\u0001\u0010\u0006R\u0013\u0010\u00a7\u0001\u001a\u00020\u0004\u00a2\u0006\t\n\u0000\u001a\u0005\b\u00a8\u0001\u0010\u0006\u00a8\u0006\u00b3\u0001"}, d2 = {"Lcom/trading/app/components/DrawingIcons;", "", "()V", "ABCDPattern", "Landroidx/compose/ui/graphics/vector/ImageVector;", "getABCDPattern", "()Landroidx/compose/ui/graphics/vector/ImageVector;", "AnchoredVWAP", "getAnchoredVWAP", "AnchoredVolume", "getAnchoredVolume", "Arc", "getArc", "Arrow", "getArrow", "ArrowMarker", "getArrowMarker", "ArrowMarkerDown", "getArrowMarkerDown", "ArrowMarkerUp", "getArrowMarkerUp", "BarsPattern", "getBarsPattern", "Brush", "getBrush", "Circle", "getCircle", "CrossLine", "getCrossLine", "Curve", "getCurve", "CyclicLines", "getCyclicLines", "CypherPattern", "getCypherPattern", "DateAndPriceRange", "getDateAndPriceRange", "DateRange", "getDateRange", "DoubleCurve", "getDoubleCurve", "ElliottCorrectionWave", "getElliottCorrectionWave", "ElliottDoubleComboWave", "getElliottDoubleComboWave", "ElliottImpulseWave", "getElliottImpulseWave", "ElliottTriangleWave", "getElliottTriangleWave", "ElliottTripleComboWave", "getElliottTripleComboWave", "Ellipse", "getEllipse", "Emojis", "getEmojis", "Eraser", "getEraser", "ExtendedLine", "getExtendedLine", "FibChannel", "getFibChannel", "FibCircles", "getFibCircles", "FibRetracement", "getFibRetracement", "FibSpeedResistanceArcs", "getFibSpeedResistanceArcs", "FibSpeedResistanceFan", "getFibSpeedResistanceFan", "FibSpiral", "getFibSpiral", "FibTimeZone", "getFibTimeZone", "FibWedge", "getFibWedge", "FixedRangeVolume", "getFixedRangeVolume", "FlatTopBottom", "getFlatTopBottom", "Forecast", "getForecast", "GannBox", "getGannBox", "GannFan", "getGannFan", "GannSquare", "getGannSquare", "GannSquareFixed", "getGannSquareFixed", "GhostFeed", "getGhostFeed", "HeadAndShoulders", "getHeadAndShoulders", "HideDrawings", "getHideDrawings", "Highlighter", "getHighlighter", "HorizontalLine", "getHorizontalLine", "HorizontalRay", "getHorizontalRay", "IconsVisuals", "getIconsVisuals", "InfoLine", "getInfoLine", "KeepDrawing", "getKeepDrawing", "LockAllDrawings", "getLockAllDrawings", "LongPosition", "getLongPosition", "Magnet", "getMagnet", "Measure", "getMeasure", "ParallelChannel", "getParallelChannel", "Path", "getPath", "Pitchfan", "getPitchfan", "Pitchfork", "getPitchfork", "Polyline", "getPolyline", "PriceRange", "getPriceRange", "Projection", "getProjection", "Ray", "getRay", "Rectangle", "getRectangle", "RegressionTrend", "getRegressionTrend", "RemoveAll", "getRemoveAll", "RotatedRectangle", "getRotatedRectangle", "ShortPosition", "getShortPosition", "SineLine", "getSineLine", "Stickers", "getStickers", "ThreeDrivesPattern", "getThreeDrivesPattern", "TimeCycles", "getTimeCycles", "TrendAngle", "getTrendAngle", "TrendBasedFibExtension", "getTrendBasedFibExtension", "TrendBasedFibTime", "getTrendBasedFibTime", "TrendLine", "getTrendLine", "Triangle", "getTriangle", "TrianglePattern", "getTrianglePattern", "VerticalLine", "getVerticalLine", "XABCDPattern", "getXABCDPattern", "ZoomIn", "getZoomIn", "ZoomOut", "getZoomOut", "addOval", "", "Landroidx/compose/ui/graphics/vector/PathBuilder;", "x", "", "y", "w", "h", "drawCircle", "radius", "app_debug"})
public final class DrawingIcons {
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector TrendLine = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Ray = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector InfoLine = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ExtendedLine = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector TrendAngle = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector HorizontalLine = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector HorizontalRay = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector VerticalLine = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector CrossLine = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ParallelChannel = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector RegressionTrend = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector FlatTopBottom = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Pitchfork = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Brush = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Highlighter = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ArrowMarker = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Arrow = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ArrowMarkerUp = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ArrowMarkerDown = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Rectangle = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector RotatedRectangle = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Path = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Circle = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Ellipse = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Polyline = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Triangle = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Arc = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Curve = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector DoubleCurve = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector FibRetracement = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector TrendBasedFibExtension = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector FibChannel = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector FibTimeZone = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector FibSpeedResistanceFan = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector TrendBasedFibTime = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector FibCircles = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector FibSpiral = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector FibSpeedResistanceArcs = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector FibWedge = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector GannBox = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector GannSquareFixed = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector GannSquare = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector GannFan = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector XABCDPattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector CypherPattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector HeadAndShoulders = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ABCDPattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector TrianglePattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ThreeDrivesPattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ElliottImpulseWave = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ElliottCorrectionWave = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ElliottTriangleWave = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ElliottDoubleComboWave = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ElliottTripleComboWave = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector CyclicLines = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector TimeCycles = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector SineLine = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Measure = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Eraser = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector KeepDrawing = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector HideDrawings = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector LockAllDrawings = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Magnet = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector RemoveAll = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Pitchfan = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ZoomIn = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ZoomOut = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector LongPosition = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector ShortPosition = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Forecast = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector BarsPattern = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector GhostFeed = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Projection = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector AnchoredVWAP = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector FixedRangeVolume = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector AnchoredVolume = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector PriceRange = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector DateRange = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector DateAndPriceRange = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Emojis = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector Stickers = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.compose.ui.graphics.vector.ImageVector IconsVisuals = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.trading.app.components.DrawingIcons INSTANCE = null;
    
    private DrawingIcons() {
        super();
    }
    
    private final void drawCircle(androidx.compose.ui.graphics.vector.PathBuilder $this$drawCircle, float x, float y, float radius) {
    }
    
    private final void addOval(androidx.compose.ui.graphics.vector.PathBuilder $this$addOval, float x, float y, float w, float h) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getTrendLine() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getRay() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getInfoLine() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getExtendedLine() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getTrendAngle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getHorizontalLine() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getHorizontalRay() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getVerticalLine() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getCrossLine() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getParallelChannel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getRegressionTrend() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getFlatTopBottom() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getPitchfork() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getBrush() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getHighlighter() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getArrowMarker() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getArrow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getArrowMarkerUp() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getArrowMarkerDown() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getRectangle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getRotatedRectangle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getPath() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getCircle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getEllipse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getPolyline() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getTriangle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getArc() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getCurve() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getDoubleCurve() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getFibRetracement() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getTrendBasedFibExtension() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getFibChannel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getFibTimeZone() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getFibSpeedResistanceFan() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getTrendBasedFibTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getFibCircles() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getFibSpiral() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getFibSpeedResistanceArcs() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getFibWedge() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getGannBox() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getGannSquareFixed() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getGannSquare() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getGannFan() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getXABCDPattern() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getCypherPattern() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getHeadAndShoulders() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getABCDPattern() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getTrianglePattern() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getThreeDrivesPattern() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getElliottImpulseWave() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getElliottCorrectionWave() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getElliottTriangleWave() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getElliottDoubleComboWave() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getElliottTripleComboWave() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getCyclicLines() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getTimeCycles() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getSineLine() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getMeasure() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getEraser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getKeepDrawing() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getHideDrawings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getLockAllDrawings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getMagnet() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getRemoveAll() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getPitchfan() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getZoomIn() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getZoomOut() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getLongPosition() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getShortPosition() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getForecast() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getBarsPattern() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getGhostFeed() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getProjection() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getAnchoredVWAP() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getFixedRangeVolume() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getAnchoredVolume() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getPriceRange() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getDateRange() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getDateAndPriceRange() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getEmojis() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getStickers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getIconsVisuals() {
        return null;
    }
}