package com.essy.nexa.ui.screens.events

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.ui.theme.*

@Composable
fun CheckInDoneScreen(navController: NavController) {
    val primary = NexaPrimary
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium))
    }

    Box(modifier = Modifier.fillMaxSize().background(NexaSecondary)) {
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(60.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(100.dp).scale(scale.value)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(64.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("You're Checked In!", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                Text("Attendance recorded successfully", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {

                    Text("Check-in Details", color = NexaTextDark, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    listOf(
                        Triple(Icons.Default.Event, "Event", "Annual Hackathon 2025"),
                        Triple(Icons.Default.AccessTime, "Check-in Time", "9:04 AM • Fri 16 May 2025"),
                        Triple(Icons.Default.LocationOn, "Location", "Innovation Hub, Block C"),
                        Triple(Icons.Default.ConfirmationNumber, "Attendee ID", "#NXA-2025-0087")
                    ).forEach { (icon, label, value) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(40.dp).background(NexaTagBg, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, null, tint = primary, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(label, color = NexaTextGrey, fontSize = 12.sp)
                                Text(value, color = NexaTextDark, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            }
                        }
                        Divider(color = NexaDivider)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                        colors = ButtonDefaults.buttonColors(containerColor = primary),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Icon(Icons.Default.Home, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Back to Home", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { navController.navigate("events") },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, primary)
                    ) {
                        Text("Browse More Events", color = primary, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckInDonePreview() {
    CheckInDoneScreen(rememberNavController())
}
