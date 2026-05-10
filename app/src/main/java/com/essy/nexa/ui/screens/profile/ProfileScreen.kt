package com.essy.nexa.ui.screens.profile

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// ───────────────── COLORS ─────────────────
private val DeepMidnight = Color(0xFF06050F)
private val DarkSurface = Color(0xFF0D0918)
private val CardBg = Color(0xFF12101D)

private val HotPink = Color(0xFFFF2D9B)
private val BlazeOrange = Color(0xFFFF6400)
private val GoldYellow = Color(0xFFFFB300)
private val VioletDeep = Color(0xFF7B2FFF)
private val VioletLight = Color(0xFFA855F7)

private val White = Color.White
private val TextMuted = White.copy(alpha = 0.5f)
private val CardBorder = White.copy(alpha = 0.07f)

private val FireGradient = Brush.linearGradient(
    listOf(
        HotPink,
        BlazeOrange,
        GoldYellow
    )
)

private val PurpleFireGradient = Brush.linearGradient(
    listOf(
        VioletLight,
        HotPink,
        BlazeOrange
    )
)

// ───────────────── PROFILE SCREEN ─────────────────
@Composable
fun ProfileScreen(navController: NavController) {

    var isOpenToOpportunities by remember {
        mutableStateOf(true)
    }

    var userName by remember {
        mutableStateOf("Loading...")
    }

    var userCourse by remember {
        mutableStateOf("")
    }

    var userYear by remember {
        mutableStateOf("")
    }

    var userBio by remember {
        mutableStateOf("")
    }

    var userSkills by remember {
        mutableStateOf(listOf<String>())
    }

    var userInterests by remember {
        mutableStateOf(listOf<String>())
    }

    LaunchedEffect(Unit) {

        val uid =
            FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .addSnapshotListener { doc, _ ->

                    if (doc != null && doc.exists()) {

                        userName =
                            doc.getString("name") ?: "User"

                        userCourse =
                            doc.getString("course") ?: ""

                        userYear =
                            doc.getString("year") ?: ""

                        userBio =
                            doc.getString("bio") ?: ""

                        userSkills =
                            (doc.get("skills") as? List<String>)
                                ?: emptyList()

                        userInterests =
                            (doc.get("interests") as? List<String>)
                                ?: emptyList()

                        isOpenToOpportunities =
                            doc.getBoolean("openToOpportunities")
                                ?: true
                    }
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {

        PremiumBackground()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {

            // ───────────────── HEADER ─────────────────
            item {

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {

                        Text(
                            "My Profile",
                            color = White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(Modifier.height(2.dp))

                        Text(
                            "Your Nexa identity",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        TopIconButton(
                            icon = Icons.Default.Settings
                        ) {
                            navController.navigate("settings")
                        }

                        TopIconButton(
                            icon = Icons.Default.Edit
                        ) {
                            navController.navigate("edit_profile")
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            // ───────────────── HERO CARD ─────────────────
            item {

                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(CardBg)
                        .border(
                            1.dp,
                            CardBorder,
                            RoundedCornerShape(30.dp)
                        )
                        .padding(22.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // AVATAR
                        Box(
                            modifier = Modifier
                                .size(92.dp)
                                .clip(CircleShape)
                                .background(FireGradient)
                                .padding(3.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(DarkSurface),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    userName.take(1).uppercase(),
                                    color = White,
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    userName,
                                    color = White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black
                                )

                                Spacer(Modifier.width(6.dp))

                                Icon(
                                    Icons.Default.Verified,
                                    null,
                                    tint = GoldYellow,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(Modifier.height(4.dp))

                            Text(
                                "$userCourse • $userYear",
                                color = TextMuted,
                                fontSize = 12.sp
                            )

                            Spacer(Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                MiniBadge(
                                    text = "Nexa Pro",
                                    gradient = FireGradient
                                )

                                if (isOpenToOpportunities) {

                                    MiniBadge(
                                        text = "Open to Work",
                                        gradient = Brush.linearGradient(
                                            listOf(
                                                VioletLight,
                                                HotPink
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ACTION BUTTONS
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        ProfileActionButton(
                            modifier = Modifier.weight(1f),
                            text = "Resume",
                            icon = Icons.Default.Description,
                            gradient = FireGradient
                        )

                        ProfileActionButton(
                            modifier = Modifier.weight(1f),
                            text = "Share",
                            icon = Icons.Default.Share,
                            gradient = PurpleFireGradient
                        )
                    }

                    Spacer(Modifier.height(22.dp))

                    // STATS
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        StatCard(
                            modifier = Modifier.weight(1f),
                            value = "128",
                            label = "Connections",
                            color = HotPink,
                            icon = Icons.Default.People
                        )

                        StatCard(
                            modifier = Modifier.weight(1f),
                            value = "14",
                            label = "Projects",
                            color = BlazeOrange,
                            icon = Icons.Default.Code
                        )

                        StatCard(
                            modifier = Modifier.weight(1f),
                            value = "7",
                            label = "Badges",
                            color = GoldYellow,
                            icon = Icons.Default.EmojiEvents
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            // ───────────────── TRENDING CARD ─────────────────
            item {

                GlassCard(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    FireGradient
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurface),
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    Icons.Default.TrendingUp,
                                    null,
                                    tint = HotPink
                                )
                            }
                        }

                        Spacer(Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                "Your profile is trending",
                                color = White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                "Recruiters viewed your profile 12 times this week",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }

                        Icon(
                            Icons.Default.ArrowForwardIos,
                            null,
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            // ───────────────── ABOUT ─────────────────
            item {

                SectionTitle("About Me")

                GlassCard(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {

                    Text(
                        userBio.ifEmpty {
                            "No bio yet. Edit your profile and tell people who you are."
                        },
                        color = White.copy(alpha = 0.82f),
                        fontSize = 13.sp,
                        lineHeight = 22.sp
                    )
                }

                Spacer(Modifier.height(22.dp))
            }

            // ───────────────── SKILLS ─────────────────
            if (userSkills.isNotEmpty()) {

                item {

                    SectionTitle("Skills")

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        items(userSkills) { skill ->

                            NeonChip(
                                text = skill,
                                gradient = FireGradient
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))
                }
            }

            // ───────────────── INTERESTS ─────────────────
            if (userInterests.isNotEmpty()) {

                item {

                    SectionTitle("Interests")

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        items(userInterests) { interest ->

                            NeonChip(
                                text = interest,
                                gradient = Brush.linearGradient(
                                    listOf(
                                        VioletLight,
                                        HotPink
                                    )
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))
                }
            }

            // ───────────────── QUICK ACTIONS ─────────────────
            item {

                SectionTitle("Quick Actions")

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ActionCard(
                        icon = Icons.Default.Work,
                        title = "Career Opportunities",
                        subtitle = "View internships and jobs",
                        color = BlazeOrange
                    )

                    ActionCard(
                        icon = Icons.Default.Groups,
                        title = "Networking",
                        subtitle = "Connect with recruiters & students",
                        color = HotPink
                    )

                    ActionCard(
                        icon = Icons.Default.Bookmark,
                        title = "Saved Posts",
                        subtitle = "Access your saved content",
                        color = GoldYellow
                    )
                }

                Spacer(Modifier.height(26.dp))
            }
        }
    }
}

// ───────────────── PREMIUM BACKGROUND ─────────────────
private fun DrawScope.drawRadialOrb(
    center: Offset,
    radius: Float,
    color: Color
) {

    drawCircle(
        brush = Brush.radialGradient(
            listOf(
                color.copy(alpha = 0.28f),
                color.copy(alpha = 0.10f),
                Color.Transparent
            ),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
}

@Composable
private fun PremiumBackground() {

    val inf =
        rememberInfiniteTransition(label = "bg")

    val move by inf.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(
                9000,
                easing = EaseInOutSine
            ),
            RepeatMode.Reverse
        ),
        label = "move"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {

                val w = size.width
                val h = size.height

                drawRadialOrb(
                    Offset(w * 0.15f, h * 0.12f),
                    300.dp.toPx(),
                    HotPink
                )

                drawRadialOrb(
                    Offset(w * 0.9f, h * 0.3f),
                    260.dp.toPx(),
                    VioletDeep
                )

                drawRadialOrb(
                    Offset(
                        w * 0.6f + move * 80f,
                        h * 0.8f
                    ),
                    320.dp.toPx(),
                    BlazeOrange
                )
            }
    )
}

// ───────────────── TOP ICON ─────────────────
@Composable
private fun TopIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(
                White.copy(alpha = 0.05f)
            )
            .border(
                1.dp,
                White.copy(alpha = 0.08f),
                CircleShape
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Icon(
            icon,
            null,
            tint = White,
            modifier = Modifier.size(18.dp)
        )
    }
}

// ───────────────── GLASS CARD ─────────────────
@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardBg)
            .border(
                1.dp,
                CardBorder,
                RoundedCornerShape(24.dp)
            )
            .padding(18.dp),
        content = content
    )
}

// ───────────────── SECTION TITLE ─────────────────
@Composable
private fun SectionTitle(title: String) {

    Row(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .width(3.dp)
                .height(16.dp)
                .clip(CircleShape)
                .background(FireGradient)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            title,
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }

    Spacer(Modifier.height(12.dp))
}

// ───────────────── MINI BADGE ─────────────────
@Composable
private fun MiniBadge(
    text: String,
    gradient: Brush
) {

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(gradient)
            .padding(1.dp)
    ) {

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(DarkSurface)
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                )
        ) {

            Text(
                text,
                color = White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ───────────────── ACTION BUTTON ─────────────────
@Composable
private fun ProfileActionButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: Brush
) {

    Button(
        onClick = { },
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    gradient,
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    icon,
                    null,
                    tint = White,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text,
                    color = White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ───────────────── CHIP ─────────────────
@Composable
private fun NeonChip(
    text: String,
    gradient: Brush
) {

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(gradient)
            .padding(1.dp)
    ) {

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(CardBg)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {

            Text(
                text,
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

// ───────────────── STAT CARD ─────────────────
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(DarkSurface)
            .border(
                1.dp,
                color.copy(alpha = 0.22f),
                RoundedCornerShape(20.dp)
            )
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            icon,
            null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.height(10.dp))

        Text(
            value,
            color = White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(Modifier.height(4.dp))

        Text(
            label,
            color = TextMuted,
            fontSize = 11.sp
        )
    }
}

// ───────────────── ACTION CARD ─────────────────
@Composable
private fun ActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    color: Color
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(CardBg)
            .border(
                1.dp,
                CardBorder,
                RoundedCornerShape(22.dp)
            )
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(
                    color.copy(alpha = 0.14f)
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                icon,
                null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                title,
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(2.dp))

            Text(
                subtitle,
                color = TextMuted,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Icon(
            Icons.Default.ArrowForwardIos,
            null,
            tint = TextMuted,
            modifier = Modifier.size(14.dp)
        )
    }
}

// ───────────────── PREVIEW ─────────────────
@Preview(
    showBackground = true,
    backgroundColor = 0xFF06050F
)
@Composable
fun ProfilePreview() {

    ProfileScreen(
        rememberNavController()
    )
}