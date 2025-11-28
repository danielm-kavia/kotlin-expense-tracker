package com.kavia.expensetracker.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

/**
 * PUBLIC_INTERFACE
 * Formats a BigDecimal amount in pt-BR currency (BRL).
 */
fun formatBRL(amount: BigDecimal): String {
    val nf = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return nf.format(amount)
}
