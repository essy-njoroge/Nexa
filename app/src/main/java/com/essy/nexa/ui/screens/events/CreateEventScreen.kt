package com.essy.nexa.ui.screens.events

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

// ─── Colors (same palette) ────────────────────────────────────────────────────
private val DeepMidnight = Color(0xFF06050F)
private val CardBg       = Color(0xFF100E1A)
private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val VioletDeep   = Color(0xFF7B2FFF)
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.45f)
private val CardBorder   = White.copy(alpha = 0.07f)

private val FireGradient = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))

private val eventCategories = listOf("Tech","Career","Arts","Wellness","Business","Sports","General")

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRadialOrb(
    center: Offset, radius: Float, color: Color, strength: Float = 1f
) {
    drawCircle(
        brush = Brush.radialGradient(
            listOf(color.copy(alpha = 0.35f * strength), color.copy(alpha = 0.14f * strength), color.copy(alpha = 0.04f * strength), Color.Transparent),
            center = center, radius = radius
        ),
        radius = radius, center = center
    )
}

@Composable
private fun CreateEventBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val o1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")
    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val gs = 36.dp.toPx(); val gc = HotPink.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
        var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += gs }
        var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += gs }
        drawRadialOrb(Offset(w * 0.1f + o1 * 20.dp.toPx(), h * 0.06f), 180.dp.toPx(), HotPink)
        drawRadialOrb(Offset(w * 0.9f, h * 0.5f + o1 * 15.dp.toPx()), 150.dp.toPx(), VioletDeep, 0.7f)
    })
}

@Composable
private fun CornerBrackets() {
    Box(modifier = Modifier.fillMaxSize()) {
        val sw = 1.5.dp
        Box(modifier = Modifier.align(Alignment.TopStart).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = HotPink.copy(alpha = 0.45f)
            drawLine(c, Offset(s,0f), Offset(0f,0f), w, cap = StrokeCap.Square); drawLine(c, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = HotPink.copy(alpha = 0.45f)
            drawLine(c, Offset(0f,0f), Offset(s,0f), w, cap = StrokeCap.Square); drawLine(c, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.BottomStart).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = GoldYellow.copy(alpha = 0.45f)
            drawLine(c, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square); drawLine(c, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = GoldYellow.copy(alpha = 0.45f)
            drawLine(c, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square); drawLine(c, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square)
        })
    }
}

@Composable
private fun NexaField(label: String, value: String, onChange: (String) -> Unit, placeholder: String, singleLine: Boolean = true, minHeight: Int = 52) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, color = TextMuted)
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            placeholder = { Text(placeholder, color = TextMuted, fontSize = 14.sp) },
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HotPink.copy(alpha = 0.55f),
                unfocusedBorderColor = White.copy(alpha = 0.10f),
                focusedContainerColor = White.copy(alpha = 0.04f),
                unfocusedContainerColor = White.copy(alpha = 0.04f),
                focusedTextColor = White, unfocusedTextColor = White, cursorColor = HotPink
            )
        )
    }
}

