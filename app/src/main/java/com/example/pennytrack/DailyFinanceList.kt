package com.example.pennytrack

import android.app.TimePickerDialog
import android.icu.text.CaseMap.Title
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.lang.annotations.JdkConstants
import java.util.Calendar
import java.util.Locale

data class DetailItem(
    val id: Int,
    var name: String,
    var amount: Int,
    var description: String,
    var isEditing: Boolean = false,
)

@Composable
fun ItemListApp(modifier: Modifier) {
    var items by rememberSaveable { mutableStateOf(listOf<DetailItem>()) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Column(
         modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(42.dp))
        Text("Welcome to PennyWise!", fontSize = 23.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Daily Expense Tracker", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Button(onClick = { showDialog = true }) { Text("Add") }
        }

        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items) { item ->
                if (item.isEditing) {
                    EditDetailEditor(item = item, onEditComplete = { editedName, editedAmount, editedDescription ->
                        items = items.map {
                            if (it.id == item.id) it.copy(name = editedName, amount = editedAmount, description = editedDescription, isEditing = false)
                            else it
                        }
                    })
                } else {
                    DetailList(
                        detailItem = item,
                        onEditClick = {
                            items = items.map { it.copy(isEditing = it.id == item.id) }
                        },
                        onDeleteClick = {
                            items = items.filter { it.id != item.id }
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {
        ExpenseDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onSave = { name, amount, description, date, time ->
                val newDetailItem = DetailItem(
                    id = items.size + 1,
                    name = name,
                    amount = amount,
                    description = "$description\nDate: $date\nTime: $time"
                )
                items = items + newDetailItem
            }
        )
    }
}

@Composable
fun ExpenseDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, Int, String, String, String) -> Unit
) {
    var detailName by rememberSaveable { mutableStateOf("") }
    var detailAmount by rememberSaveable { mutableStateOf("") }
    var detailDescription by rememberSaveable { mutableStateOf("") }
    var selectedDate by rememberSaveable { mutableStateOf("") }
    var selectedTime by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    val calendar = Calendar.getInstance()

    // Date Picker Dialog
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Time Picker Dialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
        }
        ,
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (detailName.isNotBlank() && detailAmount.isNotBlank()) {
                                onSave(detailName, detailAmount.toInt(), detailDescription, selectedDate, selectedTime)
                                onDismiss()
                            }
                        }
                    ) { Text("Add") }

                    Button(onClick = onDismiss) { Text("Cancel") }
                }
            },
            title = { Text("Add Expense") },
            text = {
                Column {
                    Text("Enter Item:", fontStyle = FontStyle.Italic)
                    OutlinedTextField(
                        value = detailName,
                        onValueChange = { detailName = it },
                        modifier = Modifier.padding(8.dp),
                        singleLine = true
                    )

                    Text("Enter Amount:", fontStyle = FontStyle.Italic)
                    OutlinedTextField(
                        value = detailAmount,
                        onValueChange = { detailAmount = it },
                        modifier = Modifier.padding(8.dp),
                        singleLine = true
                    )

                    Text("Description:", fontStyle = FontStyle.Italic)
                    OutlinedTextField(
                        value = detailDescription,
                        onValueChange = { detailDescription = it },
                        modifier = Modifier.padding(8.dp),
                        maxLines = 3
                    )

                    // Date Picker
                    Text("Select Date:", fontStyle = FontStyle.Italic)
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = {},
                        modifier = Modifier.padding(8.dp),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { datePickerDialog.show() }) {
                                Icon(Icons.Default.Edit, contentDescription = "Pick Date")
                            }
                        }
                    )

                    // Time Picker
                    Text("Select Time:", fontStyle = FontStyle.Italic)
                    OutlinedTextField(
                        value = selectedTime,
                        onValueChange = {},
                        modifier = Modifier.padding(8.dp),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { timePickerDialog.show() }) {
                                Icon(Icons.Default.Edit, contentDescription = "Pick Time")
                            }
                        }
                    )
                }
            }
        )
    }
}

@Composable
fun EditDetailEditor(item: DetailItem, onEditComplete: (String, Int, String) -> Unit) {
    var editedName by rememberSaveable { mutableStateOf(item.name) }
    var editedAmount by rememberSaveable { mutableStateOf(item.amount.toString()) }
    var editedDescription by rememberSaveable { mutableStateOf(item.description) }

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
            label = { Text("Name") },
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onEditComplete(editedName, editedAmount.toIntOrNull() ?: 0, editedDescription) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

@Composable
fun DetailList(detailItem: DetailItem, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
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
                detailItem.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text("Amount: ${detailItem.amount} $", fontSize = 14.sp)
            Text(
                detailItem.description,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Blue)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}