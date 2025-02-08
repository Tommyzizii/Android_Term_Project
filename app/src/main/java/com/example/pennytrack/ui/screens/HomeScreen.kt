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
import com.example.pennytrack.viewmodels.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, expenseViewModel: ExpenseViewModel = viewModel()) {
    // Use remember to hold the expenses list to trigger recomposition when it changes
    var expenses by remember { mutableStateOf(expenseViewModel.expenses) }
    var selectedExpense by remember { mutableStateOf<Expense?>(null) } // Track selected expense for editing

    // Navigation logic for Home screen
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Penny Track",
                    fontStyle = FontStyle.Italic,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                ) },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Main content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Total Daily Expenses Display
                    Text(
                        text = "Total Daily Expenses: \$${expenses.sumOf { it.amount.toDouble() }}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

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
                                    expenses = expenseViewModel.expenses // Update expenses list after delete
                                }
                            )
                        }
                    }
                }

                BottomAppBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)

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
                            .padding(0.dp)
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
        EditDetailEditor(
            expense = expense,
            onEditComplete = { name, amount, description, date, time ->
                // Update the expense in the ViewModel
                val updatedExpense = expense.copy(title = name, amount = amount.toFloat(), description = description, date = date, time = time)
                expenseViewModel.updateExpense(updatedExpense)

                // Update the expenses list in the HomeScreen
                expenses = expenseViewModel.expenses // Get the updated list after edit

                selectedExpense = null // Close the editor after saving
            }
        )
    }
}

@Composable
fun ExpenseItem(expense: Expense, onEdit: () -> Unit, onDelete: () -> Unit) {
    Column (modifier = Modifier.padding(vertical = 8.dp)){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(16.dp))
                .border(BorderStroke(2.dp, Color.LightGray), RoundedCornerShape(16.dp))
                .padding(16.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    expense.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text("Amount: \$${expense.amount}", fontSize = 14.sp)
                Text(
                    "Description: ${expense.description}",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    "Date: ${expense.date}",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    "Time: ${expense.time}",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.Blue)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun EditDetailEditor(expense: Expense, onEditComplete: (String, Float, String, String, String) -> Unit) {
    var editedName by rememberSaveable { mutableStateOf(expense.title) }
    var editedAmount by rememberSaveable { mutableStateOf(expense.amount.toString()) }
    var editedDescription by rememberSaveable { mutableStateOf(expense.description) }
    var editedDate by rememberSaveable { mutableStateOf(expense.date) }
    var editedTime by rememberSaveable { mutableStateOf(expense.time) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text("Edit Details", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

        OutlinedTextField(
            value = editedName,
            onValueChange = { editedName = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = editedAmount,
            onValueChange = { editedAmount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = editedDescription,
            onValueChange = { editedDescription = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Date Input
        OutlinedTextField(
            value = editedDate,
            onValueChange = { editedDate = it },
            label = { Text("Date") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Time Input
        OutlinedTextField(
            value = editedTime,
            onValueChange = { editedTime = it },
            label = { Text("Time") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onEditComplete(editedName, editedAmount.toFloatOrNull() ?: 0f, editedDescription, editedDate, editedTime)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
