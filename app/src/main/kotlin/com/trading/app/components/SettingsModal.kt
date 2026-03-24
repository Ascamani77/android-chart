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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.trading.app.models.ChartSettings

@Composable
fun SettingsModal(
    settings: ChartSettings,
    onUpdate: (ChartSettings) -> Unit,
    onClose: () -> Unit
) {
    var activeSubModal by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (activeSubModal == null) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Settings",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = onClose) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color(0xFF787B86),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        // Scrollable Settings List
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            SettingsItem("Symbol", Icons.Default.CandlestickChart) { activeSubModal = "Symbol" }
                            SettingsItem("Status line", Icons.Default.Notes) { activeSubModal = "Status line" }
                            SettingsItem("Scales and lines", Icons.Default.Straighten) { activeSubModal = "Scales and lines" }
                            SettingsItem("Canvas", Icons.Default.Edit) { activeSubModal = "Canvas" }
                            SettingsItem("Trading", Icons.Default.TrendingUp) { activeSubModal = "Trading" }
                            SettingsItem("Alerts", Icons.Default.NotificationsNone) { activeSubModal = "Alerts" }
                            SettingsItem("Events", Icons.Default.CalendarToday) { activeSubModal = "Events" }
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
                                    onClick = onClose,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text("Ok", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    when (activeSubModal) {
                        "Symbol" -> {
                            SymbolSettingsModal(
                                settings = settings,
                                onUpdate = onUpdate,
                                onClose = { activeSubModal = null }
                            )
                        }
                        "Status line" -> {
                            StatusLineSettingsModal(
                                settings = settings,
                                onUpdate = onUpdate,
                                onClose = { activeSubModal = null }
                            )
                        }
                        "Scales and lines" -> {
                            ScalesAndLinesSettingsModal(
                                settings = settings,
                                onUpdate = onUpdate,
                                onClose = { activeSubModal = null }
                            )
                        }
                        "Canvas" -> {
                            CanvasSettingsModal(
                                settings = settings,
                                onUpdate = onUpdate,
                                onClose = { activeSubModal = null }
                            )
                        }
                        "Trading" -> {
                            TradingSettingsModal(
                                settings = settings,
                                onUpdate = onUpdate,
                                onClose = { activeSubModal = null }
                            )
                        }
                        "Alerts" -> {
                            AlertsSettingsModal(
                                settings = settings,
                                onUpdate = onUpdate,
                                onClose = { activeSubModal = null }
                            )
                        }
                        "Events" -> {
                            EventsSettingsModal(
                                settings = settings,
                                onUpdate = onUpdate,
                                onClose = { activeSubModal = null }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                null,
                tint = Color(0xFFD1D4DC),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                label,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.ChevronRight,
                null,
                tint = Color(0xFF434651),
                modifier = Modifier.size(20.dp)
            )
        }
        Divider(
            color = Color(0xFF1E222D),
            modifier = Modifier.padding(start = 56.dp)
        )
    }
}
