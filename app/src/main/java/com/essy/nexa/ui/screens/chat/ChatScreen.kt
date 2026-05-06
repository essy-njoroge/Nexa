package com.essy.nexa.ui.screens.chat

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.ChatPreview
import com.essy.nexa.ui.theme.*

val chatList = listOf(
    ChatPreview("1","TechClub 💻","Session starts at 5PM today — Room B204","2m ago",3,true),
    ChatPreview("2","Aisha Kamau","Thanks for the notes! You're a lifesaver 🙏","15m ago",1,false),
    ChatPreview("3","CS Year 3 Group","Anyone finished the algorithms assignment?","1h ago",12,true),
    ChatPreview("4","Brian Otieno","Are you coming to the hackathon?","2h ago",0,false),
    ChatPreview("5","Career Office","Your profile has been viewed by 2 recruiters","3h ago",1,false),
    ChatPreview("6","Study Squad 📚","Library session confirmed for tomorrow 6PM","5h ago",0,true),
    ChatPreview("7","Grace Wanjiru","See you at the photography walk Sunday!","Yesterday",0,false),
    ChatPreview("8","Nexa AI ✨","You have 3 new event recommendations","Yesterday",0,false)
)

@Composable
fun ChatScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    val primary = NexaPrimary

    Box(modifier = Modifier.fillMaxSize().background(primary)) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Messages", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black)
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Edit, contentDescription = "New", tint = Color.White)
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

                    OutlinedTextField(
                        value = searchQuery, onValueChange = { searchQuery = it },
                        placeholder = { Text("Search messages...") },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = NexaTextGrey) },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp), singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("All", "Groups", "Direct").forEachIndexed { idx, tab ->
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = if (selectedTab == idx) primary else NexaTagBg,
                                modifier = Modifier.clickable { selectedTab = idx }
                            ) {
                                Text(tab, color = if (selectedTab == idx) Color.White else primary,
                                    fontWeight = FontWeight.Medium, fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val filtered = chatList.filter {
                        when (selectedTab) { 1 -> it.isGroup; 2 -> !it.isGroup; else -> true }
                    }

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(filtered) { chat ->
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable { navController.navigate("conversation") }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(48.dp).clip(CircleShape).background(primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(chat.name.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(chat.name, color = NexaTextDark, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                    Text(chat.lastMessage, color = NexaTextGrey, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(chat.timestamp, color = NexaTextGrey, fontSize = 11.sp)
                                    if (chat.unreadCount > 0) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(modifier = Modifier.size(18.dp).clip(CircleShape).background(primary),
                                            contentAlignment = Alignment.Center) {
                                            Text("${chat.unreadCount}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                            Divider(color = NexaDivider, modifier = Modifier.padding(horizontal = 16.dp))
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(rememberNavController())
}
