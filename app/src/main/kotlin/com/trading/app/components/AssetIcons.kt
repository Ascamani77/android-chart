package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.trading.app.models.SymbolInfo

@Composable
fun AssetIcon(symbol: SymbolInfo, modifier: Modifier = Modifier, size: Int = 32) {
    val type = symbol.type.lowercase()
    val ticker = symbol.ticker.uppercase()
    
    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            // Crypto + Fiat Pair (e.g. BTCUSD, ETHUSD)
            (type.contains("crypto") || ticker.startsWith("BTC") || ticker.startsWith("ETH")) && 
            ticker.length > 3 && isFiat(ticker.takeLast(3)) -> {
                val crypto = ticker.take(ticker.length - 3)
                val fiat = ticker.takeLast(3)
                
                Box(modifier = Modifier.fillMaxSize()) {
                    CryptoLogo(crypto, size = (size * 0.75).toInt(), modifier = Modifier.align(Alignment.TopStart))
                    FlagImage(
                        currency = fiat,
                        modifier = Modifier
                            .size((size * 0.65).dp)
                            .align(Alignment.BottomEnd)
                            .border(1.5.dp, Color.Black, CircleShape)
                            .padding(0.5.dp)
                    )
                }
            }
            // Forex Pair (e.g. EURUSD, GBPUSD)
            (type.contains("forex") || ticker.length == 6) && isFiat(ticker.take(3)) && isFiat(ticker.takeLast(3)) -> {
                val base = ticker.substring(0, 3)
                val quote = ticker.substring(3, 6)
                
                Box(modifier = Modifier.fillMaxSize()) {
                    FlagImage(
                        currency = base,
                        modifier = Modifier
                            .size((size * 0.7).dp)
                            .align(Alignment.TopStart)
                    )
                    FlagImage(
                        currency = quote,
                        modifier = Modifier
                            .size((size * 0.7).dp)
                            .align(Alignment.BottomEnd)
                            .border(1.5.dp, Color.Black, CircleShape)
                            .padding(0.5.dp)
                    )
                }
            }
            // Pure Crypto (e.g. BTC, ETH)
            type.contains("crypto") || ticker in listOf("BTC", "ETH", "SOL", "USDT") -> {
                CryptoLogo(ticker, size = size)
            }
            // Bonds, Indices, Stocks (Single Flag or Logo)
            type.contains("bond") || type.contains("index") || ticker in listOf("NIFTY", "BANKNIFTY", "SPX", "DJI", "IXIC") -> {
                val country = when {
                    ticker.startsWith("US") || ticker.startsWith("SP") || ticker.startsWith("DJ") || ticker == "IXIC" -> "US"
                    ticker.contains("NIFTY") || ticker.startsWith("IN") -> "IN"
                    ticker.startsWith("GB") -> "GB"
                    ticker.startsWith("DE") -> "DE"
                    else -> "US"
                }
                FlagImage(currency = country, modifier = Modifier.size(size.dp))
            }
            type.contains("commodity") || ticker.contains("XAU") || ticker.contains("XAG") -> {
                Box(
                    modifier = Modifier
                        .size(size.dp)
                        .background(Color(0xFFFFB300), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (ticker.contains("XAU")) "Au" else if (ticker.contains("XAG")) "Ag" else ticker.take(1),
                        color = Color.White,
                        fontSize = (size * 0.45).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            else -> {
                Box(
                    modifier = Modifier
                        .size(size.dp)
                        .background(getSymbolBackgroundColor(symbol.type), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        ticker.take(1), 
                        color = Color.White, 
                        fontSize = (size * 0.45).sp, 
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun isFiat(code: String): Boolean {
    return code.uppercase() in listOf("USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "INR")
}

@Composable
fun FlagImage(currency: String, modifier: Modifier = Modifier) {
    val code = when (currency.uppercase()) {
        "EUR" -> "eu"
        "USD", "US" -> "us"
        "JPY", "JP" -> "jp"
        "GBP", "GB" -> "gb"
        "AUD" -> "au"
        "CAD" -> "ca"
        "INR", "IN" -> "in"
        "CHF" -> "ch"
        "DE" -> "de"
        else -> "us"
    }
    
    AsyncImage(
        model = "https://flagcdn.com/w80/$code.png",
        contentDescription = currency,
        modifier = modifier
            .clip(CircleShape)
            .background(Color(0xFF1E222D)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CryptoLogo(ticker: String, modifier: Modifier = Modifier, size: Int = 32) {
    val url = when {
        ticker.contains("BTC") -> "https://assets.coingecko.com/coins/images/1/small/bitcoin.png"
        ticker.contains("ETH") -> "https://assets.coingecko.com/coins/images/279/small/ethereum.png"
        ticker.contains("USDT") -> "https://assets.coingecko.com/coins/images/325/small/tether.png"
        ticker.contains("SOL") -> "https://assets.coingecko.com/coins/images/4128/small/solana.png"
        else -> null
    }
    
    if (url != null) {
        AsyncImage(
            model = url,
            contentDescription = ticker,
            modifier = modifier.size(size.dp).clip(CircleShape)
        )
    } else {
        Box(
            modifier = modifier.size(size.dp).background(Color(0xFF455A64), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(ticker.take(1), color = Color.White, fontSize = (size * 0.45).sp)
        }
    }
}

@Composable
fun ExchangeIcon(exchange: String, modifier: Modifier = Modifier) {
    val color = when (exchange.lowercase()) {
        "binance" -> Color(0xFFF0B90B)
        "oanda" -> Color(0xFF2962FF)
        "fxcm" -> Color(0xFF003399)
        "bitstamp" -> Color(0xFF4CAF50)
        "pepperstone" -> Color(0xFF003399)
        else -> Color(0xFF787B86)
    }
    Box(
        modifier = modifier
            .size(14.dp)
            .background(color, CircleShape)
            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(exchange.take(1).uppercase(), color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
    }
}

fun getSymbolBackgroundColor(type: String): Color {
    val t = type.lowercase()
    return when {
        t.contains("stock") -> Color(0xFF1E88E5)
        t.contains("forex") -> Color(0xFF43A047)
        t.contains("crypto") -> Color(0xFFF4511E)
        t.contains("bond") -> Color(0xFF8E24AA)
        t.contains("futures") -> Color(0xFFE53935)
        t.contains("fund") -> Color(0xFF00ACC1)
        t.contains("index") -> Color(0xFF3949AB)
        t.contains("commodity") -> Color(0xFFFFB300)
        else -> Color(0xFF455A64)
    }
}
