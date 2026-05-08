package com.essy.nexa.ui.screens.chat

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.ChatPreview
import com.essy.nexa.ui.theme.*

// ─── CHAT DATA ───────────────────────────────────────────
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {

        // ─── BACKGROUND ─────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {

                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(HotPink.copy(0.25f), androidx.compose.ui.graphics.Color.Transparent),
                            center = Offset(200f, 200f),
                            radius = 500f
                        )
                    )

                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(VioletDeep.copy(0.20f), androidx.compose.ui.graphics.Color.Transparent),
                            center = Offset(size.width, size.height),
                            radius = 600f
                        )
                    )

                    val step = 60f

                    var x = 0f
                    while (x < size.width) {
                        drawLine(
                            color = White.copy(alpha = 0.04f),
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 1f
                        )
                        x += step
                    }

                    var y = 0f
                    while (y < size.height) {
                        drawLine(
                            color = White.copy(alpha = 0.04f),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                        y += step
                    }
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {

            Spacer(Modifier.height(60.dp))

            // ─── HEADER ─────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        "Your",
                        color = White.copy(0.55f),
                        fontSize = 18.sp
                    )

                    Text(
                        "Messages",
                        color = White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(SurfaceGlass)
                        .border(1.dp, SurfaceStroke, RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Edit, null, tint = White)
                }
            }

            Spacer(Modifier.height(28.dp))

            // ─── SEARCH ─────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SurfaceGlass)
                    .border(1.dp, SurfaceStroke, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(Icons.Default.Search, null, tint = SoftText)

                Spacer(Modifier.width(10.dp))

                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search conversations...", color = MutedText) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                        cursorColor = HotPink,
                        focusedTextColor = White,
                        unfocusedTextColor = White
                    ),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(Modifier.height(18.dp))

            // ─── FILTERS ───────────────────────────────────
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                listOf("All", "Groups", "Direct").forEachIndexed { index, title ->

                    val selected = selectedTab == index

                    val bg by animateColorAsState(
                        if (selected) HotPink.copy(0.18f)
                        else SurfaceGlass,
                        label = ""
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(bg)
                            .border(
                                1.dp,
                                if (selected) HotPink.copy(0.4f) else SurfaceStroke,
                                RoundedCornerShape(50)
                            )
                            .clickable { selectedTab = index }
                            .padding(horizontal = 18.dp, vertical = 10.dp)
                    ) {
                        Text(
                            title,
                            color = if (selected) White else SoftText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(Modifier.height(22.dp))

            // ─── CHAT LIST ─────────────────────────────────
            val filtered = chatList.filter {
                val tabMatch = when (selectedTab) {
                    1 -> it.isGroup
                    2 -> !it.isGroup
                    else -> true
                }
                tabMatch && it.name.contains(searchQuery, true)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                items(filtered) { chat ->

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(SurfaceGlass)
                            .border(1.dp, SurfaceStroke, RoundedCornerShape(24.dp))
                            .clickable { navController.navigate("conversation") }
                            .padding(18.dp)
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Box(
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(FireGradient),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    chat.name.take(1),
                                    color = White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {

                                Text(
                                    chat.name,
                                    color = White,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    chat.lastMessage,
                                    color = SoftText,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {

                                Text(chat.timestamp, color = MutedText, fontSize = 11.sp)

                                if (chat.unreadCount > 0) {
                                    Spacer(Modifier.height(6.dp))

                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(FireGradient),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "${chat.unreadCount}",
                                            color = White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(100.dp)) }
            }
        }
    }
}

@Preview
@Composable
fun ChatPreview() {
    ChatScreen(rememberNavController())
}