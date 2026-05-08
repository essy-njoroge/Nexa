package com.essy.nexa.ui.screens.events

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

// COLORS
private val DeepMidnight = Color(0xFF06050F)
private val HotPink = Color(0xFFFF2D9B)
private val BlazeOrange = Color(0xFFFF6400)
private val GoldYellow = Color(0xFFFFB300)
private val White = Color.White

@Composable
fun CheckInDoneScreen(
    navController: NavController
) {

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {
        scale.animateTo(
            1f,
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    val gradient = Brush.horizontalGradient(
        listOf(
            HotPink,
            BlazeOrange,
            GoldYellow
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(70.dp))

            // SUCCESS ICON
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(scale.value)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    HotPink.copy(alpha = 0.35f),
                                    BlazeOrange.copy(alpha = 0.15f),
                                    Color.Transparent
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(CircleShape)
                            .background(
                                gradient
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "You're In 🎉",
                    color = White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Your attendance has been successfully recorded.",
                    color = White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // DETAILS CARD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        White.copy(alpha = 0.05f)
                    )
                    .padding(22.dp)
            ) {

                Text(
                    text = "CHECK-IN DETAILS",
                    color = HotPink,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(22.dp))

                DetailRow(
                    icon = Icons.Default.Event,
                    title = "Event",
                    value = "Annual Hackathon 2025"
                )

                Spacer(modifier = Modifier.height(18.dp))

                DetailRow(
                    icon = Icons.Default.Schedule,
                    title = "Check-in Time",
                    value = "9:04 AM • Fri 16 May 2025"
                )

                Spacer(modifier = Modifier.height(18.dp))

                DetailRow(
                    icon = Icons.Default.LocationOn,
                    title = "Venue",
                    value = "Innovation Hub, Block C"
                )

                Spacer(modifier = Modifier.height(18.dp))

                DetailRow(
                    icon = Icons.Default.Badge,
                    title = "Attendee ID",
                    value = "#NXA-2025-0087"
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // BUTTONS
            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            gradient,
                            RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            tint = White
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Back To Home",
                            color = White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate("events")
                },
                border = BorderStroke(
                    1.dp,
                    White.copy(alpha = 0.12f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = White
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {

                Text(
                    text = "Browse More Events",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(34.dp))
        }
    }
}

@Composable
fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    White.copy(alpha = 0.06f)
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                icon,
                contentDescription = null,
                tint = HotPink,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {

            Text(
                text = title,
                color = White.copy(alpha = 0.45f),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                color = White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckInDonePreview() {

    CheckInDoneScreen(
        navController = rememberNavController()
    )
}