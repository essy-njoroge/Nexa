package com.essy.nexa.ui.screens.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// ───────────────── COLORS ─────────────────
private val DeepMidnight = Color(0xFF06050F)
private val HotPink      = Color(0xFFFF2D9B)
private val BlazeOrange  = Color(0xFFFF6400)
private val GoldYellow   = Color(0xFFFFB300)
private val White        = Color.White

// ───────────────── DATA ─────────────────
data class InterestItem(val emoji: String, val label: String)

val interestList = listOf(
    InterestItem("💻", "Technology"),
    InterestItem("🎨", "Design"),
    InterestItem("📊", "Business"),
    InterestItem("🔬", "Science"),
    InterestItem("🎭", "Drama"),
    InterestItem("⚽", "Sports"),
    InterestItem("🎵", "Music"),
    InterestItem("🌍", "Environment"),
    InterestItem("📸", "Photography"),
    InterestItem("🚀", "Startups"),
    InterestItem("🤝", "Networking"),
    InterestItem("📚", "Academia"),
    InterestItem("🎮", "Gaming"),
    InterestItem("🏥", "Health"),
    InterestItem("📰", "Journalism"),
    InterestItem("🍕", "Social")
)

// ───────────────── BACKGROUND ─────────────────
@Composable
fun RegisterBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(DeepMidnight, Color(0xFF0A0A18)))
            )
    )
}

// ───────────────── LOGO ─────────────────
@Composable
fun MiniLogo() {
    Text(
        text = "NEXA",
        color = White,
        fontSize = 22.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 3.sp
    )
}

// ───────────────── MAIN SCREEN ─────────────────
@Composable
fun InterestSelectionScreen(navController: NavController) {

    val selected  = remember { mutableStateListOf<String>() }
    var isSaving  by remember { mutableStateOf(false) }
    var errorMsg  by remember { mutableStateOf("") }

    val buttonGradient = Brush.horizontalGradient(listOf(HotPink, BlazeOrange, GoldYellow))

    // ── Save interests to Firestore then navigate home ──
    fun saveAndContinue() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            errorMsg = "Not logged in. Please restart the app."
            return
        }
        isSaving = true
        errorMsg = ""

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .update("interests", selected.toList())
            .addOnSuccessListener {
                isSaving = false
                navController.navigate("home") {
                    // Clear the entire auth stack so back doesn't return here
                    popUpTo(0) { inclusive = true }
                }
            }
            .addOnFailureListener { e ->
                isSaving = false
                errorMsg = "Couldn't save interests. Please try again."
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
    ) {

        RegisterBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp)
        ) {

            Spacer(Modifier.height(60.dp))

            MiniLogo()

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Choose\nYour Interests",
                color = White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 48.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Pick at least 3 to personalize your Nexa experience",
                color = White.copy(alpha = 0.45f),
                fontSize = 13.sp
            )

            Spacer(Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(White.copy(alpha = 0.04f))
                    .border(1.dp, White.copy(alpha = 0.08f), RoundedCornerShape(24.dp))
                    .padding(18.dp)
            ) {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(420.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(interestList) { item ->
                        val isSelected = selected.contains(item.label)

                        val animatedBorder by animateColorAsState(
                            if (isSelected) HotPink.copy(alpha = 0.6f) else White.copy(alpha = 0.08f)
                        )
                        val animatedBg by animateColorAsState(
                            if (isSelected) HotPink.copy(alpha = 0.18f) else White.copy(alpha = 0.04f)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(animatedBg)
                                .border(1.dp, animatedBorder, RoundedCornerShape(16.dp))
                                .clickable {
                                    if (isSelected) selected.remove(item.label)
                                    else selected.add(item.label)
                                }
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(item.emoji, fontSize = 22.sp)
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    item.label,
                                    color = if (isSelected) White else White.copy(alpha = 0.75f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "${selected.size} selected",
                    color = if (selected.size >= 3) HotPink else White.copy(alpha = 0.4f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Error message
                if (errorMsg.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = errorMsg,
                        color = Color.Red.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { saveAndContinue() },   // ← FIXED: was empty { }
                    enabled = selected.size >= 3 && !isSaving,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(buttonGradient, RoundedCornerShape(50.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                color = White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text(
                                text = if (selected.size >= 3) "Let's Go 🚀" else "Select at least 3",
                                color = White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Skip for now",
                color = White.copy(alpha = 0.55f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF06050F)
@Composable
fun InterestSelectionScreenPreview() {
    InterestSelectionScreen(navController = rememberNavController())
}