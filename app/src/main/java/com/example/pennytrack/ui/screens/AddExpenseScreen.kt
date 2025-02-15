package com.example.pennytrack.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.shape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pennytrack.viewmodels.ExpenseViewModel
import com.example.pennytrack.data.models.Expense
import com.example.pennytrack.ui.theme.AddScreenBackground
import com.example.pennytrack.ui.theme.TopAppBarColor
import com.example.pennytrack.ui.theme.softPurple
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
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

    Column(modifier = Modifier.fillMaxSize().background(color = Color.White).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Add Expense", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            value = expenseTitle,
            onValueChange = { expenseTitle = it },
            label = { Text("Expense Title")},
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFE0F7FA),
                focusedBorderColor = Color(0xFF00796B),
                unfocusedBorderColor = Color(0xFFB2DFDB)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            value = expenseAmount,
            onValueChange = { expenseAmount = it },
            label = { Text("Amount")},
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFE0F7FA),
                focusedBorderColor = Color(0xFF00796B),
                unfocusedBorderColor = Color(0xFFB2DFDB)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            value = expenseDescription,
            onValueChange = { expenseDescription = it },
            label = { Text("Description") },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFE0F7FA),
                focusedBorderColor = Color(0xFF00796B),
                unfocusedBorderColor = Color(0xFFB2DFDB)
            )
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
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFE0F7FA),
                focusedBorderColor = Color(0xFF00796B),
                unfocusedBorderColor = Color(0xFFB2DFDB)
            )
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
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFE0F7FA),
                focusedBorderColor = Color(0xFF00796B),
                unfocusedBorderColor = Color(0xFFB2DFDB)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(){

            Button(onClick = { navController.popBackStack() },
                modifier = Modifier.padding(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TopAppBarColor)
                ){
                Text("Cancel", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            }

            Button(onClick = {
                val newId = expenseViewModel.expenses.value.size + 1
                val expense = Expense(
                    id = newId,
                    title = expenseTitle,
                    amount = expenseAmount.toFloatOrNull() ?: 0f,
                    description = expenseDescription,
                    date = expenseDate,
                    time = expenseTime
                )
                // Save the new expense in the ViewModel
                expenseViewModel.addExpense(expense)
                navController.popBackStack()  // Navigate back after adding the expense
            },
                modifier = Modifier.padding(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TopAppBarColor)) {
                Text("Add", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddExpenseScreenPreview() {
    AddExpenseScreen(navController = rememberNavController())
}
