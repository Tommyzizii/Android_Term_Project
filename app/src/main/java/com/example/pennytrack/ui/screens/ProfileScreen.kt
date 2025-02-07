package com.example.pennytrack.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
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

                // Back to Home Button
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Home")
                }

                Spacer(modifier = Modifier.height(8.dp))

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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, color = Color.DarkGray)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ButtonPanel() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* Edit Action */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Edit")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun RightDrawerContent(navController: NavController, drawerState: DrawerState, onSettingsClick: () -> Unit) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp)
            .background(MaterialTheme.colorScheme.surface),
        drawerContainerColor = Color.White
    ) {
        Text(
            text = "Menu",
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
                .padding(8.dp),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black
        )

        Divider(color = Color.White, thickness = 1.dp)

        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Profile", tint = Color.Black) },
            label = { Text("Profile", color = Color.Black) },
            selected = false,
            onClick = { scope.launch { drawerState.close() } }
        )

        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color.Black) },
            label = { Text("Settings", color = Color.Black) },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                onSettingsClick()
            }
        )
    }
}

@Composable
fun SettingsDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onLanguageChange: (String) -> Unit,
    onThemeChange: (Boolean) -> Unit
) {
    var selectedLanguage by remember { mutableStateOf("English") }
    var isDarkMode by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Settings", style = MaterialTheme.typography.titleLarge)

                    Divider()

                    Text(text = "Select Language:", style = MaterialTheme.typography.bodyLarge)
                    Column {
                        listOf("English", "Myanmar").forEach { language ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = language)
                                RadioButton(
                                    selected = (selectedLanguage == language),
                                    onClick = {
                                        selectedLanguage = language
                                        onLanguageChange(language)
                                    }
                                )
                            }
                        }
                    }

                    Divider()

                    Text(text = "Theme Mode:", style = MaterialTheme.typography.bodyLarge)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = if (isDarkMode) "Dark Mode 🌙" else "Light Mode ☀️")
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = {
                                isDarkMode = it
                                onThemeChange(it)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                        Text("Done")
                    }
                }
            }
        }
    }
}
