package com.example.kotlinexpensetracker

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.kotlinexpensetracker.databinding.ActivityMainBinding
import com.example.kotlinexpensetracker.utils.CurrencyUtils
import kotlinx.coroutines.launch

/**
 * PUBLIC_INTERFACE
 * MainActivity
 *
 * Minimal launcher activity that wires a ViewModel and observes StateFlows
 * to render month navigation and summary values.
 *
 * Entry point that demonstrates:
 * - Prev/Next month navigation
 * - Reactive month label
 * - Income, Expenses, and Balance summary (BRL formatted)
 * - A placeholder count of filtered transactions for the current month
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Entry point of the activity lifecycle.
         * Inflates layout and hooks button listeners and observers.
         */
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Kotlin Expense Tracker"

        setupClicks()
        setupObservers()
    }

    private fun setupClicks() {
        binding.btnPrev.setOnClickListener {
            viewModel.goToPreviousMonth()
        }
        binding.btnNext.setOnClickListener {
            viewModel.goToNextMonth()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.monthLabel.collect { label ->
                        binding.tvMonth.text = label
                    }
                }
                launch {
                    viewModel.totalIncome.collect { income ->
                        binding.tvIncome.text = CurrencyUtils.formatBRL(income)
                    }
                }
                launch {
                    viewModel.totalExpenses.collect { expenses ->
                        binding.tvExpenses.text = CurrencyUtils.formatBRL(expenses)
                    }
                }
                launch {
                    viewModel.balance.collect { balance ->
                        binding.tvBalance.text = CurrencyUtils.formatBRL(balance)
                    }
                }
                // Placeholder wiring proving filteredTransactions StateFlow updates.
                launch {
                    viewModel.filteredTransactions.collect { list ->
                        binding.tvTransactionsCount.text = "Itens do mês: ${list.size}"
                    }
                }
            }
        }
    }
}
