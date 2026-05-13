package com.essy.nexa.ui.screens.opportunities

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
private val CobaltBlue   = Color(0xFF00A3FF)
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.45f)
private val CardBorder   = White.copy(alpha = 0.07f)

private val FireGradient = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))

private val jobTypes = listOf("Internship", "Graduate Role", "Part-time", "Remote")

private fun typeColor(type: String) = when (type) {
    "Internship"    -> HotPink
    "Graduate Role" -> GoldYellow
    "Part-time"     -> BlazeOrange
    "Remote"        -> CobaltBlue
    else            -> VioletDeep
}

// ─── Background ───────────────────────────────────────────────────────────────
@Composable
private fun CreateOppBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val o1  by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr  by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")

    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val gs = 36.dp.toPx(); val gc = HotPink.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
        var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += gs }
        var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += gs }
        drawCircle(
            brush = Brush.radialGradient(listOf(HotPink.copy(0.3f * (0.5f + o1 * 0.5f)), Color.Transparent),
                center = Offset(w * 0.1f, h * 0.1f), radius = 200.dp.toPx()),
            radius = 200.dp.toPx(), center = Offset(w * 0.1f, h * 0.1f)
        )
        drawCircle(
            brush = Brush.radialGradient(listOf(VioletDeep.copy(0.2f), Color.Transparent),
                center = Offset(w * 0.9f, h * 0.5f), radius = 160.dp.toPx()),
            radius = 160.dp.toPx(), center = Offset(w * 0.9f, h * 0.5f)
        )
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
        Text(text, color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

// ─── Styled text field ────────────────────────────────────────────────────────
@Composable
private fun NexaField(
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(placeholder, color = TextMuted, fontSize = 14.sp) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        maxLines = maxLines,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = HotPink.copy(alpha = 0.55f),
            unfocusedBorderColor    = White.copy(alpha = 0.10f),
            focusedContainerColor   = White.copy(alpha = 0.04f),
            unfocusedContainerColor = White.copy(alpha = 0.04f),
            focusedTextColor        = White,
            unfocusedTextColor      = White,
            cursorColor             = HotPink
        ),
        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = White)
    )
}

