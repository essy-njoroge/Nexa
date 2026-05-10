package com.essy.nexa.ui.screens.lostfound

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// ─── Colors ───────────────────────────────────────────────────────────────────
private val DeepMidnight = Color(0xFF06050F)
private val DarkSurface  = Color(0xFF0D0918)
private val CardBg       = Color(0xFF100E1A)
private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val VioletDeep   = Color(0xFF7B2FFF)
private val TealGreen    = Color(0xFF00D4AA)
private val LostRed      = Color(0xFFFF4757)
private val FoundGreen   = Color(0xFF2ED573)
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.45f)
private val CardBorder   = White.copy(alpha = 0.07f)

private val FireGradient = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))

// ─── Data ─────────────────────────────────────────────────────────────────────
data class LostFoundItem(
    val id: String,
    val type: String,
    val title: String,
    val description: String,
    val location: String,
    val postedBy: String,
    val timeAgo: String,
    val isResolved: Boolean = false
)

val lfItems = listOf(
    LostFoundItem("1","Lost","Black Laptop Bag","Dell bag with charger inside, left in Library Block B","Library Block B","Kevin M.","1h ago"),
    LostFoundItem("2","Found","Student ID Card","Found near the cafeteria. Name: J. Otieno","Cafeteria","Aisha K.","2h ago"),
    LostFoundItem("3","Lost","AirPods Pro (White case)","Lost somewhere between ICT block and main gate","ICT Block","Grace W.","3h ago"),
    LostFoundItem("4","Found","Blue Water Bottle","Hydro Flask bottle found in Room C204","Room C204","Brian O.","5h ago"),
    LostFoundItem("5","Lost","Calculator (Casio fx-991)","Scientific calculator needed for exam tomorrow!","Engineering Block","James N.","6h ago"),
    LostFoundItem("6","Found","Keys (3 keys on a red keyring)","Found near the parking lot","Parking Lot","Fatima A.","Yesterday"),
    LostFoundItem("7","Lost","Purple Umbrella","Left in the cafeteria during yesterday's rain","Cafeteria","Rita M.","Yesterday",true),
    LostFoundItem("8","Found","Earphones (wired, black)","Found on a bench near the student centre","Student Centre","Tom K.","2 days ago",true)
)

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

// ─── Background ───────────────────────────────────────────────────────────────
@Composable
private fun LFBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val o1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")
    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val gs = 36.dp.toPx(); val gc = HotPink.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
        var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += gs }
        var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += gs }
        drawRadialOrb(Offset(w * 0.1f + o1 * 20.dp.toPx(), h * 0.06f), 170.dp.toPx(), HotPink)
        drawRadialOrb(Offset(w * 0.88f, h * 0.42f + o1 * 12.dp.toPx()), 140.dp.toPx(), VioletDeep, 0.7f)
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

// ─── Post dialog ──────────────────────────────────────────────────────────────
@Composable
private fun PostItemDialog(onDismiss: () -> Unit) {
    var postType     by remember { mutableStateOf("Lost") }
    var postTitle    by remember { mutableStateOf("") }
    var postDesc     by remember { mutableStateOf("") }
    var postLocation by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardBg,
        shape = RoundedCornerShape(24.dp),
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.width(3.dp).height(18.dp).clip(CircleShape).background(FireGradient))
                    Spacer(Modifier.width(8.dp))
                    Text("Post Item", color = White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                }
                Spacer(Modifier.height(4.dp))
                // Lost / Found type selector
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Lost" to LostRed, "Found" to FoundGreen).forEach { (type, color) ->
                        val isSel = postType == type
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isSel) color.copy(alpha = 0.18f) else White.copy(alpha = 0.05f))
                                .border(1.dp, if (isSel) color.copy(alpha = 0.45f) else White.copy(alpha = 0.10f), CircleShape)
                                .clickable { postType = type }
                                .padding(horizontal = 18.dp, vertical = 8.dp)
                        ) {
                            Text(type, color = if (isSel) color else TextMuted, fontSize = 13.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    Triple("ITEM NAME", postTitle, { v: String -> postTitle = v }),
                    Triple("DESCRIPTION", postDesc, { v: String -> postDesc = v }),
                    Triple("LOCATION", postLocation, { v: String -> postLocation = v })
                ).forEach { (label, value, onChange) ->
                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, color = TextMuted)
                        OutlinedTextField(
                            value = value, onValueChange = onChange,
                            singleLine = label != "DESCRIPTION",
                            modifier = Modifier.fillMaxWidth(),
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
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(42.dp).widthIn(min = 100.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize().background(FireGradient, RoundedCornerShape(50.dp)).padding(horizontal = 20.dp), contentAlignment = Alignment.Center) {
                    Text("Post", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted, fontSize = 13.sp)
            }
        }
    )
}

