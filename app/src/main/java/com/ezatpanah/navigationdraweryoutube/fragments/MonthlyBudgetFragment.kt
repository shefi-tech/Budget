package com.ezatpanah.navigationdraweryoutube.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ezatpanah.navigationdraweryoutube.R
import com.ezatpanah.navigationdraweryoutube.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MonthlyBudgetFragment : Fragment() {

    private lateinit var expenseViewModel: ExpenseViewModel
    private var monthlyBudget: Double = 10000.0
    private lateinit var selectedMonthTextView: TextView
    private lateinit var budgetTextView: TextView
    private lateinit var remainingBudgetTextView: TextView

    private lateinit var categorySpinner: Spinner
    private lateinit var selectedMonthCategoryTextView: TextView
    private lateinit var budgetCategoryTextView: TextView
    private lateinit var remainingBudgetCategoryTextView: TextView

    private val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    private var selectedYearMonth = dateFormat.format(Date())
    private var selectedYearMonthCategory = dateFormat.format(Date())
    private var selectedCategory: String = "Food"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_monthly_budget, container, false)

        selectedMonthTextView = view.findViewById(R.id.selectedMonthTextView)
        budgetTextView = view.findViewById(R.id.budgetTextView)
        remainingBudgetTextView = view.findViewById(R.id.remainingBudgetTextView)

        selectedMonthTextView.text = "Selected Month: $selectedYearMonth"

        selectedMonthCategoryTextView = view.findViewById(R.id.selectedMonthCategoryTextView)
        budgetCategoryTextView = view.findViewById(R.id.budgetCategoryTextView)
        remainingBudgetCategoryTextView = view.findViewById(R.id.remainingBudgetCategoryTextView)
        categorySpinner = view.findViewById(R.id.categorySpinner)

        selectedMonthCategoryTextView.text = "Selected Month: $selectedYearMonthCategory"

        expenseViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        val categories = arrayOf("Food", "Transport", "Entertainment")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categorySpinner.adapter = adapter
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categories[position]
                showExpensesForCategoryAndMonth(selectedYearMonth, selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        showExpensesForMonth(selectedYearMonth)
        showExpensesForCategoryAndMonth(selectedYearMonth, selectedCategory)
        view.findViewById<View>(R.id.selectMonthButton).setOnClickListener {
            showMonthYearPicker()
        }
        view.findViewById<View>(R.id.selectMonthCategoryButton).setOnClickListener {
            showMonthYearCategoryPicker()
        }
        return view
    }
    private fun showMonthYearCategoryPicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, _: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                selectedYearMonthCategory = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time)
                selectedMonthCategoryTextView.text = "Selected Month: $selectedYearMonthCategory"

                showExpensesForCategoryAndMonth(selectedYearMonthCategory, selectedCategory)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, 1900)
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
    private fun showMonthYearPicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, _: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                selectedYearMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time)
                selectedMonthTextView.text = "Selected Month: $selectedYearMonth"
                showExpensesForMonth(selectedYearMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, 1900)
            set(Calendar.MONTH, 0)
            set(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showExpensesForMonth(month: String) {
        expenseViewModel.getMonthlyExpenses(month).observe(viewLifecycleOwner, Observer { totalExpenses ->
            if (totalExpenses == null || totalExpenses.isNaN()) {
                budgetTextView.text = "Total Expenses: 0.00"
                remainingBudgetTextView.text = "Remaining Budget: $monthlyBudget"
            } else {
                val remainingBudget = monthlyBudget - totalExpenses
                budgetTextView.text = "Total Expenses: $totalExpenses"
                remainingBudgetTextView.text = "Remaining Budget: $remainingBudget"
            }
        })
    }

    private fun showExpensesForCategoryAndMonth(month: String, category: String) {
        expenseViewModel.getMonthlyExpensesByCategory(month, category).observe(viewLifecycleOwner, Observer { totalExpenses ->
            if (totalExpenses == null || totalExpenses.isNaN()) {
                budgetCategoryTextView.text = "Total Category Expenses: 0.00"
                remainingBudgetCategoryTextView.text = "Remaining Category Budget: $monthlyBudget"
            } else {
                val remainingBudget = monthlyBudget - totalExpenses
                budgetCategoryTextView.text = "Total Category Expenses: $totalExpenses"
                remainingBudgetCategoryTextView.text = "Remaining Category Budget: $remainingBudget"
            }
        })
    }
}

