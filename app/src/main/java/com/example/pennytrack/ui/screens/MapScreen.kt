package com.example.pennytrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pennytrack.ui.theme.md_theme_light_onPrimary
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bank Locations",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = md_theme_light_onPrimary) },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = md_theme_light_primary,
                    scrolledContainerColor = md_theme_light_onPrimary
                )
            )
        },

        bottomBar = {
            BottomAppBar(
                modifier = Modifier,
                containerColor = md_theme_light_surface,
                contentColor = md_theme_light_primary

            ) {
                IconButton(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }

                // History Icon
                IconButton(
                    onClick = { navController.navigate("chart") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ShowChart,
                        contentDescription = "Chart")
                }

                // Add Expense (Floating Action Button)
                FloatingActionButton(
                    onClick = { navController.navigate("addExpense") },
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.CenterVertically)
                        .padding(0.dp),
                    containerColor = md_theme_light_primary,
                    contentColor = md_theme_light_onPrimary
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Expense")
                }

                // Map Icon
                IconButton(
                    onClick = { navController.navigate("map") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Map")
                }

                // Profile Icon
                IconButton(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                }
            }
    },
        containerColor = md_theme_light_surface
    ){
        innerPadding ->
        Text("Map is Here", modifier = Modifier.padding(innerPadding)
            .fillMaxSize()
            .wrapContentHeight(Alignment.CenterVertically)
            .wrapContentWidth(Alignment.CenterHorizontally))
    }
}