// ─── CreateOpportunityScreen ──────────────────────────────────────────────────
@Composable
fun CreateOpportunityScreen(navController: NavController) {
    val db  = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var title        by remember { mutableStateOf("") }
    var company      by remember { mutableStateOf("") }
    var location     by remember { mutableStateOf("") }
    var deadline     by remember { mutableStateOf("") }
    var description  by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Internship") }
    var skillInput   by remember { mutableStateOf("") }
    var skills       by remember { mutableStateOf<List<String>>(emptyList()) }
    var isPosting    by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val canSubmit = title.isNotBlank() && company.isNotBlank() &&
            location.isNotBlank() && description.isNotBlank() && !isPosting

    fun addSkill() {
        val s = skillInput.trim()
        if (s.isNotEmpty() && !skills.contains(s)) {
            skills = skills + s
            skillInput = ""
        }
    }

    fun submit() {
        if (!canSubmit || uid == null) return
        isPosting    = true
        errorMessage = ""

        val opp = hashMapOf(
            "title"       to title.trim(),
            "company"     to company.trim(),
            "type"        to selectedType,
            "location"    to location.trim(),
            "deadline"    to deadline.trim().ifEmpty { "Open" },
            "description" to description.trim(),
            "skills"      to skills,
            "createdBy"   to uid,
            "createdAt"   to FieldValue.serverTimestamp()
        )

        db.collection("opportunities").add(opp)
            .addOnSuccessListener {
                isPosting = false
                navController.popBackStack()
            }
            .addOnFailureListener { e ->
                isPosting    = false
                errorMessage = e.message ?: "Failed to post. Please try again."
            }
    }

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        CreateOppBackground()
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
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                        .background(White.copy(alpha = 0.06f))
                        .border(1.dp, White.copy(alpha = 0.10f), CircleShape)
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Close, null, tint = White, modifier = Modifier.size(18.dp))
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Post Opportunity", color = White, fontSize = 18.sp, fontWeight = FontWeight.Black)
                    Text("Admin only", color = HotPink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (canSubmit) FireGradient else Brush.linearGradient(listOf(White.copy(0.08f), White.copy(0.08f))))
                        .clickable(enabled = canSubmit) { submit() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (isPosting) {
                        CircularProgressIndicator(color = White, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                    } else {
                        Text("Post", color = if (canSubmit) White else TextMuted, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Job title ──
            SectionLabel("Job Title")
            Spacer(Modifier.height(8.dp))
            NexaField(title, { title = it }, "e.g. Android Developer Intern")

            Spacer(Modifier.height(20.dp))

            // ── Company ──
            SectionLabel("Company")
            Spacer(Modifier.height(8.dp))
            NexaField(company, { company = it }, "e.g. TechCorp Kenya")

            Spacer(Modifier.height(20.dp))

            // ── Type selector ──
            SectionLabel("Job Type")
            Spacer(Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(jobTypes) { type ->
                    val isSel = selectedType == type
                    val tc    = typeColor(type)
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSel) Brush.linearGradient(listOf(tc, tc.copy(alpha = 0.7f)))
                                else Brush.linearGradient(listOf(White.copy(0.06f), White.copy(0.06f)))
                            )
                            .border(1.dp, if (isSel) Color.Transparent else White.copy(0.10f), CircleShape)
                            .clickable { selectedType = type }
                            .padding(horizontal = 16.dp, vertical = 9.dp)
                    ) {
                        Text(type, color = if (isSel) White else TextMuted, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal, fontSize = 12.sp)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Location ──
            SectionLabel("Location")
            Spacer(Modifier.height(8.dp))
            NexaField(location, { location = it }, "e.g. Nairobi (Hybrid) or Remote")

            Spacer(Modifier.height(20.dp))

            // ── Deadline ──
            SectionLabel("Application Deadline")
            Spacer(Modifier.height(8.dp))
            NexaField(deadline, { deadline = it }, "e.g. 31 May 2026  (leave blank = Open)")

            Spacer(Modifier.height(20.dp))

            // ── Description ──
            SectionLabel("Description")
            Spacer(Modifier.height(8.dp))
            NexaField(description, { description = it }, "Describe the role, responsibilities, and what to expect...", maxLines = 5)

            Spacer(Modifier.height(20.dp))

            // ── Skills ──
            SectionLabel("Required Skills")
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = skillInput,
                    onValueChange = { skillInput = it },
                    placeholder = { Text("e.g. Kotlin", color = TextMuted, fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor      = HotPink.copy(alpha = 0.55f),
                        unfocusedBorderColor    = White.copy(alpha = 0.10f),
                        focusedContainerColor   = White.copy(alpha = 0.04f),
                        unfocusedContainerColor = White.copy(alpha = 0.04f),
                        focusedTextColor        = White,
                        unfocusedTextColor      = White,
                        cursorColor             = HotPink
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = White)
                )
                Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                        .background(if (skillInput.isNotBlank()) FireGradient else Brush.linearGradient(listOf(White.copy(0.08f), White.copy(0.08f))))
                        .clickable(enabled = skillInput.isNotBlank()) { addSkill() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, null, tint = if (skillInput.isNotBlank()) White else TextMuted, modifier = Modifier.size(20.dp))
                }
            }

            if (skills.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(skills) { skill ->
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(HotPink.copy(alpha = 0.10f))
                                .border(1.dp, HotPink.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(skill, color = White, fontSize = 12.sp)
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                Icons.Default.Close, null, tint = HotPink.copy(0.7f),
                                modifier = Modifier.size(12.dp).clickable { skills = skills - skill }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Error ──
            if (errorMessage.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Red.copy(alpha = 0.08f))
                        .border(1.dp, Color.Red.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ErrorOutline, null, tint = Color.Red.copy(0.8f), modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(errorMessage, color = Color.Red.copy(0.8f), fontSize = 13.sp)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── Submit button ──
            Button(
                onClick = { submit() },
                enabled = canSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(
                            if (canSubmit) FireGradient
                            else Brush.linearGradient(listOf(White.copy(0.08f), White.copy(0.08f))),
                            RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPosting) {
                        CircularProgressIndicator(color = White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Work, null, tint = if (canSubmit) White else TextMuted, modifier = Modifier.size(18.dp))
                            Text("Post Opportunity", color = if (canSubmit) White else TextMuted, fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
fun CreateOpportunityPreview() { CreateOpportunityScreen(rememberNavController()) }