package com.essy.nexa.ui.screens.lostfound

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
import com.essy.nexa.ui.theme.*

data class LostFoundItem(
    val id: String,
    val type: String,        // "Lost" or "Found"
    val title: String,
    val description: String,
    val location: String,
    val postedBy: String,
    val timeAgo: String,
    val isResolved: Boolean = false
)

val lfItems = listOf(
    LostFoundItem("1","Lost","Black Laptop Bag","Dell bag with charger inside, left in Library Block B","Library Block B","Kevin M.","1h ago"),
    LostFoundItem("2","Found","Student ID Card","Found near the cafeteria. Name: J. Otieno","Cafeteria","Aisha K.","2h ago"),
    LostFoundItem("3","Lost","AirPods Pro (White case)","Lost somewhere between ICT block and main gate","ICT Block","Grace W.","3h ago"),
    LostFoundItem("4","Found","Blue Water Bottle","Hydro Flask bottle found in Room C204","Room C204","Brian O.","5h ago"),
    LostFoundItem("5","Lost","Calculator (Casio fx-991)","Scientific calculator needed for exam tomorrow!","Engineering Block","James N.","6h ago"),
    LostFoundItem("6","Found","Keys (3 keys on a red keyring)","Found near the parking lot","Parking Lot","Fatima A.","Yesterday"),
    LostFoundItem("7","Lost","Purple Umbrella","Left in the cafeteria during yesterday's rain","Cafeteria","Rita M.","Yesterday",true),
    LostFoundItem("8","Found","Earphones (wired, black)","Found on a bench near the student centre","Student Centre","Tom K.","2 days ago",true)
)

@Composable
fun LostFoundScreen(navController: NavController) {
    val teal = NexaPrimary
    var selectedTab by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showPostDialog by remember { mutableStateOf(false) }
    var postType by remember { mutableStateOf("Lost") }
    var postTitle by remember { mutableStateOf("") }
    var postDesc by remember { mutableStateOf("") }
    var postLocation by remember { mutableStateOf("") }

    val filtered = lfItems.filter {
        (selectedTab == "All" || it.type == selectedTab) &&
        (searchQuery.isEmpty() || it.title.contains(searchQuery, ignoreCase = true))
    }

    if (showPostDialog) {
        AlertDialog(
            onDismissRequest = { showPostDialog = false },
            title = { Text("Post Item", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Lost","Found").forEach { type ->
                            Surface(shape = RoundedCornerShape(50.dp),
                                color = if (postType == type) teal else NexaTagBg,
                                modifier = Modifier.clickable { postType = type }) {
                                Text(type, color = if (postType == type) Color.White else teal,
                                    fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                            }
                        }
                    }
                    OutlinedTextField(value = postTitle, onValueChange = { postTitle = it },
                        placeholder = { Text("Item name") }, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp), singleLine = true)
                    OutlinedTextField(value = postDesc, onValueChange = { postDesc = it },
                        placeholder = { Text("Description") }, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = postLocation, onValueChange = { postLocation = it },
                        placeholder = { Text("Location") }, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp), singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = { showPostDialog = false; postTitle = ""; postDesc = ""; postLocation = "" },
                    colors = ButtonDefaults.buttonColors(containerColor = teal)) {
                    Text("Post", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPostDialog = false }) { Text("Cancel") }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(teal)) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Lost & Found", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Help your fellow students", color = NexaTextWhite80, fontSize = 12.sp)
                }
            }

            Card(shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(value = searchQuery, onValueChange = { searchQuery = it },
                        placeholder = { Text("Search items...") },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = NexaTextGrey) },
                        trailingIcon = { if (searchQuery.isNotEmpty()) IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Close, null, tint = NexaTextGrey) } },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp), singleLine = true)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("All","Lost","Found","Resolved").forEach { tab ->
                            val sel = selectedTab == tab
                            Surface(shape = RoundedCornerShape(50.dp),
                                color = if (sel) teal else NexaTagBg,
                                modifier = Modifier.clickable { selectedTab = tab }) {
                                Text(tab, color = if (sel) Color.White else teal,
                                    fontWeight = FontWeight.Medium, fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)) {
                        items(filtered) { item ->
                            LostFoundCard(item = item, teal = teal)
                        }
                        item { Spacer(modifier = Modifier.height(90.dp)) }
                    }
                }
            }
        }

        // FAB
        Box(modifier = Modifier.fillMaxSize().padding(end = 16.dp, bottom = 24.dp), contentAlignment = Alignment.BottomEnd) {
            FloatingActionButton(onClick = { showPostDialog = true },
                containerColor = teal, shape = RoundedCornerShape(16.dp)) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }
        }
    }
}

@Composable
fun LostFoundCard(item: LostFoundItem, teal: Color) {
    val isLost = item.type == "Lost"
    val typeColor = if (isLost) Color(0xFFEF4444) else Color(0xFF22C55E)

    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NexaTagBg),
        elevation = CardDefaults.cardElevation(0.dp)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(8.dp), color = typeColor.copy(alpha = 0.15f)) {
                    Text(item.type, color = typeColor, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
                if (item.isResolved) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = teal.copy(alpha = 0.15f)) {
                        Text("Resolved ✓", color = teal, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(item.timeAgo, color = NexaTextGrey, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.title, color = NexaTextDark, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.description, color = NexaTextGrey, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = NexaTextGrey, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(item.location, color = NexaTextGrey, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, null, tint = NexaTextGrey, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(item.postedBy, color = NexaTextGrey, fontSize = 12.sp)
                }
            }
            if (!item.isResolved) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = {}, modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(50.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, teal),
                        contentPadding = PaddingValues(0.dp)) {
                        Text("Contact", color = teal, fontSize = 13.sp)
                    }
                    Button(onClick = {}, modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = teal),
                        contentPadding = PaddingValues(0.dp)) {
                        Text("Mark Resolved", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LostFoundPreview() { LostFoundScreen(rememberNavController()) }
