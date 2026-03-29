package com.trading.app.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun QuickActionsButton(
    onClick: () -> Unit,
    offset: IntOffset,
    onOffsetChange: (IntOffset) -> Unit,
    isLocked: Boolean = false,
    isModalOpen: Boolean = false,
    autoHideDelay: Long = 3000L,
    modifier: Modifier = Modifier
) {
    var isFaded by remember { mutableStateOf(false) }
    var interactionTrigger by remember { mutableIntStateOf(0) }

    val alpha by animateFloatAsState(
        targetValue = if (isFaded && !isModalOpen) 0.3f else 1f,
        animationSpec = tween(durationMillis = 500),
        label = "ButtonFadeAlpha"
    )

    LaunchedEffect(interactionTrigger, isModalOpen) {
        if (isModalOpen) {
            isFaded = false
            return@LaunchedEffect
        }
        isFaded = false
        delay(autoHideDelay)
        isFaded = true
    }

    val currentOffset by rememberUpdatedState(offset)
    val currentOnOffsetChange by rememberUpdatedState(onOffsetChange)

    Box(
        modifier = modifier
            .offset { currentOffset }
            .size(63.dp) // Reduced from 70.dp (10% reduction)
            .graphicsLayer(alpha = alpha)
            .pointerInput(isLocked) {
                if (!isLocked) {
                    detectDragGestures(
                        onDragStart = {
                            isFaded = false
                            interactionTrigger++
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            currentOnOffsetChange(
                                IntOffset(
                                    (currentOffset.x + dragAmount.x).roundToInt(),
                                    (currentOffset.y + dragAmount.y).roundToInt()
                                )
                            )
                            interactionTrigger++
                        }
                    )
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    if (isFaded) {
                        isFaded = false
                        interactionTrigger++
                    } else {
                        onClick()
                        interactionTrigger++
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        // Main button body: Rounded Square
        Box(
            modifier = Modifier
                .size(54.dp) // Reduced from 60.dp (10% reduction)
                .clip(RoundedCornerShape(18.dp)) // Slightly adjusted corner radius
                .background(Color(0xFF121212))
                .border(2.dp, Color(0xFF363A45), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Shortcuts-like Icon (Double Diamond/Square)
            Canvas(modifier = Modifier.size(25.dp)) { // Reduced from 28.dp
                val strokeWidth = 2.25.dp.toPx() // Adjusted stroke
                val iconColor = Color(0xFF787B86)
                
                // Top diamond
                val topPath = Path().apply {
                    moveTo(size.width / 2, size.height * 0.1f)
                    lineTo(size.width * 0.9f, size.height * 0.4f)
                    lineTo(size.width / 2, size.height * 0.7f)
                    lineTo(size.width * 0.1f, size.height * 0.4f)
                    close()
                }

                // Bottom diamond (shifted down)
                val bottomPath = Path().apply {
                    moveTo(size.width / 2, size.height * 0.35f)
                    lineTo(size.width * 0.9f, size.height * 0.65f)
                    lineTo(size.width / 2, size.height * 0.95f)
                    lineTo(size.width * 0.1f, size.height * 0.65f)
                    close()
                }

                // Draw bottom first, then top
                drawPath(
                    path = bottomPath,
                    color = iconColor,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                
                drawPath(
                    path = topPath,
                    color = iconColor,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        // Lock Indicator
        if (isLocked) {
            Box(
                modifier = Modifier
                    .size(18.dp) // Slightly reduced
                    .align(Alignment.TopEnd)
                    .offset(x = (-3.6).dp, y = 3.6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2962FF))
                    .padding(3.6.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.White,
                    modifier = Modifier.size(10.8.dp)
                )
            }
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
    isLocked: Boolean = false,
    onLockToggle: () -> Unit = {},
    onClose: () -> Unit,
    offset: IntOffset,
    onOffsetChange: (IntOffset) -> Unit
) {
    val currentOffset by rememberUpdatedState(offset)
    val currentOnOffsetChange by rememberUpdatedState(onOffsetChange)

    Box(
        modifier = Modifier
            .offset { currentOffset }
            .pointerInput(isLocked) {
                if (!isLocked) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        currentOnOffsetChange(
                            IntOffset(
                                (currentOffset.x + dragAmount.x).roundToInt(),
                                (currentOffset.y + dragAmount.y).roundToInt()
                            )
                        )
                    }
                }
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
            
            // Lock Button Toggle
            QuickActionToggleRow(
                label = "Lock Button Position",
                isActive = isLocked,
                onToggle = onLockToggle
            )

            Spacer(modifier = Modifier.height(12.dp))
            
            // Custom row for "hide timezone pane"
            QuickActionToggleRow(
                label = if (isTimezoneVisible) "Hide Timezone Pane" else "Show Timezone Pane",
                isActive = isTimezoneVisible,
                onToggle = onTimezoneToggle
            )

            Spacer(modifier = Modifier.height(24.dp))
            
            // Drag handle footer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.DragIndicator,
                    contentDescription = null,
                    tint = Color(0xFF434651),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isLocked) "Button is pinned" else "Drag to move",
                    color = Color(0xFF434651),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun QuickActionToggleRow(
    label: String,
    isActive: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E222D))
            .clickable { onToggle() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Box(
            modifier = Modifier
                .size(36.dp, 20.dp)
                .clip(CircleShape)
                .background(if (isActive) Color(0xFF2962FF) else Color(0xFF434651))
                .padding(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(if (isActive) Alignment.CenterEnd else Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(Color.White)
            )
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
