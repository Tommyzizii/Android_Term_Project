package com.example.pennytrack.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pennytrack.viewmodels.ExpenseViewModel
import com.example.pennytrack.data.models.Expense
import java.util.*

@Composable
fun AddExpenseScreen(navController: NavController, expenseViewModel: ExpenseViewModel = viewModel()) {
    var expenseTitle by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var expenseDescription by remember { mutableStateOf("") }
    var expenseDate by remember { mutableStateOf("") }
    var expenseTime by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    // Date Picker
    val datePickerDialog = DatePickerDialog(
        navController.context,
        { _, year, month, dayOfMonth ->
            expenseDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Time Picker
    val timePickerDialog = TimePickerDialog(
        navController.context,
        { _, hourOfDay, minute ->
            expenseTime = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Add Expense", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            value = expenseTitle,
            onValueChange = { expenseTitle = it },
            label = { Text("Expense Title") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            value = expenseAmount,
            onValueChange = { expenseAmount = it },
            label = { Text("Amount") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            value = expenseDescription,
            onValueChange = { expenseDescription = it },
            label = { Text("Description") }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            value = expenseDate,
            onValueChange = {},
            label = { Text("Expense Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(Icons.Default.Edit, contentDescription = "Pick Date")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            value = expenseTime,
            onValueChange = {},
            label = { Text("Expense Time") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { timePickerDialog.show() }) {
                    Icon(Icons.Default.Edit, contentDescription = "Pick Time")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(){

            Button(onClick = {
                val expense = Expense(
                    title = expenseTitle,
                    amount = expenseAmount.toFloatOrNull() ?: 0f,
                    description = expenseDescription,
                    date = expenseDate,
                    time = expenseTime
                )
                // Save the new expense in the ViewModel
                expenseViewModel.addExpense(expense)
                navController.popBackStack()  // Navigate back after adding the expense
            }, modifier = Modifier.padding(32.dp)) {
                Text("Add Expense")
            }

            Button(onClick = { navController.popBackStack() },
                modifier = Modifier.padding(32.dp)){
                Text("Cancel")
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddExpenseScreenPreview() {
    AddExpenseScreen(navController = rememberNavController())
}
