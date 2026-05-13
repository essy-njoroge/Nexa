package com.essy.nexa.ui.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// ───────────────── COLORS ─────────────────
private val DeepMidnight = Color(0xFF06050F)
private val DarkSurface  = Color(0xFF0D0918)
private val CardBg       = Color(0xFF100E1A)

private val HotPink     = Color(0xFFFF2D9B)
private val BlazeOrange = Color(0xFFFF6400)
private val GoldYellow  = Color(0xFFFFB300)
private val VioletDeep  = Color(0xFF7B2FFF)
private val TealGreen   = Color(0xFF00D4AA)
private val CobaltBlue  = Color(0xFF00A3FF)

private val White      = Color.White
private val TextMuted  = White.copy(alpha = 0.45f)
private val CardBorder = White.copy(alpha = 0.07f)

private val FireGradient = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))

// ───────────────── DATA ─────────────────
data class FeedPost(
    val id: String        = "",
    val uid: String       = "",
    val authorName: String = "Anonymous",
    val content: String   = "",
    val timestamp: String = "",
    val likes: Int        = 0,
    val tag: String       = "General"
)

val feedFilters = listOf("All", "General", "Tech", "Career", "Wellness", "Arts", "Business", "Sports", "Announcement")

private fun tagColor(tag: String) = when (tag) {
    "Tech"         -> HotPink
    "Career"       -> GoldYellow
    "Arts"         -> Color(0xFFEC4899)
    "Wellness"     -> TealGreen
    "Business"     -> VioletDeep
    "Sports"       -> CobaltBlue
    "Announcement" -> BlazeOrange
    else           -> White.copy(alpha = 0.55f)
}

private data class QuickAction(val label: String, val icon: ImageVector, val route: String)

private val quickActions = listOf(
    QuickAction("Events",    Icons.Default.Event,       "events"),
    QuickAction("AI Chat",   Icons.Default.AutoAwesome, "ai_assistant"),
    QuickAction("Jobs",      Icons.Default.Work,        "opportunities"),
    QuickAction("Study",     Icons.Default.MenuBook,    "study_group"),
    QuickAction("Timetable", Icons.Default.Schedule,    "timetable"),
    QuickAction("Clubs",     Icons.Default.Groups,      "clubs")
)

// ───────────────── BACKGROUND ─────────────────
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRadialOrb(
    center: Offset, radius: Float, color: Color
) {
    drawCircle(
        brush = Brush.radialGradient(
            listOf(color.copy(alpha = 0.30f), color.copy(alpha = 0.12f), Color.Transparent),
            center = center, radius = radius
        ),
        radius = radius, center = center
    )
}

@Composable
private fun NexaBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val move by inf.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(9000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "move"
    )
    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        drawRadialOrb(Offset(w * 0.15f, h * 0.10f), 260.dp.toPx(), HotPink)
        drawRadialOrb(Offset(w * 0.9f - move * 80f, h * 0.35f), 220.dp.toPx(), VioletDeep)
    })
}

// ───────────────── TOP BAR ─────────────────
@Composable
private fun HomeTopBar(userName: String, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Good morning 👋", fontSize = 12.sp, color = TextMuted)
            Text("Hey, $userName", fontSize = 22.sp, fontWeight = FontWeight.Black, color = White)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(34.dp).clip(CircleShape).background(FireGradient)
                    .clickable { navController.navigate("ai_assistant") },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(1.5.dp).clip(CircleShape).background(DarkSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = HotPink, modifier = Modifier.size(16.dp))
                }
            }
            Box(
                modifier = Modifier.size(34.dp).clip(CircleShape)
                    .background(White.copy(alpha = 0.06f))
                    .border(1.dp, White.copy(alpha = 0.10f), CircleShape)
                    .clickable { navController.navigate("notifications") },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Notifications, null, tint = White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
            }
        }
    }
}

