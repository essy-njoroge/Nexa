package com.essy.nexa.ui.screens.profile

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import com.google.firebase.storage.FirebaseStorage

@Composable
fun EditProfileScreen(navController: NavController) {

    val teal = NexaPrimary

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
    var profileImageUrl by remember { mutableStateOf("") }

    var isSaving by remember { mutableStateOf(false) }
    var savedMsg by remember { mutableStateOf("") }

    var showPhotoDialog by remember { mutableStateOf(false) }

    // LOAD USER DATA
    LaunchedEffect(Unit) {

        val uid = auth.currentUser?.uid ?: return@LaunchedEffect

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->

                name = doc.getString("name") ?: ""
                course = doc.getString("course") ?: ""
                year = doc.getString("year") ?: ""
                bio = doc.getString("bio") ?: ""

                profileImageUrl =
                    doc.getString("profileImage") ?: ""

                @Suppress("UNCHECKED_CAST")
                val skills =
                    (doc.get("skills") as? List<String>)
                        ?: emptyList()

                skillsText = skills.joinToString(", ")
            }
    }

    // GALLERY PICKER
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        if (uri == null) return@rememberLauncherForActivityResult

        imageUri = uri
        isSaving = true

        val uid = auth.currentUser?.uid
            ?: return@rememberLauncherForActivityResult

        val ref =
            storage.reference.child("profile_images/$uid.jpg")

        ref.putFile(uri)
            .addOnSuccessListener {

                ref.downloadUrl.addOnSuccessListener { downloadUri ->

                    profileImageUrl = downloadUri.toString()

                    firestore.collection("users")
                        .document(uid)
                        .update(
                            "profileImage",
                            profileImageUrl
                        )
                        .addOnSuccessListener {

                            isSaving = false

                            Toast.makeText(
                                context,
                                "Profile photo updated",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {

                            isSaving = false

                            Toast.makeText(
                                context,
                                "Failed to save image",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener {

                isSaving = false

                Toast.makeText(
                    context,
                    "Failed to upload image",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // CAMERA PICKER
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        Toast.makeText(
            context,
            "Camera upload not implemented yet",
            Toast.LENGTH_SHORT
        ).show()
    }

    // PHOTO DIALOG
    if (showPhotoDialog) {

        Dialog(
            onDismissRequest = {
                showPhotoDialog = false
            }
        ) {

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        text = "Change Photo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NexaTextDark
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // CAMERA OPTION
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                showPhotoDialog = false
                                cameraLauncher.launch(null)
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    NexaTagBg,
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = teal
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {

                            Text(
                                "Take a Photo",
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                "Use your camera",
                                fontSize = 12.sp,
                                color = NexaTextGrey
                            )
                        }
                    }

                    HorizontalDivider(color = NexaDivider)

                    // GALLERY OPTION
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                showPhotoDialog = false
                                galleryLauncher.launch("image/*")
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    NexaTagBg,
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                tint = teal
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {

                            Text(
                                "Choose from Gallery",
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                "Pick from your photos",
                                fontSize = 12.sp,
                                color = NexaTextGrey
                            )
                        }
                    }

                    HorizontalDivider(color = NexaDivider)

                    TextButton(
                        onClick = {
                            showPhotoDialog = false
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {

                        Text(
                            "Cancel",
                            color = NexaTextGrey
                        )
                    }
                }
            }
        }
    }

    // MAIN UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(teal)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // TOP BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        )
                ) {

                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Edit Profile",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            // CONTENT CARD
            Card(
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                ),
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {

                    // PROFILE IMAGE
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {

                        if (
                            imageUri != null ||
                            profileImageUrl.isNotEmpty()
                        ) {

                            AsyncImage(
                                model = imageUri ?: profileImageUrl,
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(88.dp)
                                    .clip(CircleShape)
                            )

                        } else {

                            Box(
                                modifier = Modifier
                                    .size(88.dp)
                                    .clip(CircleShape)
                                    .background(teal),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = name.take(1)
                                        .uppercase()
                                        .ifEmpty { "?" },
                                    color = Color.White,
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        // CAMERA BUTTON
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(teal)
                                .clickable {
                                    showPhotoDialog = true
                                },
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Change Photo",
                        color = teal,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                showPhotoDialog = true
                            }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // PERSONAL INFO
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = {
                            Text("Full Name")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Person, null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = course,
                        onValueChange = { course = it },
                        placeholder = {
                            Text("Course / Programme")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.School, null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it },
                        placeholder = {
                            Text("Academic Year")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.CalendarToday, null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // BIO
                    OutlinedTextField(
                        value = bio,
                        onValueChange = {
                            if (it.length <= 200) {
                                bio = it
                            }
                        },
                        placeholder = {
                            Text("Tell others about yourself...")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text(
                        text = "${bio.length}/200",
                        modifier = Modifier.align(Alignment.End),
                        color = NexaTextGrey,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // SKILLS
                    OutlinedTextField(
                        value = skillsText,
                        onValueChange = {
                            skillsText = it
                        },
                        placeholder = {
                            Text("e.g Kotlin, Python")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Star, null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // SAVE BUTTON
                    Button(
                        onClick = {

                            isSaving = true

                            val uid =
                                auth.currentUser?.uid
                                    ?: return@Button

                            val skills = skillsText
                                .split(",")
                                .map { it.trim() }
                                .filter { it.isNotEmpty() }

                            firestore.collection("users")
                                .document(uid)
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

                                    Toast.makeText(
                                        context,
                                        "Profile updated",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.popBackStack()
                                }
                                .addOnFailureListener {

                                    isSaving = false

                                    Toast.makeText(
                                        context,
                                        "Failed to save",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = teal
                        )
                    ) {

                        if (isSaving) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )

                        } else {

                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Save Changes",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {

    EditProfileScreen(
        rememberNavController()
    )
}