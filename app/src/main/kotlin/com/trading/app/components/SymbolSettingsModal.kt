package com.trading.app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.trading.app.models.ChartSettings
import com.trading.app.models.ColorPickerState
import com.trading.app.models.SymbolSettings

private val TRADING_VIEW_COLORS = listOf(
    "#ffffff", "#d1d4dc", "#b2b5be", "#868993", "#5d606b", "#434651", "#363a45", "#2a2e39", "#131722", "#000000",
    "#ff5252", "#ff9800", "#ffeb3b", "#4caf50", "#00bcd4", "#2196f3", "#3f51b5", "#9c27b0", "#e91e63", "#795548",
    "#ffebee", "#fff3e0", "#fffde7", "#e8f5e9", "#e0f7fa", "#e3f2fd", "#e8eaf6", "#f3e5f5", "#fce4ec", "#efebe9",
    "#ffcdd2", "#ffe0b2", "#fff9c4", "#c8e6c9", "#b2ebf2", "#bbdefb", "#c5cae9", "#e1bee7", "#f8bbd0", "#d7ccc8",
    "#ef9a9a", "#ffcc80", "#fff59d", "#a5d6a7", "#80deea", "#90caf9", "#9fa8da", "#ce93d8", "#f48fb1", "#bcaaa4",
    "#e57373", "#ffb74d", "#fff176", "#81c784", "#4dd0e1", "#64b5f6", "#7986cb", "#ba68c8", "#f06292", "#a1887f",
    "#ef5350", "#ffa726", "#ffee58", "#66bb6a", "#26c6da", "#42a5f5", "#5c6bc0", "#ab47bc", "#ec407a", "#8d6e63",
    "#f44336", "#fb8c00", "#fdd835", "#43a047", "#00acc1", "#1e88e5", "#3949ab", "#8e24aa", "#d81b60", "#6d4c41",
    "#d32f2f", "#f57c00", "#fbc02d", "#388e3c", "#0097a7", "#1976d2", "#303f9f", "#7b1fa2", "#c2185b", "#5d4037",
    "#c62828", "#ef6c00", "#f9a825", "#2e7d32", "#00838f", "#1565c0", "#283593", "#6a1b9a", "#ad1457", "#4e342e",
    "#b71c1c", "#e65100", "#f57f17", "#1b5e20", "#006064", "#0d47a1", "#1a237e", "#4a148c", "#880e4f", "#3e2723"
)

private fun parseColor(colorString: String?, defaultColor: Color = Color.Gray): Color {
    if (colorString.isNullOrBlank()) return defaultColor
    return try {
        if (colorString.startsWith("rgba", ignoreCase = true)) {
            val parts = colorString.substringAfter("(").substringBefore(")").split(",")
            val r = parts[0].trim().toInt() / 255f
            val g = parts[1].trim().toInt() / 255f
            val b = parts[2].trim().toInt() / 255f
            val a = parts.getOrNull(3)?.trim()?.toFloat() ?: 1f
            Color(r, g, b, a)
        } else if (colorString.startsWith("#")) {
            Color(android.graphics.Color.parseColor(colorString))
        } else {
            Color.Gray
        }
    } catch (e: Exception) {
        defaultColor
    }
}

