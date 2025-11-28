package com.kavia.expensetracker.data

/**
 * PUBLIC_INTERFACE
 * categories contains the static set of categories comparable to the React app.
 * Each category has title, color, and expense flag (false = income).
 */
val categories: Map<String, Category> = listOf(
    Category("food", "Alimentação", "#FF7043", expense = true),
    Category("rent", "Aluguel", "#8D6E63", expense = true),
    Category("salary", "Salário", "#43A047", expense = false),
    Category("freelance", "Freelancer", "#1E88E5", expense = false),
    Category("transport", "Transporte", "#7E57C2", expense = true),
    Category("entertainment", "Lazer", "#F4511E", expense = true),
    Category("health", "Saúde", "#D81B60", expense = true),
    Category("other", "Outros", "#546E7A", expense = true)
).associateBy { it.key }
