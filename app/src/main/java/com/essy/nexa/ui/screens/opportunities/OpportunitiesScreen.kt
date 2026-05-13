package com.essy.nexa.ui.screens.opportunities

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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Opportunity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

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
private val TealGreen    = Color(0xFF00D4AA)
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.55f)

private val FireGradient = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))

private fun typeColor(type: String) = when (type) {
    "Internship"    -> HotPink
    "Graduate Role" -> GoldYellow
    "Part-time"     -> BlazeOrange
    "Remote"        -> CobaltBlue
    else            -> VioletLight
}

// ─── Hardcoded fallback ───────────────────────────────────────────────────────
val opportunityListFallback = listOf(
    Opportunity("1","Android Developer Intern","TechCorp Kenya","Internship","Nairobi (Hybrid)","31 May 2026", listOf("Kotlin","Compose","Firebase"),"Build real Android features used by thousands."),
    Opportunity("2","UI/UX Design Intern","CreativeHub","Internship","Remote","15 Jun 2026", listOf("Figma","UI Design","Prototyping"),"Help shape products used across East Africa."),
    Opportunity("3","Junior Data Analyst","FinBank Ltd","Graduate Role","Nairobi","30 May 2026", listOf("Python","SQL","Data Viz"),"Analyse financial data to drive business decisions."),
    Opportunity("4","Campus Brand Ambassador","Safaricom","Part-time","On Campus","Open", listOf("Marketing","Communication","Social Media"),"Represent Safaricom on campus and earn commissions."),
    Opportunity("5","Software Engineering Intern","StartupHub Africa","Internship","Nairobi","20 Jun 2026", listOf("APIs","Problem Solving","Backend"),"Work on cutting-edge products in a fast-paced startup.")
)

private val opFilters = listOf("All","Internship","Graduate Role","Part-time","Remote")

// ─── Background ───────────────────────────────────────────────────────────────
private fun DrawScope.drawRadialOrb(center: Offset, radius: Float, color: Color, strength: Float = 1f) {
    drawCircle(
        brush = Brush.radialGradient(
            listOf(color.copy(alpha = 0.35f * strength), color.copy(alpha = 0.14f * strength), color.copy(alpha = 0.04f * strength), Color.Transparent),
            center = center, radius = radius
        ),
        radius = radius, center = center
    )
}

@Composable
private fun OppBackground() {
    val infinite = rememberInfiniteTransition(label = "bg")
    val orbAnim  by infinite.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "orb")
    val gridAnim by infinite.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "grid")

    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val grid = 36.dp.toPx(); val lw = 0.5.dp.toPx(); val gc = HotPink.copy(alpha = 0.03f)
        var gx = gridAnim % grid; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += grid }
        var gy = gridAnim % grid; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += grid }
        drawRadialOrb(Offset(w * 0.1f + orbAnim * 20.dp.toPx(), h * 0.06f), 170.dp.toPx(), HotPink)
        drawRadialOrb(Offset(w * 0.88f, h * 0.42f + orbAnim * 12.dp.toPx()), 140.dp.toPx(), VioletDeep, 0.7f)
    })
}

@Composable
private fun CornerBrackets() {
    val stroke = 1.5.dp
    Box(modifier = Modifier.fillMaxSize()) {
        listOf(
            Triple(Alignment.TopStart,   true,  true),
            Triple(Alignment.TopEnd,     true,  false),
            Triple(Alignment.BottomStart,false, true),
            Triple(Alignment.BottomEnd,  false, false)
        ).forEach { (alignment, top, left) ->
            val color = if (top) HotPink.copy(alpha = 0.45f) else GoldYellow.copy(alpha = 0.45f)
            Box(modifier = Modifier.align(alignment).padding(14.dp).size(18.dp).drawBehind {
                val s = size.width; val sw = stroke.toPx()
                if (top && left)   { drawLine(color, Offset(s,0f), Offset(0f,0f), sw, cap = StrokeCap.Square); drawLine(color, Offset(0f,0f), Offset(0f,s), sw, cap = StrokeCap.Square) }
                if (top && !left)  { drawLine(color, Offset(0f,0f), Offset(s,0f), sw, cap = StrokeCap.Square); drawLine(color, Offset(s,0f), Offset(s,s), sw, cap = StrokeCap.Square) }
                if (!top && left)  { drawLine(color, Offset(0f,s), Offset(s,s), sw, cap = StrokeCap.Square); drawLine(color, Offset(0f,0f), Offset(0f,s), sw, cap = StrokeCap.Square) }
                if (!top && !left) { drawLine(color, Offset(0f,s), Offset(s,s), sw, cap = StrokeCap.Square); drawLine(color, Offset(s,0f), Offset(s,s), sw, cap = StrokeCap.Square) }
            })
        }
    }
}

