package com.essy.nexa.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

// COLORS
private val DeepMidnight = Color(0xFF06050F)
private val HotPink = Color(0xFFFF2D9B)
private val BlazeOrange = Color(0xFFFF6400)
private val GoldYellow = Color(0xFFFFB300)
private val White = Color.White

private val Gradient = Brush.linearGradient(
    listOf(HotPink, BlazeOrange, GoldYellow)
)

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var show by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepMidnight)
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            // HEADER
            Text(
                text = "NEXA",
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                color = White
            )

            Spacer(Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(120.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Gradient)
            )

            Spacer(Modifier.height(32.dp))

            // EMAIL FIELD (FIXED WHITE CARD)
            InputCard {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors()
                )
            }

            Spacer(Modifier.height(14.dp))

            // PASSWORD FIELD (FIXED WHITE CARD)
            InputCard {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password") },
                    singleLine = true,
                    visualTransformation =
                        if (show) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { show = !show }) {
                            Icon(
                                if (show) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = HotPink
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors()
                )
            }

            if (error.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                Text(error, color = Color.Red)
            }

            Spacer(Modifier.height(24.dp))

            // LOGIN BUTTON
            Button(
                onClick = {
                    loading = true
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            loading = false
                            navController.navigate("home")
                        }
                        .addOnFailureListener {
                            loading = false
                            error = it.message ?: "Login failed"
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                enabled = !loading
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Gradient, RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (loading) "Loading..." else "Login",
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // SIGN UP LINK
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = White.copy(0.6f),
                    fontSize = 13.sp
                )

                Text(
                    text = "Sign up",
                    color = HotPink,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate("register")
                    }
                )
            }
        }
    }
}

/* ───── FIX: WHITE INPUT BACKGROUND LIKE REGISTER ───── */
@Composable
private fun InputCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)   // <- FIXED: clear white like register
            .padding(12.dp)
    ) {
        content()
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = HotPink,
    unfocusedBorderColor = Color.Black.copy(0.2f),
    cursorColor = HotPink,
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black
)

@Preview
@Composable
fun LoginPreview() {
    LoginScreen(rememberNavController())
}