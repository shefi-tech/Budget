package com.ezatpanah.navigationdraweryoutube.repository

import androidx.lifecycle.LiveData
import com.ezatpanah.navigationdraweryoutube.dao.Expense
import com.ezatpanah.navigationdraweryoutube.dao.ExpenseDao

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()

    suspend fun addExpense(expense: Expense) {
        expenseDao.addExpense(expense)
    }

    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }
    fun getMonthlyExpenses(month: String): LiveData<Double> {
        return expenseDao.getMonthlyExpenses(month)
    }
    fun getExpenseSummaryByCategoryAndMonth(
        category: String,
        month: String
    ): LiveData<List<ExpenseDao.ExpenseSummary>> {
        return expenseDao.getExpenseSummaryByCategoryAndMonth(category, month)
    }

    fun getMonthlyExpensesByCategoryAndMonth(month: String, category: String): LiveData<Double> {
        return expenseDao.getMonthlyExpensesByCategoryAndMonth(month, category)
    }

    fun getMonthlyChartByCategoryAndMonth(month: String): LiveData<List<Double>> {
        return expenseDao.getMonthlyChartsByCategoryAndMonth(month)
    }
}
