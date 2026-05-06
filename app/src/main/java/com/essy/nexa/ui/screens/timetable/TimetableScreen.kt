package com.essy.nexa.ui.screens.timetable

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.ui.theme.*

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

@Composable
fun TimetableScreen(navController: NavController) {

    val teal = NexaPrimary

    var selectedDay by remember { mutableStateOf("Mon") }

    // ✅ FIX: mutable state so UI updates when adding items
    val timetableData = remember {
        mutableStateMapOf(
            "Mon" to mutableStateListOf(
                ClassItem("Data Structures","CS301","8:00 AM","10:00 AM","Room B204","Dr. Kamau","Lecture", Color(0xFF6366F1))
            ),
            "Tue" to mutableStateListOf(
                ClassItem("Mobile Development","CS405","9:00 AM","11:00 AM","Room A101","Dr. Wanjiru","Lecture", Color(0xFF0D9488))
            ),
            "Wed" to mutableStateListOf(
                ClassItem("Software Engineering","CS401","8:00 AM","10:00 AM","Room B201","Dr. Njoroge","Lecture", Color(0xFFF59E0B))
            ),
            "Thu" to mutableStateListOf(
                ClassItem("Networks","CS304","2:00 PM","4:00 PM","Room A203","Dr. Achieng","Lecture", Color(0xFF14B8A6))
            ),
            "Fri" to mutableStateListOf(
                ClassItem("Research Methods","CS402","9:00 AM","11:00 AM","Room D105","Prof. Kimani","Lecture", Color(0xFF8B5CF6))
            )
        )
    }

    val todayClasses = timetableData.getOrPut(selectedDay) { mutableStateListOf() }
    // ✅ Add dialog state
    var showDialog by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(teal)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "My Timetable",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Semester 1 — 2025",
                        color = NexaTextWhite80,
                        fontSize = 12.sp
                    )
                }

                // ✅ FIXED + BUTTON
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.White)
                }
            }

            // DAY SELECTOR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                days.forEach { day ->
                    val selected = selectedDay == day
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (selected) Color.White else Color.White.copy(0.15f),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedDay = day }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            day,
                            color = if (selected) teal else Color.White,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // BODY
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                Column(modifier = Modifier.fillMaxSize()) {

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "${todayClasses.size} classes on $selectedDay",
                        color = NexaTextGrey,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    if (todayClasses.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No classes 🎉")
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(todayClasses) { cls ->
                                ClassCard(cls)
                            }
                        }
                    }
                }
            }
        }

        // ✅ ADD NOTE DIALOG (REAL FUNCTIONALITY)
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add Reminder / Note") },
                text = {
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        placeholder = { Text("e.g. Study CS301 / Submit assignment") }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (noteText.isNotBlank()) {
                                timetableData[selectedDay]?.add(
                                    ClassItem(
                                        subject = noteText,
                                        code = "NOTE",
                                        startTime = "Anytime",
                                        endTime = "",
                                        room = "Personal",
                                        lecturer = "",
                                        type = "Note",
                                        color = Color(0xFF6366F1)
                                    )
                                )
                            }
                            noteText = ""
                            showDialog = false
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ClassCard(cls: ClassItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NexaTagBg)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(60.dp)
                    .background(cls.color)
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(cls.subject, fontWeight = FontWeight.Bold)
                Text(cls.code, color = NexaTextGrey, fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetablePreview() {
    TimetableScreen(rememberNavController())
}