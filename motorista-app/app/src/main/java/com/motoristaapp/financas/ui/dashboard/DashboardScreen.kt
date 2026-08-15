package com.motoristaapp.financas.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.motoristaapp.financas.data.entity.Platform
import com.motoristaapp.financas.ui.components.HighlightMetric
import com.motoristaapp.financas.ui.components.MetricRow
import com.motoristaapp.financas.ui.components.SectionCard
import com.motoristaapp.financas.ui.theme.GreenProfit
import com.motoristaapp.financas.ui.theme.RedExpense
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
fun DashboardScreen(viewModel: DashboardViewModel) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Dashboard") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    PeriodFilter.entries.forEachIndexed { index, p ->
                        SegmentedButton(
                            selected = state.period == p,
                            onClick = { viewModel.setPeriod(p) },
                            shape = SegmentedButtonDefaults.itemShape(index, PeriodFilter.entries.size)
                        ) {
                            Text(
                                when (p) {
                                    PeriodFilter.HOJE -> "Hoje"
                                    PeriodFilter.SEMANA -> "Semana"
                                    PeriodFilter.MES -> "Mês"
                                }
                            )
                        }
                    }
                }
            }

            item {
                // Lucro real em destaque (item 13)
                SectionCard(title = "Quanto eu ganhei de verdade?") {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        HighlightMetric("Faturamento", Formatters.currency(state.grossRevenue), Color.Unspecified)
                        HighlightMetric("Despesas", Formatters.currency(state.totalExpenses), RedExpense)
                        HighlightMetric("Lucro real", Formatters.currency(state.netProfit), GreenProfit)
                    }
                    Spacer(Modifier.height(8.dp))
                    MetricRow("Margem de lucro", Formatters.percent(state.profitMargin))
                }
            }

            item {
                SectionCard(title = "Ganhos") {
                    MetricRow("Ganhos totais", Formatters.currency(state.totalEarnings))
                    Platform.entries.forEach { p ->
                        val v = state.earningsByPlatform[p]
                        if (v != null && v > 0) MetricRow(platformLabel(p), Formatters.currency(v))
                    }
                }
            }

            item {
                SectionCard(title = "Despesas") {
                    if (state.expensesByCategory.isEmpty()) {
                        Text("Nenhuma despesa registrada no período.")
                    } else {
                        state.expensesByCategory.entries.sortedByDescending { it.value }.forEach { (cat, v) ->
                            MetricRow(cat.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }, Formatters.currency(v))
                        }
                    }
                    MetricRow("Total de despesas", Formatters.currency(state.totalExpenses), RedExpense)
                }
            }

            item {
                SectionCard(title = "Indicadores") {
                    MetricRow("Ganho médio por dia", Formatters.currency(state.avgEarningPerDay))
                    MetricRow("Ganho médio por hora", Formatters.currency(state.avgEarningPerHour))
                    MetricRow("Lucro médio por dia", Formatters.currency(state.avgProfitPerDay))
                    MetricRow("Quilômetros rodados", "${Formatters.decimal(state.totalKm)} km")
                    MetricRow("Ganho por km", Formatters.currency(state.earningPerKm))
                    MetricRow("Custo por km", Formatters.currency(state.costPerKm))
                    MetricRow("Lucro por km", Formatters.currency(state.profitPerKm), GreenProfit)
                    MetricRow("Horas trabalhadas", "${Formatters.decimal(state.totalHours)} h")
                }
            }

            if (state.monthlyRevenueGoal > 0) {
                item {
                    SectionCard(title = "Meta do mês") {
                        val goalColor = when {
                            state.goalPercent >= 100 -> GreenProfit
                            state.goalPercent >= 70 -> Color(0xFFF9AB00)
                            else -> RedExpense
                        }
                        LinearProgressIndicator(
                            progress = { (state.goalPercent / 100.0).toFloat().coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth(),
                            color = goalColor
                        )
                        Spacer(Modifier.height(8.dp))
                        MetricRow("Meta mensal", Formatters.currency(state.monthlyRevenueGoal))
                        MetricRow("Realizado", Formatters.currency(state.goalRealized))
                        MetricRow("Falta", Formatters.currency(state.goalRemaining))
                        MetricRow("Percentual atingido", Formatters.percent(state.goalPercent))
                        MetricRow("Dias de trabalho restantes", "${state.remainingWorkDays}")
                        MetricRow("Necessário por dia", Formatters.currency(state.neededPerDay))
                    }
                }
            }

            if (state.alerts.isNotEmpty()) {
                item {
                    SectionCard(title = "Alertas") {
                        state.alerts.forEach { alert ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                                Icon(Icons.Filled.Warning, contentDescription = null, tint = Color(0xFFF9AB00))
                                Spacer(Modifier.width(8.dp))
                                Text(alert, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(72.dp)) }
        }
    }
}
