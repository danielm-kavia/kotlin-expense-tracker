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
 * A minimal launcher activity that displays a simple text.
 * Wires a ViewModel instance and observes StateFlow to update the UI,
 * avoiding placeholder logs.
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
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.monthLabel.collect { label ->
                        binding.helloText.text = "Mês: $label"
                    }
                }
                launch {
                    viewModel.balance.collect { balance ->
                        val text = "Saldo: ${CurrencyUtils.formatBRL(balance)}"
                        // Append under month for now in the single TextView
                        binding.helloText.text = "${binding.helloText.text}\n$text"
                    }
                }
                launch {
                    viewModel.totalIncome.collect { income ->
                        val text = "Receitas: ${CurrencyUtils.formatBRL(income)}"
                        binding.helloText.text = "${binding.helloText.text}\n$text"
                    }
                }
                launch {
                    viewModel.totalExpenses.collect { expenses ->
                        val text = "Despesas: ${CurrencyUtils.formatBRL(expenses)}"
                        binding.helloText.text = "${binding.helloText.text}\n$text"
                    }
                }
            }
        }
    }
}
