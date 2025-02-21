package com.example.pennytrack.view

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pennytrack.data.models.Expense
import com.example.pennytrack.ui.theme.md_theme_light_onPrimary
import com.example.pennytrack.ui.theme.md_theme_light_onPrimaryContainer
import com.example.pennytrack.ui.theme.md_theme_light_onSurfaceVariant
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_primaryContainer
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.example.pennytrack.ui.theme.md_theme_light_surfaceVariant
import com.example.pennytrack.viewmodels.ExpenseViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyExpenseScreen(
    navController: NavController,
    viewModel: ExpenseViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Monthly History",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = md_theme_light_onPrimary
                    )
                },
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

                IconButton(
                    onClick = { navController.navigate("chart") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ShowChart,
                        contentDescription = "Chart"
                    )
                }

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

                IconButton(
                    onClick = { navController.navigate("history") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = "History")
                }

                IconButton(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                }
            }
        },
        containerColor = md_theme_light_surface
    ) { innerPadding ->
        MonthlyExpenseSummaryScreen(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
