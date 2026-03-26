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
    onPairSelect: (String, String) -> Unit = { _, _ -> },
    backgroundColor: Color = Color(0xFF08090C)
) {
    var currentTime by remember { mutableStateOf("") }
    var marketStatus by remember { mutableStateOf("Closed") }
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
                marketStatus = "Open"
                marketStatusColor = Color(0xFF089981)
            } else {
                marketStatus = "Closed"
                marketStatusColor = Color(0xFFF23645)
            }
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .navigationBarsPadding() // Pushes the content up to avoid overlap with system navigation
    ) {
        // Subtle top border to separate the bottom bar from the chart area
        Divider(modifier = Modifier.fillMaxWidth().height(0.5.dp), color = Color(0xFF2A2E39))

        // Last Viewed Pane (Recent Pairs) - MOVED TO TOP
        if (recentPairs.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
                    .background(backgroundColor)
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
                    Divider(
                        modifier = Modifier.fillMaxHeight().width(1.dp),
                        color = Color(0xFF2A2E39)
                    )
                }
            }

            // White separator line between last viewed pane and date pane
            Divider(modifier = Modifier.fillMaxWidth().height(2.dp), color = Color.White.copy(alpha = 0.2f))
        }

        // Date Pane (Range selection, time, market status) - NOW BELOW LAST VIEWED
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(backgroundColor)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Scrollable section for Range Selection
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .horizontalScroll(bottomScrollState),
                verticalAlignment = Alignment.CenterVertically
            ) {
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

            // Fixed section
            Spacer(modifier = Modifier.width(12.dp))
            Divider(modifier = Modifier.height(20.dp).width(1.dp), color = Color(0xFF2A2E39))
            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                Icons.Default.DateRange,
                null,
                tint = Color.White,
                modifier = Modifier.size(18.dp).clickable { onGoToClick() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Market Status & Time
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(marketStatusColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(marketStatus, color = Color(0xFF787B86), fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(currentTime, color = Color.White, fontSize = 14.sp)
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
        symbol.contains("BTC") -> Color(0xFFF7931A)
        symbol.contains("ETH") -> Color(0xFF627EEA)
        symbol.contains("USD") -> Color(0xFF008500)
        symbol.contains("EUR") -> Color(0xFF003399)
        symbol.contains("AAPL") -> Color(0xFF555555)
        symbol.contains("TSLA") -> Color(0xFFE81123)
        symbol.contains("GOLD") -> Color(0xFFFFD700)
        else -> Color(0xFF2962FF)
    }
}
