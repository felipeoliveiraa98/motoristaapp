package com.motoristaapp.financas.ui.expenses

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.motoristaapp.financas.data.entity.ExpenseCategory
import com.motoristaapp.financas.data.entity.PaymentMethod
import com.motoristaapp.financas.util.DateUtils

private fun categoryLabel(c: ExpenseCategory) = c.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
private fun paymentLabel(p: PaymentMethod) = when (p) {
    PaymentMethod.DINHEIRO -> "Dinheiro"
    PaymentMethod.DEBITO -> "Débito"
    PaymentMethod.CREDITO -> "Crédito"
    PaymentMethod.PIX -> "Pix"
    PaymentMethod.OUTRO -> "Outro"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(viewModel: ExpensesViewModel, onBack: () -> Unit) {
    var category by remember { mutableStateOf(ExpenseCategory.COMBUSTIVEL) }
    var payment by remember { mutableStateOf(PaymentMethod.PIX) }
    var amountText by remember { mutableStateOf("") }
    var odometerText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var litersText by remember { mutableStateOf("") }
    var pricePerLiterText by remember { mutableStateOf("") }
    var fuelType by remember { mutableStateOf("Gasolina") }
    var error by remember { mutableStateOf<String?>(null) }
    var categoryMenu by remember { mutableStateOf(false) }
    var paymentMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar despesa") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar") } }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ExposedDropdownMenuBox(expanded = categoryMenu, onExpandedChange = { categoryMenu = it }) {
                OutlinedTextField(
                    value = categoryLabel(category), onValueChange = {}, readOnly = true,
                    label = { Text("Categoria") }, modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = categoryMenu, onDismissRequest = { categoryMenu = false }) {
                    ExpenseCategory.entries.forEach { c ->
                        DropdownMenuItem(text = { Text(categoryLabel(c)) }, onClick = { category = c; categoryMenu = false })
                    }
                }
            }

            OutlinedTextField(
                value = amountText, onValueChange = { amountText = it },
                label = { Text("Valor (R\$)") }, modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(expanded = paymentMenu, onExpandedChange = { paymentMenu = it }) {
                OutlinedTextField(
                    value = paymentLabel(payment), onValueChange = {}, readOnly = true,
                    label = { Text("Forma de pagamento") }, modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = paymentMenu, onDismissRequest = { paymentMenu = false }) {
                    PaymentMethod.entries.forEach { p ->
                        DropdownMenuItem(text = { Text(paymentLabel(p)) }, onClick = { payment = p; paymentMenu = false })
                    }
                }
            }

            OutlinedTextField(
                value = odometerText, onValueChange = { odometerText = it },
                label = { Text("Quilometragem do veículo (opcional)") }, modifier = Modifier.fillMaxWidth()
            )

            if (category == ExpenseCategory.COMBUSTIVEL) {
                OutlinedTextField(
                    value = fuelType, onValueChange = { fuelType = it },
                    label = { Text("Tipo de combustível") }, modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = litersText, onValueChange = { litersText = it },
                        label = { Text("Litros abastecidos") }, modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = pricePerLiterText, onValueChange = { pricePerLiterText = it },
                        label = { Text("Valor por litro") }, modifier = Modifier.weight(1f)
                    )
                }
            }

            OutlinedTextField(
                value = note, onValueChange = { note = it },
                label = { Text("Observação") }, modifier = Modifier.fillMaxWidth()
            )

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(
                onClick = {
                    val amount = amountText.replace(",", ".").toDoubleOrNull()
                    val odometer = odometerText.replace(",", ".").toDoubleOrNull()
                    val liters = litersText.replace(",", ".").toDoubleOrNull()
                    val pricePerLiter = pricePerLiterText.replace(",", ".").toDoubleOrNull()

                    if (amount == null) { error = "Informe um valor válido."; return@Button }

                    viewModel.addExpense(
                        date = DateUtils.startOfDay(),
                        category = category,
                        amount = amount,
                        paymentMethod = payment,
                        odometerKm = odometer,
                        note = note,
                        fuelLiters = if (category == ExpenseCategory.COMBUSTIVEL) liters else null,
                        fuelPricePerLiter = if (category == ExpenseCategory.COMBUSTIVEL) pricePerLiter else null,
                        fuelType = if (category == ExpenseCategory.COMBUSTIVEL) fuelType else null,
                        onError = { error = it },
                        onSuccess = onBack
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Salvar despesa") }
        }
    }
}
