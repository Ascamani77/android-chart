package com.trading.app.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SideMenu(
    onClose: () -> Unit,
    onNavigate: (String) -> Unit = {}
) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onClose() }
        ) {
            // Sliding Menu Content
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.85f)
                    .background(Color(0xFF000000))
                    .clickable(enabled = false) { } // Prevent click-through to background
            ) {
                // MT5-style Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF000000))
                        .padding(top = 24.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // User Avatar / Logo
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E222D)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column {
                            Text(
                                "Nelson Ekomwenrenren",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "10010207420 - MetaQuotes-Demo",
                                color = Color(0xFF787B86),
                                fontSize = 13.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        "Manage accounts",
                        color = Color(0xFF2962FF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { onNavigate("Accounts") }
                    )
                }

                Divider(color = Color(0xFF1E222D), thickness = 1.dp)

                // Menu Items
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    MT5MenuItem(Icons.Outlined.Draw, "Drawings") { onNavigate("Drawings"); onClose() }
                    MT5MenuItem(Icons.Outlined.Hub, "Analysis hub") { onNavigate("Analysis"); onClose() }
                    MT5MenuItem(Icons.Outlined.Build, "Tools") { onNavigate("Tools"); onClose() }
                    
                    Divider(color = Color(0xFF1E222D), modifier = Modifier.padding(vertical = 8.dp))
                    
                    MT5MenuItem(Icons.Outlined.TrendingUp, "Trade")
                    MT5MenuItem(Icons.Outlined.Article, "News")
                    MT5MenuItem(Icons.Outlined.Mail, "Mailbox", badge = "8")
                    MT5MenuItem(Icons.Outlined.MenuBook, "Journal")
                    MT5MenuItem(Icons.Outlined.Settings, "Settings")
                    MT5MenuItem(Icons.Outlined.CalendarToday, "Economic calendar", hasAds = true)
                    
                    Divider(color = Color(0xFF1E222D), modifier = Modifier.padding(vertical = 8.dp))
                    
                    MT5MenuItem(Icons.Outlined.Groups, "Traders Community")
                    MT5MenuItem(Icons.Outlined.Send, "MQL5 Algo Trading")
                    
                    Divider(color = Color(0xFF1E222D), modifier = Modifier.padding(vertical = 8.dp))
                    
                    MT5MenuItem(Icons.Outlined.HelpOutline, "User guide")
                    MT5MenuItem(Icons.Outlined.Info, "About")
                    MT5MenuItem(Icons.Default.MoreHoriz, "More")
                }
            }
        }
    }
}

@Composable
fun MT5MenuItem(
    icon: ImageVector,
    label: String,
    badge: String? = null,
    hasAds: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            null,
            tint = Color(0xFFD1D4DC),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            label,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        
        if (badge != null) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF05252)),
                contentAlignment = Alignment.Center
            ) {
                Text(badge, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        if (hasAds) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF2962FF).copy(alpha = 0.2f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("Ads", color = Color(0xFF2962FF), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
