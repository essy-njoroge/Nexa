package com.essy.nexa.ui.screens.post

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.45f)
private val CardBorder   = White.copy(alpha = 0.07f)

private val FireGradient = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))

val postCategoryList = listOf("General", "Event", "Study", "Career", "Club", "Announcement")

private val categoryColors = mapOf(
    "General"      to HotPink,
    "Event"        to BlazeOrange,
    "Study"        to Color(0xFF00D4AA),
    "Career"       to GoldYellow,
    "Club"         to VioletDeep,
    "Announcement" to Color(0xFF00A3FF)
)

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
private fun PostBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val o1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")

    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val gs = 36.dp.toPx(); val gc = HotPink.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
        var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx, 0f), Offset(gx, h), lw); gx += gs }
        var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f, gy), Offset(w, gy), lw); gy += gs }
        drawRadialOrb(Offset(w * 0.1f + o1 * 20.dp.toPx(), h * 0.06f), 180.dp.toPx(), HotPink)
        drawRadialOrb(Offset(w * 0.9f, h * 0.5f + o1 * 15.dp.toPx()), 150.dp.toPx(), VioletDeep, 0.7f)
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

// ─── CreatePostScreen ─────────────────────────────────────────────────────────
@Composable
fun CreatePostScreen(
    navController: NavController,
    previewMode: Boolean = false
) {
    var postContent  by remember { mutableStateOf("") }
    var selectedCat  by remember { mutableStateOf("General") }
    var isAnonymous  by remember { mutableStateOf(false) }
    var isPosting    by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var authorName   by remember { mutableStateOf("User") }
    var authorCourse by remember { mutableStateOf("") }

    val auth      = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }

    // Load author info once
    if (!previewMode) {
        LaunchedEffect(Unit) {
            val uid = auth.currentUser?.uid ?: return@LaunchedEffect
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener {
                    authorName   = it.getString("name")   ?: "User"
                    authorCourse = it.getString("course") ?: ""
                }
        }
    }

    val selectedColor = categoryColors[selectedCat] ?: HotPink

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        PostBackground()
        CornerBrackets()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Top bar ──
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                Column {
                    Text("Create Post", color = White, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Text("Share with your campus", color = TextMuted, fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Author preview ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardBg)
                    .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(HotPink, BlazeOrange))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isAnonymous) "?" else authorName.take(1),
                        color = White, fontWeight = FontWeight.Black, fontSize = 16.sp
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (isAnonymous) "Anonymous" else authorName,
                        color = White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp
                    )
                    if (!isAnonymous && authorCourse.isNotEmpty()) {
                        Text(authorCourse, color = TextMuted, fontSize = 11.sp)
                    }
                }
                // Category badge
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(selectedColor.copy(alpha = 0.12f))
                        .border(1.dp, selectedColor.copy(alpha = 0.30f), CircleShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(selectedCat, color = selectedColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Text input ──
            OutlinedTextField(
                value = postContent,
                onValueChange = { if (it.length <= 500) postContent = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp),
                placeholder = {
                    Text("What's happening on campus?", color = TextMuted, fontSize = 14.sp)
                },
                shape = RoundedCornerShape(16.dp),
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

            // Char count
            Text(
                "${postContent.length}/500",
                color = if (postContent.length > 450) GoldYellow else TextMuted,
                fontSize = 11.sp,
                modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
            )

            Spacer(Modifier.height(18.dp))

            // ── Category label ──
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.width(3.dp).height(14.dp).clip(CircleShape).background(FireGradient))
                Spacer(Modifier.width(8.dp))
                Text("Category", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(Modifier.height(10.dp))

            // ── Category chips ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                postCategoryList.forEach { cat ->
                    val isSel = selectedCat == cat
                    val catColor = categoryColors[cat] ?: HotPink
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSel) Brush.linearGradient(listOf(catColor, catColor.copy(alpha = 0.75f)))
                                else Brush.linearGradient(listOf(White.copy(alpha = 0.06f), White.copy(alpha = 0.06f)))
                            )
                            .border(
                                1.dp,
                                if (isSel) Color.Transparent else White.copy(alpha = 0.10f),
                                CircleShape
                            )
                            .clickable { selectedCat = cat }
                            .padding(horizontal = 16.dp, vertical = 9.dp)
                    ) {
                        Text(
                            cat,
                            color = if (isSel) White else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Anonymous toggle ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardBg)
                    .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Post anonymously", color = White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("Your name won't be shown", color = TextMuted, fontSize = 11.sp)
                }
                Switch(
                    checked = isAnonymous,
                    onCheckedChange = { isAnonymous = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = White,
                        checkedTrackColor = HotPink,
                        uncheckedThumbColor = White.copy(alpha = 0.5f),
                        uncheckedTrackColor = White.copy(alpha = 0.12f),
                        uncheckedBorderColor = White.copy(alpha = 0.15f)
                    )
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Error message ──
            if (errorMessage.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFF2D2D).copy(alpha = 0.08f))
                        .border(1.dp, Color(0xFFFF2D2D).copy(alpha = 0.20f), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(errorMessage, color = Color(0xFFFF6B6B), fontSize = 13.sp)
                }
                Spacer(Modifier.height(12.dp))
            }

            // ── Post button ──
            // FIX: button uses its own click handler cleanly, no early returns that block state reset
            Button(
                onClick = {
                    errorMessage = ""

                    val content = postContent.trim()
                    if (content.isBlank()) {
                        errorMessage = "Write something first"
                        return@Button
                    }

                    val currentUser = auth.currentUser
                    if (currentUser == null) {
                        errorMessage = "Not logged in"
                        return@Button
                    }

                    isPosting = true

                    val postData = hashMapOf(
                        "authorId"     to currentUser.uid,
                        "authorName"   to if (isAnonymous) "Anonymous" else authorName,
                        "authorCourse" to if (isAnonymous) "" else authorCourse,
                        "content"      to content,
                        "category"     to selectedCat,
                        "likes"        to 0,
                        "comments"     to 0,
                        "timestamp"    to Timestamp.now(),
                        "isAnonymous"  to isAnonymous
                    )

                    firestore.collection("posts")
                        .add(postData)
                        .addOnSuccessListener {
                            // reset state BEFORE navigating so no stale state on back
                            postContent  = ""
                            selectedCat  = "General"
                            isAnonymous  = false
                            isPosting    = false
                            errorMessage = ""
                            navController.navigate("home") {
                                popUpTo("create_post") { inclusive = true }
                            }
                        }
                        .addOnFailureListener { e ->
                            isPosting    = false
                            errorMessage = e.message ?: "Failed to post. Try again."
                        }
                },
                enabled = !isPosting,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isPosting)
                                Brush.linearGradient(listOf(HotPink.copy(alpha = 0.5f), GoldYellow.copy(alpha = 0.5f)))
                            else
                                FireGradient,
                            RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPosting) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CircularProgressIndicator(
                                color = White,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                            Text("Posting...", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    } else {
                        Text("Share Post", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun CreatePostPreview() {
    CreatePostScreen(rememberNavController(), previewMode = true)
}