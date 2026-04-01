package com.trading.app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trading.app.models.Position
import java.util.*
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderModal(
    symbol: String,
    bidPrice: Float,
    askPrice: Float,
    onClose: () -> Unit,
    onPlaceOrder: (Position) -> Unit,
    onTradingSettingsClick: () -> Unit
) {
    var selectedType by remember { mutableStateOf("buy") }
    var selectedTab by remember { mutableStateOf("Market") }
    
    // Inputs
    var unitsInput by remember { mutableStateOf("1") }
    var limitPriceInput by remember { mutableStateOf("") }
    var tpEnabled by remember { mutableStateOf(false) }
    var tpPriceInput by remember { mutableStateOf("") }
    var slEnabled by remember { mutableStateOf(false) }
    var slPriceInput by remember { mutableStateOf("") }
    
    // Default values logic - Runs when entering a tab or changing side
    LaunchedEffect(selectedTab, selectedType, symbol) {
        val currentMarketPrice = if (selectedType == "buy") askPrice else bidPrice
        if (limitPriceInput.isEmpty()) {
            limitPriceInput = formatPriceValue(currentMarketPrice, symbol).replace(",", "")
        }
        if (tpPriceInput.isEmpty()) {
            val tpOffset = if (selectedType == "buy") 1.05f else 0.95f
            tpPriceInput = formatPriceValue(currentMarketPrice * tpOffset, symbol).replace(",", "")
        }
        if (slPriceInput.isEmpty()) {
            val slOffset = if (selectedType == "buy") 0.95f else 1.05f
            slPriceInput = formatPriceValue(currentMarketPrice * slOffset, symbol).replace(",", "")
        }
    }

    // Modes
    var unitsMode by remember { mutableStateOf("Units") }
    var secondaryUnitsMode by remember { mutableStateOf("Margin USD") }
    var tpMode by remember { mutableStateOf("Price") }
    var secondaryTpMode by remember { mutableStateOf("Ticks") }
    var slMode by remember { mutableStateOf("Price") }
    var secondarySlMode by remember { mutableStateOf("Ticks") }
    var timeInForce by remember { mutableStateOf("Week") }
    
    // UI State
    var showModeSelector by remember { mutableStateOf<String?>(null) }
    var showPresets by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }
    var slEnablesQuantityInRisk by remember { mutableStateOf(false) }
    var showExitLevelsModal by remember { mutableStateOf(false) }

    // Constants & Derived Data
    val tickSize = when {
        symbol.uppercase().contains("BTC") -> 1f
        symbol.length == 6 || symbol.contains("/") -> 0.00001f
        else -> 0.01f
    }
    val leverage = 500f
    val balance = 102721.68f // Simulated balance

    // Current Values
    val marketPrice = if (selectedType == "buy") askPrice else bidPrice
    val limitPrice = limitPriceInput.replace(",", "").toFloatOrNull() ?: marketPrice
    val entryPrice = if (selectedTab == "Market") marketPrice else limitPrice
    val units = unitsInput.toFloatOrNull() ?: 0f
    
    val tpPrice = tpPriceInput.replace(",", "").toFloatOrNull() ?: entryPrice
    val slPrice = slPriceInput.replace(",", "").toFloatOrNull() ?: entryPrice
    
    // Calculations
    val tradeValue = units * entryPrice
    val margin = if (leverage > 0) tradeValue / leverage else 0f
    val tpTicks = abs(tpPrice - entryPrice) / tickSize
    val slTicks = abs(slPrice - entryPrice) / tickSize
    
    val riskAmount = units * abs(entryPrice - slPrice)
    val riskPercent = if (balance > 0) (riskAmount / balance) * 100f else 0f
    val tpProfitAmount = units * abs(tpPrice - entryPrice)
    val tpProfitPercent = if (balance > 0) (tpProfitAmount / balance) * 100f else 0f

    // Relative Label (e.g. Ask - 21)
    val priceDiffTicks = ((limitPrice - askPrice) / tickSize).toInt()
    val priceSign = if (priceDiffTicks >= 0) "+" else "-"
    val priceLabel = "Ask $priceSign ${abs(priceDiffTicks)}"

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF787B86), width = 40.dp, height = 4.dp) },
        containerColor = Color.Black,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = Color(0xFF2A2E39),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onClose) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }
                    Text(symbol, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f).padding(start = 8.dp))
                    IconButton(onClick = { showPresets = !showPresets }) { Icon(Icons.Default.GridView, null, tint = if (showPresets) Color.White else Color(0xFF787B86)) }
                    IconButton(onClick = { showMoreMenu = !showMoreMenu }) { Icon(Icons.Default.MoreHoriz, null, tint = if (showMoreMenu) Color.White else Color(0xFF787B86)) }
                }

                // Buy/Sell buttons
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).height(46.dp).background(Color(0xFF1E222D), RoundedCornerShape(10.dp))) {
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().background(if (selectedType == "sell") Color(0xFFF23645) else Color(0xFF2A2E39), RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)).clickable { selectedType = "sell" }.padding(horizontal = 12.dp), contentAlignment = Alignment.CenterStart) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text("Sell", color = Color.White, fontSize = 11.sp)
                            Text(formatPriceValue(bidPrice, symbol), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Box(modifier = Modifier.width(60.dp).fillMaxHeight().background(Color(0xFF1E222D)), contentAlignment = Alignment.Center) {
                        Text(String.format("%.2f", (askPrice - bidPrice) / tickSize * (if (tickSize < 0.01) 0.1 else 1.0)), color = Color(0xFF787B86), fontSize = 11.sp)
                    }
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().background(if (selectedType == "buy") Color(0xFF2962FF) else Color(0xFF2A2E39), RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)).clickable { selectedType = "buy" }.padding(horizontal = 12.dp), contentAlignment = Alignment.CenterEnd) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Buy", color = Color.White, fontSize = 11.sp)
                            Text(formatPriceValue(askPrice, symbol), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Tabs
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("Market", "Limit", "Stop").forEach { tab ->
                        Column(modifier = Modifier.weight(1f).clickable { selectedTab = tab }, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(tab, color = if (selectedTab == tab) Color.White else Color(0xFF787B86), fontSize = 14.sp, fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal, modifier = Modifier.padding(vertical = 8.dp))
                            if (selectedTab == tab) Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.White))
                        }
                    }
                }

                // Content
                Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
                    if (selectedTab != "Market") {
                        Text("Price", color = Color(0xFF787B86), fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(1.dp, if (selectedType == "sell") Color(0xFFF23645).copy(alpha = 0.4f) else Color(0xFF2A2E39), RoundedCornerShape(4.dp)).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            BasicTextField(value = limitPriceInput, onValueChange = { limitPriceInput = it }, textStyle = TextStyle(color = Color.White, fontSize = 16.sp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), cursorBrush = SolidColor(Color.White), modifier = Modifier.weight(1f))
                            Icon(Icons.Default.SwapHoriz, null, tint = Color(0xFF787B86), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp)); Text(priceLabel, color = Color(0xFF787B86), fontSize = 14.sp)
                        }
                    }

                    // Units
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Row(modifier = Modifier.clickable { showModeSelector = "Units" }, verticalAlignment = Alignment.CenterVertically) {
                            Text(unitsMode, color = Color(0xFF787B86), fontSize = 14.sp)
                            Icon(if (showModeSelector == "Units") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(1.dp, Color(0xFF2A2E39), RoundedCornerShape(4.dp)).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        BasicTextField(value = unitsInput, onValueChange = { unitsInput = it }, textStyle = TextStyle(color = Color.White, fontSize = 16.sp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), cursorBrush = SolidColor(Color.White), modifier = Modifier.weight(1f))
                        Icon(Icons.Default.SwapHoriz, null, tint = Color(0xFF787B86), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(modifier = Modifier.clickable { showModeSelector = "SecondaryUnits" }, verticalAlignment = Alignment.CenterVertically) {
                            Text(String.format("%.2f USD", margin), color = Color(0xFF787B86), fontSize = 14.sp)
                            Icon(if (showModeSelector == "SecondaryUnits") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
                        }
                    }

                    // Exits Section
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Exits", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        val rr = if (slTicks > 0) String.format("%.2f", tpTicks / slTicks) else "0.00"
                        Text("• Risk/Reward $rr", color = Color(0xFF787B86), fontSize = 12.sp)
                        Spacer(modifier = Modifier.weight(1f)); Icon(Icons.Default.KeyboardArrowUp, null, tint = Color(0xFF787B86))
                    }

                    // Take Profit
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Row(modifier = Modifier.weight(1f).clickable { showModeSelector = "TP" }, verticalAlignment = Alignment.CenterVertically) {
                            Text("Take profit, ${tpMode.lowercase()}", color = if (tpEnabled) Color.White else Color(0xFF787B86), fontSize = 14.sp)
                            Icon(if (showModeSelector == "TP") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
                        }
                        Switch(checked = tpEnabled, onCheckedChange = { tpEnabled = it }, modifier = Modifier.scale(0.7f), colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF2962FF), uncheckedThumbColor = Color(0xFF787B86), uncheckedTrackColor = Color(0xFF2A2E39)))
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(1.dp, if (tpEnabled) Color(0xFF2A2E39) else Color(0xFF2A2E39).copy(alpha = 0.5f), RoundedCornerShape(4.dp)).background(if (tpEnabled) Color.Transparent else Color(0xFF131722).copy(alpha = 0.5f), RoundedCornerShape(4.dp)).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        BasicTextField(value = tpPriceInput, onValueChange = { tpPriceInput = it }, enabled = tpEnabled, textStyle = TextStyle(color = if (tpEnabled) Color.White else Color(0xFF434651), fontSize = 16.sp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), cursorBrush = SolidColor(Color.White), modifier = Modifier.weight(1f))
                        Icon(Icons.Default.SwapHoriz, null, tint = if (tpEnabled) Color(0xFF787B86) else Color(0xFF434651), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(modifier = Modifier.clickable { if (tpEnabled) showModeSelector = "SecondaryTP" }, verticalAlignment = Alignment.CenterVertically) {
                            val secondaryValue = when(secondaryTpMode) {
                                "Ticks" -> String.format("%.0f ticks", tpTicks)
                                "% price" -> String.format("%.2f%%", (abs(tpPrice - entryPrice)/entryPrice)*100)
                                "Risk, USD" -> String.format("%.2f USD", tpProfitAmount)
                                "Risk, % balance" -> String.format("%.2f%%", tpProfitPercent)
                                else -> String.format("%.0f ticks", tpTicks)
                            }
                            Text(secondaryValue, color = if (tpEnabled) Color(0xFF787B86) else Color(0xFF434651), fontSize = 14.sp)
                            Icon(if (showModeSelector == "SecondaryTP") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = if (tpEnabled) Color(0xFF787B86) else Color(0xFF434651), modifier = Modifier.size(16.dp))
                        }
                    }

                    // Stop Loss
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Row(modifier = Modifier.weight(1f).clickable { showModeSelector = "SL" }, verticalAlignment = Alignment.CenterVertically) {
                            Text("Stop loss, ${slMode.lowercase()}", color = if (slEnabled) Color.White else Color(0xFF787B86), fontSize = 14.sp)
                            Icon(if (showModeSelector == "SL") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
                        }
                        Switch(checked = slEnabled, onCheckedChange = { slEnabled = it }, modifier = Modifier.scale(0.7f), colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFF23645), uncheckedThumbColor = Color(0xFF787B86), uncheckedTrackColor = Color(0xFF2A2E39)))
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).border(1.dp, if (slEnabled) Color(0xFF2A2E39) else Color(0xFF2A2E39).copy(alpha = 0.5f), RoundedCornerShape(4.dp)).background(if (slEnabled) Color.Transparent else Color(0xFF131722).copy(alpha = 0.5f), RoundedCornerShape(4.dp)).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        BasicTextField(value = slPriceInput, onValueChange = { slPriceInput = it }, enabled = slEnabled, textStyle = TextStyle(color = if (slEnabled) Color.White else Color(0xFF434651), fontSize = 16.sp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), cursorBrush = SolidColor(Color.White), modifier = Modifier.weight(1f))
                        Icon(Icons.Default.SwapHoriz, null, tint = if (slEnabled) Color(0xFF787B86) else Color(0xFF434651), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(modifier = Modifier.clickable { if (slEnabled) showModeSelector = "SecondarySL" }, verticalAlignment = Alignment.CenterVertically) {
                            val secondaryValue = when(secondarySlMode) {
                                "Ticks" -> String.format("%.0f ticks", slTicks)
                                "% price" -> String.format("%.2f%%", (abs(slPrice - entryPrice)/entryPrice)*100)
                                "Risk, USD" -> String.format("%.2f USD", riskAmount)
                                "Risk, % balance" -> String.format("%.2f%%", riskPercent)
                                else -> String.format("%.0f ticks", slTicks)
                            }
                            Text(secondaryValue, color = if (slEnabled) Color(0xFF787B86) else Color(0xFF434651), fontSize = 14.sp)
                            Icon(if (showModeSelector == "SecondarySL") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = if (slEnabled) Color(0xFF787B86) else Color(0xFF434651), modifier = Modifier.size(16.dp))
                        }
                    }

                    Text("+ Add level", color = Color(0xFF2962FF), fontSize = 14.sp, modifier = Modifier.padding(vertical = 12.dp).clickable { showExitLevelsModal = true })

                    if (selectedTab != "Market") {
                        Text("Extra settings", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                        Text("Time in force", color = Color(0xFF787B86), fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).border(1.dp, Color(0xFF2A2E39), RoundedCornerShape(4.dp)).clickable { showModeSelector = "TIF" }.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(timeInForce, color = Color.White, fontSize = 16.sp, modifier = Modifier.weight(1f))
                            Icon(if (showModeSelector == "TIF") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(20.dp))
                        }
                    }

                    // Order Info
                    Text("Order info", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Margin", color = Color.White, fontSize = 14.sp); Icon(Icons.Default.HelpOutline, null, tint = Color(0xFF787B86), modifier = Modifier.size(14.dp).padding(start = 4.dp))
                        Spacer(modifier = Modifier.weight(1f)); Text(String.format("%.2f / %,.2f", margin, balance), color = Color.White, fontSize = 14.sp)
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).height(6.dp).clip(RoundedCornerShape(50)).background(Color(0xFF2A2E39)), contentAlignment = Alignment.CenterStart) {
                        Box(modifier = Modifier.fillMaxWidth((margin / balance).coerceIn(0f, 1f)).fillMaxHeight().background(Color(0xFF787B86)))
                    }
                    InfoRow("Leverage", "500:1")
                    InfoRow("Tick value", String.format("%.2f USD", tickSize))
                    InfoRow("Trade value", String.format("%,.2f USD", tradeValue))
                    InfoRow("Risk", String.format("%.2f%% / %,.2f USD", riskPercent, riskAmount))
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // Bottom Buttons
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(top = 12.dp, bottom = 32.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = onClose, modifier = Modifier.weight(1f).height(44.dp), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)), colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent, contentColor = Color.White), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(0.dp)) { Text("Discard", fontSize = 14.sp, fontWeight = FontWeight.Medium) }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { onPlaceOrder(Position(symbol = symbol, type = selectedType, entryPrice = entryPrice, volume = units, time = System.currentTimeMillis(), tp = if (tpEnabled) tpPrice else null, sl = if (slEnabled) slPrice else null)); onClose() }, modifier = Modifier.weight(1f).height(44.dp), colors = ButtonDefaults.buttonColors(containerColor = if (selectedType == "buy") Color(0xFF2962FF) else Color(0xFFF23645)), shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(0.dp)) {
                        Text(if (selectedTab == "Market") (if (selectedType == "buy") "Buy" else "Sell") else "Place Order", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Selector Overlays (Units, SecondaryUnits, TP, SL, TIF)
            if (showModeSelector != null) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable { showModeSelector = null }) {
                    Surface(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 32.dp), color = Color(0xFF1E222D), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            val currentMode = when(showModeSelector) { "Units" -> unitsMode; "SecondaryUnits" -> secondaryUnitsMode; "TP" -> tpMode; "SecondaryTP" -> secondaryTpMode; "SL" -> slMode; "SecondarySL" -> secondarySlMode; else -> timeInForce }
                            val options = when(showModeSelector) {
                                "Units" -> listOf(Triple("Units", "", false), Triple("Margin USD", "", true), Triple("% balance", "", true), Triple("Risk, USD", "", true), Triple("Risk, % balance", "", true))
                                "SecondaryUnits" -> listOf(Triple("Margin USD", String.format("%.2f", margin), false), Triple("% balance", String.format("%.2f", (margin/balance)*100), false), Triple("Risk, USD", String.format("%.2f", riskAmount), true), Triple("Risk, % balance", String.format("%.2f", riskPercent), true))
                                "TIF" -> listOf(Triple("Day", "", false), Triple("Week", "", false), Triple("Month", "", false), Triple("GTD", "", false))
                                "SecondaryTP" -> listOf(Triple("Ticks", String.format("%,.0f", tpTicks), false), Triple("% price", String.format("%.2f%%", (abs(tpPrice - entryPrice)/entryPrice)*100), false), Triple("Risk, USD", String.format("%,.2f", tpProfitAmount), false), Triple("Risk, % balance", String.format("%.2f", tpProfitPercent), false))
                                "SecondarySL" -> listOf(Triple("Ticks", String.format("%,.0f", slTicks), false), Triple("% price", String.format("%.2f%%", (abs(slPrice - entryPrice)/entryPrice)*100), false), Triple("Risk, USD", String.format("%,.2f", riskAmount), false), Triple("Risk, % balance", String.format("%.2f", riskPercent), false))
                                else -> listOf(Triple("Price", "", false), Triple("Ticks", "", false), Triple("% price", "", true), Triple("Risk, USD", "", true), Triple("Risk, % balance", "", true))
                            }
                            options.forEach { (label, value, hasInfo) ->
                                val isSelected = label == currentMode
                                Row(modifier = Modifier.fillMaxWidth().background(if (isSelected) Color.White else Color.Transparent).clickable { 
                                    when(showModeSelector) { "Units" -> unitsMode = label; "SecondaryUnits" -> secondaryUnitsMode = label; "TP" -> tpMode = label; "SecondaryTP" -> secondaryTpMode = label; "SL" -> slMode = label; "SecondarySL" -> secondarySlMode = label; "TIF" -> timeInForce = label }
                                    showModeSelector = null 
                                }.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                                        Text(label, color = if (isSelected) Color.Black else Color.White, fontSize = 16.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                        if (hasInfo) { Spacer(modifier = Modifier.width(4.dp)); Icon(Icons.Outlined.Info, null, tint = if (isSelected) Color.Black else Color(0xFF787B86), modifier = Modifier.size(18.dp)) }
                                    }
                                    if (value.isNotEmpty()) Text(value, color = if (isSelected) Color.Black else Color(0xFF787B86), fontSize = 16.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                }
                            }
                        }
                    }
                }
            }
            
            // Presets and More menu...
            if (showPresets) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable { showPresets = false }) {
                    Surface(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 32.dp), color = Color(0xFF1E222D), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) {
                        Column(modifier = Modifier.padding(vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.RocketLaunch, null, tint = Color(0xFF787B86), modifier = Modifier.size(64.dp))
                                Icon(Icons.Outlined.Edit, null, tint = Color(0xFF787B86), modifier = Modifier.size(20.dp).offset(y = 12.dp))
                            }
                            Spacer(modifier = Modifier.height(16.dp)); Text("No order presets created yet", color = Color(0xFF787B86), fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(24.dp)); Divider(color = Color(0xFF2A2E39), modifier = Modifier.fillMaxWidth())
                            Row(modifier = Modifier.fillMaxWidth().clickable { }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.CloudUpload, null, tint = Color.White, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp)); Text("Save order preset...", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
            if (showMoreMenu) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable { showMoreMenu = false }) {
                    Surface(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 32.dp), color = Color(0xFF1E222D), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth().clickable { showMoreMenu = false; onTradingSettingsClick() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Settings, null, tint = Color.White, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp)); Text("Trading settings...", color = Color.White, fontSize = 16.sp)
                            }
                            Divider(color = Color(0xFF2A2E39), modifier = Modifier.fillMaxWidth())
                            Row(modifier = Modifier.fillMaxWidth().clickable { slEnablesQuantityInRisk = !slEnablesQuantityInRisk }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = slEnablesQuantityInRisk, onCheckedChange = { slEnablesQuantityInRisk = it }, colors = CheckboxDefaults.colors(checkedColor = Color.White, uncheckedColor = Color(0xFF434651), checkmarkColor = Color.Black))
                                Spacer(modifier = Modifier.width(12.dp)); Text("SL enables quantity in risk", color = Color.White, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (showExitLevelsModal) {
        ExitLevelsModal(symbol = symbol, orderType = if (selectedTab == "Market") (if (selectedType == "buy") "Buy" else "Sell") else selectedTab, entryPrice = entryPrice, initialUnits = unitsInput, initialTp = tpPriceInput, initialSl = slPriceInput, onClose = { showExitLevelsModal = false }, onConfirm = { showExitLevelsModal = false })
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Color.White, fontSize = 14.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text(value, color = Color.White, fontSize = 14.sp)
    }
}

private fun formatPriceValue(price: Float, symbol: String = ""): String {
    val uppercaseSymbol = symbol.uppercase()
    val isBitcoin = uppercaseSymbol.contains("BTC")
    val isForex = uppercaseSymbol.length == 6 || uppercaseSymbol.contains("/")
    val pattern = when {
        isBitcoin -> "%,.0f"
        isForex -> "%,.5f"
        else -> "%,.2f"
    }
    return String.format(pattern, price)
}
