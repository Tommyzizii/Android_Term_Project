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
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pennytrack.ui.theme.md_theme_dark_primaryContainer
import com.example.pennytrack.ui.theme.md_theme_light_onPrimary
import com.example.pennytrack.ui.theme.md_theme_light_onPrimaryContainer
import com.example.pennytrack.ui.theme.md_theme_light_onSurface
import com.example.pennytrack.ui.theme.md_theme_light_onSurfaceVariant
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_primaryContainer
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.example.pennytrack.ui.theme.md_theme_light_surfaceVariant
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        profileBitmap = bitmap
    }

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
                    title = { Text("Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = md_theme_light_onPrimary) },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = md_theme_light_primary,
                        scrolledContainerColor = md_theme_light_onPrimary
                    ),
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = md_theme_light_onPrimary
                            )
                        }
                    }
                )
            },

            bottomBar = {
                BottomAppBar(
                    containerColor = md_theme_light_surface,
                    contentColor = md_theme_light_primary
                ) {
                    IconButton(onClick = { navController.navigate("home") },
                        modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }

                    IconButton(onClick = { navController.navigate("chart") },
                        modifier = Modifier.weight(1f))
                    {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ShowChart,
                            contentDescription = "Chart")
                    }

                    FloatingActionButton(
                        onClick = { navController.navigate("addExpense") },
                        modifier = Modifier.size(56.dp),
                        containerColor = md_theme_light_primary,
                        contentColor = md_theme_light_onPrimary
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Expense")
                    }

                    IconButton(onClick = { navController.navigate("history") },
                        modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.DateRange, contentDescription = "History")
                    }

                    IconButton(onClick = { navController.navigate("profile") },
                        modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(md_theme_light_surface)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Thant Zin Min",
                    style = MaterialTheme.typography.headlineMedium,
                    color = md_theme_light_onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(md_theme_light_primaryContainer)
                        .border(2.dp, md_theme_light_primary, CircleShape)
                        .clickable { launcher.launch() },
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
                            text = "T",
                            style = MaterialTheme.typography.headlineLarge,
                            color = md_theme_light_onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                ProfileDetailCard(
                    details = listOf(
                        "Birthday" to "04/06/2004",
                        "Income" to "50,000",
                        "Expected Outcome" to "30,000"
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* Edit Profile Action */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = md_theme_light_primary,
                        contentColor = md_theme_light_onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile")
                }
            }
        }

    }
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
private fun ProfileDetailCard(details: List<Pair<String, String>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = md_theme_light_surfaceVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            details.forEach { (label, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = label,
                        color = md_theme_light_onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = value,
                        color = md_theme_light_onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}




