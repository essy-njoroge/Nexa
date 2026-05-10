package com.essy.nexa.ui.screens.opportunities

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Opportunity

// ───────────────── Colors ─────────────────
private val DeepMidnight = Color(0xFF06050F)
private val DarkSurface = Color(0xFF0D0918)
private val CardBg = Color(0xFF100E1A)

private val HotPink = Color(0xFFFF2D9B)
private val BlazeOrange = Color(0xFFFF6400)
private val GoldYellow = Color(0xFFFFB300)
private val VioletDeep = Color(0xFF7B2FFF)
private val VioletLight = Color(0xFFA855F7)
private val CobaltBlue = Color(0xFF00A3FF)

private val White = Color.White
private val TextMuted = White.copy(alpha = 0.55f)

private val FireGradient = Brush.linearGradient(
    colors = listOf(HotPink, BlazeOrange, GoldYellow)
)

// ───────────────── Type Colors ─────────────────
private fun typeColor(type: String): Color {
    return when (type) {
        "Internship" -> HotPink
        "Graduate Role" -> GoldYellow
        "Part-time" -> BlazeOrange
        "Remote" -> CobaltBlue
        else -> VioletLight
    }
}

// ───────────────── Dummy Data ─────────────────
val opportunityList = listOf(
    Opportunity(
        "1",
        "Android Developer Intern",
        "TechCorp Kenya",
        "Internship",
        "Nairobi (Hybrid)",
        "31 May 2026",
        listOf("Kotlin", "Compose", "Firebase"),
        "Build real Android features used by thousands."
    ),
    Opportunity(
        "2",
        "UI/UX Design Intern",
        "CreativeHub",
        "Internship",
        "Remote",
        "15 Jun 2026",
        listOf("Figma", "UI Design", "Prototyping"),
        "Help shape products used across East Africa."
    ),
    Opportunity(
        "3",
        "Junior Data Analyst",
        "FinBank Ltd",
        "Graduate Role",
        "Nairobi",
        "30 May 2026",
        listOf("Python", "SQL", "Data Viz"),
        "Analyse financial data to drive business decisions."
    ),
    Opportunity(
        "4",
        "Campus Brand Ambassador",
        "Safaricom",
        "Part-time",
        "On Campus",
        "Open",
        listOf("Marketing", "Communication", "Social Media"),
        "Represent Safaricom on campus and earn commissions."
    ),
    Opportunity(
        "5",
        "Software Engineering Intern",
        "StartupHub Africa",
        "Internship",
        "Nairobi",
        "20 Jun 2026",
        listOf("APIs", "Problem Solving", "Backend"),
        "Work on cutting-edge products in a fast-paced startup."
    )
)

private val opFilters = listOf(
    "All",
    "Internship",
    "Graduate Role",
    "Part-time",
    "Remote"
)

// ───────────────── Background Orb ─────────────────
private fun DrawScope.drawRadialOrb(
    center: Offset,
    radius: Float,
    color: Color,
    strength: Float = 1f
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color.copy(alpha = 0.35f * strength),
                color.copy(alpha = 0.14f * strength),
                color.copy(alpha = 0.04f * strength),
                Color.Transparent
            ),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
}

// ───────────────── Animated Background ─────────────────
@Composable
private fun OppBackground() {

    val infinite = rememberInfiniteTransition(label = "bg")

    val orbAnim by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb"
    )

    val gridAnim by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 36f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "grid"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {

                val w = size.width
                val h = size.height

                val grid = 36.dp.toPx()
                val lineWidth = 0.5.dp.toPx()

                val gridColor = HotPink.copy(alpha = 0.03f)

                var gx = gridAnim % grid
                while (gx < w) {
                    drawLine(
                        color = gridColor,
                        start = Offset(gx, 0f),
                        end = Offset(gx, h),
                        strokeWidth = lineWidth
                    )
                    gx += grid
                }

                var gy = gridAnim % grid
                while (gy < h) {
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, gy),
                        end = Offset(w, gy),
                        strokeWidth = lineWidth
                    )
                    gy += grid
                }

                drawRadialOrb(
                    center = Offset(
                        w * 0.1f + orbAnim * 20.dp.toPx(),
                        h * 0.06f
                    ),
                    radius = 170.dp.toPx(),
                    color = HotPink
                )

                drawRadialOrb(
                    center = Offset(
                        w * 0.88f,
                        h * 0.42f + orbAnim * 12.dp.toPx()
                    ),
                    radius = 140.dp.toPx(),
                    color = VioletDeep,
                    strength = 0.7f
                )
            }
    )
}

// ───────────────── Corner Decorations ─────────────────
@Composable
private fun CornerBrackets() {

    val stroke = 1.5.dp

    Box(modifier = Modifier.fillMaxSize()) {

        Corner(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(14.dp),
            top = true,
            left = true,
            color = HotPink,
            stroke = stroke
        )

        Corner(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(14.dp),
            top = true,
            left = false,
            color = HotPink,
            stroke = stroke
        )

        Corner(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp),
            top = false,
            left = true,
            color = GoldYellow,
            stroke = stroke
        )

        Corner(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(14.dp),
            top = false,
            left = false,
            color = GoldYellow,
            stroke = stroke
        )
    }
}

