package com.essy.nexa.ui.screens.study

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
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
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.45f)
private val CardBorder   = White.copy(alpha = 0.07f)

private val FireGradient   = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))
private val VioletGradient = Brush.linearGradient(listOf(VioletLight, VioletDeep))

// Avatar gradient pairs per index
private val avatarGradients = listOf(
    listOf(HotPink, BlazeOrange),
    listOf(VioletLight, VioletDeep),
    listOf(BlazeOrange, GoldYellow),
    listOf(GoldYellow, HotPink),
    listOf(VioletDeep, HotPink)
)

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

// ─── Section header ───────────────────────────────────────────────────────────
@Composable
private fun SectionHeader(title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(3.dp).height(14.dp).clip(CircleShape).background(FireGradient))
        Spacer(Modifier.width(8.dp))
        Text(title, color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp)
    }
}

// ─── Info chip ────────────────────────────────────────────────────────────────
@Composable
private fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(White.copy(alpha = 0.05f))
            .border(1.dp, White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = VioletLight, modifier = Modifier.size(15.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, color = White.copy(alpha = 0.85f), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ─── StudyGroupDetailScreen ───────────────────────────────────────────────────
@Composable
fun StudyGroupDetailScreen(
    navController: NavController,
    groupId: String = "1"          // passed from nav arg
) {
    // Find the group — in real app you'd fetch from Firestore by groupId
    val group = studyGroups.find { it.id == groupId } ?: studyGroups[0]

    // Live member list — starts with hardcoded members, merges Firestore updates
    var members by remember { mutableStateOf(group.members.toMutableList()) }
    var isLeaving by remember { mutableStateOf(false) }

    // Listen to Firestore for live member updates
    LaunchedEffect(groupId) {
        FirebaseFirestore.getInstance()
            .collection("study_groups").document(groupId)
            .addSnapshotListener { snap, _ ->
                if (snap != null) {
                    @Suppress("UNCHECKED_CAST")
                    val firestoreMembers = snap.get("members") as? List<String>
                    if (!firestoreMembers.isNullOrEmpty()) {
                        members = firestoreMembers.toMutableList()
                    }
                }
            }
    }

    val inf = rememberInfiniteTransition(label = "bg")
    val o1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {

        // Background
        Box(modifier = Modifier.fillMaxSize().drawBehind {
            val w = size.width; val h = size.height
            val gs = 36.dp.toPx(); val gc = VioletLight.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
            var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += gs }
            var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += gs }
            drawRadialOrb(Offset(w * 0.5f, h * 0.12f + o1 * 15.dp.toPx()), 200.dp.toPx(), VioletDeep, 0.6f)
            drawRadialOrb(Offset(w * 0.88f, h * 0.55f), 130.dp.toPx(), HotPink, 0.5f)
        })

        // Corner brackets
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 110.dp)
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Study Group", color = White, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                        Text(group.course, color = TextMuted, fontSize = 12.sp)
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── Hero banner ──
            item {
                Box(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(150.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.linearGradient(listOf(VioletDeep.copy(alpha = 0.35f), HotPink.copy(alpha = 0.20f))))
                        .border(1.dp, VioletLight.copy(alpha = 0.20f), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.fillMaxSize().drawBehind {
                        drawRadialOrb(Offset(size.width * 0.3f, size.height * 0.5f), size.width * 0.4f, VioletDeep, 0.8f)
                        drawRadialOrb(Offset(size.width * 0.75f, size.height * 0.4f), size.width * 0.3f, HotPink, 0.5f)
                    })
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📚", fontSize = 42.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(group.topic, color = White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                        Text("${members.size}/${group.maxMembers} members", color = VioletLight, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(Modifier.height(22.dp))
            }

            // ── Info chips ──
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        InfoChip(Icons.Default.Schedule, group.schedule, Modifier.weight(1f))
                        InfoChip(Icons.Default.School, group.course, Modifier.weight(1f))
                    }
                    InfoChip(Icons.Default.People, "${members.size} of ${group.maxMembers} spots filled", Modifier.fillMaxWidth())
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── You're in! badge ──
            item {
                Box(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Brush.linearGradient(listOf(VioletDeep.copy(alpha = 0.14f), HotPink.copy(alpha = 0.10f))))
                        .border(1.dp, VioletLight.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(VioletLight.copy(alpha = 0.15f)).border(1.dp, VioletLight.copy(alpha = 0.30f), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.CheckCircle, null, tint = VioletLight, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("You're in this group!", color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Sessions start at the scheduled time", color = TextMuted, fontSize = 12.sp)
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── Members ──
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SectionHeader("Group Members")
                    Spacer(Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        members.forEachIndexed { index, memberName ->
                            val gradientColors = avatarGradients[index % avatarGradients.size]
                            val isCurrentUser = memberName == (FirebaseAuth.getInstance().currentUser?.displayName ?: "You")

                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (isCurrentUser) Brush.linearGradient(listOf(VioletDeep.copy(alpha = 0.12f), HotPink.copy(alpha = 0.08f)))
                                        else Brush.linearGradient(listOf(CardBg, CardBg))
                                    )
                                    .border(1.dp, if (isCurrentUser) VioletLight.copy(alpha = 0.22f) else CardBorder, RoundedCornerShape(14.dp))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(40.dp).clip(CircleShape)
                                        .background(Brush.linearGradient(gradientColors)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(modifier = Modifier.fillMaxSize().padding(2.dp).clip(CircleShape).background(DarkSurface), contentAlignment = Alignment.Center) {
                                        Text(memberName.take(1), color = White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        if (isCurrentUser) "$memberName (You)" else memberName,
                                        color = White, fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.SemiBold, fontSize = 14.sp
                                    )
                                    if (index == 0) Text("Group lead", color = GoldYellow, fontSize = 11.sp)
                                    else Text(group.course, color = TextMuted, fontSize = 11.sp)
                                }
                                if (index == 0) {
                                    Box(modifier = Modifier.clip(CircleShape).background(GoldYellow.copy(alpha = 0.12f)).border(1.dp, GoldYellow.copy(alpha = 0.28f), CircleShape).padding(horizontal = 8.dp, vertical = 3.dp)) {
                                        Text(
                                            "Lead",
                                            color = GoldYellow,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── Study resources (demo) ──
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    SectionHeader("Study Resources")
                    Spacer(Modifier.height(12.dp))
                    listOf(
                        Pair("📄", "Lecture notes — Week 1-6"),
                        Pair("🔗", "Course reference materials"),
                        Pair("📝", "Practice questions bank")
                    ).forEach { (emoji, label) ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(CardBg)
                                .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                                .clickable { }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(emoji, fontSize = 20.sp)
                            Spacer(Modifier.width(12.dp))
                            Text(label, color = White.copy(alpha = 0.80f), fontSize = 13.sp, modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ArrowForward, null, tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── Action buttons ──
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    // Message group — opens WhatsApp or external chat
                    Button(
                        onClick = { /* open WhatsApp / group chat link */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(50.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize().background(VioletGradient, RoundedCornerShape(50.dp)), contentAlignment = Alignment.Center) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Chat, null, tint = White, modifier = Modifier.size(18.dp))
                                Text("Message Group", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }

                    // Leave group
                    Box(
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(HotPink.copy(alpha = 0.07f))
                            .border(1.dp, HotPink.copy(alpha = 0.25f), RoundedCornerShape(50.dp))
                            .clickable {
                                if (isLeaving) return@clickable
                                isLeaving = true
                                val uid  = FirebaseAuth.getInstance().currentUser?.uid
                                val name = FirebaseAuth.getInstance().currentUser?.displayName ?: ""
                                if (uid != null && name.isNotEmpty()) {
                                    FirebaseFirestore.getInstance()
                                        .collection("study_groups").document(groupId)
                                        .update("members", FieldValue.arrayRemove(name))
                                        .addOnCompleteListener {
                                            isLeaving = false
                                            navController.popBackStack()
                                        }
                                } else {
                                    isLeaving = false
                                    navController.popBackStack()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLeaving) {
                            CircularProgressIndicator(color = HotPink, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Leave Group", color = HotPink, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun StudyGroupDetailPreview() { StudyGroupDetailScreen(rememberNavController(), "1") }
