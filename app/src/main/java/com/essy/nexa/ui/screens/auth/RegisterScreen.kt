package com.essy.nexa.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(navController: NavController) {

    var fullName  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var course    by remember { mutableStateOf("") }
    var selYear   by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg  by remember { mutableStateOf("") }

    val primary = NexaPrimary
    val years   = listOf("Y1","Y2","Y3","Y4","PG")

    Box(modifier = Modifier.fillMaxSize().background(primary)) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Join the Nexa community today", color = NexaTextWhite80, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    OutlinedTextField(value = fullName, onValueChange = { fullName = it },
                        placeholder = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp), singleLine = true)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(value = email, onValueChange = { email = it },
                        placeholder = { Text("University Email") }, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp), singleLine = true)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(value = password, onValueChange = { password = it },
                        placeholder = { Text("Password (min 6 chars)") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp), singleLine = true)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(value = course, onValueChange = { course = it },
                        placeholder = { Text("Course / Programme") }, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp), singleLine = true)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Academic Year", fontWeight = FontWeight.SemiBold, color = NexaTextDark, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        years.forEach { yr ->
                            val sel = selYear == yr
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (sel) primary else NexaTagBg,
                                modifier = Modifier.clickable { selYear = yr }
                            ) {
                                Text(yr, color = if (sel) Color.White else primary,
                                    fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                            }
                        }
                    }

                    if (errorMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(errorMsg, color = Color(0xFFE53935), fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (fullName.isBlank() || email.isBlank() || password.isBlank() || course.isBlank()) {
                                errorMsg = "Please fill in all fields"; return@Button
                            }
                            if (password.length < 6) {
                                errorMsg = "Password must be at least 6 characters"; return@Button
                            }
                            isLoading = true; errorMsg = ""
                            FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(email.trim(), password)
                                .addOnSuccessListener { result ->
                                    val uid = result.user?.uid ?: return@addOnSuccessListener
                                    // Save profile to Firestore
                                    val userMap = hashMapOf(
                                        "uid"    to uid,
                                        "name"   to fullName,
                                        "email"  to email.trim(),
                                        "course" to course,
                                        "year"   to selYear,
                                        "bio"    to "",
                                        "skills" to emptyList<String>(),
                                        "interests" to emptyList<String>(),
                                        "openToOpportunities" to false,
                                        "eventsAttended" to 0,
                                        "connections" to 0
                                    )
                                    FirebaseFirestore.getInstance()
                                        .collection("users").document(uid)
                                        .set(userMap)
                                        .addOnSuccessListener {
                                            isLoading = false
                                            navController.navigate("interests") {
                                                popUpTo("register") { inclusive = true }
                                            }
                                        }
                                        .addOnFailureListener { isLoading = false }
                                }
                                .addOnFailureListener { e ->
                                    isLoading = false
                                    errorMsg = e.message ?: "Registration failed"
                                }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primary),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading)
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        else
                            Text("Continue", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Already have an account? ", color = Color.White)
                Text("Sign In", color = Color.White, fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("login") })
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() { RegisterScreen(rememberNavController()) }
