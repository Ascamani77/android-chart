package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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

data class CurrencyInfo(
    val code: String,
    val name: String,
    val flagColor: Color,
    val isFavorite: Boolean = false
)

@Composable
fun CurrencySelectionModal(
    currentSymbol: String,
    selectedCurrency: String,
    onCurrencySelect: (String) -> Unit,
    onClose: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val allCurrencies = listOf(
        CurrencyInfo("BTC", "Bitcoin", Color(0xFFF7931A), true),
        CurrencyInfo("ETH", "Ethereum", Color(0xFF627EEA), true),
        CurrencyInfo("SOL", "Solana", Color(0xFF14F195), true),
        CurrencyInfo("DOGE", "Dogecoin", Color(0xFFC2A633)),
        CurrencyInfo("ADA", "Cardano", Color(0xFF0033AD)),
        CurrencyInfo("DOT", "Polkadot", Color(0xFFE6007A)),
        CurrencyInfo("MATIC", "Polygon", Color(0xFF8247E5)),
        CurrencyInfo("LINK", "Chainlink", Color(0xFF2A5ADA)),
        CurrencyInfo("AVAX", "Avalanche", Color(0xFFE84142)),
        CurrencyInfo("UNI", "Uniswap", Color(0xFFFF007A))
    )

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
                        Icon(Icons.Default.Close, null, tint = Color.White)
                    }
                    Text(
                        "Select currency",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search", color = Color(0xFF787B86)) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFF787B86)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF131722),
                        unfocusedContainerColor = Color(0xFF131722),
                        focusedBorderColor = Color(0xFF2962FF),
                        unfocusedBorderColor = Color(0xFF2A2E39),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                // List
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        SectionHeader("FAVORITES")
                    }
                    
                    val favorites = allCurrencies.filter { it.isFavorite }
                    items(favorites) { currency ->
                        CurrencyItem(
                            currency = currency,
                            isSelected = currency.code == selectedCurrency,
                            onSelect = {
                                onCurrencySelect(currency.code)
                                onClose()
                            }
                        )
                    }

                    item {
                        SectionHeader("ALL CURRENCIES")
                    }

                    val others = allCurrencies.filter { !it.isFavorite }
                    items(others) { currency ->
                        CurrencyItem(
                            currency = currency,
                            isSelected = currency.code == selectedCurrency,
                            onSelect = {
                                onCurrencySelect(currency.code)
                                onClose()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyItem(
    currency: CurrencyInfo,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Flag/Icon
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(currency.flagColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                currency.code.take(1),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    currency.code,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (currency.isFavorite) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.Star,
                        null,
                        tint = Color(0xFFF7931A),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Text(
                currency.name,
                color = Color(0xFF787B86),
                fontSize = 13.sp
            )
        }
        
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
