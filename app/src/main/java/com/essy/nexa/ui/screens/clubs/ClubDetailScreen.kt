package com.essy.nexa.ui.screens.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

val clubAnnouncements = listOf(
    "📅 Next meeting: Wednesday 7PM — Room C102. Topic: Building REST APIs with Kotlin",
    "🏆 We came 2nd at the National Tech Olympiad! Proud of every member who participated!",
    "📢 Committee applications for 2025/26 are open. Apply by May 30th."
)

// COLORS
private val DeepMidnight = Color(0xFF06050F)
private val HotPink = Color(0xFFFF2D9B)
private val BlazeOrange = Color(0xFFFF6400)
private val GoldYellow = Color(0xFFFFB300)
private val SoftWhite = Color(0xFFF5F7FF)

@Composable
fun ClubDetailScreen(navController: NavController) {

    val club = clubList[0]

    var isJoined by remember {
        mutableStateOf(club.isJoined)
    }

    val gradientBrush = Brush.horizontalGradient(
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

        // GLOW EFFECTS
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = (-60).dp, y = (-40).dp)
                .background(
                    HotPink.copy(alpha = 0.18f),
                    CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(240.dp)
                .offset(x = 220.dp, y = 120.dp)
                .background(
                    BlazeOrange.copy(alpha = 0.14f),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 22.dp,
                        vertical = 22.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Color.White.copy(alpha = 0.08f)
                        )
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {

                    Text(
                        text = "${club.iconEmoji} ${club.name}",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = club.category,
                        color = Color.White.copy(alpha = 0.55f),
                        fontSize = 13.sp
                    )
                }
            }

            // MAIN CONTENT
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 34.dp,
                            topEnd = 34.dp
                        )
                    )
                    .background(
                        SoftWhite.copy(alpha = 0.05f)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(
                            topStart = 34.dp,
                            topEnd = 34.dp
                        )
                    ),
                contentPadding = PaddingValues(
                    20.dp,
                    24.dp,
                    20.dp,
                    120.dp
                )
            ) {

                // HERO CARD
                item {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp))
                            .background(
                                gradientBrush
                            )
                            .padding(24.dp)
                    ) {

                        Column {

                            Text(
                                text = club.iconEmoji,
                                fontSize = 48.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = club.name,
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = club.description,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                ClubStatCard(
                                    title = "Members",
                                    value = "${club.members}"
                                )

                                ClubStatCard(
                                    title = "Events",
                                    value = "24"
                                )

                                ClubStatCard(
                                    title = "Active",
                                    value = "Weekly"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))
                }

                // JOIN BUTTON
                item {

                    Button(
                        onClick = {
                            isJoined = !isJoined
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        shape = RoundedCornerShape(50.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    if (isJoined)
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFF00C853),
                                                Color(0xFF64DD17)
                                            )
                                        )
                                    else
                                        gradientBrush,
                                    RoundedCornerShape(50.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    if (isJoined)
                                        Icons.Default.Check
                                    else
                                        Icons.Default.GroupAdd,
                                    contentDescription = null,
                                    tint = Color.White
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text =
                                        if (isJoined)
                                            "Joined Successfully"
                                        else
                                            "Join ${club.name}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                }

                // ABOUT
                item {

                    Text(
                        text = "About Club",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Color.White.copy(alpha = 0.05f)
                            )
                            .border(
                                1.dp,
                                Color.White.copy(alpha = 0.05f),
                                RoundedCornerShape(22.dp)
                            )
                            .padding(18.dp)
                    ) {

                        Text(
                            text =
                                club.description +
                                        "\n\nWe welcome students from all backgrounds and experience levels. Whether you're a beginner or expert, there's a place for you here.",
                            color = Color.White.copy(alpha = 0.78f),
                            fontSize = 14.sp,
                            lineHeight = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                }

                // ANNOUNCEMENTS TITLE
                item {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Campaign,
                            contentDescription = null,
                            tint = HotPink
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Recent Announcements",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // ANNOUNCEMENTS
                items(clubAnnouncements) { announcement ->

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Color.White.copy(alpha = 0.05f)
                            )
                            .border(
                                1.dp,
                                Color.White.copy(alpha = 0.05f),
                                RoundedCornerShape(22.dp)
                            )
                            .padding(16.dp)
                    ) {

                        Row {

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        gradientBrush
                                    ),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = club.iconEmoji,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {

                                Text(
                                    text = club.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = announcement,
                                    color = Color.White.copy(alpha = 0.72f),
                                    fontSize = 13.sp,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClubStatCard(
    title: String,
    value: String
) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                Color.White.copy(alpha = 0.16f)
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 11.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClubDetailPreview() {
    ClubDetailScreen(
        rememberNavController()
    )
}