package com.example.pennytrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

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