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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pennytrack.data.models.Expense
import com.example.pennytrack.viewmodels.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel
) {
    // Collect expenses and current date from the view model.
    val expenses by expenseViewModel.expenses.collectAsState()
    val currentDate by expenseViewModel.currentDate.collectAsState()
    var selectedExpense by remember { mutableStateOf<Expense?>(null) }
    // Get the total expense for the current date from LiveData.
    val totalExpense by expenseViewModel.getTotalExpenseForDay(currentDate).observeAsState(0f)

    // Determine whether the current page is Today or Yesterday.
    val isToday = expenseViewModel.isToday(currentDate)
    val isYesterday = expenseViewModel.isYesterday(currentDate)

    // Format the current date for display.
    val dateFormatter = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) }
    val displayDate = remember(currentDate) {
        try {
            val parsedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(currentDate)
            dateFormatter.format(parsedDate)
        } catch (e: Exception) {
            currentDate
        }
    }

    // Calculate total expenses from the list (optional if you want to use the LiveData total).
    val totalExpenses = expenses.sumOf { it.amount.toDouble() }

    // When the HomeScreen appears, refresh today's expenses.
    LaunchedEffect(Unit) {
        expenseViewModel.refreshTodayExpenses()
    }

    // Pagination: Toggle between Today and Yesterday.
    var selectedPage by remember { mutableStateOf("Today") }
    val todayDate = expenseViewModel.getTodayDate()
    val yesterdayDate = remember {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.format(calendar.time)
    }
    // When selectedPage changes, update the view model's current date.
    LaunchedEffect(selectedPage) {
        if (selectedPage == "Today") {
            expenseViewModel.setDate(todayDate)
        } else {
            expenseViewModel.setDate(yesterdayDate)
        }
    }

    // UI Scaffold with top bar, bottom pagination toggle, and expense list.
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
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            displayDate,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            Column {
                // Pagination Toggle Row.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { selectedPage = "Today" },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (selectedPage == "Today") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("Today")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(
                        onClick = { selectedPage = "Yesterday" },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (selectedPage == "Yesterday") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("Yesterday")
                    }
                }
                // Bottom Navigation Bar.
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    IconButton(
                        onClick = { /* Already on home */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = MaterialTheme.colorScheme.primary
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
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
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
        },
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Total Expenses Display Card.
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (selectedPage == "Today") "Today's Expenses" else "Yesterday's Expenses",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.padding(end = 32.dp)
                            )
                            Text(
                                text = "$${String.format("%.2f", totalExpenses)}",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }

                    // Expense List or Empty State.
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
                                    Icons.Filled.MoneyOff,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                )
                                Text(
                                    "No expenses recorded for ${if (selectedPage == "Today") "today" else "yesterday"}",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                                Button(
                                    onClick = { navController.navigate("addExpense") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("Add Your First Expense")
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            items(expenses) { expense ->
                                ExpenseItem(
                                    expense = expense,
                                    onEdit = { selectedExpense = expense },
                                    onDelete = { expenseViewModel.removeExpense(expense) }
                                )
                            }
                        }
                    }
                }

                selectedExpense?.let { expense ->
                    EditExpenseDialog(
                        expense = expense,
                        onDismiss = { selectedExpense = null },
                        expenseViewModel = expenseViewModel
                    )
                }
            }
        }
    )
}


@Composable
fun ExpenseItem(expense: Expense, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    expense.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "$${String.format("%.2f", expense.amount)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                expense.description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Text(
                        expense.time,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
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
                        modifier = Modifier.size(32.dp)
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
