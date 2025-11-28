package com.example.kotlinexpensetracker.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

/**
 * PUBLIC_INTERFACE
 * Repository interface for transactions with observable data source.
 *
 * Exposes a StateFlow<List<Item>> to observe all items and CRUD methods
 * to add, update, and delete items. This is an in-memory implementation
 * safe for CI and early UI development. Replace with Room/DB in future.
 */
interface ITransactionsRepository {
    /** PUBLIC_INTERFACE */
    val items: StateFlow<List<Item>>

    /**
     * PUBLIC_INTERFACE
     * Adds a new transaction item. If id is blank, a new UUID will be generated.
     * Returns the added item with its effective id.
     */
    fun add(item: Item): Item

    /**
     * PUBLIC_INTERFACE
     * Updates an existing transaction by id, returning true if updated.
     */
    fun update(item: Item): Boolean

    /**
     * PUBLIC_INTERFACE
     * Deletes an item by id, returning true if deleted.
     */
    fun delete(id: String): Boolean

    /**
     * PUBLIC_INTERFACE
     * Replaces whole list (used for seeding or imports).
     */
    fun replaceAll(newItems: List<Item>)
}

/**
 * PUBLIC_INTERFACE
 * A simple in-memory TransactionsRepository with initial seed data.
 *
 * Thread-safety: Uses a MutableStateFlow; synchronized mutation on a lock to
 * keep list updates atomic for this simple scenario.
 */
class InMemoryTransactionsRepository(
    initial: List<Item> = defaultSeed()
) : ITransactionsRepository {

    private val lock = Any()
    private val _items = MutableStateFlow(initial)
    override val items: StateFlow<List<Item>> = _items.asStateFlow()

    override fun add(item: Item): Item {
        val finalItem = if (item.id.isBlank()) item.copy(id = UUID.randomUUID().toString()) else item
        synchronized(lock) {
            _items.value = _items.value + finalItem
        }
        return finalItem
    }

    override fun update(item: Item): Boolean {
        var updated = false
        synchronized(lock) {
            val current = _items.value
            val idx = current.indexOfFirst { it.id == item.id }
            if (idx >= 0) {
                val newList = current.toMutableList()
                newList[idx] = item
                _items.value = newList
                updated = true
            }
        }
        return updated
    }

    override fun delete(id: String): Boolean {
        var removed = false
        synchronized(lock) {
            val current = _items.value
            if (current.any { it.id == id }) {
                _items.value = current.filterNot { it.id == id }
                removed = true
            }
        }
        return removed
    }

    override fun replaceAll(newItems: List<Item>) {
        synchronized(lock) {
            _items.value = newItems.toList()
        }
    }

    companion object {
        /**
         * PUBLIC_INTERFACE
         * Provides default seed items mirroring the React example behavior: a few
         * entries in current month with common categories.
         */
        fun defaultSeed(): List<Item> {
            val today = Date()
            return listOf(
                Item(
                    id = UUID.randomUUID().toString(),
                    date = today,
                    categoryId = "salary",
                    title = "Salário",
                    value = BigDecimal("5500.00")
                ),
                Item(
                    id = UUID.randomUUID().toString(),
                    date = today,
                    categoryId = "rent",
                    title = "Aluguel",
                    value = BigDecimal("2200.00")
                ),
                Item(
                    id = UUID.randomUUID().toString(),
                    date = today,
                    categoryId = "food",
                    title = "Alimentação",
                    value = BigDecimal("350.40")
                ),
                Item(
                    id = UUID.randomUUID().toString(),
                    date = today,
                    categoryId = "transport",
                    title = "Transporte",
                    value = BigDecimal("120.00")
                )
            )
        }
    }
}
