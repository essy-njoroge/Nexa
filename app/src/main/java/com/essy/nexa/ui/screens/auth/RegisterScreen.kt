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
import com.google.firebase.firestore.FirebaseFirestore

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
fun RegisterScreen(navController: NavController) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") } // ✅ ADDED
    var showPass by remember { mutableStateOf(false) }

    val years = listOf("Y1", "Y2", "Y3", "Y4", "PG") // ✅ ADDED

    Box(
        Modifier
            .fillMaxSize()
            .background(DeepMidnight)
            .padding(24.dp)
    ) {

        Column {

            Text(
                "CREATE ACCOUNT",
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                color = White
            )

            Spacer(Modifier.height(24.dp))

            InputCard { Field("Full Name", name) { name = it } }
            Spacer(Modifier.height(12.dp))

            InputCard { Field("Email", email) { email = it } }
            Spacer(Modifier.height(12.dp))

            InputCard {
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    placeholder = { Text("Password") },
                    visualTransformation =
                        if (showPass) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPass = !showPass }) {
                            Icon(
                                if (showPass) Icons.Default.Visibility
                                else Icons.Default.VisibilityOff,
                                null,
                                tint = White
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors()
                )
            }

            Spacer(Modifier.height(12.dp))

            InputCard { Field("Course", course) { course = it } }

            // ✅ ADDED YEAR SECTION
            Spacer(Modifier.height(16.dp))

            Text("Year", color = White, fontSize = 12.sp)

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                years.forEach {
                    val selected = year == it
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selected) HotPink.copy(0.3f)
                                else Color.White.copy(0.1f)
                            )
                            .clickable { year = it }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(it, color = White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email, pass)
                        .addOnSuccessListener { res ->
                            val uid = res.user?.uid ?: return@addOnSuccessListener

                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(uid)
                                .set(
                                    mapOf(
                                        "uid" to uid,
                                        "name" to name,
                                        "email" to email,
                                        "course" to course,
                                        "year" to year // ✅ ADDED
                                    )
                                )

                            navController.navigate("interests")
                        }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Gradient, RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Continue", color = White, fontWeight = FontWeight.Bold)
                }
            }

            // ✅ ADDED LOGIN LINK
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account? ", color = White)
                Text(
                    "Login",
                    color = HotPink,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    }
                )
            }
        }
    }
}

/* helpers unchanged */

@Composable
private fun InputCard(content: @Composable () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {
        content()
    }
}

@Composable
private fun Field(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = fieldColors()
    )
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
fun RegisterPreview() {
    RegisterScreen(rememberNavController())
}