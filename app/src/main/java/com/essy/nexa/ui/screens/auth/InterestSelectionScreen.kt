package com.essy.nexa.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class InterestItem(val emoji: String, val label: String)

val interestList = listOf(
    InterestItem("💻", "Technology"), InterestItem("🎨", "Design"),
    InterestItem("📊", "Business"),   InterestItem("🔬", "Science"),
    InterestItem("🎭", "Drama"),      InterestItem("⚽", "Sports"),
    InterestItem("🎵", "Music"),      InterestItem("🌍", "Environment"),
    InterestItem("📸", "Photography"),InterestItem("🚀", "Startups"),
    InterestItem("🤝", "Networking"), InterestItem("📚", "Academia"),
    InterestItem("🎮", "Gaming"),     InterestItem("🏥", "Health"),
    InterestItem("📰", "Journalism"), InterestItem("🍕", "Social")
)

@Composable
fun InterestSelectionScreen(navController: NavController) {

    val selected  = remember { mutableStateListOf<String>() }
    val primary   = NexaPrimary
    var isSaving  by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(primary)) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {

            Spacer(modifier = Modifier.height(32.dp))

            Text("What are you into?", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(
                "Pick at least 3 to personalise your Nexa experience",
                color = NexaTextWhite80, fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(interestList) { item ->
                            val isSelected = selected.contains(item.label)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) primary else NexaTagBg,
                                modifier = Modifier.fillMaxWidth().clickable {
                                    if (isSelected) selected.remove(item.label)
                                    else selected.add(item.label)
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(item.emoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        item.label,
                                        color = if (isSelected) Color.White else primary,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "${selected.size} selected",
                        color = if (selected.size >= 3) primary else NexaTextGrey,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (selected.size < 3) return@Button
                            isSaving = true
                            val uid = FirebaseAuth.getInstance().currentUser?.uid
                            if (uid != null) {
                                FirebaseFirestore.getInstance()
                                    .collection("users").document(uid)
                                    .update("interests", selected.toList())
                                    .addOnSuccessListener {
                                        isSaving = false
                                        navController.navigate("home") {
                                            popUpTo("interests") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener {
                                        isSaving = false
                                        // navigate anyway even if save fails
                                        navController.navigate("home") {
                                            popUpTo("interests") { inclusive = true }
                                        }
                                    }
                            } else {
                                // No firebase user, navigate anyway
                                navController.navigate("home") {
                                    popUpTo("interests") { inclusive = true }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selected.size >= 3) primary else NexaTagBg
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                if (selected.size >= 3) "Let's Go! 🚀" else "Select at least 3",
                                color = if (selected.size >= 3) Color.White else NexaTextGrey,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Skip option
            Text(
                "Skip for now",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().clickable {
                    navController.navigate("home") {
                        popUpTo("interests") { inclusive = true }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InterestSelectionPreview() { InterestSelectionScreen(rememberNavController()) }
