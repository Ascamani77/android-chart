package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymbolSearchModal(
    onClose: () -> Unit,
    onSymbolSelect: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Stocks", "Funds", "Futures", "Forex", "Crypto", "Indices", "Bonds")

    val allSymbols = remember {
        listOf(
            // Forex - Multiple sources as requested
            SymbolInfo("EURUSD", "Euro / U.S. dollar", "FXCM", "forex"),
            SymbolInfo("EURUSD", "Euro / U.S. dollar", "OANDA", "forex"),
            SymbolInfo("GBPUSD", "British Pound / U.S. dollar", "FXCM", "forex"),
            SymbolInfo("GBPUSD", "British Pound / U.S. dollar", "OANDA", "forex"),
            SymbolInfo("USDJPY", "US Dollar / Japanese Yen", "FXCM", "forex"),
            SymbolInfo("USDJPY", "US Dollar / Japanese Yen", "OANDA", "forex"),
            SymbolInfo("USDINR", "U.S. DOLLAR / INDIAN RUPEE", "ICE", "forex"),
            SymbolInfo("AUDUSD", "Australian Dollar / US Dollar", "OANDA", "forex"),
            SymbolInfo("USDCAD", "US Dollar / Canadian Dollar", "FXCM", "forex"),
            
            // Crypto - Requested Specific
            SymbolInfo("BTCUSD", "Bitcoin / U.S. dollar", "Bitstamp", "spot crypto"),
            SymbolInfo("BTCUSD", "Bitcoin", "CRYPTO", "spot crypto"),
            SymbolInfo("BTCUSDT", "Bitcoin / TetherUS", "Binance", "spot crypto"),
            SymbolInfo("BTCUSDT.P", "Bitcoin / TetherUS PERP", "Binance", "swap crypto"),
            SymbolInfo("ETHUSD", "Ethereum", "CRYPTO", "spot crypto"),
            SymbolInfo("ETHUSD", "Ethereum / U.S. dollar", "Bitstamp", "spot crypto"),
            SymbolInfo("ETHUSDT", "Ethereum / TetherUS", "Binance", "spot crypto"),
            
            // Stocks - Top 10 Major
            SymbolInfo("AAPL", "Apple Inc.", "NASDAQ", "stock"),
            SymbolInfo("MSFT", "Microsoft Corporation", "NASDAQ", "stock"),
            SymbolInfo("GOOGL", "Alphabet Inc.", "NASDAQ", "stock"),
            SymbolInfo("AMZN", "Amazon.com, Inc.", "NASDAQ", "stock"),
            SymbolInfo("NVDA", "NVIDIA Corporation", "NASDAQ", "stock"),
            SymbolInfo("TSLA", "Tesla, Inc.", "NASDAQ", "stock"),
            SymbolInfo("META", "Meta Platforms, Inc.", "NASDAQ", "stock"),
            SymbolInfo("V", "Visa Inc.", "NYSE", "stock"),
            SymbolInfo("UNH", "UnitedHealth Group Inc.", "NYSE", "stock"),
            SymbolInfo("JPM", "JPMorgan Chase & Co.", "NYSE", "stock"),

            // Bonds
            SymbolInfo("US10Y", "United States 10Y Gov Bond", "TVC", "bond"),
            SymbolInfo("IN10Y", "India 10Y Gov Bond", "TVC", "bond"),
            SymbolInfo("US02Y", "United States 2Y Gov Bond", "TVC", "bond"),
            SymbolInfo("GB02Y", "United Kingdom 2Y Gov Bond", "TVC", "bond"),
            
            // Futures
            SymbolInfo("ES1!", "S&P 500 E-mini Futures", "CME", "futures"),
            SymbolInfo("NQ1!", "Nasdaq 100 E-mini Futures", "CME", "futures"),
            SymbolInfo("GC1!", "Gold Futures", "COMEX", "futures"),
            SymbolInfo("NIFTY", "GIFT NIFTY 50 INDEX FUTURES", "NSEIX", "futures"),

            // Funds
            SymbolInfo("SPY", "SPDR S&P 500 ETF Trust", "AMEX", "fund"),
            SymbolInfo("QQQ", "Invesco QQQ Trust", "NASDAQ", "fund"),

            // Indices
            SymbolInfo("SPX", "S&P 500 Index", "S&P", "index"),
            SymbolInfo("NIFTY", "Nifty 50 Index", "NSE", "index"),
            SymbolInfo("BANKNIFTY", "Nifty Bank Index", "NSE", "index"),
            
            // Metals
            SymbolInfo("XAUUSD", "Gold / US Dollar", "OANDA", "commodity cfd"),
            SymbolInfo("XAUUSD", "Gold vs US Dollar", "Pepperstone", "commodity cfd")
        )
    }

    val filteredSymbols = remember(searchQuery, selectedCategory) {
        allSymbols.filter { symbol ->
            val matchesSearch = symbol.ticker.contains(searchQuery, ignoreCase = true) || 
                              symbol.name.contains(searchQuery, ignoreCase = true)
            val matchesCategory = when (selectedCategory) {
                "All" -> true
                "Stocks" -> symbol.type.contains("stock", ignoreCase = true)
                "Forex" -> symbol.type.contains("forex", ignoreCase = true)
                "Crypto" -> symbol.type.contains("crypto", ignoreCase = true)
                "Bonds" -> symbol.type.contains("bond", ignoreCase = true)
                "Futures" -> symbol.type.contains("futures", ignoreCase = true)
                "Funds" -> symbol.type.contains("fund", ignoreCase = true)
                "Indices" -> symbol.type.contains("index", ignoreCase = true)
                else -> true
            }
            matchesSearch && matchesCategory
        }
    }

    Dialog(
        onDismissRequest = onClose, 
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF000000)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Search (use = to do math)", color = Color(0xFF787B86), fontSize = 16.sp) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = Color(0xFF2962FF),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                }

                // Categories Row
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) Color(0xFF2A2E39) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = category,
                                color = if (isSelected) Color.White else Color(0xFF787B86),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Divider(color = Color(0xFF2A2E39), thickness = 0.5.dp)

                // Symbol List
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredSymbols) { symbol ->
                        SymbolListItem(
                            symbol = symbol,
                            onSelect = { 
                                onSymbolSelect(symbol.ticker)
                                onClose()
                            }
                        )
                        Divider(color = Color(0xFF1E222D), thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun SymbolListItem(symbol: SymbolInfo, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Asset Icon with Flags (from AssetIcons.kt)
        AssetIcon(symbol)

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = symbol.ticker,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Text(
                text = symbol.name,
                color = Color(0xFF787B86),
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = symbol.exchange,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                if (symbol.exchange.lowercase() in listOf("binance", "bitstamp", "oanda", "fxcm", "pepperstone")) {
                    Spacer(modifier = Modifier.width(4.dp))
                    ExchangeIcon(symbol.exchange)
                }
            }
            Text(
                text = symbol.type.lowercase(),
                color = Color(0xFF787B86),
                fontSize = 11.sp
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        val isAdded = symbol.ticker in listOf("BTCUSD", "EURUSD", "AAPL", "US10Y", "XAUUSD")
        
        Icon(
            imageVector = if (isAdded) Icons.Default.Check else Icons.Default.Add,
            contentDescription = "Action",
            tint = if (isAdded) Color(0xFF2962FF) else Color(0xFF787B86),
            modifier = Modifier.size(22.dp)
        )
    }
}
