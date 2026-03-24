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
import androidx.compose.material.icons.outlined.HelpOutline
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
import com.trading.app.models.ScalesSettings

@Composable
fun ScalesAndLinesSettingsModal(
    settings: ChartSettings,
    onUpdate: (ChartSettings) -> Unit,
    onClose: () -> Unit
) {
    var tempSettings by remember { mutableStateOf(settings.scales) }

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
                        "Scales and lines",
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
                    // PRICE SCALE
                    SectionHeader("PRICE SCALE")
                    
                    SettingsDropdownRow(
                        label = "Currency and Unit",
                        value = tempSettings.currencyAndUnit,
                        options = listOf("Always visible", "Visible on mouse over", "Hidden"),
                        onValueChange = { tempSettings = tempSettings.copy(currencyAndUnit = it) }
                    )
                    
                    SettingsDropdownRow(
                        label = "Scale modes (A and L)",
                        value = tempSettings.scaleModes,
                        options = listOf("Visible on tap", "Always visible", "Hidden"),
                        onValueChange = { tempSettings = tempSettings.copy(scaleModes = it) }
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = tempSettings.lockRatio,
                            onCheckedChange = { tempSettings = tempSettings.copy(lockRatio = it) },
                            colors = CheckboxDefaults.colors(checkedColor = Color.White, uncheckedColor = Color(0xFF434651), checkmarkColor = Color.Black)
                        )
                        Text("Lock price to bar ratio", color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(36.dp)
                                .background(if (tempSettings.lockRatio) Color.Transparent else Color(0xFF1E222D), RoundedCornerShape(4.dp))
                                .border(1.dp, Color(0xFF363A45), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(tempSettings.lockRatioValue, color = if (tempSettings.lockRatio) Color.White else Color(0xFF434651), fontSize = 13.sp)
                        }
                    }
                    
                    SettingsDropdownRow(
                        label = "Scales placement",
                        value = tempSettings.scalesPlacement,
                        options = listOf("Auto", "Left", "Right", "None"),
                        onValueChange = { tempSettings = tempSettings.copy(scalesPlacement = it) }
                    )

                    // PRICE LABELS & LINES
                    SectionHeader("PRICE LABELS & LINES")
                    
                    ScalesCheckboxRow("No overlapping labels", tempSettings.noOverlappingLabels) { tempSettings = tempSettings.copy(noOverlappingLabels = it) }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().clickable { tempSettings = tempSettings.copy(plusButton = !tempSettings.plusButton) }
                    ) {
                        Checkbox(
                            checked = tempSettings.plusButton,
                            onCheckedChange = { tempSettings = tempSettings.copy(plusButton = it) },
                            colors = CheckboxDefaults.colors(checkedColor = Color.White, uncheckedColor = Color(0xFF434651), checkmarkColor = Color.Black)
                        )
                        Text("Plus button", color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Outlined.HelpOutline, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
                    }
                    
                    ScalesCheckboxRow("Countdown to bar close", tempSettings.countdown) { tempSettings = tempSettings.copy(countdown = it) }

                    SettingsLabelDropdownRow(
                        label = "Symbol",
                        value = tempSettings.symbolLabel,
                        options = listOf("Name, value, line", "Name", "Value", "None"),
                        onValueChange = { tempSettings = tempSettings.copy(symbolLabel = it) }
                    )
                    Row(modifier = Modifier.padding(start = 48.dp, bottom = 8.dp)) {
                        SymbolLinePreview()
                    }
                    
                    SettingsLabelDropdownRow(
                        label = "",
                        value = tempSettings.symbolLastValueMode,
                        options = listOf("Value according to scale", "No overlap", "None"),
                        onValueChange = { tempSettings = tempSettings.copy(symbolLastValueMode = it) }
                    )
                    
                    SettingsLabelDropdownRow(
                        label = "High and low",
                        value = tempSettings.highLowMode,
                        options = listOf("Hidden", "Price", "Lines", "Both"),
                        onValueChange = { tempSettings = tempSettings.copy(highLowMode = it) }
                    )
                    Row(modifier = Modifier.padding(start = 48.dp, bottom = 8.dp)) {
                        SymbolLinePreview()
                    }
                    
                    SettingsLabelDropdownRow(
                        label = "Bid and ask",
                        value = tempSettings.bidAskMode,
                        options = listOf("Hidden", "Price", "Lines", "Both"),
                        onValueChange = { tempSettings = tempSettings.copy(bidAskMode = it) }
                    )
                    Row(modifier = Modifier.padding(start = 48.dp, bottom = 16.dp)) {
                        ColorBox("#2962FF", {})
                        Spacer(modifier = Modifier.width(8.dp))
                        ColorBox("#F05252", {})
                    }

                    // TIME SCALE
                    SectionHeader("TIME SCALE")
                    
                    ScalesCheckboxRow("Day of week on labels", tempSettings.dayOfWeekOnLabels) { tempSettings = tempSettings.copy(dayOfWeekOnLabels = it) }
                    
                    SettingsDropdownRow(
                        label = "Date format",
                        value = tempSettings.dateFormat,
                        options = listOf("Mon 29 Sep '97", "29 Sep '97", "09/29/97"),
                        onValueChange = { tempSettings = tempSettings.copy(dateFormat = it) }
                    )
                    SettingsDropdownRow(
                        label = "Time hours format",
                        value = tempSettings.timeFormat,
                        options = listOf("24-hours", "12-hours"),
                        onValueChange = { tempSettings = tempSettings.copy(timeFormat = it) }
                    )
                    
                    ScalesCheckboxRow("Save chart left edge position when changing interval", tempSettings.saveLeftEdge) { tempSettings = tempSettings.copy(saveLeftEdge = it) }
                    
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
                                onUpdate(settings.copy(scales = tempSettings))
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
fun SettingsDropdownRow(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Box {
            Box(
                modifier = Modifier
                    .width(160.dp)
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
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = Color.White,
                            trailingIconColor = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsLabelDropdownRow(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (label.isNotEmpty()) {
            Text(label, color = Color.White, fontSize = 14.sp, modifier = Modifier.weight(1f))
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
        Box {
            Box(
                modifier = Modifier
                    .width(160.dp)
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
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ScalesCheckboxRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!checked) }.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = Color.White, uncheckedColor = Color(0xFF434651), checkmarkColor = Color.Black)
        )
        Text(label, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun SymbolLinePreview() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(80.dp)
            .height(32.dp)
            .background(Color(0xFF1E222D), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF363A45), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp)
    ) {
        // Split color box (Red/Green)
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(2.dp))
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFF05252)))
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFF089981)))
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        // White line
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(1.dp)
                .background(Color.White)
        )
    }
}
