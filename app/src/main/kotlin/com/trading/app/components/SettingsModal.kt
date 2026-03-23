package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.trading.app.models.ChartSettings
import android.graphics.Color as AndroidColor

private fun safeParseColor(colorString: String?, defaultColor: Int = AndroidColor.GRAY): Int {
    if (colorString.isNullOrBlank()) return defaultColor
    return try {
        if (colorString.startsWith("rgba", ignoreCase = true)) {
            val parts = colorString.substringAfter("(").substringBefore(")").split(",")
            val r = parts[0].trim().toInt()
            val g = parts[1].trim().toInt()
            val b = parts[2].trim().toInt()
            val a = (parts.getOrNull(3)?.trim()?.toFloat() ?: 1f).let { (it * 255).toInt() }
            AndroidColor.argb(a, r, g, b)
        } else if (colorString.startsWith("rgb", ignoreCase = true)) {
            val parts = colorString.substringAfter("(").substringBefore(")").split(",")
            val r = parts[0].trim().toInt()
            val g = parts[1].trim().toInt()
            val b = parts[2].trim().toInt()
            AndroidColor.rgb(r, g, b)
        } else {
            AndroidColor.parseColor(colorString)
        }
    } catch (e: Exception) {
        defaultColor
    }
}

@Composable
fun SettingsModal(
    settings: ChartSettings,
    onUpdate: (ChartSettings) -> Unit,
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E222D))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Chart Settings", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Icon(
                        Icons.Default.Close,
                        null,
                        tint = Color.White,
                        modifier = Modifier.clickable { onClose() }
                    )
                }
                
                Divider(color = Color(0xFF363A45))
                
                // Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text("Symbol", color = Color(0xFF2962FF), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    SettingRow("Up Color", settings.symbol.upColor) { onUpdate(settings.copy(symbol = settings.symbol.copy(upColor = it))) }
                    SettingRow("Down Color", settings.symbol.downColor) { onUpdate(settings.copy(symbol = settings.symbol.copy(downColor = it))) }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Canvas", color = Color(0xFF2962FF), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    SettingRow("Background", settings.canvas.background) { onUpdate(settings.copy(canvas = settings.canvas.copy(background = it))) }
                    SettingRow("Grid Color", settings.canvas.gridColor) { onUpdate(settings.copy(canvas = settings.canvas.copy(gridColor = it))) }
                }
                
                Divider(color = Color(0xFF363A45))
                
                // Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onClose) {
                        Text("Cancel", color = Color(0xFF787B86))
                    }
                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF))
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingRow(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color(0xFFD1D4DC), fontSize = 14.sp)
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(Color(safeParseColor(value)))
                .clickable { /* Color picker would go here */ }
        )
    }
}
