package com.essy.nexa.ui.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.essy.nexa.ui.theme.*

data class AiMessage(val content: String, val isUser: Boolean)

val quickPrompts = listOf(
    "🎯 Events for me today",
    "💼 Job opportunities",
    "📚 Find study groups",
    "🏫 Club updates"
)

fun getAiReply(input: String): String = when {
    input.contains("event", true) || input.contains("today", true) ->
        "Based on your interests in Technology & Startups, here are today's picks:\n\n• 🛠️ Hackathon Kickoff — 9AM, Innovation Hub\n• 🤖 AI Workshop — 2PM, Lab 3\n• 💡 Startup Pitch Prep — 5PM, Business Block\n\nShall I RSVP you to any of these?"
    input.contains("job", true) || input.contains("opportunit", true) ->
        "I found 3 opportunities matching your Kotlin & Android skills:\n\n• 💻 Android Intern @ TechCorp (Deadline: May 31)\n• 🎨 UI/UX Intern @ CreativeHub (Remote)\n• 🚀 SWE Intern @ StartupHub\n\nYour profile has been viewed by 2 recruiters this week!"
    input.contains("study", true) || input.contains("group", true) ->
        "I found 2 study groups for your modules:\n\n• 📊 Algorithms Study Group — 6 members, Tues/Thurs\n• 🖥️ Mobile Dev Workshop — 4 members, Fridays\n\nWant me to add you to one?"
    input.contains("club", true) ->
        "Here are updates from clubs you follow:\n\n• 💻 Tech Club: Meeting today at 5PM, Room B204\n• 📊 Business Club: Pitch competition registrations open!\n\nView all club updates in the Clubs tab."
    else ->
        "I'm Nexa AI — your smart campus assistant! I can help you:\n\n• 🎯 Find events tailored to your interests\n• 💼 Discover career opportunities\n• 📚 Match you with study groups\n• 🏫 Stay updated on clubs\n\nWhat can I help you with today?"
}

@Composable
fun AiAssistantScreen(navController: NavController) {
    val primary = NexaPrimary
    val messages = remember {
        mutableStateListOf(
            AiMessage("Hey! I'm Nexa AI — your smart campus companion 🤖✨\n\nI can recommend events, find opportunities, match you with study groups and more. What's on your mind?", false)
        )
    }
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Box(modifier = Modifier.fillMaxSize().background(primary)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
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

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = primary, modifier = Modifier.size(22.dp))
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("Nexa AI", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(NexaSecondary, CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Always available", color = NexaTextWhite80, fontSize = 11.sp)
                    }
                }
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
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(messages) { msg ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                            ) {
                                if (!msg.isUser) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 16.dp,
                                                topEnd = 16.dp,
                                                bottomStart = if (msg.isUser) 16.dp else 4.dp,
                                                bottomEnd = if (msg.isUser) 4.dp else 16.dp
                                            )
                                        )
                                        .background(
                                            color = if (msg.isUser) primary else Color.White
                                        )
                                        .padding(14.dp)
                                        .widthIn(max = 280.dp)
                                ) {
                                    Text(
                                        msg.content,
                                        color = if (msg.isUser) Color.White else NexaTextDark,
                                        fontSize = 14.sp,
                                        lineHeight = 21.sp
                                    )
                                }
                            }
                        }

                        if (messages.size == 1) {
                            item {
                                Column {
                                    Text(
                                        "Try asking:",
                                        color = NexaTextGrey,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                                    )

                                    quickPrompts.chunked(2).forEach { row ->
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp)
                                        ) {
                                            row.forEach { prompt ->
                                                Surface(
                                                    shape = RoundedCornerShape(12.dp),
                                                    color = Color.White,
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clickable {
                                                            messages.add(AiMessage(prompt, true))
                                                            messages.add(AiMessage(getAiReply(prompt), false))
                                                        }
                                                ) {
                                                    Text(
                                                        prompt,
                                                        color = NexaTextDark,
                                                        fontSize = 12.sp,
                                                        modifier = Modifier.padding(10.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = {
                                Text("Ask Nexa AI anything...", color = NexaTextGrey, fontSize = 14.sp)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp),
                            singleLine = false,
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primary,
                                unfocusedBorderColor = NexaDivider
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    val q = inputText
                                    messages.add(AiMessage(q, true))
                                    messages.add(AiMessage(getAiReply(q), false))
                                    inputText = ""
                                }
                            },
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(primary)
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
fun AiAssistantPreview() {
    AiAssistantScreen(rememberNavController())
}