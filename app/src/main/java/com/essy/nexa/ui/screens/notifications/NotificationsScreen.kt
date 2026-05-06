package com.essy.nexa.ui.screens.notifications

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Notification
import com.essy.nexa.ui.theme.*

val notifList = listOf(
    Notification("1","Event Reminder 📅","Annual Hackathon starts in 2 hours — Innovation Hub, Block C","2h ago","event",false),
    Notification("2","New Message 💬","Aisha Kamau sent you a message","3h ago","message",false),
    Notification("3","Recruiter Interest 💼","A recruiter from TechCorp viewed your profile","5h ago","recruiter",false),
    Notification("4","AI Recommendation ✨","3 new events match your interests this week","6h ago","ai",true),
    Notification("5","Club Update 🏫","Tech Club: Meeting today at 5PM in Room B204","8h ago","club",true),
    Notification("6","Event RSVP Confirmed ✅","You're registered for the Career Fair on Monday","Yesterday","event",true),
    Notification("7","New Post 📝","Business Club posted an announcement you might like","Yesterday","post",true),
    Notification("8","Study Group Match 🧠","Nexa found a study group for your Algorithms module","2 days ago","ai",true)
)

fun notifIcon(type: String): ImageVector = when (type) {
    "event"    -> Icons.Default.Event
    "message"  -> Icons.Default.Chat
    "recruiter"-> Icons.Default.Work
    "ai"       -> Icons.Default.AutoAwesome
    "club"     -> Icons.Default.Groups
    "post"     -> Icons.Default.Article
    else       -> Icons.Default.Notifications
}

fun notifColor(type: String) = when (type) {
    "event"    -> NexaPrimary
    "message"  -> NexaSecondary
    "recruiter"-> NexaWarning
    "ai"       -> NexaPrimary
    "club"     -> NexaAccent
    else       -> NexaTextGrey
}

@Composable
fun NotificationsScreen(navController: NavController) {
    val primary = NexaPrimary
    val notifications = remember { notifList.toMutableStateList() }

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
                Text("Notifications", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f))
                TextButton(onClick = { notifications.replaceAll { it.copy(isRead = true) } }) {
                    Text("Mark all read", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                }
            }

            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(12.dp))

                    val unread = notifications.count { !it.isRead }
                    if (unread > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NexaTagBg,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            Text("$unread unread", color = primary, fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(notifications) { notif ->
                            val color = notifColor(notif.type)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (!notif.isRead) NexaTagBg else Color.White)
                                    .clickable {
                                        val idx = notifications.indexOf(notif)
                                        if (idx != -1) notifications[idx] = notif.copy(isRead = true)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier.size(44.dp).clip(CircleShape)
                                        .background(color.copy(alpha = 0.14f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(notifIcon(notif.type), null, tint = color, modifier = Modifier.size(22.dp))
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(notif.title, color = NexaTextDark,
                                            fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                                            modifier = Modifier.weight(1f))
                                        if (!notif.isRead) {
                                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(primary))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(notif.message, color = NexaTextGrey, fontSize = 13.sp, lineHeight = 18.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(notif.timestamp, color = NexaTextGrey, fontSize = 11.sp)
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
fun NotificationsPreview() {
    NotificationsScreen(rememberNavController())
}
