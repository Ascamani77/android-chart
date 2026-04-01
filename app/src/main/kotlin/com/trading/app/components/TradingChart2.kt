package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trading.app.models.ChartSettings
import com.trading.app.models.Drawing
import com.trading.app.models.PartialOrder
import com.trading.app.models.Position
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun TradingChart2(
    symbol: String,
    timeframe: String,
    style: String,
    chartSettings: ChartSettings,
    drawings: List<Drawing>,
    onDrawingUpdate: (Drawing) -> Unit,
    activeTool: String?,
    onToolReset: () -> Unit,
    showRsi: Boolean = false,
    rsiPeriod: Int = 14,
    showEma10: Boolean = false,
    ema10Period: Int = 10,
    showEma20: Boolean = false,
    ema20Period: Int = 20,
    showSma1: Boolean = false,
    sma1Period: Int = 21,
    showSma2: Boolean = false,
    sma2Period: Int = 10,
    showVwap: Boolean = false,
    showBb: Boolean = false,
    bbPeriod: Int = 20,
    showAtr: Boolean = false,
    atrPeriod: Int = 14,
    showVolume: Boolean = true,
    isCrosshairActive: Boolean = false,
    onCrosshairToggle: (Boolean) -> Unit = {},
    onVolumeToggle: (Boolean) -> Unit = {},
    onIndicatorSettingsClick: (String) -> Unit = {},
    isMagnetEnabled: Boolean = false,
    isLocked: Boolean = false,
    isVisible: Boolean = true,
    selectedCurrency: String = "USD",
    onCurrencyClick: () -> Unit = {},
    isFullscreen: Boolean = false,
    onFullscreenExit: () -> Unit = {},
    scrollToTimestamp: Long? = null,
    onScrollDone: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    selectedTimeZone: String = "UTC",
    onQuoteUpdate: (SymbolQuote) -> Unit = {},
    positions: List<Position>,
    onPositionUpdate: (Position) -> Unit,
    onPositionDelete: (String) -> Unit,
    isTradingBarVisible: Boolean = false
) {
    var lotSize by remember { mutableStateOf("1.0000") }
    var tpPrice by remember { mutableStateOf<Float?>(null) }
    var slPrice by remember { mutableStateOf<Float?>(null) }
    
    var currentLiveQuote by remember { mutableStateOf<SymbolQuote?>(null) }
    var showModifyModal by remember { mutableStateOf(false) }
    var selectedPositionToModify by remember { mutableStateOf<Position?>(null) }
    var offset by remember { mutableStateOf(IntOffset(100, 100)) }

    Box(modifier = Modifier.fillMaxSize()) {
        TradingChart(
            symbol = symbol,
            timeframe = timeframe,
            style = style,
            chartSettings = chartSettings,
            drawings = drawings,
            onDrawingUpdate = onDrawingUpdate,
            activeTool = activeTool,
            onToolReset = onToolReset,
            showRsi = showRsi,
            rsiPeriod = rsiPeriod,
            showEma10 = showEma10,
            ema10Period = ema10Period,
            showEma20 = showEma20,
            ema20Period = ema20Period,
            showSma1 = showSma1,
            sma1Period = sma1Period,
            showSma2 = showSma2,
            sma2Period = sma2Period,
            showVwap = showVwap,
            showBb = showBb,
            bbPeriod = bbPeriod,
            showAtr = showAtr,
            atrPeriod = atrPeriod,
            showVolume = showVolume,
            isCrosshairActive = isCrosshairActive,
            onCrosshairToggle = onCrosshairToggle,
            onVolumeToggle = onVolumeToggle,
            onIndicatorSettingsClick = onIndicatorSettingsClick,
            isMagnetEnabled = isMagnetEnabled,
            isLocked = isLocked,
            isVisible = isVisible,
            selectedCurrency = selectedCurrency,
            onCurrencyClick = onCurrencyClick,
            isFullscreen = isFullscreen,
            onFullscreenExit = onFullscreenExit,
            scrollToTimestamp = scrollToTimestamp,
            onScrollDone = onScrollDone,
            onLongPress = onLongPress,
            onSettingsClick = onSettingsClick,
            selectedTimeZone = selectedTimeZone,
            onQuoteUpdate = { 
                currentLiveQuote = it
                onQuoteUpdate(it)
            },
            positions = positions,
            onPositionUpdate = onPositionUpdate,
            onPositionDelete = onPositionDelete,
            onDoubleClick = { price ->
                val lastPrice = currentLiveQuote?.lastPrice ?: price
                val tolerance = lastPrice * 0.02f
                val targetPos = positions.find {
                    it.symbol.equals(symbol, ignoreCase = true) &&
                    abs(it.entryPrice - lastPrice) < tolerance
                }
                if (targetPos != null) {
                    selectedPositionToModify = targetPos
                }
            }
        )

        // Floating Trading Bar
        if (isTradingBarVisible && chartSettings.trading.oneClickTrading) {
            Box(
                modifier = Modifier
                    .offset { offset }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offset = IntOffset(
                                (offset.x + dragAmount.x).roundToInt(),
                                (offset.y + dragAmount.y).roundToInt()
                            )
                        }
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF131722).copy(alpha = 0.95f))
                    .padding(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(40.dp)
                ) {
                    // Drag handle
                    Column(
                        modifier = Modifier.padding(start = 6.dp, end = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        repeat(3) {
                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                repeat(2) {
                                    Box(modifier = Modifier.size(2.5.dp).background(Color(0xFF434651), RoundedCornerShape(50)))
                                }
                            }
                        }
                    }

                    // Sell Button
                    Box(
                        modifier = Modifier
                            .width(82.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFFF23645))
                            .clickable {
                                currentLiveQuote?.let { quote ->
                                    onPositionUpdate(Position(
                                        symbol = symbol,
                                        type = "sell",
                                        entryPrice = quote.bid,
                                        volume = lotSize.toFloatOrNull() ?: 1f,
                                        time = System.currentTimeMillis(),
                                        tp = tpPrice,
                                        sl = slPrice
                                    ))
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentLiveQuote?.bid?.toString() ?: "0.00",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }

                    // Lot Size Input
                    Column(
                        modifier = Modifier
                            .width(58.dp)
                            .padding(horizontal = 2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("0.00", color = Color(0xFF787B86), fontSize = 9.sp)
                        BasicTextField(
                            value = lotSize,
                            onValueChange = { lotSize = it },
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            cursorBrush = SolidColor(Color.White),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Buy Button
                    Box(
                        modifier = Modifier
                            .width(82.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFF2962FF))
                            .clickable {
                                currentLiveQuote?.let { quote ->
                                    onPositionUpdate(Position(
                                        symbol = symbol,
                                        type = "buy",
                                        entryPrice = quote.ask,
                                        volume = lotSize.toFloatOrNull() ?: 1f,
                                        time = System.currentTimeMillis(),
                                        tp = tpPrice,
                                        sl = slPrice
                                    ))
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentLiveQuote?.ask?.toString() ?: "0.00",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }

                    // More Button
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp, end = 2.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { showModifyModal = true }
                            .padding(2.dp)
                    ) {
                        Icon(
                            Icons.Default.MoreHoriz,
                            contentDescription = "More",
                            tint = Color(0xFFD1D4DC),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Modal for settings TP/SL for the next trade
        if (showModifyModal) {
            ModifyTpSlModal(
                symbol = symbol,
                qty = lotSize,
                entryPrice = currentLiveQuote?.lastPrice ?: 0f,
                lastTradedPrice = currentLiveQuote?.lastPrice ?: 0f,
                isBuy = true,
                initialTp = tpPrice,
                initialSl = slPrice,
                initialPartialOrders = emptyList(),
                allPositions = positions,
                currentPrice = currentLiveQuote?.lastPrice ?: 0f,
                onConfirm = { tp, sl, partials ->
                    tpPrice = tp
                    slPrice = sl
                    showModifyModal = false
                },
                onCancel = { showModifyModal = false },
                onClosePosition = onPositionDelete
            )
        }

        // Modal for modifying an existing position
        if (selectedPositionToModify != null) {
            val pos = selectedPositionToModify!!
            ModifyTpSlModal(
                symbol = pos.symbol,
                qty = pos.volume.toString(),
                entryPrice = pos.entryPrice,
                lastTradedPrice = currentLiveQuote?.lastPrice ?: pos.entryPrice,
                isBuy = pos.type.equals("buy", ignoreCase = true),
                initialTp = pos.tp,
                initialSl = pos.sl,
                initialPartialOrders = pos.partialOrders,
                allPositions = positions,
                currentPrice = currentLiveQuote?.lastPrice ?: 0f,
                onConfirm = { tp, sl, partials ->
                    onPositionUpdate(pos.copy(tp = tp, sl = sl, partialOrders = partials))
                    selectedPositionToModify = null
                },
                onCancel = { selectedPositionToModify = null },
                onClosePosition = onPositionDelete
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyTpSlModal(
    symbol: String,
    qty: String,
    entryPrice: Float,
    lastTradedPrice: Float,
    isBuy: Boolean = true,
    initialTp: Float? = null,
    initialSl: Float? = null,
    initialPartialOrders: List<PartialOrder> = emptyList(),
    allPositions: List<Position> = emptyList(),
    currentPrice: Float = 0f,
    onConfirm: (Float?, Float?, List<PartialOrder>) -> Unit,
    onCancel: () -> Unit,
    onClosePosition: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("Close Position") }
    
    val tickSize = if (symbol.uppercase().contains("BTC")) 0.1f else 0.00001f
    val precision = if (symbol.uppercase().contains("BTC")) 1 else 5
    fun formatPriceValue(price: Float): String = String.format("%.${precision}f", price).replace(",", ".")

    var tpTriggerPrice by remember { mutableStateOf(initialTp?.let { formatPriceValue(it) } ?: "") }
    var slTriggerPrice by remember { mutableStateOf(initialSl?.let { formatPriceValue(it) } ?: "") }
    var tpSource by remember { mutableStateOf("Last") }
    var slSource by remember { mutableStateOf("Last") }
    
    val partialOrders = remember { mutableStateListOf<PartialOrder>().apply { addAll(initialPartialOrders) } }
    
    // State for adding/modifying a partial order
    var isEditingPartial by remember { mutableStateOf(false) }
    var partialOrderId by remember { mutableStateOf<String?>(null) }
    var partialQty by remember { mutableStateOf("0.001") }
    var partialTpPrice by remember { mutableStateOf("") }
    var partialSlPrice by remember { mutableStateOf("") }

    val primaryColor = Color(0xFF2962FF)
    val backgroundColor = Color(0xFF131722)
    val boxBorderColor = Color.White.copy(alpha = 0.15f)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF787B86), width = 40.dp, height = 4.dp) },
        containerColor = backgroundColor,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp).padding(horizontal = 16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(if (isEditingPartial) "Modify Partial Order" else "Modify TP/SL", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = if (isEditingPartial) { { isEditingPartial = false } } else onCancel, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, null, tint = Color(0xFF787B86))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Stats Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem("Entry Price", String.format("%,.2f", entryPrice))
                StatItem("Qty", qty)
                StatItem("Last Traded Price", String.format("%,.2f", lastTradedPrice))
                StatItem("Liq. Price", "18,860.0", color = Color(0xFFF2A52C))
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!isEditingPartial) {
                // Tabs
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    TabItem("Close Position", selectedTab == "Close Position", primaryColor) { selectedTab = "Close Position" }
                    Spacer(modifier = Modifier.width(16.dp))
                    TabItem("Entire Position", selectedTab == "Entire Position", primaryColor) { selectedTab = "Entire Position" }
                    Spacer(modifier = Modifier.width(16.dp))
                    TabItem("Partial Position", selectedTab == "Partial Position", primaryColor, isNew = true) { selectedTab = "Partial Position" }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.HelpOutline, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                when (selectedTab) {
                    "Close Position" -> {
                        var closeSubTab by remember { mutableStateOf("Live") }
                        
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    "Live Trades (${allPositions.size})",
                                    color = if (closeSubTab == "Live") Color.White else Color(0xFF787B86),
                                    fontSize = 14.sp,
                                    fontWeight = if (closeSubTab == "Live") FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.clickable { closeSubTab = "Live" }.padding(vertical = 4.dp)
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(
                                    "Pending Trades",
                                    color = if (closeSubTab == "Pending") Color.White else Color(0xFF787B86),
                                    fontSize = 14.sp,
                                    fontWeight = if (closeSubTab == "Pending") FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.clickable { closeSubTab = "Pending" }.padding(vertical = 4.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (closeSubTab == "Live") {
                                if (allPositions.isEmpty()) {
                                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
                                        Text("No live trades", color = Color(0xFF787B86))
                                    }
                                } else {
                                    LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                                        items(allPositions) { pos ->
                                            val pnl = if (pos.type.lowercase() == "buy") (currentPrice - pos.entryPrice) * pos.volume else (pos.entryPrice - currentPrice) * pos.volume
                                            val pnlColor = if (pnl >= 0) Color(0xFF089981) else Color(0xFFF23645)
                                            
                                            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Text(pos.symbol, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Text(
                                                                pos.type.uppercase(),
                                                                color = if (pos.type.lowercase() == "buy") Color(0xFF2962FF) else Color(0xFFF2A52C),
                                                                fontSize = 11.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                modifier = Modifier.background(
                                                                    (if (pos.type.lowercase() == "buy") Color(0xFF2962FF) else Color(0xFFF2A52C)).copy(alpha = 0.1f),
                                                                    RoundedCornerShape(2.dp)
                                                                ).padding(horizontal = 4.dp, vertical = 1.dp)
                                                            )
                                                        }
                                                        Text("Qty: ${pos.volume} @ ${String.format("%,.2f", pos.entryPrice)}", color = Color(0xFF787B86), fontSize = 12.sp)
                                                    }
                                                    
                                                    Column(horizontalAlignment = Alignment.End) {
                                                        Text(
                                                            "${if (pnl >= 0) "+" else ""}${String.format("%.2f", pnl)} USDT",
                                                            color = pnlColor,
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 15.sp
                                                        )
                                                        Button(
                                                            onClick = { onClosePosition(pos.id) },
                                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2E39)),
                                                            shape = RoundedCornerShape(4.dp),
                                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                                            modifier = Modifier.height(28.dp)
                                                        ) {
                                                            Text("Close", color = Color.White, fontSize = 12.sp)
                                                        }
                                                    }
                                                }
                                                Divider(color = boxBorderColor, modifier = Modifier.padding(top = 8.dp))
                                            }
                                        }
                                    }
                                }

                                // Find current position's P&L
                                val currentPos = allPositions.find { 
                                    it.symbol.equals(symbol, ignoreCase = true) && 
                                    abs(it.entryPrice - entryPrice) < 0.001f && 
                                    abs(it.volume - (qty.toFloatOrNull() ?: 0f)) < 0.001f 
                                }
                                
                                if (currentPos != null) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    val pnl = if (currentPos.type.lowercase() == "buy") (currentPrice - currentPos.entryPrice) * currentPos.volume else (currentPos.entryPrice - currentPrice) * currentPos.volume
                                    
                                    Button(
                                        onClick = { 
                                            onClosePosition(currentPos.id)
                                            onCancel()
                                        },
                                        modifier = Modifier.fillMaxWidth().height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = if (pnl >= 0) Color(0xFF089981) else Color(0xFFF23645)),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "Close ${currentPos.symbol} with ${if (pnl >= 0) "profit" else "loss"} ${if (pnl >= 0) "+" else ""}${String.format("%.2f", pnl)} USDT",
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    // If empty, add a small spacer to maintain some height consistency
                                    Spacer(modifier = Modifier.height(40.dp))
                                }
                            } else {
                                // Pending orders (TP/SL/Partials)
                                val pendingOrders = allPositions.flatMap { pos -> 
                                    pos.partialOrders.map { it.copy(tpOrderPrice = pos.symbol) } // Using tpOrderPrice as temporary symbol holder for display
                                }
                                
                                if (pendingOrders.isEmpty()) {
                                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
                                        Text("No pending trades", color = Color(0xFF787B86))
                                    }
                                } else {
                                    LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                                        items(pendingOrders) { order ->
                                            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(order.tpOrderPrice, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                                        Text("Partial Order Qty: ${order.volume}", color = Color(0xFF787B86), fontSize = 12.sp)
                                                        Row {
                                                            if (order.tp != null) Text("TP: ${String.format("%,.2f", order.tp)}", color = Color(0xFF089981), fontSize = 11.sp, modifier = Modifier.padding(end = 8.dp))
                                                            if (order.sl != null) Text("SL: ${String.format("%,.2f", order.sl)}", color = Color(0xFFF23645), fontSize = 11.sp)
                                                        }
                                                    }
                                                    Button(
                                                        onClick = { /* Cancel partial */ },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2E39)),
                                                        shape = RoundedCornerShape(4.dp),
                                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                                        modifier = Modifier.height(28.dp)
                                                    ) {
                                                        Text("Cancel", color = Color.White, fontSize = 12.sp)
                                                    }
                                                }
                                                Divider(color = boxBorderColor, modifier = Modifier.padding(top = 8.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    "Entire Position" -> {
                        // Take Profit Section
                        val tpVal = tpTriggerPrice.toFloatOrNull()
                        val tpPnl = tpVal?.let { if (isBuy) (it - entryPrice) * (qty.toFloatOrNull() ?: 1f) else (entryPrice - it) * (qty.toFloatOrNull() ?: 1f) }
                        val tpPnlText = tpPnl?.let { " (${if(it >= 0) "+" else ""}${String.format("%.2f", it)})" } ?: ""

                        Text("Take Profit-Trigger by Price$tpPnlText", color = Color(0xFF787B86), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        PriceInputRow(
                            value = tpTriggerPrice,
                            onValueChange = { tpTriggerPrice = it },
                            onIncrement = { 
                                val current = tpTriggerPrice.toFloatOrNull() ?: lastTradedPrice
                                tpTriggerPrice = formatPriceValue(current + tickSize)
                            },
                            onDecrement = {
                                val current = tpTriggerPrice.toFloatOrNull() ?: lastTradedPrice
                                tpTriggerPrice = formatPriceValue((current - tickSize).coerceAtLeast(0f))
                            },
                            selectedSource = tpSource,
                            onSourceChange = { tpSource = it },
                            primaryColor = primaryColor,
                            borderColor = boxBorderColor
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        PercentageSelector(borderColor = boxBorderColor, onPercentSelect = { pct ->
                            val target = if (isBuy) entryPrice * (1f + pct / 100f) else entryPrice * (1f - pct / 100f)
                            tpTriggerPrice = formatPriceValue(target)
                        })

                        Spacer(modifier = Modifier.height(12.dp))
                        SummaryText("Take Profit", tpTriggerPrice, entryPrice, qty.toFloatOrNull() ?: 1f, isBuy, isProfit = true)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Stop Loss Section
                        val slVal = slTriggerPrice.toFloatOrNull()
                        val slPnl = slVal?.let { if (isBuy) (it - entryPrice) * (qty.toFloatOrNull() ?: 1f) else (entryPrice - it) * (qty.toFloatOrNull() ?: 1f) }
                        val slPnlText = slPnl?.let { " (${if(it >= 0) "+" else ""}${String.format("%.2f", it)})" } ?: ""

                        Text("Stop Loss-Trigger by Price$slPnlText", color = Color(0xFF787B86), fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        PriceInputRow(
                            value = slTriggerPrice,
                            onValueChange = { slTriggerPrice = it },
                            onIncrement = { 
                                val current = slTriggerPrice.toFloatOrNull() ?: lastTradedPrice
                                slTriggerPrice = formatPriceValue(current + tickSize)
                            },
                            onDecrement = {
                                val current = slTriggerPrice.toFloatOrNull() ?: lastTradedPrice
                                slTriggerPrice = formatPriceValue((current - tickSize).coerceAtLeast(0f))
                            },
                            selectedSource = slSource,
                            onSourceChange = { slSource = it },
                            primaryColor = primaryColor,
                            borderColor = boxBorderColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        PercentageSelector(borderColor = boxBorderColor, onPercentSelect = { pct ->
                            val target = if (isBuy) entryPrice * (1f - pct / 100f) else entryPrice * (1f + pct / 100f)
                            slTriggerPrice = formatPriceValue(target)
                        })

                        Spacer(modifier = Modifier.height(12.dp))
                        SummaryText("Stop Loss", slTriggerPrice, entryPrice, qty.toFloatOrNull() ?: 1f, isBuy, isProfit = false)

                    }
                    "Partial Position" -> {
                        // Partial Position List design
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Sort by Order Time", color = Color(0xFF787B86), fontSize = 14.sp)
                                    Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
                                }
                                Row {
                                    Button(onClick = { partialOrders.clear() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2E39)), shape = RoundedCornerShape(4.dp), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
                                        Text("Cancel All", color = Color.White, fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { 
                                            partialOrderId = null
                                            partialQty = "0.001"
                                            partialTpPrice = formatPriceValue(if (isBuy) entryPrice * 1.1f else entryPrice * 0.9f)
                                            partialSlPrice = formatPriceValue(if (isBuy) entryPrice * 0.9f else entryPrice * 1.1f)
                                            isEditingPartial = true
                                        }, 
                                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor), 
                                        shape = RoundedCornerShape(4.dp), 
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text("Add", color = Color.White, fontSize = 12.sp)
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Header for list
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Text("Take Profit", color = Color(0xFF787B86), fontSize = 11.sp, modifier = Modifier.weight(1f))
                                Text("Stop Loss", color = Color(0xFF787B86), fontSize = 11.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                            }
                            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                Text("TP Order Price", color = Color(0xFF787B86), fontSize = 11.sp, modifier = Modifier.weight(1f))
                                Text("Qty", color = Color(0xFF787B86), fontSize = 11.sp, modifier = Modifier.width(60.dp), textAlign = TextAlign.Center)
                                Text("SL Order Price", color = Color(0xFF787B86), fontSize = 11.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                            }
                            
                            Divider(color = boxBorderColor)
                            
                            LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)) {
                                items(partialOrders) { order ->
                                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            val tpPnl = order.tp?.let { if (isBuy) (it - entryPrice) * order.volume else (entryPrice - it) * order.volume }
                                            val slPnl = order.sl?.let { if (isBuy) (it - entryPrice) * order.volume else (entryPrice - it) * order.volume }

                                            Text(
                                                text = order.tp?.let { String.format("%,.1f(Last)%s", it, tpPnl?.let { pnl -> " (${if(pnl>=0) "+" else ""}${String.format("%.2f", pnl)})" } ?: "") } ?: "--",
                                                color = Color(0xFF089981),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                text = order.sl?.let { String.format("%s%,.1f(Last)", slPnl?.let { pnl -> "(${if(pnl>=0) "+" else ""}${String.format("%.2f", pnl)}) " } ?: "", it) } ?: "--",
                                                color = Color(0xFFF23645),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.End
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                            Text(order.tpOrderPrice, color = Color.White, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                            Text(order.volume.toString(), color = Color.White, fontSize = 13.sp, modifier = Modifier.width(60.dp), textAlign = TextAlign.Center)
                                            Text(order.slOrderPrice, color = Color.White, fontSize = 13.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Button(
                                                onClick = { 
                                                    partialOrderId = order.id
                                                    partialQty = order.volume.toString()
                                                    partialTpPrice = order.tp?.let { formatPriceValue(it) } ?: ""
                                                    partialSlPrice = order.sl?.let { formatPriceValue(it) } ?: ""
                                                    isEditingPartial = true
                                                }, 
                                                modifier = Modifier.weight(1f).height(32.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2E39)),
                                                shape = RoundedCornerShape(4.dp),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Text("Modify", color = Color.White, fontSize = 12.sp)
                                            }
                                            Button(
                                                onClick = { partialOrders.remove(order) }, 
                                                modifier = Modifier.weight(1f).height(32.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2E39)),
                                                shape = RoundedCornerShape(4.dp),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Text("Cancel", color = Color.White, fontSize = 12.sp)
                                            }
                                        }
                                    }
                                    Divider(color = boxBorderColor)
                                }
                            }
                        }
                    }
                }
            } else {
                // Editor view for adding or modifying a partial order
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Qty", color = Color(0xFF787B86), fontSize = 14.sp)
                    Box(modifier = Modifier.fillMaxWidth().height(48.dp).padding(vertical = 4.dp).background(Color.Black, RoundedCornerShape(4.dp)).border(1.dp, boxBorderColor, RoundedCornerShape(4.dp)).padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
                        BasicTextField(value = partialQty, onValueChange = { partialQty = it }, textStyle = TextStyle(color = Color.White, fontSize = 16.sp), modifier = Modifier.fillMaxWidth())
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Take Profit Price", color = Color(0xFF787B86), fontSize = 14.sp)
                    PriceInputRow(value = partialTpPrice, onValueChange = { partialTpPrice = it }, onIncrement = { partialTpPrice = formatPriceValue((partialTpPrice.toFloatOrNull() ?: entryPrice) + tickSize) }, onDecrement = { partialTpPrice = formatPriceValue((partialTpPrice.toFloatOrNull() ?: entryPrice) - tickSize) }, selectedSource = "Last", onSourceChange = {}, primaryColor = primaryColor, borderColor = boxBorderColor)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Stop Loss Price", color = Color(0xFF787B86), fontSize = 14.sp)
                    PriceInputRow(value = partialSlPrice, onValueChange = { partialSlPrice = it }, onIncrement = { partialSlPrice = formatPriceValue((partialSlPrice.toFloatOrNull() ?: entryPrice) + tickSize) }, onDecrement = { partialSlPrice = formatPriceValue((partialSlPrice.toFloatOrNull() ?: entryPrice) - tickSize) }, selectedSource = "Last", onSourceChange = {}, primaryColor = primaryColor, borderColor = boxBorderColor)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = {
                                val newOrder = PartialOrder(
                                    id = partialOrderId ?: java.util.UUID.randomUUID().toString(),
                                    volume = partialQty.toFloatOrNull() ?: 0.001f,
                                    tp = partialTpPrice.toFloatOrNull(),
                                    sl = partialSlPrice.toFloatOrNull()
                                )
                                val index = partialOrders.indexOfFirst { it.id == partialOrderId }
                                if (index != -1) partialOrders[index] = newOrder else partialOrders.add(newOrder)
                                isEditingPartial = false
                            },
                            modifier = Modifier.width(110.dp).height(38.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Save", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = { isEditingPartial = false },
                            modifier = Modifier.width(110.dp).height(38.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2E39)),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Back", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (!isEditingPartial && selectedTab != "Close Position") {
                Spacer(modifier = Modifier.height(32.dp))
                // Action Buttons (Positioned at middle)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = { onConfirm(tpTriggerPrice.toFloatOrNull(), slTriggerPrice.toFloatOrNull(), partialOrders.toList()) },
                        modifier = Modifier.width(110.dp).height(38.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Confirm", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.width(110.dp).height(38.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2E39)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Cancel", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color = Color.White) {
    Column {
        Text(label, color = Color(0xFF787B86), fontSize = 11.sp)
        Text(value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TabItem(label: String, isSelected: Boolean, primaryColor: Color, isNew: Boolean = false, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable { onClick() }, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = if (isSelected) Color.White else Color(0xFF787B86), fontSize = 15.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
            if (isNew) {
                Spacer(modifier = Modifier.width(4.dp))
                Text("new", color = Color(0xFFF2A52C), fontSize = 10.sp)
            }
        }
        if (isSelected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(modifier = Modifier.width(40.dp).height(2.dp).background(primaryColor))
        }
    }
}

@Composable
fun PriceInputRow(
    value: String, 
    onValueChange: (String) -> Unit, 
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    selectedSource: String,
    onSourceChange: (String) -> Unit,
    primaryColor: Color,
    borderColor: Color
) {
    var sourceDropdownExpanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth().height(48.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color.Black, RoundedCornerShape(4.dp)).border(1.dp, borderColor, RoundedCornerShape(4.dp)).padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium),
                    modifier = Modifier.weight(1f),
                    cursorBrush = SolidColor(primaryColor),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Icon(Icons.Default.MoreHoriz, null, tint = Color(0xFFF2A52C), modifier = Modifier.size(18.dp).clickable { onValueChange("") })
                Spacer(modifier = Modifier.width(12.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("—", color = Color(0xFF787B86), fontSize = 18.sp, modifier = Modifier.clickable { onDecrement() })
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.width(1.dp).height(16.dp).background(Color(0xFF2A2E39)))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("+", color = Color(0xFF787B86), fontSize = 18.sp, modifier = Modifier.clickable { onIncrement() })
                }
            }
        }
        Box(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight()
                .background(Color.Black, RoundedCornerShape(4.dp))
                .border(1.dp, borderColor, RoundedCornerShape(4.dp))
                .clickable { sourceDropdownExpanded = true }
                .padding(horizontal = 8.dp), 
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(selectedSource, color = Color.White, fontSize = 14.sp)
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
            }
            
            DropdownMenu(
                expanded = sourceDropdownExpanded,
                onDismissRequest = { sourceDropdownExpanded = false },
                modifier = Modifier.background(Color(0xFF1E222D)).border(1.dp, Color(0xFF363A45))
            ) {
                listOf("Last", "Bid", "Ask").forEach { source ->
                    DropdownMenuItem(
                        text = { Text(source, color = Color.White) },
                        onClick = {
                            onSourceChange(source)
                            sourceDropdownExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PercentageSelector(borderColor: Color, onPercentSelect: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        listOf(5, 10, 15, 20, 25).forEach { pct ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .padding(horizontal = 2.dp)
                    .background(Color.Black, RoundedCornerShape(4.dp))
                    .border(1.dp, borderColor, RoundedCornerShape(4.dp))
                    .clickable { onPercentSelect(pct) }, 
                contentAlignment = Alignment.Center
            ) {
                Text("$pct%", color = Color(0xFF787B86), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun SummaryText(type: String, targetPrice: String, entryPrice: Float, volume: Float, isBuy: Boolean, isProfit: Boolean) {
    val target = targetPrice.toFloatOrNull() ?: entryPrice
    val diff = if (isBuy) {
        if (isProfit) target - entryPrice else entryPrice - target
    } else {
        if (isProfit) entryPrice - target else target - entryPrice
    }
    val amount = diff * volume
    val roi = if (entryPrice != 0f) (diff / entryPrice) * 100f else 0f
    
    Text(
        text = "Last Traded Price to ${if (targetPrice.isEmpty()) "..." else targetPrice} will trigger market $type order; your expected ${if (isProfit) "profit" else "loss"} will be ${String.format("%.4f", abs(amount))} USDT (ROI: ${String.format("%.2f", roi)}%)",
        color = Color(0xFF787B86),
        fontSize = 12.sp,
        lineHeight = 16.sp,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
    )
}
