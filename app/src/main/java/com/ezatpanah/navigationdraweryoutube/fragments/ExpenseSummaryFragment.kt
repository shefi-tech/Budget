package com.ezatpanah.navigationdraweryoutube.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
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
import java.util.Locale

class ExpenseSummaryFragment : Fragment() {

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var categorySpinner: Spinner
    private lateinit var selectDateButton: Button
    private lateinit var summaryTextView: TextView

    private var selectedCategory: String = "Food"
    private var selectedMonth: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_summary, container, false)

        categorySpinner = view.findViewById(R.id.categorySpinner)
        selectDateButton = view.findViewById(R.id.selectDateButton)
        summaryTextView = view.findViewById(R.id.summaryTextView)

        expenseViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        val categories = arrayOf("Food", "Transport", "Entertainment")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categorySpinner.adapter = adapter
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Set up the date picker
        selectDateButton.setOnClickListener {
            showDatePicker()
        }

        return view
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, _: Int ->

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                selectedMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time)

                // Fetch and display the summary
                showExpenseSummary(selectedCategory, selectedMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun showExpenseSummary(category: String, month: String) {
        expenseViewModel.getExpenseSummaryByCategoryAndMonth(category, month).observe(viewLifecycleOwner, Observer { summaries ->
            if (summaries.isNullOrEmpty()) {
                summaryTextView.text = "No expenses found for the selected criteria."
            } else {
                val summaryBuilder = StringBuilder()
                for (summary in summaries) {
                    summaryBuilder.append("Month: ${summary.month}, Category: ${summary.category}, Total Amount: ${summary.totalAmount}\n")
                }
                summaryTextView.text = summaryBuilder.toString()
            }
        })
    }
}
