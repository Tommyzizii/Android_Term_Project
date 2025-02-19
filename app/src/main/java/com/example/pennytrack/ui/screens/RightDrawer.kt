package com.example.pennytrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pennytrack.ui.theme.md_theme_light_onSurface
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_primaryContainer
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.example.pennytrack.ui.theme.md_theme_light_surfaceVariant
import kotlinx.coroutines.launch

@Composable
fun RightDrawerContent(
    navController: NavController,
    drawerState: DrawerState,
    onSettingsClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp),
        drawerContainerColor = md_theme_light_surface,
    ) {
        Text(
            text = "Menu",
            modifier = Modifier
                .padding(24.dp),
            style = MaterialTheme.typography.titleLarge,
            color = md_theme_light_onSurface
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
                    tint = md_theme_light_primary
                )
            },
            label = {
                Text(
                    "Profile",
                    color = md_theme_light_onSurface
                )
            },
            selected = false,
            onClick = { scope.launch { drawerState.close() } },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = md_theme_light_surface,
                selectedContainerColor = md_theme_light_primaryContainer,
                unselectedIconColor = md_theme_light_primary,
                unselectedTextColor = md_theme_light_onSurface,
                selectedIconColor = md_theme_light_primary,
                selectedTextColor = md_theme_light_onSurface
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )

        NavigationDrawerItem(
            icon = {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = md_theme_light_primary
                )
            },
            label = {
                Text(
                    "Settings",
                    color = md_theme_light_onSurface
                )
            },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                onSettingsClick()
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = md_theme_light_surface,
                selectedContainerColor = md_theme_light_primaryContainer,
                unselectedIconColor = md_theme_light_primary,
                unselectedTextColor = md_theme_light_onSurface,
                selectedIconColor = md_theme_light_primary,
                selectedTextColor = md_theme_light_onSurface
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )

        NavigationDrawerItem(
            icon = {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = "Bank Locations",
                    tint = md_theme_light_primary
                )
            },
            label = {
                Text(
                    "Bank Locations",
                    color = md_theme_light_onSurface
                )
            },
            selected = false,
            onClick = {
                scope.launch { drawerState.close() }
                // I want to navigate to new screen.
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = md_theme_light_surface,
                selectedContainerColor = md_theme_light_primaryContainer,
                unselectedIconColor = md_theme_light_primary,
                unselectedTextColor = md_theme_light_onSurface,
                selectedIconColor = md_theme_light_primary,
                selectedTextColor = md_theme_light_onSurface
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)

        )
    }
}