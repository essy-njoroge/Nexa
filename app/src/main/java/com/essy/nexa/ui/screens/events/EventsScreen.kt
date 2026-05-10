package com.essy.nexa.ui.screens.events

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// ─── Colors ───────────────────────────────────────────────────────────────────
private val DeepMidnight = Color(0xFF06050F)
private val DarkSurface  = Color(0xFF0D0918)
private val CardBg       = Color(0xFF100E1A)
private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val VioletDeep   = Color(0xFF7B2FFF)
private val TealGreen    = Color(0xFF00D4AA)
private val CobaltBlue   = Color(0xFF00A3FF)
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.45f)
private val CardBorder   = White.copy(alpha = 0.07f)

private val FireGradient = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))

// ─── Category → color ─────────────────────────────────────────────────────────
fun eventCategoryColor(category: String) = when (category) {
    "Tech"      -> HotPink
    "Career"    -> GoldYellow
    "Arts"      -> Color(0xFFEC4899)
    "Wellness"  -> TealGreen
    "Business"  -> VioletDeep
    "Sports"    -> CobaltBlue
    else        -> BlazeOrange
}

// ─── Hardcoded fallback list (shown when Firestore is empty / offline) ────────
val eventList = listOf(
    Event("1","Annual Hackathon 2025","Build solutions for Africa's challenges","Innovation Hub, Block C","Fri 16 May","9:00 AM","Tech",87,120,"TechClub",false),
    Event("2","Career Fair — Tech Edition","Meet top companies actively hiring","Main Auditorium","Mon 19 May","10:00 AM","Career",230,300,"Career Office",true),
    Event("3","AI & ML Workshop","Hands-on intro to ML with Python","Lab 3, ICT Block","Wed 21 May","2:00 PM","Tech",45,50,"CS Dept",false),
    Event("4","Mental Health Awareness","Panel discussion + free counselling","Student Centre","Thu 22 May","11:00 AM","Wellness",60,200,"Student Affairs",true),
    Event("5","Business Pitch Competition","Pitch your startup idea to real investors","Board Room","Sat 24 May","1:00 PM","Business",34,40,"Business Club",false),
    Event("6","Photography Walk","Campus photo tour for all skill levels","Main Gate","Sun 25 May","7:00 AM","Arts",22,30,"Photo Society",false),
    Event("7","Music Night 🎶","Live performances by campus bands","Amphitheatre","Fri 16 May","7:00 PM","Arts",90,150,"Music Society",false),
    Event("8","Startup Networking","Meet founders, investors and mentors","Innovation Hub","Sat 17 May","3:00 PM","Business",55,80,"Startup Club",false)
)

val eventFilters = listOf("All","Today","This Week","Tech","Career","Arts","Wellness","Business")

// ─── Radial orb ───────────────────────────────────────────────────────────────
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRadialOrb(
    center: Offset, radius: Float, color: Color, strength: Float = 1f
) {
    drawCircle(
        brush = Brush.radialGradient(
            listOf(
                color.copy(alpha = 0.35f * strength), color.copy(alpha = 0.14f * strength),
                color.copy(alpha = 0.04f * strength), Color.Transparent
            ),
            center = center, radius = radius
        ),
        radius = radius, center = center
    )
}

// ─── Background ───────────────────────────────────────────────────────────────
@Composable
private fun EventsBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val o1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")

    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val gs = 36.dp.toPx(); val gc = HotPink.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
        var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx, 0f), Offset(gx, h), lw); gx += gs }
        var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f, gy), Offset(w, gy), lw); gy += gs }
        drawRadialOrb(Offset(w * 0.1f + o1 * 20.dp.toPx(), h * 0.06f), 180.dp.toPx(), HotPink)
        drawRadialOrb(Offset(w * 0.88f, h * 0.45f + o1 * 15.dp.toPx()), 150.dp.toPx(), VioletDeep, 0.7f)
    })
}

// ─── Corner brackets ──────────────────────────────────────────────────────────
@Composable
private fun CornerBrackets() {
    Box(modifier = Modifier.fillMaxSize()) {
        val sw = 1.5.dp
        Box(modifier = Modifier.align(Alignment.TopStart).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = HotPink.copy(alpha = 0.45f)
            drawLine(c, Offset(s,0f), Offset(0f,0f), w, cap = StrokeCap.Square)
            drawLine(c, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = HotPink.copy(alpha = 0.45f)
            drawLine(c, Offset(0f,0f), Offset(s,0f), w, cap = StrokeCap.Square)
            drawLine(c, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.BottomStart).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = GoldYellow.copy(alpha = 0.45f)
            drawLine(c, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square)
            drawLine(c, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = GoldYellow.copy(alpha = 0.45f)
            drawLine(c, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square)
            drawLine(c, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square)
        })
    }
}

// ─── Event card ───────────────────────────────────────────────────────────────
@Composable
fun EventCard(event: Event, onClick: () -> Unit) {
    val catColor = eventCategoryColor(event.category)
    val fillFraction = (event.attendees.toFloat() / event.maxAttendees).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CardBg)
            .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        // Top row: category tag + RSVP badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(catColor.copy(alpha = 0.12f))
                    .border(1.dp, catColor.copy(alpha = 0.28f), CircleShape)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(event.category, color = catColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
            Spacer(Modifier.weight(1f))
            if (event.isRsvped) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(TealGreen.copy(alpha = 0.10f))
                        .border(1.dp, TealGreen.copy(alpha = 0.25f), CircleShape)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = TealGreen, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("RSVP'd", color = TealGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(event.title, color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text(event.description, color = TextMuted, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 18.sp)

        Spacer(Modifier.height(12.dp))

        // Date / time / attendees row
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, null, tint = catColor.copy(alpha = 0.70f), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text(event.date, color = TextMuted, fontSize = 11.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, null, tint = catColor.copy(alpha = 0.70f), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text(event.time, color = TextMuted, fontSize = 11.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.People, null, tint = catColor.copy(alpha = 0.70f), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text("${event.attendees}/${event.maxAttendees}", color = TextMuted, fontSize = 11.sp)
            }
        }

        Spacer(Modifier.height(10.dp))

        // Capacity bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(White.copy(alpha = 0.07f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fillFraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(2.dp))
                    .background(Brush.horizontalGradient(listOf(catColor, catColor.copy(alpha = 0.60f))))
            )
        }
    }
}

