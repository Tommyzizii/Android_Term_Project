package com.example.pennytrack.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.example.pennytrack.PennyTrackApplication
import com.example.pennytrack.dao.ExpenseDatabase
import com.example.pennytrack.dao.MonthlyTotal
import com.example.pennytrack.data.models.Expense
import com.example.pennytrack.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ExpenseRepository

    // Current date tracking
    private val _currentDate = MutableStateFlow(getTodayDate())
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()

    // Current month tracking for history feature
    private val _currentMonth = MutableStateFlow(getCurrentMonth())
    val currentMonth: StateFlow<String> = _currentMonth.asStateFlow()

    // All expenses
    val allExpenses: LiveData<List<Expense>>

    // Today's expenses as StateFlow for better Compose integration
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    // Monthly expenses for history feature
    private val _monthlyExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val monthlyExpenses: StateFlow<List<Expense>> = _monthlyExpenses.asStateFlow()

    init {
        val expenseDao = ExpenseDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(expenseDao)
        allExpenses = repository.allExpenses.asLiveData()

        // Initialize with today's expenses
        viewModelScope.launch {
            repository.getExpensesByDate(_currentDate.value)
                .collect { expenses ->
                    _expenses.value = expenses
                }
        }

        // Watch for date changes
        viewModelScope.launch {
            _currentDate.collect { date ->
                repository.getExpensesByDate(date)
                    .collect { expenses ->
                        _expenses.value = expenses
                    }
            }
        }

        // Watch for month changes for history feature
        viewModelScope.launch {
            _currentMonth.collect { month ->
                repository.getExpensesByMonth(month)
                    .collect { expenses ->
                        _monthlyExpenses.value = expenses
                    }
            }
        }
    }

    // Date handling functions
    fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun getCurrentMonth(): String {
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun setDate(date: String) {
        _currentDate.value = date
    }

    fun setMonth(monthYear: String) {
        _currentMonth.value = monthYear
    }

    fun refreshTodayExpenses() {
        setDate(getTodayDate())
    }

    // Month navigation for history
    fun goToPreviousMonth() {
        val currentFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        val date = currentFormat.parse(_currentMonth.value) ?: return
        val calendar = java.util.Calendar.getInstance()
        calendar.time = date
        calendar.add(java.util.Calendar.MONTH, -1)
        _currentMonth.value = currentFormat.format(calendar.time)
    }

    fun goToNextMonth() {
        val currentFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        val date = currentFormat.parse(_currentMonth.value) ?: return
        val calendar = java.util.Calendar.getInstance()
        calendar.time = date
        calendar.add(java.util.Calendar.MONTH, 1)
        _currentMonth.value = currentFormat.format(calendar.time)
    }

    // Database operations
    fun addExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        // Ensure the expense has today's date if not specified
        val finalExpense = if (expense.date.isEmpty()) {
            expense.copy(date = getTodayDate())
        } else {
            expense
        }
        repository.insert(finalExpense)
    }

    fun updateExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(expense)
    }

    fun removeExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(expense)
    }

    // Analytics queries
    fun getMonthlyTotals(): LiveData<List<MonthlyTotal>> {
        return repository.getMonthlyTotals().asLiveData()
    }

    fun getTotalExpenseForDay(date: String): LiveData<Float> {
        return repository.getTotalExpenseForDay(date)
            .map { it ?: 0f }
            .asLiveData()
    }

    fun getTotalExpenseForMonth(monthYear: String): LiveData<Float> {
        return repository.getTotalExpenseForMonth(monthYear)
            .map { it ?: 0f }
            .asLiveData()
    }
}