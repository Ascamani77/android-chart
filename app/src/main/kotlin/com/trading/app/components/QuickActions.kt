package com.trading.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun QuickActionsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(70.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Main circular button body
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFF2A2E39))
                .padding(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.Black)
            )
        }
        
        // Gear Icon
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Quick Actions",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
        
        // The side triangle indicator tab
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 10.dp)
                .size(20.dp, 24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF2A2E39)),
            contentAlignment = Alignment.Center
        ) {
            // Inner triangle
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun QuickActionsModal(
    isFullscreen: Boolean,
    onFullscreenToggle: () -> Unit,
    isHeaderVisible: Boolean,
    onHeaderToggle: () -> Unit,
    isBottomMenuVisible: Boolean,
    onBottomMenuToggle: () -> Unit,
    onSettingsClick: () -> Unit,
    onDrawingsClick: () -> Unit,
    onChartTypeClick: () -> Unit,
    isTimezoneVisible: Boolean,
    onTimezoneToggle: () -> Unit,
    onClose: () -> Unit,
    offset: IntOffset,
    onOffsetChange: (IntOffset) -> Unit
) {
    Box(
        modifier = Modifier
            .offset { offset }
            // pointerInput to handle both dragging and tap consumption
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onOffsetChange(
                            IntOffset(
                                (offset.x + dragAmount.x).roundToInt(),
                                (offset.y + dragAmount.y).roundToInt()
                            )
                        )
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { /* Consume taps inside modal to prevent backdrop close */ }
            }
            .width(260.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFF121212))
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Grid of buttons
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    QuickActionButtonItem(
                        icon = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                        label = if (isFullscreen) "Exit Fullscreen" else "Fullscreen",
                        onClick = onFullscreenToggle,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButtonItem(
                        icon = if (isHeaderVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        label = if (isHeaderVisible) "Hide Header" else "Show Header",
                        onClick = onHeaderToggle,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    QuickActionButtonItem(
                        icon = Icons.Default.Menu,
                        label = if (isBottomMenuVisible) "Hide Bottom Menu" else "Show Bottom Menu",
                        onClick = onBottomMenuToggle,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButtonItem(
                        icon = Icons.Default.Settings,
                        label = "Settings",
                        onClick = onSettingsClick,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    QuickActionButtonItem(
                        icon = Icons.Default.Edit,
                        label = "Drawings Indicators",
                        onClick = onDrawingsClick,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButtonItem(
                        icon = Icons.Default.BarChart,
                        label = "Chart Type",
                        onClick = onChartTypeClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            
            // Custom row for "hide timezone pane"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E222D))
                    .clickable { onTimezoneToggle() }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isTimezoneVisible) "Hide Timezone Pane" else "Show Timezone Pane",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(36.dp, 20.dp)
                        .clip(CircleShape)
                        .background(if (isTimezoneVisible) Color(0xFF2962FF) else Color(0xFF434651))
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .align(if (isTimezoneVisible) Alignment.CenterEnd else Alignment.CenterStart)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // Drag handle footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DragIndicator,
                    contentDescription = null,
                    tint = Color(0xFF434651),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Drag to move",
                    color = Color(0xFF434651),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun QuickActionButtonItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2A2E39), Color(0xFF1E222D))
                )
            )
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 12.sp
        )
    }
}
