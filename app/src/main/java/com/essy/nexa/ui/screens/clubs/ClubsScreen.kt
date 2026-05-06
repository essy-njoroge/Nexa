package com.essy.nexa.ui.screens.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Club
import com.essy.nexa.ui.theme.*

val clubList = listOf(
    Club("1","Tech Club","Coding, hackathons, and tech talks for all skill levels","Technology",342,false,"💻"),
    Club("2","Business Club","Entrepreneurship, case studies, and competitions","Business",218,true,"📊"),
    Club("3","Drama Society","Theatre productions, improvisation, and acting workshops","Arts",156,false,"🎭"),
    Club("4","Photography Society","Visual storytelling, photo walks, and editing workshops","Arts",98,true,"📸"),
    Club("5","Environmental Club","Sustainability projects and campus clean-ups","Environment",187,false,"🌍"),
    Club("6","Sports Club","Multi-sport events, intramural leagues, and fitness","Sports",401,false,"⚽"),
    Club("7","Music Society","Live performances, open mic nights, and production","Arts",134,false,"🎵"),
    Club("8","Debate Society","Critical thinking, public speaking, and competitions","Academic",89,false,"🎤")
)
val clubCats = listOf("All","Technology","Business","Arts","Sports","Environment","Academic")

@Composable
fun ClubsScreen(navController: NavController) {
    var selectedCat by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    val primary = NexaPrimary

    val filtered = clubList.filter {
        (selectedCat == "All" || it.category == selectedCat) &&
        (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true))
    }

    Box(modifier = Modifier.fillMaxSize().background(primary)) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Clubs", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black)
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Add, contentDescription = "Create", tint = Color.White)
                }
            }

            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = searchQuery, onValueChange = { searchQuery = it },
                        placeholder = { Text("Search clubs...") },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = NexaTextGrey) },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp), singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(clubCats) { cat ->
                            val sel = selectedCat == cat
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = if (sel) primary else NexaTagBg,
                                modifier = Modifier.clickable { selectedCat = cat }
                            ) {
                                Text(cat, color = if (sel) Color.White else primary,
                                    fontWeight = FontWeight.Medium, fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filtered) { club ->
                            ClubCard(club = club, primary = primary, onClick = { navController.navigate("club_detail") })
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun ClubCard(club: Club, primary: Color, onClick: () -> Unit) {
    var joined by remember { mutableStateOf(club.isJoined) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = NexaTagBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(52.dp).background(primary.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(club.iconEmoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(club.name, color = NexaTextDark, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(3.dp))
                Text(club.description, color = NexaTextGrey, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.People, null, tint = NexaTextGrey, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${club.members} members", color = NexaTextGrey, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = { joined = !joined },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (joined) Color.White else primary,
                    contentColor = if (joined) primary else Color.White
                ),
                shape = RoundedCornerShape(50.dp),
                border = if (joined) androidx.compose.foundation.BorderStroke(1.dp, primary) else null,
                modifier = Modifier.width(80.dp).height(36.dp),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(if (joined) "Joined" else "Join", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClubsScreenPreview() {
    ClubsScreen(rememberNavController())
}
