package com.example.kotlinexpensetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinexpensetracker.domain.CategoriesRepository
import com.example.kotlinexpensetracker.domain.CategoryEntry
import com.example.kotlinexpensetracker.domain.ITransactionsRepository
import com.example.kotlinexpensetracker.domain.InMemoryTransactionsRepository
import com.example.kotlinexpensetracker.domain.Item
import com.example.kotlinexpensetracker.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Locale

/**
 * PUBLIC_INTERFACE
 * MainViewModel
 *
 * Provides state management for:
 * - currentMonthId: "yyyy-MM"
 * - monthLabel: human-readable month label "MMM/yyyy" (pt-BR capitalization)
 * - all transactions and month-filtered transactions
 * - computed totals: income, expenses, balance
 * - month navigation (previous/next)
 *
 * Backed by an in-memory TransactionsRepository. CategoriesRepository is used
 * to derive item types (income vs expense) during aggregation.
 */
class MainViewModel(
    private val transactionsRepository: ITransactionsRepository = InMemoryTransactionsRepository()
) : ViewModel() {

    private val _currentMonthId = MutableStateFlow(DateUtils.currentMonthId())
    /** PUBLIC_INTERFACE
     * Current month identifier in "yyyy-MM".
     */
    val currentMonthId: StateFlow<String> = _currentMonthId

    /** PUBLIC_INTERFACE
     * Month label like "Mar/2025" (pt-BR).
     */
    val monthLabel: StateFlow<String> =
        _currentMonthId
            .map { DateUtils.formatMonthLabel(it) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, DateUtils.formatMonthLabel(_currentMonthId.value))

    /** PUBLIC_INTERFACE
     * All items from the repository.
     */
    val allItems: StateFlow<List<Item>> = transactionsRepository.items
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /** PUBLIC_INTERFACE
     * Items filtered to the currently selected month.
     */
    val filteredItems: StateFlow<List<Item>> =
        combine(allItems, _currentMonthId) { items, monthId ->
            items.filter { item ->
                val iso = DateUtils.formatIsoDate(item.date)
                DateUtils.isSameMonth(iso, monthId)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /** PUBLIC_INTERFACE
     * Alias exposed for platform parity with "filteredTransactions". This ensures
     * Activity/UI collects the exact flow name required by the spec.
     */
    val filteredTransactions: StateFlow<List<Item>> = filteredItems

    /** PUBLIC_INTERFACE
     * Sum of income values in the current month.
     */
    val totalIncome: StateFlow<BigDecimal> =
        filteredItems.map { items ->
            items.fold(BigDecimal.ZERO) { acc, item ->
                val cat = CategoriesRepository.getById(item.categoryId)
                if (cat != null && !cat.isExpense) acc + item.value else acc
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, BigDecimal.ZERO)

    /** PUBLIC_INTERFACE
     * Sum of expense values in the current month.
     */
    val totalExpenses: StateFlow<BigDecimal> =
        filteredItems.map { items ->
            items.fold(BigDecimal.ZERO) { acc, item ->
                val cat = CategoriesRepository.getById(item.categoryId)
                if (cat != null && cat.isExpense) acc + item.value else acc
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, BigDecimal.ZERO)

    /** PUBLIC_INTERFACE
     * Net balance = income - expenses.
     */
    val balance: StateFlow<BigDecimal> =
        combine(totalIncome, totalExpenses) { income, expenses ->
            income - expenses
        }.stateIn(viewModelScope, SharingStarted.Eagerly, BigDecimal.ZERO)

    /**
     * PUBLIC_INTERFACE
     * Moves to the previous month.
     */
    fun goToPreviousMonth() {
        _currentMonthId.value = DateUtils.getPreviousMonth(_currentMonthId.value)
    }

    /**
     * PUBLIC_INTERFACE
     * Moves to the next month.
     */
    fun goToNextMonth() {
        _currentMonthId.value = DateUtils.getNextMonth(_currentMonthId.value)
    }

    /**
     * PUBLIC_INTERFACE
     * Directly sets the current month id (yyyy-MM). Throws if invalid.
     */
    fun setCurrentMonth(monthId: String) {
        // Will throw inside DateUtils.formatMonthLabel if invalid; allow surface for early dev
        DateUtils.formatMonthLabel(monthId)
        _currentMonthId.value = monthId
    }

    /**
     * PUBLIC_INTERFACE
     * Adds a new item into the repository.
     */
    fun addItem(item: Item) {
        viewModelScope.launch {
            transactionsRepository.add(item)
        }
    }

    /**
     * PUBLIC_INTERFACE
     * Updates an existing item; no-op if not found.
     */
    fun updateItem(item: Item) {
        viewModelScope.launch {
            transactionsRepository.update(item)
        }
    }

    /**
     * PUBLIC_INTERFACE
     * Deletes an item by id; no-op if not found.
     */
    fun deleteItem(id: String) {
        viewModelScope.launch {
            transactionsRepository.delete(id)
        }
    }

    /**
     * PUBLIC_INTERFACE
     * Convenience for UI: resolves a CategoryEntry by id.
     */
    fun getCategory(categoryId: String): CategoryEntry? = CategoriesRepository.getById(categoryId)

    /**
     * PUBLIC_INTERFACE
     * Returns all categories for use in pickers.
     */
    fun getAllCategories(): List<CategoryEntry> = CategoriesRepository.getAll()

    /**
     * PUBLIC_INTERFACE
     * Locale used for UI formatting where needed (pt-BR for currency and labels).
     */
    val uiLocale: Locale = Locale("pt", "BR")
}
