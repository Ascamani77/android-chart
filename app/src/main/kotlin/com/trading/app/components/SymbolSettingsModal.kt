package com.trading.app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.trading.app.models.ChartSettings
import com.trading.app.models.SymbolSettings

@Composable
fun SymbolSettingsModal(
    settings: ChartSettings,
    onUpdate: (ChartSettings) -> Unit,
    onClose: () -> Unit
) {
    var tempSettings by remember { mutableStateOf(settings.symbol) }

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
                        onColorUpClick = { /* Color Picker */ },
                        onColorDownClick = { /* Color Picker */ }
                    )

                    // Borders
                    SettingsToggleWithColors(
                        label = "Borders",
                        checked = tempSettings.borderVisible,
                        onCheckedChange = { tempSettings = tempSettings.copy(borderVisible = it) },
                        colorUp = tempSettings.borderColorUp,
                        colorDown = tempSettings.borderColorDown,
                        onColorUpClick = { /* Color Picker */ },
                        onColorDownClick = { /* Color Picker */ }
                    )

                    // Wick
                    SettingsToggleWithColors(
                        label = "Wick",
                        checked = tempSettings.wickVisible,
                        onCheckedChange = { tempSettings = tempSettings.copy(wickVisible = it) },
                        colorUp = tempSettings.wickColorUp,
                        colorDown = tempSettings.wickColorDown,
                        onColorUpClick = { /* Color Picker */ },
                        onColorDownClick = { /* Color Picker */ }
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
                                onUpdate(settings.copy(symbol = tempSettings))
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
            modifier = Modifier.weight(1f)
        )
        
        ColorBox(colorUp, onColorUpClick)
        Spacer(modifier = Modifier.width(8.dp))
        ColorBox(colorDown, onColorDownClick)
    }
}

@Composable
fun ColorBox(hex: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(android.graphics.Color.parseColor(hex)))
            .border(1.dp, Color(0xFF2A2E39), RoundedCornerShape(4.dp))
            .clickable { onClick() }
    )
}

@Composable
fun SettingsDropdown(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(40.dp)
                .border(1.dp, Color(0xFF363A45), RoundedCornerShape(4.dp))
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
