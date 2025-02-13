package com.example.pennytrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pennytrack.ui.theme.TopAppBarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController){

        Scaffold(

            topBar = {
                TopAppBar(
                    title = {Text("History") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = TopAppBarColor)
                )
            },

            bottomBar = {
            BottomAppBar {
                IconButton(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }

                IconButton(
                    onClick = { navController.navigate("history") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = "History")
                }

                FloatingActionButton(
                    onClick = { navController.navigate("addExpense") },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Expense")
                }

                IconButton(
                    onClick = { navController.navigate("map") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Map")
                }

                IconButton(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                }
            }
        }){innerPadding ->
            Text("Test", modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .wrapContentHeight(Alignment.CenterVertically))
        }
}