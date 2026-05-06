package com.essy.nexa.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController? = null) {

    val logoScale = remember { Animatable(0f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }
    val dotsAlpha = remember { Animatable(0f) }

    val pulse = rememberInfiniteTransition(label = "pulse")

    val ring1Scale by pulse.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = EaseInOutSine),
            RepeatMode.Reverse
        ),
        label = "ring1"
    )

    val ring2Scale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            tween(2600, easing = EaseInOutSine),
            RepeatMode.Reverse
        ),
        label = "ring2"
    )

    val dot1Alpha by pulse.animateFloat(0.3f, 1f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "d1"
    )

    val dot2Alpha by pulse.animateFloat(0.3f, 1f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 200), RepeatMode.Reverse),
        label = "d2"
    )

    val dot3Alpha by pulse.animateFloat(0.3f, 1f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 400), RepeatMode.Reverse),
        label = "d3"
    )

    LaunchedEffect(true) {
        logoScale.animateTo(1f, tween(700, easing = EaseOutBack))
        logoAlpha.animateTo(1f, tween(400))

        textAlpha.animateTo(1f, tween(600))
        delay(200)

        taglineAlpha.animateTo(1f, tween(700))
        delay(200)

        dotsAlpha.animateTo(1f, tween(500))

        delay(2000)

        navController?.navigate("onboarding") {
            popUpTo("splash") { inclusive = true }
        }
    }

    // ✅ CLEAN NEXA THEME GRADIENT (NO BLUE)
    val gradient = Brush.verticalGradient(
        colors = listOf(
            NexaPrimary,
            Color(0xFF0D9488), // teal accent
            Color.Black.copy(alpha = 0.85f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {

        // Glow effects (kept subtle, no blue)
        Box(
            modifier = Modifier
                .size(340.dp)
                .scale(ring2Scale)
                .blur(60.dp)
                .background(
                    NexaPrimary.copy(alpha = 0.25f),
                    CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(220.dp)
                .scale(ring1Scale)
                .blur(40.dp)
                .background(
                    Color(0xFF0D9488).copy(alpha = 0.25f),
                    CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            // Logo
            Box(
                modifier = Modifier
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
                    .size(96.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.18f),
                                Color.White.copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .blur(16.dp)
                        .background(NexaPrimary.copy(alpha = 0.6f), CircleShape)
                )

                Text(
                    text = "N",
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text = "NEXA",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 10.sp,
                modifier = Modifier.alpha(textAlpha.value)
            )

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .width(48.dp)
                    .height(2.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(NexaPrimary, Color(0xFF0D9488))
                        )
                    )
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Connecting students, skills & opportunities",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.65f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(taglineAlpha.value)
                    .padding(horizontal = 40.dp)
            )

            Spacer(Modifier.height(40.dp))

            Text(
                text = "Welcome to Nexa",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.alpha(taglineAlpha.value)
            )

            Spacer(Modifier.height(40.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.alpha(dotsAlpha.value)
            ) {
                listOf(dot1Alpha, dot2Alpha, dot3Alpha).forEach { a ->
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .alpha(a)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 36.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "v1.0.0",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}