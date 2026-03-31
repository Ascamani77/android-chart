package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartSettingsBottomSheet(
    onDismissRequest: () -> Unit,
    onMoreSettingsClick: () -> Unit,
    onResetScale: () -> Unit = {},
    onAutoToggle: (Boolean) -> Unit = {},
    onScaleTypeChange: (String) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    
    var currentPage by remember { mutableStateOf("Main") }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color(0xFF1E222D),
        dragHandle = {
            if (currentPage == "Main") {
                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .width(42.dp)
                        .height(5.dp)
                        .background(Color(0xFF434651), shape = MaterialTheme.shapes.extraLarge)
                )
            }
        },
        windowInsets = WindowInsets(0)
    ) {
        when (currentPage) {
            "Main" -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 80.dp)
                ) {
                    BottomSheetItem(
                        label = "Reset price scale",
                        icon = Icons.Default.Refresh,
                        onClick = {
                            onResetScale()
                            onDismissRequest()
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color(0xFF2A2E39), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    BottomSheetItem(label = "Auto (fits data to screen)", onClick = { onAutoToggle(true) })
                    BottomSheetItem(
                        label = "Lock price to bar ratio",
                        trailing = { Text("5.2912", color = Color(0xFF787B86), fontSize = 15.sp) }
                    )
                    BottomSheetItem(label = "Scale price chart only")
                    BottomSheetItem(label = "Invert scale")

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color(0xFF2A2E39), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    var selectedScaleType by remember { mutableStateOf("Regular") }
                    BottomSheetItem(
                        label = "Regular",
                        trailing = { if (selectedScaleType == "Regular") Icon(Icons.Default.Check, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(24.dp)) },
                        onClick = { selectedScaleType = "Regular" }
                    )
                    BottomSheetItem(
                        label = "Percent",
                        trailing = { if (selectedScaleType == "Percent") Icon(Icons.Default.Check, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(24.dp)) },
                        onClick = { selectedScaleType = "Percent" }
                    )
                    BottomSheetItem(
                        label = "Indexed to 100",
                        trailing = { if (selectedScaleType == "Indexed") Icon(Icons.Default.Check, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(24.dp)) },
                        onClick = { selectedScaleType = "Indexed" }
                    )
                    BottomSheetItem(
                        label = "Logarithmic",
                        trailing = { if (selectedScaleType == "Logarithmic") Icon(Icons.Default.Check, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(24.dp)) },
                        onClick = { selectedScaleType = "Logarithmic" }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color(0xFF2A2E39), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    BottomSheetItem(label = "Move scale to left")
                    BottomSheetItem(
                        label = "Labels", 
                        trailing = { Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF787B86), modifier = Modifier.size(22.dp)) },
                        onClick = { currentPage = "Labels" }
                    )
                    BottomSheetItem(
                        label = "Lines", 
                        trailing = { Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF787B86), modifier = Modifier.size(22.dp)) },
                        onClick = { currentPage = "Lines" }
                    )
                    BottomSheetItem(label = "Plus button")

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color(0xFF2A2E39), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    BottomSheetItem(
                        label = "More settings...",
                        icon = Icons.Default.Settings,
                        onClick = onMoreSettingsClick
                    )
                }
            }
            "Labels" -> {
                LabelsPage(onBack = { currentPage = "Main" })
            }
            "Lines" -> {
                LinesPage(onBack = { currentPage = "Main" })
            }
        }
    }
}

@Composable
fun LabelsPage(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 19.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ArrowBack, 
                    contentDescription = "Back",
                    tint = Color(0xFFD1D4DC),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                "Labels",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Options
        LabelOption("Symbol name label")
        LabelOption("Symbol last price label", isChecked = true)
        LabelOption("Symbol previous day close price label")
        LabelOption("Pre/post market price label", isChecked = true, enabled = false)
        LabelOption("High and low price labels", isChecked = true)
        LabelOption("Bid and ask labels")
        LabelOption("Indicators and financials name labels")
        LabelOption("Indicators and financials value labels", isChecked = true)
        LabelOption("Countdown to bar close", isChecked = true)
        
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color(0xFF2A2E39), thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(8.dp))
        
        LabelOption("No overlapping labels", isChecked = true)
    }
}

@Composable
fun LinesPage(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 19.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ArrowBack, 
                    contentDescription = "Back",
                    tint = Color(0xFFD1D4DC),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                "Lines",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Options
        LabelOption("Price line", isChecked = true)
        LabelOption("Previous day close price line", isChecked = true)
        LabelOption("Pre/post market price line", isChecked = true, enabled = false)
        LabelOption("High and low price lines")
        LabelOption("Bid and ask lines")
    }
}

@Composable
fun LabelOption(label: String, isChecked: Boolean = false, enabled: Boolean = true, onClick: () -> Unit = {}) {
    val contentColor = if (enabled) Color(0xFFD1D4DC) else Color(0xFF787B86).copy(alpha = 0.4f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 24.dp, vertical = 19.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(36.dp)) {
            if (isChecked) {
                Icon(
                    Icons.Default.Check, 
                    null, 
                    tint = contentColor, 
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Text(
            text = label,
            color = contentColor,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BottomSheetItem(
    label: String,
    icon: ImageVector? = null,
    onClick: () -> Unit = {},
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 19.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(icon, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(26.dp))
            Spacer(modifier = Modifier.width(20.dp))
        }
        Text(
            text = label,
            color = Color(0xFFD1D4DC),
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        if (trailing != null) {
            trailing()
        }
    }
}
