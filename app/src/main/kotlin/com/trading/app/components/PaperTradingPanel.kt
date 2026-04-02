package com.trading.app.components

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trading.app.models.Position
import java.util.Locale

@Composable
fun PaperTradingPanel(
    onClose: () -> Unit,
    positions: List<Position> = emptyList(),
    currentPrice: Float = 0f,
    balance: Double = 102789.72,
    backgroundColor: Color = Color(0xFF08090C)
) {
    var activeTab by remember { mutableStateOf("Positions") }
    val tabs = listOf("Positions", "Orders", "Order History", "Balance History")
    val labelColor = Color(0xFF787B86)
    
    val horizontalMargin = 16.dp

    // Calculations
    val totalUnrealizedPnl = positions.sumOf { 
        ((currentPrice - it.entryPrice) * it.volume * (if (it.type == "buy") 1f else -1f)).toDouble()
    }
    
    val equity = balance + totalUnrealizedPnl
    
    // Simple margin calculation: 1% of total trade value
    val totalMargin = positions.sumOf { (it.entryPrice * it.volume * 0.01f).toDouble() }
    val availableFunds = equity - totalMargin
    val marginBuffer = if (equity > 0) (availableFunds / equity) * 100 else 100.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Settings, null, tint = labelColor, modifier = Modifier.size(24.dp))
            }
            Text("|", color = Color(0xFF2A2E39), modifier = Modifier.padding(horizontal = 4.dp), fontSize = 20.sp)
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, null, tint = labelColor, modifier = Modifier.size(28.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Account Stats Grid
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                AccountStatItem("Account balance", String.format(Locale.US, "%,.2f", balance), Modifier.weight(1f))
                AccountStatItem("Equity", String.format(Locale.US, "%,.2f", equity), Modifier.weight(1f))
                AccountStatItem("Realized P&L", "+0.00", Modifier.weight(1f), Color(0xFF089981))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                val unrealizedColor = if (totalUnrealizedPnl >= 0) Color(0xFF089981) else Color(0xFFF23645)
                val sign = if (totalUnrealizedPnl >= 0) "+" else ""
                AccountStatItem(
                    "Unrealized P&L", 
                    String.format(Locale.US, "%s%,.2f", sign, totalUnrealizedPnl), 
                    Modifier.weight(1f), 
                    unrealizedColor
                )
                AccountStatItem("Account margin", String.format(Locale.US, "%,.2f", totalMargin), Modifier.weight(1f), showInfo = true)
                AccountStatItem("Available funds", String.format(Locale.US, "%,.2f", availableFunds), Modifier.weight(1f), showInfo = true)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                AccountStatItem("Orders margin", "0.00", Modifier.weight(1f), showInfo = true)
                AccountStatItem("Margin buffer", String.format(Locale.US, "%.2f%%", marginBuffer), Modifier.weight(1f), showInfo = true)
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navbar: 4 items, Evenly spaced (SpaceBetween), 14sp font
        Column(modifier = Modifier.padding(horizontal = horizontalMargin)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tabs.forEach { tab ->
                    val isSelected = activeTab == tab
                    val countText = if (tab == "Positions" && positions.isNotEmpty()) " ${positions.size}" else ""
                    val text = "$tab$countText"
                    
                    Column(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { activeTab = tab },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = text,
                            color = if (isSelected) Color.White else labelColor,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp),
                            softWrap = false,
                            maxLines = 1,
                            textAlign = TextAlign.Center
                        )
                        // Indicator line
                        Box(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(if (isSelected) Color.White else Color.Transparent)
                        )
                    }
                }
            }
            Divider(color = Color(0xFF2A2E39), thickness = 2.dp) // Thicker divider
        }

        // Content
        Box(modifier = Modifier.fillMaxSize()) {
            if (activeTab == "Positions") {
                if (positions.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "There are no open positions in your trading account yet",
                            color = labelColor,
                            fontSize = 14.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 40.dp)
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(positions) { position ->
                            PositionItem(position, currentPrice)
                            Divider(color = Color(0xFF2A2E39), thickness = 1.dp)
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No data available for $activeTab", color = labelColor)
                }
            }
        }
    }
}

@Composable
fun AccountStatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color? = null,
    showInfo: Boolean = false
) {
    val labelColor = Color(0xFF787B86)
    val defaultValColor = Color(0xFFD1D4DC)
    
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                label, 
                color = labelColor, 
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            if (showInfo) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Outlined.Info,
                    null,
                    tint = labelColor,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Text(
            value,
            color = valueColor ?: defaultValColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun PositionItem(position: Position, currentPrice: Float) {
    val labelColor = Color(0xFF787B86)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF2962FF))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    "PEPPERSTONE:${position.symbol.uppercase()}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.MoreVert, null, tint = labelColor, modifier = Modifier.size(18.dp))
        }
        
        Text(
            "GOLD VS US DOLLAR",
            color = labelColor,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        PositionDetailRow("Side", if (position.type == "buy") "Long" else "Short", if (position.type == "buy") Color(0xFF2962FF) else Color(0xFFF23645))
        PositionDetailRow("Qty", position.volume.toString())
        PositionDetailRow("Avg Fill Price", String.format(Locale.US, "%,.2f", position.entryPrice))
        PositionDetailRow("Take Profit", position.tp?.let { String.format(Locale.US, "%,.2f", it) } ?: "—")
        PositionDetailRow("Stop Loss", position.sl?.let { String.format(Locale.US, "%,.2f", it) } ?: "—")
        PositionDetailRow("Last Price", String.format(Locale.US, "%,.2f", currentPrice))
        
        val pnl = (currentPrice - position.entryPrice) * position.volume * (if (position.type == "buy") 1f else -1f)
        val pnlColor = if (pnl >= 0) Color(0xFF089981) else Color(0xFFF23645)
        
        PositionDetailRow("Unrealized P&L", String.format(Locale.US, "%+.2f USD", pnl), pnlColor)
        PositionDetailRow("Unrealized P&L %", String.format(Locale.US, "%+.2f%%", (pnl / (position.entryPrice * position.volume)) * 100), pnlColor)
        
        PositionDetailRow("Trade Value", String.format(Locale.US, "%,.2f USD", position.entryPrice * position.volume))
        PositionDetailRow("Market Value", String.format(Locale.US, "%,.2f USD", currentPrice * position.volume))
        PositionDetailRow("Leverage", "500:1")
        PositionDetailRow("Margin", String.format(Locale.US, "%,.2f USD", position.entryPrice * position.volume * 0.01f))
        PositionDetailRow("Expiration Date", "—")
    }
}

@Composable
fun PositionDetailRow(label: String, value: String, valueColor: Color = Color(0xFFD1D4DC)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFF787B86), fontSize = 13.sp)
        Text(value, color = valueColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}
