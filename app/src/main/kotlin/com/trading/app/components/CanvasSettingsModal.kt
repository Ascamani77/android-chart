package com.trading.app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.trading.app.models.CanvasSettings
import com.trading.app.models.ChartSettings

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

@Composable
fun CanvasSettingsModal(
    settings: ChartSettings,
    onUpdate: (ChartSettings) -> Unit,
    onClose: () -> Unit
) {
    var tempSettings by remember { mutableStateOf(settings.canvas) }
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
                            "Canvas",
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
                        SectionHeader("CHART BASIC STYLES")
                        
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Full Chart Color", color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                            CanvasDropdown(
                                value = tempSettings.fullChartColor,
                                options = listOf("Default", "Pure Black", "Dark Blue", "OLED Black"),
                                onValueChange = { tempSettings = tempSettings.copy(fullChartColor = it) },
                                modifier = Modifier.width(160.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Header Visibility", color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                            CanvasDropdown(
                                value = tempSettings.headerVisibility,
                                options = listOf("Always visible", "Auto-hide"),
                                onValueChange = { tempSettings = tempSettings.copy(headerVisibility = it) },
                                modifier = Modifier.width(160.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Background", color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                            CanvasDropdown(
                                value = tempSettings.backgroundType,
                                options = listOf("Solid", "Gradient"),
                                onValueChange = { tempSettings = tempSettings.copy(backgroundType = it) },
                                modifier = Modifier.width(160.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            ColorBox(tempSettings.background, { colorPickerTarget = ColorPickerState("Background", tempSettings.background) { tempSettings = tempSettings.copy(background = it) } })
                            if (tempSettings.backgroundType == "Gradient") {
                                Spacer(modifier = Modifier.width(8.dp))
                                ColorBox(tempSettings.backgroundGradientEnd, { colorPickerTarget = ColorPickerState("Background Gradient", tempSettings.backgroundGradientEnd) { tempSettings = tempSettings.copy(backgroundGradientEnd = it) } })
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Grid lines", color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                            CanvasDropdown(
                                value = tempSettings.gridType,
                                options = listOf("Vert and horz", "Vert", "Horz", "None"),
                                onValueChange = { tempSettings = tempSettings.copy(gridType = it) },
                                modifier = Modifier.width(160.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            ColorBox(tempSettings.gridColor, { colorPickerTarget = ColorPickerState("Vertical Grid", tempSettings.gridColor) { tempSettings = tempSettings.copy(gridColor = it) } })
                            Spacer(modifier = Modifier.width(8.dp))
                            ColorBox(tempSettings.horzGridColor, { colorPickerTarget = ColorPickerState("Horizontal Grid", tempSettings.horzGridColor) { tempSettings = tempSettings.copy(horzGridColor = it) } })
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Crosshair", color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(36.dp)
                                    .background(Color(0xFF1E222D), RoundedCornerShape(4.dp))
                                    .border(1.dp, Color(0xFF363A45), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp)
                                    .clickable { colorPickerTarget = ColorPickerState("Crosshair", tempSettings.crosshairColor) { tempSettings = tempSettings.copy(crosshairColor = it) } }
                            ) {
                                Box(modifier = Modifier.size(20.dp).clip(RoundedCornerShape(2.dp)).background(parseColor(tempSettings.crosshairColor)))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("----", color = Color(0xFF787B86), fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.weight(0.1f))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Watermark", color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                            CanvasDropdown(
                                value = tempSettings.watermarkType,
                                options = listOf("Replay mode", "Symbol", "None"),
                                onValueChange = { tempSettings = tempSettings.copy(watermarkType = it) },
                                modifier = Modifier.width(160.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            ColorBox(tempSettings.watermarkColor, { colorPickerTarget = ColorPickerState("Watermark", tempSettings.watermarkColor) { tempSettings = tempSettings.copy(watermarkColor = it) } })
                        }

                        SectionHeader("SCALES")

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Text", color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                            ColorBox(tempSettings.scaleTextColor, { colorPickerTarget = ColorPickerState("Scale Text", tempSettings.scaleTextColor) { tempSettings = tempSettings.copy(scaleTextColor = it) } })
                            Spacer(modifier = Modifier.width(8.dp))
                            CanvasDropdown(
                                value = tempSettings.scaleFontSize.toString(),
                                options = listOf("10", "11", "12", "14", "16"),
                                onValueChange = { tempSettings = tempSettings.copy(scaleFontSize = it.toInt()) },
                                modifier = Modifier.width(120.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Lines", color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                            ColorBox(tempSettings.scaleLineColor, { colorPickerTarget = ColorPickerState("Scale Lines", tempSettings.scaleLineColor) { tempSettings = tempSettings.copy(scaleLineColor = it) } })
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        SectionHeader("BUTTONS")
                        SettingsDropdownRowSimple("Navigation", tempSettings.navigationButtons) { tempSettings = tempSettings.copy(navigationButtons = it) }
                        SettingsDropdownRowSimple("Pane", tempSettings.paneButtons) { tempSettings = tempSettings.copy(paneButtons = it) }

                        SectionHeader("MARGINS")
                        MarginInputRow("Top", tempSettings.marginTop, "%") { tempSettings = tempSettings.copy(marginTop = it) }
                        MarginInputRow("Bottom", tempSettings.marginBottom, "%") { tempSettings = tempSettings.copy(marginBottom = it) }
                        MarginInputRow("Right", tempSettings.marginRight, "bars") { tempSettings = tempSettings.copy(marginRight = it) }

                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    // Footer
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp, 36.dp).border(1.dp, Color(0xFF2A2E39), RoundedCornerShape(8.dp)).clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.MoreHoriz, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = onClose,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                border = BorderStroke(1.dp, Color(0xFF2A2E39)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Cancel", color = Color.White, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = { 
                                    onUpdate(settings.copy(canvas = tempSettings))
                                    onClose()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Ok", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Color Picker Modal Overlay with Opacity Slider and Advanced Picker
                colorPickerTarget?.let { state ->
                    var selectedHex by remember { mutableStateOf(state.initialHex) }
                    var opacity by remember { mutableFloatStateOf(parseColor(state.initialHex).alpha) }
                    var showAdvancedPicker by remember { mutableStateOf(false) }

                    Dialog(
                        onDismissRequest = { colorPickerTarget = null },
                        properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        Surface(
                            modifier = Modifier.width(320.dp).padding(16.dp),
                            color = Color(0xFF1E222D),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFF363A45))
                        ) {
                            if (!showAdvancedPicker) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(state.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(10),
                                        modifier = Modifier.height(240.dp),
                                        contentPadding = PaddingValues(4.dp),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        items(TRADING_VIEW_COLORS) { hex ->
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(RoundedCornerShape(2.dp))
                                                    .background(parseColor(hex))
                                                    .border(
                                                        width = if (selectedHex.lowercase() == hex.lowercase()) 2.dp else 1.dp,
                                                        color = if (selectedHex.lowercase() == hex.lowercase()) Color(0xFF2962FF) else Color.White.copy(alpha = 0.1f),
                                                        shape = RoundedCornerShape(2.dp)
                                                    )
                                                    .clickable { 
                                                        selectedHex = hex
                                                        val finalColor = parseColor(hex).copy(alpha = opacity)
                                                        state.onColorSelect(toRgbaString(finalColor))
                                                    }
                                            )
                                        }
                                    }
                                    
                                    Divider(color = Color(0xFF363A45), modifier = Modifier.padding(vertical = 12.dp))
                                    
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { showAdvancedPicker = true }) {
                                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("Opacity", color = Color(0xFF787B86), fontSize = 12.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Slider(
                                            value = opacity,
                                            onValueChange = { 
                                                opacity = it
                                                val finalColor = parseColor(selectedHex).copy(alpha = opacity)
                                                state.onColorSelect(toRgbaString(finalColor))
                                            },
                                            modifier = Modifier.weight(1f),
                                            colors = SliderDefaults.colors(
                                                thumbColor = Color.White,
                                                activeTrackColor = Color(0xFF2962FF),
                                                inactiveTrackColor = Color(0xFF363A45)
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(50.dp)
                                                .height(32.dp)
                                                .border(1.dp, Color(0xFF363A45), RoundedCornerShape(4.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("${(opacity * 100).toInt()}%", color = Color.White, fontSize = 12.sp)
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { colorPickerTarget = null },
                                        modifier = Modifier.fillMaxWidth().height(36.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF)),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text("Done", color = Color.White, fontSize = 14.sp)
                                    }
                                }
                            } else {
                                AdvancedColorPicker(
                                    initialColor = parseColor(selectedHex),
                                    onColorChange = { color ->
                                        val rgba = toRgbaString(color.copy(alpha = opacity))
                                        state.onColorSelect(rgba)
                                    },
                                    onClose = { showAdvancedPicker = false },
                                    onAdd = { color ->
                                        val rgba = toRgbaString(color.copy(alpha = opacity))
                                        state.onColorSelect(rgba)
                                        colorPickerTarget = null
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdvancedColorPicker(
    initialColor: Color,
    onColorChange: (Color) -> Unit,
    onClose: () -> Unit,
    onAdd: (Color) -> Unit
) {
    var hsv by remember { mutableStateOf(initialColor.toHsvArray()) }
    var hue by remember { mutableFloatStateOf(hsv[0]) }
    var saturation by remember { mutableFloatStateOf(hsv[1]) }
    var value by remember { mutableFloatStateOf(hsv[2]) }
    
    val currentColor = remember(hue, saturation, value) {
        Color.hsv(hue, saturation, value)
    }

    Column(modifier = Modifier.padding(12.dp)) {
        // Header with Preview and Hex Input
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(currentColor)
                    .border(1.dp, Color(0xFF363A45), RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(32.dp)
                    .border(1.dp, Color(0xFF363A45), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = String.format("#%06X", (0xFFFFFF and currentColor.toArgbInt())),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onAdd(currentColor) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text("Add", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Saturation-Value Gradient Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(4.dp))
                .pointerInput(hue) {
                    detectDragGestures { change, _ ->
                        val x = change.position.x.coerceIn(0f, size.width.toFloat())
                        val y = change.position.y.coerceIn(0f, size.height.toFloat())
                        saturation = x / size.width
                        value = 1f - (y / size.height)
                        onColorChange(Color.hsv(hue, saturation, value))
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val saturationGradient = Brush.horizontalGradient(listOf(Color.White, Color.hsv(hue, 1f, 1f)))
                val valueGradient = Brush.verticalGradient(listOf(Color.Transparent, Color.Black))
                
                drawRect(saturationGradient)
                drawRect(valueGradient)
                
                // Selector circle
                val selectorX = saturation * size.width
                val selectorY = (1f - value) * size.height
                drawCircle(
                    color = Color.White,
                    radius = 6.dp.toPx(),
                    center = Offset(selectorX, selectorY),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hue Slider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(2.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        hue = (change.position.x.coerceIn(0f, size.width.toFloat()) / size.width) * 360f
                        onColorChange(Color.hsv(hue, saturation, value))
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val hues = (0..360 step 60).map { Color.hsv(it.toFloat(), 1f, 1f) }
                drawRect(Brush.horizontalGradient(hues))
                
                // Selector line
                val selectorX = (hue / 360f) * size.width
                drawRect(
                    color = Color.White,
                    topLeft = Offset(selectorX - 2.dp.toPx(), 0f),
                    size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height),
                    style = Stroke(width = 1.dp.toPx())
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Default.ArrowBack, null, tint = Color.White)
        }
    }
}

// Fixed helper methods
private fun Color.toArgbInt(): Int {
    return (this.alpha * 255).toInt() shl 24 or
           ((this.red * 255).toInt() shl 16) or
           ((this.green * 255).toInt() shl 8) or
           (this.blue * 255).toInt()
}

private fun Color.toHsvArray(): FloatArray {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(this.toArgbInt(), hsv)
    return hsv
}

data class ColorPickerState(
    val title: String,
    val initialHex: String,
    val onColorSelect: (String) -> Unit
)

private fun toRgbaString(color: Color): String {
    return "rgba(${(color.red * 255).toInt()},${(color.green * 255).toInt()},${(color.blue * 255).toInt()},${color.alpha})"
}

private fun parseColor(hex: String): Color {
    return try {
        if (hex.startsWith("#")) Color(android.graphics.Color.parseColor(hex))
        else if (hex.startsWith("rgba")) {
            val parts = hex.substringAfter("(").substringBefore(")").split(",")
            val r = parts[0].trim().toInt()
            val g = parts[1].trim().toInt()
            val b = parts[2].trim().toInt()
            val a = parts.getOrNull(3)?.trim()?.toFloat() ?: 1f
            Color(r, g, b).copy(alpha = a)
        } else Color.Gray
    } catch (e: Exception) {
        Color.Gray
    }
}

@Composable
fun MarginInputRow(label: String, value: Int, unit: String, onValueChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(36.dp)
                    .border(1.dp, Color(0xFF363A45), RoundedCornerShape(4.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = value.toString(),
                    onValueChange = { 
                        it.toIntOrNull()?.let { num -> onValueChange(num) }
                    },
                    textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                    cursorBrush = SolidColor(Color.White),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(unit, color = Color(0xFF787B86), fontSize = 14.sp, modifier = Modifier.width(40.dp))
        }
    }
}

@Composable
fun SettingsDropdownRowSimple(label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        CanvasDropdown(
            value = value,
            options = listOf("Visible on tap", "Always visible", "Visible on mouse over", "Hidden"),
            onValueChange = onValueChange,
            modifier = Modifier.width(160.dp)
        )
    }
}

@Composable
fun CanvasDropdown(
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .border(1.dp, Color(0xFF363A45), RoundedCornerShape(4.dp))
                .clickable { expanded = true }
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(value, color = Color.White, fontSize = 13.sp)
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF1E222D)).border(1.dp, Color(0xFF363A45))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White, fontSize = 14.sp) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ColorBox(hex: String, onClick: () -> Unit, modifier: Modifier = Modifier.size(32.dp)) {
    val color = try {
        if (hex.startsWith("#")) Color(android.graphics.Color.parseColor(hex))
        else if (hex.startsWith("rgba")) {
            val parts = hex.substringAfter("(").substringBefore(")").split(",")
            val r = parts[0].trim().toInt()
            val g = parts[1].trim().toInt()
            val b = parts[2].trim().toInt()
            val a = parts.getOrNull(3)?.trim()?.toFloat() ?: 1f
            Color(r, g, b).copy(alpha = a)
        } else Color.Gray
    } catch (e: Exception) {
        Color.Gray
    }
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .border(1.dp, Color(0xFF2A2E39), RoundedCornerShape(4.dp))
            .clickable { onClick() }
    )
}
