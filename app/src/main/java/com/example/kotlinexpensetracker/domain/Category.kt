package com.example.kotlinexpensetracker.domain

import androidx.annotation.ColorInt

/**
 * PUBLIC_INTERFACE
 * Represents a transaction category (e.g., Salary, Food, Rent).
 *
 * @property id Unique identifier for the category.
 * @property title Human-readable name of the category.
 * @property color ARGB color int used to visually identify the category.
 * @property isExpense Whether this category represents an expense (true) or income (false).
 */
data class Category(
    val id: String,
    val title: String,
    @ColorInt val color: Int,
    val isExpense: Boolean
) {
    companion object {
        /**
         * PUBLIC_INTERFACE
         * Creates a copy of this category toggling between expense and income.
         *
         * Useful for testing or quick derivations.
         */
        fun Category.asToggledExpense(): Category = copy(isExpense = !this.isExpense)
    }
}
