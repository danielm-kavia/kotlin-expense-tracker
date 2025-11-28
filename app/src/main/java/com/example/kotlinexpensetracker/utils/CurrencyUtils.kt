package com.example.kotlinexpensetracker.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * PUBLIC_INTERFACE
 * Utilities for formatting currency values in BRL, equivalent to the React formatPrice.ts.
 *
 * Uses pt-BR locale with BRL currency. Handles nulls and scales BigDecimal safely.
 */
object CurrencyUtils {

    private val brLocale: Locale = Locale("pt", "BR")
    private val brlCurrency: Currency = Currency.getInstance("BRL")

    // NumberFormat is not thread-safe; we create a fresh instance per call for safety.
    private fun newFormatter(): NumberFormat =
        NumberFormat.getCurrencyInstance(brLocale).apply { currency = brlCurrency }

    /**
     * PUBLIC_INTERFACE
     * Formats a BigDecimal amount to "R$ 1.234,56" style string in pt-BR.
     *
     * @param amount BigDecimal amount (can be null).
     * @return Formatted currency string; returns "R$ 0,00" for null.
     */
    fun formatBRL(amount: BigDecimal?): String {
        val formatter = newFormatter()
        return formatter.format(amount ?: BigDecimal.ZERO)
    }

    /**
     * PUBLIC_INTERFACE
     * Formats a Double amount to BRL string.
     */
    fun formatBRL(amount: Double?): String {
        val formatter = newFormatter()
        return formatter.format(amount ?: 0.0)
    }

    /**
     * PUBLIC_INTERFACE
     * Formats a Long amount to BRL string (useful for cents after scaling).
     */
    fun formatBRL(amount: Long?): String {
        val formatter = newFormatter()
        return formatter.format(amount ?: 0L)
    }
}
