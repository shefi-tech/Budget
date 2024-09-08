package com.ezatpanah.navigationdraweryoutube.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExpenseDao {
    @Insert
    suspend fun addExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): LiveData<List<Expense>>



    @Query("""
        SELECT strftime('%Y-%m', date/1000, 'unixepoch') as month,
               category,
               IFNULL(SUM(amount), 0) as totalAmount
        FROM expenses
        WHERE category = :category
        AND strftime('%Y-%m', date/1000, 'unixepoch') = :month
        GROUP BY month, category
        ORDER BY month DESC
    """)
    fun getExpenseSummaryByCategoryAndMonth(
        category: String,
        month: String
    ): LiveData<List<ExpenseSummary>>
    data class ExpenseSummary(
        val month: String,
        val category: String,
        val totalAmount: Double
    )


    @Query("SELECT IFNULL(SUM(amount), 0) FROM expenses WHERE strftime('%Y-%m', date/1000, 'unixepoch') = :month")
    fun getMonthlyExpenses(month: String): LiveData<Double>

    @Query("""
        SELECT IFNULL(SUM(amount), 0) 
        FROM expenses 
        WHERE category = :category 
        AND strftime('%Y-%m', datetime(date / 1000, 'unixepoch')) = :month
    """)
    fun getMonthlyExpensesByCategoryAndMonth(category: String, month: String): LiveData<Double>


    @Query("""
    SELECT amount 
    FROM expenses 
    WHERE strftime('%Y-%m', datetime(date / 1000, 'unixepoch')) = :month
""")
    fun getMonthlyChartsByCategoryAndMonth(month: String): LiveData<List<Double>>


}
