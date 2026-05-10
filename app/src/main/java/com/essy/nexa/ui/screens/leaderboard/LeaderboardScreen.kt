package com.essy.nexa.ui.screens.leaderboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
private val Silver       = Color(0xFFB0B8C8)
private val Bronze       = Color(0xFFCD7C3A)
private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.45f)
private val CardBorder   = White.copy(alpha = 0.07f)

private val FireGradient   = Brush.linearGradient(listOf(HotPink, BlazeOrange, GoldYellow))
private val GoldGradient   = Brush.linearGradient(listOf(Color(0xFFFFD700), GoldYellow, Color(0xFFFFB300)))
private val SilverGradient = Brush.linearGradient(listOf(Color(0xFFD0D8E8), Silver))
private val BronzeGradient = Brush.linearGradient(listOf(Color(0xFFE8A060), Bronze))

// ─── Data ─────────────────────────────────────────────────────────────────────
data class LeaderboardEntry(
    val name: String,
    val course: String,
    val points: Int,
    val eventsAttended: Int,
    val postsCreated: Int,
    val badge: String
)

val leaderboard = listOf(
    LeaderboardEntry("Aisha Kamau",    "Computer Science", 1540, 24, 18, "🏆"),
    LeaderboardEntry("Brian Otieno",   "Business",         1320, 20, 22, "🥈"),
    LeaderboardEntry("Grace Wanjiru",  "Architecture",     1210, 18, 15, "🥉"),
    LeaderboardEntry("Kevin Mwangi",   "Computer Science",  980, 14,  8, "⭐"),
    LeaderboardEntry("Fatima Ahmed",   "Medicine",          870, 12, 11, "⭐"),
    LeaderboardEntry("James Njoroge",  "Engineering",       760, 10,  9, "⭐"),
    LeaderboardEntry("Rita Mwende",    "Law",               650,  8, 14, "⭐"),
    LeaderboardEntry("Tom Kariuki",    "Economics",         540,  7,  6, "⭐"),
    LeaderboardEntry("Sarah Wangui",   "Education",         430,  6,  8, "⭐"),
    LeaderboardEntry("Mark Odhiambo",  "Agriculture",       320,  4,  5, "⭐")
)

// ─── Rank → gradient ──────────────────────────────────────────────────────────
private fun rankGradient(rank: Int) = when (rank) {
    1    -> GoldGradient
    2    -> SilverGradient
    3    -> BronzeGradient
    else -> Brush.linearGradient(listOf(HotPink, BlazeOrange))
}

private fun rankColor(rank: Int) = when (rank) {
    1    -> GoldYellow
    2    -> Silver
    3    -> Bronze
    else -> HotPink
}

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
private fun LeaderboardBackground() {
    val inf = rememberInfiniteTransition(label = "bg")
    val o1 by inf.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val gr by inf.animateFloat(0f, 36f, infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")
    Box(modifier = Modifier.fillMaxSize().drawBehind {
        val w = size.width; val h = size.height
        val gs = 36.dp.toPx(); val gc = GoldYellow.copy(alpha = 0.03f); val lw = 0.5.dp.toPx()
        var gx = gr % gs; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += gs }
        var gy = gr % gs; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += gs }
        drawRadialOrb(Offset(w * 0.5f, h * 0.18f + o1 * 20.dp.toPx()), 220.dp.toPx(), GoldYellow, 0.5f)
        drawRadialOrb(Offset(w * 0.1f, h * 0.06f), 160.dp.toPx(), HotPink, 0.6f)
        drawRadialOrb(Offset(w * 0.9f, h * 0.4f + o1 * 10.dp.toPx()), 130.dp.toPx(), VioletDeep, 0.55f)
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

// ─── Podium item ──────────────────────────────────────────────────────────────
@Composable
private fun PodiumItem(entry: LeaderboardEntry, rank: Int, pillarHeight: Int) {
    val color    = rankColor(rank)
    val gradient = rankGradient(rank)
    val avatarSize = if (rank == 1) 60.dp else 48.dp
    val nameSize   = if (rank == 1) 13.sp else 11.sp
    val ptsSize    = if (rank == 1) 12.sp else 10.sp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Badge emoji
        Text(entry.badge, fontSize = if (rank == 1) 28.sp else 20.sp)
        Spacer(Modifier.height(6.dp))

        // Avatar ring
        Box(
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(gradient)
                .border(
                    2.dp,
                    if (rank == 1) Brush.linearGradient(listOf(GoldYellow, White.copy(alpha = 0.6f), GoldYellow))
                    else Brush.linearGradient(listOf(color, color.copy(alpha = 0.5f))),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(2.dp).clip(CircleShape).background(DarkSurface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    entry.name.take(1),
                    color = color,
                    fontWeight = FontWeight.Black,
                    fontSize = if (rank == 1) 26.sp else 18.sp
                )
            }
        }

        Spacer(Modifier.height(6.dp))
        Text(entry.name.split(" ").first(), color = White, fontSize = nameSize, fontWeight = FontWeight.SemiBold)
        Text("${entry.points}pts", color = color, fontSize = ptsSize, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))

        // Pillar
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(pillarHeight.dp)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(
                    Brush.verticalGradient(listOf(color.copy(alpha = 0.30f), color.copy(alpha = 0.10f)))
                )
                .border(
                    1.dp,
                    Brush.verticalGradient(listOf(color.copy(alpha = 0.50f), color.copy(alpha = 0.15f))),
                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("#$rank", color = color, fontWeight = FontWeight.Black, fontSize = 18.sp)
        }
    }
}

