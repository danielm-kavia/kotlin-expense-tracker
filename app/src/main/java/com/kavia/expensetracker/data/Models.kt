package com.kavia.expensetracker.data

import java.math.BigDecimal
import java.time.LocalDate

/**
 * PUBLIC_INTERFACE
 * Item represents a single transaction entry.
 */
data class Item(
    val id: Long,
    val date: LocalDate,
    val categoryKey: String,
    val title: String,
    val value: BigDecimal
)

/**
 * PUBLIC_INTERFACE
 * Category represents a static category definition with display title, color, and expense flag.
 */
data class Category(
    val key: String,
    val title: String,
    val colorHex: String,
    val expense: Boolean
)
