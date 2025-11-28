package com.example.kotlinexpensetracker.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.Date

/**
 * PUBLIC_INTERFACE
 * Date utilities mirroring the React app's dateFilter.ts behavior but implemented
 * with pre-java.time APIs for Android minSdk 24 compatibility (avoids lint NewApi).
 *
 * Provides helpers to:
 * - Format current month as "MMM/yyyy" (e.g., "Mar/2025" for pt-BR).
 * - Navigate to previous/next month based on a "yyyy-MM" identifier.
 * - Check if a date falls in a specific "currentMonth" (yyyy-MM).
 * - Parse and format ISO day dates (yyyy-MM-dd).
 */
object DateUtils {

    private val ptBr = Locale("pt", "BR")
    private val utcTz: TimeZone = TimeZone.getTimeZone("UTC")

    private fun sdf(pattern: String, locale: Locale = ptBr): SimpleDateFormat =
        SimpleDateFormat(pattern, locale).apply { timeZone = utcTz }

    private val monthIdFormat get() = sdf("yyyy-MM", Locale.US) // monthId always numeric, locale-agnostic
    private val monthLabelFormat get() = sdf("MMM/yyyy", ptBr)
    private val isoDateFormat get() = sdf("yyyy-MM-dd", Locale.US)

    /**
     * PUBLIC_INTERFACE
     * Returns the current month identifier in "yyyy-MM" format.
     * Example: 2025-03
     */
    fun currentMonthId(now: Date = Date()): String = monthIdFormat.format(now)

    /**
     * PUBLIC_INTERFACE
     * Formats a "yyyy-MM" month identifier to a label like "Mar/2025"
     * with pt-BR month short name capitalized.
     *
     * @param monthId A string in "yyyy-MM".
     */
    fun formatMonthLabel(monthId: String): String {
        val date = requireNotNull(parseMonthIdToDate(monthId)) { "Invalid monthId: $monthId" }
        val raw = monthLabelFormat.format(date) // e.g., "mar/2025" on some locales
        // Capitalize first letter to match "Mar/2025"
        return raw.replaceFirstChar { if (it.isLowerCase()) it.titlecase(ptBr) else it.toString() }
    }

    /**
     * PUBLIC_INTERFACE
     * Returns the next month identifier for a given "yyyy-MM".
     */
    fun getNextMonth(monthId: String): String {
        val cal = monthIdToCalendar(monthId)
        cal.add(Calendar.MONTH, 1)
        return monthIdFormat.format(cal.time)
    }

    /**
     * PUBLIC_INTERFACE
     * Returns the previous month identifier for a given "yyyy-MM".
     */
    fun getPreviousMonth(monthId: String): String {
        val cal = monthIdToCalendar(monthId)
        cal.add(Calendar.MONTH, -1)
        return monthIdFormat.format(cal.time)
    }

    /**
     * PUBLIC_INTERFACE
     * Checks whether a given ISO date string (yyyy-MM-dd) is in the same month
     * as the provided "currentMonth" (yyyy-MM).
     */
    fun isSameMonth(isoDate: String, currentMonth: String): Boolean {
        val date = parseIsoDate(isoDate) ?: return false
        val calDate = Calendar.getInstance(utcTz).apply { time = date }
        val calMonth = monthIdToCalendar(currentMonth)
        return calDate.get(Calendar.YEAR) == calMonth.get(Calendar.YEAR) &&
                calDate.get(Calendar.MONTH) == calMonth.get(Calendar.MONTH)
    }

    /**
     * PUBLIC_INTERFACE
     * Parses a date string in ISO-8601 (yyyy-MM-dd) to a Date (UTC). Returns null for invalid input.
     */
    fun parseIsoDate(dateStr: String): Date? = try {
        isoDateFormat.parse(dateStr)
    } catch (_: ParseException) {
        null
    }

    /**
     * PUBLIC_INTERFACE
     * Formats a Date (UTC) to ISO-8601 string (yyyy-MM-dd).
     */
    fun formatIsoDate(date: Date): String = isoDateFormat.format(date)

    private fun parseMonthIdToDate(monthId: String): Date? = try {
        monthIdFormat.parse(monthId)
    } catch (_: ParseException) {
        null
    }

    private fun monthIdToCalendar(monthId: String): Calendar {
        val date = requireNotNull(parseMonthIdToDate(monthId)) { "Invalid monthId: $monthId" }
        return Calendar.getInstance(utcTz).apply {
            time = date
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }
}
