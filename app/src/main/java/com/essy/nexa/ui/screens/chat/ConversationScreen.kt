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

val demoMessages = listOf(
    Message("1","other","Aisha","Hey! Are you attending the hackathon this Friday?","9:01 AM"),
    Message("2","me","Me","Yes! I've already RSVP'd. You?","9:03 AM"),
    Message("3","other","Aisha","Same! I'm thinking of pitching an idea about campus waste management with IoT sensors.","9:04 AM"),
    Message("4","me","Me","That's brilliant 🔥 I've been working on a student mental health tracker. Maybe we can combine?","9:06 AM"),
    Message("5","other","Aisha","Oh wow, that actually complements perfectly! Let's grab coffee before Friday to plan?","9:07 AM"),
    Message("6","me","Me","100%! Campus cafe at 2PM Thursday?","9:08 AM"),
    Message("7","other","Aisha","Perfect. See you then! 👋","9:09 AM")
)

// COLORS
private val DeepMidnight = Color(0xFF06050F)
private val HotPink = Color(0xFFFF2D9B)
private val BlazeOrange = Color(0xFFFF6400)
private val GoldYellow = Color(0xFFFFB300)
private val SoftWhite = Color(0xFFF5F7FF)

@Composable
fun ConversationScreen(navController: NavController) {

    val messages = remember {
        demoMessages.toMutableStateList()
    }

    var messageText by remember {
        mutableStateOf("")
    }

    val listState = rememberLazyListState()

    val gradientBrush = Brush.horizontalGradient(
        listOf(
            HotPink,
            BlazeOrange,
            GoldYellow
        )
    )

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {

        // GLOW EFFECTS
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = (-70).dp, y = (-40).dp)
                .background(
                    HotPink.copy(alpha = 0.18f),
                    CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(240.dp)
                .offset(x = 220.dp, y = 90.dp)
                .background(
                    BlazeOrange.copy(alpha = 0.12f),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 22.dp,
                        vertical = 22.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Color.White.copy(alpha = 0.08f)
                        )
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(gradientBrush),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "A",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Aisha Kamau",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(
                                    GoldYellow,
                                    CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            "Online now",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Color.White.copy(alpha = 0.08f)
                        )
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            // CHAT CONTAINER
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 34.dp,
                            topEnd = 34.dp
                        )
                    )
                    .background(
                        SoftWhite.copy(alpha = 0.05f)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(
                            topStart = 34.dp,
                            topEnd = 34.dp
                        )
                    )
            ) {

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(
                        top = 22.dp,
                        bottom = 18.dp
                    )
                ) {

                    items(messages) { msg ->

                        val isMe = msg.senderId == "me"

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                if (isMe)
                                    Arrangement.End
                                else
                                    Arrangement.Start
                        ) {

                            Column(
                                horizontalAlignment =
                                    if (isMe)
                                        Alignment.End
                                    else
                                        Alignment.Start
                            ) {

                                Box(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 24.dp,
                                                topEnd = 24.dp,
                                                bottomStart =
                                                    if (isMe) 24.dp else 6.dp,
                                                bottomEnd =
                                                    if (isMe) 6.dp else 24.dp
                                            )
                                        )
                                        .background(
                                            if (isMe)
                                                HotPink.copy(alpha = 0.9f)
                                            else
                                                Color.White.copy(alpha = 0.08f)
                                        )
                                        .border(
                                            1.dp,
                                            if (isMe)
                                                Color.Transparent
                                            else
                                                Color.White.copy(alpha = 0.06f),
                                            RoundedCornerShape(
                                                topStart = 24.dp,
                                                topEnd = 24.dp,
                                                bottomStart =
                                                    if (isMe) 24.dp else 6.dp,
                                                bottomEnd =
                                                    if (isMe) 6.dp else 24.dp
                                            )
                                        )
                                        .padding(
                                            horizontal = 16.dp,
                                            vertical = 12.dp
                                        )
                                        .widthIn(max = 290.dp)
                                ) {

                                    Column {

                                        Text(
                                            text = msg.content,
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            lineHeight = 21.sp
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = msg.timestamp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            fontSize = 10.sp,
                                            modifier = Modifier.align(
                                                Alignment.End
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // INPUT BAR
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 14.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedTextField(
                        value = messageText,
                        onValueChange = {
                            messageText = it
                        },
                        placeholder = {
                            Text(
                                "Type something...",
                                color = Color.White.copy(alpha = 0.35f)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(28.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor =
                                Color.White.copy(alpha = 0.06f),

                            unfocusedContainerColor =
                                Color.White.copy(alpha = 0.04f),

                            focusedBorderColor =
                                HotPink.copy(alpha = 0.5f),

                            unfocusedBorderColor =
                                Color.White.copy(alpha = 0.08f),

                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(gradientBrush),
                        contentAlignment = Alignment.Center
                    ) {

                        IconButton(
                            onClick = {

                                if (messageText.isNotBlank()) {

                                    messages.add(
                                        Message(
                                            id = "${messages.size + 1}",
                                            senderId = "me",
                                            senderName = "Me",
                                            content = messageText,
                                            timestamp = "Now"
                                        )
                                    )

                                    messageText = ""
                                }
                            }
                        ) {

                            Icon(
                                Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.White
                            )
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
    ConversationScreen(
        rememberNavController()
    )
}