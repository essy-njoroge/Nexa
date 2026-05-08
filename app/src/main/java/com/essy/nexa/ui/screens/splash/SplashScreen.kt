package com.essy.nexa.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

// ─── Color Palette ────────────────────────────────────────────────────────────
private val DeepMidnight = Color(0xFF06050F)
private val DarkSurface  = Color(0xFF0D0918)
private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val VioletDeep   = Color(0xFF7B2FFF)
private val White        = Color.White

// ─── Fire gradient (logo ring, accent bar, badge border) ─────────────────────
private val FireGradient = Brush.linearGradient(
    colors = listOf(HotPink, BlazeOrange, GoldYellow)
)

// ─── Radial orb helper (drawn on Canvas — zero blur hack) ────────────────────
private fun DrawScope.drawRadialOrb(
    center: Offset,
    radius: Float,
    color: Color,
    strength: Float = 1f
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color.copy(alpha = 0.45f * strength),
                color.copy(alpha = 0.20f * strength),
                color.copy(alpha = 0.08f * strength),
                Color.Transparent
            ),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
}

// ─── Background: grid + orbs + scan line — all on one Canvas ─────────────────
@Composable
private fun BackgroundEffects() {
    val inf = rememberInfiniteTransition(label = "bg")

    val orb1t by inf.animateFloat(0f, 1f,
        infiniteRepeatable(tween(7000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val orb2t by inf.animateFloat(0f, 1f,
        infiniteRepeatable(tween(9000, easing = EaseInOutSine), RepeatMode.Reverse), "o2")
    val orb3s by inf.animateFloat(1f, 1.35f,
        infiniteRepeatable(tween(11000, easing = EaseInOutSine), RepeatMode.Reverse), "o3")
    val scanT by inf.animateFloat(0f, 1f,
        infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Restart), "sc")
    val gridT by inf.animateFloat(0f, 36f,
        infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")

    Box(modifier = Modifier
        .fillMaxSize()
        .drawBehind {
            val w = size.width; val h = size.height

            // Grid lines
            val gs = 36.dp.toPx()
            val gc = HotPink.copy(alpha = 0.05f)
            val lw = 0.5.dp.toPx()
            var gx = gridT % gs; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += gs }
            var gy = gridT % gs; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += gs }

            // Orb 1 — hot pink, top-left, drifting
            drawRadialOrb(
                Offset((-80.dp.toPx() + orb1t * 30.dp.toPx()) + 170.dp.toPx(),
                    (-120.dp.toPx() + orb1t * 40.dp.toPx()) + 170.dp.toPx()),
                200.dp.toPx(), HotPink
            )

            // Orb 2 — gold, bottom-right, drifting
            drawRadialOrb(
                Offset((w + 60.dp.toPx() - orb2t * 25.dp.toPx()) - 150.dp.toPx(),
                    (h + 100.dp.toPx() - orb2t * 30.dp.toPx()) - 150.dp.toPx()),
                180.dp.toPx(), GoldYellow
            )

            // Orb 3 — violet, centre, breathing
            drawRadialOrb(
                Offset(w * 0.5f, h * 0.38f),
                130.dp.toPx() * orb3s, VioletDeep, 0.6f
            )

            // Scan line
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(Color.Transparent, HotPink.copy(alpha = 0.18f), Color.Transparent)
                ),
                start = Offset(0f, h * scanT),
                end   = Offset(w,  h * scanT),
                strokeWidth = 1.dp.toPx()
            )
        }
    )
}

// ─── Corner brackets ──────────────────────────────────────────────────────────
@Composable
private fun CornerBrackets() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top-left — pink
        Box(modifier = Modifier.align(Alignment.TopStart).padding(22.dp).size(22.dp)
            .drawBehind {
                val s = size.width; val sw = 1.5.dp.toPx(); val c = HotPink.copy(alpha = 0.55f)
                drawLine(c, Offset(s, 0f), Offset(0f, 0f), sw, cap = StrokeCap.Square)
                drawLine(c, Offset(0f, 0f), Offset(0f, s), sw, cap = StrokeCap.Square)
            })
        // Top-right — pink
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(22.dp).size(22.dp)
            .drawBehind {
                val s = size.width; val sw = 1.5.dp.toPx(); val c = HotPink.copy(alpha = 0.55f)
                drawLine(c, Offset(0f, 0f), Offset(s, 0f), sw, cap = StrokeCap.Square)
                drawLine(c, Offset(s, 0f), Offset(s, s), sw, cap = StrokeCap.Square)
            })
        // Bottom-left — gold
        Box(modifier = Modifier.align(Alignment.BottomStart).padding(22.dp).size(22.dp)
            .drawBehind {
                val s = size.width; val sw = 1.5.dp.toPx(); val c = GoldYellow.copy(alpha = 0.55f)
                drawLine(c, Offset(0f, s), Offset(s, s), sw, cap = StrokeCap.Square)
                drawLine(c, Offset(0f, 0f), Offset(0f, s), sw, cap = StrokeCap.Square)
            })
        // Bottom-right — gold
        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(22.dp).size(22.dp)
            .drawBehind {
                val s = size.width; val sw = 1.5.dp.toPx(); val c = GoldYellow.copy(alpha = 0.55f)
                drawLine(c, Offset(0f, s), Offset(s, s), sw, cap = StrokeCap.Square)
                drawLine(c, Offset(s, 0f), Offset(s, s), sw, cap = StrokeCap.Square)
            })
    }
}

