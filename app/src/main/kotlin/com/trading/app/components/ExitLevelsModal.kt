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
import androidx.compose.material.icons.outlined.Delete
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
import com.trading.app.models.SymbolInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExitLevelsModal(
    symbol: String,
    orderType: String,
    entryPrice: Float,
    onClose: () -> Unit,
    onConfirm: (List<ExitLevel>) -> Unit
) {
    var levels by remember { mutableStateOf(listOf(ExitLevel(id = 1, units = "1"))) }
    val scrollState = rememberScrollState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Color(0xFF787B86),
                width = 40.dp,
                height = 4.dp
            )
        },
        containerColor = Color.Black,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Text(
                    "Exit levels",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Icon(
                    Icons.Outlined.HelpOutline,
                    null,
                    tint = Color(0xFF787B86),
                    modifier = Modifier.size(18.dp).padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, null, tint = Color.White)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
            ) {
                // Asset Info
                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssetIcon(SymbolInfo(ticker = symbol, name = "", type = "Crypto"), size = 32)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(symbol, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "$orderType Market 1 @ ${String.format("%,.2f", entryPrice)}",
                            color = Color(0xFF787B86),
                            fontSize = 13.sp
                        )
                    }
                }

                // Protected size
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Protected size", color = Color(0xFF787B86), fontSize = 14.sp)
                    Text(" • 200.00%", color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Warning, null, tint = Color(0xFFF2A52C), modifier = Modifier.size(14.dp))
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF2A2E39))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f) // Based on the image showing 200% maybe? Assuming it's full orange
                            .fillMaxHeight()
                            .background(Color(0xFFF2A52C))
                    )
                }

                Text(
                    "Exits • Risk/Reward 3.24",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // Render Levels
                levels.forEachIndexed { index, level ->
                    LevelItem(
                        level = level,
                        index = index,
                        isLast = index == levels.size - 1,
                        onUpdate = { updated ->
                            levels = levels.toMutableList().apply { set(index, updated) }
                        },
                        onDelete = {
                            if (levels.size > 1) {
                                levels = levels.toMutableList().apply { removeAt(index) }
                            }
                        }
                    )
                }

                // Add level button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                        .clickable {
                            levels = levels + ExitLevel(id = levels.size + 1, units = "1")
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("+ Add level", color = Color(0xFF2962FF), fontSize = 14.sp)
                }
            }

            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onClose,
                    modifier = Modifier.weight(1f).height(48.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Discard", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { onConfirm(levels) },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Confirm", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun LevelItem(
    level: ExitLevel,
    index: Int,
    isLast: Boolean,
    onUpdate: (ExitLevel) -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Level ${index + 1} • 100.00%", color = Color(0xFF787B86), fontSize = 13.sp)
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Outlined.Delete, null, tint = Color(0xFF787B86), modifier = Modifier.size(18.dp))
            }
        }

        if (!isLast) {
            // Summary view for previously added levels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(Color(0xFF1E222D), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFF2A2E39), RoundedCornerShape(4.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TP", color = Color(0xFF089981), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(" 1 @ 5,079.66", color = Color.White, fontSize = 14.sp)
                Text(" • ", color = Color(0xFF787B86))
                Text("SL", color = Color(0xFFF23645), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(" 1 @ 4,462.78", color = Color.White, fontSize = 14.sp)
            }
        } else {
            // Expanded editable view for the current level
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(Color(0xFF1E222D), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                // Units
                Text("Units", color = Color(0xFF787B86), fontSize = 12.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .border(1.dp, Color(0xFF2962FF), RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(level.units, color = Color.White, fontSize = 15.sp, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.SwapHoriz, null, tint = Color(0xFF787B86), modifier = Modifier.size(18.dp))
                    Text("100.00%", color = Color(0xFF787B86), fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
                }

                // Take Profit
                Row(modifier = Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Take profit, price", color = Color(0xFF787B86), fontSize = 12.sp)
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(14.dp))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .background(Color(0xFF131722), RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("5079.66", color = Color.White, fontSize = 15.sp, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.SwapHoriz, null, tint = Color(0xFF787B86), modifier = Modifier.size(18.dp))
                    Text("47150 ticks", color = Color(0xFF787B86), fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(14.dp))
                }

                // Stop Loss
                Row(modifier = Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Stop loss, price", color = Color(0xFF787B86), fontSize = 12.sp)
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(14.dp))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .background(Color(0xFF131722), RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("4462.78", color = Color.White, fontSize = 15.sp, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.SwapHoriz, null, tint = Color(0xFF787B86), modifier = Modifier.size(18.dp))
                    Text("14538 ticks", color = Color(0xFF787B86), fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = Color(0xFF787B86), modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

data class ExitLevel(
    val id: Int,
    val units: String = "1",
    val tp: String = "",
    val sl: String = ""
)
