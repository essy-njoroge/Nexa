package com.essy.nexa

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
private val DarkSurface = Color(0xFF0E0A19)
private val CardBg = Color(0xFF141122)

private val NeonPink = Color(0xFFFF2D9B)
private val NeonBlue = Color(0xFF00A3FF)
private val NeonPurple = Color(0xFF8B5CF6)
private val NeonGreen = Color(0xFFD7FF00)
private val Gold = Color(0xFFFFB800)

private val White = Color.White
private val TextMuted = White.copy(alpha = 0.6f)

private val MainGradient = Brush.linearGradient(
    listOf(NeonPink, NeonPurple, NeonBlue)
)

// ───────────────── PROFILE SCREEN ─────────────────
@Composable
fun ProfileScreen(navController: NavController) {

    var isOpenToOpportunities by remember { mutableStateOf(true) }

    var userName by remember { mutableStateOf("Loading...") }
    var userCourse by remember { mutableStateOf("") }
    var userYear by remember { mutableStateOf("") }
    var userBio by remember { mutableStateOf("") }

    var userSkills by remember { mutableStateOf(listOf<String>()) }
    var userInterests by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .addSnapshotListener { doc, _ ->

                    if (doc != null && doc.exists()) {

                        userName = doc.getString("name") ?: "User"
                        userCourse = doc.getString("course") ?: ""
                        userYear = doc.getString("year") ?: ""
                        userBio = doc.getString("bio") ?: ""

                        userSkills =
                            (doc.get("skills") as? List<String>) ?: emptyList()

                        userInterests =
                            (doc.get("interests") as? List<String>) ?: emptyList()

                        isOpenToOpportunities =
                            doc.getBoolean("openToOpportunities") ?: true
                    }
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {

        NeonBackground()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {

            // ───────────────── TOP BAR ─────────────────
            item {

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        "My Profile",
                        color = White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                        IconCircle(
                            icon = Icons.Default.Settings,
                            onClick = {
                                navController.navigate("settings")
                            }
                        )

                        IconCircle(
                            icon = Icons.Default.Edit,
                            onClick = {
                                navController.navigate("edit_profile")
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // ───────────────── PROFILE CARD ─────────────────
            item {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(95.dp)
                            .clip(CircleShape)
                            .background(MainGradient)
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
                                fontWeight = FontWeight.Black,
                                fontSize = 38.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            userName,
                            color = White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (userCourse.isNotEmpty()) {

                        Text(
                            "$userCourse • $userYear",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ───────────── BUTTONS ─────────────
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        NeonButton(
                            text = if (isOpenToOpportunities)
                                "Open To Work"
                            else
                                "Unavailable",
                            color = NeonGreen
                        )

                        NeonButton(
                            text = "Share",
                            color = NeonBlue
                        )

                        NeonButton(
                            text = "Resume",
                            color = NeonPink
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // ───────────────── STATS ─────────────────
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Connections",
                        value = "128",
                        color = NeonPink,
                        icon = Icons.Default.People
                    )

                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Projects",
                        value = "14",
                        color = NeonBlue,
                        icon = Icons.Default.Code
                    )

                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Badges",
                        value = "7",
                        color = Gold,
                        icon = Icons.Default.EmojiEvents
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))
            }

            // ───────────────── PROFILE STATUS ─────────────────
            item {

                GlassCard(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    NeonPink.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                Icons.Default.Visibility,
                                contentDescription = null,
                                tint = NeonPink
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                "Your profile is trending",
                                color = White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                "Recruiters viewed your profile 12 times this week",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }

                        Icon(
                            Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))
            }

            // ───────────────── BIO ─────────────────
            item {

                SectionTitle("About")

                GlassCard(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {

                    Text(
                        userBio.ifEmpty {
                            "No bio added yet. Edit your profile to add one."
                        },
                        color = TextMuted,
                        lineHeight = 22.sp,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
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

                            SkillChip(
                                text = skill,
                                color = NeonPink
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
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

                            SkillChip(
                                text = interest,
                                color = NeonBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
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
                        icon = Icons.Default.Bookmark,
                        title = "Saved Opportunities",
                        subtitle = "View jobs you've bookmarked",
                        color = NeonPink
                    )

                    ActionCard(
                        icon = Icons.Default.Groups,
                        title = "Networking",
                        subtitle = "Connect with students & recruiters",
                        color = NeonBlue
                    )

                    ActionCard(
                        icon = Icons.Default.Work,
                        title = "Applications",
                        subtitle = "Track your applications",
                        color = NeonGreen
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ───────────────── BACKGROUND ─────────────────
@Composable
private fun NeonBackground() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            NeonPink.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    ),
                    radius = 600f,
                    center = Offset(size.width * 0.2f, size.height * 0.1f)
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            NeonBlue.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    radius = 700f,
                    center = Offset(size.width * 0.9f, size.height * 0.35f)
                )
            }
    )
}

// ───────────────── ICON BUTTON ─────────────────
@Composable
private fun IconCircle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(White.copy(alpha = 0.06f))
            .border(
                1.dp,
                White.copy(alpha = 0.08f),
                CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Icon(
            icon,
            contentDescription = null,
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
            .clip(RoundedCornerShape(22.dp))
            .background(CardBg)
            .border(
                1.dp,
                White.copy(alpha = 0.06f),
                RoundedCornerShape(22.dp)
            )
            .padding(16.dp),
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
                .background(MainGradient)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            title,
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
}

// ───────────────── CHIP ─────────────────
@Composable
private fun SkillChip(
    text: String,
    color: Color
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(color.copy(alpha = 0.14f))
            .border(
                1.dp,
                color.copy(alpha = 0.30f),
                RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Text(
            text,
            color = color,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

// ───────────────── STAT CARD ─────────────────
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .border(
                1.dp,
                color.copy(alpha = 0.18f),
                RoundedCornerShape(20.dp)
            )
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            value,
            color = White,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            title,
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
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .border(
                1.dp,
                White.copy(alpha = 0.05f),
                RoundedCornerShape(20.dp)
            )
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {

            Text(
                title,
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

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
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(14.dp)
        )
    }
}

// ───────────────── NEON BUTTON ─────────────────
@Composable
private fun NeonButton(
    text: String,
    color: Color
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(color.copy(alpha = 0.15f))
            .border(
                1.dp,
                color.copy(alpha = 0.35f),
                RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 16.dp, vertical = 9.dp)
    ) {

        Text(
            text,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

// ───────────────── PREVIEW ─────────────────
@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun ProfilePreview() {

    ProfileScreen(
        rememberNavController()
    )
}