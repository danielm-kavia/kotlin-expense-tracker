package com.example.kotlinexpensetracker.domain

import java.math.BigDecimal
import java.util.Date

/**
 * PUBLIC_INTERFACE
 * Represents a financial entry (income or expense).
 *
 * Mirrors the React app's Item model. The 'type' is derived from the associated Category.
 *
 * @property id Unique identifier for the item.
 * @property date The entry date (UTC Date for broad Android compatibility).
 * @property categoryId Reference ID of the item's category (use to lookup Category).
 * @property title Description or title of the entry.
 * @property value Monetary amount (BigDecimal to avoid floating-point errors).
 */
data class Item(
    val id: String,
    val date: Date,
    val categoryId: String,
    val title: String,
    val value: BigDecimal
) {

    /**
     * PUBLIC_INTERFACE
     * Type of the item (Income or Expense).
     *
     * This is derived from the Category.isExpense field in the associated Category.
     */
    enum class Type { INCOME, EXPENSE }

    /**
     * PUBLIC_INTERFACE
     * Returns the derived Type using the provided category.
     *
     * @param category The category associated with this item.
     * @return Type.EXPENSE if category.isExpense is true, otherwise Type.INCOME.
     */
    fun deriveType(category: Category): Type =
        if (category.isExpense) Type.EXPENSE else Type.INCOME

    /**
     * PUBLIC_INTERFACE
     * Convenience helper that returns whether this item is an expense given its category.
     */
    fun isExpense(category: Category): Boolean = deriveType(category) == Type.EXPENSE

    /**
     * PUBLIC_INTERFACE
     * Convenience helper that returns whether this item is an income given its category.
     */
    fun isIncome(category: Category): Boolean = deriveType(category) == Type.INCOME
}
