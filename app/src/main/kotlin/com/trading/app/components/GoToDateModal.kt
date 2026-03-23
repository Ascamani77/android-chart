package com.trading.app.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GoToDateModal(
    onClose: () -> Unit,
    onGoTo: (Long) -> Unit
) {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var hour by remember { mutableStateOf("09") }
    var minute by remember { mutableStateOf("30") }

    Dialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.95f),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF1E222D)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Go to", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, null, tint = Color(0xFF787B86))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date Selection (Simplified)
                Text("Date", color = Color(0xFF787B86), fontSize = 12.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF363A45), RoundedCornerShape(4.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        SimpleDateFormat("MMM dd, yyyy", Locale.US).format(selectedDate.time),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.DateRange, null, tint = Color(0xFF787B86), modifier = Modifier.size(16.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Time Selection
                Text("Time", color = Color(0xFF787B86), fontSize = 12.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = hour,
                        onValueChange = { if (it.length <= 2) hour = it },
                        modifier = Modifier.width(64.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2962FF),
                            unfocusedBorderColor = Color(0xFF363A45),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Text(" : ", color = Color.White, modifier = Modifier.padding(horizontal = 4.dp))
                    OutlinedTextField(
                        value = minute,
                        onValueChange = { if (it.length <= 2) minute = it },
                        modifier = Modifier.width(64.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2962FF),
                            unfocusedBorderColor = Color(0xFF363A45),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val cal = selectedDate.clone() as Calendar
                        cal.set(Calendar.HOUR_OF_DAY, hour.toIntOrNull() ?: 0)
                        cal.set(Calendar.MINUTE, minute.toIntOrNull() ?: 0)
                        onGoTo(cal.timeInMillis)
                        onClose()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF)),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Go to", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
