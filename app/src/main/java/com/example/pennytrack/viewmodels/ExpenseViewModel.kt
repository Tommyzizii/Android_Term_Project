package com.example.pennytrack.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.example.pennytrack.PennyTrackApplication
import com.example.pennytrack.dao.ExpenseDatabase
import com.example.pennytrack.data.models.Expense
import com.example.pennytrack.data.models.MonthlyTotal
import com.example.pennytrack.repository.ExpenseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExpenseViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ExpenseRepository

    //Notification
    private val notificationViewModel: NotificationViewModel = NotificationViewModel()

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
//        viewModelScope.launch {
//            repository.getExpensesByDate(_currentDate.value)
//                .collect { expenses ->
//                    _expenses.value = expenses
//                }
//        }

        // Watch for date changes
        viewModelScope.launch {
            _currentDate.collectLatest { date ->
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

        viewModelScope.launch {
            repository.allExpenses.collect { expenses ->
                println("Total expenses in database: ${expenses.size}")
                println("Sample expense dates: ${expenses.take(5).map { it.date }}")
            }
        }
    }

    fun checkExpenseThresholds(monthlyTotal: Float, expectedOutcome: Float, userId: String) {
        Log.d("ExpenseDebug", "reach")
        when {
            monthlyTotal >= expectedOutcome -> {
                notificationViewModel.addNotification(userId, "You have exceeded your expected outcome!")
            }
            monthlyTotal >= expectedOutcome * 2 / 3 -> {
                notificationViewModel.addNotification(userId, "You are close to your expected outcome.Current total expense \$ $monthlyTotal .")
            }
            monthlyTotal >= expectedOutcome / 2 -> {
                notificationViewModel.addNotification(userId, "You are halfway to your expected outcome.")
            }
        }
    }

    // Date handling functions
    fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getCurrentMonth(): String {
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun setDate(date: String) {
        _currentDate.value = date
    }

    fun goToPreviousDay() {
        val currentFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = currentFormat.parse(_currentDate.value) ?: return
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        _currentDate.value = currentFormat.format(calendar.time)
    }


    fun goToNextDay() {
        val currentFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = currentFormat.parse(_currentDate.value) ?: return
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        _currentDate.value = currentFormat.format(calendar.time)
    }

    fun setMonth(monthYear: String) {
        viewModelScope.launch {
            println("Setting month to: $monthYear")
            _currentMonth.value = monthYear

            // Explicitly load data
            repository.getExpensesByMonth(monthYear)
                .collect { expenses ->
                    println("Loaded ${expenses.size} expenses for $monthYear")
                    _monthlyExpenses.value = expenses
                }
        }
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
        Log.d("ExpenseDebug", "Expense added: ${finalExpense.title}")

        // Fetch the current user's ID
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("ExpenseDebug", "User ID: $userId")

        // Fetch the expected outcome from Firestore
        if (userId != null) {
            val expectedOutcome = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .await()
                .getString("expectedOutcome")
                ?.toFloatOrNull() ?: 0f

            // Get the month-year for the added expense (in the format MM/yyyy)
            val monthYear = finalExpense.date.substring(3, 10)  // Extract MM/yyyy from dd/MM/yyyy
            Log.d("ExpenseDebug", "Extracted monthYear: $monthYear")

            // Get the total expense for the specific month
            val monthlyTotal = repository.getTotalExpenseForSpecificMonth(monthYear)
                .map { it ?: 0f }
                .first() // Get the first value emitted by the Flow
            Log.d("ExpenseDebug", "Expected outcome: $expectedOutcome")
            Log.d("ExpenseDebug", "Monthly total for $monthYear: $monthlyTotal")

            // Check thresholds and trigger notifications
            checkExpenseThresholds(monthlyTotal, expectedOutcome, userId)
        }

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

    // Function to get all expenses grouped by day for the current month
    fun getExpensesGroupedByDayForCurrentMonth(): StateFlow<Map<String, List<Expense>>> {
        return _monthlyExpenses.map { expenses ->
            expenses.groupBy { it.date }
                .toSortedMap(compareByDescending { it }) // Most recent first
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap()
        )
    }

    // Function to get formatted month display name from MM/yyyy format
    fun formatMonthYear(monthYear: String): String {
        val dateParts = monthYear.split("/")
        return if (dateParts.size == 2) {
            val month = dateParts[0].toIntOrNull() ?: 1
            val year = dateParts[1].toIntOrNull() ?: 2025

            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.MONTH, month - 1)
            calendar.set(java.util.Calendar.YEAR, year)

            SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
        } else {
            monthYear
        }
    }

    // Function to get daily totals for the current selected month
    fun getDailyTotalsForCurrentMonth(): StateFlow<List<Pair<String, Float>>> {
        return _monthlyExpenses.map { expenses ->
            println("Processing ${expenses.size} expenses for month ${_currentMonth.value}")
            expenses.groupBy { it.date }
                .map { (date, expensesForDay) ->
                    val total = expensesForDay.sumOf { it.amount.toDouble() }.toFloat()
                    println("Date: $date, Total: $total")
                    Pair(date, total)
                }
                .sortedByDescending { (date, _) -> date }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    // Format a date string from "dd/MM/yyyy" to "EEE, MMM d" (e.g., "Mon, Jan 15")
    fun formatDateDisplay(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            if (date != null) {
                outputFormat.format(date)
            } else {
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }

    // Check if a date is today
    fun isToday(dateString: String): Boolean {
        return dateString == getTodayDate()
    }

    // Check if a date is yesterday
    fun isYesterday(dateString: String): Boolean {
        return dateString == getYesterdayDate()
    }

    // Check if a given month is the current month
    fun isCurrentMonth(monthYear: String): Boolean {
        return monthYear == getCurrentMonth()
    }

    // Get total number of expenses for a month
    fun getExpenseCountForMonth(monthYear: String): LiveData<Int> {
        return repository.getExpensesByMonth(monthYear)
            .map { it.size }
            .asLiveData()
    }

    // Get current month with formatted display name
    fun getCurrentMonthFormatted(): StateFlow<Pair<String, String>> {
        return _currentMonth.map { monthYear ->
            Pair(monthYear, formatMonthYear(monthYear))
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            Pair(getCurrentMonth(), formatMonthYear(getCurrentMonth()))
        )
    }
}