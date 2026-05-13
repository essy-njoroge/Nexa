package com.essy.nexa.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.toMutableStateList
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
import com.essy.nexa.model.Message

private val DeepMidnight = Color(0xFF06050F)
private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val SoftWhite    = Color(0xFFF5F7FF)

// ─── Hardcoded messages per chat ID ──────────────────────────────────────────
private val allMessages = mapOf(
    "1" to listOf(
        Message("1","other","TechClub","Welcome to TechClub 💻 Session today at 5PM, Room B204","9:00 AM"),
        Message("2","other","TechClub","Please bring your laptops and chargers","9:01 AM"),
        Message("3","me","Me","Thanks! Will be there 👍","9:05 AM"),
        Message("4","other","TechClub","Great! See you then 🚀","9:06 AM")
    ),
    "2" to listOf(
        Message("1","other","Aisha","Hey! Are you attending the hackathon this Friday?","9:01 AM"),
        Message("2","me","Me","Yes! I've already RSVP'd. You?","9:03 AM"),
        Message("3","other","Aisha","Same! I'm thinking of pitching an idea about campus waste management with IoT sensors.","9:04 AM"),
        Message("4","me","Me","That's brilliant 🔥 I've been working on a student mental health tracker. Maybe we can combine?","9:06 AM"),
        Message("5","other","Aisha","Oh wow, that actually complements perfectly! Let's grab coffee before Friday to plan?","9:07 AM"),
        Message("6","me","Me","100%! Campus cafe at 2PM Thursday?","9:08 AM"),
        Message("7","other","Aisha","Perfect. See you then! 👋","9:09 AM")
    ),
    "3" to listOf(
        Message("1","other","Brian","Hey CS Year 3! Anyone finished the algorithms assignment?","8:00 AM"),
        Message("2","other","Grace","Not yet 😭 Question 4 is killing me","8:05 AM"),
        Message("3","me","Me","I finished it last night. Happy to help!","8:10 AM"),
        Message("4","other","Brian","You're a legend 🙌 Can we meet at the library?","8:12 AM"),
        Message("5","me","Me","Sure, 3PM today works?","8:13 AM"),
        Message("6","other","Grace","Perfect for me too!","8:14 AM")
    ),
    "4" to listOf(
        Message("1","other","Brian","Hey, are you coming to the hackathon?","10:00 AM"),
        Message("2","me","Me","Yes definitely! You?","10:02 AM"),
        Message("3","other","Brian","100%! Want to team up?","10:03 AM"),
        Message("4","me","Me","That would be great 🔥","10:05 AM")
    ),
    "5" to listOf(
        Message("1","other","Career Office","Hello! Your profile has been viewed by 2 recruiters this week.","Yesterday"),
        Message("2","other","Career Office","Make sure your CV is up to date on the portal.","Yesterday"),
        Message("3","me","Me","Thank you! I'll update it today.","Yesterday"),
        Message("4","other","Career Office","Great! Let us know if you need any help 😊","Yesterday")
    ),
    "6" to listOf(
        Message("1","other","Study Squad","Library session confirmed for tomorrow 6PM 📚","5h ago"),
        Message("2","other","Aisha","Which floor are we on?","5h ago"),
        Message("3","other","Brian","3rd floor quiet zone","4h ago"),
        Message("4","me","Me","I'll be there!","4h ago")
    ),
    "7" to listOf(
        Message("1","other","Grace","Hey! Are you joining the photography walk on Sunday?","Yesterday"),
        Message("2","me","Me","Yes! What time does it start?","Yesterday"),
        Message("3","other","Grace","7AM at the main gate. Don't be late 📸","Yesterday"),
        Message("4","me","Me","See you there! 🎉","Yesterday")
    ),
    "8" to listOf(
        Message("1","other","Nexa AI","Hi! You have 3 new event recommendations based on your interests.","Yesterday"),
        Message("2","other","Nexa AI","1. AI & ML Workshop — Wed 21 May","Yesterday"),
        Message("3","other","Nexa AI","2. Startup Networking — Sat 17 May","Yesterday"),
        Message("4","other","Nexa AI","3. Photography Walk — Sun 25 May","Yesterday"),
        Message("5","me","Me","Thanks! I'll check them out 👍","Yesterday")
    )
)

