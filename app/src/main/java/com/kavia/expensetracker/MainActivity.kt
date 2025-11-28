package com.kavia.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.kavia.expensetracker.ui.ExpenseTrackerScreen
import com.kavia.expensetracker.ui.theme.AppTheme

/**
 * PUBLIC_INTERFACE
 * MainActivity is the single entry point that hosts the entire expense tracker screen in Compose.
 *
 * This activity sets up the Material 3 theme and displays the ExpenseTrackerScreen which implements:
 * - Month navigation
 * - Summary totals (income, expense, balance)
 * - Add/Edit/Delete transactions
 * - Transaction list filtered by the selected month
 *
 * No persistence is used; all state is in-memory.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ExpenseTrackerScreen()
                }
            }
        }
    }
}
