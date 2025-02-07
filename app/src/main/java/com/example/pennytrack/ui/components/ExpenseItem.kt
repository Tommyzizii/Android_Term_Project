package com.example.pennytrack.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pennytrack.data.models.Expense

@Composable
fun ExpenseItem(expense: Expense) {
    Row {
        Text(expense.title)
        Spacer(modifier = Modifier.width(8.dp))
        Text("\$${expense.amount}")
        Spacer(modifier = Modifier.width(8.dp))
        Text(expense.date)
        Spacer(modifier = Modifier.width(8.dp))
        Text(expense.time)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Description: ${expense.description}")
    }
}
