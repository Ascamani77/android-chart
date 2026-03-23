package com.trading.app.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Sidebar(
    activeTool: String?,
    onToolClick: (String) -> Unit,
    onToolSearchClick: () -> Unit,
    stayInDrawingMode: Boolean,
    onStayInModeToggle: () -> Unit,
    isMagnetEnabled: Boolean,
    onMagnetToggle: () -> Unit,
    isLocked: Boolean,
    onLockToggle: () -> Unit,
    isVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    onClearDrawings: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .width(52.dp)
            .fillMaxHeight()
            .background(Color(0xFF08090C))
    ) {
        // Right separator line
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(Color(0xFF2A2E39))
                .align(Alignment.CenterEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 1.dp)
                .verticalScroll(scrollState)
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Crosshair (Square background when active)
            SidebarToolIconItem(
                icon = Icons.Default.Add,
                toolId = "cursor",
                isActive = activeTool == "cursor",
                onClick = { onToolClick("cursor") },
                isSquare = true,
                hasSubMenu = false
            )
            
            // 2. Trend Line
            SidebarToolIconItem(icon = Icons.Outlined.Timeline, toolId = "trendline", isActive = activeTool == "trendline", onClick = onToolClick)
            
            // 3. Gann and Fibonacci
            SidebarToolIconItem(icon = Icons.Outlined.Notes, toolId = "fib", isActive = activeTool == "fib", onClick = onToolClick)
            
            // 4. Geometric Shapes
            SidebarToolIconItem(icon = Icons.Outlined.Polyline, toolId = "shapes", isActive = activeTool == "shapes", onClick = onToolClick)
            
            // 5. Prediction/Measurement
            SidebarToolIconItem(icon = Icons.Outlined.AutoGraph, toolId = "prediction", isActive = activeTool == "prediction", onClick = onToolClick)
            
            // 6. Brush
            SidebarToolIconItem(icon = Icons.Outlined.Brush, toolId = "brush", isActive = activeTool == "brush", onClick = onToolClick)
            
            // 7. Text
            SidebarToolIconItem(icon = Icons.Outlined.Title, toolId = "text", isActive = activeTool == "text", onClick = onToolClick)
            
            // 8. Icons (Smiley)
            SidebarToolIconItem(icon = Icons.Outlined.SentimentSatisfied, toolId = "icons", isActive = activeTool == "icons", onClick = onToolClick)

            SidebarDivider()

            // 9. Measure (Ruler)
            SidebarToolIconItem(icon = Icons.Outlined.Straighten, toolId = "measure", isActive = activeTool == "measure", onClick = onToolClick, hasSubMenu = false)
            
            // 10. Zoom
            SidebarToolIconItem(icon = Icons.Outlined.ZoomIn, toolId = "zoom", isActive = activeTool == "zoom", onClick = onToolClick, hasSubMenu = false)
            
            SidebarDivider()

            // 11. Magnet
            SidebarToolIconItem(
                icon = Icons.Outlined.FlashOn,
                toolId = "magnet",
                isActive = isMagnetEnabled,
                onClick = { onMagnetToggle() },
                hasSubMenu = false
            )
            
            // 12. Stay in Drawing Mode (Pencil with lock)
            SidebarToolIconItem(
                icon = if (stayInDrawingMode) Icons.Default.Edit else Icons.Outlined.Edit,
                toolId = "stay",
                isActive = stayInDrawingMode,
                onClick = { onStayInModeToggle() },
                hasSubMenu = false
            )
            
            // 13. Lock (Padlock)
            SidebarToolIconItem(
                icon = if (isLocked) Icons.Default.Lock else Icons.Outlined.Lock,
                toolId = "lock",
                isActive = isLocked,
                onClick = { onLockToggle() },
                hasSubMenu = false
            )
            
            // 14. Hide (Eye with Brush)
            SidebarToolIconItem(
                icon = if (isVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                toolId = "hide",
                isActive = !isVisible,
                onClick = { onVisibilityToggle() },
                hasSubMenu = false
            )
            
            SidebarDivider()

            // 15. Remove (Bin)
            SidebarToolIconItem(
                icon = Icons.Outlined.Delete,
                toolId = "remove",
                isActive = false,
                onClick = { onClearDrawings() },
                hasSubMenu = false
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun SidebarDivider() {
    Divider(
        modifier = Modifier
            .width(32.dp)
            .padding(vertical = 4.dp),
        color = Color(0xFF2A2E39),
        thickness = 1.dp
    )
}

@Composable
fun SidebarToolIconItem(
    icon: ImageVector,
    toolId: String,
    isActive: Boolean,
    onClick: (String) -> Unit,
    hasSubMenu: Boolean = true,
    isSquare: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(if (isSquare) 8.dp else 4.dp))
            .background(if (isActive) Color(0xFF2A2E39) else Color.Transparent)
            .clickable { onClick(toolId) },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = toolId,
            tint = if (isActive) Color.White else Color(0xFFD1D4DC),
            modifier = Modifier.size(24.dp)
        )
        
        if (hasSubMenu && !isActive) {
            // Tiny arrow indicator
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 2.dp, end = 2.dp)
            ) {
                Canvas(modifier = Modifier.size(3.dp)) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(size.width, size.height)
                        lineTo(size.width, 0f)
                        lineTo(0f, size.height)
                        close()
                    }
                    drawPath(path, color = Color(0xFF787B86))
                }
            }
        }
    }
}
