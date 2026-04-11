package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingsModal(
    onClose: () -> Unit,
    onToolSelect: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = Color(0xFF121212),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF363A45))
            )
        },
        windowInsets = WindowInsets(0),
        modifier = Modifier.fillMaxHeight(0.93f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Drawings",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            var searchQuery by remember { mutableStateOf("") }

            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                placeholder = { Text("Search", color = Color(0xFF787B86)) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFF787B86)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E222D),
                    unfocusedContainerColor = Color(0xFF1E222D),
                    disabledContainerColor = Color(0xFF1E222D),
                    cursorColor = Color(0xFF2962FF),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            // Categories Row
            val categories = listOf(
                "Favorites", "Tools", "Trend lines", "Gann and Fibonacci", 
                "Patterns", "Forecasting and measurement", "Geometric shapes", 
                "Annotation", "Visuals"
            )
            var selectedCategory by remember { mutableStateOf("Favorites") }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF2A2E39) else Color.Transparent)
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else Color(0xFF787B86),
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }

            // Content Area
            var showFavoritesOnChart by remember { mutableStateOf(true) }
            
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                item {
                    Text(
                        text = selectedCategory.uppercase(),
                        color = Color(0xFF787B86),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
                
                item {
                    DrawingToolItem(
                        name = "Horizontal Line",
                        icon = Icons.Outlined.Edit,
                        isFavorite = true,
                        onClick = { 
                            onToolSelect("horizontal_line")
                            onClose()
                        }
                    )
                }
            }

            // Bottom Toggle
            Divider(color = Color(0xFF2A2E39), thickness = 0.5.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Edit, null, tint = Color(0xFFD1D4DC), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Show favorites on Chart", color = Color(0xFFD1D4DC), fontSize = 14.sp)
                }
                Switch(
                    checked = showFavoritesOnChart,
                    onCheckedChange = { showFavoritesOnChart = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF2962FF),
                        uncheckedThumbColor = Color(0xFF787B86),
                        uncheckedTrackColor = Color(0xFF2A2E39)
                    )
                )
            }
        }
    }
}

@Composable
fun DrawingToolItem(
    name: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E222D))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                color = Color.White,
                fontSize = 11.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 13.sp
            )
        }
        
        Icon(
            imageVector = if (isFavorite) Icons.Default.Star else Icons.Outlined.StarOutline,
            contentDescription = null,
            tint = if (isFavorite) Color(0xFFFF9800) else Color(0xFF787B86),
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.TopEnd)
        )
    }
}
