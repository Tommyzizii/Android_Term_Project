package com.example.pennytrack.view

import android.app.DatePickerDialog
import android.content.Context
import java.util.Calendar
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pennytrack.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.surface

    // State for profile details
    var birthday by remember { mutableStateOf(TextFieldValue("")) }
    var income by remember { mutableStateOf(TextFieldValue("")) }
    var outcome by remember { mutableStateOf(TextFieldValue("")) }
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Fetch current user
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid

    // Fetch profile data from Firestore when the screen is loaded
    LaunchedEffect(userId) {
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    // Update text fields with data from Firestore
                    birthday = TextFieldValue(document.getString("birthday") ?: "")
                    income = TextFieldValue(document.getString("income") ?: "")
                    outcome = TextFieldValue(document.getString("expectedOutcome") ?: "")
                }
                .addOnFailureListener { e ->
                    // Handle error
                    println("Error fetching profile data: ${e.message}")
                }
        }
    }

    // Camera picker
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        profileBitmap = bitmap
        profileImageUri = null
    }

    // Gallery picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
        profileBitmap = null
    }

    // DatePickerDialog
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Update the birthday field with the selected date
            birthday = TextFieldValue("$dayOfMonth/${month + 1}/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.edit_profile),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        saveProfileChanges(birthday, income, outcome, navController)
                    }) {
                        Icon(
                            Icons.Filled.Save,
                            contentDescription = stringResource(R.string.save),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .clickable {
                        scope.launch {
                            galleryLauncher.launch("image/*")
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                when {
                    profileBitmap != null -> {
                        Image(
                            bitmap = profileBitmap!!.asImageBitmap(),
                            contentDescription = stringResource(R.string.profile),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    profileImageUri != null -> {
                        Image(
                            painter = rememberAsyncImagePainter(profileImageUri),
                            contentDescription = stringResource(R.string.profile),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    else -> {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = stringResource(R.string.profile),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Camera and Gallery buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                FilledTonalButton(
                    onClick = { cameraLauncher.launch() },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Outlined.CameraAlt,
                        contentDescription = stringResource(R.string.camera),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.camera), fontSize = 14.sp)
                }

                FilledTonalButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Outlined.Image,
                        contentDescription =stringResource(R.string.gallery),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.gallery), fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Text fields for profile details
            OutlinedTextField(
                value = birthday,
                onValueChange = { birthday = it },
                label = { Text(stringResource(R.string.birthday), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                leadingIcon = {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = stringResource(R.string.date),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = stringResource(R.string.pick_date),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = income,
                onValueChange = { income = it },
                label = { Text(stringResource(R.string.income), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                leadingIcon = {
                    Icon(
                        Icons.Filled.AttachMoney,
                        contentDescription = stringResource(R.string.income),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = outcome,
                onValueChange = { outcome = it },
                label = { Text(stringResource(R.string.expected_outcome), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                ),
                leadingIcon = {
                    Icon(
                        Icons.Filled.Money,
                        contentDescription = stringResource(R.string.expected_outcome),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Save Changes button that updates Firestore with new data
            Button(
                onClick = {
                    saveProfileChanges(birthday, income, outcome, navController)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(stringResource(R.string.save_changes), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun saveProfileChanges(
    birthday: TextFieldValue,
    income: TextFieldValue,
    outcome: TextFieldValue,
    navController: NavController
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        val userDocRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(currentUser.uid)
        val updatedData: Map<String, Any> = hashMapOf(
            "birthday" to birthday.text,
            "income" to income.text,
            "expectedOutcome" to outcome.text
        )
        userDocRef.update(updatedData)
            .addOnSuccessListener {
                // After a successful update, navigate back to the ProfileScreen
                navController.popBackStack()
            }
            .addOnFailureListener { e ->
                // Handle the error, e.g., show a Snackbar or log the error
                println("Error updating profile: ${e.message}")
            }
    }
}