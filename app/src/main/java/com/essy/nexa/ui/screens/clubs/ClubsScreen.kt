package com.essy.nexa.ui.screens.clubs

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.model.Club

val clubList = listOf(
    Club("1","Tech Club","Coding, hackathons, and tech talks for all skill levels","Technology",342,false,"💻"),
    Club("2","Business Club","Entrepreneurship, case studies, and competitions","Business",218,true,"📊"),
    Club("3","Drama Society","Theatre productions, improvisation, and acting workshops","Arts",156,false,"🎭"),
    Club("4","Photography Society","Visual storytelling, photo walks, and editing workshops","Arts",98,true,"📸"),
    Club("5","Environmental Club","Sustainability projects and campus clean-ups","Environment",187,false,"🌍"),
    Club("6","Sports Club","Multi-sport events, intramural leagues, and fitness","Sports",401,false,"⚽"),
    Club("7","Music Society","Live performances, open mic nights, and production","Arts",134,false,"🎵"),
    Club("8","Debate Society","Critical thinking, public speaking, and competitions","Academic",89,false,"🎤")
)

val clubCats = listOf(
    "All",
    "Technology",
    "Business",
    "Arts",
    "Sports",
    "Environment",
    "Academic"
)

// COLORS
private val DeepMidnight = Color(0xFF06050F)
private val HotPink = Color(0xFFFF2D9B)
private val BlazeOrange = Color(0xFFFF6400)
private val GoldYellow = Color(0xFFFFB300)
private val White = Color.White

@Composable
fun ClubsScreen(navController: NavController) {

    var selectedCat by remember {
        mutableStateOf("All")
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    val filtered = clubList.filter {
        (selectedCat == "All" || it.category == selectedCat) &&
                (searchQuery.isEmpty() ||
                        it.name.contains(searchQuery, true))
    }

    val gradient = Brush.horizontalGradient(
        listOf(
            HotPink,
            BlazeOrange,
            GoldYellow
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = "Discover\nClubs",
                        color = White,
                        fontSize = 38.sp,
                        lineHeight = 42.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Find communities that match your interests",
                        color = White.copy(alpha = 0.55f),
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            White.copy(alpha = 0.08f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Groups,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // SEARCH
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                placeholder = {
                    Text(
                        "Search clubs...",
                        color = White.copy(alpha = 0.4f)
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = White.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedBorderColor = HotPink.copy(alpha = 0.5f),
                    unfocusedBorderColor = White.copy(alpha = 0.08f),
                    focusedContainerColor = White.copy(alpha = 0.04f),
                    unfocusedContainerColor = White.copy(alpha = 0.04f),
                    cursorColor = HotPink
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // FILTERS
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(clubCats) { cat ->

                    val selected = selectedCat == cat

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (selected)
                                    gradient
                                else
                                    Brush.horizontalGradient(
                                        listOf(
                                            White.copy(alpha = 0.05f),
                                            White.copy(alpha = 0.05f)
                                        )
                                    )
                            )
                            .clickable {
                                selectedCat = cat
                            }
                            .padding(
                                horizontal = 18.dp,
                                vertical = 10.dp
                            )
                    ) {

                        Text(
                            text = cat,
                            color =
                                if (selected)
                                    White
                                else
                                    White.copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CLUB LIST
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {

                items(filtered) { club ->

                    ClubCard(
                        club = club,
                        gradient = gradient,
                        onClick = {
                            navController.navigate("club_detail")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ClubCard(
    club: Club,
    gradient: Brush,
    onClick: () -> Unit
) {

    var joined by remember {
        mutableStateOf(club.isJoined)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                White.copy(alpha = 0.05f)
            )
            .clickable {
                onClick()
            }
            .padding(18.dp)
    ) {

        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ICON
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            gradient
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = club.iconEmoji,
                        fontSize = 30.sp
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = club.name,
                        color = White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = club.category,
                        color = HotPink,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Groups,
                            contentDescription = null,
                            tint = White.copy(alpha = 0.45f),
                            modifier = Modifier.size(14.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "${club.members} members",
                            color = White.copy(alpha = 0.45f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = club.description,
                color = White.copy(alpha = 0.7f),
                fontSize = 13.sp,
                lineHeight = 21.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(18.dp))

            // BUTTONS
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = {
                        joined = !joined
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (joined)
                            HotPink
                        else
                            White.copy(alpha = 0.08f)
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            if (joined)
                                "Joined"
                            else
                                "Join Club",
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            gradient,
                            RoundedCornerShape(50.dp)
                        )
                ) {

                    Text(
                        text = "View",
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClubsScreenPreview() {

    ClubsScreen(
        navController = rememberNavController()
    )
}