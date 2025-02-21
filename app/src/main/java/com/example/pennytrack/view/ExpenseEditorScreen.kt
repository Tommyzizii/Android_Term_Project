package com.example.pennytrack.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pennytrack.data.models.Expense
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_primaryContainer
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.example.pennytrack.viewmodels.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditExpenseDialog(
    expense: Expense,
    onDismiss: () -> Unit,
    //onEditComplete: (String, String, String, String, String) -> Unit,
    expenseViewModel: ExpenseViewModel
) {
    var editedName by rememberSaveable { mutableStateOf(expense.title) }
    var editedAmount by rememberSaveable { mutableStateOf(expense.amount.toString()) }
    var editedDescription by rememberSaveable { mutableStateOf(expense.description) }
    var editedDate by rememberSaveable { mutableStateOf(expense.date) }
    var editedTime by rememberSaveable { mutableStateOf(expense.time) }

    val expenseTypes = remember {
        listOf(
            "Education",
            "Groceries",
            "Transportation",
            "Dining Out",
            "Entertainment",
            "Shopping",
            "Utilities"
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = md_theme_light_surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Edit Expense",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = md_theme_light_primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = md_theme_light_primary,
                        focusedLabelColor = md_theme_light_primary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editedAmount,
                    onValueChange = { editedAmount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = md_theme_light_primary,
                        focusedLabelColor = md_theme_light_primary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = editedDescription,
                        onValueChange = { editedDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = md_theme_light_primary,
                            focusedLabelColor = md_theme_light_primary
                        )
                    )

                    LazyRow (
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        state = rememberLazyListState(),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(expenseTypes) { expenseType ->
                            SuggestionChip(
                                onClick = { editedDescription = expenseType },
                                label = { Text(expenseType) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = if (editedDescription == expenseType) {
                                        md_theme_light_primaryContainer
                                    } else {
                                        md_theme_light_surface
                                    }
                                ),
                                border = SuggestionChipDefaults.suggestionChipBorder(
                                    enabled = true,
                                    borderColor = md_theme_light_primary.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Date field with current date as default if empty
                    val defaultDate = if (editedDate.isEmpty()) expenseViewModel.getTodayDate() else editedDate
                    OutlinedTextField(
                        value = defaultDate,
                        onValueChange = { editedDate = it },
                        label = { Text("Date") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = md_theme_light_primary,
                            focusedLabelColor = md_theme_light_primary
                        )
                    )

                    // Time field with current time as default if empty
                    val currentTime = if (editedTime.isEmpty()) {
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                    } else {
                        editedTime
                    }

                    OutlinedTextField(
                        value = currentTime,
                        onValueChange = { editedTime = it },
                        label = { Text("Time") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = md_theme_light_primary,
                            focusedLabelColor = md_theme_light_primary
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val updatedExpense = expense.copy(
                                title = editedName,
                                amount = editedAmount.toFloatOrNull() ?: 0f,
                                description = editedDescription,
                                date = editedDate,
                                time = editedTime
                            )
                            expenseViewModel.updateExpense(updatedExpense)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_primary)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}