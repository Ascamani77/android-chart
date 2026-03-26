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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.trading.app.models.ChartSettings
import com.trading.app.models.ColorPickerState

@Composable
fun CanvasSettingsModal(
    settings: ChartSettings,
    onUpdate: (ChartSettings) -> Unit,
    onClose: () -> Unit
) {
    var tempSettings by remember { mutableStateOf(settings.canvas) }
    var colorPickerTarget by remember { mutableStateOf<ColorPickerState?>(null) }

    // Apply changes in real-time to the chart
    LaunchedEffect(tempSettings) {
        onUpdate(settings.copy(canvas = tempSettings))
    }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
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
                    // CHART BASIC STYLES
                    CanvasSectionHeader("CHART BASIC STYLES")
                    
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Background", color = Color.White, fontSize = 14.sp, modifier = Modifier.width(100.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CanvasDropdown(
                                value = tempSettings.backgroundType,
                                options = listOf("Solid", "Gradient"),
                                onValueChange = { tempSettings = tempSettings.copy(backgroundType = it) },
                                modifier = Modifier.width(130.dp)
                            )
                            CanvasColorBox(tempSettings.background) {
                                colorPickerTarget = ColorPickerState("Background Color", tempSettings.background) { 
                                    tempSettings = tempSettings.copy(background = it) 
                                }
                            }
                            if (tempSettings.backgroundType == "Gradient") {
                                CanvasColorBox(tempSettings.backgroundGradientEnd) {
                                    colorPickerTarget = ColorPickerState("Gradient End Color", tempSettings.backgroundGradientEnd) { 
                                        tempSettings = tempSettings.copy(backgroundGradientEnd = it) 
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Grid lines", color = Color.White, fontSize = 14.sp, modifier = Modifier.width(100.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CanvasDropdown(
                                value = tempSettings.gridType,
                                options = listOf("Vert and horz", "Vert", "Horz", "None"),
                                onValueChange = { tempSettings = tempSettings.copy(gridType = it) },
                                modifier = Modifier.width(130.dp)
                            )
                            CanvasColorBox(tempSettings.gridColor) {
                                colorPickerTarget = ColorPickerState("Grid Color", tempSettings.gridColor) { 
                                    tempSettings = tempSettings.copy(gridColor = it) 
                                }
                            }
                            CanvasColorBox(tempSettings.horzGridColor) {
                                colorPickerTarget = ColorPickerState("Grid Color", tempSettings.horzGridColor) { 
                                    tempSettings = tempSettings.copy(horzGridColor = it) 
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Grid Opacity", color = Color.White, fontSize = 14.sp, modifier = Modifier.width(100.dp))
                        Slider(
                            value = tempSettings.gridOpacity.toFloat(),
                            onValueChange = { tempSettings = tempSettings.copy(gridOpacity = it.toInt()) },
                            valueRange = 0f..100f,
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = Color(0xFF2962FF),
                                inactiveTrackColor = Color(0xFF363A45)
                            )
                        )
                        Text("${tempSettings.gridOpacity}%", color = Color.White, fontSize = 12.sp, modifier = Modifier.width(40.dp).padding(start = 8.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Crosshair", color = Color.White, fontSize = 14.sp, modifier = Modifier.width(100.dp))
                        CrosshairPreviewBox(
                            color = tempSettings.crosshairColor,
                            thickness = tempSettings.crosshairThickness,
                            style = tempSettings.crosshairLineStyle,
                            onClick = {
                                colorPickerTarget = ColorPickerState(
                                    title = "Crosshair",
                                    initialHex = tempSettings.crosshairColor,
                                    isCrosshair = true,
                                    initialThickness = tempSettings.crosshairThickness,
                                    initialLineStyle = getLineStyleIndex(tempSettings.crosshairLineStyle),
                                    onCrosshairUpdate = { color, thickness, styleIndex ->
                                        tempSettings = tempSettings.copy(
                                            crosshairColor = color,
                                            crosshairThickness = thickness,
                                            crosshairLineStyle = getLineStyleText(styleIndex)
                                        )
                                    },
                                    onColorSelect = { color ->
                                        tempSettings = tempSettings.copy(crosshairColor = color)
                                    }
                                )
                            }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Watermark", color = Color.White, fontSize = 14.sp, modifier = Modifier.width(100.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CanvasDropdown(
                                value = tempSettings.watermarkType,
                                options = listOf("Symbol", "Replay mode", "None"),
                                onValueChange = { tempSettings = tempSettings.copy(watermarkType = it) },
                                modifier = Modifier.width(130.dp)
                            )
                            CanvasColorBox(tempSettings.watermarkColor) {
                                colorPickerTarget = ColorPickerState("Watermark Color", tempSettings.watermarkColor) { 
                                    tempSettings = tempSettings.copy(watermarkColor = it) 
                                }
                            }
                        }
                    }

                    // SCALES
                    CanvasSectionHeader("SCALES")
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Text", color = Color.White, fontSize = 14.sp, modifier = Modifier.width(100.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CanvasColorBox(tempSettings.scaleTextColor) {
                                colorPickerTarget = ColorPickerState("Scale Text Color", tempSettings.scaleTextColor) { 
                                    tempSettings = tempSettings.copy(scaleTextColor = it) 
                                }
                            }
                            CanvasDropdown(
                                value = "${tempSettings.scaleFontSize}",
                                options = listOf("8", "10", "11", "12", "14", "16", "20"),
                                onValueChange = { tempSettings = tempSettings.copy(scaleFontSize = it.toIntOrNull() ?: 11) },
                                modifier = Modifier.width(80.dp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Lines", color = Color.White, fontSize = 14.sp, modifier = Modifier.width(100.dp))
                        CanvasColorBox(tempSettings.scaleLineColor) {
                            colorPickerTarget = ColorPickerState("Scale Line Color", tempSettings.scaleLineColor) { 
                                tempSettings = tempSettings.copy(scaleLineColor = it) 
                                }
                        }
                    }

                    // BUTTONS
                    CanvasSectionHeader("BUTTONS")
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Navigation", color = Color.White, fontSize = 14.sp, modifier = Modifier.width(100.dp))
                        CanvasDropdown(
                            value = tempSettings.navigationButtons,
                            options = listOf("Always visible", "Visible on tap", "Always invisible"),
                            onValueChange = { tempSettings = tempSettings.copy(navigationButtons = it) },
                            modifier = Modifier.width(160.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Pane", color = Color.White, fontSize = 14.sp, modifier = Modifier.width(100.dp))
                        CanvasDropdown(
                            value = tempSettings.paneButtons,
                            options = listOf("Always visible", "Visible on tap", "Always invisible"),
                            onValueChange = { tempSettings = tempSettings.copy(paneButtons = it) },
                            modifier = Modifier.width(160.dp)
                        )
                    }

                    // MARGINS
                    CanvasSectionHeader("MARGINS")
                    
                    MarginInputRow("Top", tempSettings.marginTop) { tempSettings = tempSettings.copy(marginTop = it) }
                    MarginInputRow("Bottom", tempSettings.marginBottom) { tempSettings = tempSettings.copy(marginBottom = it) }
                    MarginInputRow("Right", tempSettings.marginRight) { tempSettings = tempSettings.copy(marginRight = it) }

                    Spacer(modifier = Modifier.height(32.dp))
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
                                .size(44.dp, 36.dp)
                                .border(1.dp, Color(0xFF2A2E39), RoundedCornerShape(8.dp))
                                .clickable { },
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
            }
        }
    }

    colorPickerTarget?.let { state ->
        ColorPickerDialog(
            state = state,
            onClose = { colorPickerTarget = null }
        )
    }
}

