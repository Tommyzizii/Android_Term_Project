package com.example.pennytrack.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.pennytrack.ui.theme.TopAppBarColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Image Picker Launcher
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        profileBitmap = bitmap // Save captured image
    }

    var showSettingsDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            RightDrawerContent(
                navController = navController,
                drawerState = drawerState,
                onSettingsClick = { showSettingsDialog = true }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Profile") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = TopAppBarColor
                    ),
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Open Menu")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar {
                    IconButton(onClick = { navController.navigate("home") }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }

                    IconButton(onClick = { navController.navigate("history") }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.DateRange, contentDescription = "History")
                    }

                    FloatingActionButton(
                        onClick = { navController.navigate("addExpense") },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Expense")
                    }

                    IconButton(onClick = { navController.navigate("map") }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Map")
                    }

                    IconButton(onClick = { navController.navigate("profile") }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Welcome message
                Text(
                    text = "Welcome, Thant Zin Min!",
                    fontSize = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Image Box
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable { launcher.launch() }, // Opens the camera
                    contentAlignment = Alignment.Center
                ) {
                    if (profileBitmap != null) {
                        Image(
                            bitmap = profileBitmap!!.asImageBitmap(),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = "T", // Placeholder (e.g., First letter of name)
                            fontSize = 40.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Upload Photo", style = MaterialTheme.typography.bodyMedium)
                Text(text = "⭐ Daily expenses ⭐", color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Details
                ProfileDetail(label = "Name", value = "Thant Zin Min")
                ProfileDetail(label = "Birthday", value = "04/06/2004")
                ProfileDetail(label = "Income", value = "50,000")
                ProfileDetail(label = "Expected Outcome", value = "30,000")

                Spacer(modifier = Modifier.height(20.dp))

//                // Back to Home Button
//                Button(
//                    onClick = { navController.navigate("home") },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Back to Home")
//                }

                //Spacer(modifier = Modifier.height(8.dp))

                // Edit Profile Button
                ButtonPanel()
            }
        }
    }


    // Show Settings Dialog
    if (showSettingsDialog) {
        SettingsDialog(
            showDialog = showSettingsDialog,
            onDismiss = { showSettingsDialog = false },
            onLanguageChange = { newLanguage -> println("Language changed to: $newLanguage") },
            onThemeChange = { isDark -> println("Theme changed to: ${if (isDark) "Dark" else "Light"} Mode") }
        )
    }
}

@Composable
fun ProfileDetail(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.small)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, color = Color.DarkGray)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ButtonPanel() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)  // Add padding for better spacing
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ElevatedButton(
            onClick = { /* Edit Action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),  // Adjust the button height
            shape = MaterialTheme.shapes.medium,  // Rounded corners
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(8.dp)  // Elevation for depth
        ) {
            Icon(
                imageVector = Icons.Default.Edit,  // Use the "Edit" icon
                contentDescription = "Edit",
                modifier = Modifier.size(20.dp)  // Adjust the icon size
            )
            Spacer(modifier = Modifier.width(8.dp))  // Space between icon and text
            Text("Edit", style = MaterialTheme.typography.bodyLarge)  // Button text
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}




