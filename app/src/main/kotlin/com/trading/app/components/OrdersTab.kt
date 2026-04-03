package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trading.app.models.Order
import java.util.Locale

@Composable
fun OrdersTab(
    orders: List<Order>,
    labelColor: Color = Color(0xFF787B86)
) {
    var activeSubTab by remember { mutableStateOf("All") }
    val subTabs = listOf("All", "Working", "Inactive", "Filled", "Cancelled", "Rejected")

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            subTabs.forEach { subTab ->
                val isSelected = activeSubTab.equals(subTab, ignoreCase = true)
                val count = when (subTab) {
                    "All" -> 1
                    "Filled" -> 1
                    else -> 0
                }
                val displayText = if (count > 0) "$subTab $count" else subTab
                
                Text(
                    text = displayText,
                    color = if (isSelected) Color.White else labelColor,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0xFF2A2E39) else Color.Transparent)
                        .clickable { activeSubTab = subTab }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
        Divider(color = Color(0xFF2A2E39), thickness = 1.dp)

        // Mock order for demonstration
        val mockOrder = Order(
            symbol = "PEPPERSTONE:XAUUSD",
            type = "sell",
            orderType = "Market",
            status = "Filled",
            price = 4654.77f,
            averagePrice = 4654.77f,
            volume = 1f,
            time = 1712066912000L // 2026-04-02 17:08:32
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                OrderItemComponent(
                    order = mockOrder,
                    showStatus = activeSubTab.equals("All", ignoreCase = true)
                )
                Divider(color = Color(0xFF2A2E39), thickness = 1.dp)
            }
        }
    }
}

@Composable
private fun OrderItemComponent(order: Order, showStatus: Boolean) {
    val labelColor = Color(0xFF787B86)
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Circle, null, tint = Color(0xFFEBC147), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Color(0xFF2962FF)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                Text(text = "PEPPERSTONE:${order.symbol.uppercase().split(":").last()}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.DragHandle, null, tint = Color(0xFF2A2E39), modifier = Modifier.size(18.dp))
        }
        Text(text = "GOLD VS US DOLLAR", color = labelColor, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp, start = 32.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.padding(start = 32.dp)) {
            val isBuy = order.type.equals("buy", ignoreCase = true)
            OrderDetailRow("Side", if (isBuy) "Buy" else "Sell", if (isBuy) Color(0xFF2962FF) else Color(0xFFF23645))
            OrderDetailRow("Type", order.orderType)
            OrderDetailRow("Qty", order.volume.toInt().toString())
            OrderDetailRow("Fill Price", String.format(Locale.US, "%,.2f", order.averagePrice))
            OrderDetailRow("Take Profit", "")
            OrderDetailRow("Stop Loss", "")
            
            if (showStatus) {
                val statusColor = when (order.status.lowercase()) {
                    "filled" -> Color(0xFF089981)
                    "rejected" -> Color(0xFFF23645)
                    "working", "inactive" -> Color(0xFFEBC147)
                    else -> Color(0xFFD1D4DC)
                }
                OrderDetailRow("Status", order.status.uppercase(), statusColor)
            }

            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            OrderDetailRow("Placing Time", dateFormat.format(java.util.Date(order.time)))
            OrderDetailRow("Order ID", order.id.filter { it.isDigit() }.take(10).ifEmpty { "2934146682" })
            OrderDetailRow("Expiry", "2026-05-02 17:08:32") // Mocked for screenshot
            OrderDetailRow("Leverage", "500:1")
            OrderDetailRow("Margin", "9.31 USD")
        }
    }
}

@Composable
private fun OrderDetailRow(label: String, value: String, valueColor: Color = Color(0xFFD1D4DC)) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, color = Color(0xFF787B86), fontSize = 14.sp, modifier = Modifier.width(130.dp))
        Text(text = value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
