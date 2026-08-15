package com.motoristaapp.financas.ui.expenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.motoristaapp.financas.data.entity.Expense
import com.motoristaapp.financas.util.DateUtils
import com.motoristaapp.financas.util.Formatters

private fun categoryLabel(name: String) = name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(viewModel: ExpensesViewModel, onAddClick: () -> Unit) {
    val expenses by viewModel.expenses.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Despesas") }) },
        floatingActionButton = { FloatingActionButton(onClick = onAddClick) { Text("+") } }
    ) { padding ->
        if (expenses.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Nenhuma despesa registrada ainda. Toque em + para adicionar.")
            }
        } else {
            LazyColumn(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(expenses, key = { it.id }) { expense ->
                    ExpenseCard(expense, onDelete = { viewModel.deleteExpense(expense) })
                }
                item { Spacer(Modifier.height(72.dp)) }
            }
        }
    }
}

@Composable
private fun ExpenseCard(expense: Expense, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("${DateUtils.formatDate(expense.date)} · ${categoryLabel(expense.category.name)}", fontWeight = FontWeight.Bold)
                Text(Formatters.currency(expense.amount), style = MaterialTheme.typography.titleMedium)
                if (expense.fuelLiters != null) {
                    Text(
                        "${Formatters.decimal(expense.fuelLiters)} L · ${Formatters.currency(expense.fuelPricePerLiter ?: 0.0)}/L",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (expense.note.isNotBlank()) Text(expense.note, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) { Icon(Icons.Filled.Delete, contentDescription = "Excluir") }
        }
    }
}
