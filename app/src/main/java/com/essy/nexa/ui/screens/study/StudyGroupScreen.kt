package com.essy.nexa.ui.screens.study

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.drawBehind
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
import com.essy.nexa.model.StudyGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// ─── Colors ───────────────────────────────────────────────────────────────────
private val DeepMidnight = Color(0xFF06050F)
private val DarkSurface  = Color(0xFF0D0918)
private val CardBg       = Color(0xFF100E1A)
private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val VioletDeep   = Color(0xFF7B2FFF)
private val VioletLight  = Color(0xFFA855F7)
private val CobaltBlue   = Color(0xFF00A3FF)
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.45f)
private val CardBorder   = White.copy(alpha = 0.07f)

private val FireGradient = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))

// ─── Data ─────────────────────────────────────────────────────────────────────
val studyGroups = listOf(
    StudyGroup("1","Computer Science","Algorithms & Data Structures", listOf("Kevin","Aisha","Brian","Grace"),5,"Tues & Thurs 6PM"),
    StudyGroup("2","Computer Science","Mobile Development (Android)", listOf("James","Fatima","Kevin"),4,"Fridays 4PM"),
    StudyGroup("3","Computer Science","Database Systems", listOf("Aisha","Tom","Rita"),5,"Mondays 5PM"),
    StudyGroup("4","Business","Financial Accounting", listOf("Grace","Brian","Sarah","Mark"),5,"Wednesdays 3PM"),
    StudyGroup("5","Computer Science","AI & Machine Learning", listOf("Kevin","Grace"),4,"Thursdays 5PM"),
    StudyGroup("6","Computer Science","Software Engineering", listOf("Aisha","James","Brian"),5,"Tuesdays 4PM"),
    StudyGroup("7","Computer Science","Computer Networks", listOf("Tom","Rita","Mark"),4,"Fridays 3PM")
)

// Module → keywords that match it in the schedule / topic
private val moduleKeywords = mapOf(
    "Algorithms"    to listOf("algorithms","data structures"),
    "Mobile Dev"    to listOf("mobile","android"),
    "Databases"     to listOf("database","sql"),
    "Networks"      to listOf("networks","networking"),
    "AI & ML"       to listOf("ai","machine learning","ml"),
    "Software Eng"  to listOf("software engineering","software eng")
)

// Day → keywords that appear in schedule strings
private val dayKeywords = mapOf(
    "Monday"    to listOf("mon"),
    "Tuesday"   to listOf("tues","tue"),
    "Wednesday" to listOf("wed"),
    "Thursday"  to listOf("thurs","thu"),
    "Friday"    to listOf("fri")
)

// Score a group 0-2 based on how well it matches selected module + day
private fun matchScore(group: StudyGroup, module: String, day: String): Int {
    var score = 0
    val topicLower = group.topic.lowercase()
    val schedLower = group.schedule.lowercase()

    if (module.isNotEmpty()) {
        val keywords = moduleKeywords[module] ?: listOf(module.lowercase())
        if (keywords.any { topicLower.contains(it) }) score++
    }
    if (day.isNotEmpty()) {
        val keywords = dayKeywords[day] ?: listOf(day.take(3).lowercase())
        if (keywords.any { schedLower.contains(it) }) score++
    }
    return score
}

val modules = listOf("Algorithms","Mobile Dev","Databases","Networks","AI & ML","Software Eng")
val days    = listOf("Monday","Tuesday","Wednesday","Thursday","Friday")

// ─── Radial orb ───────────────────────────────────────────────────────────────
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRadialOrb(
    center: Offset, radius: Float, color: Color, strength: Float = 1f
) {
    drawCircle(
        brush = Brush.radialGradient(
            listOf(color.copy(alpha = 0.35f * strength), color.copy(alpha = 0.14f * strength),
                color.copy(alpha = 0.04f * strength), Color.Transparent),
            center = center, radius = radius
        ),
        radius = radius, center = center
    )
}

@Composable
private fun StudyBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val o1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")
    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val gs = 36.dp.toPx(); val gc = VioletLight.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
        var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += gs }
        var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += gs }
        drawRadialOrb(Offset(w * 0.1f + o1 * 20.dp.toPx(), h * 0.06f), 170.dp.toPx(), VioletLight)
        drawRadialOrb(Offset(w * 0.88f, h * 0.42f + o1 * 12.dp.toPx()), 140.dp.toPx(), HotPink, 0.7f)
    })
}

