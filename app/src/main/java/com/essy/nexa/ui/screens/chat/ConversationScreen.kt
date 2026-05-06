package com.essy.nexa.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.essy.nexa.model.Message
import com.essy.nexa.ui.theme.*

val demoMessages = listOf(
    Message("1","other","Aisha","Hey! Are you attending the hackathon this Friday?","9:01 AM"),
    Message("2","me","Me","Yes! I've already RSVP'd. You?","9:03 AM"),
    Message("3","other","Aisha","Same! I'm thinking of pitching an idea about campus waste management with IoT sensors.","9:04 AM"),
    Message("4","me","Me","That's brilliant 🔥 I've been working on a student mental health tracker. Maybe we can combine?","9:06 AM"),
    Message("5","other","Aisha","Oh wow, that actually complements perfectly! Let's grab coffee before Friday to plan?","9:07 AM"),
    Message("6","me","Me","100%! Campus cafe at 2PM Thursday?","9:08 AM"),
    Message("7","other","Aisha","Perfect. See you then! 👋","9:09 AM")
)

@Composable
fun ConversationScreen(navController: NavController) {
    val primary = NexaPrimary
    val messages = remember { demoMessages.toMutableStateList() }
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Box(modifier = Modifier.fillMaxSize().background(primary)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header
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
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.size(38.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                    Text("A", color = primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Aisha Kamau", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(NexaSecondary, CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Online", color = NexaTextWhite80, fontSize = 12.sp)
                    }
                }
                IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, null, tint = Color.White) }
            }

            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5FA)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        items(messages) { msg ->
                            val isMe = msg.senderId == "me"
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(
                                            topStart = 16.dp, topEnd = 16.dp,
                                            bottomStart = if (isMe) 16.dp else 4.dp,
                                            bottomEnd = if (isMe) 4.dp else 16.dp
                                        ))
                                        .background(if (isMe) primary else Color.White)
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                        .widthIn(max = 260.dp)
                                ) {
                                    Column {
                                        Text(msg.content, color = if (isMe) Color.White else NexaTextDark, fontSize = 14.sp, lineHeight = 20.sp)
                                        Text(msg.timestamp, color = if (isMe) Color.White.copy(alpha = 0.7f) else NexaTextGrey, fontSize = 10.sp,
                                            modifier = Modifier.align(Alignment.End))
                                    }
                                }
                            }
                        }
                    }

                    // Input
                    Row(
                        modifier = Modifier.fillMaxWidth().background(Color.White)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = messageText, onValueChange = { messageText = it },
                            placeholder = { Text("Type a message...", color = NexaTextGrey, fontSize = 14.sp) },
                            modifier = Modifier.weight(1f), shape = RoundedCornerShape(24.dp),
                            singleLine = false, maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primary, unfocusedBorderColor = NexaDivider
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (messageText.isNotBlank()) {
                                    messages.add(Message("${messages.size + 1}", "me", "Me", messageText, "Now"))
                                    messageText = ""
                                }
                            },
                            modifier = Modifier.size(46.dp).clip(CircleShape).background(primary)
                        ) {
                            Icon(Icons.Default.Send, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversationPreview() {
    ConversationScreen(rememberNavController())
}
