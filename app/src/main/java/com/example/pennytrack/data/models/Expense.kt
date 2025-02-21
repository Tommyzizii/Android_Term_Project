package com.example.pennytrack.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey (autoGenerate = true) val id: Int,
    val title: String,
    val amount: Float,
    val description: String,
    val date: String,
    val time: String
)