// ───────────────── QUICK ACTIONS ─────────────────
@Composable
private fun QuickActionsGrid(navController: NavController) {
    val rows = quickActions.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { action ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(White.copy(alpha = 0.05f))
                            .border(1.dp, White.copy(alpha = 0.08f), RoundedCornerShape(18.dp))
                            .clickable { navController.navigate(action.route) }
                            .padding(vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(action.icon, null, tint = HotPink, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(action.label, color = White, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

// ───────────────── FILTERS ─────────────────
@Composable
private fun FilterChips(selected: String, onSelect: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(feedFilters) { filter ->
            val isSelected = selected == filter
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (isSelected) FireGradient
                        else Brush.linearGradient(listOf(White.copy(alpha = 0.06f), White.copy(alpha = 0.06f)))
                    )
                    .clickable { onSelect(filter) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(filter, color = White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ───────────────── POST CARD ─────────────────
@Composable
fun FeedPostCard(post: FeedPost) {
    val tc = tagColor(post.tag)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(CardBg)
            .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(FireGradient),
                contentAlignment = Alignment.Center
            ) {
                Text(post.authorName.take(1).uppercase(), color = White, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(post.authorName, color = White, fontWeight = FontWeight.Bold)
                Text(post.timestamp, color = TextMuted, fontSize = 11.sp)
            }
            // Tag chip
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(tc.copy(alpha = 0.12f))
                    .border(1.dp, tc.copy(alpha = 0.28f), CircleShape)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(post.tag, color = tc, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(14.dp))
        Text(post.content, color = White.copy(alpha = 0.88f), lineHeight = 22.sp)
        Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.FavoriteBorder, null, tint = TextMuted, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(5.dp))
            Text("${post.likes}", color = TextMuted, fontSize = 12.sp)
            Spacer(Modifier.weight(1f))
        }
    }
}

// ───────────────── EMPTY STATE ─────────────────
@Composable
private fun EmptyFeed(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(White.copy(alpha = 0.05f))
                .border(1.dp, White.copy(alpha = 0.08f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Article, null, tint = HotPink.copy(alpha = 0.6f), modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text("No posts yet", color = White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(6.dp))
        Text(
            "Be the first to share something with the campus!",
            color = TextMuted,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(FireGradient)
                .clickable { navController.navigate("create_post") }
                .padding(horizontal = 24.dp, vertical = 10.dp)
        ) {
            Text("Create a Post", color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

// ───────────────── HOME SCREEN ─────────────────
@Composable
fun HomeScreen(navController: NavController) {

    var selectedFilter by remember { mutableStateOf("All") }
    var userName       by remember { mutableStateOf("there") }
    var posts          by remember { mutableStateOf<List<FeedPost>>(emptyList()) }
    var isLoading      by remember { mutableStateOf(true) }

    val db  = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    // ── Fetch user's display name once ──
    LaunchedEffect(Unit) {
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    val name = doc.getString("name") ?: "there"
                    userName = name.split(" ").firstOrNull() ?: name
                }
        }
    }

    // ── Real-time Firestore listener for posts ──
    // This re-runs whenever selectedFilter changes so the query updates too.
    DisposableEffect(selectedFilter) {
        isLoading = true

        val query: Query = if (selectedFilter == "All") {
            db.collection("posts").orderBy("createdAt", Query.Direction.DESCENDING)
        } else {
            db.collection("posts")
                .whereEqualTo("tag", selectedFilter)
                .orderBy("createdAt", Query.Direction.DESCENDING)
        }

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                isLoading = false
                return@addSnapshotListener
            }

            // For each post, we also look up the author name from the users collection.
            // We build a list and resolve names async, then update state.
            val rawPosts = snapshot.documents.mapNotNull { doc ->
                val postUid   = doc.getString("uid") ?: return@mapNotNull null
                val content   = doc.getString("content") ?: return@mapNotNull null
                val tag       = doc.getString("tag") ?: "General"
                val likes     = (doc.getLong("likes") ?: 0L).toInt()
                val createdAt = doc.getTimestamp("createdAt")
                val timeStr   = if (createdAt != null) {
                    val diffMs  = System.currentTimeMillis() - createdAt.toDate().time
                    val diffMin = diffMs / 60_000
                    when {
                        diffMin < 1   -> "Just now"
                        diffMin < 60  -> "${diffMin}m ago"
                        diffMin < 1440 -> "${diffMin / 60}h ago"
                        else           -> "${diffMin / 1440}d ago"
                    }
                } else "Just now"

                FeedPost(
                    id        = doc.id,
                    uid       = postUid,
                    content   = content,
                    tag       = tag,
                    likes     = likes,
                    timestamp = timeStr,
                    authorName = "Loading…"   // placeholder while we resolve name
                )
            }

            if (rawPosts.isEmpty()) {
                posts = emptyList()
                isLoading = false
                return@addSnapshotListener
            }

            // Resolve author names in bulk
            val resolved = rawPosts.toMutableList()
            var pending  = rawPosts.size

            rawPosts.forEachIndexed { index, post ->
                db.collection("users").document(post.uid).get()
                    .addOnSuccessListener { userDoc ->
                        val name = userDoc.getString("name") ?: "Anonymous"
                        resolved[index] = post.copy(authorName = name)
                        pending--
                        if (pending == 0) {
                            posts     = resolved.toList()
                            isLoading = false
                        }
                    }
                    .addOnFailureListener {
                        resolved[index] = post.copy(authorName = "Anonymous")
                        pending--
                        if (pending == 0) {
                            posts     = resolved.toList()
                            isLoading = false
                        }
                    }
            }
        }

        onDispose { listener.remove() }
    }

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {

        NexaBackground()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            item { HomeTopBar(userName, navController) }

            item {
                Spacer(Modifier.height(10.dp))
                Box(modifier = Modifier.padding(horizontal = 20.dp)) { QuickActionsGrid(navController) }
                Spacer(Modifier.height(22.dp))
            }

            item {
                FilterChips(selected = selectedFilter, onSelect = { selectedFilter = it })
                Spacer(Modifier.height(20.dp))
            }

            // Loading indicator
            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = HotPink, strokeWidth = 2.dp, modifier = Modifier.size(32.dp))
                    }
                }
            } else if (posts.isEmpty()) {
                // Empty state
                item { EmptyFeed(navController) }
            } else {
                items(posts, key = { it.id }) { post ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) { FeedPostCard(post) }
                    Spacer(Modifier.height(14.dp))
                }
            }
        }

        // FAB
        Box(
            modifier = Modifier.fillMaxSize().padding(end = 20.dp, bottom = 96.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(FireGradient)
                    .clickable { navController.navigate("create_post") },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, null, tint = White, modifier = Modifier.size(28.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun HomeScreenPreview() { HomeScreen(rememberNavController()) }