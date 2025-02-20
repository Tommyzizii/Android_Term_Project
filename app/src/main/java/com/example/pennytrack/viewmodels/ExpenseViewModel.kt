package com.example.pennytrack.viewmodels

import androidx.lifecycle.ViewModel
import com.example.pennytrack.data.models.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExpenseViewModel : ViewModel() {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    fun addExpense(expense: Expense) {
        _expenses.value += expense
    }

    fun removeExpense(expense: Expense) {
        _expenses.value -= expense
    }

    fun updateExpense(updatedExpense: Expense) {
//        val updatedList = _expenses.map {
//            if (it == updatedExpense) updatedExpense else it
//        }
//        _expenses.clear() // Clear the list
//        _expenses.addAll(updatedList) // Add the updated list to trigger recomposition
//    }
        _expenses.value = _expenses.value.map {
            if (it.id == updatedExpense.id) updatedExpense else it
        }
    }
}
