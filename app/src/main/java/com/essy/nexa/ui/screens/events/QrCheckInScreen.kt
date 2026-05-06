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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun QrCheckInScreen(navController: NavController) {
    var scanned by remember { mutableStateOf(false) }
    val primary = NexaPrimary
    val scanLine = remember { Animatable(0f) }

    LaunchedEffect(scanned) {
        if (!scanned) {
            while (!scanned) {
                scanLine.animateTo(1f, tween(1800, easing = LinearEasing))
                scanLine.snapTo(0f)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(primary)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("QR Check-In", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            // White card
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Event info mini card
                    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = NexaTagBg),
                        elevation = CardDefaults.cardElevation(0.dp)) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Event, null, tint = primary, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Annual Hackathon 2025", color = NexaTextDark, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Fri 16 May • Innovation Hub", color = NexaTextGrey, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Scanner box
                    Box(
                        modifier = Modifier.size(240.dp).clip(RoundedCornerShape(20.dp))
                            .background(NexaTagBg),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!scanned) {
                            Box(modifier = Modifier.fillMaxWidth().height(2.dp)
                                .offset(y = (240 * scanLine.value).dp)
                                .background(primary))
                            Icon(Icons.Default.QrCode, null, tint = NexaTextGrey.copy(alpha = 0.3f), modifier = Modifier.size(100.dp))
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CheckCircle, null, tint = NexaSecondary, modifier = Modifier.size(64.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        if (scanned) "✅ Check-in Successful!" else "Point camera at QR code",
                        color = NexaTextDark, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center
                    )
                    Text(
                        if (scanned) "Attendance recorded 🎉" else "QR code is shown at the event entrance",
                        color = NexaTextGrey, fontSize = 13.sp, textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if (!scanned) {
                        Button(
                            onClick = { scanned = true },
                            colors = ButtonDefaults.buttonColors(containerColor = primary),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Icon(Icons.Default.QrCodeScanner, null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Simulate Scan (Demo)", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { navController.navigate("checkin_done") },
                            colors = ButtonDefaults.buttonColors(containerColor = NexaSecondary),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("View Confirmation", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QrCheckInPreview() {
    QrCheckInScreen(rememberNavController())
}
