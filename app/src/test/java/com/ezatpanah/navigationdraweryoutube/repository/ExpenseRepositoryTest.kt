package com.ezatpanah.navigationdraweryoutube.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ezatpanah.navigationdraweryoutube.dao.Expense
import com.ezatpanah.navigationdraweryoutube.dao.ExpenseDao
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ExpenseRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var expenseDao: ExpenseDao
    private lateinit var repository: ExpenseRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        expenseDao = Mockito.mock(ExpenseDao::class.java)
        repository = ExpenseRepository(expenseDao)
    }

    @Test
    fun `addExpense should call addExpense on expenseDao`() = runBlocking {
        val expense = Expense(1, "Test", 100.0, "Food", System.currentTimeMillis())
        repository.addExpense(expense)
        Mockito.verify(expenseDao).addExpense(expense)
    }

    @Test
    fun `updateExpense should call updateExpense on expenseDao`() = runBlocking {
        val expense = Expense(1, "Test", 100.0, "Food", System.currentTimeMillis())
        repository.updateExpense(expense)
        Mockito.verify(expenseDao).updateExpense(expense)
    }

    @Test
    fun `deleteExpense should call deleteExpense on expenseDao`() = runBlocking {
        val expense = Expense(1, "Test", 100.0, "Food", System.currentTimeMillis())
        repository.deleteExpense(expense)
        Mockito.verify(expenseDao).deleteExpense(expense)
    }

    @Test
    fun `getMonthlyExpenses should return data from expenseDao`() {
        val month = "2024-09"
        val liveData = MutableLiveData<Double>()
        Mockito.`when`(expenseDao.getMonthlyExpenses(month)).thenReturn(liveData)
        val result: LiveData<Double> = repository.getMonthlyExpenses(month)
        assertEquals(liveData, result)
    }

    @Test
    fun `getExpenseSummaryByCategoryAndMonth should return data from expenseDao`() {
        val category = "Food"
        val month = "2024-09"
        val liveData = MutableLiveData<List<ExpenseDao.ExpenseSummary>>()
        Mockito.`when`(expenseDao.getExpenseSummaryByCategoryAndMonth(category, month)).thenReturn(liveData)
        val result: LiveData<List<ExpenseDao.ExpenseSummary>> = repository.getExpenseSummaryByCategoryAndMonth(category, month)
        assertEquals(liveData, result)
    }

    @Test
    fun `getMonthlyExpensesByCategory should return data from expenseDao`() {
        val category = "Food"
        val month = "2024-09"
        val liveData = MutableLiveData<Double>()
        Mockito.`when`(expenseDao.getMonthlyExpensesByCategoryAndMonth(month, category)).thenReturn(liveData)
        val result: LiveData<Double> = repository.getMonthlyExpensesByCategoryAndMonth(month, category)
        assertEquals(liveData, result)
    }

    @Test
    fun `getMonthlyChartByCategoryAndMonth should return data from expenseDao`() {
        val month = "2024-09"
        val liveData = MutableLiveData<List<Double>>()
        Mockito.`when`(expenseDao.getMonthlyChartsByCategoryAndMonth(month)).thenReturn(liveData)
        val result: LiveData<List<Double>> = repository.getMonthlyChartByCategoryAndMonth(month)
        assertEquals(liveData, result)
    }
}
