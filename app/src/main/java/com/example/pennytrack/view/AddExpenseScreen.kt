package com.example.pennytrack.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pennytrack.R
import com.example.pennytrack.viewmodels.ExpenseViewModel
import com.example.pennytrack.data.models.Expense
import com.example.pennytrack.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel
) {
    var expenseTitle by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var expenseDescription by remember { mutableStateOf("") }
    var expenseDate by remember { mutableStateOf(expenseViewModel.getTodayDate()) } // Default to today
    var expenseTime by remember { mutableStateOf("") }

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

    val calendar = Calendar.getInstance()

    // Parse the current date
    val dateComponents = expenseDate.split("/")
    if (dateComponents.size == 3) {
        try {
            calendar.set(Calendar.DAY_OF_MONTH, dateComponents[0].toInt())
            calendar.set(Calendar.MONTH, dateComponents[1].toInt() - 1)
            calendar.set(Calendar.YEAR, dateComponents[2].toInt())
        } catch (e: Exception) {
            // Use current date if parsing fails
        }
    }

    val datePickerDialog = DatePickerDialog(
        navController.context,
        { _, year, month, dayOfMonth ->
            expenseDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        navController.context,
        { _, hourOfDay, minute ->
            expenseTime = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    // Set current time by default
    LaunchedEffect(Unit) {
        expenseTime = String.format("%02d:%02d",
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.add_expense),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(stringResource(R.string.expense_title), modifier = Modifier.fillMaxWidth()
                .padding(top = 2.dp),
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            // Amount Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                    value = expenseTitle,
                    onValueChange = { expenseTitle = it },
                    label = { Text(stringResource(R.string.title)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    ),
                    singleLine = true
                )
            }

            Text(stringResource(R.string.amount_and_description), modifier = Modifier.fillMaxWidth()
                .padding(top = 2.dp),
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            // Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = expenseAmount,
                        onValueChange = { expenseAmount = it },
                        label = { Text(stringResource(R.string.amount)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        ),
                        singleLine = true
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = expenseDescription,
                            onValueChange = { expenseDescription = it },
                            label = { Text(stringResource(R.string.description)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            ),
                            minLines = 2
                        )

                        LazyRow (
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            state = rememberLazyListState(),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            items(expenseTypes) { expenseType ->
                                SuggestionChip(
                                    onClick = { expenseDescription = expenseType },
                                    label = { Text(expenseType) },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = if (expenseDescription == expenseType) {
                                            MaterialTheme.colorScheme.primaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.surface
                                        }
                                    ),
                                    border = SuggestionChipDefaults.suggestionChipBorder(
                                        enabled = true,
                                        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Text(stringResource(R.string.date_and_time), modifier = Modifier.fillMaxWidth()
                .padding(top = 2.dp),
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            // Date and Time Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = expenseDate,
                        onValueChange = {},
                        label = { Text(stringResource(R.string.date)) },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { datePickerDialog.show() }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = stringResource(R.string.pick_date),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = expenseTime,
                        onValueChange = {},
                        label = { Text(stringResource(R.string.time)) },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { timePickerDialog.show() }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = stringResource(R.string.pick_time),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(stringResource(R.string.cancel))
                }

                Button(
                    onClick = {
                        // Only proceed if title and amount are not empty
                        if (expenseTitle.isNotBlank() && expenseAmount.isNotBlank()) {
                            val expense = Expense(
                                id = 0, // Let Room generate the ID
                                title = expenseTitle,
                                amount = expenseAmount.toFloatOrNull() ?: 0f,
                                description = expenseDescription,
                                date = expenseDate,
                                time = expenseTime
                            )
                            expenseViewModel.addExpense(expense)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = expenseTitle.isNotBlank() && expenseAmount.isNotBlank()
                ) {
                    Text(stringResource(R.string.add_expense))
                }
            }
        }
    }
}