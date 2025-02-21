package com.example.pennytrack.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.pennytrack.data.models.Expense
import com.example.pennytrack.ui.theme.md_theme_light_onPrimary
import com.example.pennytrack.ui.theme.md_theme_light_onSurfaceVariant
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.example.pennytrack.ui.theme.md_theme_light_surfaceVariant
import com.example.pennytrack.viewmodels.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel
) {

    val expenses = expenseViewModel.expenses.collectAsState().value
    val currentDate = expenseViewModel.currentDate.collectAsState().value
    var selectedExpense by remember { mutableStateOf<Expense?>(null) } // Track selected expense for editing

    // Format current date for display
    val dateFormatter = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) }
    val displayDate = remember(currentDate) {
        try {
            val parsedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(currentDate)
            dateFormatter.format(parsedDate)
        } catch (e: Exception) {
            currentDate
        }
    }

    // Calculate total expenses for today
    val totalExpenses = expenses.sumOf { it.amount.toDouble() }

    // Ensure we're always showing today's expenses when this screen appears
    LaunchedEffect(Unit) {
        expenseViewModel.refreshTodayExpenses()
    }

    // Navigation logic for Home screen
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Penny Track",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = md_theme_light_onPrimary
                        )
                        Text(
                            displayDate, // Show formatted current date
                            fontSize = 14.sp,
                            color = md_theme_light_onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = md_theme_light_surfaceVariant
                        )
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Today's Expenses",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = md_theme_light_onSurfaceVariant.copy(alpha = 0.7f),
                                modifier =  Modifier.padding(end = 32.dp)
                            )

                            Text(
                                text = "$${String.format("%.2f", totalExpenses)}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineMedium,
                                color = md_theme_light_primary,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }

                    // Empty state message
                    if (expenses.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.MoneyOff,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = md_theme_light_primary.copy(alpha = 0.5f)
                                )
                                Text(
                                    "No expenses recorded today",
                                    fontSize = 16.sp,
                                    color = md_theme_light_onSurfaceVariant.copy(alpha = 0.7f)
                                )
                                Button(
                                    onClick = { navController.navigate("addExpense") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = md_theme_light_primary
                                    )
                                ) {
                                    Text("Add Your First Expense")
                                }
                            }
                        }
                    } else {
                        // Expense List
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            items(expenses) { expense ->
                                ExpenseItem(
                                    expense = expense,
                                    onEdit = {
                                        selectedExpense = expense
                                    },
                                    onDelete = {
                                        expenseViewModel.removeExpense(expense)
                                    }
                                )
                            }
                        }
                    }
                }

                BottomAppBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    containerColor = md_theme_light_surface,
                    contentColor = md_theme_light_primary
                ) {
                    IconButton(
                        onClick = { /* Already on home */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = md_theme_light_primary // Highlight the active tab
                        )
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
            }
        }
    )

    // Show the EditDetailsEditor when an expense is selected for editing
    selectedExpense?.let { expense ->
        // Edit detail editor within the HomeScreen composable
        EditExpenseDialog (
            expense = expense,
            onDismiss = { selectedExpense = null},
            expenseViewModel = expenseViewModel
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

