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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import com.example.pennytrack.R
import com.example.pennytrack.ui.theme.md_theme_light_onSurface
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_primaryContainer
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.example.pennytrack.ui.theme.md_theme_light_surfaceVariant
import com.example.pennytrack.viewmodels.AuthViewModel
import com.example.pennytrack.viewmodels.NotificationViewModel
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
    onSettingsClick: () -> Unit,
    notificationViewModel: NotificationViewModel
) {
    val scope = rememberCoroutineScope()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid

    val notifications by notificationViewModel.notifications.observeAsState(emptyList())
    var showNotificationDialog by remember { mutableStateOf(false) }

    // Fetch notifications when the drawer is opened
    LaunchedEffect(userId) {
        if (userId != null) {
            notificationViewModel.fetchNotifications(userId)
        }
    }

    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
    ) {
        Text(
            text = stringResource(R.string.menu),
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
                    contentDescription = stringResource(R.string.profile),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.profile),
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
                    contentDescription = stringResource(R.string.settings),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(
                    stringResource(R.string.settings),
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
                    contentDescription = stringResource(R.string.bank_locations),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(
                    stringResource(R.string.bank_locations),
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

        // Notification Button
        NavigationDrawerItem(
            icon = {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.noti),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(
                    stringResource(R.string.noti),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                showNotificationDialog = true
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

        // Divider before logout
        Divider(
            color = md_theme_light_surfaceVariant,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Updated Logout Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    authViewModel.signout()
                    scope.launch { drawerState.close() }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red.copy(alpha = 0.8f)
                ),
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.ExitToApp,
                        contentDescription = stringResource(R.string.logout),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.logout),
                        color = Color.White
                    )
                }
            }
        }
    }

    // Notification Dialog
    NotificationDialog(
        showDialog = showNotificationDialog,
        onDismiss = { showNotificationDialog = false },
        notifications = notifications,
        onDeleteNotification = { notificationId ->
            if (userId != null) {
                notificationViewModel.deleteNotification(userId, notificationId)
            }
        },
        onMarkAsRead = { notificationId ->
            if (userId != null) {
                notificationViewModel.markAsRead(userId, notificationId)
            }
        }
    )
}