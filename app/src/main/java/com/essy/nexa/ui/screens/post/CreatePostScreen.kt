package com.essy.nexa.ui.screens.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

val postCategoryList =
    listOf("General", "Event", "Study", "Career", "Club", "Announcement")

@Composable
fun CreatePostScreen(
    navController: NavController,
    previewMode: Boolean = false
) {

    val teal = NexaPrimary

    var postContent by remember { mutableStateOf("") }
    var selectedCat by remember { mutableStateOf("General") }
    var isAnonymous by remember { mutableStateOf(false) }
    var isPosting by remember { mutableStateOf(false) }

    var authorName by remember { mutableStateOf("User") }
    var authorCourse by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    // ✅ Only run Firebase in real mode
    if (!previewMode) {
        LaunchedEffect(Unit) {
            val uid = auth.currentUser?.uid ?: return@LaunchedEffect

            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    authorName = doc.getString("name") ?: "User"
                    authorCourse = doc.getString("course") ?: ""
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
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(Modifier.width(12.dp))

                Text(
                    text = "Create Post",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            // BODY CARD
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {

                    // TEXT INPUT
                    OutlinedTextField(
                        value = postContent,
                        onValueChange = { if (it.length <= 500) postContent = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 130.dp),
                        placeholder = { Text("What's happening on campus?") },
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "Category",
                        fontWeight = FontWeight.SemiBold,
                        color = NexaTextGrey
                    )

                    Spacer(Modifier.height(10.dp))

                    // CATEGORY SCROLL
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        postCategoryList.forEach { cat ->
                            val selected = selectedCat == cat

                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = if (selected) teal else NexaTagBg,
                                modifier = Modifier.clickable { selectedCat = cat }
                            ) {
                                Text(
                                    text = cat,
                                    color = if (selected) Color.White else teal,
                                    modifier = Modifier.padding(
                                        horizontal = 14.dp,
                                        vertical = 8.dp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ANONYMOUS TOGGLE
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Post anonymously", modifier = Modifier.weight(1f))

                        Switch(
                            checked = isAnonymous,
                            onCheckedChange = { isAnonymous = it }
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // POST BUTTON
                    Button(
                        onClick = {
                            val uid = auth.currentUser?.uid
                            if (uid == null || postContent.isBlank()) return@Button

                            isPosting = true

                            val post = hashMapOf(
                                "authorId" to uid,
                                "authorName" to if (isAnonymous) "Anonymous" else authorName,
                                "authorCourse" to if (isAnonymous) "" else authorCourse,
                                "content" to postContent,
                                "category" to selectedCat,
                                "likes" to 0,
                                "comments" to 0,
                                "timestamp" to System.currentTimeMillis(),
                                "isAnonymous" to isAnonymous
                            )

                            firestore.collection("posts")
                                .add(post)
                                .addOnSuccessListener {
                                    isPosting = false
                                    navController.popBackStack()
                                }
                                .addOnFailureListener {
                                    isPosting = false
                                }
                        },
                        enabled = postContent.isNotBlank() && !isPosting,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = teal)
                    ) {
                        if (isPosting) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Share Post", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CreatePostPreview() {
    CreatePostScreen(
        navController = rememberNavController(),
        previewMode = true
    )
}

