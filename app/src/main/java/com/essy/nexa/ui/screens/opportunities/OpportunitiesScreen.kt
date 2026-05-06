package com.essy.nexa.ui.screens.opportunities

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Opportunity
import com.essy.nexa.ui.theme.*

val opportunityList = listOf(
    Opportunity("1","Android Developer Intern","TechCorp Kenya","Internship","Nairobi (Hybrid)","31 May 2025", listOf("Kotlin","Jetpack Compose","Firebase"),"Build real Android features used by thousands."),
    Opportunity("2","UI/UX Design Intern","CreativeHub","Internship","Remote","15 Jun 2025", listOf("Figma","UI Design","Prototyping"),"Help shape products used across East Africa."),
    Opportunity("3","Junior Data Analyst","FinBank Ltd","Graduate Role","Nairobi","30 May 2025", listOf("Python","SQL","Data Viz"),"Analyse financial data to drive business decisions."),
    Opportunity("4","Campus Brand Ambassador","Safaricom","Part-time","On Campus","Open", listOf("Marketing","Communication","Social Media"),"Represent Safaricom on campus and earn commissions."),
    Opportunity("5","Software Engineering Intern","StartupHub Africa","Internship","Nairobi","20 Jun 2025", listOf("Any Language","APIs","Problem Solving"),"Work on cutting-edge products in a fast-paced startup.")
)

val opFilters = listOf("All","Internship","Graduate Role","Part-time","Remote")

@Composable
fun OpportunitiesScreen(navController: NavController) {
    val primary = NexaPrimary
    var selectedFilter by remember { mutableStateOf("All") }

    val filtered = opportunityList.filter {
        selectedFilter == "All" || it.type == selectedFilter ||
        (selectedFilter == "Remote" && it.location.contains("Remote"))
    }

    Box(modifier = Modifier.fillMaxSize().background(primary)) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Opportunities", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Recruiter banner
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(primary)
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Visibility, null, tint = Color.White, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Your profile is live!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("2 recruiters viewed your profile this week", color = NexaTextWhite80, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(opFilters) { f ->
                            val sel = selectedFilter == f
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = if (sel) primary else NexaTagBg,
                                modifier = Modifier.clickable { selectedFilter = f }
                            ) {
                                Text(f, color = if (sel) Color.White else primary,
                                    fontWeight = FontWeight.Medium, fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filtered) { opp ->
                            OpportunityCard(opp = opp, primary = primary)
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun OpportunityCard(opp: Opportunity, primary: Color) {
    var saved by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NexaTagBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(46.dp).clip(CircleShape).background(primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(opp.company.take(1), color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(opp.title, color = NexaTextDark, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(opp.company, color = NexaTextGrey, fontSize = 13.sp)
                }
                IconButton(onClick = { saved = !saved }, modifier = Modifier.size(36.dp)) {
                    Icon(
                        if (saved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        null, tint = if (saved) primary else NexaTextGrey,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(opp.description, color = NexaTextGrey, fontSize = 13.sp, lineHeight = 19.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = NexaTextGrey, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(opp.location, color = NexaTextGrey, fontSize = 12.sp)
                }
                Surface(shape = RoundedCornerShape(6.dp), color = primary.copy(alpha = 0.12f)) {
                    Text(opp.type, color = primary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(opp.skills) { skill ->
                    Surface(shape = RoundedCornerShape(6.dp), color = Color.White) {
                        Text(skill, color = NexaTextGrey, fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, null, tint = NexaWarning, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Deadline: ${opp.deadline}", color = NexaWarning, fontSize = 12.sp, fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = primary),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.height(34.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text("Apply", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OpportunitiesPreview() {
    OpportunitiesScreen(rememberNavController())
}
