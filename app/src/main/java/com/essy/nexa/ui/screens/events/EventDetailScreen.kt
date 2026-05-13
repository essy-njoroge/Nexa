package com.essy.nexa.ui.screens.events

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

// ─── Colors ───────────────────────────────────────────────────────────────────
private val DeepMidnight = Color(0xFF06050F)
private val CardBg       = Color(0xFF100E1A)
private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val VioletDeep   = Color(0xFF7B2FFF)
private val TealGreen    = Color(0xFF00D4AA)
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.45f)
private val CardBorder   = White.copy(alpha = 0.07f)

private val FireGradient = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))

// ─── Radial orb ───────────────────────────────────────────────────────────────
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRadialOrb(
    center: Offset, radius: Float, color: Color, strength: Float = 1f
) {
    drawCircle(
        brush = Brush.radialGradient(
            listOf(
                color.copy(alpha = 0.35f * strength), color.copy(alpha = 0.15f * strength),
                color.copy(alpha = 0.05f * strength), Color.Transparent
            ),
            center = center, radius = radius
        ),
        radius = radius, center = center
    )
}

// ─── Background ───────────────────────────────────────────────────────────────
@Composable
private fun EventDetailBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val o1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")

    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val gs = 36.dp.toPx(); val gc = HotPink.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
        var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx, 0f), Offset(gx, h), lw); gx += gs }
        var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f, gy), Offset(w, gy), lw); gy += gs }
        drawRadialOrb(Offset(w * 0.15f + o1 * 20.dp.toPx(), h * 0.08f), 180.dp.toPx(), HotPink)
        drawRadialOrb(Offset(w * 0.85f, h * 0.4f - o1 * 15.dp.toPx()), 150.dp.toPx(), VioletDeep, 0.7f)
    })
}