@Composable
private fun Corner(
    modifier: Modifier,
    top: Boolean,
    left: Boolean,
    color: Color,
    stroke: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = modifier
            .size(18.dp)
            .drawBehind {

                val s = size.width
                val sw = stroke.toPx()

                if (top && left) {
                    drawLine(color, Offset(s, 0f), Offset(0f, 0f), sw, StrokeCap.Square)
                    drawLine(color, Offset(0f, 0f), Offset(0f, s), sw, StrokeCap.Square)
                }

                if (top && !left) {
                    drawLine(color, Offset(0f, 0f), Offset(s, 0f), sw, StrokeCap.Square)
                    drawLine(color, Offset(s, 0f), Offset(s, s), sw, StrokeCap.Square)
                }

                if (!top && left) {
                    drawLine(color, Offset(0f, s), Offset(s, s), sw, StrokeCap.Square)
                    drawLine(color, Offset(0f, 0f), Offset(0f, s), sw, StrokeCap.Square)
                }

                if (!top && !left) {
                    drawLine(color, Offset(0f, s), Offset(s, s), sw, StrokeCap.Square)
                    drawLine(color, Offset(s, 0f), Offset(s, s), sw, StrokeCap.Square)
                }
            }
    )
}

// ───────────────── Opportunity Card ─────────────────
@Composable
fun OpportunityCard(opp: Opportunity) {

    var saved by remember { mutableStateOf(false) }

    val color = typeColor(opp.type)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .border(
                1.dp,
                color.copy(alpha = 0.15f),
                RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                color,
                                color.copy(alpha = 0.6f)
                            )
                        )
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
                    Text(
                        text = opp.company.take(1),
                        color = color,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = opp.title,
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = opp.company,
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (saved)
                            color.copy(alpha = 0.12f)
                        else
                            White.copy(alpha = 0.05f)
                    )
                    .border(
                        1.dp,
                        if (saved)
                            color.copy(alpha = 0.30f)
                        else
                            White.copy(alpha = 0.08f),
                        CircleShape
                    )
                    .clickable { saved = !saved },
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = if (saved)
                        Icons.Default.Bookmark
                    else
                        Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = if (saved) color else TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = opp.description,
            color = TextMuted,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = color.copy(alpha = 0.75f),
                    modifier = Modifier.size(12.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = opp.location,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f))
                    .border(
                        1.dp,
                        color.copy(alpha = 0.25f),
                        CircleShape
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {

                Text(
                    text = opp.type,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {

            items(opp.skills) { skill ->

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(White.copy(alpha = 0.05f))
                        .border(
                            1.dp,
                            White.copy(alpha = 0.10f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = skill,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        HorizontalDivider(color = White.copy(alpha = 0.06f))

        Spacer(modifier = Modifier.height(14.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                Icons.Default.AccessTime,
                contentDescription = null,
                tint = GoldYellow.copy(alpha = 0.85f),
                modifier = Modifier.size(13.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "Deadline: ${opp.deadline}",
                color = GoldYellow.copy(alpha = 0.85f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.height(36.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    color,
                                    color.copy(alpha = 0.75f)
                                )
                            ),
                            RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 18.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Apply →",
                        color = White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ───────────────── Main Screen ─────────────────
@Composable
fun OpportunitiesScreen(navController: NavController) {

    var selectedFilter by remember {
        mutableStateOf("All")
    }

    val filtered = opportunityList.filter {

        selectedFilter == "All" ||
                it.type == selectedFilter ||
                (
                        selectedFilter == "Remote" &&
                                it.location.contains("Remote", true)
                        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {

        OppBackground()
        CornerBrackets()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            item {

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(White.copy(alpha = 0.06f))
                            .border(
                                1.dp,
                                White.copy(alpha = 0.10f),
                                CircleShape
                            )
                            .clickable {
                                navController.popBackStack()
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {

                        Text(
                            text = "Opportunities",
                            color = White,
                            fontWeight = FontWeight.Black,
                            fontSize = 24.sp
                        )

                        Text(
                            text = "${filtered.size} roles available",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))
            }

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    HotPink.copy(alpha = 0.14f),
                                    VioletDeep.copy(alpha = 0.14f)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            HotPink.copy(alpha = 0.22f),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(16.dp)
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(HotPink.copy(alpha = 0.15f))
                                .border(
                                    1.dp,
                                    HotPink.copy(alpha = 0.30f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                Icons.Default.Visibility,
                                contentDescription = null,
                                tint = HotPink,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                text = "Your profile is live!",
                                color = White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )

                            Text(
                                text = "2 recruiters viewed your profile this week",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }

                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = HotPink.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(opFilters) { filter ->

                        val selected = selectedFilter == filter
                        val filterColor =
                            if (filter == "All") HotPink
                            else typeColor(filter)

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (selected)
                                        Brush.linearGradient(
                                            listOf(
                                                filterColor,
                                                filterColor.copy(alpha = 0.75f)
                                            )
                                        )
                                    else
                                        Brush.linearGradient(
                                            listOf(
                                                White.copy(alpha = 0.06f),
                                                White.copy(alpha = 0.06f)
                                            )
                                        )
                                )
                                .border(
                                    1.dp,
                                    if (selected)
                                        Color.Transparent
                                    else
                                        White.copy(alpha = 0.10f),
                                    CircleShape
                                )
                                .clickable {
                                    selectedFilter = filter
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {

                            Text(
                                text = filter,
                                color = if (selected) White else TextMuted,
                                fontSize = 12.sp,
                                fontWeight = if (selected)
                                    FontWeight.Bold
                                else
                                    FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            item {

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

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Open Roles",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            items(filtered, key = { it.id }) { opp ->

                Box(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    OpportunityCard(opp)
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun OpportunitiesPreview() {
    OpportunitiesScreen(
        navController = rememberNavController()
    )
}