package com.example.pennytrack.data.models

data class Expense(
    val id: Int,
    val title: String,
    val amount: Float,
    val description: String,
    val date: String,
    val time: String
)