@Composable
private fun MarginInputRow(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.White, fontSize = 14.sp, modifier = Modifier.width(100.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BasicTextField(
                value = "$value",
                onValueChange = { 
                    val newVal = it.filter { char -> char.isDigit() }.toIntOrNull() ?: 0
                    onValueChange(newVal)
                },
                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                modifier = Modifier
                    .width(60.dp)
                    .height(36.dp)
                    .background(Color(0xFF1E222D), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFF434651), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                cursorBrush = SolidColor(Color.White),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text("%", color = Color(0xFF787B86), fontSize = 14.sp)
        }
    }
}

@Composable
private fun CanvasSectionHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFF787B86),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
    )
}

@Composable
private fun CanvasDropdown(
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
                .background(Color(0xFF1E222D), RoundedCornerShape(4.dp))
                .border(1.dp, Color(0xFF434651), RoundedCornerShape(4.dp))
                .clickable { expanded = true }
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(value, color = Color.White, fontSize = 13.sp, maxLines = 1)
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
            }
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF1E222D)).border(1.dp, Color(0xFF434651))
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
private fun CanvasColorBox(hex: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(parseCanvasColor(hex))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
            .clickable { onClick() }
    )
}

@Composable
private fun CrosshairPreviewBox(color: String, thickness: Int, style: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .height(32.dp)
            .background(Color(0xFF1E222D), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF434651), RoundedCornerShape(4.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Color box
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(parseCanvasColor(color))
            )
            // Line preview
            androidx.compose.foundation.Canvas(modifier = Modifier.weight(1f).height(1.dp)) {
                val effect = when (style) {
                    "Dashed" -> PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                    "Dotted" -> PathEffect.dashPathEffect(floatArrayOf(2f, 4f), 0f)
                    else -> null
                }
                drawLine(
                    color = Color.White,
                    start = Offset(0f, 0.5f),
                    end = Offset(size.width, 0.5f),
                    strokeWidth = thickness.toFloat(),
                    pathEffect = effect
                )
            }
        }
    }
}

