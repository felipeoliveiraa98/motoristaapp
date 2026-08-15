package com.motoristaapp.financas.ui.earnings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.motoristaapp.financas.data.entity.Earning
import com.motoristaapp.financas.data.entity.Platform
import com.motoristaapp.financas.util.DateUtils
import com.motoristaapp.financas.util.Formatters

private fun platformLabel(p: Platform) = when (p) {
    Platform.UBER -> "Uber"
    Platform.NOVENTA_E_NOVE -> "99"
    Platform.INDRIVE -> "InDrive"
    Platform.PARTICULAR -> "Particular"
    Platform.OUTRO -> "Outro"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarningsScreen(viewModel: EarningsViewModel, onAddClick: () -> Unit) {
    val earnings by viewModel.earnings.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ganhos") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) { Text("+") }
        }
    ) { padding ->
        if (earnings.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Nenhum ganho registrado ainda. Toque em + para adicionar.")
            }
        } else {
            LazyColumn(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(earnings, key = { it.id }) { earning ->
                    EarningCard(earning, onDelete = { viewModel.deleteEarning(earning) })
                }
                item { Spacer(Modifier.height(72.dp)) }
            }
        }
    }
}

@Composable
private fun EarningCard(earning: Earning, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("${DateUtils.formatDate(earning.date)} · ${platformLabel(earning.platform)}", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text(Formatters.currency(earning.amount), style = MaterialTheme.typography.titleMedium)
                Text(
                    "${Formatters.decimal(earning.hoursWorked)}h · ${Formatters.decimal(earning.kmDriven)}km · " +
                        "${Formatters.currency(earning.earningPerHour)}/h · ${Formatters.currency(earning.earningPerKm)}/km",
                    style = MaterialTheme.typography.bodySmall
                )
                if (earning.note.isNotBlank()) {
                    Text(earning.note, style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Excluir")
            }
        }
    }
}