// ─── Corner brackets ──────────────────────────────────────────────────────────
@Composable
private fun CornerBrackets() {
    Box(modifier = Modifier.fillMaxSize()) {
        val sw = 1.5.dp
        Box(modifier = Modifier.align(Alignment.TopStart).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = HotPink.copy(alpha = 0.45f)
            drawLine(c, Offset(s, 0f), Offset(0f, 0f), w, cap = StrokeCap.Square)
            drawLine(c, Offset(0f, 0f), Offset(0f, s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = HotPink.copy(alpha = 0.45f)
            drawLine(c, Offset(0f, 0f), Offset(s, 0f), w, cap = StrokeCap.Square)
            drawLine(c, Offset(s, 0f), Offset(s, s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.BottomStart).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = GoldYellow.copy(alpha = 0.45f)
            drawLine(c, Offset(0f, s), Offset(s, s), w, cap = StrokeCap.Square)
            drawLine(c, Offset(0f, 0f), Offset(0f, s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = GoldYellow.copy(alpha = 0.45f)
            drawLine(c, Offset(0f, s), Offset(s, s), w, cap = StrokeCap.Square)
            drawLine(c, Offset(s, 0f), Offset(s, s), w, cap = StrokeCap.Square)
        })
    }
}

// ─── Info chip ────────────────────────────────────────────────────────────────
@Composable
private fun InfoChip(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(White.copy(alpha = 0.05f))
            .border(1.dp, White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = HotPink, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, color = White.copy(alpha = 0.85f), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ─── Section header ───────────────────────────────────────────────────────────
@Composable
private fun SectionHeader(title: String, trailing: String = "") {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(3.dp).height(16.dp).clip(CircleShape).background(FireGradient))
        Spacer(Modifier.width(8.dp))
        Text(title, color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        if (trailing.isNotEmpty()) {
            Spacer(Modifier.weight(1f))
            Text(trailing, color = TextMuted, fontSize = 12.sp)
        }
    }
}

// ─── EventDetailScreen ────────────────────────────────────────────────────────
// FIX 1: Accept eventId so we load the right event, not always index 0
@Composable
fun EventDetailScreen(navController: NavController, eventId: String?) {
    val db  = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    // FIX 1: Load the event from Firestore by ID; fall back to hardcoded list if offline
    var event    by remember { mutableStateOf<Event?>(eventList.find { it.id == eventId } ?: eventList[0]) }
    var isLoading by remember { mutableStateOf(true) }

    // FIX 2: isRsvped loaded from Firestore — persists across sessions
    var isRsvped  by remember { mutableStateOf(false) }
    var isRsvping by remember { mutableStateOf(false) } // prevents double-tap

    // Load event + RSVP state from Firestore
    LaunchedEffect(eventId) {
        if (eventId == null) { isLoading = false; return@LaunchedEffect }

        // Load event document
        db.collection("events").document(eventId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    event = Event(
                        id            = doc.id,
                        title         = doc.getString("title") ?: "",
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
                }
                isLoading = false
            }
            .addOnFailureListener { isLoading = false }

        // FIX 2: Check if current user already RSVPed
        if (uid != null) {
            db.collection("events").document(eventId)
                .collection("rsvps").document(uid).get()
                .addOnSuccessListener { doc -> isRsvped = doc.exists() }
        }
    }

    // FIX 2: Toggle RSVP in Firestore + update attendee count atomically
    fun toggleRsvp() {
        val ev = event ?: return
        if (uid == null || isRsvping) return
        isRsvping = true

        val rsvpRef = db.collection("events").document(ev.id)
            .collection("rsvps").document(uid)
        val eventRef = db.collection("events").document(ev.id)

        if (isRsvped) {
            // Un-RSVP
            rsvpRef.delete().addOnSuccessListener {
                eventRef.update("attendees", FieldValue.increment(-1))
                isRsvped  = false
                isRsvping = false
                event = ev.copy(attendees = (ev.attendees - 1).coerceAtLeast(0))
            }.addOnFailureListener { isRsvping = false }
        } else {
            // RSVP
            rsvpRef.set(mapOf("uid" to uid, "timestamp" to FieldValue.serverTimestamp()))
                .addOnSuccessListener {
                    eventRef.update("attendees", FieldValue.increment(1))
                    isRsvped  = true
                    isRsvping = false
                    event = ev.copy(attendees = ev.attendees + 1)
                }.addOnFailureListener { isRsvping = false }
        }
    }

    val ev = event ?: return

    // FIX 4: category color uses original case — matches your category values exactly
    val categoryColor = eventCategoryColor(ev.category)

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        EventDetailBackground()
        CornerBrackets()

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = HotPink, strokeWidth = 2.dp, modifier = Modifier.size(32.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 110.dp)
            ) {

                // ── Top bar ──
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(White.copy(alpha = 0.06f))
                                .border(1.dp, White.copy(alpha = 0.10f), CircleShape)
                                .clickable { navController.popBackStack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ArrowBack, null, tint = White, modifier = Modifier.size(18.dp))
                        }

                        Spacer(Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(categoryColor.copy(alpha = 0.12f))
                                    .border(1.dp, categoryColor.copy(alpha = 0.30f), CircleShape)
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                            ) {
                                Text(ev.category, color = categoryColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(ev.title, color = White, fontSize = 20.sp, fontWeight = FontWeight.Black, lineHeight = 24.sp)
                            Text("by ${ev.organizerName}", color = TextMuted, fontSize = 12.sp)
                        }
                    }
                }

                // ── Hero banner ──
                item {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(listOf(
                                    HotPink.copy(alpha = 0.30f), VioletDeep.copy(alpha = 0.30f), GoldYellow.copy(alpha = 0.20f)
                                ))
                            )
                            .border(1.dp, White.copy(alpha = 0.08f), RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.fillMaxSize().drawBehind {
                            drawRadialOrb(Offset(size.width * 0.3f, size.height * 0.4f), size.width * 0.5f, HotPink, 0.8f)
                            drawRadialOrb(Offset(size.width * 0.75f, size.height * 0.6f), size.width * 0.4f, GoldYellow, 0.6f)
                        })
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎯", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(ev.title, color = White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }

                // ── Date / Time / Location chips ──
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            InfoChip(Icons.Default.CalendarToday, ev.date, Modifier.weight(1f))
                            InfoChip(Icons.Default.AccessTime, ev.time, Modifier.weight(1f))
                        }
                        InfoChip(Icons.Default.LocationOn, ev.location, Modifier.fillMaxWidth())
                    }
                    Spacer(Modifier.height(24.dp))
                }

                // ── About ──
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SectionHeader("About This Event")
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(CardBg)
                                .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                "${ev.description}\n\nThis is a flagship event for all students. Come with ideas, leave with connections and new skills. Refreshments will be provided.",
                                color = White.copy(alpha = 0.70f),
                                fontSize = 14.sp,
                                lineHeight = 24.sp
                            )
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }

                // ── Attendees ──
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SectionHeader("Who's Attending", "${ev.attendees} people")
                        Spacer(Modifier.height(14.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy((-10).dp)) {
                            listOf("A", "B", "G", "K", "F").forEachIndexed { i, letter ->
                                val colors = listOf(
                                    listOf(HotPink, BlazeOrange),
                                    listOf(BlazeOrange, GoldYellow),
                                    listOf(VioletDeep, HotPink),
                                    listOf(GoldYellow, BlazeOrange),
                                    listOf(HotPink, VioletDeep)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(colors[i]))
                                        .border(2.dp, DeepMidnight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(letter, color = White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                }
                            }
                            if (ev.attendees > 5) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(White.copy(alpha = 0.08f))
                                        .border(2.dp, DeepMidnight, CircleShape)
                                        .border(1.dp, White.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("+${ev.attendees - 5}", color = GoldYellow, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Capacity", color = TextMuted, fontSize = 12.sp)
                            Spacer(Modifier.weight(1f))
                            Text("${ev.attendees}/${ev.maxAttendees}", color = GoldYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(White.copy(alpha = 0.08f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth((ev.attendees.toFloat() / ev.maxAttendees).coerceIn(0f, 1f))
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Brush.horizontalGradient(listOf(HotPink, GoldYellow)))
                            )
                        }
                    }
                    Spacer(Modifier.height(28.dp))
                }

                // ── RSVP buttons ──
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // FIX 2: calls toggleRsvp() which writes to Firestore
                        Button(
                            onClick = { toggleRsvp() },
                            enabled = !isRsvping,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            shape = RoundedCornerShape(50.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        if (isRsvped)
                                            Brush.linearGradient(listOf(TealGreen, Color(0xFF00A87C)))
                                        else
                                            FireGradient,
                                        RoundedCornerShape(50.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isRsvping) {
                                    CircularProgressIndicator(color = White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                                } else {
                                    Text(
                                        text = if (isRsvped) "✓  You're Going!" else "RSVP for This Event",
                                        color = White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        letterSpacing = 0.3.sp
                                    )
                                }
                            }
                        }

                        // FIX 3: pass eventId to qr_checkin so it knows which event
                        if (isRsvped) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(HotPink.copy(alpha = 0.08f))
                                    .border(
                                        1.5.dp,
                                        Brush.horizontalGradient(listOf(HotPink.copy(alpha = 0.50f), GoldYellow.copy(alpha = 0.50f))),
                                        RoundedCornerShape(50.dp)
                                    )
                                    .clickable { navController.navigate("qr_checkin/${ev.id}") },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.QrCodeScanner, null, tint = HotPink, modifier = Modifier.size(18.dp))
                                    Text("Check In with QR Code", color = HotPink, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun EventDetailPreview() {
    EventDetailScreen(rememberNavController(), eventId = "1")
}