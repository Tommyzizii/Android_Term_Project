package com.example.pennytrack

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.lang.annotations.JdkConstants

data class DetailItem(
    val id: Int,
    var name: String,
    var amount: Int,
    var description: String,
    var isEditing: Boolean = false,
)

@Composable
fun ItemListApp(modifier: Modifier){
    var sItems by rememberSaveable { mutableStateOf(listOf<DetailItem>()) }
    var detailName by rememberSaveable { mutableStateOf("") }
    var detailAmount by rememberSaveable { mutableStateOf("") }
    var detailDescription by rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }


    Column (
        modifier = Modifier.fillMaxSize().
        wrapContentWidth(Alignment.CenterHorizontally).
        wrapContentHeight(Alignment.CenterVertically))
    {
        Text(
            "Welcome!!!",
            modifier = Modifier.padding(15.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )

        Text(
            "PennyWise",
            modifier = Modifier.padding(14.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row (modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text("Daily Expense Tracker",
                modifier = Modifier.padding(16.dp).align(Alignment.CenterVertically))
            Button(
                onClick = {showDialog = true},
                modifier = Modifier.padding(16.dp)
            ) { Text("Add") }
        }


        LazyColumn (
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            items(sItems){
                    item ->
                if(item.isEditing){
                    EditDetailEditor(item = item,
                        onEditComplete = {
                                editedName, editedAmount, editedDescription ->
                            sItems = sItems.map { it.copy(isEditing = false) }
                            val editedDetail = sItems.find { it.id == item.id }
                            editedDetail?.let {
                                it.name = editedName
                                it.amount = editedAmount
                                it.description = editedDescription
                            }
                        })
                }else{
                    DetailList(detailItem = item,
                        onEditClick = {
                            sItems = sItems.map { it.copy(isEditing = it.id == item.id) }},
                        onDeleteClick = {
                            sItems = sItems - item
                        }
                    )
                }
            }
        }
    }

    if(showDialog){
        AlertDialog(
            onDismissRequest = {showDialog = false},
            confirmButton = {
                Row (modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(
                        onClick = {
                            if (detailName.isNotBlank()){
                                val newDetailItem = DetailItem(
                                    id = sItems.size + 1,
                                    name = detailName,
                                    amount = detailAmount.toInt(),
                                    description = detailDescription
                                )
                                sItems = sItems + newDetailItem
                                showDialog = false
                                detailName = ""
                                detailAmount = ""
                                detailDescription = ""
                            }
                        }
                    ) { Text("Add") }
                    Button(
                        onClick = {
                            showDialog = false
                        }
                    ) { Text("Cancel") }
                }
            },
            title = { Text("Update Expenses") },
            text = {
                Column {
                    Text("Enter Item:", fontStyle = FontStyle.Italic)
                    OutlinedTextField(
                        value = detailName,
                        onValueChange = {detailName = it},
                        modifier = Modifier.
                        wrapContentHeight(Alignment.CenterVertically).
                        padding(8.dp),
                        singleLine = true,
                        maxLines = 1
                    )
                    Text("Enter Amount:", fontStyle = FontStyle.Italic)
                    OutlinedTextField(
                        value = detailAmount,
                        onValueChange = {detailAmount = it},
                        modifier = Modifier.
                        wrapContentHeight(Alignment.CenterVertically).
                        padding(8.dp),
                        singleLine = true,
                        maxLines = 1
                    )
                    Text("Description", fontStyle = FontStyle.Italic)
                    OutlinedTextField(
                        value = detailDescription,
                        onValueChange = {detailDescription = it},
                        modifier = Modifier.
                        wrapContentHeight(Alignment.CenterVertically).
                        padding(8.dp),
                        maxLines = 3
                    )
                }
            }
        )
    }
}

@Composable
fun EditDetailEditor(
    item: DetailItem,
    onEditComplete: (String,Int,String) -> Unit,
){
    var editedName by rememberSaveable { mutableStateOf(item.name) }
    var editedAmount by rememberSaveable { mutableStateOf(item.amount.toString()) }
    var editedDescription by rememberSaveable { mutableStateOf(item.description) }
    var isEditing by rememberSaveable { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color.White).padding(8.dp).border(
                border = BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
            BasicTextField(
                value = editedAmount,
                onValueChange = {editedAmount = it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
            BasicTextField(
                value = editedDescription,
                onValueChange = {editedDescription = it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
        }

        Button(onClick = {
            isEditing = false
            onEditComplete(editedName,editedAmount.toIntOrNull() ?:1,editedDescription)
        }, modifier = Modifier.align(Alignment.CenterVertically)) {
            Text("Save",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium)
        }
    }

}

@Composable
fun DetailList(
    detailItem: DetailItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
){

    Column {
        Text(detailItem.description, modifier = Modifier.padding(8.dp),
            fontSize = 15.sp,
            color = Color.DarkGray)
    }

    Row ( modifier = Modifier.padding(8.dp).fillMaxWidth()
        .background(color = Color.LightGray, shape = RoundedCornerShape(20))
        .border(
            border = BorderStroke(2.dp, color = Color.LightGray),
            shape = RoundedCornerShape(20)
        ), horizontalArrangement = Arrangement.SpaceBetween,){
        //Name
        Text(
            text = detailItem.name,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterVertically).padding(8.dp)
        )
        // Amount
        Text(
            text = "Amount: ${detailItem.amount} $",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Row (modifier = Modifier.padding(8.dp).align(Alignment.CenterVertically)){
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}