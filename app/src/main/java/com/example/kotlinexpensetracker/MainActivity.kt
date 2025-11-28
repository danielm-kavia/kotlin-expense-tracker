package com.example.kotlinexpensetracker

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kotlinexpensetracker.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * PUBLIC_INTERFACE
 * MainActivity
 *
 * A minimal launcher activity that displays a simple text.
 * This satisfies CI build requirements for a runnable application.
 * Additionally wires a ViewModel instance and logs state changes to confirm
 * StateFlow is properly working (useful in early CI without full UI).
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * This is the entry point of the activity lifecycle.
         * It inflates a simple layout and sets the title.
         */
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Kotlin Expense Tracker"
        binding.helloText.text = "Hello from Kotlin Expense Tracker!"

        // Observe ViewModel state as a sanity check; log to Logcat
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.monthLabel
            .onEach { Log.d("MainActivity", "Month label: $it") }
            .launchIn(lifecycleScope)

        viewModel.totalIncome
            .onEach { Log.d("MainActivity", "Income: $it") }
            .launchIn(lifecycleScope)

        viewModel.totalExpenses
            .onEach { Log.d("MainActivity", "Expenses: $it") }
            .launchIn(lifecycleScope)

        viewModel.balance
            .onEach { Log.d("MainActivity", "Balance: $it") }
            .launchIn(lifecycleScope)
    }
}
