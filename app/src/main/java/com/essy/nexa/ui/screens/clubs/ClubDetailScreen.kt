package com.essy.nexa.ui.screens.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

// ─── Colors ───────────────────────────────────────────────────────────────────
private val DeepMidnight = Color(0xFF06050F)
private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val SoftWhite    = Color(0xFFF5F7FF)
private val White        = Color.White

// ─── ClubDetailScreen ─────────────────────────────────────────────────────────
// FIX 1: Accept clubId so we load the right club
@Composable
fun ClubDetailScreen(navController: NavController, clubId: String?) {
    val db  = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    // FIX 1: Load real club from Firestore; fall back to hardcoded list
    var club          by remember { mutableStateOf<Club?>(clubListFallback.find { it.id == clubId } ?: clubListFallback[0]) }
    var isJoined      by remember { mutableStateOf(false) }
    var isJoining     by remember { mutableStateOf(false) }
    var announcements by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading     by remember { mutableStateOf(true) }

    val gradientBrush = Brush.horizontalGradient(listOf(HotPink, BlazeOrange, GoldYellow))

    // FIX 1 + 3 + 4: Load club, check join status, load announcements with cleanup
    DisposableEffect(clubId) {
        if (clubId == null) { isLoading = false; return@DisposableEffect onDispose {} }

        // Load club document
        db.collection("clubs").document(clubId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    club = Club(
                        id          = doc.id,
                        name        = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        category    = doc.getString("category") ?: "General",
                        members     = (doc.getLong("members") ?: 0).toInt(),
                        isJoined    = false,
                        iconEmoji   = doc.getString("iconEmoji") ?: "🏛️"
                    )
                }
                isLoading = false
            }
            .addOnFailureListener { isLoading = false }

        // FIX 2: Check if user has joined this club
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    @Suppress("UNCHECKED_CAST")
                    val joined = (doc.get("joinedClubs") as? List<String>) ?: emptyList()
                    isJoined = joined.contains(clubId)
                }
        }

        // FIX 3: Load announcements from Firestore subcollection
        val announcementsListener: ListenerRegistration = db.collection("clubs")
            .document(clubId)
            .collection("announcements")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    announcements = snapshot.documents.mapNotNull { it.getString("text") }
                }
                // Fallback if no announcements in Firestore yet
                if (announcements.isEmpty()) {
                    announcements = listOf(
                        "📅 Next meeting: Wednesday 7PM — Room C102",
                        "🏆 Check the noticeboard for upcoming events",
                        "📢 New members welcome — introduce yourself in the group chat"
                    )
                }
            }

        // FIX 4: Remove listener on dispose
        onDispose { announcementsListener.remove() }
    }

    // FIX 2: Toggle join — persists to Firestore
    fun toggleJoin() {
        val cl = club ?: return
        if (uid == null || isJoining) return
        isJoining = true

        val clubRef = db.collection("clubs").document(cl.id)
        val userRef = db.collection("users").document(uid)

        if (isJoined) {
            clubRef.update("members", FieldValue.increment(-1))
            userRef.update("joinedClubs", FieldValue.arrayRemove(cl.id))
                .addOnSuccessListener { isJoined = false; isJoining = false }
                .addOnFailureListener { isJoining = false }
        } else {
            clubRef.update("members", FieldValue.increment(1))
            userRef.update("joinedClubs", FieldValue.arrayUnion(cl.id))
                .addOnSuccessListener { isJoined = true; isJoining = false }
                .addOnFailureListener { isJoining = false }
        }
    }

    val cl = club ?: return

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        // Glow effects
        Box(modifier = Modifier.size(260.dp).offset(x = (-60).dp, y = (-40).dp).background(HotPink.copy(alpha = 0.18f), CircleShape))
        Box(modifier = Modifier.size(240.dp).offset(x = 220.dp, y = 120.dp).background(BlazeOrange.copy(alpha = 0.14f), CircleShape))

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(42.dp).clip(CircleShape).background(White.copy(alpha = 0.08f))
                ) {
                    Icon(Icons.Default.ArrowBack, null, tint = White)
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    // FIX 1: real club name
                    Text("${cl.iconEmoji} ${cl.name}", color = White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.height(2.dp))
                    Text(cl.category, color = White.copy(alpha = 0.55f), fontSize = 13.sp)
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = HotPink, strokeWidth = 2.dp, modifier = Modifier.size(32.dp))
                }
            } else {
                // ── Main content ──────────────────────────────────────────────
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp))
                        .background(SoftWhite.copy(alpha = 0.05f))
                        .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp)),
                    contentPadding = PaddingValues(20.dp, 24.dp, 20.dp, 120.dp)
                ) {

                    // Hero card
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(28.dp))
                                .background(gradientBrush)
                                .padding(24.dp)
                        ) {
                            Column {
                                Text(cl.iconEmoji, fontSize = 48.sp)
                                Spacer(Modifier.height(12.dp))
                                Text(cl.name, color = White, fontSize = 26.sp, fontWeight = FontWeight.Black)
                                Spacer(Modifier.height(8.dp))
                                Text(cl.description, color = White.copy(alpha = 0.9f), fontSize = 14.sp, lineHeight = 22.sp)
                                Spacer(Modifier.height(20.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    ClubStatCard("Members", "${cl.members}")
                                    ClubStatCard("Events", "24")
                                    ClubStatCard("Active", "Weekly")
                                }
                            }
                        }
                        Spacer(Modifier.height(22.dp))
                    }

                    // FIX 2: Join button writes to Firestore
                    item {
                        Button(
                            onClick = { toggleJoin() },
                            enabled = !isJoining,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.fillMaxWidth().height(58.dp),
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        if (isJoined) Brush.horizontalGradient(listOf(Color(0xFF00C853), Color(0xFF64DD17)))
                                        else gradientBrush,
                                        RoundedCornerShape(50.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isJoining) {
                                    CircularProgressIndicator(color = White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                                } else {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(if (isJoined) Icons.Default.Check else Icons.Default.GroupAdd, null, tint = White)
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            if (isJoined) "Joined Successfully" else "Join ${cl.name}",
                                            color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(28.dp))
                    }

                    // About
                    item {
                        Text("About Club", color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .background(White.copy(alpha = 0.05f))
                                .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(22.dp))
                                .padding(18.dp)
                        ) {
                            Text(
                                "${cl.description}\n\nWe welcome students from all backgrounds and experience levels. Whether you're a beginner or expert, there's a place for you here.",
                                color = White.copy(alpha = 0.78f), fontSize = 14.sp, lineHeight = 24.sp
                            )
                        }
                        Spacer(Modifier.height(28.dp))
                    }

                    // Announcements title
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Campaign, null, tint = HotPink)
                            Spacer(Modifier.width(8.dp))
                            Text("Recent Announcements", color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(16.dp))
                    }

                    // FIX 3: Real announcements from Firestore
                    items(announcements) { announcement ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 14.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(White.copy(alpha = 0.05f))
                                .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(22.dp))
                                .padding(16.dp)
                        ) {
                            Row {
                                Box(
                                    modifier = Modifier.size(48.dp).clip(CircleShape).background(gradientBrush),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(cl.iconEmoji, fontSize = 20.sp)
                                }
                                Spacer(Modifier.width(14.dp))
                                Column {
                                    Text(cl.name, color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(Modifier.height(6.dp))
                                    Text(announcement, color = White.copy(alpha = 0.72f), fontSize = 13.sp, lineHeight = 22.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClubStatCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(White.copy(alpha = 0.16f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, color = White, fontWeight = FontWeight.Black, fontSize = 18.sp)
        Spacer(Modifier.height(4.dp))
        Text(title, color = White.copy(alpha = 0.75f), fontSize = 11.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ClubDetailPreview() { ClubDetailScreen(rememberNavController(), clubId = "1") }