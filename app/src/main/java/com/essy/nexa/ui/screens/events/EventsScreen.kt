package com.essy.nexa.ui.screens.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Event
import com.essy.nexa.ui.theme.*

val eventList = listOf(
    Event("1","Annual Hackathon 2025","Build solutions for Africa's challenges","Innovation Hub, Block C","Fri 16 May","9:00 AM","Tech",87,120,"TechClub",false),
    Event("2","Career Fair — Tech Edition","Meet top companies actively hiring","Main Auditorium","Mon 19 May","10:00 AM","Career",230,300,"Career Office",true),
    Event("3","AI & ML Workshop","Hands-on intro to ML with Python","Lab 3, ICT Block","Wed 21 May","2:00 PM","Tech",45,50,"CS Dept",false),
    Event("4","Mental Health Awareness","Panel discussion + free counselling","Student Centre","Thu 22 May","11:00 AM","Wellness",60,200,"Student Affairs",true),
    Event("5","Business Pitch Competition","Pitch your startup idea to real investors","Board Room","Sat 24 May","1:00 PM","Business",34,40,"Business Club",false),
    Event("6","Photography Walk","Campus photo tour for all skill levels","Main Gate","Sun 25 May","7:00 AM","Arts",22,30,"Photo Society",false),
    Event("7","Music Night 🎶","Live performances by campus bands","Amphitheatre","Fri 16 May","7:00 PM","Arts",90,150,"Music Society",false),
    Event("8","Startup Networking","Meet founders, investors and mentors","Innovation Hub","Sat 17 May","3:00 PM","Business",55,80,"Startup Club",false)
)

val eventFilters = listOf("All","Today","This Week","Tech","Career","Arts","Wellness","Business")

@Composable
fun EventsScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery    by remember { mutableStateOf("") }
    val teal = NexaPrimary

    // Filter logic — WORKING
    val filtered = eventList.filter { event ->
        val matchesFilter = when (selectedFilter) {
            "All"       -> true
            "Today"     -> event.date.contains("16 May")
            "This Week" -> true // all are this week in demo
            else        -> event.category == selectedFilter
        }
        val matchesSearch = searchQuery.isEmpty() ||
                event.title.contains(searchQuery, ignoreCase = true) ||
                event.location.contains(searchQuery, ignoreCase = true) ||
                event.category.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    Box(modifier = Modifier.fillMaxSize().background(teal)) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Events", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black)
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Tune, null, tint = Color.White)
                }
            }

            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // WORKING search bar
                    OutlinedTextField(
                        value = searchQuery, onValueChange = { searchQuery = it },
                        placeholder = { Text("Search events...") },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = NexaTextGrey) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, null, tint = NexaTextGrey)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp), singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // WORKING filter chips
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(eventFilters) { f ->
                            val sel = selectedFilter == f
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = if (sel) teal else NexaTagBg,
                                modifier = Modifier.clickable { selectedFilter = f }
                            ) {
                                Text(f, color = if (sel) Color.White else teal,
                                    fontWeight = if (sel) FontWeight.SemiBold else FontWeight.Normal,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Result count
                    Text("${filtered.size} events found", color = NexaTextGrey, fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    if (filtered.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🔍", fontSize = 40.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No events found", color = NexaTextGrey, fontSize = 15.sp)
                                Text("Try a different filter or search term", color = NexaTextGrey, fontSize = 13.sp)
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(filtered) { event ->
                                EventCard(event = event, primary = teal,
                                    onClick = { navController.navigate("event_detail") })
                            }
                            item { Spacer(modifier = Modifier.height(80.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(event: Event, primary: Color, onClick: () -> Unit) {
    val catColor = when (event.category) {
        "Tech"     -> primary
        "Career"   -> Color(0xFF3B82F6)
        "Arts"     -> Color(0xFFEC4899)
        "Wellness" -> NexaWarning
        "Business" -> Color(0xFF8B5CF6)
        else       -> primary
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = NexaTagBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(8.dp), color = catColor.copy(alpha = 0.15f)) {
                    Text(event.category, color = catColor, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                if (event.isRsvped) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = primary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("RSVP'd", color = primary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(event.title, color = NexaTextDark, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(event.description, color = NexaTextGrey, fontSize = 13.sp, maxLines = 2)
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, null, tint = NexaTextGrey, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(event.date, color = NexaTextGrey, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, null, tint = NexaTextGrey, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(event.time, color = NexaTextGrey, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.People, null, tint = NexaTextGrey, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${event.attendees}/${event.maxAttendees}", color = NexaTextGrey, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { event.attendees.toFloat() / event.maxAttendees },
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                color = catColor, trackColor = NexaDivider
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventsScreenPreview() { EventsScreen(rememberNavController()) }
