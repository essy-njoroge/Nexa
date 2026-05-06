package com.essy.nexa.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.essy.nexa.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EditProfileScreen(navController: NavController) {
    val teal = NexaPrimary
    var name     by remember { mutableStateOf("") }
    var course   by remember { mutableStateOf("") }
    var year     by remember { mutableStateOf("") }
    var bio      by remember { mutableStateOf("") }
    var skillsText by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var savedMsg by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showPhotoDialog by remember { mutableStateOf(false) }

    // Load current data
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@LaunchedEffect
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                name   = doc.getString("name") ?: ""
                course = doc.getString("course") ?: ""
                year   = doc.getString("year") ?: ""
                bio    = doc.getString("bio") ?: ""
                @Suppress("UNCHECKED_CAST")
                val skills = (doc.get("skills") as? List<String>) ?: emptyList()
                skillsText = skills.joinToString(", ")
            }
    }

    // Gallery picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    // Camera picker
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap -> /* In a real app upload bitmap to Firebase Storage */ }

    // Photo source dialog
    if (showPhotoDialog) {
        Dialog(onDismissRequest = { showPhotoDialog = false }) {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Change Photo", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = NexaTextDark)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Camera option
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            showPhotoDialog = false
                            cameraLauncher.launch(null)
                        }.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(44.dp).background(NexaTagBg, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.CameraAlt, null, tint = teal, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("Take a Photo", color = NexaTextDark, fontWeight = FontWeight.SemiBold)
                            Text("Use your camera", color = NexaTextGrey, fontSize = 12.sp)
                        }
                    }

                    HorizontalDivider(color = NexaDivider)

                    // Gallery option
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            showPhotoDialog = false
                            galleryLauncher.launch("image/*")
                        }.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(44.dp).background(NexaTagBg, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PhotoLibrary, null, tint = teal, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("Choose from Gallery", color = NexaTextDark, fontWeight = FontWeight.SemiBold)
                            Text("Pick from your photos", color = NexaTextGrey, fontSize = 12.sp)
                        }
                    }

                    HorizontalDivider(color = NexaDivider)

                    TextButton(onClick = { showPhotoDialog = false }, modifier = Modifier.align(Alignment.End)) {
                        Text("Cancel", color = NexaTextGrey)
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(teal)) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Edit Profile", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    isSaving = true; savedMsg = ""
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@TextButton
                    val skills = skillsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    FirebaseFirestore.getInstance().collection("users").document(uid)
                        .update(mapOf("name" to name, "course" to course, "year" to year, "bio" to bio, "skills" to skills))
                        .addOnSuccessListener { isSaving = false; savedMsg = "Saved!"; navController.popBackStack() }
                        .addOnFailureListener { isSaving = false; savedMsg = "Failed to save" }
                }) {
                    if (isSaving) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    else Text("Save", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Card(shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp)) {
                Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {

                    // Avatar with camera/gallery button
                    Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        if (imageUri != null) {
                            AsyncImage(model = imageUri, contentDescription = "Profile photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(88.dp).clip(CircleShape))
                        } else {
                            Box(modifier = Modifier.size(88.dp).clip(CircleShape).background(teal),
                                contentAlignment = Alignment.Center) {
                                Text(name.take(1).uppercase().ifEmpty { "?" }, color = Color.White,
                                    fontSize = 36.sp, fontWeight = FontWeight.Black)
                            }
                        }
                        // Camera icon overlay — OPENS DIALOG
                        Box(modifier = Modifier.size(28.dp).align(Alignment.BottomEnd)
                            .clip(CircleShape).background(teal).clickable { showPhotoDialog = true },
                            contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Change Photo", color = teal, fontSize = 13.sp, fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.CenterHorizontally).clickable { showPhotoDialog = true })

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Personal Info", color = NexaTextGrey, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(value = name, onValueChange = { name = it },
                        placeholder = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = NexaTextGrey) },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(value = course, onValueChange = { course = it },
                        placeholder = { Text("Course / Programme") },
                        leadingIcon = { Icon(Icons.Default.School, null, tint = NexaTextGrey) },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(value = year, onValueChange = { year = it },
                        placeholder = { Text("Academic Year (e.g. Year 3)") },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = NexaTextGrey) },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)

                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Bio", color = NexaTextGrey, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(value = bio, onValueChange = { if (it.length <= 200) bio = it },
                        placeholder = { Text("Tell others about yourself...") },
                        modifier = Modifier.fillMaxWidth().height(110.dp), shape = RoundedCornerShape(12.dp))
                    Text("${bio.length}/200", color = NexaTextGrey, fontSize = 11.sp,
                        modifier = Modifier.align(Alignment.End).padding(top = 4.dp))

                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Skills (comma separated)", color = NexaTextGrey, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(value = skillsText, onValueChange = { skillsText = it },
                        placeholder = { Text("e.g. Kotlin, Python, Design") },
                        leadingIcon = { Icon(Icons.Default.Star, null, tint = NexaTextGrey) },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(onClick = {
                        isSaving = true; savedMsg = ""
                        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button
                        val skills = skillsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        FirebaseFirestore.getInstance().collection("users").document(uid)
                            .update(mapOf("name" to name, "course" to course, "year" to year, "bio" to bio, "skills" to skills))
                            .addOnSuccessListener { isSaving = false; navController.popBackStack() }
                            .addOnFailureListener { isSaving = false }
                    },
                        colors = ButtonDefaults.buttonColors(containerColor = teal),
                        shape = RoundedCornerShape(50.dp), modifier = Modifier.fillMaxWidth().height(50.dp)) {
                        if (isSaving) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        else { Icon(Icons.Default.Check, null, tint = Color.White); Spacer(modifier = Modifier.width(8.dp)); Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePreview() { EditProfileScreen(rememberNavController()) }
