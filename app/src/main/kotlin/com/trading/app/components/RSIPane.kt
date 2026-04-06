package com.trading.app.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun RSIPane(
    rsiValues: List<Float?>,
    rsiMaValues: List<Float?>,
    modifier: Modifier = Modifier,
    rsiPeriod: Int = 14,
    overbought: Float = 70f,
    oversold: Float = 30f
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF131722))
            .border(0.5.dp, Color(0xFF2A2E39))
    ) {
        // If no RSI data, show placeholder
        val hasData = rsiValues.any { it != null }
        android.util.Log.d("RSIPane", "RSIPane rendered: size=${rsiValues.size}, hasData=$hasData, nonNull=${rsiValues.count { it != null }}")
        if (!hasData) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No RSI data", color = Color(0xFFD1D4DC), fontSize = 12.sp)
            }
            return
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val y70 = height * (1 - overbought / 100f)
            val y30 = height * (1 - oversold / 100f)
            val y50 = height * 0.5f

            // Overbought/Oversold Background Shading
            drawRect(
                color = Color(0xFF7E57C2).copy(alpha = 0.1f),
                topLeft = Offset(0f, y70),
                size = Size(width, y30 - y70)
            )

            // Horizontal Grid Lines (Dashed effect can be added with path effect if needed)
            drawLine(Color(0xFF2A2E39), Offset(0f, y70), Offset(width, y70), strokeWidth = 1f)
            drawLine(Color(0xFF2A2E39), Offset(0f, y30), Offset(width, y30), strokeWidth = 1f)
            drawLine(Color(0xFF2A2E39).copy(alpha = 0.5f), Offset(0f, y50), Offset(width, y50), strokeWidth = 0.5f)

            // Calculate stepX based on the number of values we want to show
            // For a scrolling chart, we might only want to show a subset of values.
            // Assuming rsiValues corresponds to the visible range or is scaled.
            val stepX = if (rsiValues.size > 1) width / (rsiValues.size - 1) else width

            // Draw RSI Moving Average (Yellow Line)
            if (rsiMaValues.isNotEmpty()) {
                val maPath = Path()
                var firstPoint = true
                rsiMaValues.forEachIndexed { index, value ->
                    if (value != null) {
                        val x = index * stepX
                        val y = height * (1 - value / 100f)
                        if (firstPoint) {
                            maPath.moveTo(x, y)
                            firstPoint = false
                        } else {
                            maPath.lineTo(x, y)
                        }
                    }
                }
                drawPath(
                    path = maPath,
                    color = Color(0xFFFFD700), // Yellow/Gold
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            // Draw RSI (Purple Line)
            if (rsiValues.isNotEmpty()) {
                val rsiPath = Path()
                var firstPoint = true
                rsiValues.forEachIndexed { index, value ->
                    if (value != null) {
                        val x = index * stepX
                        val y = height * (1 - value / 100f)
                        if (firstPoint) {
                            rsiPath.moveTo(x, y)
                            firstPoint = false
                        } else {
                            rsiPath.lineTo(x, y)
                        }
                    }
                }
                drawPath(
                    path = rsiPath,
                    color = Color(0xFF7E57C2), // Purple
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
        }

        // Legends and current values (Top Left)
        val latestRsi = rsiValues.lastOrNull()
        val latestMa = rsiMaValues.lastOrNull()
        
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RSI $rsiPeriod close",
                color = Color(0xFFD1D4DC),
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            if (latestRsi != null) {
                Text(
                    text = String.format(Locale.US, "%.2f", latestRsi),
                    color = Color(0xFF7E57C2),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            if (latestMa != null) {
                Text(
                    text = String.format(Locale.US, "%.2f", latestMa),
                    color = Color(0xFFFFD700),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Y-Axis Labels (Right Side)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text("70.00", color = Color(0xFFD1D4DC).copy(alpha = 0.5f), fontSize = 10.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text("50.00", color = Color(0xFFD1D4DC).copy(alpha = 0.5f), fontSize = 10.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text("30.00", color = Color(0xFFD1D4DC).copy(alpha = 0.5f), fontSize = 10.sp)
        }
    }
}
