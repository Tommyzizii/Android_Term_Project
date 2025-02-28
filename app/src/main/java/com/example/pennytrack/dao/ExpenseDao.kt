package com.example.pennytrack.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pennytrack.data.models.Expense
import com.example.pennytrack.data.models.MonthlyTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY date DESC, time DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date = :date ORDER BY time DESC")
    fun getExpensesByDate(date: String): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT SUM(amount) FROM expenses WHERE date = :date")
    fun getTotalExpenseForDay(date: String): Flow<Float?>

    @Query("SELECT SUM(amount) FROM expenses WHERE date LIKE :monthYear || '%'")
    fun getTotalExpenseForMonth(monthYear: String): Flow<Float?>

    @Query("""
        SELECT 
            substr(date, 4, 7) AS monthYear, 
            SUM(amount) as total,
            COUNT(*) as count
        FROM expenses 
        GROUP BY substr(date, 4, 7)
        ORDER BY date DESC
    """)
    fun getMonthlyTotals(): Flow<List<MonthlyTotal>>

    @Query("""
        SELECT * FROM expenses 
        WHERE substr(date, 4, 7) = :monthYear 
        ORDER BY date DESC, time DESC
    """)
    fun getExpensesByMonth(monthYear: String): Flow<List<Expense>>

    @Query("""
        SELECT SUM(amount)
        FROM expenses     
        WHERE substr(date, 4, 7) = :monthYear
    """)
    fun getTotalExpenseForSpecificMonth(monthYear: String): Flow<Float?>



}
