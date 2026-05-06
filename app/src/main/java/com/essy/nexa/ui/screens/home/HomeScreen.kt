package com.essy.nexa.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class FeedPost(
    val id: String = "",
    val authorName: String = "",
    val authorCourse: String = "",
    val content: String = "",
    val timestamp: String = "",
    val likes: Int = 0,
    val comments: Int = 0,
    val isLiked: Boolean = false,
    val category: String = ""
)

val demoFeedPosts = listOf(
    FeedPost("1","Aisha Kamau","CS Year 3","🚀 Just got my internship at a top tech firm through Nexa's recruiter feature. Fill your profile — it works!","2h ago",84,12,false,"Career"),
    FeedPost("2","Brian Otieno","Business Y2","Hackathon this Friday at Innovation Hub 🛠️ Teams of 3-5, theme: FinTech for Africa. RSVP in Events tab!","4h ago",56,23,true,"Event"),
    FeedPost("3","Nexa AI ✨","Smart Feed","Based on your interests, here are 3 events happening this week that match your profile 👇","AI",0,0,false,"AI"),
    FeedPost("4","Grace Wanjiru","Architecture Y4","Study group for design finals — tomorrow 6PM at the library. DM me 📚","6h ago",31,18,false,"Study"),
    FeedPost("5","TechClub 💻","Official Club","Weekly coding session TODAY at 5PM, Room B204. Topic: Jetpack Compose. All levels welcome!","1h ago",102,34,true,"Club")
)

val feedFilters = listOf("All","Events","Clubs","Career","Study","AI")

@Composable
fun HomeScreen(navController: NavController) {
    val teal = NexaPrimary
    var selectedFilter by remember { mutableStateOf("All") }
    var userName by remember { mutableStateOf("there") }

    // Load user name from Firestore
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    val name = doc.getString("name") ?: "there"
                    userName = name.split(" ").firstOrNull() ?: name
                }
        }
    }

    val filteredPosts = if (selectedFilter == "All") demoFeedPosts
    else demoFeedPosts.filter { it.category == selectedFilter }

    Box(modifier = Modifier.fillMaxSize().background(teal)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top bar on teal ─────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Good morning 👋", color = NexaTextWhite80, fontSize = 13.sp)
                    Text("Nexa", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { navController.navigate("ai_assistant") },
                        modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) { Icon(Icons.Default.AutoAwesome, null, tint = Color.White) }
                    IconButton(
                        onClick = { navController.navigate("notifications") },
                        modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) { Icon(Icons.Default.Notifications, null, tint = Color.White) }
                }
            }

            // ── White card ───────────────────────────────────────────────
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    // AI Banner
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(14.dp)).background(teal)
                                .clickable { navController.navigate("ai_assistant") }.padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("AI Smart Feed Active", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("3 events match your interests today", color = NexaTextWhite80, fontSize = 12.sp)
                                }
                                Icon(Icons.Default.ArrowForward, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // ── Quick Action Buttons (WORKING) ───────────────────
                    item {
                        Text("Quick Actions", color = NexaTextDark, fontWeight = FontWeight.Bold,
                            fontSize = 15.sp, modifier = Modifier.padding(horizontal = 16.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            QuickActionBtn("Events",    Icons.Default.Event,       teal) { navController.navigate("events") }
                            QuickActionBtn("AI Chat",   Icons.Default.AutoAwesome, teal) { navController.navigate("ai_assistant") }
                            QuickActionBtn("Jobs",      Icons.Default.Work,        teal) { navController.navigate("opportunities") }
                            QuickActionBtn("Study",     Icons.Default.MenuBook,    teal) { navController.navigate("study_group") }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            QuickActionBtn("Lost & Found", Icons.Default.FindInPage, teal) { navController.navigate("lost_found") }
                            QuickActionBtn("Timetable", Icons.Default.Schedule,    teal) { navController.navigate("timetable") }
                            QuickActionBtn("Board",     Icons.Default.Leaderboard, teal) { navController.navigate("leaderboard") }
                            QuickActionBtn("Clubs",     Icons.Default.Groups,      teal) { navController.navigate("clubs") }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // ── Filter Chips (WORKING) ────────────────────────────
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(feedFilters) { filter ->
                                val sel = selectedFilter == filter
                                Surface(
                                    shape = RoundedCornerShape(50.dp),
                                    color = if (sel) teal else NexaTagBg,
                                    modifier = Modifier.clickable { selectedFilter = filter }
                                ) {
                                    Text(filter, color = if (sel) Color.White else teal,
                                        fontWeight = FontWeight.Medium, fontSize = 13.sp,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // ── Feed Posts ────────────────────────────────────────
                    item {
                        Text("Campus Feed 🔥", color = NexaTextDark, fontWeight = FontWeight.Bold,
                            fontSize = 15.sp, modifier = Modifier.padding(horizontal = 16.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(filteredPosts) { post ->
                        FeedPostCard(post = post, teal = teal,
                            onEventClick = { navController.navigate("event_detail") })
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        // FAB
        Box(modifier = Modifier.fillMaxSize().padding(end = 16.dp, bottom = 90.dp), contentAlignment = Alignment.BottomEnd) {
            FloatingActionButton(
                onClick = { navController.navigate("create_post") },
                containerColor = teal, shape = RoundedCornerShape(16.dp)
            ) { Icon(Icons.Default.Add, null, tint = Color.White) }
        }
    }
}

@Composable
fun RowScope.QuickActionBtn(label: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Column(
        modifier = Modifier.weight(1f)
            .background(NexaTagBg, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = NexaTextDark, fontWeight = FontWeight.Medium,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun FeedPostCard(post: FeedPost, teal: Color, onEventClick: () -> Unit = {}) {
    var liked by remember { mutableStateOf(post.isLiked) }
    var likeCount by remember { mutableIntStateOf(post.likes) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = NexaTagBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(teal),
                    contentAlignment = Alignment.Center) {
                    Text(post.authorName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(post.authorName, color = NexaTextDark, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(post.authorCourse, color = NexaTextGrey, fontSize = 12.sp)
                }
                Surface(shape = RoundedCornerShape(6.dp), color = teal.copy(alpha = 0.15f)) {
                    Text(post.category, color = teal, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(post.content, color = NexaTextDark, fontSize = 14.sp, lineHeight = 20.sp)

            if (post.category == "Event") {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = onEventClick, shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(1.dp, teal)) {
                    Text("View Event", color = teal, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = NexaDivider)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    null, tint = if (liked) NexaAccent else NexaTextGrey,
                    modifier = Modifier.size(18.dp).clickable {
                        liked = !liked; likeCount = if (liked) likeCount + 1 else likeCount - 1
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("$likeCount", color = NexaTextGrey, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Outlined.ChatBubbleOutline, null, tint = NexaTextGrey, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${post.comments}", color = NexaTextGrey, fontSize = 13.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(post.timestamp, color = NexaTextGrey, fontSize = 11.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() { HomeScreen(rememberNavController()) }
