package com.essy.nexa.ui.screens.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
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
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

// ─── Color palette (matches SplashScreen) ────────────────────────────────────
private val DeepMidnight = Color(0xFF06050F)
private val DarkSurface  = Color(0xFF0D0918)
private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val VioletDeep   = Color(0xFF7B2FFF)
private val VioletLight  = Color(0xFFA855F7)
private val White        = Color.White

// ─── Page model ───────────────────────────────────────────────────────────────
data class OnboardingPage(
    val emoji: String,
    val tag: String,
    val title: String,
    val subtitle: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color
)

val onboardingPages = listOf(
    OnboardingPage(
        emoji = "🎓",
        tag = "01 — DISCOVER",
        title = "Welcome\nto Nexa",
        subtitle = "Your all-in-one smart campus platform — events, clubs, chats, and career opportunities in one place.",
        primaryColor = HotPink,
        secondaryColor = BlazeOrange,
        accentColor = GoldYellow
    ),
    OnboardingPage(
        emoji = "🤖",
        tag = "02 — PERSONALIZE",
        title = "AI-Powered\nFor You",
        subtitle = "Nexa learns your interests and recommends the right events, study groups, and opportunities automatically.",
        primaryColor = VioletDeep,
        secondaryColor = HotPink,
        accentColor = VioletLight
    ),
    OnboardingPage(
        emoji = "🚀",
        tag = "03 — LAUNCH",
        title = "Launch\nYour Future",
        subtitle = "Connect with recruiters, showcase your skills, and land your dream internship — all from campus.",
        primaryColor = GoldYellow,
        secondaryColor = BlazeOrange,
        accentColor = HotPink
    )
)

// ─── Radial orb (canvas, no blur) ────────────────────────────────────────────
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRadialOrb(
    center: Offset,
    radius: Float,
    color: Color,
    strength: Float = 1f
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color.copy(alpha = 0.40f * strength),
                color.copy(alpha = 0.18f * strength),
                color.copy(alpha = 0.06f * strength),
                Color.Transparent
            ),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
}

// ─── Animated background (grid + orbs + scan) ────────────────────────────────
@Composable
private fun OnboardingBackground(page: OnboardingPage) {
    val inf = rememberInfiniteTransition(label = "bg")

    val orb1t by inf.animateFloat(0f, 1f,
        infiniteRepeatable(tween(7000, easing = EaseInOutSine), RepeatMode.Reverse), "o1")
    val orb2t by inf.animateFloat(0f, 1f,
        infiniteRepeatable(tween(9000, easing = EaseInOutSine), RepeatMode.Reverse), "o2")
    val scanT by inf.animateFloat(0f, 1f,
        infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Restart), "sc")
    val gridT by inf.animateFloat(0f, 36f,
        infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart), "gr")

    // Animate colors when page changes
    val primary = page.primaryColor
    val accent  = page.accentColor

    Box(modifier = Modifier
        .fillMaxSize()
        .drawBehind {
            val w = size.width; val h = size.height

            // Grid
            val gs = 36.dp.toPx()
            val gc = White.copy(alpha = 0.03f)
            val lw = 0.5.dp.toPx()
            var gx = gridT % gs; while (gx < w) { drawLine(gc, Offset(gx,0f), Offset(gx,h), lw); gx += gs }
            var gy = gridT % gs; while (gy < h) { drawLine(gc, Offset(0f,gy), Offset(w,gy), lw); gy += gs }

            // Orb top-left
            drawRadialOrb(
                Offset((-80.dp.toPx() + orb1t * 30.dp.toPx()) + 150.dp.toPx(),
                    (-100.dp.toPx() + orb1t * 40.dp.toPx()) + 150.dp.toPx()),
                190.dp.toPx(), primary
            )

            // Orb bottom-right
            drawRadialOrb(
                Offset((w + 60.dp.toPx() - orb2t * 25.dp.toPx()) - 130.dp.toPx(),
                    (h + 80.dp.toPx() - orb2t * 30.dp.toPx()) - 130.dp.toPx()),
                170.dp.toPx(), accent
            )

            // Scan line
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(Color.Transparent, primary.copy(alpha = 0.15f), Color.Transparent)
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
private fun CornerBrackets(page: OnboardingPage) {
    Box(modifier = Modifier.fillMaxSize()) {
        val topColor    = page.primaryColor.copy(alpha = 0.55f)
        val bottomColor = page.accentColor.copy(alpha = 0.55f)
        val sw = 1.5.dp

        // Top-left
        Box(modifier = Modifier.align(Alignment.TopStart).padding(18.dp).size(20.dp)
            .drawBehind {
                val s = size.width; val w = sw.toPx()
                drawLine(topColor, Offset(s,0f), Offset(0f,0f), w, cap = StrokeCap.Square)
                drawLine(topColor, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square)
            })
        // Top-right
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(18.dp).size(20.dp)
            .drawBehind {
                val s = size.width; val w = sw.toPx()
                drawLine(topColor, Offset(0f,0f), Offset(s,0f), w, cap = StrokeCap.Square)
                drawLine(topColor, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square)
            })
        // Bottom-left
        Box(modifier = Modifier.align(Alignment.BottomStart).padding(18.dp).size(20.dp)
            .drawBehind {
                val s = size.width; val w = sw.toPx()
                drawLine(bottomColor, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square)
                drawLine(bottomColor, Offset(0f,0f), Offset(0f,s), w, cap = StrokeCap.Square)
            })
        // Bottom-right
        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(18.dp).size(20.dp)
            .drawBehind {
                val s = size.width; val w = sw.toPx()
                drawLine(bottomColor, Offset(0f,s), Offset(s,s), w, cap = StrokeCap.Square)
                drawLine(bottomColor, Offset(s,0f), Offset(s,s), w, cap = StrokeCap.Square)
            })
    }
}