@Composable
internal fun ColorPickerDialog(
    state: ColorPickerState,
    onClose: () -> Unit
) {
    var currentHex by remember { mutableStateOf(state.initialHex) }
    var currentThickness by remember { mutableStateOf(state.initialThickness) }
    var currentLineStyle by remember { mutableStateOf(state.initialLineStyle) }
    var opacity by remember { mutableFloatStateOf(100f) }

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
                                    if (state.isCrosshair) {
                                        state.onCrosshairUpdate?.invoke(currentHex, currentThickness, currentLineStyle)
                                    } else {
                                        state.onColorSelect(currentHex)
                                    }
                                }
                        )
                    }
                }

                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0xFF363A45),
                    thickness = 1.dp
                )

                if (state.onAddClick != null) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(1.dp, Color(0xFF363A45), RoundedCornerShape(8.dp))
                            .clickable { state.onAddClick.invoke() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add color",
                            tint = Color.White
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

                if (state.isCrosshair) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Thickness", color = Color(0xFF787B86), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf(1, 2, 3, 4).forEach { t ->
                            val selected = currentThickness == t
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (selected) Color.White else Color(0xFF2A2E39))
                                    .border(1.dp, Color(0xFF363A45), RoundedCornerShape(6.dp))
                                    .clickable {
                                        currentThickness = t
                                        state.onCrosshairUpdate?.invoke(currentHex, currentThickness, currentLineStyle)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                    val centerY = size.height / 2f
                                    val lineColor = if (selected) Color.Black else Color.White
                                    drawLine(
                                        color = lineColor,
                                        start = Offset(16.dp.toPx(), centerY),
                                        end = Offset(size.width - 16.dp.toPx(), centerY),
                                        strokeWidth = t.dp.toPx()
                                    )
                                }
                            }
                        }
                    }

                    // Line style (only for crosshair)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Line style", color = Color(0xFF787B86), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf(0, 1, 2).forEach { style ->
                            val selected = currentLineStyle == style
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (selected) Color.White else Color(0xFF2A2E39))
                                    .border(1.dp, Color(0xFF363A45), RoundedCornerShape(6.dp))
                                    .clickable {
                                        currentLineStyle = style
                                        state.onCrosshairUpdate?.invoke(currentHex, currentThickness, currentLineStyle)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                    val centerY = size.height / 2f
                                    val lineColor = if (selected) Color.Black else Color.White
                                    val stroke = 2.2f.dp.toPx()
                                    when (style) {
                                        0 -> drawLine(lineColor, Offset(16.dp.toPx(), centerY), Offset(size.width - 16.dp.toPx(), centerY), strokeWidth = stroke)
                                        1 -> drawLine(lineColor, Offset(16.dp.toPx(), centerY), Offset(size.width - 16.dp.toPx(), centerY), strokeWidth = stroke, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f))
                                        2 -> drawLine(lineColor, Offset(16.dp.toPx(), centerY), Offset(size.width - 16.dp.toPx(), centerY), strokeWidth = stroke, pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 6f), 0f))
                                    }
                                }
                            }
                        }
                    }
                }

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
                                if (state.isCrosshair) {
                                    state.onCrosshairUpdate?.invoke(finalHex, currentThickness, currentLineStyle)
                                } else {
                                    state.onColorSelect(finalHex)
                                }
                            } catch (e: Exception) {
                                if (state.isCrosshair) state.onCrosshairUpdate?.invoke(currentHex, currentThickness, currentLineStyle) else state.onColorSelect(currentHex)
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

private val TRADING_VIEW_COLORS = listOf(
    "#ffffff", "#d1d4dc", "#b2b5be", "#868993", "#6a6d78", "#434651", "#363a45", "#2a2e39", "#131722", "#000000",
    "#ff5252", "#ff9800", "#ffeb3b", "#4caf50", "#00bcd4", "#2196f3", "#673ab7", "#9c27b0", "#e91e63", "#795548",
    "#ffcdd2", "#ffe0b2", "#fff9c4", "#c8e6c9", "#b2ebf2", "#bbdefb", "#d1c4e9", "#e1bee7", "#f8bbd0", "#d7ccc8",
    "#ef9a9a", "#ffcc80", "#fff59d", "#a5d6a7", "#80deea", "#90caf9", "#b39ddb", "#ce93d8", "#f48fb1", "#bcaaa4",
    "#e57373", "#ffb74d", "#fff176", "#81c784", "#4dd0e1", "#64b5f6", "#9575cd", "#ba68c8", "#f06292", "#a1887f",
    "#f44336", "#ffa726", "#ffee58", "#66bb6a", "#26c6da", "#42a5f5", "#7e57c2", "#ab47bc", "#ec407a", "#8d6e63",
    "#d32f2f", "#f57c00", "#fbc02d", "#388e3c", "#0097a7", "#1976d2", "#512da8", "#7b1fa2", "#c2185b", "#5d4037",
    "#b71c1c", "#e65100", "#f57f17", "#1b5e20", "#006064", "#0d47a1", "#311b92", "#4a148c", "#880e4f", "#3e2723"
)

private fun getLineStyleText(style: Int): String {
    return when (style) {
        0 -> "Solid"
        1 -> "Dashed"
        2 -> "Dotted"
        else -> "Solid"
    }
}

private fun getLineStyleIndex(style: String): Int {
    return when (style) {
        "Solid" -> 0
        "Dashed" -> 1
        "Dotted" -> 2
        else -> 0
    }
}

private fun parseCanvasColor(hex: String): Color {
    return try {
        if (hex.startsWith("#")) {
            Color(android.graphics.Color.parseColor(hex))
        } else {
            Color.Gray
        }
    } catch (e: Exception) {
        Color.Gray
    }
}
