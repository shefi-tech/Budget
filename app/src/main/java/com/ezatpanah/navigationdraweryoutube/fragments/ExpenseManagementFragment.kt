package com.ezatpanah.navigationdraweryoutube.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezatpanah.navigationdraweryoutube.R
import com.ezatpanah.navigationdraweryoutube.adapter.ExpenseAdapter
import com.ezatpanah.navigationdraweryoutube.dao.Expense
import com.ezatpanah.navigationdraweryoutube.viewmodel.ExpenseViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseManagementFragment : Fragment() {
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculate_budget, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        val dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.divider)
        dividerItemDecoration.setDrawable(dividerDrawable!!)
        recyclerView.addItemDecoration(dividerItemDecoration)
        adapter = ExpenseAdapter(
            onDeleteClick = { expense -> onDeleteExpense(expense) },
            onUpdateClick = { expense -> showUpdateExpenseDialog(expense) }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        expenseViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        expenseViewModel.allExpenses.observe(viewLifecycleOwner, Observer { expenses ->
            expenses?.let { adapter.submitList(it) }
        })

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            showAddExpenseDialog()
        }

        return view
    }

    private fun showAddExpenseDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_expense, null)

        val categorySpinner: Spinner = dialogView.findViewById(R.id.categorySpinner)
        val amountEditText: EditText = dialogView.findViewById(R.id.expenseAmountEditText)
        val submitButton: Button = dialogView.findViewById(R.id.submitExpenseButton)

        val categories = arrayOf("Food", "Transport", "Entertainment")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categorySpinner.adapter = adapter

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        submitButton.setOnClickListener {
            val selectedCategory = categorySpinner.selectedItem.toString()
            val amountText = amountEditText.text.toString()

            if (amountText.isNotEmpty()) {
                val amount = amountText.toDouble()
                saveExpense(selectedCategory, amount)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun saveExpense(category: String, amount: Double) {
        val currentTimeMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val title = dateFormat.format(Date(currentTimeMillis))

        val newExpense = Expense(
            title = title,
            amount = amount,
            category = category,
            date = currentTimeMillis
        )
        expenseViewModel.addExpense(newExpense)
    }

    private fun onDeleteExpense(expense: Expense) {
        expenseViewModel.deleteExpense(expense)
    }

    private fun showUpdateExpenseDialog(expense: Expense) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_expense, null)

        val categorySpinner: Spinner = dialogView.findViewById(R.id.categorySpinner)
        val amountEditText: EditText = dialogView.findViewById(R.id.expenseAmountEditText)
        val submitButton: Button = dialogView.findViewById(R.id.submitExpenseButton)

        val categories = arrayOf("Food", "Transport", "Entertainment")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categorySpinner.adapter = adapter
        amountEditText.setText(expense.amount.toString())
        val categoryPosition = categories.indexOf(expense.category)
        categorySpinner.setSelection(categoryPosition)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        submitButton.setOnClickListener {
            val updatedCategory = categorySpinner.selectedItem.toString()
            val updatedAmountText = amountEditText.text.toString()

            if (updatedAmountText.isNotEmpty()) {
                val updatedAmount = updatedAmountText.toDouble()
                val updatedExpense = expense.copy(category = updatedCategory, amount = updatedAmount)

                expenseViewModel.updateExpense(updatedExpense)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
