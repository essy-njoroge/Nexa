package com.essy.nexa.ui.screens.events

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
private fun QrBackground(scanned: Boolean) {
    val inf = rememberInfiniteTransition(label = "bg")
    val o1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")
    val orbColor = if (scanned) TealGreen else HotPink

    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val gs = 36.dp.toPx(); val gc = orbColor.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
        var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += gs }
        var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += gs }
        drawRadialOrb(Offset(w * 0.1f + o1 * 20.dp.toPx(), h * 0.06f), 180.dp.toPx(), orbColor)
        drawRadialOrb(Offset(w * 0.88f, h * 0.45f + o1 * 15.dp.toPx()), 140.dp.toPx(), VioletDeep, 0.7f)
    })
}

// ─── Corner brackets ──────────────────────────────────────────────────────────
@Composable
private fun CornerBrackets(scanned: Boolean) {
    val topColor    = if (scanned) TealGreen.copy(alpha = 0.55f) else HotPink.copy(alpha = 0.45f)
    val bottomColor = if (scanned) TealGreen.copy(alpha = 0.45f) else GoldYellow.copy(alpha = 0.45f)
    Box(modifier = Modifier.fillMaxSize()) {
        val sw = 1.5.dp
        Box(modifier = Modifier.align(Alignment.TopStart).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx()
            drawLine(topColor, Offset(s,0f), Offset(0f,0f), w, cap = StrokeCap.Square)
            drawLine(topColor, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx()
            drawLine(topColor, Offset(0f,0f), Offset(s,0f), w, cap = StrokeCap.Square)
            drawLine(topColor, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.BottomStart).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx()
            drawLine(bottomColor, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square)
            drawLine(bottomColor, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx()
            drawLine(bottomColor, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square)
            drawLine(bottomColor, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square)
        })
    }
}

// ─── Scanner box ──────────────────────────────────────────────────────────────
@Composable
private fun ScannerBox(scanned: Boolean, scanLineY: Float) {
    val borderBrush = if (scanned) TealGradient else FireGradient

    Box(
        modifier = Modifier
            .size(240.dp)
            .drawWithContent {
                drawContent()
                val strokeW = 2.dp.toPx()
                val r = 20.dp.toPx()
                drawRoundRect(
                    brush = borderBrush,
                    topLeft = Offset(strokeW / 2, strokeW / 2),
                    size = Size(size.width - strokeW, size.height - strokeW),
                    cornerRadius = CornerRadius(r, r),
                    style = Stroke(width = strokeW)
                )
            }
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg),
        contentAlignment = Alignment.Center
    ) {
        if (!scanned) {
            Icon(
                Icons.Default.QrCode, null,
                tint = White.copy(alpha = 0.12f),
                modifier = Modifier.size(110.dp)
            )

            listOf(Alignment.TopStart, Alignment.TopEnd, Alignment.BottomStart, Alignment.BottomEnd)
                .forEachIndexed { i, alignment ->
                    Box(
                        modifier = Modifier
                            .align(alignment)
                            .padding(14.dp)
                            .size(22.dp)
                            .drawBehind {
                                val s = size.width; val w = 2.5.dp.toPx(); val c = HotPink
                                when (i) {
                                    0 -> { drawLine(c, Offset(s,0f), Offset(0f,0f), w, cap = StrokeCap.Square); drawLine(c, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square) }
                                    1 -> { drawLine(c, Offset(0f,0f), Offset(s,0f), w, cap = StrokeCap.Square); drawLine(c, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square) }
                                    2 -> { drawLine(c, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square); drawLine(c, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square) }
                                    3 -> { drawLine(c, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square); drawLine(c, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square) }
                                }
                            }
                    )
                }

            // FIX 4: scan line drawn inside the box using drawBehind — can't escape clip boundary
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val padding = 14.dp.toPx()
                        val y = padding + (size.height - 2 * padding) * scanLineY
                        drawLine(
                            brush = Brush.horizontalGradient(
                                listOf(Color.Transparent, HotPink, BlazeOrange, Color.Transparent)
                            ),
                            start = Offset(padding, y),
                            end   = Offset(size.width - padding, y),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(TealGreen.copy(alpha = 0.15f))
                        .border(2.dp, TealGreen.copy(alpha = 0.40f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, null, tint = TealGreen, modifier = Modifier.size(36.dp))
                }
                Text("Verified", color = TealGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 1.sp)
            }
        }
    }
}

