package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import java.util.*

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

        // Last Viewed Pane (Recent Pairs)
        if (recentPairs.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(37.dp)
                    .horizontalScroll(pairsScrollState)
                    .padding(horizontal = 1.dp),
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

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isActive) Color(0xFF1E222D) else Color.Transparent)
                            .border(
                                width = if (isActive) 1.dp else 0.dp,
                                color = if (isActive) Color(0xFF363A45) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onPairSelect(symbol, timeframe) }
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentWidth()) {
                            Box(modifier = Modifier.width(32.dp).height(20.dp), contentAlignment = Alignment.CenterStart) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(getSymbolFlagColor(symbol))
                                        .border(1.dp, Color.Black, CircleShape)
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF2A2E39))
                                        .border(1.dp, Color.Black, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(modifier = Modifier.fillMaxSize()) {
                                        Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color(0xFF002868)))
                                        Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color(0xFFBF0A30)))
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(4.dp))
                            
                            Text(
                                text = "$symbol,$timeframe",
                                color = if (isActive) Color.White else Color(0xFF787B86),
                                fontSize = fontSize,
                                fontWeight = if (isActive) FontWeight.Bold else fontWeight,
                                maxLines = 1
                            )
                            
                            Spacer(modifier = Modifier.width(4.dp))
                            
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
                                fontSize = fontSize,
                                fontWeight = fontWeight,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color(0xFF1E222D))
        }
    }
}

fun getSymbolFlagColor(symbol: String): Color {
    return when {
        symbol.contains("BTC") -> Color(0xFFF7931A)
        symbol.contains("ETH") -> Color(0xFF627EEA)
        symbol.contains("USD") -> Color(0xFF008500)
        symbol.contains("EUR") -> Color(0xFF003399)
        symbol.contains("AAPL") -> Color(0xFF555555)
        symbol.contains("TSLA") -> Color(0xFFE81123)
        symbol.contains("GOLD") -> Color(0xFFFFD700)
        symbol.contains("SOL") -> Color(0xFF00FFA3)
        else -> Color(0xFF2962FF)
    }
}
