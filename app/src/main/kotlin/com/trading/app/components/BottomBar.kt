package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
        // MT5 Style Recent Pairs Row - Reduced height
        if (recentPairs.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color(0xFF08090C)),
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
                        Text(
                            text = "$symbol,$timeframe",
                            color = if (isActive) Color.Black else Color(0xFFD1D4DC),
                            fontSize = 11.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                        )
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
                .height(26.dp)
                .background(Color(0xFF08090C))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tabs
            val tabs = listOf("Stock Screener", "Pine Editor", "Strategy Tester", "Trading Panel")
            tabs.forEach { tab ->
                val isActive = activeTab == tab
                Text(
                    tab,
                    color = if (isActive) Color.White else Color(0xFF787B86),
                    fontSize = 11.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clickable { onTabClick(tab) }
                )
            }

            Spacer(modifier = Modifier.width(6.dp))
            Divider(modifier = Modifier.height(14.dp).width(1.dp), color = Color(0xFF2A2E39))
            Spacer(modifier = Modifier.width(6.dp))

            // Range Selection
            val ranges = listOf("1D", "5D", "1M", "3M", "6M", "YTD", "1Y", "5Y", "All")
            ranges.forEach { range ->
                Text(
                    range,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { onRangeClick(range) }
                )
            }

            Spacer(modifier = Modifier.width(6.dp))
            Divider(modifier = Modifier.height(14.dp).width(1.dp), color = Color(0xFF2A2E39))
            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                Icons.Default.DateRange,
                null,
                tint = Color.White,
                modifier = Modifier.size(12.dp).clickable { onGoToClick() }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Market Status & Time
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(marketStatusColor)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(marketStatus, color = Color(0xFF787B86), fontSize = 10.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(currentTime, color = Color.White, fontSize = 10.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    selectedTimeZone,
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.clickable { onTimeZoneClick() }
                )
            }
        }
    }
}
