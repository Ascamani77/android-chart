package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
            // Forex - Top 5 Major
            SymbolInfo("EURUSD", "Euro / US Dollar", "FXCM", "Forex"),
            SymbolInfo("USDJPY", "US Dollar / Japanese Yen", "OANDA", "Forex"),
            SymbolInfo("GBPUSD", "British Pound / US Dollar", "FXCM", "Forex"),
            SymbolInfo("AUDUSD", "Australian Dollar / US Dollar", "OANDA", "Forex"),
            SymbolInfo("USDCAD", "US Dollar / Canadian Dollar", "FXCM", "Forex"),
            
            // Crypto - Requested Specific
            SymbolInfo("BTCUSD", "Bitcoin / US Dollar", "BITSTAMP", "Crypto"),
            SymbolInfo("BTCUSDT", "Bitcoin / TetherUS", "BINANCE", "Crypto"),
            SymbolInfo("ETHUSD", "Ethereum / US Dollar", "BITSTAMP", "Crypto"),
            SymbolInfo("ETHUSDT", "Ethereum / TetherUS", "BINANCE", "Crypto"),
            
            // Stocks - Top 10 Major
            SymbolInfo("AAPL", "Apple Inc.", "NASDAQ", "Stock"),
            SymbolInfo("MSFT", "Microsoft Corporation", "NASDAQ", "Stock"),
            SymbolInfo("GOOGL", "Alphabet Inc.", "NASDAQ", "Stock"),
            SymbolInfo("AMZN", "Amazon.com, Inc.", "NASDAQ", "Stock"),
            SymbolInfo("NVDA", "NVIDIA Corporation", "NASDAQ", "Stock"),
            SymbolInfo("TSLA", "Tesla, Inc.", "NASDAQ", "Stock"),
            SymbolInfo("META", "Meta Platforms, Inc.", "NASDAQ", "Stock"),
            SymbolInfo("V", "Visa Inc.", "NYSE", "Stock"),
            SymbolInfo("UNH", "UnitedHealth Group Inc.", "NYSE", "Stock"),
            SymbolInfo("JPM", "JPMorgan Chase & Co.", "NYSE", "Stock"),

            // Bonds
            SymbolInfo("US10Y", "United States 10Y Gov Bond", "TVC", "Bond"),
            SymbolInfo("US02Y", "United States 2Y Gov Bond", "TVC", "Bond"),
            SymbolInfo("IN10Y", "India 10Y Gov Bond", "TVC", "Bond"),
            SymbolInfo("GB02Y", "United Kingdom 2Y Gov Bond", "TVC", "Bond"),
            SymbolInfo("DE10", "Germany 10Y Gov Bond", "TVC", "Bond"),
            
            // Futures
            SymbolInfo("ES1!", "S&P 500 E-mini Futures", "CME", "Futures"),
            SymbolInfo("NQ1!", "Nasdaq 100 E-mini Futures", "CME", "Futures"),
            SymbolInfo("GC1!", "Gold Futures", "COMEX", "Futures"),
            SymbolInfo("CL1!", "Crude Oil Futures", "NYMEX", "Futures"),

            // Funds
            SymbolInfo("SPY", "SPDR S&P 500 ETF Trust", "AMEX", "Fund"),
            SymbolInfo("QQQ", "Invesco QQQ Trust", "NASDAQ", "Fund"),
            SymbolInfo("IVV", "iShares Core S&P 500 ETF", "NYSE", "Fund"),

            // Indices
            SymbolInfo("SPX", "S&P 500 Index", "S&P", "Index"),
            SymbolInfo("NIFTY", "Nifty 50 Index", "NSE", "Index"),
            SymbolInfo("BANKNIFTY", "Nifty Bank Index", "NSE", "Index"),
            SymbolInfo("DJI", "Dow Jones Industrial Average", "DJI", "Index"),
            
            // Metals (Often in Futures or All)
            SymbolInfo("XAUUSD", "Gold / US Dollar", "OANDA", "Commodity CFD"),
            SymbolInfo("XAGUSD", "Silver / US Dollar", "OANDA", "Commodity CFD")
        )
    }

    val filteredSymbols = remember(searchQuery, selectedCategory) {
        allSymbols.filter { symbol ->
            val matchesSearch = symbol.ticker.contains(searchQuery, ignoreCase = true) || 
                              symbol.name.contains(searchQuery, ignoreCase = true)
            val matchesCategory = when (selectedCategory) {
                "All" -> true
                "Stocks" -> symbol.type == "Stock"
                "Forex" -> symbol.type == "Forex"
                "Crypto" -> symbol.type == "Crypto"
                "Bonds" -> symbol.type == "Bond"
                "Futures" -> symbol.type == "Futures"
                "Funds" -> symbol.type == "Fund"
                "Indices" -> symbol.type == "Index"
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
                // Header with Back and Search
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
        // Icon / Circle
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(getSymbolBackgroundColor(symbol.type), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                symbol.ticker.take(1), 
                color = Color.White, 
                fontSize = 14.sp, 
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

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
            Text(
                text = symbol.exchange,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = symbol.type.lowercase(),
                color = Color(0xFF787B86),
                fontSize = 11.sp
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Match the checkmark/plus logic from screenshots
        val isAdded = symbol.ticker in listOf("BTCUSD", "EURUSD", "AAPL", "US10Y")
        
        Icon(
            imageVector = if (isAdded) Icons.Default.Check else Icons.Default.Add,
            contentDescription = "Action",
            tint = if (isAdded) Color(0xFF2962FF) else Color(0xFF787B86),
            modifier = Modifier.size(22.dp)
        )
    }
}

fun getSymbolBackgroundColor(type: String): Color {
    return when (type) {
        "Stock" -> Color(0xFF1E88E5)
        "Forex" -> Color(0xFF43A047)
        "Crypto" -> Color(0xFFF4511E)
        "Bond" -> Color(0xFF8E24AA)
        "Futures" -> Color(0xFFE53935)
        "Fund" -> Color(0xFF00ACC1)
        "Index" -> Color(0xFF3949AB)
        "Commodity CFD" -> Color(0xFFFFB300)
        else -> Color(0xFF455A64)
    }
}
