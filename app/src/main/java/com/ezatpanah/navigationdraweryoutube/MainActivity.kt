package com.ezatpanah.navigationdraweryoutube

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.ezatpanah.navigationdraweryoutube.databinding.ActivityMainBinding
import com.ezatpanah.navigationdraweryoutube.fragments.ExpenseChart
import com.ezatpanah.navigationdraweryoutube.fragments.ExpenseManagementFragment
import com.ezatpanah.navigationdraweryoutube.fragments.ExpenseSummaryFragment
import com.ezatpanah.navigationdraweryoutube.fragments.MonthlyBudgetFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedTheme() // Apply saved theme before super.onCreate()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_light_mode -> {
                    saveThemePreference(false) // Save light mode preference
                    recreate() // Recreate the activity to apply the new theme
                    true
                }
                R.id.action_dark_mode -> {
                    saveThemePreference(true) // Save dark mode preference
                    recreate() // Recreate the activity to apply the new theme
                    true
                }
                else -> false
            }
        }

        // Set up the navigation drawer
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set default fragment
        if (savedInstanceState == null) {
            replaceFragment(ExpenseManagementFragment()) // Load default fragment
        }

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.firstItem -> {
                    replaceFragment(ExpenseManagementFragment())
                }
                R.id.secondtItem -> {
                    replaceFragment(MonthlyBudgetFragment())
                }
                R.id.thirdItem -> {
                    replaceFragment(ExpenseSummaryFragment())
                }
                R.id.forthItem -> {
                    replaceFragment(ExpenseChart())
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Apply saved theme before onCreate
    private fun applySavedTheme() {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        if (isDarkMode) {
            setTheme(R.style.Theme_Dark)
        } else {
            setTheme(R.style.Theme_Light)
        }
    }

    // Save theme preference
    private fun saveThemePreference(isDarkMode: Boolean) {
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPreferences.edit()
        editor.putBoolean("dark_mode", isDarkMode)
        editor.apply()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return when (item.itemId) {
            R.id.action_dark_mode -> {
                val nightMode = AppCompatDelegate.getDefaultNightMode()
                if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.theme_menu, menu)
        return true
    }
}