// ─── Applied confirmation dialog ─────────────────────────────────────────────
@Composable
private fun AppliedDialog(jobTitle: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(CardBg)
                .border(1.dp, TealGreen.copy(alpha = 0.25f), RoundedCornerShape(28.dp))
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(72.dp).clip(CircleShape)
                    .background(TealGreen.copy(alpha = 0.12f))
                    .border(2.dp, TealGreen.copy(alpha = 0.35f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, null, tint = TealGreen, modifier = Modifier.size(36.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text("Application Sent! 🎉", color = White, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(8.dp))
            Text(
                "You've applied for $jobTitle. We'll notify you once the recruiter reviews your profile.",
                color = TextMuted,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .background(Brush.linearGradient(listOf(TealGreen, Color(0xFF00A87C))))
                    .clickable { onDismiss() }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Got it", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

// ─── Opportunity card ─────────────────────────────────────────────────────────
@Composable
fun OpportunityCard(
    opp: Opportunity,
    isSaved: Boolean,        // FIX 4: passed from parent
    isApplied: Boolean,      // FIX 3: passed from parent
    onSave: () -> Unit,
    onApply: () -> Unit
) {
    // FIX 3: local dialog state
    var showAppliedDialog by remember { mutableStateOf(false) }

    val color = typeColor(opp.type)

    if (showAppliedDialog) {
        AppliedDialog(jobTitle = opp.title) { showAppliedDialog = false }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .border(1.dp, color.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape)
                    .background(Brush.linearGradient(listOf(color, color.copy(alpha = 0.6f)))),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(2.dp).clip(CircleShape).background(DarkSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Text(opp.company.take(1), color = color, fontWeight = FontWeight.Black, fontSize = 18.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(opp.title, color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(opp.company, color = TextMuted, fontSize = 12.sp)
            }
            // FIX 4: bookmark uses Firestore state
            Box(
                modifier = Modifier.size(38.dp).clip(CircleShape)
                    .background(if (isSaved) color.copy(alpha = 0.12f) else White.copy(alpha = 0.05f))
                    .border(1.dp, if (isSaved) color.copy(alpha = 0.30f) else White.copy(alpha = 0.08f), CircleShape)
                    .clickable { onSave() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    null, tint = if (isSaved) color else TextMuted, modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(opp.description, color = TextMuted, fontSize = 13.sp, lineHeight = 20.sp)
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = color.copy(alpha = 0.75f), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text(opp.location, color = TextMuted, fontSize = 11.sp)
            }
            Box(
                modifier = Modifier.clip(CircleShape)
                    .background(color.copy(alpha = 0.12f))
                    .border(1.dp, color.copy(alpha = 0.25f), CircleShape)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(opp.type, color = color, fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(opp.skills) { skill ->
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        .background(White.copy(alpha = 0.05f))
                        .border(1.dp, White.copy(alpha = 0.10f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(skill, color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        HorizontalDivider(color = White.copy(alpha = 0.06f))
        Spacer(Modifier.height(14.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccessTime, null, tint = GoldYellow.copy(alpha = 0.85f), modifier = Modifier.size(13.dp))
            Spacer(Modifier.width(4.dp))
            Text("Deadline: ${opp.deadline}", color = GoldYellow.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))

            // FIX 3: Apply button
            Button(
                onClick = {
                    if (!isApplied) {
                        onApply()
                        showAppliedDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            if (isApplied)
                                Brush.linearGradient(listOf(TealGreen, Color(0xFF00A87C)))
                            else
                                Brush.linearGradient(listOf(color, color.copy(alpha = 0.75f))),
                            RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isApplied) "✓ Applied" else "Apply →",
                        color = White, fontSize = 12.sp, fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ─── OpportunitiesScreen ──────────────────────────────────────────────────────
@Composable
fun OpportunitiesScreen(navController: NavController) {
    val db  = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var selectedFilter    by remember { mutableStateOf("All") }
    var firestoreOpps     by remember { mutableStateOf<List<Opportunity>>(emptyList()) }
    var isLoading         by remember { mutableStateOf(true) }
    var isAdmin           by remember { mutableStateOf(false) }
    var savedIds          by remember { mutableStateOf<Set<String>>(emptySet()) }
    var appliedIds        by remember { mutableStateOf<Set<String>>(emptySet()) }

    // FIX 1 + 2: Load opportunities, admin status, saved/applied sets
    DisposableEffect(uid) {
        if (uid != null) {
            // Check admin + load saved/applied from user doc
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    isAdmin = doc.getBoolean("isAdmin") ?: false
                    @Suppress("UNCHECKED_CAST")
                    savedIds   = ((doc.get("savedJobs")   as? List<String>) ?: emptyList()).toSet()
                    @Suppress("UNCHECKED_CAST")
                    appliedIds = ((doc.get("appliedJobs") as? List<String>) ?: emptyList()).toSet()
                }
        }

        val listener: ListenerRegistration = db.collection("opportunities")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                isLoading = false
                if (snapshot != null) {
                    firestoreOpps = snapshot.documents.mapNotNull { doc ->
                        try {
                            @Suppress("UNCHECKED_CAST")
                            Opportunity(
                                id          = doc.id,
                                title       = doc.getString("title") ?: return@mapNotNull null,
                                company     = doc.getString("company") ?: "",
                                type        = doc.getString("type") ?: "Internship",
                                location    = doc.getString("location") ?: "",
                                deadline    = doc.getString("deadline") ?: "Open",
                                skills      = (doc.get("skills") as? List<String>) ?: emptyList(),
                                description = doc.getString("description") ?: ""
                            )
                        } catch (e: Exception) { null }
                    }
                }
            }

        onDispose { listener.remove() }
    }

    val allOpps = if (firestoreOpps.isNotEmpty()) firestoreOpps else opportunityListFallback

    val filtered = allOpps.filter {
        selectedFilter == "All" || it.type == selectedFilter ||
                (selectedFilter == "Remote" && it.location.contains("Remote", ignoreCase = true))
    }

    // FIX 4: Toggle save — persists to Firestore
    fun toggleSave(opp: Opportunity) {
        if (uid == null) return
        val userRef = db.collection("users").document(uid)
        if (savedIds.contains(opp.id)) {
            userRef.update("savedJobs", FieldValue.arrayRemove(opp.id))
            savedIds = savedIds - opp.id
        } else {
            userRef.update("savedJobs", FieldValue.arrayUnion(opp.id))
            savedIds = savedIds + opp.id
        }
    }

    // FIX 3: Submit application — writes to Firestore
    fun applyToJob(opp: Opportunity) {
        if (uid == null || appliedIds.contains(opp.id)) return

        // Write to applications subcollection on the opportunity
        db.collection("opportunities").document(opp.id)
            .collection("applications").document(uid)
            .set(mapOf(
                "uid"       to uid,
                "oppId"     to opp.id,
                "title"     to opp.title,
                "company"   to opp.company,
                "appliedAt" to FieldValue.serverTimestamp()
            ))

        // Track on user doc too for easy querying
        db.collection("users").document(uid)
            .update("appliedJobs", FieldValue.arrayUnion(opp.id))

        appliedIds = appliedIds + opp.id
    }

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        OppBackground()
        CornerBrackets()

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 100.dp)) {

            // ── Top bar ───────────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(42.dp).clip(CircleShape)
                            .background(White.copy(alpha = 0.06f))
                            .border(1.dp, White.copy(alpha = 0.10f), CircleShape)
                            .clickable { navController.popBackStack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ArrowBack, null, tint = White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Opportunities", color = White, fontWeight = FontWeight.Black, fontSize = 24.sp)
                        Text("${filtered.size} roles available", color = TextMuted, fontSize = 12.sp)
                    }
                    // FIX 2: Admin-only add job button
                    if (isAdmin) {
                        Box(
                            modifier = Modifier.size(42.dp).clip(CircleShape)
                                .background(FireGradient)
                                .clickable { navController.navigate("create_opportunity") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, null, tint = White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
                Spacer(Modifier.height(22.dp))
            }

            // ── Profile banner ────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Brush.linearGradient(listOf(HotPink.copy(alpha = 0.14f), VioletDeep.copy(alpha = 0.14f))))
                        .border(1.dp, HotPink.copy(alpha = 0.22f), RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(CircleShape)
                                .background(HotPink.copy(alpha = 0.15f))
                                .border(1.dp, HotPink.copy(alpha = 0.30f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Visibility, null, tint = HotPink, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Your profile is live!", color = White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("2 recruiters viewed your profile this week", color = TextMuted, fontSize = 12.sp)
                        }
                        Icon(Icons.Default.ArrowForward, null, tint = HotPink.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            // ── Filters ───────────────────────────────────────────────────────
            item {
                LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(opFilters) { filter ->
                        val selected    = selectedFilter == filter
                        val filterColor = if (filter == "All") HotPink else typeColor(filter)
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (selected) Brush.linearGradient(listOf(filterColor, filterColor.copy(alpha = 0.75f)))
                                    else Brush.linearGradient(listOf(White.copy(alpha = 0.06f), White.copy(alpha = 0.06f)))
                                )
                                .border(1.dp, if (selected) Color.Transparent else White.copy(alpha = 0.10f), CircleShape)
                                .clickable { selectedFilter = filter }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(filter, color = if (selected) White else TextMuted, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
                Spacer(Modifier.height(18.dp))
            }

            // ── Section label ─────────────────────────────────────────────────
            item {
                Row(modifier = Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.width(3.dp).height(16.dp).clip(CircleShape).background(FireGradient))
                    Spacer(Modifier.width(8.dp))
                    Text("Open Roles", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(Modifier.height(14.dp))
            }

            // ── Job cards ─────────────────────────────────────────────────────
            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = HotPink, strokeWidth = 2.dp, modifier = Modifier.size(32.dp))
                    }
                }
            } else {
                items(filtered, key = { it.id }) { opp ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        OpportunityCard(
                            opp       = opp,
                            isSaved   = savedIds.contains(opp.id),
                            isApplied = appliedIds.contains(opp.id),
                            onSave    = { toggleSave(opp) },
                            onApply   = { applyToJob(opp) }
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun OpportunitiesPreview() { OpportunitiesScreen(rememberNavController()) }