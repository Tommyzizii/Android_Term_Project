package com.example.pennytrack.repository

import com.example.pennytrack.dao.ExpenseDao
import com.example.pennytrack.data.models.Expense
import com.example.pennytrack.data.models.MonthlyTotal
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao
){
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()

    fun getExpensesByDate(date: String): Flow<List<Expense>> {
        return expenseDao.getExpensesByDate(date)
    }

    fun getExpensesByMonth(monthYear: String): Flow<List<Expense>> {
        return expenseDao.getExpensesByMonth(monthYear)
    }

    fun getMonthlyTotals(): Flow<List<MonthlyTotal>> {
        return expenseDao.getMonthlyTotals()
    }

    fun getTotalExpenseForDay(date: String): Flow<Float?> {
        return expenseDao.getTotalExpenseForDay(date)
    }

    fun getTotalExpenseForMonth(monthYear: String): Flow<Float?> {
        return expenseDao.getTotalExpenseForMonth(monthYear)
    }

    suspend fun insert(expense: Expense): Long {
        return expenseDao.insertExpense(expense)
    }

    suspend fun update(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    suspend fun delete(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    // Method to fetch total expense for a specific month
    fun getTotalExpenseForSpecificMonth(monthYear: String): Flow<Float?> {
        return expenseDao.getTotalExpenseForSpecificMonth(monthYear)
    }

}