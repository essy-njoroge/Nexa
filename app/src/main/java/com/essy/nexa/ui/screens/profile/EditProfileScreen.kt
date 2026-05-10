package com.essy.nexa.ui.screens.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

// ───────── COLORS ─────────
private val Bg = Color(0xFF06050F)
private val CardBg = Color(0xFF0F0D18)

private val Pink = Color(0xFFFF2D9B)
private val Orange = Color(0xFFFF6400)
private val Gold = Color(0xFFFFB300)
private val Purple = Color(0xFF7B2FFF)

private val White = Color.White
private val Muted = White.copy(alpha = 0.55f)

@Composable
fun EditProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()

    var name by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var skillsText by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }

    var isSaving by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val imageModel = imageUri ?: profileImageUrl

    // ───── LOAD DATA ─────
    LaunchedEffect(Unit) {
        val uid = auth.currentUser?.uid ?: return@LaunchedEffect

        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                name = doc.getString("name") ?: ""
                course = doc.getString("course") ?: ""
                year = doc.getString("year") ?: ""
                bio = doc.getString("bio") ?: ""
                profileImageUrl = doc.getString("profileImage")

                val skills = doc.get("skills") as? List<String> ?: emptyList()
                skillsText = skills.joinToString(", ")
            }
    }

    // ───── GALLERY ─────
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

        if (uri == null) return@rememberLauncherForActivityResult

        imageUri = uri
        isSaving = true

        val uid = auth.currentUser?.uid ?: return@rememberLauncherForActivityResult
        val ref = storage.reference.child("profile_images/$uid.jpg")

        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { url ->

                    profileImageUrl = url.toString()
                    imageUri = null

                    firestore.collection("users")
                        .document(uid)
                        .update("profileImage", profileImageUrl)
                        .addOnSuccessListener {
                            isSaving = false
                            Toast.makeText(context, "Photo updated", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                isSaving = false
                Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) {
        Toast.makeText(context, "Camera not enabled", Toast.LENGTH_SHORT).show()
    }

    // ───── PHOTO DIALOG ─────
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(CardBg)
            ) {

                Column(Modifier.padding(20.dp)) {

                    Text("Change Photo", color = White, fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(16.dp))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                showDialog = false
                                cameraLauncher.launch(null)
                            }
                            .padding(12.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, null, tint = Pink)
                        Spacer(Modifier.width(10.dp))
                        Text("Camera", color = White)
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                showDialog = false
                                galleryLauncher.launch("image/*")
                            }
                            .padding(12.dp)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, null, tint = Gold)
                        Spacer(Modifier.width(10.dp))
                        Text("Gallery", color = White)
                    }

                    TextButton(
                        onClick = { showDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cancel", color = Muted)
                    }
                }
            }
        }
    }

    // ───── SCREEN ─────
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Bg,
                        Color(0xFF120A2A),
                        Color(0xFF1A0B3D)
                    )
                )
            )
    ) {

        Column {

            // TOP BAR
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null, tint = White)
                }

                Text(
                    "Edit Profile",
                    color = White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // CARD
            Card(
                Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                colors = CardDefaults.cardColors(CardBg),
                border = BorderStroke(1.dp, White.copy(alpha = 0.06f))
            ) {

                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {

                    Spacer(Modifier.height(10.dp))

                    // PROFILE IMAGE
                    Box(Modifier.align(Alignment.CenterHorizontally)) {

                        Box(
                            Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            Pink.copy(0.4f),
                                            Purple.copy(0.2f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        AsyncImage(
                            model = imageModel,
                            contentDescription = null,
                            modifier = Modifier
                                .size(92.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            Modifier
                                .size(32.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(Pink, Gold))
                                )
                                .clickable { showDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, null, tint = White)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Your Identity",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Text(
                        "Update your campus profile",
                        color = Muted,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(20.dp))

                    // ───── REUSABLE FIELD ─────
                    @Composable
                    fun Field(
                        value: String,
                        label: String,
                        icon: androidx.compose.ui.graphics.vector.ImageVector,
                        onChange: (String) -> Unit
                    ) {
                        OutlinedTextField(
                            value = value,
                            onValueChange = onChange,
                            label = { Text(label) },
                            leadingIcon = { Icon(icon, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Pink,
                                unfocusedBorderColor = White.copy(0.1f),
                                cursorColor = Gold
                            )
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    Field(name, "Full Name", Icons.Default.Person) { name = it }
                    Field(course, "Course", Icons.Default.School) { course = it }
                    Field(year, "Year", Icons.Default.CalendarToday) { year = it }

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { if (it.length <= 200) bio = it },
                        label = { Text("Bio") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Text(
                        "${bio.length}/200",
                        color = Muted,
                        modifier = Modifier.align(Alignment.End),
                        fontSize = 11.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    Field(skillsText, "Skills", Icons.Default.Star) {
                        skillsText = it
                    }

                    Spacer(Modifier.height(24.dp))

                    // SAVE BUTTON
                    Button(
                        onClick = {

                            isSaving = true

                            val uid = auth.currentUser?.uid ?: return@Button
                            val skills = skillsText.split(",").map { it.trim() }

                            firestore.collection("users").document(uid)
                                .update(
                                    mapOf(
                                        "name" to name,
                                        "course" to course,
                                        "year" to year,
                                        "bio" to bio,
                                        "skills" to skills,
                                        "profileImage" to profileImageUrl
                                    )
                                )
                                .addOnSuccessListener {
                                    isSaving = false
                                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(Pink)
                    ) {

                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Check, null, tint = White)
                            Spacer(Modifier.width(8.dp))
                            Text("Save Changes", color = White)
                        }
                    }

                    Spacer(Modifier.height(40.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    EditProfileScreen(rememberNavController())
}