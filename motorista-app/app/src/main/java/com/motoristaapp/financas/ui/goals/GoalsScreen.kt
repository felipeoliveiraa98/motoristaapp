package com.motoristaapp.financas.ui.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.motoristaapp.financas.ui.components.MetricRow
import com.motoristaapp.financas.ui.components.SectionCard
import com.motoristaapp.financas.ui.theme.GreenProfit
import com.motoristaapp.financas.util.Formatters
import java.util.Calendar

private val dayLabels = listOf("D", "S", "T", "Q", "Q", "S", "S") // Dom..Sáb (Calendar.DAY_OF_WEEK 1..7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(viewModel: GoalsViewModel) {
    val state by viewModel.uiState.collectAsState()

    var revenueGoalText by remember(state.monthlyRevenueGoal) { mutableStateOf(if (state.monthlyRevenueGoal > 0) state.monthlyRevenueGoal.toString() else "") }
    var profitGoalText by remember(state.monthlyProfitGoal) { mutableStateOf(if (state.monthlyProfitGoal > 0) state.monthlyProfitGoal.toString() else "") }
    var manualDailyText by remember(state.manualDailyGoal) { mutableStateOf(state.manualDailyGoal?.toString() ?: "") }
    var workDaysMask by remember(state.workDaysMask) { mutableStateOf(state.workDaysMask) }

    Scaffold(topBar = { TopAppBar(title = { Text("Metas") }) }) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionCard(title = "Progresso do mês") {
                val goalColor = if (state.percentRevenue >= 100) GreenProfit else MaterialTheme.colorScheme.primary
                if (state.monthlyRevenueGoal > 0) {
                    LinearProgressIndicator(
                        progress = { (state.percentRevenue / 100.0).toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth(), color = goalColor
                    )
                    Spacer(Modifier.height(8.dp))
                }
                MetricRow("Meta de faturamento", Formatters.currency(state.monthlyRevenueGoal))
                MetricRow("Realizado", Formatters.currency(state.realizedRevenue))
                MetricRow("Falta", Formatters.currency((state.monthlyRevenueGoal - state.realizedRevenue).coerceAtLeast(0.0)))
                MetricRow("Percentual atingido", Formatters.percent(state.percentRevenue))
                Spacer(Modifier.height(8.dp))
                MetricRow("Meta de lucro", Formatters.currency(state.monthlyProfitGoal))
                MetricRow("Lucro realizado", Formatters.currency(state.realizedProfit), GreenProfit)
                MetricRow("Percentual de lucro atingido", Formatters.percent(state.percentProfit))
                Spacer(Modifier.height(8.dp))
                MetricRow("Dias de trabalho restantes", "${state.remainingWorkDays}")
                MetricRow("Necessário/dia (faturamento)", Formatters.currency(state.neededPerDayRevenue))
                MetricRow("Necessário/dia (lucro)", Formatters.currency(state.neededPerDayProfit))
            }

            SectionCard(title = "Configurar metas") {
                OutlinedTextField(
                    value = revenueGoalText, onValueChange = { revenueGoalText = it },
                    label = { Text("Meta de faturamento mensal (R\$)") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = profitGoalText, onValueChange = { profitGoalText = it },
                    label = { Text("Meta de lucro mensal (R\$)") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = manualDailyText, onValueChange = { manualDailyText = it },
                    label = { Text("Meta diária manual (opcional)") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Text("Dias de trabalho na semana", style = MaterialTheme.typography.labelLarge)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (dow in 1..7) {
                        val selected = (workDaysMask shr (dow - 1)) and 1 == 1
                        FilterChip(
                            selected = selected,
                            onClick = {
                                workDaysMask = if (selected) workDaysMask and (1 shl (dow - 1)).inv()
                                else workDaysMask or (1 shl (dow - 1))
                            },
                            label = { Text(dayLabels[dow - 1]) }
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        val revenueGoal = revenueGoalText.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val profitGoal = profitGoalText.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val manualDaily = manualDailyText.replace(",", ".").toDoubleOrNull()
                        viewModel.saveGoals(revenueGoal, profitGoal, manualDaily, workDaysMask)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Salvar metas") }
            }
        }
    }
}
