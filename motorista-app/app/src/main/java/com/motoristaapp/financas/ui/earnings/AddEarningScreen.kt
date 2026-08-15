package com.motoristaapp.financas.ui.earnings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.motoristaapp.financas.data.entity.Platform
import java.util.Calendar

private fun platformLabel(p: Platform) = when (p) {
    Platform.UBER -> "Uber"
    Platform.NOVENTA_E_NOVE -> "99"
    Platform.INDRIVE -> "InDrive"
    Platform.PARTICULAR -> "Particular"
    Platform.OUTRO -> "Outro"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEarningScreen(viewModel: EarningsViewModel, onBack: () -> Unit) {
    var platform by remember { mutableStateOf(Platform.UBER) }
    var amountText by remember { mutableStateOf("") }
    var startTimeText by remember { mutableStateOf("08:00") }
    var endTimeText by remember { mutableStateOf("17:00") }
    var startKmText by remember { mutableStateOf("") }
    var endKmText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var platformMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar ganho") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar") }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ExposedDropdownMenuBox(expanded = platformMenuExpanded, onExpandedChange = { platformMenuExpanded = it }) {
                OutlinedTextField(
                    value = platformLabel(platform),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Plataforma") },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = platformMenuExpanded, onDismissRequest = { platformMenuExpanded = false }) {
                    Platform.entries.forEach { p ->
                        DropdownMenuItem(text = { Text(platformLabel(p)) }, onClick = { platform = p; platformMenuExpanded = false })
                    }
                }
            }

            OutlinedTextField(
                value = amountText, onValueChange = { amountText = it },
                label = { Text("Valor ganho (R\$)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = startTimeText, onValueChange = { startTimeText = it },
                    label = { Text("Horário inicial (HH:mm)") }, modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = endTimeText, onValueChange = { endTimeText = it },
                    label = { Text("Horário final (HH:mm)") }, modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = startKmText, onValueChange = { startKmText = it },
                    label = { Text("Km inicial") }, modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = endKmText, onValueChange = { endKmText = it },
                    label = { Text("Km final") }, modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = note, onValueChange = { note = it },
                label = { Text("Observação") }, modifier = Modifier.fillMaxWidth()
            )

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(
                onClick = {
                    val amount = amountText.replace(",", ".").toDoubleOrNull()
                    val startKm = startKmText.replace(",", ".").toDoubleOrNull()
                    val endKm = endKmText.replace(",", ".").toDoubleOrNull()
                    val startMillis = parseTimeToday(startTimeText)
                    val endMillis = parseTimeToday(endTimeText)

                    if (amount == null) { error = "Informe um valor de ganho válido."; return@Button }
                    if (startKm == null || endKm == null) { error = "Informe a quilometragem inicial e final."; return@Button }
                    if (startMillis == null || endMillis == null) { error = "Informe horários válidos (HH:mm)."; return@Button }

                    viewModel.addEarning(
                        date = com.motoristaapp.financas.util.DateUtils.startOfDay(),
                        platform = platform,
                        amount = amount,
                        startTimeMillis = startMillis,
                        endTimeMillis = endMillis,
                        startKm = startKm,
                        endKm = endKm,
                        note = note,
                        onError = { error = it },
                        onSuccess = onBack
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Salvar ganho") }
        }
    }
}

private fun parseTimeToday(text: String): Long? {
    val parts = text.split(":")
    if (parts.size != 2) return null
    val hour = parts[0].toIntOrNull() ?: return null
    val minute = parts[1].toIntOrNull() ?: return null
    if (hour !in 0..23 || minute !in 0..59) return null
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, hour)
    cal.set(Calendar.MINUTE, minute)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}