// ─── Item card ────────────────────────────────────────────────────────────────
@Composable
fun LostFoundCard(item: LostFoundItem) {
    val isLost     = item.type == "Lost"
    val typeColor  = if (isLost) LostRed else FoundGreen

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CardBg)
            .border(
                1.dp,
                if (item.isResolved) TealGreen.copy(alpha = 0.18f)
                else typeColor.copy(alpha = 0.15f),
                RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
    ) {
        // Top row
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(typeColor.copy(alpha = 0.12f))
                    .border(1.dp, typeColor.copy(alpha = 0.30f), CircleShape)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(item.type, color = typeColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }

            if (item.isResolved) {
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(TealGreen.copy(alpha = 0.10f))
                        .border(1.dp, TealGreen.copy(alpha = 0.25f), CircleShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Resolved ✓", color = TealGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.weight(1f))
            Text(item.timeAgo, color = TextMuted, fontSize = 11.sp)
        }

        Spacer(Modifier.height(10.dp))
        Text(item.title, color = White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(Modifier.height(4.dp))
        Text(item.description, color = TextMuted, fontSize = 13.sp, lineHeight = 20.sp)
        Spacer(Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = typeColor.copy(alpha = 0.70f), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(3.dp))
                Text(item.location, color = TextMuted, fontSize = 11.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, null, tint = typeColor.copy(alpha = 0.70f), modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(3.dp))
                Text(item.postedBy, color = TextMuted, fontSize = 11.sp)
            }
        }

        if (!item.isResolved) {
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Contact button
                Box(
                    modifier = Modifier
                        .weight(1f).height(38.dp)
                        .clip(CircleShape)
                        .background(typeColor.copy(alpha = 0.08f))
                        .border(1.dp, typeColor.copy(alpha = 0.30f), CircleShape)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Contact", color = typeColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                // Mark resolved button
                Box(
                    modifier = Modifier
                        .weight(1f).height(38.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                if (isLost) listOf(LostRed.copy(alpha = 0.80f), LostRed.copy(alpha = 0.60f))
                                else listOf(FoundGreen.copy(alpha = 0.80f), FoundGreen.copy(alpha = 0.60f))
                            )
                        )
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Mark Resolved", color = White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ─── LostFoundScreen ──────────────────────────────────────────────────────────
@Composable
fun LostFoundScreen(navController: NavController) {
    var selectedTab    by remember { mutableStateOf("All") }
    var searchQuery    by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var showDialog     by remember { mutableStateOf(false) }

    val tabs = listOf("All", "Lost", "Found", "Resolved")

    val filtered = lfItems.filter {
        val matchTab = when (selectedTab) {
            "Resolved" -> it.isResolved
            "All"      -> true
            else       -> it.type == selectedTab && !it.isResolved
        }
        val matchSearch = searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true)
        matchTab && matchSearch
    }

    if (showDialog) PostItemDialog(onDismiss = { showDialog = false })

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        LFBackground()
        CornerBrackets()

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(16.dp))

            // ── Top bar ──
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                    Text("Lost & Found", color = White, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Text("Help your fellow students", color = TextMuted, fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                        .background(if (isSearchActive) HotPink.copy(alpha = 0.15f) else White.copy(alpha = 0.06f))
                        .border(1.dp, if (isSearchActive) HotPink.copy(alpha = 0.35f) else White.copy(alpha = 0.10f), CircleShape)
                        .clickable { isSearchActive = !isSearchActive; if (!isSearchActive) searchQuery = "" },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Search, null, tint = if (isSearchActive) HotPink else White.copy(alpha = 0.80f), modifier = Modifier.size(18.dp))
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Search bar ──
            if (isSearchActive) {
                OutlinedTextField(
                    value = searchQuery, onValueChange = { searchQuery = it },
                    placeholder = { Text("Search items...", color = TextMuted, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = HotPink, modifier = Modifier.size(18.dp)) },
                    trailingIcon = { if (searchQuery.isNotEmpty()) IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Close, null, tint = TextMuted, modifier = Modifier.size(18.dp)) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HotPink.copy(alpha = 0.55f), unfocusedBorderColor = White.copy(alpha = 0.10f),
                        focusedContainerColor = White.copy(alpha = 0.04f), unfocusedContainerColor = White.copy(alpha = 0.04f),
                        focusedTextColor = White, unfocusedTextColor = White, cursorColor = HotPink
                    )
                )
                Spacer(Modifier.height(12.dp))
            }

            // ── Tab filters ──
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEach { tab ->
                    val isSel = selectedTab == tab
                    val tabColor = when (tab) {
                        "Lost"     -> LostRed
                        "Found"    -> FoundGreen
                        "Resolved" -> TealGreen
                        else       -> HotPink
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSel) Brush.linearGradient(listOf(tabColor, tabColor.copy(alpha = 0.75f)))
                                else Brush.linearGradient(listOf(White.copy(alpha = 0.06f), White.copy(alpha = 0.06f)))
                            )
                            .border(1.dp, if (isSel) Color.Transparent else White.copy(alpha = 0.10f), CircleShape)
                            .clickable { selectedTab = tab }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(tab, color = if (isSel) White else TextMuted, fontSize = 12.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Stats row ──
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val lostCount    = lfItems.count { it.type == "Lost" && !it.isResolved }
                val foundCount   = lfItems.count { it.type == "Found" && !it.isResolved }
                val resolvedCount = lfItems.count { it.isResolved }

                listOf(
                    Triple("$lostCount Active", "Lost", LostRed),
                    Triple("$foundCount Active", "Found", FoundGreen),
                    Triple("$resolvedCount Done", "Resolved", TealGreen)
                ).forEach { (count, label, color) ->
                    Box(
                        modifier = Modifier.weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color.copy(alpha = 0.08f))
                            .border(1.dp, color.copy(alpha = 0.18f), RoundedCornerShape(12.dp))
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(count, color = color, fontWeight = FontWeight.Black, fontSize = 14.sp)
                            Text(label, color = TextMuted, fontSize = 10.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // ── Section header ──
            Row(modifier = Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.width(3.dp).height(14.dp).clip(CircleShape).background(FireGradient))
                Spacer(Modifier.width(8.dp))
                Text("${filtered.size} item${if (filtered.size != 1) "s" else ""}", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(Modifier.height(10.dp))

            // ── List ──
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered, key = { it.id }) { item ->
                    LostFoundCard(item = item)
                }
                item { Spacer(Modifier.height(100.dp)) }
            }
        }

        // ── FAB ──
        Box(
            modifier = Modifier.fillMaxSize().padding(end = 20.dp, bottom = 96.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(FireGradient).clickable { showDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, null, tint = White, modifier = Modifier.size(26.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun LostFoundPreview() { LostFoundScreen(rememberNavController()) }