@Composable
fun SymbolSettingsModal(
    settings: ChartSettings,
    onUpdate: (ChartSettings) -> Unit,
    onClose: () -> Unit
) {
    var tempSettings by remember { mutableStateOf(settings.symbol) }
    var colorPickerTarget by remember { mutableStateOf<ColorPickerState?>(null) }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                    Text(
                        "Symbol",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, null, tint = Color(0xFF787B86))
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        "CANDLES",
                        color = Color(0xFF787B86),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    // Color bars based on previous close
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { tempSettings = tempSettings.copy(barColorer = !tempSettings.barColorer) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = tempSettings.barColorer,
                            onCheckedChange = { tempSettings = tempSettings.copy(barColorer = it) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.White,
                                uncheckedColor = Color(0xFF434651),
                                checkmarkColor = Color.Black
                            )
                        )
                        Text(
                            "Color bars based on previous close",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    // Body
                    SettingsToggleWithColors(
                        label = "Body",
                        checked = tempSettings.bodyVisible,
                        onCheckedChange = { tempSettings = tempSettings.copy(bodyVisible = it) },
                        colorUp = tempSettings.upColor,
                        colorDown = tempSettings.downColor,
                        onColorUpClick = { colorPickerTarget = ColorPickerState("Up Color", tempSettings.upColor) { tempSettings = tempSettings.copy(upColor = it) } },
                        onColorDownClick = { colorPickerTarget = ColorPickerState("Down Color", tempSettings.downColor) { tempSettings = tempSettings.copy(downColor = it) } }
                    )

                    // Borders
                    SettingsToggleWithColors(
                        label = "Borders",
                        checked = tempSettings.borderVisible,
                        onCheckedChange = { tempSettings = tempSettings.copy(borderVisible = it) },
                        colorUp = tempSettings.borderColorUp,
                        colorDown = tempSettings.borderColorDown,
                        onColorUpClick = { colorPickerTarget = ColorPickerState("Border Up Color", tempSettings.borderColorUp) { tempSettings = tempSettings.copy(borderColorUp = it) } },
                        onColorDownClick = { colorPickerTarget = ColorPickerState("Border Down Color", tempSettings.borderColorDown) { tempSettings = tempSettings.copy(borderColorDown = it) } }
                    )

                    // Wick
                    SettingsToggleWithColors(
                        label = "Wick",
                        checked = tempSettings.wickVisible,
                        onCheckedChange = { tempSettings = tempSettings.copy(wickVisible = it) },
                        colorUp = tempSettings.wickColorUp,
                        colorDown = tempSettings.wickColorDown,
                        onColorUpClick = { colorPickerTarget = ColorPickerState("Wick Up Color", tempSettings.wickColorUp) { tempSettings = tempSettings.copy(wickColorUp = it) } },
                        onColorDownClick = { colorPickerTarget = ColorPickerState("Wick Down Color", tempSettings.wickColorDown) { tempSettings = tempSettings.copy(wickColorDown = it) } }
                    )

                    Text(
                        "OHLC",
                        color = Color(0xFF787B86),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                    )

                    // Open
                    SimpleCheckbox(
                        label = "Open",
                        checked = tempSettings.openVisible,
                        onCheckedChange = { tempSettings = tempSettings.copy(openVisible = it) }
                    )

                    // High
                    SimpleCheckbox(
                        label = "High",
                        checked = tempSettings.highVisible,
                        onCheckedChange = { tempSettings = tempSettings.copy(highVisible = it) }
                    )

                    // Low
                    SimpleCheckbox(
                        label = "Low",
                        checked = tempSettings.lowVisible,
                        onCheckedChange = { tempSettings = tempSettings.copy(lowVisible = it) }
                    )

                    // Close
                    SimpleCheckbox(
                        label = "Close",
                        checked = tempSettings.closeVisible,
                        onCheckedChange = { tempSettings = tempSettings.copy(closeVisible = it) }
                    )

                    Text(
                        "DATA MODIFICATION",
                        color = Color(0xFF787B86),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                    )

                    // Precision
                    SettingsDropdown(
                        label = "Precision",
                        value = tempSettings.precision
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Timezone
                    SettingsDropdown(
                        label = "Timezone",
                        value = tempSettings.timezone
                    )
                }

                // Footer
                Surface(
                    color = Color(0xFF131722),
                    border = BorderStroke(1.dp, Color(0xFF2A2E39))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF1E222D), RoundedCornerShape(4.dp))
                                .border(1.dp, Color(0xFF434651), RoundedCornerShape(4.dp))
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MoreHoriz, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        OutlinedButton(
                            onClick = onClose,
                            modifier = Modifier.height(36.dp),
                            border = BorderStroke(1.dp, Color(0xFF434651)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Cancel", fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = { 
                                try {
                                    val updatedSettings = settings.copy(symbol = tempSettings)
                                    onUpdate(updatedSettings)
                                    onClose()
                                } catch (e: Exception) {
                                    android.util.Log.e("SymbolSettings", "Failed to apply settings changes", e)
                                    onClose()
                                }
                            },
                            modifier = Modifier.height(36.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Ok", color = Color.Black, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }

    colorPickerTarget?.let { state ->
        ColorPickerDialogSymbol(
            state = state,
            onClose = { colorPickerTarget = null }
        )
    }
}
}

@Composable
fun SettingsToggleWithColors(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    colorUp: String,
    colorDown: String,
    onColorUpClick: () -> Unit,
    onColorDownClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                uncheckedColor = Color(0xFF434651),
                checkmarkColor = Color.Black
            )
        )
        Text(
            label,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.width(80.dp)
        )
        
        ColorBox(colorUp, onColorUpClick)
        Spacer(modifier = Modifier.width(4.dp))
        ColorBox(colorDown, onColorDownClick)
    }
}

@Composable
fun SimpleCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                uncheckedColor = Color(0xFF434651),
                checkmarkColor = Color.Black
            )
        )
        Text(
            label,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ColorBox(hex: String, onClick: () -> Unit) {
    val color = parseColor(hex)

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
            .clickable { onClick() }
    )
}

@Composable
fun SettingsDropdown(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White, fontSize = 14.sp, modifier = Modifier.width(100.dp))
        
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(40.dp)
                .background(Color(0xFF1E222D), RoundedCornerShape(4.dp))
                .border(1.dp, Color(0xFF434651), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(value, color = Color.White, fontSize = 13.sp)
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun ColorPickerDialogSymbol(
    state: ColorPickerState,
    onClose: () -> Unit
) {
    var currentHex by remember { mutableStateOf(state.initialHex) }
    var opacity by remember { mutableStateOf(100f) }

    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color(0xFF1E222D),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF363A45))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(state.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(10),
                    modifier = Modifier.height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(TRADING_VIEW_COLORS) { colorHex ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(android.graphics.Color.parseColor(colorHex)))
                                .border(
                                    width = if (currentHex.lowercase() == colorHex.lowercase()) 2.dp else 0.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(2.dp)
                                )
                                .clickable {
                                    currentHex = colorHex
                                    state.onColorSelect(currentHex)
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Opacity", color = Color(0xFF787B86), fontSize = 12.sp)
                Slider(
                    value = opacity,
                    onValueChange = { opacity = it },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color(0xFF2962FF),
                        inactiveTrackColor = Color(0xFF363A45)
                    )
                )

                // Buttons
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            state.onColorSelect(state.initialHex)
                            onClose()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF2A2E39)),
                        border = BorderStroke(1.dp, Color(0xFF363A45))
                    ) {
                        Text("Cancel", color = Color(0xFFD1D4DC), fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            try {
                                val alpha = (opacity / 100f * 255f).toInt().coerceIn(0, 255)
                                val alphaHex = String.format("%02X", alpha)
                                val base = currentHex.removePrefix("#")
                                val rgb = if (base.length >= 6) base.takeLast(6) else base.padStart(6, '0')
                                val finalHex = "#${alphaHex}${rgb}"
                                state.onColorSelect(finalHex)
                            } catch (e: Exception) {
                                state.onColorSelect(currentHex)
                            }
                            onClose()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF))
                    ) {
                        Text("Done", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