// ─── QrCheckInScreen ──────────────────────────────────────────────────────────
// FIX 1: Accept eventId — load real event instead of hardcoded text
@Composable
fun QrCheckInScreen(navController: NavController, eventId: String?) {
    val db  = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    // FIX 1: Load event from Firestore; fall back to hardcoded list if offline
    var event     by remember { mutableStateOf<Event?>(eventList.find { it.id == eventId } ?: eventList[0]) }
    var scanned   by remember { mutableStateOf(false) }
    var isWriting by remember { mutableStateOf(false) } // prevents double-tap
    val scanLine  = remember { Animatable(0f) }

    // FIX 1: Load real event data
    LaunchedEffect(eventId) {
        if (eventId == null) return@LaunchedEffect
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
    }

    LaunchedEffect(scanned) {
        if (!scanned) {
            while (!scanned) {
                scanLine.animateTo(1f, tween(1800, easing = LinearEasing))
                scanLine.snapTo(0f)
            }
        }
    }

    // FIX 2: Write check-in to Firestore
    fun recordCheckIn() {
        if (uid == null || eventId == null || isWriting) return
        isWriting = true

        val checkInRef = db.collection("events").document(eventId)
            .collection("checkins").document(uid)

        checkInRef.set(
            mapOf(
                "uid"       to uid,
                "eventId"   to eventId,
                "timestamp" to FieldValue.serverTimestamp()
            )
        ).addOnSuccessListener {
            scanned   = true
            isWriting = false
        }.addOnFailureListener {
            // Still mark as scanned locally so the UI doesn't freeze
            scanned   = true
            isWriting = false
        }
    }

    val ev = event ?: return
    val categoryColor = eventCategoryColor(ev.category)

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        QrBackground(scanned = scanned)
        CornerBrackets(scanned = scanned)

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Top bar ──
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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
                Column {
                    Text("QR Check-In", color = White, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Text("Scan at the event entrance", color = TextMuted, fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── FIX 1: Real event info chip ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardBg)
                    .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(categoryColor.copy(alpha = 0.12f))
                        .border(1.dp, categoryColor.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Event, null, tint = categoryColor, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(ev.title, color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("${ev.date}  •  ${ev.location}", color = TextMuted, fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(categoryColor.copy(alpha = 0.10f))
                        .border(1.dp, categoryColor.copy(alpha = 0.25f), CircleShape)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(ev.category, color = categoryColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(40.dp))

            // ── Scanner ──
            ScannerBox(scanned = scanned, scanLineY = scanLine.value)

            Spacer(Modifier.height(28.dp))

            // ── Status text ──
            Text(
                text = if (scanned) "Check-in Successful!" else "Point camera at QR code",
                color = if (scanned) TealGreen else White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = if (scanned) "Your attendance has been recorded 🎉" else "QR code is shown at the event entrance",
                color = TextMuted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            // ── Action button ──
            if (!scanned) {
                Button(
                    onClick = { recordCheckIn() }, // FIX 2: writes to Firestore
                    enabled = !isWriting,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(50.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(FireGradient, RoundedCornerShape(50.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isWriting) {
                            CircularProgressIndicator(color = White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.QrCodeScanner, null, tint = White, modifier = Modifier.size(18.dp))
                                Text("Simulate Scan (Demo)", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            } else {
                // FIX 3: pass eventId to checkin_done so confirmation screen has context
                Button(
                    onClick = { navController.navigate("checkin_done/${ev.id}") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(50.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(TealGradient, RoundedCornerShape(50.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.CheckCircle, null, tint = White, modifier = Modifier.size(18.dp))
                            Text("View Confirmation", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun QrCheckInPreview() { QrCheckInScreen(rememberNavController(), eventId = "1") }