// ─── Leaderboard row ──────────────────────────────────────────────────────────
@Composable
private fun LeaderboardRow(entry: LeaderboardEntry, rank: Int, isMe: Boolean) {
    val color    = if (isMe) HotPink else rankColor(rank).let { if (rank > 3) TextMuted else it }
    val gradient = rankGradient(rank)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isMe)
                    Brush.linearGradient(listOf(HotPink.copy(alpha = 0.10f), BlazeOrange.copy(alpha = 0.08f)))
                else
                    Brush.linearGradient(listOf(CardBg, CardBg))
            )
            .border(
                1.dp,
                if (isMe) HotPink.copy(alpha = 0.25f) else CardBorder,
                RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank number
        Text(
            "#$rank",
            color = if (rank <= 3) rankColor(rank) else TextMuted,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.width(34.dp)
        )

        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isMe) FireGradient else gradient),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(1.5.dp).clip(CircleShape).background(DarkSurface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    entry.name.take(1),
                    color = if (isMe) HotPink else rankColor(rank),
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                entry.name,
                color = if (isMe) White else White.copy(alpha = 0.85f),
                fontWeight = if (isMe) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(entry.course, color = TextMuted, fontSize = 11.sp)
        }

        // Points
        Column(horizontalAlignment = Alignment.End) {
            Text(
                "${entry.points}",
                color = if (isMe) HotPink else if (rank <= 3) rankColor(rank) else White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
            Text("pts", color = TextMuted, fontSize = 10.sp)
        }
    }
}

// ─── LeaderboardScreen ────────────────────────────────────────────────────────
@Composable
fun LeaderboardScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        LeaderboardBackground()
        CornerBrackets()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            // ── Top bar ──
            item {
                Spacer(Modifier.height(16.dp))
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
                    Column {
                        Text("Leaderboard", color = White, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                        Text("Top students this semester", color = TextMuted, fontSize = 12.sp)
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── Podium ──
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Bottom
                ) {
                    PodiumItem(entry = leaderboard[1], rank = 2, pillarHeight = 80)
                    PodiumItem(entry = leaderboard[0], rank = 1, pillarHeight = 110)
                    PodiumItem(entry = leaderboard[2], rank = 3, pillarHeight = 60)
                }
                Spacer(Modifier.height(28.dp))
            }

            // ── Points key ──
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(CardBg)
                        .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf("🎯 Event = 50pts", "📝 Post = 20pts", "❤️ Like = 5pts").forEach { hint ->
                        Text(hint, color = GoldYellow, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── Section header ──
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(3.dp).height(16.dp).clip(CircleShape).background(FireGradient))
                    Spacer(Modifier.width(8.dp))
                    Text("Rankings", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp)
                }
                Spacer(Modifier.height(12.dp))
            }

            // ── Rows 4–10 ──
            itemsIndexed(leaderboard.drop(3)) { index, entry ->
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    LeaderboardRow(
                        entry = entry,
                        rank  = index + 4,
                        isMe  = entry.name == "Kevin Mwangi"
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun LeaderboardPreview() { LeaderboardScreen(rememberNavController()) }