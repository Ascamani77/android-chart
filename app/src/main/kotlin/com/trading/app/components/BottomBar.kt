package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trading.app.models.ChartSettings
import com.trading.app.models.SymbolInfo
import java.util.Locale

@Composable
fun BottomBar(
    onRangeClick: (String) -> Unit = {},
    onGoToClick: () -> Unit = {},
    onTabClick: (String) -> Unit = {},
    activeTab: String? = null,
    recentPairs: List<Pair<String, String>> = emptyList(),
    currentSymbol: String = "",
    currentTimeframe: String = "",
    onPairSelect: (String, String) -> Unit = { _, _ -> },
    backgroundColor: Color = Color(0xFF08090C),
    settings: ChartSettings = ChartSettings(),
    currentQuote: SymbolQuote? = null // Pass the live quote down
) {
    val pairsScrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .navigationBarsPadding()
    ) {
        Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color(0xFF2A2E39))

        if (recentPairs.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.4.dp)
                    .horizontalScroll(pairsScrollState)
                    .padding(start = 1.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                recentPairs.forEachIndexed { index, (symbol, timeframe) ->
                    val isActive = symbol == currentSymbol && timeframe == currentTimeframe
                    
                    // Use live data if this is the active symbol, else use mock/saved data
                    val displayChange = if (isActive && currentQuote != null) {
                        String.format(Locale.US, "%+.2f%%", currentQuote.changePercent)
                    } else {
                        "+2.4%" // Fallback for non-active symbols in history
                    }
                    val isUp = !displayChange.startsWith("-")

                    val symbolInfo = remember(symbol) {
                        val type = when {
                            symbol.startsWith("BTC") || symbol.startsWith("ETH") || symbol.startsWith("SOL") -> "Crypto"
                            symbol.length == 6 && (symbol.contains("USD") || symbol.contains("EUR") || symbol.contains("JPY")) -> "Forex"
                            else -> "Stock"
                        }
                        SymbolInfo(ticker = symbol, name = "", type = type)
                    }

                    Box(
                        modifier = Modifier
                            .padding(start = if (index == 0) 0.dp else 4.dp, end = 4.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isActive) Color(0xFF1E222D) else Color.Transparent)
                            .border(
                                width = 1.dp,
                                color = if (isActive) Color(0xFF363A45) else Color(0xFF2A2E39),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable { onPairSelect(symbol, timeframe) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AssetIcon(symbolInfo, size = 24)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$symbol,$timeframe",
                                color = if (isActive) Color.White else Color(0xFFD1D4DC),
                                fontSize = 13.sp,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = if (isUp) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = if (isUp) Color(0xFF089981) else Color(0xFFF23645),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = displayChange,
                                color = if (isUp) Color(0xFF089981) else Color(0xFFF23645),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
            Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color(0xFF2A2E39))
        }
    }
}
