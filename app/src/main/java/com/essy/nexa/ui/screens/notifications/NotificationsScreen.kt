package com.essy.nexa.ui.screens.notifications

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Notification

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

// ─── Notification type → icon + color ────────────────────────────────────────
fun notifIcon(type: String): ImageVector = when (type) {
    "event"     -> Icons.Default.Event
    "message"   -> Icons.Default.Chat
    "recruiter" -> Icons.Default.Work
    "ai"        -> Icons.Default.AutoAwesome
    "club"      -> Icons.Default.Groups
    "post"      -> Icons.Default.Article
    else        -> Icons.Default.Notifications
}

fun notifColor(type: String) = when (type) {
    "event"     -> HotPink
    "message"   -> CobaltBlue
    "recruiter" -> GoldYellow
    "ai"        -> VioletLight
    "club"      -> BlazeOrange
    "post"      -> HotPink
    else        -> TextMuted
}

val notifList = listOf(
    Notification("1","Event Reminder 📅","Annual Hackathon starts in 2 hours — Innovation Hub, Block C","2h ago","event",false),
    Notification("2","New Message 💬","Aisha Kamau sent you a message","3h ago","message",false),
    Notification("3","Recruiter Interest 💼","A recruiter from TechCorp viewed your profile","5h ago","recruiter",false),
    Notification("4","AI Recommendation ✨","3 new events match your interests this week","6h ago","ai",true),
    Notification("5","Club Update 🏫","Tech Club: Meeting today at 5PM in Room B204","8h ago","club",true),
    Notification("6","Event RSVP Confirmed ✅","You're registered for the Career Fair on Monday","Yesterday","event",true),
    Notification("7","New Post 📝","Business Club posted an announcement you might like","Yesterday","post",true),
    Notification("8","Study Group Match 🧠","Nexa found a study group for your Algorithms module","2 days ago","ai",true)
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
private fun NotifBackground() {
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

// ─── Notification row ─────────────────────────────────────────────────────────
@Composable
private fun NotifRow(notif: Notification, onClick: () -> Unit) {
    val color = notifColor(notif.type)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (!notif.isRead)
                    Brush.linearGradient(listOf(color.copy(alpha = 0.07f), White.copy(alpha = 0.03f)))
                else
                    Brush.linearGradient(listOf(CardBg, CardBg))
            )
            .border(
                1.dp,
                if (!notif.isRead) color.copy(alpha = 0.20f) else CardBorder,
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon circle
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f))
                .border(1.dp, color.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(notifIcon(notif.type), null, tint = color, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    notif.title,
                    color = if (!notif.isRead) White else White.copy(alpha = 0.70f),
                    fontWeight = if (!notif.isRead) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                if (!notif.isRead) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
            Spacer(Modifier.height(3.dp))
            Text(notif.message, color = TextMuted, fontSize = 12.sp, lineHeight = 18.sp)
            Spacer(Modifier.height(4.dp))
            Text(notif.timestamp, color = TextMuted.copy(alpha = 0.60f), fontSize = 10.sp, letterSpacing = 0.3.sp)
        }
    }
}

// ─── NotificationsScreen ──────────────────────────────────────────────────────
@Composable
fun NotificationsScreen(navController: NavController) {
    val notifications = remember { notifList.toMutableStateList() }
    val unreadCount   = notifications.count { !it.isRead }

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {
        NotifBackground()
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
                    Text("Notifications", color = White, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    if (unreadCount > 0) {
                        Text("$unreadCount unread", color = HotPink, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    } else {
                        Text("All caught up", color = TextMuted, fontSize = 11.sp)
                    }
                }

                // Mark all read
                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(HotPink.copy(alpha = 0.10f))
                            .border(1.dp, HotPink.copy(alpha = 0.25f), CircleShape)
                            .clickable { notifications.replaceAll { it.copy(isRead = true) } }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("Mark all read", color = HotPink, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Section header ──
            Row(modifier = Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.width(3.dp).height(14.dp).clip(CircleShape).background(FireGradient))
                Spacer(Modifier.width(8.dp))
                Text("Recent", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp)
            }

            Spacer(Modifier.height(10.dp))

            // ── List ──
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notifications, key = { it.id }) { notif ->
                    NotifRow(notif = notif) {
                        val idx = notifications.indexOf(notif)
                        if (idx != -1) notifications[idx] = notif.copy(isRead = true)
                    }
                }
                item { Spacer(Modifier.height(100.dp)) }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun NotificationsPreview() { NotificationsScreen(rememberNavController()) }