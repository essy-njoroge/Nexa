package com.essy.nexa.ui.screens.study

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
import com.essy.nexa.model.StudyGroup
import com.essy.nexa.ui.theme.*

val studyGroups = listOf(
    StudyGroup("1","Computer Science","Algorithms & Data Structures", listOf("Kevin","Aisha","Brian","Grace"),5,"Tues & Thurs 6PM"),
    StudyGroup("2","Computer Science","Mobile Development (Android)", listOf("James","Fatima","Kevin"),4,"Fridays 4PM"),
    StudyGroup("3","Computer Science","Database Systems", listOf("Aisha","Tom","Rita"),5,"Mondays 5PM"),
    StudyGroup("4","Business","Financial Accounting", listOf("Grace","Brian","Sarah","Mark"),5,"Wednesdays 3PM")
)

val modules = listOf("Algorithms","Mobile Dev","Databases","Networks","AI & ML","Software Eng")
val days = listOf("Monday","Tuesday","Wednesday","Thursday","Friday")

@Composable
fun StudyGroupScreen(navController: NavController) {
    val primary = NexaPrimary

    var selectedModule by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("") }
    var showMatches by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(primary)) {

        Column(modifier = Modifier.fillMaxSize()) {

            // TOP BAR
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text("Study Matcher", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("AI-powered group matching", color = Color.White.copy(0.8f), fontSize = 12.sp)
                }
            }

            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {

                    // AI badge
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = NexaTagBg),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(Modifier.padding(14.dp)) {
                                Icon(Icons.Default.AutoAwesome, null, tint = primary)
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text("AI Matching", fontWeight = FontWeight.Bold)
                                    Text("Course + schedule based grouping", fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    // MODULES (NO FLOWROW)
                    item {
                        Text("What are you studying?", fontWeight = FontWeight.Bold)

                        Spacer(Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            modules.chunked(3).forEach { rowItems ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowItems.forEach { mod ->
                                        val selected = selectedModule == mod

                                        Surface(
                                            shape = RoundedCornerShape(50.dp),
                                            color = if (selected) primary else NexaTagBg,
                                            modifier = Modifier
                                                .clickable { selectedModule = mod }
                                        ) {
                                            Text(
                                                mod,
                                                color = if (selected) Color.White else primary,
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    // DAYS (NO FLOWROW)
                    item {
                        Text("When are you available?", fontWeight = FontWeight.Bold)

                        Spacer(Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            days.chunked(3).forEach { rowItems ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    rowItems.forEach { day ->
                                        val selected = selectedDay == day

                                        Surface(
                                            shape = RoundedCornerShape(50.dp),
                                            color = if (selected) primary else NexaTagBg,
                                            modifier = Modifier
                                                .clickable { selectedDay = day }
                                        ) {
                                            Text(
                                                day.take(3),
                                                color = if (selected) Color.White else primary,
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = { showMatches = true },
                            colors = ButtonDefaults.buttonColors(containerColor = primary),
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Icon(Icons.Default.Search, null, tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("Find Study Group", color = Color.White)
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    if (showMatches) {
                        item {
                            Text("Matched Groups", fontWeight = FontWeight.Bold)
                            Text("${studyGroups.size} groups found", fontSize = 12.sp, color = Color.Gray)
                            Spacer(Modifier.height(10.dp))
                        }

                        items(studyGroups) { group ->
                            StudyGroupCard(group, primary)
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudyGroupCard(group: StudyGroup, primary: Color) {
    var joined by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NexaTagBg),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(42.dp)
                        .background(primary.copy(0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📚")
                }

                Spacer(Modifier.width(10.dp))

                Column(Modifier.weight(1f)) {
                    Text(group.topic, fontWeight = FontWeight.Bold)
                    Text(group.course, fontSize = 12.sp, color = Color.Gray)
                }

                Text("${group.members.size}/${group.maxMembers}", color = primary)
            }

            Spacer(Modifier.height(8.dp))

            Text(group.schedule, fontSize = 12.sp, color = Color.Gray)

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = { joined = !joined },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (joined) Color.White else primary,
                    contentColor = if (joined) primary else Color.White
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (joined) "✓ Joined" else "Join Group")
            }
        }
    }
}

@Preview
@Composable
fun StudyPreview() {
    StudyGroupScreen(rememberNavController())
}