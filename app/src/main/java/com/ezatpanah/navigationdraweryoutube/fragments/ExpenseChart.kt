package com.ezatpanah.navigationdraweryoutube.fragments

import android.app.DatePickerDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ezatpanah.navigationdraweryoutube.R
import com.ezatpanah.navigationdraweryoutube.viewmodel.ExpenseViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ExpenseChart : Fragment() {

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var selectedMonthCategoryTextView: TextView
    private lateinit var budgetCategoryTextView: TextView
    private lateinit var remainingBudgetCategoryTextView: TextView
    private lateinit var expenseLineChart: LineChart
    private lateinit var expenseBarChart: BarChart

    private val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    private var selectedYearMonth = dateFormat.format(Date())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_chart, container, false)

        selectedMonthCategoryTextView = view.findViewById(R.id.selectedMonthCategoryTextView)
        budgetCategoryTextView = view.findViewById(R.id.budgetCategoryTextView)
        remainingBudgetCategoryTextView = view.findViewById(R.id.remainingBudgetCategoryTextView)
        expenseLineChart = view.findViewById(R.id.expenseLineChart)
        expenseBarChart = view.findViewById(R.id.expenseBarChart)
        expenseViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        view.findViewById<View>(R.id.selectMonthCategoryButton).setOnClickListener {
            showMonthYearPicker()
        }
        updateExpenseSummary()

        return view
    }

    private fun showMonthYearPicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, month: Int, _: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                selectedYearMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time)
                selectedMonthCategoryTextView.text = "Selected Month: $selectedYearMonth"
                updateExpenseSummary()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis // Start of the current month

        calendar.set(Calendar.YEAR, currentYear)
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis // End of the current month

        datePickerDialog.datePicker.calendarViewShown = false
        datePickerDialog.datePicker.findViewById<View>(
            resources.getIdentifier("day", "id", "android")
        )?.visibility = View.GONE

        datePickerDialog.show()
    }


    private fun updateExpenseSummary() {
        expenseViewModel.getMonthlyChartByCategoryAndMonth(selectedYearMonth)
            .observe(viewLifecycleOwner, Observer { expenseList ->
                if (expenseList.isNullOrEmpty()) {
                    budgetCategoryTextView.text = "No expenses found for this month."
                    remainingBudgetCategoryTextView.text = "Remaining Category Budget: 10000.0"
                    updateLineChart(emptyList())
                    updateBarChart(emptyList())
                } else {
                    val totalExpenses = expenseList.sum()
                    val remainingBudget = 10000.0 - totalExpenses

                    budgetCategoryTextView.text = "Total Expenses: $totalExpenses"
                    remainingBudgetCategoryTextView.text = "Remaining Budget: $remainingBudget"

                    val days = (1..expenseList.size).toList()
                    val dayExpensePairs = days.zip(expenseList)

                    updateLineChart(dayExpensePairs)
                    updateBarChart(dayExpensePairs)
                }
            })
    }

    private fun updateLineChart(dayExpensePairs: List<Pair<Int, Double>>) {

        val entries = dayExpensePairs.map { (day, amount) ->
            Entry(day.toFloat(), amount.toFloat())
        }

        val dataSet = LineDataSet(entries, "Expenses")
        dataSet.color = Color.BLUE
        dataSet.lineWidth = 2f
        dataSet.setDrawFilled(true)

        dataSet.fillDrawable = createGradient()

        val data = LineData(dataSet)
        expenseLineChart.data = data
        expenseLineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        expenseLineChart.axisLeft.granularity = 1f
        expenseLineChart.invalidate()
    }

    private fun createGradient(): Drawable {
        val gradient = LinearGradient(
            0f, 0f, 0f, expenseLineChart.height.toFloat(),
            Color.BLUE, Color.TRANSPARENT, Shader.TileMode.CLAMP
        )
        return object : Drawable() {
            override fun draw(canvas: Canvas) {
                val paint = Paint()
                paint.shader = gradient
                canvas.drawRect(bounds, paint)
            }

            override fun setAlpha(alpha: Int) {}
            override fun setColorFilter(colorFilter: ColorFilter?) {}
            override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
        }
    }

    private fun updateBarChart(dayExpensePairs: List<Pair<Int, Double>>) {
        val entries = dayExpensePairs.map { (day, amount) ->
            BarEntry(day.toFloat(), amount.toFloat())
        }

        val dataSet = BarDataSet(entries, "Daily Expenses")
        dataSet.color = Color.GREEN

        val data = BarData(dataSet)
        expenseBarChart.data = data
        expenseBarChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        expenseBarChart.xAxis.valueFormatter = object : IAxisValueFormatter {
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                return value.toInt().toString()
            }
        }
        expenseBarChart.axisLeft.granularity = 1f
        expenseBarChart.legend.isEnabled = true
        expenseBarChart.invalidate()
    }

}
