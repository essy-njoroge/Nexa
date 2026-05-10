package com.essy.nexa.ui.screens.timetable

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// ───────────────── COLORS ─────────────────
private val DeepMidnight = Color(0xFF06050F)
private val DarkSurface  = Color(0xFF0D0918)
private val CardBg       = Color(0xFF100E1A)

private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val VioletDeep   = Color(0xFF7B2FFF)
private val VioletLight  = Color(0xFFA855F7)

private val White        = Color.White
private val TextMuted    = White.copy(alpha = 0.55f)
private val CardBorder   = White.copy(alpha = 0.08f)

private val FireGradient =
    Brush.linearGradient(
        listOf(
            HotPink,
            BlazeOrange,
            GoldYellow
        )
    )

// ───────────────── DATA ─────────────────
data class ClassItem(
    val subject: String,
    val code: String,
    val startTime: String,
    val endTime: String,
    val room: String,
    val lecturer: String,
    val type: String,
    val color: Color
)

val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")

// ───────────────── BACKGROUND ─────────────────
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRadialOrb(
    center: Offset,
    radius: Float,
    color: Color,
    strength: Float = 1f
) {
    drawCircle(
        brush = Brush.radialGradient(
            listOf(
                color.copy(alpha = 0.32f * strength),
                color.copy(alpha = 0.12f * strength),
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
private fun TimetableBackground() {

    val infinite = rememberInfiniteTransition(label = "bg")

    val move by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(9000, easing = EaseInOutSine),
            RepeatMode.Reverse
        ),
        label = "move"
    )

    val grid by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 36f,
        animationSpec = infiniteRepeatable(
            tween(3000, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "grid"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {

                val w = size.width
                val h = size.height

                val gridSize = 36.dp.toPx()
                val gridColor = VioletLight.copy(alpha = 0.03f)

                var gx = grid % gridSize
                while (gx < w) {
                    drawLine(
                        color = gridColor,
                        start = Offset(gx, 0f),
                        end = Offset(gx, h),
                        strokeWidth = 0.5.dp.toPx()
                    )
                    gx += gridSize
                }

                var gy = grid % gridSize
                while (gy < h) {
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, gy),
                        end = Offset(w, gy),
                        strokeWidth = 0.5.dp.toPx()
                    )
                    gy += gridSize
                }

                drawRadialOrb(
                    center = Offset(
                        w * 0.12f + move * 20.dp.toPx(),
                        h * 0.10f
                    ),
                    radius = 180.dp.toPx(),
                    color = HotPink
                )

                drawRadialOrb(
                    center = Offset(
                        w * 0.88f,
                        h * 0.40f + move * 12.dp.toPx()
                    ),
                    radius = 150.dp.toPx(),
                    color = BlazeOrange
                )

                drawRadialOrb(
                    center = Offset(
                        w * 0.55f,
                        h * 0.92f
                    ),
                    radius = 200.dp.toPx(),
                    color = GoldYellow,
                    strength = 0.7f
                )
            }
    )
}

// ───────────────── SCREEN ─────────────────
@Composable
fun TimetableScreen(navController: NavController) {

    var selectedDay by remember { mutableStateOf("Mon") }

    val timetableData = remember {
        mutableStateMapOf(
            "Mon" to mutableStateListOf(
                ClassItem(
                    "Data Structures",
                    "CS301",
                    "8:00 AM",
                    "10:00 AM",
                    "B204",
                    "Dr. Kamau",
                    "Lecture",
                    VioletLight
                )
            ),
            "Tue" to mutableStateListOf(
                ClassItem(
                    "Mobile Development",
                    "CS405",
                    "9:00 AM",
                    "11:00 AM",
                    "A101",
                    "Dr. Wanjiru",
                    "Lecture",
                    BlazeOrange
                )
            ),
            "Wed" to mutableStateListOf(
                ClassItem(
                    "Software Engineering",
                    "CS401",
                    "8:00 AM",
                    "10:00 AM",
                    "B201",
                    "Dr. Njoroge",
                    "Lecture",
                    GoldYellow
                )
            ),
            "Thu" to mutableStateListOf(
                ClassItem(
                    "Networks",
                    "CS304",
                    "2:00 PM",
                    "4:00 PM",
                    "A203",
                    "Dr. Achieng",
                    "Lecture",
                    HotPink
                )
            ),
            "Fri" to mutableStateListOf(
                ClassItem(
                    "Research Methods",
                    "CS402",
                    "9:00 AM",
                    "11:00 AM",
                    "D105",
                    "Prof. Kimani",
                    "Lecture",
                    BlazeOrange
                )
            )
        )
    }

    val todayClasses =
        timetableData.getOrPut(selectedDay) { mutableStateListOf() }

    var showDialog by remember { mutableStateOf(false) }

    var noteText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {

        TimetableBackground()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {

            // TOP BAR
            item {

                Spacer(Modifier.height(18.dp))

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
                            null,
                            tint = White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {

                        Text(
                            "My Timetable",
                            color = White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )

                        Text(
                            "Semester 1 • 2025",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        HotPink,
                                        BlazeOrange
                                    )
                                )
                            )
                            .clickable {
                                showDialog = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            null,
                            tint = White
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            // BANNER
            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    HotPink.copy(alpha = 0.14f),
                                    BlazeOrange.copy(alpha = 0.12f),
                                    GoldYellow.copy(alpha = 0.10f)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            HotPink.copy(alpha = 0.25f),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(16.dp)
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    White.copy(alpha = 0.08f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.CalendarMonth,
                                null,
                                tint = GoldYellow
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column {

                            Text(
                                "Stay Organised",
                                color = White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Text(
                                "Track your classes & reminders easily",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(22.dp))
            }

            // DAY SELECTOR
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    days.forEach { day ->

                        val selected = selectedDay == day

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (selected)
                                        Brush.linearGradient(
                                            listOf(
                                                HotPink,
                                                BlazeOrange
                                            )
                                        )
                                    else
                                        Brush.linearGradient(
                                            listOf(
                                                White.copy(alpha = 0.05f),
                                                White.copy(alpha = 0.05f)
                                            )
                                        )
                                )
                                .border(
                                    1.dp,
                                    if (selected)
                                        Color.Transparent
                                    else
                                        White.copy(alpha = 0.08f),
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable {
                                    selectedDay = day
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                day,
                                color = if (selected) White else TextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            // SECTION TITLE
            item {

                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(14.dp)
                            .clip(CircleShape)
                            .background(FireGradient)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "${todayClasses.size} Classes Today",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(Modifier.height(14.dp))
            }

            // CARDS
            if (todayClasses.isEmpty()) {

                item {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(CardBg)
                            .border(
                                1.dp,
                                CardBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            "No classes today 🎉",
                            color = White
                        )
                    }
                }

            } else {

                items(todayClasses) { cls ->

                    TimetableCard(cls)

                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        // DIALOG
        if (showDialog) {

            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                containerColor = DarkSurface,
                title = {
                    Text(
                        "Add Reminder",
                        color = White
                    )
                },
                text = {

                    OutlinedTextField(
                        value = noteText,
                        onValueChange = {
                            noteText = it
                        },
                        placeholder = {
                            Text("Study for CS301")
                        }
                    )
                },
                confirmButton = {

                    TextButton(
                        onClick = {

                            if (noteText.isNotBlank()) {

                                timetableData[selectedDay]?.add(
                                    ClassItem(
                                        noteText,
                                        "NOTE",
                                        "Anytime",
                                        "",
                                        "Personal",
                                        "",
                                        "Reminder",
                                        HotPink
                                    )
                                )
                            }

                            noteText = ""
                            showDialog = false
                        }
                    ) {
                        Text("Add", color = GoldYellow)
                    }
                },
                dismissButton = {

                    TextButton(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("Cancel", color = TextMuted)
                    }
                }
            )
        }
    }
}

// ───────────────── CARD ─────────────────
@Composable
fun TimetableCard(cls: ClassItem) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBg
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(58.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    cls.color,
                                    BlazeOrange
                                )
                            )
                        )
                )

                Spacer(Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        cls.subject,
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        cls.code,
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            cls.color.copy(alpha = 0.12f)
                        )
                        .border(
                            1.dp,
                            cls.color.copy(alpha = 0.30f),
                            CircleShape
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {

                    Text(
                        cls.type,
                        color = cls.color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            HorizontalDivider(
                color = White.copy(alpha = 0.06f)
            )

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.Schedule,
                    null,
                    tint = GoldYellow,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    "${cls.startTime} - ${cls.endTime}",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.LocationOn,
                    null,
                    tint = BlazeOrange,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    "${cls.room} • ${cls.lecturer}",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun TimetablePreview() {
    TimetableScreen(rememberNavController())
}