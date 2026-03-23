package com.trading.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import com.trading.app.models.SymbolInfo

@Composable
fun SymbolSearchModal(
    onClose: () -> Unit,
    onSymbolSelect: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val symbols = listOf(
        SymbolInfo("AAPL", "Apple Inc.", "NASDAQ", "Stock"),
        SymbolInfo("TSLA", "Tesla, Inc.", "NASDAQ", "Stock"),
        SymbolInfo("BTCUSD", "Bitcoin / US Dollar", "BINANCE", "Crypto"),
        SymbolInfo("ETHUSD", "Ethereum / US Dollar", "BINANCE", "Crypto"),
        SymbolInfo("EURUSD", "Euro / US Dollar", "FXCM", "Forex"),
        SymbolInfo("XAUUSD", "Gold / US Dollar", "OANDA", "Metals"),
        SymbolInfo("NVDA", "NVIDIA Corporation", "NASDAQ", "Stock"),
        SymbolInfo("MSFT", "Microsoft Corporation", "NASDAQ", "Stock"),
        SymbolInfo("AMZN", "Amazon.com, Inc.", "NASDAQ", "Stock"),
        SymbolInfo("GOOGL", "Alphabet Inc.", "NASDAQ", "Stock")
    )

    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(0.8f),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF1E222D)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Symbol Search", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, null, tint = Color(0xFF787B86))
                    }
                }

                // Search Bar
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

                // Symbol List
                LazyColumn {
                    items(symbols.filter { it.ticker.contains(searchQuery, ignoreCase = true) || it.name.contains(searchQuery, ignoreCase = true) }) { symbol ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSymbolSelect(symbol.ticker); onClose() }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(symbol.ticker, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(symbol.name, color = Color(0xFF787B86), fontSize = 12.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(symbol.exchange, color = Color(0xFF787B86), fontSize = 12.sp)
                                Text(symbol.type, color = Color(0xFF787B86), fontSize = 12.sp)
                            }
                        }
                        Divider(color = Color(0xFF2A2E39))
                    }
                }
            }
        }
    }
}
