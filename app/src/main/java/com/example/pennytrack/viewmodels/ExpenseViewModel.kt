package com.example.pennytrack.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.pennytrack.data.models.Expense

class ExpenseViewModel : ViewModel() {
    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> get() = _expenses

    fun addExpense(expense: Expense) {
        _expenses.add(expense)
    }

    fun removeExpense(expense: Expense) {
        _expenses.remove(expense) // This will trigger recomposition
    }

    fun updateExpense(updatedExpense: Expense) {
        val updatedList = _expenses.map {
            if (it == updatedExpense) updatedExpense else it
        }
        _expenses.clear() // Clear the list
        _expenses.addAll(updatedList) // Add the updated list to trigger recomposition
    }
}
