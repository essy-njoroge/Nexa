package com.essy.nexa.ui.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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

data class LeaderboardEntry(
    val name: String,
    val course: String,
    val points: Int,
    val eventsAttended: Int,
    val postsCreated: Int,
    val badge: String
)

val leaderboard = listOf(
    LeaderboardEntry("Aisha Kamau","Computer Science",1540,24,18,"🏆"),
    LeaderboardEntry("Brian Otieno","Business",1320,20,22,"🥈"),
    LeaderboardEntry("Grace Wanjiru","Architecture",1210,18,15,"🥉"),
    LeaderboardEntry("Kevin Mwangi","Computer Science",980,14,8,"⭐"),
    LeaderboardEntry("Fatima Ahmed","Medicine",870,12,11,"⭐"),
    LeaderboardEntry("James Njoroge","Engineering",760,10,9,"⭐"),
    LeaderboardEntry("Rita Mwende","Law",650,8,14,"⭐"),
    LeaderboardEntry("Tom Kariuki","Economics",540,7,6,"⭐"),
    LeaderboardEntry("Sarah Wangui","Education",430,6,8,"⭐"),
    LeaderboardEntry("Mark Odhiambo","Agriculture",320,4,5,"⭐")
)

@Composable
fun LeaderboardScreen(navController: NavController) {
    val teal = NexaPrimary

    Box(modifier = Modifier.fillMaxSize().background(teal)) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Leaderboard 🏆", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Top students this semester", color = NexaTextWhite80, fontSize = 12.sp)
                }
            }

            // Top 3 podium
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.Bottom) {
                // 2nd place
                PodiumItem(entry = leaderboard[1], rank = 2, height = 80, teal = teal)
                // 1st place
                PodiumItem(entry = leaderboard[0], rank = 1, height = 110, teal = teal)
                // 3rd place
                PodiumItem(entry = leaderboard[2], rank = 3, height = 60, teal = teal)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Points key
                    Card(shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = NexaTagBg),
                        elevation = CardDefaults.cardElevation(0.dp),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        Row(modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround) {
                            listOf("Event = 50pts","Post = 20pts","Like = 5pts").forEach { hint ->
                                Text(hint, color = teal, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)) {
                        itemsIndexed(leaderboard.drop(3)) { index, entry ->
                            LeaderboardRow(entry = entry, rank = index + 4, teal = teal, isMe = entry.name == "Kevin Mwangi")
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumItem(entry: LeaderboardEntry, rank: Int, height: Int, teal: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(entry.badge, fontSize = if (rank == 1) 28.sp else 22.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.size(if (rank == 1) 56.dp else 46.dp).clip(CircleShape)
            .background(Color.White.copy(alpha = 0.25f)), contentAlignment = Alignment.Center) {
            Text(entry.name.take(1), color = Color.White, fontWeight = FontWeight.Black,
                fontSize = if (rank == 1) 24.sp else 18.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(entry.name.split(" ").first(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Text("${entry.points}pts", color = NexaTextWhite80, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.width(70.dp).height(height.dp)
            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
            contentAlignment = Alignment.Center) {
            Text("#$rank", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
        }
    }
}

@Composable
fun LeaderboardRow(entry: LeaderboardEntry, rank: Int, teal: Color, isMe: Boolean) {
    Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (isMe) teal.copy(alpha = 0.08f) else NexaTagBg),
        elevation = CardDefaults.cardElevation(0.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("#$rank", color = if (isMe) teal else NexaTextGrey,
                fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.width(32.dp))
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isMe) teal else NexaTextGrey.copy(0.2f)),
                contentAlignment = Alignment.Center) {
                Text(entry.name.take(1), color = if (isMe) Color.White else NexaTextDark,
                    fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.name, color = if (isMe) teal else NexaTextDark,
                    fontWeight = if (isMe) FontWeight.Bold else FontWeight.SemiBold, fontSize = 14.sp)
                Text(entry.course, color = NexaTextGrey, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${entry.points}", color = if (isMe) teal else NexaTextDark,
                    fontWeight = FontWeight.Black, fontSize = 16.sp)
                Text("points", color = NexaTextGrey, fontSize = 11.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardPreview() { LeaderboardScreen(rememberNavController()) }