@Composable
private fun CornerBrackets() {
    Box(modifier = Modifier.fillMaxSize()) {
        val sw = 1.5.dp
        Box(modifier = Modifier.align(Alignment.TopStart).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = VioletLight.copy(alpha = 0.55f)
            drawLine(c, Offset(s,0f), Offset(0f,0f), w, cap = StrokeCap.Square)
            drawLine(c, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = VioletLight.copy(alpha = 0.55f)
            drawLine(c, Offset(0f,0f), Offset(s,0f), w, cap = StrokeCap.Square)
            drawLine(c, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.BottomStart).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = HotPink.copy(alpha = 0.45f)
            drawLine(c, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square)
            drawLine(c, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square)
        })
        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(14.dp).size(18.dp).drawBehind {
            val s = size.width; val w = sw.toPx(); val c = HotPink.copy(alpha = 0.45f)
            drawLine(c, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square)
            drawLine(c, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square)
        })
    }
}

// ─── Selector chip ────────────────────────────────────────────────────────────
@Composable
private fun SelectorChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(
                if (selected) Brush.linearGradient(listOf(VioletLight, VioletDeep))
                else Brush.linearGradient(listOf(White.copy(alpha = 0.06f), White.copy(alpha = 0.06f)))
            )
            .border(1.dp, if (selected) Color.Transparent else White.copy(alpha = 0.10f), CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 9.dp)
    ) {
        Text(label, color = if (selected) White else TextMuted, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}

// ─── Study group card ─────────────────────────────────────────────────────────
@Composable
fun StudyGroupCard(group: StudyGroup, matchScore: Int) {
    var joined by remember { mutableStateOf(false) }

    val isPerfect  = matchScore == 2
    val isPartial  = matchScore == 1
    val borderColor = when {
        isPerfect -> VioletLight.copy(alpha = 0.40f)
        isPartial -> HotPink.copy(alpha = 0.22f)
        else      -> CardBorder
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CardBg)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icon circle
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPerfect) Brush.linearGradient(listOf(VioletLight, VioletDeep))
                        else Brush.linearGradient(listOf(HotPink.copy(alpha = 0.60f), BlazeOrange.copy(alpha = 0.60f)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(2.dp).clip(CircleShape).background(DarkSurface), contentAlignment = Alignment.Center) {
                    Text("📚", fontSize = 20.sp)
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(group.topic, color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(group.course, color = TextMuted, fontSize = 12.sp)
            }

            // Member count badge
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(VioletLight.copy(alpha = 0.10f))
                    .border(1.dp, VioletLight.copy(alpha = 0.25f), CircleShape)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("${group.members.size}/${group.maxMembers}", color = VioletLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(10.dp))

        // Schedule
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Schedule, null, tint = GoldYellow.copy(alpha = 0.70f), modifier = Modifier.size(13.dp))
            Spacer(Modifier.width(5.dp))
            Text(group.schedule, color = TextMuted, fontSize = 12.sp)
        }

        Spacer(Modifier.height(6.dp))

        // Members preview
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.People, null, tint = TextMuted.copy(alpha = 0.70f), modifier = Modifier.size(13.dp))
            Spacer(Modifier.width(5.dp))
            Text(group.members.take(3).joinToString(", ") + if (group.members.size > 3) " +${group.members.size - 3}" else "", color = TextMuted, fontSize = 12.sp)
        }

        // Match indicator
        if (matchScore > 0) {
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (isPerfect) Brush.linearGradient(listOf(VioletLight.copy(alpha = 0.15f), VioletDeep.copy(alpha = 0.15f)))
                        else Brush.linearGradient(listOf(HotPink.copy(alpha = 0.10f), BlazeOrange.copy(alpha = 0.10f)))
                    )
                    .border(1.dp, if (isPerfect) VioletLight.copy(alpha = 0.30f) else HotPink.copy(alpha = 0.20f), CircleShape)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    if (isPerfect) "✦ Perfect match" else "~ Partial match",
                    color = if (isPerfect) VioletLight else HotPink,
                    fontSize = 10.sp, fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        HorizontalDivider(color = White.copy(alpha = 0.06f))

        Spacer(Modifier.height(10.dp))

        // Join button
        Button(
            onClick = { joined = !joined },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(50.dp),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.fillMaxWidth().height(42.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (joined) Brush.linearGradient(listOf(GoldYellow.copy(alpha = 0.80f), BlazeOrange.copy(alpha = 0.80f)))
                        else Brush.linearGradient(listOf(VioletLight, VioletDeep)),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (joined) "✓  Joined" else "Join Group",
                    color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp
                )
            }
        }
    }
}

