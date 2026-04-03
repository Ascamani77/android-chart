package com.trading.app.components

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trading.app.models.Position
import com.trading.app.models.Order
import com.trading.app.data.Mt5Service
import java.util.Locale

@Composable
fun PaperTradingPanel(
    onClose: () -> Unit,
    positions: List<Position> = emptyList(),
    orders: List<Order> = emptyList(),
    currentPrice: Float = 0f,
    balance: Double = 102789.72,
    accountInfo: Mt5Service.AccountInfo? = null,
    backgroundColor: Color = Color(0xFF08090C)
) {
    var activeTab by remember { mutableStateOf("Positions") }
    val tabs = listOf("Positions", "Orders", "Order History", "Balance History", "Trading Journal")
    
    val labelColor = Color(0xFF787B86)
    val horizontalMargin = 16.dp

    // Calculations for Header Stats
    val totalUnrealizedPnl = accountInfo?.unrealizedPnl ?: positions.sumOf { 
        ((currentPrice - it.entryPrice) * it.volume * (if (it.type == "buy") 1f else -1f)).toDouble()
    }
    val displayBalance = accountInfo?.balance ?: balance
    val equity = accountInfo?.equity ?: (displayBalance + totalUnrealizedPnl)
    val totalMargin = accountInfo?.margin ?: positions.sumOf { (it.entryPrice * it.volume * 0.01f).toDouble() }
    val availableFunds = accountInfo?.availableFunds ?: (equity - totalMargin)
    val marginBuffer = accountInfo?.marginBuffer ?: (if (equity > 0) (availableFunds / equity) * 100 else 100.0)
    val realizedPnl = accountInfo?.realizedPnl ?: 0.0
    val ordersMargin = accountInfo?.ordersMargin ?: 0.0

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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.clickable { }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Paper Trading", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("nelsonekomwenrenren USD", color = labelColor, fontSize = 12.sp)
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = labelColor, modifier = Modifier.size(14.dp))
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
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
                AccountStatItem("Account balance", String.format(Locale.US, "%,.2f", displayBalance), Modifier.weight(1f))
                AccountStatItem("Equity", String.format(Locale.US, "%,.2f", equity), Modifier.weight(1f))
                val realizedColor = if (realizedPnl >= 0) Color(0xFF089981) else Color(0xFFF23645)
                val realizedSign = if (realizedPnl >= 0) "+" else ""
                AccountStatItem(
                    "Realized P&L", 
                    String.format(Locale.US, "%s%,.2f", realizedSign, realizedPnl), 
                    Modifier.weight(1f), 
                    realizedColor
                )
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
                AccountStatItem("Account margin", String.format(Locale.US, "%.2f", totalMargin), Modifier.weight(1f), showInfo = true)
                AccountStatItem("Free margin", String.format(Locale.US, "%,.2f", availableFunds), Modifier.weight(1f), showInfo = true)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                AccountStatItem("Orders margin", String.format(Locale.US, "%.2f", ordersMargin), Modifier.weight(1f), showInfo = true)
                AccountStatItem("Margin level", String.format(Locale.US, "%.2f%%", marginBuffer), Modifier.weight(1f), showInfo = true)
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navbar: Items
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = horizontalMargin),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                tabs.forEach { tab ->
                    val isSelected = activeTab == tab
                    val count = when (tab) {
                        "Positions" -> if (positions.isNotEmpty()) positions.size else null
                        "Orders" -> {
                            val activeOrdersCount = orders.count { it.status.lowercase() == "working" || it.status.lowercase() == "inactive" }
                            if (activeOrdersCount > 0) activeOrdersCount else null
                        }
                        "Order History" -> if (orders.isNotEmpty()) orders.size else null
                        else -> null
                    }
                    
                    Column(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { 
                                activeTab = tab 
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = tab,
                                color = if (isSelected) Color.White else labelColor,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            if (count != null) {
                                Text(
                                    text = " $count",
                                    color = labelColor,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                        // Indicator line
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(Color.White)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }
                }
            }
            Divider(modifier = Modifier.padding(horizontal = horizontalMargin), color = Color(0xFF2A2E39), thickness = 2.dp)
        }

        // Independent Tab Content
        Box(modifier = Modifier.fillMaxSize()) {
            when (activeTab) {
                "Positions" -> PositionsTab(positions, currentPrice)
                "Orders" -> OrdersTab(orders)
                "Order History" -> OrderHistoryTab(orders)
                "Balance History" -> BalanceHistoryTab()
                "Trading Journal" -> TradingJournalTab()
                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No data available for $activeTab", color = labelColor)
                    }
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
