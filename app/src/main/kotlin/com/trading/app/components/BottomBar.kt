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
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay

@Composable
fun BottomBar(
    onRangeClick: (String) -> Unit,
    onGoToClick: () -> Unit,
    onTimeZoneClick: () -> Unit,
    selectedTimeZone: String,
    onTabClick: (String) -> Unit,
    activeTab: String?,
    recentPairs: List<Pair<String, String>> = emptyList(),
    currentSymbol: String = "",
    currentTimeframe: String = "",
    onPairSelect: (String, String) -> Unit = { _, _ -> }
) {
    var currentTime by remember { mutableStateOf("") }
    var marketStatus by remember { mutableStateOf("Market Closed") }
    var marketStatusColor by remember { mutableStateOf(Color(0xFFF23645)) }
    
    val bottomScrollState = rememberScrollState()
    val pairsScrollState = rememberScrollState()

    LaunchedEffect(selectedTimeZone) {
        while (true) {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.US)
            val tzId = if (selectedTimeZone.contains(")")) {
                selectedTimeZone.substringAfter(") ").trim()
            } else {
                selectedTimeZone
            }
            val tz = TimeZone.getTimeZone(tzId)
            sdf.timeZone = tz
            val now = Calendar.getInstance(tz)
            currentTime = sdf.format(now.time)

            val hour = now.get(Calendar.HOUR_OF_DAY)
            val day = now.get(Calendar.DAY_OF_WEEK)
            if (day in Calendar.MONDAY..Calendar.FRIDAY && hour in 9..16) {
                marketStatus = "Market Open"
                marketStatusColor = Color(0xFF089981)
            } else {
                marketStatus = "Market Closed"
                marketStatusColor = Color(0xFFF23645)
            }
            delay(1000)
        }
    }

    Column(modifier = Modifier.fillMaxWidth().background(Color(0xFF08090C))) {
        // MT5 Style Recent Pairs Row - Scrollable
        if (recentPairs.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color(0xFF08090C))
                    .horizontalScroll(pairsScrollState),
                verticalAlignment = Alignment.CenterVertically
            ) {
                recentPairs.forEach { (symbol, timeframe) ->
                    val isActive = symbol == currentSymbol && timeframe == currentTimeframe
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(if (isActive) Color.White else Color.Transparent)
                            .border(
                                width = if (isActive) 1.dp else 0.dp,
                                color = if (isActive) Color.Black else Color.Transparent
                            )
                            .clickable { onPairSelect(symbol, timeframe) }
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Flag Icon
                            Box(
                                modifier = Modifier
                                    .size(14.dp, 10.dp)
                                    .background(getSymbolFlagColor(symbol), RoundedCornerShape(1.dp))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$symbol,$timeframe",
                                color = if (isActive) Color.Black else Color(0xFFD1D4DC),
                                fontSize = 11.sp,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                    // Divider between pair tabs
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp),
                        color = Color(0xFF2A2E39)
                    )
                }
            }
        }

        // Very Dull Separator Line
        Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color.White.copy(alpha = 0.1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp) 
                .background(Color(0xFF08090C))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Scrollable section for Range Selection
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(bottomScrollState),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Range Selection
                val ranges = listOf("1D", "5D", "1M", "3M", "6M", "YTD", "1Y", "5Y", "All")
                ranges.forEach { range ->
                    Text(
                        range,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .clickable { onRangeClick(range) }
                    )
                }
            }

            // Fixed section (Not scrollable)
            Spacer(modifier = Modifier.width(12.dp))
            Divider(modifier = Modifier.height(20.dp).width(1.dp), color = Color(0xFF2A2E39))
            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                Icons.Default.DateRange,
                null,
                tint = Color.White,
                modifier = Modifier.size(18.dp).clickable { onGoToClick() }
            )
            
            Spacer(modifier = Modifier.width(16.dp))

            // Fixed Market Status & Time
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(marketStatusColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(marketStatus, color = Color(0xFF787B86), fontSize = 14.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(currentTime, color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    selectedTimeZone,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onTimeZoneClick() }
                )
            }
        }
    }
}

fun getSymbolFlagColor(symbol: String): Color {
    return when {
        symbol.contains("BTC") -> Color(0xFFF7931A) // Bitcoin Orange
        symbol.contains("ETH") -> Color(0xFF627EEA) // Ethereum Blue
        symbol.contains("USD") -> Color(0xFF008500) // USD Green
        symbol.contains("EUR") -> Color(0xFF003399) // EUR Blue
        symbol.contains("AAPL") -> Color(0xFF555555) // Apple Grey
        symbol.contains("TSLA") -> Color(0xFFE81123) // Tesla Red
        symbol.contains("GOLD") -> Color(0xFFFFD700) // Gold
        else -> Color(0xFF2962FF)
    }
}
