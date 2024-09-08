package com.ezatpanah.navigationdraweryoutube.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezatpanah.navigationdraweryoutube.R
import com.ezatpanah.navigationdraweryoutube.dao.Expense
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseAdapter(
    private val onDeleteClick: (Expense) -> Unit,
    private val onUpdateClick: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)
        holder.bind(expense)
        holder.deleteButton.setOnClickListener {
            onDeleteClick(expense)
        }
        holder.updateButton.setOnClickListener {
            onUpdateClick(expense)
        }
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val expenseTitle: TextView = itemView.findViewById(R.id.expenseTitle)
        private val expenseAmount: TextView = itemView.findViewById(R.id.expenseAmount)
        val expenseCategory: TextView = itemView.findViewById(R.id.expenseCategory)
        val expenseDate: TextView = itemView.findViewById(R.id.expenseDate)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val updateButton: Button = itemView.findViewById(R.id.updateButton)

        fun bind(expense: Expense) {
            val expenseDates = Date(expense.date)
            val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val formattedDate = dateFormat.format(expenseDates)
            expenseTitle.text =  java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(expense.date))
            expenseAmount.text = expense.amount.toString()
            expenseCategory.text = expense.category
            expenseDate.text = formattedDate
        }
    }

    class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}
