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
    backgroundColor: Color = Color(0xFF08090C),
    settings: ChartSettings = ChartSettings()
) {
    var currentTime by remember { mutableStateOf("") }
    var marketStatus by remember { mutableStateOf("Market Closed") }
    var marketStatusColor by remember { mutableStateOf(Color(0xFFF23645)) }
    var countdownText by remember { mutableStateOf("") }

    val bottomScrollState = rememberScrollState()
    val pairsScrollState = rememberScrollState()
    
    val fontSize = settings.canvas.bottomFontSize.sp
    val fontWeight = if (settings.canvas.bottomFontBold) FontWeight.Bold else FontWeight.Medium

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
            val isMarketOpen = day in Calendar.MONDAY..Calendar.FRIDAY && hour in 9..16
            
            if (isMarketOpen) {
                marketStatus = "Market Open"
                marketStatusColor = Color(0xFF089981)
                // Calculate time until market closes at 16:00 (4 PM)
                val closingTime = Calendar.getInstance(tz).apply {
                    set(Calendar.HOUR_OF_DAY, 16)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }
                val timeUntilClose = closingTime.timeInMillis - now.timeInMillis
                val hoursLeft = (timeUntilClose / (1000 * 60 * 60))
                val minutesLeft = ((timeUntilClose % (1000 * 60 * 60)) / (1000 * 60))
                val secondsLeft = ((timeUntilClose % (1000 * 60)) / 1000)
                countdownText = "Closes in ${hoursLeft}h ${minutesLeft}m ${secondsLeft}s"
            } else {
                marketStatus = "Market Closed"
                marketStatusColor = Color(0xFFF23645)
                // Calculate time until market opens at 9:00 (9 AM)
                val nextOpenTime = Calendar.getInstance(tz).apply {
                    add(Calendar.DAY_OF_YEAR, 1)
                    set(Calendar.HOUR_OF_DAY, 9)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }
                // If it's a weekend or after hours on weekday, check if tomorrow is a weekday
                if (day == Calendar.FRIDAY && hour >= 16) {
                    nextOpenTime.add(Calendar.DAY_OF_YEAR, 2) // Skip to Monday
                } else if (day == Calendar.SATURDAY) {
                    nextOpenTime.add(Calendar.DAY_OF_YEAR, 1) // Skip to Monday
                }
                val timeUntilOpen = nextOpenTime.timeInMillis - now.timeInMillis
                val hoursLeft = (timeUntilOpen / (1000 * 60 * 60))
                val minutesLeft = ((timeUntilOpen % (1000 * 60 * 60)) / (1000 * 60))
                val secondsLeft = ((timeUntilOpen % (1000 * 60)) / 1000)
                countdownText = "Opens in ${hoursLeft}h ${minutesLeft}m ${secondsLeft}s"
            }
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black) // Matches the pure black background in the image
            .navigationBarsPadding()
    ) {
        // Subtle top border
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
                    
                    // Mock price change data for UI parity with the request image
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
                            // Overlapping Icons
                            Box(modifier = Modifier.width(32.dp).height(20.dp), contentAlignment = Alignment.CenterStart) {
                                // Base icon (Coin)
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(getSymbolFlagColor(symbol))
                                        .border(1.dp, Color.Black, CircleShape)
                                )
                                // Overlapping Flag icon
                                Box(
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF2A2E39))
                                        .border(1.dp, Color.Black, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Mini flag pattern representation
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

            // Separator between recent pairs and date range
            Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color(0xFF1E222D))
        }

        // Date Pane
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onGoToClick() }
            ) {
                Text("GoTo", color = Color(0xFF787B86), fontSize = fontSize)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    Icons.Default.DateRange,
                    null,
                    tint = Color(0xFF787B86),
                    modifier = Modifier.size(21.6.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(currentTime, color = Color.White, fontSize = fontSize)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    selectedTimeZone,
                    color = Color.White,
                    fontSize = fontSize,
                    modifier = Modifier.clickable { onTimeZoneClick() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(marketStatusColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    countdownText,
                    color = marketStatusColor,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Medium
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
        symbol.contains("SOL") -> Color(0xFF00FFA3)
        else -> Color(0xFF2962FF)
    }
}
