package com.example.pennytrack.ui.screens

import android.os.Bundle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
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
fun HomeScreen(navController: NavController, expenseViewModel: ExpenseViewModel = viewModel()) {
    // Use remember to hold the expenses list to trigger recomposition when it changes
    val expenses by expenseViewModel.expenses.collectAsState()
    var selectedExpense by remember { mutableStateOf<Expense?>(null) } // Track selected expense for editing

    // Navigation logic for Home screen
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Penny Track",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = md_theme_light_onPrimary
                ) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = md_theme_light_primary,
                    scrolledContainerColor = md_theme_light_onPrimary
                ),
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = md_theme_light_surface,
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Main content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Total Daily Expenses Display
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = md_theme_light_surfaceVariant)
                    ){
                        Text(
                            text = "Total Daily Expenses: \$${expenses.sumOf { it.amount.toDouble() }}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        )
                    }

                    // Expense List
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                        items(expenses.size) { index ->
                            ExpenseItem(
                                expense = expenses[index],
                                onEdit = {
                                    selectedExpense = expenses[index]  // Set selected expense to edit
                                },
                                onDelete = {
                                    expenseViewModel.removeExpense(expenses[index])
                                    //expenses = expenseViewModel.expenses // Update expenses list after delete
                                }
                            )
                        }
                    }
                }

                BottomAppBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
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
                        onClick = { navController.navigate("history") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.DateRange, contentDescription = "History")
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
            }
        }
    )

    // Show the EditDetailsEditor when an expense is selected for editing
    selectedExpense?.let { expense ->
        // Edit detail editor within the HomeScreen composable
        EditExpenseDialog (
            expense = expense,
            onDismiss = { selectedExpense = null},
            onEditComplete = {
                name, amount, description, date, time ->
                val updateExpense = expense.copy(
                    title = name,
                    amount = amount.toFloat(),
                    description = description,
                    date = date,
                    time = time
                )
                expenseViewModel.updateExpense(updateExpense)
                selectedExpense = null
            }
        )
    }
}

@Composable
fun ExpenseItem(expense: Expense, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = md_theme_light_surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    expense.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = md_theme_light_onSurfaceVariant
                )
                Text(
                    "$${String.format("%.2f", expense.amount)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = md_theme_light_primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                expense.description,
                fontSize = 16.sp,
                color = md_theme_light_onSurfaceVariant.copy(alpha = 0.8f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        expense.date,
                        fontSize = 14.sp,
                        color = md_theme_light_onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Text(
                        expense.time,
                        fontSize = 14.sp,
                        color = md_theme_light_onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(32.dp)
                        //.background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = Color.Blue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(32.dp)
                        //.background(Color.Red.copy(alpha = 0.8f), CircleShape)
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}