// ─── CreateEventScreen ────────────────────────────────────────────────────────
@Composable
fun CreateEventScreen(navController: NavController, previewMode: Boolean = false) {
    var title         by remember { mutableStateOf("") }
    var description   by remember { mutableStateOf("") }
    var location      by remember { mutableStateOf("") }
    var date          by remember { mutableStateOf("") }
    var time          by remember { mutableStateOf("") }
    var organizer     by remember { mutableStateOf("") }
    var maxAttendees  by remember { mutableStateOf("") }
    var selectedCat   by remember { mutableStateOf("Tech") }
    var isPosting     by remember { mutableStateOf(false) }
    var errorMessage  by remember { mutableStateOf("") }

    val firestore = remember { FirebaseFirestore.getInstance() }
    val catColor = eventCategoryColor(selectedCat)

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        CreateEventBackground()
        CornerBrackets()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Top bar ──
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                        .background(White.copy(alpha = 0.06f))
                        .border(1.dp, White.copy(alpha = 0.10f), CircleShape)
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ArrowBack, null, tint = White, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Create Event", color = White, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Text("Admin — publish to all students", color = HotPink, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Fields ──
            NexaField("EVENT TITLE", title, { title = it }, "e.g. Annual Hackathon 2025")
            Spacer(Modifier.height(14.dp))
            NexaField("DESCRIPTION", description, { description = it }, "What's this event about?", singleLine = false, minHeight = 100)
            Spacer(Modifier.height(14.dp))
            NexaField("LOCATION", location, { location = it }, "e.g. Innovation Hub, Block C")
            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    NexaField("DATE", date, { date = it }, "e.g. Fri 16 May")
                }
                Box(modifier = Modifier.weight(1f)) {
                    NexaField("TIME", time, { time = it }, "e.g. 9:00 AM")
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    NexaField("ORGANIZER", organizer, { organizer = it }, "e.g. TechClub")
                }
                Box(modifier = Modifier.weight(1f)) {
                    NexaField("MAX CAPACITY", maxAttendees, { maxAttendees = it }, "e.g. 120")
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Category ──
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.width(3.dp).height(14.dp).clip(CircleShape).background(FireGradient))
                Spacer(Modifier.width(8.dp))
                Text("Category", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                eventCategories.forEach { cat ->
                    val isSel = selectedCat == cat
                    val cc = eventCategoryColor(cat)
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSel) Brush.linearGradient(listOf(cc, cc.copy(alpha = 0.75f))) else Brush.linearGradient(listOf(White.copy(alpha = 0.06f), White.copy(alpha = 0.06f))))
                            .border(1.dp, if (isSel) Color.Transparent else White.copy(alpha = 0.10f), CircleShape)
                            .clickable { selectedCat = cat }
                            .padding(horizontal = 16.dp, vertical = 9.dp)
                    ) {
                        Text(cat, color = if (isSel) White else TextMuted, fontSize = 12.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Preview card ──
            if (title.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.width(3.dp).height(14.dp).clip(CircleShape).background(FireGradient))
                    Spacer(Modifier.width(8.dp))
                    Text("Preview", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(Modifier.height(10.dp))
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBg)
                        .border(1.dp, catColor.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Box(modifier = Modifier.clip(CircleShape).background(catColor.copy(alpha = 0.12f)).border(1.dp, catColor.copy(alpha = 0.28f), CircleShape).padding(horizontal = 10.dp, vertical = 4.dp)) {
                        Text(selectedCat, color = catColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(title, color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    if (description.isNotEmpty()) Text(description, color = TextMuted, fontSize = 12.sp, maxLines = 2)
                    if (date.isNotEmpty() || time.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            if (date.isNotEmpty()) Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, null, tint = catColor.copy(alpha = 0.70f), modifier = Modifier.size(12.dp))
                                Spacer(Modifier.width(4.dp)); Text(date, color = TextMuted, fontSize = 11.sp)
                            }
                            if (time.isNotEmpty()) Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccessTime, null, tint = catColor.copy(alpha = 0.70f), modifier = Modifier.size(12.dp))
                                Spacer(Modifier.width(4.dp)); Text(time, color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            // ── Error ──
            if (errorMessage.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFF2D2D).copy(alpha = 0.08f))
                        .border(1.dp, Color(0xFFFF2D2D).copy(alpha = 0.20f), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) { Text(errorMessage, color = Color(0xFFFF6B6B), fontSize = 13.sp) }
                Spacer(Modifier.height(12.dp))
            }

            // ── Publish button ──
            Button(
                onClick = {
                    errorMessage = ""
                    if (title.isBlank()) { errorMessage = "Event title is required"; return@Button }
                    if (location.isBlank()) { errorMessage = "Location is required"; return@Button }
                    if (date.isBlank()) { errorMessage = "Date is required"; return@Button }
                    if (isPosting) return@Button

                    isPosting = true

                    val eventData = hashMapOf(
                        "title"         to title.trim(),
                        "description"   to description.trim(),
                        "location"      to location.trim(),
                        "date"          to date.trim(),
                        "time"          to time.trim(),
                        "category"      to selectedCat,
                        "organizerName" to organizer.trim(),
                        "maxAttendees"  to (maxAttendees.toIntOrNull() ?: 100),
                        "attendees"     to 0,
                        "isRsvped"      to false,
                        "createdAt"     to Timestamp.now()
                    )

                    if (previewMode) {
                        // In preview mode just simulate success
                        isPosting = false
                        navController.navigate("events") { popUpTo("create_event") { inclusive = true } }
                        return@Button
                    }

                    firestore.collection("events")
                        .add(eventData)
                        .addOnSuccessListener {
                            isPosting = false
                            navController.navigate("events") { popUpTo("create_event") { inclusive = true } }
                        }
                        .addOnFailureListener { e ->
                            isPosting = false
                            errorMessage = e.message ?: "Failed to publish event"
                        }
                },
                enabled = !isPosting,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(if (isPosting) Brush.linearGradient(listOf(HotPink.copy(alpha = 0.5f), GoldYellow.copy(alpha = 0.5f))) else FireGradient, RoundedCornerShape(50.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPosting) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            CircularProgressIndicator(color = White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Text("Publishing...", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    } else {
                        Text("Publish Event", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp)
                    }
                }
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun CreateEventPreview() { CreateEventScreen(rememberNavController(), previewMode = true) }