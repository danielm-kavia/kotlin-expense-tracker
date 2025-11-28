package com.kavia.expensetracker.util

import com.kavia.expensetracker.data.Item
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

/**
 * PUBLIC_INTERFACE
 * Returns previous month for a given YearMonth.
 */
fun prevMonth(current: YearMonth): YearMonth = current.minusMonths(1)

/**
 * PUBLIC_INTERFACE
 * Returns next month for a given YearMonth.
 */
fun nextMonth(current: YearMonth): YearMonth = current.plusMonths(1)

/**
 * PUBLIC_INTERFACE
 * Filters items by YearMonth for display.
 */
fun filterByMonth(items: List<Item>, month: YearMonth): List<Item> =
    items.filter { YearMonth.from(it.date) == month }

/**
 * PUBLIC_INTERFACE
 * Validates date in YYYY-MM-DD format and returns LocalDate or null.
 */
fun parseDateOrNull(text: String): LocalDate? {
    return try {
        val fmt = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT)
        LocalDate.parse(text, fmt)
    } catch (_: Exception) {
        null
    }
}
