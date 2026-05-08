package com.essy.nexa.ui.screens.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// ─── COLORS ─────────────────────────────
private val DeepMidnight = Color(0xFF06050F)
private val HotPink = Color(0xFFFF2D9B)
private val BlazeOrange = Color(0xFFFF6400)
private val GoldYellow = Color(0xFFFFB300)
private val White = Color.White

private val NeonGlass = Color.White.copy(alpha = 0.06f)
private val ChipDark = Color(0xFF1A1A24)

val postCategoryList = listOf(
    "General", "Event", "Study", "Career", "Club", "Announcement"
)

@Composable
fun CreatePostScreen(
    navController: NavController,
    previewMode: Boolean = false
) {

    var postContent by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf("General") }
    var isAnonymous by remember { mutableStateOf(false) }
    var isPosting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var authorName by remember { mutableStateOf("User") }
    var authorCourse by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    if (!previewMode) {
        LaunchedEffect(Unit) {
            val uid = auth.currentUser?.uid ?: return@LaunchedEffect

            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener {
                    authorName = it.getString("name") ?: "User"
                    authorCourse = it.getString("course") ?: ""
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // ─── HEADER ─────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(42.dp)
                        .background(NeonGlass, CircleShape)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = White
                    )
                }

                Spacer(Modifier.width(12.dp))

                Text(
                    "Create Post",
                    color = White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // ─── GLASS CONTAINER (NOT WHITE ANYMORE) ───
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(NeonGlass)
                    .padding(20.dp)
            ) {

                // TEXT INPUT
                OutlinedTextField(
                    value = postContent,
                    onValueChange = {
                        if (it.length <= 500) postContent = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 140.dp),
                    placeholder = { Text("What's happening on campus?") },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HotPink,
                        unfocusedBorderColor = Color.White.copy(0.15f),
                        cursorColor = HotPink,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "${postContent.length}/500",
                    color = White.copy(0.5f),
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(Modifier.height(18.dp))

                Text(
                    "Category",
                    fontWeight = FontWeight.Bold,
                    color = White
                )

                Spacer(Modifier.height(10.dp))

                // ─── CATEGORY CHIPS ─────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    postCategoryList.forEach { cat ->

                        val selected = selectedCat == cat

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (selected)
                                        HotPink
                                    else
                                        ChipDark
                                )
                                .clickable { selectedCat = cat }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                cat,
                                color = White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Post anonymously", modifier = Modifier.weight(1f), color = White)
                    Switch(
                        checked = isAnonymous,
                        onCheckedChange = { isAnonymous = it }
                    )
                }

                Spacer(Modifier.height(20.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = Color.Red)
                    Spacer(Modifier.height(10.dp))
                }

                // ─── BUTTON (FIXED LOADING BUG) ───
                Button(
                    onClick = {

                        if (postContent.isBlank()) {
                            errorMessage = "Write something first"
                            return@Button
                        }

                        if (auth.currentUser == null) {
                            errorMessage = "Not logged in"
                            return@Button
                        }

                        if (isPosting) return@Button
                        isPosting = true

                        firestore.collection("posts")
                            .add(
                                hashMapOf(
                                    "authorId" to auth.currentUser!!.uid,
                                    "authorName" to if (isAnonymous) "Anonymous" else authorName,
                                    "authorCourse" to if (isAnonymous) "" else authorCourse,
                                    "content" to postContent.trim(),
                                    "category" to selectedCat,
                                    "likes" to 0,
                                    "comments" to 0,
                                    "timestamp" to Timestamp.now(),
                                    "isAnonymous" to isAnonymous
                                )
                            )
                            .addOnSuccessListener {
                                isPosting = false
                                postContent = ""
                                navController.popBackStack()
                            }
                            .addOnFailureListener {
                                isPosting = false
                                errorMessage = it.message ?: "Failed"
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HotPink)
                ) {
                    Text(
                        if (isPosting) "Posting..." else "Share Post",
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
fun CreatePostPreview() {
    CreatePostScreen(rememberNavController(), previewMode = true)
}