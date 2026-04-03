package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trading.app.models.Position
import java.util.Locale

@Composable
fun PositionsTab(
    positions: List<Position>,
    currentPrice: Float,
    labelColor: Color = Color(0xFF787B86)
) {
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(positions) { position ->
                PositionItem(position, currentPrice)
                Divider(color = Color(0xFF2A2E39), thickness = 1.dp)
            }
        }
    }
}

@Composable
private fun PositionItem(position: Position, lastPrice: Float) {
    val labelColor = Color(0xFF787B86)
    
    // Calculations
    val isBuy = position.type == "buy"
    val pnl = (lastPrice - position.entryPrice) * position.volume * (if (isBuy) 1f else -1f)
    val pnlPercentage = (pnl / (position.entryPrice * position.volume)) * 100
    val tradeValue = position.entryPrice * position.volume
    val marketValue = lastPrice * position.volume
    val margin = tradeValue / 500.0f
    
    val pnlColor = if (pnl >= 0) Color(0xFF089981) else Color(0xFFF23645)

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
                    "EXNESS:${position.symbol.uppercase()}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.DragHandle, null, tint = Color(0xFF2A2E39), modifier = Modifier.size(18.dp))
        }
        
        Text(
            "${position.symbol.uppercase()} VS US DOLLAR",
            color = labelColor,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 4.dp, start = 0.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(start = 0.dp)) {
            PositionDetailRow("Side", if (isBuy) "Long" else "Short", if (isBuy) Color(0xFF2962FF) else Color(0xFFF23645))
            PositionDetailRow("Qty", String.format(Locale.US, "%.2f", position.volume))
            PositionDetailRow("Avg Fill Price", String.format(Locale.US, "%,.2f", position.entryPrice))
            PositionDetailRow("Take Profit", position.tp?.let { String.format(Locale.US, "%,.2f", it) } ?: "")
            PositionDetailRow("Stop Loss", position.sl?.let { String.format(Locale.US, "%,.2f", it) } ?: "")
            PositionDetailRow("Last Price", String.format(Locale.US, "%,.2f", lastPrice))
            PositionDetailRow("Unrealized P&L", String.format(Locale.US, "%s%,.2f USD", if (pnl >= 0) "" else "", pnl), pnlColor)
            PositionDetailRow("Unrealized P&L %", String.format(Locale.US, "%.2f%%", pnlPercentage), pnlColor)
            PositionDetailRow("Trade Value", String.format(Locale.US, "%,.2f USD", tradeValue))
            PositionDetailRow("Market Value", String.format(Locale.US, "%,.2f USD", marketValue))
            PositionDetailRow("Leverage", position.leverage)
            PositionDetailRow("Margin", String.format(Locale.US, "%.2f USD", position.margin))
            PositionDetailRow("Expiration Date", "—")
        }
    }
}

@Composable
private fun PositionDetailRow(label: String, value: String, valueColor: Color = Color(0xFFD1D4DC)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color(0xFF787B86),
            fontSize = 14.sp,
            modifier = Modifier.width(160.dp) // Increased width to add more space between label and value
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Start
        )
    }
}
