package com.essy.nexa.ui.screens.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.ui.theme.*

@Composable
fun EventDetailScreen(navController: NavController) {
    var isRsvped by remember { mutableStateOf(false) }
    val primary = NexaPrimary
    val event = eventList[0]

    Box(modifier = Modifier.fillMaxSize().background(primary)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header on coloured bg
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
                Column {
                    Surface(shape = RoundedCornerShape(6.dp), color = Color.White.copy(alpha = 0.25f)) {
                        Text(event.category, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(event.title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    Text("by ${event.organizerName}", color = NexaTextWhite80, fontSize = 13.sp)
                }
            }

            // White card
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp, 20.dp, 16.dp, 100.dp)
                ) {
                    // Info row
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            listOf(
                                Icons.Default.CalendarToday to event.date,
                                Icons.Default.AccessTime to event.time
                            ).forEach { (icon, value) ->
                                Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = NexaTagBg),
                                    elevation = CardDefaults.cardElevation(0.dp)) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(icon, null, tint = primary, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(value, color = NexaTextDark, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = NexaTagBg),
                            elevation = CardDefaults.cardElevation(0.dp)) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, null, tint = NexaSecondary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(event.location, color = NexaTextDark, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Text("About", color = NexaTextDark, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${event.description}\n\nThis is a flagship event for all students. Come with ideas, leave with connections and new skills. Refreshments will be provided.",
                            color = NexaTextGrey, fontSize = 14.sp, lineHeight = 22.sp)
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Who's Attending", color = NexaTextDark, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Text("${event.attendees} people", color = NexaTextGrey, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy((-10).dp)) {
                            listOf("A","B","G","K","F").forEach { letter ->
                                Box(modifier = Modifier.size(38.dp).clip(CircleShape).background(primary),
                                    contentAlignment = Alignment.Center) {
                                    Text(letter, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                            Box(modifier = Modifier.size(38.dp).clip(CircleShape).background(NexaTagBg),
                                contentAlignment = Alignment.Center) {
                                Text("+${event.attendees - 5}", color = primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Capacity", color = NexaTextGrey, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { event.attendees.toFloat() / event.maxAttendees },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = NexaSecondary, trackColor = NexaDivider
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }

                    item {
                        Button(
                            onClick = { isRsvped = !isRsvped },
                            colors = ButtonDefaults.buttonColors(containerColor = primary),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) {
                            Text(if (isRsvped) "✓ You're Going!" else "RSVP for This Event",
                                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        if (isRsvped) {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = { navController.navigate("qr_checkin") },
                                shape = RoundedCornerShape(50.dp),
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, primary)
                            ) {
                                Icon(Icons.Default.QrCodeScanner, null, tint = primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Check In with QR Code", color = primary, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventDetailPreview() {
    EventDetailScreen(rememberNavController())
}
