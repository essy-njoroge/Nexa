package com.essy.nexa.ui.screens.events

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

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
private val TealGradient = Brush.linearGradient(listOf(TealGreen, Color(0xFF00A87C)))

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
private fun CheckInDoneBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val o1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")

    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val gs = 36.dp.toPx(); val gc = TealGreen.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
        var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += gs }
        var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += gs }
        drawRadialOrb(Offset(w * 0.1f + o1 * 20.dp.toPx(), h * 0.06f), 200.dp.toPx(), TealGreen, 0.8f)
        drawRadialOrb(Offset(w * 0.88f, h * 0.45f + o1 * 15.dp.toPx()), 150.dp.toPx(), VioletDeep, 0.6f)
    })
}

// ─── Corner brackets ──────────────────────────────────────────────────────────
@Composable
private fun CornerBrackets() {
    Box(modifier = Modifier.fillMaxSize()) {
        val sw = 1.5.dp
        Box(modifier = Modifier.align(Alignment.TopStart).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = TealGreen.copy(alpha = 0.55f)
            drawLine(c, Offset(s,0f), Offset(0f,0f), w, cap = StrokeCap.Square)
            drawLine(c, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = TealGreen.copy(alpha = 0.55f)
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

// ─── Detail row ───────────────────────────────────────────────────────────────
@Composable
fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    valueColor: Color = White
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(White.copy(alpha = 0.06f))
                .border(1.dp, White.copy(alpha = 0.07f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = HotPink, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(title, color = TextMuted, fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ─── CheckInDoneScreen ────────────────────────────────────────────────────────
// FIX 1: Accept eventId to load real event data
@Composable
fun CheckInDoneScreen(navController: NavController, eventId: String?) {
    val db  = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    // FIX 1: Load real event
    var event by remember { mutableStateOf<Event?>(eventList.find { it.id == eventId } ?: eventList[0]) }

    // FIX 2: Real check-in timestamp from Firestore
    var checkInTime by remember { mutableStateOf("") }

    // FIX 3: Attendee ID derived from uid (short, readable)
    val attendeeId = remember(uid) {
        if (uid != null) "#NXA-${uid.takeLast(6).uppercase()}" else "#NXA-GUEST"
    }

    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
    }

    // FIX 1 + 2: Load event and real check-in timestamp
    LaunchedEffect(eventId) {
        if (eventId == null || uid == null) return@LaunchedEffect

        // Load event
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
            }

        // FIX 2: Read real check-in timestamp
        db.collection("events").document(eventId)
            .collection("checkins").document(uid).get()
            .addOnSuccessListener { doc ->
                val ts = doc.getTimestamp("timestamp")
                checkInTime = if (ts != null) {
                    val sdf = SimpleDateFormat("h:mm a • EEE d MMM yyyy", Locale.ENGLISH)
                    sdf.format(ts.toDate())
                } else {
                    // Fallback: use current time if timestamp not written yet
                    SimpleDateFormat("h:mm a • EEE d MMM yyyy", Locale.ENGLISH).format(Date())
                }
            }
    }

    val ev = event ?: return

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        CheckInDoneBackground()
        CornerBrackets()

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(70.dp))

            // ── Success icon ──
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(scale.value)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(listOf(
                                TealGreen.copy(alpha = 0.35f),
                                TealGreen.copy(alpha = 0.10f),
                                Color.Transparent
                            ))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(CircleShape)
                            .background(TealGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Check, null, tint = White, modifier = Modifier.size(52.dp))
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text("You're In 🎉", color = White, fontSize = 34.sp, fontWeight = FontWeight.Black)

                Spacer(Modifier.height(10.dp))

                Text(
                    "Your attendance has been successfully recorded.",
                    color = White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            Spacer(Modifier.height(36.dp))

            // ── Details card ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardBg)
                    .border(1.dp, CardBorder, RoundedCornerShape(24.dp))
                    .padding(22.dp)
            ) {
                Text(
                    "CHECK-IN DETAILS",
                    color = TealGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(Modifier.height(22.dp))

                // FIX 1: real event title
                DetailRow(Icons.Default.Event, "Event", ev.title)
                Spacer(Modifier.height(18.dp))

                // FIX 2: real check-in timestamp
                DetailRow(
                    Icons.Default.Schedule,
                    "Check-in Time",
                    checkInTime.ifEmpty { "Confirming..." }
                )
                Spacer(Modifier.height(18.dp))

                // FIX 1: real venue
                DetailRow(Icons.Default.LocationOn, "Venue", ev.location)
                Spacer(Modifier.height(18.dp))

                // FIX 3: uid-derived attendee ID
                DetailRow(
                    Icons.Default.Badge,
                    "Attendee ID",
                    attendeeId,
                    valueColor = GoldYellow
                )
            }

            Spacer(Modifier.weight(1f))

            // ── Buttons ──
            Button(
                onClick = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(FireGradient, RoundedCornerShape(50.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Home, null, tint = White)
                        Spacer(Modifier.width(8.dp))
                        Text("Back To Home", color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            OutlinedButton(
                onClick = { navController.navigate("events") },
                border = BorderStroke(1.dp, White.copy(alpha = 0.12f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = White),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Browse More Events", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(34.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun CheckInDonePreview() {
    CheckInDoneScreen(navController = rememberNavController(), eventId = "1")
}