// ─── EventsScreen ─────────────────────────────────────────────────────────────
@Composable
fun EventsScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery    by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    // Live Firestore events — merges with hardcoded fallback
    var firestoreEvents by remember { mutableStateOf<List<Event>>(emptyList()) }
    var isLoading       by remember { mutableStateOf(true) }

    // Check if current user is admin
    var isAdmin by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    isAdmin = doc.getBoolean("isAdmin") ?: false
                }
        }

        // Real-time listener on "events" collection
        FirebaseFirestore.getInstance()
            .collection("events")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                isLoading = false
                if (snapshot != null) {
                    firestoreEvents = snapshot.documents.mapNotNull { doc ->
                        try {
                            Event(
                                id            = doc.id,
                                title         = doc.getString("title") ?: return@mapNotNull null,
                                description   = doc.getString("description") ?: "",
                                location      = doc.getString("location") ?: "",
                                date          = doc.getString("date") ?: "",
                                time          = doc.getString("time") ?: "",
                                category      = doc.getString("category") ?: "General",
                                attendees     = (doc.getLong("attendees") ?: 0).toInt(),
                                maxAttendees  = (doc.getLong("maxAttendees") ?: 100).toInt(),
                                organizerName = doc.getString("organizerName") ?: "",
                                isRsvped      = false
                            )
                        } catch (e: Exception) { null }
                    }
                }
            }
    }

    // Merge: Firestore events first, then hardcoded fallback
    val allEvents = if (firestoreEvents.isNotEmpty()) firestoreEvents else eventList

    val filtered = allEvents.filter { event ->
        val matchesFilter = when (selectedFilter) {
            "All"       -> true
            "Today"     -> event.date.contains("16 May")
            "This Week" -> true
            else        -> event.category == selectedFilter
        }
        val matchesSearch = searchQuery.isEmpty() ||
                event.title.contains(searchQuery, ignoreCase = true) ||
                event.location.contains(searchQuery, ignoreCase = true) ||
                event.category.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        EventsBackground()
        CornerBrackets()

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top bar ──
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Events", color = White, fontSize = 28.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Text("${filtered.size} happening on campus", color = TextMuted, fontSize = 12.sp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Search toggle
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isSearchActive) HotPink.copy(alpha = 0.15f) else White.copy(alpha = 0.06f))
                            .border(1.dp, if (isSearchActive) HotPink.copy(alpha = 0.35f) else White.copy(alpha = 0.10f), CircleShape)
                            .clickable { isSearchActive = !isSearchActive; if (!isSearchActive) searchQuery = "" },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Search, null, tint = if (isSearchActive) HotPink else White.copy(alpha = 0.80f), modifier = Modifier.size(18.dp))
                    }

                    // Admin only: add event button
                    if (isAdmin) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(FireGradient)
                                .clickable { navController.navigate("create_event") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, null, tint = White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Search bar (collapsible) ──
            if (isSearchActive) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search events...", color = TextMuted, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = HotPink, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, null, tint = TextMuted, modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HotPink.copy(alpha = 0.55f),
                        unfocusedBorderColor = White.copy(alpha = 0.10f),
                        focusedContainerColor = White.copy(alpha = 0.04f),
                        unfocusedContainerColor = White.copy(alpha = 0.04f),
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        cursorColor = HotPink
                    )
                )
                Spacer(Modifier.height(12.dp))
            }

            // ── Filter chips ──
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(eventFilters) { f ->
                    val isSel = selectedFilter == f
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSel) FireGradient
                                else Brush.linearGradient(listOf(White.copy(alpha = 0.06f), White.copy(alpha = 0.06f)))
                            )
                            .border(1.dp, if (isSel) Color.Transparent else White.copy(alpha = 0.10f), CircleShape)
                            .clickable { selectedFilter = f }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            f,
                            color = if (isSel) White else TextMuted,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Event list ──
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = HotPink, strokeWidth = 2.dp, modifier = Modifier.size(32.dp))
                }
            } else if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("🔍", fontSize = 40.sp)
                        Text("No events found", color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Try a different filter or search term", color = TextMuted, fontSize = 13.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filtered, key = { it.id }) { event ->
                        EventCard(event = event, onClick = { navController.navigate("event_detail") })
                    }
                    item { Spacer(Modifier.height(90.dp)) }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun EventsScreenPreview() { EventsScreen(rememberNavController()) }