// ─── Emoji card ───────────────────────────────────────────────────────────────
@Composable
private fun EmojiCard(page: OnboardingPage) {
    val ringGradient = Brush.linearGradient(
        listOf(page.primaryColor, page.secondaryColor, page.accentColor)
    )

    Box(
        modifier = Modifier.size(140.dp).clip(RoundedCornerShape(36.dp)).background(ringGradient),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize().padding(2.dp)
                .clip(RoundedCornerShape(34.dp))
                .background(DarkSurface),
            contentAlignment = Alignment.Center
        ) {
            // Inner radial glow on canvas
            Box(modifier = Modifier.fillMaxSize().drawBehind {
                drawRadialOrb(
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = size.width * 0.55f,
                    color  = page.primaryColor
                )
            })
            Text(text = page.emoji, fontSize = 60.sp)
        }
    }
}

// ─── Dot indicators ───────────────────────────────────────────────────────────
@Composable
private fun DotIndicators(currentPage: Int, pageCount: Int, primaryColor: Color) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(pageCount) { index ->
            val isSelected = currentPage == index
            val width by animateDpAsState(
                targetValue = if (isSelected) 28.dp else 6.dp,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "dot_w_$index"
            )
            Box(
                modifier = Modifier
                    .height(6.dp).width(width)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) primaryColor
                        else White.copy(alpha = 0.25f)
                    )
            )
        }
    }
}

// ─── Progress bar ─────────────────────────────────────────────────────────────
@Composable
private fun ProgressBar(currentPage: Int, pageCount: Int, page: OnboardingPage) {
    val fillPct by animateFloatAsState(
        targetValue = (currentPage + 1f) / pageCount.toFloat(),
        animationSpec = tween(500, easing = EaseInOutSine),
        label = "progress"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .clip(CircleShape)
            .background(White.copy(alpha = 0.08f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fillPct)
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(
                        listOf(page.primaryColor, page.accentColor)
                    )
                )
        )
    }
}

// ─── Main OnboardingScreen ────────────────────────────────────────────────────
@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val currentPage = onboardingPages[pagerState.currentPage]
    val fireGradient = Brush.linearGradient(
        listOf(currentPage.primaryColor, currentPage.secondaryColor)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {
        // Background effects
        OnboardingBackground(page = currentPage)
        CornerBrackets(page = currentPage)

        // Skip button
        TextButton(
            onClick = {
                navController.navigate("login") {
                    popUpTo("onboarding") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(18.dp)
                .clip(CircleShape)
                .background(White.copy(alpha = 0.07f))
                .border(1.dp, White.copy(alpha = 0.12f), CircleShape)
        ) {
            Text(
                text = "SKIP",
                color = White.copy(alpha = 0.70f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(64.dp))

            // ── Carousel ──
            HorizontalPager(
                count = onboardingPages.size,
                state = pagerState,
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) { pageIndex ->
                val page = onboardingPages[pageIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Emoji card
                    EmojiCard(page = page)

                    Spacer(Modifier.height(32.dp))

                    // Tag pill
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(page.primaryColor.copy(alpha = 0.10f))
                            .border(1.dp, page.primaryColor.copy(alpha = 0.28f), CircleShape)
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = page.tag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = page.primaryColor
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Title — big, Bebas-style weight
                    Text(
                        text = page.title,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = White,
                        textAlign = TextAlign.Center,
                        lineHeight = 46.sp
                    )

                    Spacer(Modifier.height(18.dp))

                    // Subtitle
                    Text(
                        text = page.subtitle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light,
                        color = White.copy(alpha = 0.55f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Bottom controls ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .padding(bottom = 42.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Progress bar
                ProgressBar(
                    currentPage = pagerState.currentPage,
                    pageCount = onboardingPages.size,
                    page = currentPage
                )

                // Dot indicators
                DotIndicators(
                    currentPage = pagerState.currentPage,
                    pageCount = onboardingPages.size,
                    primaryColor = currentPage.primaryColor
                )

                // Next / Get Started button
                Button(
                    onClick = {
                        if (pagerState.currentPage < onboardingPages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            navController.navigate("login") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(50.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(fireGradient, RoundedCornerShape(50.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (pagerState.currentPage < onboardingPages.size - 1)
                                "Next  →"
                            else
                                "Get Started 🚀",
                            color = White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun OnboardingPreview() {
    OnboardingScreen(rememberNavController())
}