package com.ezatpanah.navigationdraweryoutube.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.ezatpanah.navigationdraweryoutube.dao.Expense
import com.ezatpanah.navigationdraweryoutube.repository.ExpenseRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class ExpenseViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var repository: ExpenseRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(ExpenseRepository::class.java)
        viewModel = ExpenseViewModel(mock(Application::class.java))
        viewModel = ExpenseViewModel(Application())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addExpense should call addExpense on repository`() = testScope.runTest {
        val expense = Expense(1, "Test", 100.0, "Food", System.currentTimeMillis())
        viewModel.addExpense(expense)
        verify(repository).addExpense(expense)
    }

    @Test
    fun `updateExpense should call updateExpense on repository`() = testScope.runTest {
        val expense = Expense(1, "Test", 100.0, "Food", System.currentTimeMillis())
        viewModel.updateExpense(expense)
        verify(repository).updateExpense(expense)
    }

    @Test
    fun `deleteExpense should call deleteExpense on repository`() = testScope.runTest {
        val expense = Expense(1, "Test", 100.0, "Food", System.currentTimeMillis())
        viewModel.deleteExpense(expense)
        verify(repository).deleteExpense(expense)
    }

    @Test
    fun `getMonthlyExpenses should return data from repository`() {
        val month = "2024-09"
        val liveData = MutableLiveData<Double>()
        `when`(repository.getMonthlyExpenses(month)).thenReturn(liveData)
        val result = viewModel.getMonthlyExpenses(month)
        assertEquals(liveData, result)
    }
    // Add similar tests for other ViewModel methods
}
