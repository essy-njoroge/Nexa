package com.essy.nexa.ui.screens.clubs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Club
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

// ─── Hardcoded fallback ───────────────────────────────────────────────────────
val clubListFallback = listOf(
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

// ─── Colors ───────────────────────────────────────────────────────────────────
private val DeepMidnight = Color(0xFF06050F)
private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.45f)

@Composable
fun ClubsScreen(navController: NavController) {
    val db  = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var selectedCat    by remember { mutableStateOf("All") }
    var searchQuery    by remember { mutableStateOf("") }
    var firestoreClubs by remember { mutableStateOf<List<Club>>(emptyList()) }
    var joinedClubIds  by remember { mutableStateOf<Set<String>>(emptySet()) }
    var isLoading      by remember { mutableStateOf(true) }

    val gradient = Brush.horizontalGradient(listOf(HotPink, BlazeOrange, GoldYellow))

    // FIX 1: Load clubs from Firestore + track which ones user has joined
    DisposableEffect(uid) {
        // Load user's joined clubs
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    @Suppress("UNCHECKED_CAST")
                    val joined = (doc.get("joinedClubs") as? List<String>)?.toSet() ?: emptySet()
                    joinedClubIds = joined
                }
        }

        val listener: ListenerRegistration = db.collection("clubs")
            .addSnapshotListener { snapshot, _ ->
                isLoading = false
                if (snapshot != null) {
                    firestoreClubs = snapshot.documents.mapNotNull { doc ->
                        try {
                            Club(
                                id          = doc.id,
                                name        = doc.getString("name") ?: return@mapNotNull null,
                                description = doc.getString("description") ?: "",
                                category    = doc.getString("category") ?: "General",
                                members     = (doc.getLong("members") ?: 0).toInt(),
                                isJoined    = false, // resolved from joinedClubIds below
                                iconEmoji   = doc.getString("iconEmoji") ?: "🏛️"
                            )
                        } catch (e: Exception) { null }
                    }
                }
            }

        onDispose { listener.remove() }
    }

    val allClubs = if (firestoreClubs.isNotEmpty()) firestoreClubs else clubListFallback

    val filtered = allClubs.filter {
        (selectedCat == "All" || it.category == selectedCat) &&
                (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true))
    }

    // FIX 3: Toggle join in Firestore — updates member count + user's joinedClubs list
    fun toggleJoin(club: Club) {
        if (uid == null) return
        val isCurrentlyJoined = joinedClubIds.contains(club.id)
        val clubRef  = db.collection("clubs").document(club.id)
        val userRef  = db.collection("users").document(uid)

        if (isCurrentlyJoined) {
            clubRef.update("members", FieldValue.increment(-1))
            userRef.update("joinedClubs", FieldValue.arrayRemove(club.id))
            joinedClubIds = joinedClubIds - club.id
        } else {
            clubRef.update("members", FieldValue.increment(1))
            userRef.update("joinedClubs", FieldValue.arrayUnion(club.id))
            joinedClubIds = joinedClubIds + club.id
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {

            Spacer(Modifier.height(60.dp))

            // ── Header ────────────────────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Discover\nClubs", color = White, fontSize = 38.sp, lineHeight = 42.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.height(6.dp))
                    Text("Find communities that match your interests", color = White.copy(alpha = 0.55f), fontSize = 13.sp)
                }
                Box(
                    modifier = Modifier.size(54.dp).clip(CircleShape).background(White.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Groups, null, tint = White, modifier = Modifier.size(28.dp))
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Search ────────────────────────────────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search clubs...", color = White.copy(alpha = 0.4f)) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = White.copy(alpha = 0.5f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor        = White,
                    unfocusedTextColor      = White,
                    focusedBorderColor      = HotPink.copy(alpha = 0.5f),
                    unfocusedBorderColor    = White.copy(alpha = 0.08f),
                    focusedContainerColor   = White.copy(alpha = 0.04f),
                    unfocusedContainerColor = White.copy(alpha = 0.04f),
                    cursorColor             = HotPink
                )
            )

            Spacer(Modifier.height(18.dp))

            // ── Filters ───────────────────────────────────────────────────────
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(clubCats) { cat ->
                    val selected = selectedCat == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (selected) gradient
                                else Brush.horizontalGradient(listOf(White.copy(alpha = 0.05f), White.copy(alpha = 0.05f)))
                            )
                            .clickable { selectedCat = cat }
                            .padding(horizontal = 18.dp, vertical = 10.dp)
                    ) {
                        Text(cat, color = if (selected) White else White.copy(alpha = 0.7f), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Club list ─────────────────────────────────────────────────────
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = HotPink, strokeWidth = 2.dp, modifier = Modifier.size(32.dp))
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {
                    items(filtered, key = { it.id }) { club ->
                        ClubCard(
                            club      = club,
                            gradient  = gradient,
                            isJoined  = joinedClubIds.contains(club.id),  // FIX 3: from Firestore
                            onJoin    = { toggleJoin(club) },
                            // FIX 2: pass club.id to both buttons
                            onClick   = { navController.navigate("club_detail/${club.id}") },
                        )
                    }
                }
            }
        }
    }
}

// ─── Club card ────────────────────────────────────────────────────────────────
@Composable
fun ClubCard(
    club: Club,
    gradient: Brush,
    isJoined: Boolean,       // FIX 3: passed in from parent, not local state
    onJoin: () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(White.copy(alpha = 0.05f))
            .border(1.dp, White.copy(alpha = 0.07f), RoundedCornerShape(28.dp))
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(68.dp).clip(RoundedCornerShape(22.dp)).background(gradient),
                    contentAlignment = Alignment.Center
                ) {
                    Text(club.iconEmoji, fontSize = 30.sp)
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(club.name, color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(club.category, color = HotPink, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Groups, null, tint = White.copy(alpha = 0.45f), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(5.dp))
                        Text("${club.members} members", color = White.copy(alpha = 0.45f), fontSize = 12.sp)
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(club.description, color = White.copy(alpha = 0.7f), fontSize = 13.sp, lineHeight = 21.sp, maxLines = 3, overflow = TextOverflow.Ellipsis)

            Spacer(Modifier.height(18.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // FIX 3: calls onJoin which writes to Firestore
                Button(
                    onClick = { onJoin() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, if (isJoined) HotPink else White.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isJoined) "✓ Joined" else "Join Club", color = White, fontWeight = FontWeight.Bold)
                }

                // FIX 2: View button navigates to detail
                Button(
                    onClick = { onClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.weight(1f).background(gradient, RoundedCornerShape(50.dp))
                ) {
                    Text("View", color = White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClubsScreenPreview() { ClubsScreen(rememberNavController()) }