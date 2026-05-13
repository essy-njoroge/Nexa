package com.essy.nexa.ui.screens.feed

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
private val CobaltBlue   = Color(0xFF00A3FF)
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.45f)
private val CardBorder   = White.copy(alpha = 0.07f)

private val FireGradient = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))

private val postTags = listOf("General", "Tech", "Career", "Wellness", "Arts", "Business", "Sports", "Announcement")

private fun tagColor(tag: String) = when (tag) {
    "Tech"         -> HotPink
    "Career"       -> GoldYellow
    "Arts"         -> Color(0xFFEC4899)
    "Wellness"     -> TealGreen
    "Business"     -> VioletDeep
    "Sports"       -> CobaltBlue
    "Announcement" -> BlazeOrange
    else           -> White.copy(alpha = 0.55f)
}

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
private fun CreatePostBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val o1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")

    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val gs = 36.dp.toPx(); val gc = HotPink.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
        var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += gs }
        var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += gs }
        drawRadialOrb(Offset(w * 0.1f + o1 * 20.dp.toPx(), h * 0.06f), 180.dp.toPx(), HotPink)
        drawRadialOrb(Offset(w * 0.88f, h * 0.5f + o1 * 15.dp.toPx()), 150.dp.toPx(), VioletDeep, 0.7f)
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

// ─── Section label ────────────────────────────────────────────────────────────
@Composable
private fun SectionLabel(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(3.dp).height(14.dp).clip(CircleShape).background(FireGradient))
        Spacer(Modifier.width(8.dp))
        Text(text, color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 0.3.sp)
    }
}

// ─── CreatePostScreen ─────────────────────────────────────────────────────────
@Composable
fun CreatePostScreen(navController: NavController) {
    val db  = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var postContent  by remember { mutableStateOf("") }
    var selectedTag  by remember { mutableStateOf("General") }
    var isPosting    by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Character limit
    val maxChars = 500
    val remaining = maxChars - postContent.length
    val isOverLimit = remaining < 0
    val canPost = postContent.isNotBlank() && !isOverLimit && !isPosting

    fun submitPost() {
        if (!canPost || uid == null) return
        isPosting = true
        errorMessage = ""

        val post = hashMapOf(
            "uid"       to uid,
            "content"   to postContent.trim(),
            "tag"       to selectedTag,
            "likes"     to 0,
            "createdAt" to FieldValue.serverTimestamp()
        )

        db.collection("posts").add(post)
            .addOnSuccessListener {
                isPosting = false
                navController.popBackStack()
            }
            .addOnFailureListener { e ->
                isPosting = false
                errorMessage = "Failed to post. Please try again."
            }
    }

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        CreatePostBackground()
        CornerBrackets()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // ── Top bar ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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
                    Icon(Icons.Default.Close, null, tint = White, modifier = Modifier.size(18.dp))
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("New Post", color = White, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
                    Text("Share with the campus", color = TextMuted, fontSize = 11.sp)
                }

                // Post button in top bar
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (canPost) FireGradient else Brush.linearGradient(listOf(White.copy(alpha = 0.08f), White.copy(alpha = 0.08f))))
                        .clickable(enabled = canPost) { submitPost() }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPosting) {
                        CircularProgressIndicator(color = White, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                    } else {
                        Text(
                            "Post",
                            color = if (canPost) White else TextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Content input ──
            SectionLabel("What's on your mind?")
            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(CardBg)
                    .border(
                        1.dp,
                        if (isOverLimit) Color.Red.copy(alpha = 0.5f) else CardBorder,
                        RoundedCornerShape(18.dp)
                    )
            ) {
                OutlinedTextField(
                    value = postContent,
                    onValueChange = { if (it.length <= maxChars + 20) postContent = it },
                    placeholder = {
                        Text(
                            "Share an update, question, or announcement...",
                            color = TextMuted,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )
                    },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 160.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor   = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor   = White,
                        unfocusedTextColor = White,
                        cursorColor        = HotPink
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 15.sp, lineHeight = 24.sp, color = White),
                    maxLines = 12
                )
            }

            // Character counter
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp, end = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    "$remaining",
                    color = when {
                        isOverLimit      -> Color.Red
                        remaining <= 50  -> GoldYellow
                        else             -> TextMuted
                    },
                    fontSize = 12.sp,
                    fontWeight = if (remaining <= 50) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Tag selector ──
            SectionLabel("Tag your post")
            Spacer(Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(end = 4.dp)
            ) {
                items(postTags) { tag ->
                    val isSel = selectedTag == tag
                    val tc    = tagColor(tag)
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSel) Brush.linearGradient(listOf(tc, tc.copy(alpha = 0.7f)))
                                else Brush.linearGradient(listOf(White.copy(alpha = 0.06f), White.copy(alpha = 0.06f)))
                            )
                            .border(1.dp, if (isSel) Color.Transparent else White.copy(alpha = 0.10f), CircleShape)
                            .clickable { selectedTag = tag }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            tag,
                            color      = if (isSel) White else TextMuted,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            fontSize   = 12.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Preview card ──
            if (postContent.isNotBlank()) {
                SectionLabel("Preview")
                Spacer(Modifier.height(10.dp))

                val tc = tagColor(selectedTag)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(CardBg)
                        .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(FireGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("You", color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Just now", color = TextMuted, fontSize = 11.sp)
                        }
                        Spacer(Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(tc.copy(alpha = 0.12f))
                                .border(1.dp, tc.copy(alpha = 0.28f), CircleShape)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(selectedTag, color = tc, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        postContent,
                        color = White.copy(alpha = 0.85f),
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }

                Spacer(Modifier.height(24.dp))
            }

            // ── Error message ──
            if (errorMessage.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Red.copy(alpha = 0.08f))
                        .border(1.dp, Color.Red.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ErrorOutline, null, tint = Color.Red.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(errorMessage, color = Color.Red.copy(alpha = 0.8f), fontSize = 13.sp)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── Submit button ──
            Button(
                onClick = { submitPost() },
                enabled = canPost,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (canPost) FireGradient
                            else Brush.linearGradient(listOf(White.copy(alpha = 0.08f), White.copy(alpha = 0.08f))),
                            RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPosting) {
                        CircularProgressIndicator(color = White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Send, null, tint = if (canPost) White else TextMuted, modifier = Modifier.size(18.dp))
                            Text(
                                "Share Post",
                                color      = if (canPost) White else TextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 15.sp
                            )
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
fun CreatePostPreview() { CreatePostScreen(rememberNavController()) }