package com.trading.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun IndicatorsModal(
    onClose: () -> Unit,
    onIndicatorSelect: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val indicators = listOf("EMA", "VWAP", "Bollinger Bands", "RSI", "ATR", "MACD", "Stochastic", "Volume")

    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.8f),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF1E222D)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Indicators", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, null, tint = Color(0xFF787B86))
                    }
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    placeholder = { Text("Search", color = Color(0xFF787B86)) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFF787B86)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2962FF),
                        unfocusedBorderColor = Color(0xFF363A45),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                LazyColumn {
                    items(indicators.filter { it.contains(searchQuery, ignoreCase = true) }) { indicator ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onIndicatorSelect(indicator); onClose() }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(indicator, color = Color.White, fontSize = 16.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.StarBorder, null, tint = Color(0xFF787B86))
                        }
                        Divider(color = Color(0xFF2A2E39))
                    }
                }
            }
        }
    }
}