// ─── Logo ─────────────────────────────────────────────────────────────────────
@Composable
private fun NexaLogo(scale: Float, alpha: Float) {
    Box(
        modifier = Modifier
            .scale(scale).alpha(alpha)
            .size(104.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(FireGradient),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize().padding(2.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(DarkSurface),
            contentAlignment = Alignment.Center
        ) {
            // Radial inner glow on canvas
            Box(modifier = Modifier.fillMaxSize().drawBehind {
                drawRadialOrb(
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = size.width * 0.6f,
                    color  = HotPink
                )
            })
            Text(
                text = "N",
                fontSize = 56.sp,
                fontWeight = FontWeight.Black,
                color = White,
                modifier = Modifier.offset(y = (-1).dp)
            )
        }
    }
}

// ─── Loading dots ─────────────────────────────────────────────────────────────
@Composable
private fun LoadingDots(alpha: Float) {
    val inf = rememberInfiniteTransition(label = "dots")
    val colors = listOf(HotPink, BlazeOrange, GoldYellow)
    val delays = listOf(0, 200, 400)

    val da = delays.mapIndexed { i, d ->
        inf.animateFloat(0.25f, 1f,
            infiniteRepeatable(tween(600, easing = EaseInOutSine, delayMillis = d), RepeatMode.Reverse),
            "da$i").value
    }
    val ds = delays.mapIndexed { i, d ->
        inf.animateFloat(0.8f, 1.25f,
            infiniteRepeatable(tween(600, easing = EaseInOutSine, delayMillis = d), RepeatMode.Reverse),
            "ds$i").value
    }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.alpha(alpha)) {
        colors.forEachIndexed { i, color ->
            Box(modifier = Modifier
                .size(8.dp).scale(ds[i]).alpha(da[i])
                .clip(CircleShape).background(color))
        }
    }
}

// ─── Main SplashScreen ────────────────────────────────────────────────────────
@Composable
fun SplashScreen(navController: NavController? = null) {
    val logoScale    = remember { Animatable(0f) }
    val logoAlpha    = remember { Animatable(0f) }
    val textAlpha    = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }
    val badgeAlpha   = remember { Animatable(0f) }
    val dotsAlpha    = remember { Animatable(0f) }

    LaunchedEffect(true) {
        logoScale.animateTo(1f, tween(800, easing = EaseOutBack))
        logoAlpha.animateTo(1f, tween(400))
        textAlpha.animateTo(1f, tween(600))
        delay(150)
        taglineAlpha.animateTo(1f, tween(600))
        delay(150)
        badgeAlpha.animateTo(1f, tween(500))
        delay(150)
        dotsAlpha.animateTo(1f, tween(500))
        delay(2200)
        navController?.navigate("onboarding") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(DeepMidnight),
        contentAlignment = Alignment.Center
    ) {
        BackgroundEffects()
        CornerBrackets()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            NexaLogo(scale = logoScale.value, alpha = logoAlpha.value)

            Spacer(Modifier.height(30.dp))

            Text(
                text = "NEXA",
                fontSize = 76.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 14.sp,
                color = White,
                modifier = Modifier.alpha(textAlpha.value)
            )

            Spacer(Modifier.height(8.dp))

            // Accent bar — fire gradient
            Box(
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .width(64.dp).height(3.dp)
                    .clip(CircleShape)
                    .background(FireGradient)
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Connecting Students & Opportunities",
                fontSize = 11.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 2.8.sp,
                color = White.copy(alpha = 0.45f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha.value).padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(36.dp))

            // Welcome badge
            Box(
                modifier = Modifier
                    .alpha(badgeAlpha.value)
                    .clip(CircleShape)
                    .background(HotPink.copy(alpha = 0.08f))
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            listOf(HotPink.copy(alpha = 0.40f), GoldYellow.copy(alpha = 0.40f))
                        ),
                        shape = CircleShape
                    )
                    .padding(horizontal = 26.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✦  Welcome to Nexa",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    color = HotPink
                )
            }

            Spacer(Modifier.height(44.dp))

            LoadingDots(alpha = dotsAlpha.value)
        }

        Box(
            modifier = Modifier.fillMaxSize().padding(bottom = 36.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "v1.0.0",
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                color = White.copy(alpha = 0.18f)
            )
        }
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}