package com.example.pennytrack.view

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import com.example.pennytrack.ui.theme.md_theme_light_onSurface
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_primaryContainer
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.example.pennytrack.ui.theme.md_theme_light_surfaceVariant
import com.example.pennytrack.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors



@Composable
fun RightDrawerContent(
    authViewModel: AuthViewModel,
    navController: NavController,
    drawerState: DrawerState,
    onSettingsClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid

    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
    ) {
        Text(
            text = "Menu",
            modifier = Modifier
                .padding(24.dp),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Divider(
            color = md_theme_light_surfaceVariant,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        NavigationDrawerItem(
            icon = {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(
                    "Profile",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            selected = false,
            onClick = { scope.launch { drawerState.close() } },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = MaterialTheme.colorScheme.surface,
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )

        NavigationDrawerItem(
            icon = {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(
                    "Settings",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                onSettingsClick()
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = MaterialTheme.colorScheme.surface,
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )

        NavigationDrawerItem(
            icon = {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = "Bank Locations",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(
                    "Bank Locations",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("bankLocations")
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = MaterialTheme.colorScheme.surface,
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)

        )

//        // Notification Button
//        NavigationDrawerItem(
//            icon = {
//                Icon(
//                    Icons.Default.Notifications,
//                    contentDescription = "Notifications",
//                    tint = md_theme_light_primary
//                )
//            },
//            label = {
//                Text(
//                    "Notifications",
//                    color = md_theme_light_onSurface
//                )
//            },
//            selected = false,
//            onClick = {
//                scope.launch { drawerState.close() }
//                showNotificationDialog = true
//            },
//            colors = NavigationDrawerItemDefaults.colors(
//                unselectedContainerColor = md_theme_light_surface,
//                selectedContainerColor = md_theme_light_primaryContainer,
//                unselectedIconColor = md_theme_light_primary,
//                unselectedTextColor = md_theme_light_onSurface,
//                selectedIconColor = md_theme_light_primary,
//                selectedTextColor = md_theme_light_onSurface
//            ),
//            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
//        )

        NavigationDrawerItem(
            icon = {
                Icon(
                    Icons.Filled.Dangerous,
                    contentDescription = "Logout",
                    tint = Color.Red
                )
            },
            label = {
                Text(
                    "Logout",
                    color = Color.Red
                )
            },
            selected = false,
            onClick = {
                authViewModel.signout()
                scope.launch { drawerState.close() }
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = MaterialTheme.colorScheme.surface,
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).padding(bottom = 16.dp)

        )

    }
}