// ─── StudyGroupScreen ─────────────────────────────────────────────────────────
@Composable
fun StudyGroupScreen(navController: NavController) {
    var selectedModule by remember { mutableStateOf("") }
    var selectedDay    by remember { mutableStateOf("") }
    var showMatches    by remember { mutableStateOf(false) }
    var isMatching     by remember { mutableStateOf(false) }

    // Pre-fill from user's Firestore profile (course / availability)
    var userCourse by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@LaunchedEffect
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                // If user registered with a course hint, try to auto-select a module
                userCourse = doc.getString("course") ?: ""
                val hint = userCourse.lowercase()
                val autoModule = modules.firstOrNull { mod ->
                    val keys = moduleKeywords[mod] ?: listOf(mod.lowercase())
                    keys.any { hint.contains(it) }
                }
                if (autoModule != null && selectedModule.isEmpty()) selectedModule = autoModule
            }
    }

    // Ranked results: perfect matches first, then partial, then rest
    val rankedGroups = remember(selectedModule, selectedDay, showMatches) {
        if (!showMatches) emptyList()
        else studyGroups
            .map { group -> Pair(group, matchScore(group, selectedModule, selectedDay)) }
            .sortedByDescending { it.second }
    }

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        StudyBackground()
        CornerBrackets()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            // ── Top bar ──
            item {
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
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
                        Text("Study Matcher", color = White, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                        Text("AI-powered group matching", color = TextMuted, fontSize = 12.sp)
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            // ── AI badge ──
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(VioletDeep.copy(alpha = 0.14f), HotPink.copy(alpha = 0.10f))))
                        .border(1.dp, VioletLight.copy(alpha = 0.22f), RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(38.dp).clip(CircleShape)
                            .background(VioletLight.copy(alpha = 0.15f))
                            .border(1.dp, VioletLight.copy(alpha = 0.30f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, tint = VioletLight, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("AI Matching Active", color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            if (userCourse.isNotEmpty()) "Pre-filled from your profile: $userCourse"
                            else "Select module + day to find your group",
                            color = TextMuted, fontSize = 12.sp
                        )
                    }
                }
                Spacer(Modifier.height(22.dp))
            }

            // ── Module selector ──
            item {
                Row(modifier = Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.width(3.dp).height(14.dp).clip(CircleShape).background(FireGradient))
                    Spacer(Modifier.width(8.dp))
                    Text("What are you studying?", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(Modifier.height(12.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    modules.chunked(3).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { mod ->
                                SelectorChip(mod, selectedModule == mod) {
                                    selectedModule = if (selectedModule == mod) "" else mod
                                    showMatches = false
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(22.dp))
            }

            // ── Day selector ──
            item {
                Row(modifier = Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.width(3.dp).height(14.dp).clip(CircleShape).background(FireGradient))
                    Spacer(Modifier.width(8.dp))
                    Text("When are you free?", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(Modifier.height(12.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    days.chunked(3).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { day ->
                                SelectorChip(day.take(3), selectedDay == day) {
                                    selectedDay = if (selectedDay == day) "" else day
                                    showMatches = false
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(22.dp))
            }

            // ── Find button ──
            item {
                Button(
                    onClick = {
                        isMatching = true
                        showMatches = false
                        // Simulate a brief "AI thinking" delay
                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                            isMatching = false
                            showMatches = true
                        }, 800)
                    },
                    enabled = !isMatching,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(50.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(52.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(FireGradient, RoundedCornerShape(50.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isMatching) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                CircularProgressIndicator(color = White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                Text("Matching you...", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.AutoAwesome, null, tint = White, modifier = Modifier.size(16.dp))
                                Text("Find My Study Group", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── Results ──
            if (showMatches) {
                item {
                    val perfectCount = rankedGroups.count { it.second == 2 }
                    val partialCount = rankedGroups.count { it.second == 1 }

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.width(3.dp).height(14.dp).clip(CircleShape).background(FireGradient))
                            Spacer(Modifier.width(8.dp))
                            Text("${rankedGroups.size} Groups Found", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            buildString {
                                if (perfectCount > 0) append("$perfectCount perfect match${if (perfectCount > 1) "es" else ""}")
                                if (perfectCount > 0 && partialCount > 0) append("  •  ")
                                if (partialCount > 0) append("$partialCount partial match${if (partialCount > 1) "es" else ""}")
                                if (perfectCount == 0 && partialCount == 0) append("No exact matches — showing all groups")
                            },
                            color = TextMuted, fontSize = 12.sp,
                            modifier = Modifier.padding(start = 11.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }

                if (rankedGroups.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                                .clip(RoundedCornerShape(18.dp)).background(CardBg)
                                .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔍", fontSize = 32.sp)
                                Spacer(Modifier.height(8.dp))
                                Text("No groups yet", color = White, fontWeight = FontWeight.Bold)
                                Text("Try different selections", color = TextMuted, fontSize = 12.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                } else {
                    items(rankedGroups) { (group, score) ->
                        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                            StudyGroupCard(group = group, matchScore = score)
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun StudyPreview() { StudyGroupScreen(rememberNavController()) }