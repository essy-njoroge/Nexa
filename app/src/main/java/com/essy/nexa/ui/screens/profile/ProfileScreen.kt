package com.essy.nexa.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(navController: NavController) {

    val teal = NexaPrimary
    var isOpenToOpportunities by remember { mutableStateOf(false) }

    var userName by remember { mutableStateOf("Loading...") }
    var userCourse by remember { mutableStateOf("") }
    var userYear by remember { mutableStateOf("") }
    var userBio by remember { mutableStateOf("") }
    var userSkills by remember { mutableStateOf(listOf<String>()) }
    var userInterests by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .addSnapshotListener { doc, _ ->
                    if (doc != null && doc.exists()) {
                        userName = doc.getString("name") ?: "User"
                        userCourse = doc.getString("course") ?: ""
                        userYear = doc.getString("year") ?: ""
                        userBio = doc.getString("bio") ?: ""

                        userSkills = (doc.get("skills") as? List<String>) ?: emptyList()
                        userInterests = (doc.get("interests") as? List<String>) ?: emptyList()

                        isOpenToOpportunities = doc.getBoolean("openToOpportunities") ?: false
                    }
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(teal)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Profile", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Black)

                IconButton(
                    onClick = { navController.navigate("edit_profile") },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            // AVATAR
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        userName.take(1).uppercase(),
                        color = teal,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(Modifier.height(10.dp))

                Text(userName, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                if (userCourse.isNotEmpty()) {
                    Text(
                        "$userCourse • $userYear",
                        color = Color.White.copy(0.8f),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // BODY CARD
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                LazyColumn(
                    contentPadding = PaddingValues(16.dp, 20.dp, 16.dp, 100.dp)
                ) {

                    // BIO
                    item {
                        Text("Bio", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            userBio.ifEmpty { "No bio yet. Tap edit to add one." },
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(20.dp))
                    }

                    // SKILLS (FIXED - NO FLOWROW)
                    if (userSkills.isNotEmpty()) {
                        item {
                            Text("Skills", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                userSkills.forEach { skill ->
                                    Surface(
                                        shape = RoundedCornerShape(50.dp),
                                        color = teal
                                    ) {
                                        Text(
                                            skill,
                                            color = Color.White,
                                            modifier = Modifier.padding(
                                                horizontal = 14.dp,
                                                vertical = 8.dp
                                            ),
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(20.dp))
                        }
                    }

                    // INTERESTS (FIXED - NO FLOWROW)
                    if (userInterests.isNotEmpty()) {
                        item {
                            Text("Interests", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                userInterests.forEach { interest ->
                                    Surface(
                                        shape = RoundedCornerShape(50.dp),
                                        color = NexaTagBg
                                    ) {
                                        Text(
                                            interest,
                                            color = teal,
                                            modifier = Modifier.padding(
                                                horizontal = 14.dp,
                                                vertical = 8.dp
                                            ),
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    ProfileScreen(rememberNavController())
}