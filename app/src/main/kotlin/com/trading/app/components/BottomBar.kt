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
    settings: ChartSettings = ChartSettings()
) {
    val bottomScrollState = rememberScrollState()
    val pairsScrollState = rememberScrollState()
    
    val fontSize = settings.canvas.bottomFontSize.sp
    val fontWeight = if (settings.canvas.bottomFontBold) FontWeight.Bold else FontWeight.Medium

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .navigationBarsPadding()
    ) {
        Divider(modifier = Modifier.fillMaxWidth().height(0.5.dp), color = Color(0xFF1E222D))

        // Last Viewed Pane (Recent Pairs) - Marked Yellow in Screenshot
        if (recentPairs.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .horizontalScroll(pairsScrollState)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                recentPairs.forEach { (symbol, timeframe) ->
                    val isActive = symbol == currentSymbol && timeframe == currentTimeframe
                    
                    val (changeText, isUp) = when {
                        symbol.contains("BTC") -> "+2.4%" to true
                        symbol.contains("ETH") -> "-1.8%" to false
                        symbol.contains("SOL") -> "+3.1%" to true
                        else -> "+0.5%" to true
                    }

                    // Determine symbol info for correct flag/logo
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
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isActive) Color(0xFF1E222D) else Color(0xFF131722))
                            .border(
                                width = 1.dp,
                                color = if (isActive) Color(0xFF363A45) else Color(0xFF1E222D),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable { onPairSelect(symbol, timeframe) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Use the shared AssetIcon component
                            AssetIcon(symbolInfo, size = 20)
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = "$symbol,$timeframe",
                                color = if (isActive) Color.White else Color(0xFFD1D4DC),
                                fontSize = 12.sp,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1
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
                                text = changeText,
                                color = if (isUp) Color(0xFF089981) else Color(0xFFF23645),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Divider(modifier = Modifier.fillMaxWidth().height(0.5.dp), color = Color(0xFF1E222D))
        }
    }
}