// ─── ConversationScreen ───────────────────────────────────────────────────────
@Composable
fun ConversationScreen(navController: NavController, chatId: String?) {

    // Load the right chat from chatList using chatId
    val chat = chatList.find { it.id == chatId } ?: chatList[0]

    // Load the right messages for this chat, allow new ones to be added locally
    val messages = remember(chatId) {
        (allMessages[chatId] ?: allMessages["2"]!!).toMutableStateList()
    }

    var messageText by remember { mutableStateOf("") }
    val listState   = rememberLazyListState()

    val gradientBrush = Brush.horizontalGradient(listOf(HotPink, BlazeOrange, GoldYellow))

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Box(modifier = Modifier.fillMaxSize().background(DeepMidnight)) {

        // Glow effects
        Box(modifier = Modifier.size(260.dp).offset(x = (-70).dp, y = (-40).dp).background(HotPink.copy(alpha = 0.18f), CircleShape))
        Box(modifier = Modifier.size(240.dp).offset(x = 220.dp, y = 90.dp).background(BlazeOrange.copy(alpha = 0.12f), CircleShape))

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header ────────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(42.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.08f))
                ) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }

                Spacer(Modifier.width(12.dp))

                Box(
                    modifier = Modifier.size(50.dp).clip(CircleShape).background(gradientBrush),
                    contentAlignment = Alignment.Center
                ) {
                    Text(chat.name.take(1), color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Shows the correct chat name
                    Text(chat.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(7.dp).background(GoldYellow, CircleShape))
                        Spacer(Modifier.width(5.dp))
                        Text(
                            if (chat.isGroup) "Group chat" else "Online now",
                            color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier.size(42.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.08f))
                ) { Icon(Icons.Default.MoreVert, null, tint = Color.White) }
            }

            // ── Chat container ────────────────────────────────────────────────
            Column(
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp))
                    .background(SoftWhite.copy(alpha = 0.05f))
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp))
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).padding(horizontal = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 22.dp, bottom = 18.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        val isMe = msg.senderId == "me"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                        ) {
                            Column(horizontalAlignment = if (isMe) Alignment.End else Alignment.Start) {

                                // Show sender name in group chats
                                if (chat.isGroup && !isMe) {
                                    Text(
                                        msg.senderName,
                                        color = HotPink.copy(alpha = 0.8f),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(
                                            topStart = 24.dp, topEnd = 24.dp,
                                            bottomStart = if (isMe) 24.dp else 6.dp,
                                            bottomEnd   = if (isMe) 6.dp else 24.dp
                                        ))
                                        .background(if (isMe) HotPink.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.08f))
                                        .border(
                                            1.dp,
                                            if (isMe) Color.Transparent else Color.White.copy(alpha = 0.06f),
                                            RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp,
                                                bottomStart = if (isMe) 24.dp else 6.dp,
                                                bottomEnd   = if (isMe) 6.dp else 24.dp)
                                        )
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                        .widthIn(max = 290.dp)
                                ) {
                                    Column {
                                        Text(msg.content, color = Color.White, fontSize = 14.sp, lineHeight = 21.sp)
                                        Spacer(Modifier.height(6.dp))
                                        Text(msg.timestamp, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, modifier = Modifier.align(Alignment.End))
                                    }
                                }
                            }
                        }
                    }
                }

                // ── Input bar ──────────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Type something...", color = Color.White.copy(alpha = 0.35f)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(28.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor   = Color.White.copy(alpha = 0.06f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.04f),
                            focusedBorderColor      = HotPink.copy(alpha = 0.5f),
                            unfocusedBorderColor    = Color.White.copy(alpha = 0.08f),
                            focusedTextColor        = Color.White,
                            unfocusedTextColor      = Color.White,
                            cursorColor             = HotPink
                        ),
                        maxLines = 4
                    )

                    Spacer(Modifier.width(10.dp))

                    Box(
                        modifier = Modifier.size(54.dp).clip(CircleShape).background(gradientBrush),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = {
                            if (messageText.isNotBlank()) {
                                messages.add(Message(
                                    id         = "${messages.size + 1}",
                                    senderId   = "me",
                                    senderName = "Me",
                                    content    = messageText,
                                    timestamp  = "Now"
                                ))
                                messageText = ""
                            }
                        }) {
                            Icon(Icons.Default.Send, null, tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversationPreview() { ConversationScreen(rememberNavController(), chatId = "2") }