package com.example.kotlinexpensetracker.domain

import android.graphics.Color
import androidx.annotation.DrawableRes

/**
 * Simple placeholder icon resources for categories.
 * In a real app, map these to actual drawables.
 */
object PlaceholderIcons {
    const val INCOME: Int = android.R.drawable.arrow_down_float
    const val EXPENSE: Int = android.R.drawable.arrow_up_float
}

/**
 * PUBLIC_INTERFACE
 * Category entry enriched with an optional icon resource.
 *
 * Mirrors the React categories.ts structure (id/key, title/name, color, expense flag),
 * with an extra icon placeholder for UI usage.
 */
data class CategoryEntry(
    val id: String,
    val title: String,
    val color: Int,
    val isExpense: Boolean,
    @DrawableRes val iconRes: Int? = null
)

/**
 * PUBLIC_INTERFACE
 * In-memory repository for categories.
 *
 * Provides:
 * - getAll(): List all categories
 * - getById(key): Lookup a category by its key/id
 *
 * The data is seeded to mirror the React app's categories.ts, distinguishing
 * income vs expense via isExpense flag.
 */
object CategoriesRepository {

    // Seeded categories (mirroring typical React categories.ts keys)
    // Colors chosen to be visually distinct. Use Android Color ints.
    private val seeded: Map<String, CategoryEntry> = mapOf(
        // Incomes
        "salary" to CategoryEntry(
            id = "salary",
            title = "Salário",
            color = Color.parseColor("#2E7D32"), // green
            isExpense = false,
            iconRes = PlaceholderIcons.INCOME
        ),
        "freelance" to CategoryEntry(
            id = "freelance",
            title = "Freelance",
            color = Color.parseColor("#1B5E20"),
            isExpense = false,
            iconRes = PlaceholderIcons.INCOME
        ),

        // Expenses
        "food" to CategoryEntry(
            id = "food",
            title = "Alimentação",
            color = Color.parseColor("#EF6C00"), // orange
            isExpense = true,
            iconRes = PlaceholderIcons.EXPENSE
        ),
        "rent" to CategoryEntry(
            id = "rent",
            title = "Aluguel",
            color = Color.parseColor("#C62828"), // red
            isExpense = true,
            iconRes = PlaceholderIcons.EXPENSE
        ),
        "transport" to CategoryEntry(
            id = "transport",
            title = "Transporte",
            color = Color.parseColor("#1565C0"), // blue
            isExpense = true,
            iconRes = PlaceholderIcons.EXPENSE
        ),
        "entertainment" to CategoryEntry(
            id = "entertainment",
            title = "Lazer",
            color = Color.parseColor("#6A1B9A"), // purple
            isExpense = true,
            iconRes = PlaceholderIcons.EXPENSE
        ),
        "health" to CategoryEntry(
            id = "health",
            title = "Saúde",
            color = Color.parseColor("#00897B"), // teal
            isExpense = true,
            iconRes = PlaceholderIcons.EXPENSE
        ),
        "education" to CategoryEntry(
            id = "education",
            title = "Educação",
            color = Color.parseColor("#283593"), // indigo
            isExpense = true,
            iconRes = PlaceholderIcons.EXPENSE
        ),
        "utilities" to CategoryEntry(
            id = "utilities",
            title = "Contas",
            color = Color.parseColor("#5D4037"), // brown
            isExpense = true,
            iconRes = PlaceholderIcons.EXPENSE
        ),
        "shopping" to CategoryEntry(
            id = "shopping",
            title = "Compras",
            color = Color.parseColor("#AD1457"), // pink
            isExpense = true,
            iconRes = PlaceholderIcons.EXPENSE
        )
    )

    /**
     * PUBLIC_INTERFACE
     * Returns all seeded categories as a list.
     *
     * @return List<CategoryEntry> of all categories.
     */
    fun getAll(): List<CategoryEntry> = seeded.values.toList()

    /**
     * PUBLIC_INTERFACE
     * Looks up a category by its id/key.
     *
     * @param key The category key (e.g., "food", "salary")
     * @return CategoryEntry if found, otherwise null.
     */
    fun getById(key: String): CategoryEntry? = seeded[key]
}
