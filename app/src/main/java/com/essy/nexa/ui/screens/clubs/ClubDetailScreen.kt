package com.essy.nexa.ui.screens.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

val clubAnnouncements = listOf(
    "📅 Next meeting: Wednesday 7PM — Room C102. Topic: Building REST APIs with Kotlin",
    "🏆 We came 2nd at the National Tech Olympiad! Proud of every member who participated!",
    "📢 Committee applications for 2025/26 are open. Apply by May 30th."
)

@Composable
fun ClubDetailScreen(navController: NavController) {
    val primary = NexaPrimary
    val club = clubList[0]
    var isJoined by remember { mutableStateOf(club.isJoined) }

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
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(club.iconEmoji + "  " + club.name, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text(club.category, color = NexaTextWhite80, fontSize = 13.sp)
                }
            }

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

                    // Stats
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            listOf(
                                "Members" to "${club.members}",
                                "Events" to "24",
                                "Active" to "Weekly"
                            ).forEach { (label, value) ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(value, color = primary, fontSize = 22.sp, fontWeight = FontWeight.Black)
                                    Text(label, color = NexaTextGrey, fontSize = 12.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = NexaDivider)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // About
                    item {
                        Text("About", color = NexaTextDark, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            club.description + "\n\nWe welcome students from all backgrounds and experience levels. Whether you're a beginner or expert, there's a place for you here.",
                            color = NexaTextGrey, fontSize = 14.sp, lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { isJoined = !isJoined },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isJoined) NexaSecondary else primary),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Icon(
                                if (isJoined) Icons.Default.Chat else Icons.Default.GroupAdd,
                                null, tint = Color.White, modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (isJoined) "✓ Joined — Open Group Chat" else "Join ${club.name}",
                                color = Color.White, fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Recent Announcements", color = NexaTextDark, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    items(clubAnnouncements) { announcement ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = NexaTagBg),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Row(modifier = Modifier.padding(14.dp)) {
                                Box(
                                    modifier = Modifier.size(36.dp).clip(CircleShape).background(primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(club.iconEmoji, fontSize = 16.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(club.name, color = NexaTextDark, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(announcement, color = NexaTextGrey, fontSize = 13.sp, lineHeight = 20.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClubDetailPreview() {
    ClubDetailScreen(rememberNavController())
}
