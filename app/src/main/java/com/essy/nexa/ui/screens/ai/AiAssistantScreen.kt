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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// ─── COLORS (match your login/register theme) ─────────────────────────
private val DeepMidnight = Color(0xFF06050F)
private val HotPink = Color(0xFFFF2D9B)
private val BlazeOrange = Color(0xFFFF6400)
private val GoldYellow = Color(0xFFFFB300)

private val Gradient = Brush.horizontalGradient(
    listOf(HotPink, BlazeOrange, GoldYellow)
)

// ─── MODEL ─────────────────────────────────────────────────────────────
data class AiMessage(val content: String, val isUser: Boolean)

val quickPrompts = listOf(
    "🎯 Events for me today",
    "💼 Job opportunities",
    "📚 Find study groups",
    "🏫 Club updates"
)

// ─── AI LOGIC ──────────────────────────────────────────────────────────
fun getAiReply(input: String): String {
    val q = input.lowercase()

    return when {
        q.contains("hey") || q.contains("hello") ->
            "Hey 👋 I'm Nexa AI — your campus assistant."

        q.contains("event") || q.contains("today") ->
            "Today's events:\n• Hackathon\n• AI Workshop\n• Startup Meetup"

        q.contains("job") ->
            "Jobs:\n• Android Intern\n• UI/UX Intern\n• Software Intern"

        q.contains("study") ->
            "Study groups:\n• Mobile Dev\n• Data Structures\n• AI Club"

        q.contains("club") ->
            "Club updates:\n• Tech Club meeting today\n• Media auditions open"

        else ->
            "Got it 👍\nYou said: \"$input\""
    }
}

// ─── SCREEN ─────────────────────────────────────────────────────────────
@Composable
fun AiAssistantScreen(navController: NavController) {

    val messages = remember {
        mutableStateListOf(
            AiMessage("Hey! I'm Nexa AI 🤖 Ask me anything.", false)
        )
    }

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size - 1)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // ─── HEADER ─────────────────────────────
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
                        .background(Color.White.copy(alpha = 0.08f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }

                Spacer(Modifier.width(10.dp))

                Column {
                    Text("Nexa AI", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(
                        "Always available",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp
                    )
                }
            }

            // ─── CHAT AREA ──────────────────────────
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.04f))
                    .padding(12.dp)
            ) {

                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(messages) { msg ->

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                if (msg.isUser) Arrangement.End else Arrangement.Start
                        ) {

                            // ─── FIXED BACKGROUND (NO TYPE ERROR) ───
                            val bubbleModifier =
                                if (msg.isUser) {
                                    Modifier.background(Gradient)
                                } else {
                                    Modifier.background(Color.White.copy(alpha = 0.08f))
                                }

                            Box(
                                modifier = Modifier
                                    .then(bubbleModifier)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (msg.isUser) 16.dp else 4.dp,
                                            bottomEnd = if (msg.isUser) 4.dp else 16.dp
                                        )
                                    )
                                    .padding(14.dp)
                                    .widthIn(max = 280.dp)
                            ) {
                                Text(
                                    msg.content,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // ─── INPUT BAR ──────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            "Ask Nexa AI...",
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(30.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HotPink,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Gradient)
                        .clickable {
                            if (inputText.isNotBlank()) {
                                val q = inputText
                                messages.add(AiMessage(q, true))
                                messages.add(AiMessage(getAiReply(q), false))
                                inputText = ""
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Send, null, tint = Color.White)
                }
            }
        }
    }
}

// ─── PREVIEW ───────────────────────────────────────────────────────────
@Preview(showBackground = true)
@Composable
fun AiAssistantPreview() {
    AiAssistantScreen(rememberNavController())
}