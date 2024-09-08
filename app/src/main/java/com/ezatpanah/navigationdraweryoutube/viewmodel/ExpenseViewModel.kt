package com.ezatpanah.navigationdraweryoutube.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ezatpanah.navigationdraweryoutube.dao.Expense
import com.ezatpanah.navigationdraweryoutube.dao.ExpenseDao
import com.ezatpanah.navigationdraweryoutube.dao.ExpenseDatabase
import com.ezatpanah.navigationdraweryoutube.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExpenseRepository
    val allExpenses: LiveData<List<Expense>>

    init {
        val expenseDao = ExpenseDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(expenseDao)
        allExpenses = repository.allExpenses
    }

    fun addExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.addExpense(expense)
    }

    fun updateExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateExpense(expense)
    }

    fun deleteExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteExpense(expense)
    }
    fun getMonthlyExpenses(month: String): LiveData<Double> {
        return repository.getMonthlyExpenses(month)
    }

    fun getExpenseSummaryByCategoryAndMonth(
        category: String,
        month: String
    ): LiveData<List<ExpenseDao.ExpenseSummary>> {
        return repository.getExpenseSummaryByCategoryAndMonth(category, month)
    }

    fun getMonthlyExpensesByCategory(category: String, month: String): LiveData<Double> {
        return repository.getMonthlyExpensesByCategoryAndMonth(category, month)
    }

    fun getMonthlyChartByCategoryAndMonth(month: String): LiveData<List<Double>> {
        return repository.getMonthlyChartByCategoryAndMonth(month)
    }

}
