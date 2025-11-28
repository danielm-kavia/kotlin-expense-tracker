package com.example.kotlinexpensetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinexpensetracker.databinding.ActivityMainBinding

/**
 * PUBLIC_INTERFACE
 * MainActivity
 *
 * A minimal launcher activity that displays a simple text.
 * This satisfies CI build requirements for a runnable application.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
    }
}
