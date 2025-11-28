package com.kavia.expensetracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kavia.expensetracker.data.Category
import com.kavia.expensetracker.data.Item
import com.kavia.expensetracker.data.categories
import com.kavia.expensetracker.util.filterByMonth
import com.kavia.expensetracker.util.formatBRL
import com.kavia.expensetracker.util.nextMonth
import com.kavia.expensetracker.util.parseDateOrNull
import com.kavia.expensetracker.util.prevMonth
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private data class FormState(
    var dateText: String = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
    var categoryKey: String = categories.keys.firstOrNull() ?: "other",
    var title: String = "",
    var valueText: String = "",
    var editingId: Long? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTrackerScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val items = remember { mutableStateListOf<Item>() }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val form = remember { mutableStateOf(FormState()) }
    // Channel snackbar messages through state so we trigger snackbar from a composable scope
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    // Seed with a couple demo items for UX
    LaunchedEffect(Unit) {
        if (items.isEmpty()) {
            items.addAll(
                listOf(
                    Item(
                        id = System.nanoTime(),
                        date = LocalDate.now().withDayOfMonth(5),
                        categoryKey = "salary",
                        title = "Salário",
                        value = BigDecimal("5000.00")
                    ),
                    Item(
                        id = System.nanoTime(),
                        date = LocalDate.now().withDayOfMonth(7),
                        categoryKey = "food",
                        title = "Supermercado",
                        value = BigDecimal("250.30")
                    )
                )
            )
        }
    }

    val monthItems = filterByMonth(items, currentMonth)
    val totals = remember(monthItems) {
        val income = monthItems
            .filter { categories[it.categoryKey]?.expense == false }
            .fold(BigDecimal.ZERO) { acc, it -> acc + it.value }
        val expense = monthItems
            .filter { categories[it.categoryKey]?.expense == true }
            .fold(BigDecimal.ZERO) { acc, it -> acc + it.value }
        Triple(income, expense, income - expense)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Expense Tracker") }
            )

            MonthHeader(
                currentMonth = currentMonth,
                onPrev = { currentMonth = prevMonth(currentMonth) },
                onNext = { currentMonth = nextMonth(currentMonth) }
            )

            SummaryCards(
                income = totals.first,
                expense = totals.second,
                balance = totals.third
            )

            Spacer(modifier = Modifier.height(8.dp))

            TransactionForm(
                formState = form,
                onSubmit = { result ->
                    when (result) {
                        is SubmitResult.Error -> {
                            // Set message to trigger snackbar from a composable LaunchedEffect below
                            snackbarMessage = result.message
                        }

                        is SubmitResult.Saved -> {
                            // update or add
                            val existingIndex = items.indexOfFirst { it.id == result.item.id }
                            if (existingIndex >= 0) {
                                items[existingIndex] = result.item
                            } else {
                                items.add(0, result.item) // newest first
                            }
                            // reset form
                            form.value = FormState(
                                dateText = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                                categoryKey = categories.keys.firstOrNull() ?: "other",
                                title = "",
                                valueText = "",
                                editingId = null
                            )
                        }
                    }
                }
            )

            // Show snackbar when message changes
            LaunchedEffect(snackbarMessage) {
                snackbarMessage?.let {
                    snackbarHostState.showSnackbar(it)
                    snackbarMessage = null
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TransactionList(
                items = monthItems,
                onEdit = { item ->
                    form.value = FormState(
                        dateText = item.date.format(DateTimeFormatter.ISO_DATE),
                        categoryKey = item.categoryKey,
                        title = item.title,
                        valueText = item.value.toPlainString(),
                        editingId = item.id
                    )
                },
                onDelete = { item ->
                    items.removeAll { it.id == item.id }
                }
            )

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}

@Composable
private fun MonthHeader(currentMonth: YearMonth, onPrev: () -> Unit, onNext: () -> Unit) {
    val title = currentMonth.format(DateTimeFormatter.ofPattern("MMMM uuuu"))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onPrev) { Text("< Mês anterior") }
        Text(
            text = title.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Button(onClick = onNext) { Text("Próximo mês >") }
    }
}

@Composable
private fun SummaryCards(income: BigDecimal, expense: BigDecimal, balance: BigDecimal) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SummaryCard("Entradas", formatBRL(income), Color(0xFF43A047), modifier = Modifier.weight(1f))
        SummaryCard("Saídas", formatBRL(expense), Color(0xFFD32F2F), modifier = Modifier.weight(1f))
        SummaryCard(
            "Balanço",
            formatBRL(balance),
            if (balance >= BigDecimal.ZERO) Color(0xFF1E88E5) else Color(0xFFD32F2F),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryCard(title: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = accent)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

private sealed interface SubmitResult {
    data class Saved(val item: Item) : SubmitResult
    data class Error(val message: String) : SubmitResult
}

@Composable
private fun TransactionForm(
    formState: MutableState<FormState>,
    onSubmit: (SubmitResult) -> Unit
) {
    val form = formState.value
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(12.dp)
            .verticalScroll(scroll)
    ) {
        Text(
            text = if (form.editingId == null) "Adicionar transação" else "Editar transação",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = form.dateText,
            onValueChange = { formState.value = form.copy(dateText = it) },
            label = { Text("Data (YYYY-MM-DD)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        CategoryRow(
            selectedKey = form.categoryKey,
            onSelected = { formState.value = form.copy(categoryKey = it) }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = form.title,
            onValueChange = { formState.value = form.copy(title = it) },
            label = { Text("Título/Descrição") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = form.valueText,
            onValueChange = { formState.value = form.copy(valueText = it) },
            label = { Text("Valor (ex: 123.45)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val date = parseDateOrNull(form.dateText)
                if (date == null) {
                    onSubmit(SubmitResult.Error("Data inválida. Use YYYY-MM-DD."))
                    return@Button
                }
                val cat = categories[form.categoryKey]
                if (cat == null) {
                    onSubmit(SubmitResult.Error("Categoria inválida."))
                    return@Button
                }
                val title = form.title.trim()
                if (title.isEmpty()) {
                    onSubmit(SubmitResult.Error("Título não pode ser vazio."))
                    return@Button
                }
                val amount = form.valueText.replace(",", ".").toBigDecimalOrNull()
                if (amount == null || amount <= BigDecimal.ZERO) {
                    onSubmit(SubmitResult.Error("Valor deve ser positivo."))
                    return@Button
                }
                val id = form.editingId ?: System.nanoTime()
                onSubmit(
                    SubmitResult.Saved(
                        Item(
                            id = id,
                            date = date,
                            categoryKey = form.categoryKey,
                            title = title,
                            value = amount.abs() // store positive; category expense flag determines sign in summaries
                        )
                    )
                )
            }) {
                Text(if (form.editingId == null) "Adicionar" else "Salvar")
            }

            if (form.editingId != null) {
                Button(onClick = {
                    // Cancel editing and reset
                    formState.value = FormState(
                        dateText = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                        categoryKey = categories.keys.firstOrNull() ?: "other",
                        title = "",
                        valueText = "",
                        editingId = null
                    )
                }) {
                    Text("Cancelar")
                }
            }
        }
    }
}

@Composable
private fun CategoryRow(selectedKey: String, onSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.values.forEach { cat ->
            val selected = cat.key == selectedKey
            FilterChip(
                selected = selected,
                onClick = { onSelected(cat.key) },
                label = { Text(cat.title) },
                leadingIcon = {},
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun TransactionList(
    items: List<Item>,
    onEdit: (Item) -> Unit,
    onDelete: (Item) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("Transações", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma transação para este mês.")
            }
            return
        }

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Data", modifier = Modifier.weight(1.2f), fontWeight = FontWeight.SemiBold)
            Text("Categoria", modifier = Modifier.weight(1.3f), fontWeight = FontWeight.SemiBold)
            Text("Título", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.SemiBold)
            Text("Valor", modifier = Modifier.weight(1.0f), fontWeight = FontWeight.SemiBold)
            Text("Ações", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.SemiBold)
        }
        Divider()

        items.forEach { item ->
            val cat: Category? = categories[item.categoryKey]
            val isExpense = cat?.expense == true
            val displayValue = if (isExpense) "- ${formatBRL(item.value)}" else formatBRL(item.value)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onEdit(item) }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.date.format(DateTimeFormatter.ISO_DATE), modifier = Modifier.weight(1.2f))
                Row(modifier = Modifier.weight(1.3f), verticalAlignment = Alignment.CenterVertically) {
                    val color = cat?.colorHex?.let { hexToColor(it) } ?: MaterialTheme.colorScheme.primary
                    // Small color square/pill before category title
                    Box(
                        modifier = Modifier
                            .width(12.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(color)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(cat?.title ?: item.categoryKey)
                }
                Text(item.title, modifier = Modifier.weight(1.5f))
                Text(
                    displayValue,
                    modifier = Modifier.weight(1.0f),
                    color = if (isExpense) Color(0xFFD32F2F) else Color(0xFF388E3C),
                    fontWeight = FontWeight.Medium
                )
                Row(modifier = Modifier.weight(0.8f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { onEdit(item) }) { Text("Editar") }
                    Button(onClick = { onDelete(item) }) { Text("Excluir") }
                }
            }
            Divider()
        }
    }
}

private fun hexToColor(hex: String): Color {
    val clean = hex.removePrefix("#")
    val colorInt = clean.toLong(16).toInt()
    val r = (colorInt shr 16) and 0xFF
    val g = (colorInt shr 8) and 0xFF
    val b = colorInt and 0xFF
    return Color(r, g, b)
}
