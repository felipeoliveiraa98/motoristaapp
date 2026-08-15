package com.motoristaapp.financas.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motoristaapp.financas.data.entity.Earning
import com.motoristaapp.financas.data.entity.Expense
import com.motoristaapp.financas.data.repository.EarningRepository
import com.motoristaapp.financas.data.repository.ExpenseRepository
import com.motoristaapp.financas.data.repository.GoalRepository
import com.motoristaapp.financas.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val earningRepository: EarningRepository,
    private val expenseRepository: ExpenseRepository,
    private val goalRepository: GoalRepository
) : ViewModel() {

    private val _period = MutableStateFlow(PeriodFilter.MES)
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    fun setPeriod(period: PeriodFilter) {
        _period.value = period
        observeData()
    }

    private fun rangeFor(period: PeriodFilter): Pair<Long, Long> = when (period) {
        PeriodFilter.HOJE -> DateUtils.startOfDay() to DateUtils.endOfDay()
        PeriodFilter.SEMANA -> DateUtils.startOfWeek() to DateUtils.endOfDay()
        PeriodFilter.MES -> DateUtils.startOfMonth() to DateUtils.endOfMonth()
    }

    private fun observeData() {
        val (start, end) = rangeFor(_period.value)
        viewModelScope.launch {
            combine(
                earningRepository.getBetween(start, end),
                expenseRepository.getBetween(start, end),
                goalRepository.observe(DateUtils.currentMonthKey())
            ) { earnings, expenses, goal ->
                buildState(earnings, expenses, goal?.monthlyRevenueGoal ?: 0.0, goal?.monthlyProfitGoal ?: 0.0, goal?.workDaysMask ?: 0b1111100, goal?.manualDailyGoal)
            }.collect { state ->
                _uiState.value = state.copy(period = _period.value)
            }
        }
    }

    private fun buildState(
        earnings: List<Earning>,
        expenses: List<Expense>,
        monthlyRevenueGoal: Double,
        monthlyProfitGoal: Double,
        workDaysMask: Int,
        manualDailyGoal: Double?
    ): DashboardUiState {
        val totalEarnings = earnings.sumOf { it.amount }
        val earningsByPlatform = earnings.groupBy { it.platform }.mapValues { it.value.sumOf { e -> e.amount } }
        val totalExpenses = expenses.sumOf { it.amount }
        val expensesByCategory = expenses.groupBy { it.category.name }.mapValues { it.value.sumOf { e -> e.amount } }

        val netProfit = totalEarnings - totalExpenses
        val profitMargin = if (totalEarnings > 0) (netProfit / totalEarnings) * 100.0 else 0.0

        val daysWithRecords = (earnings.map { DateUtils.formatDate(it.date) } +
                expenses.map { DateUtils.formatDate(it.date) }).toSet().size.coerceAtLeast(0)

        val totalHours = earnings.sumOf { it.hoursWorked }
        val totalKm = earnings.sumOf { it.kmDriven }

        val avgEarningPerDay = if (daysWithRecords > 0) totalEarnings / daysWithRecords else 0.0
        val avgEarningPerHour = if (totalHours > 0) totalEarnings / totalHours else 0.0
        val avgProfitPerDay = if (daysWithRecords > 0) netProfit / daysWithRecords else 0.0

        val earningPerKm = if (totalKm > 0) totalEarnings / totalKm else 0.0
        val costPerKm = if (totalKm > 0) totalExpenses / totalKm else 0.0
        val profitPerKm = if (totalKm > 0) netProfit / totalKm else 0.0

        // Metas (sempre calculadas com base no faturamento do mês corrente para clareza,
        // mesmo se o período selecionado for outro)
        val remainingWorkDays = DateUtils.remainingWorkDaysInMonth(workDaysMask)
        val goalRemaining = (monthlyRevenueGoal - totalEarnings).coerceAtLeast(0.0)
        val goalPercent = if (monthlyRevenueGoal > 0) (totalEarnings / monthlyRevenueGoal) * 100.0 else 0.0
        val neededPerDay = manualDailyGoal ?: if (remainingWorkDays > 0) goalRemaining / remainingWorkDays else 0.0

        val alerts = buildList {
            if (monthlyRevenueGoal > 0) {
                val todayGoalGap = neededPerDay - avgEarningPerDay
                if (todayGoalGap > 0) {
                    add("Você está ${formatBrl(todayGoalGap)} abaixo do ritmo necessário por dia.")
                }
                add("Para atingir sua meta mensal, você precisa fazer ${formatBrl(neededPerDay)} por dia.")
            }
            if (avgEarningPerHour > 0) {
                add("Seu ganho médio por hora neste período é ${formatBrl(avgEarningPerHour)}.")
            }
        }

        return DashboardUiState(
            totalEarnings = totalEarnings,
            earningsByPlatform = earningsByPlatform,
            totalExpenses = totalExpenses,
            expensesByCategory = expensesByCategory,
            grossRevenue = totalEarnings,
            netProfit = netProfit,
            profitMargin = profitMargin,
            avgEarningPerDay = avgEarningPerDay,
            avgEarningPerHour = avgEarningPerHour,
            avgProfitPerDay = avgProfitPerDay,
            totalKm = totalKm,
            earningPerKm = earningPerKm,
            costPerKm = costPerKm,
            profitPerKm = profitPerKm,
            totalHours = totalHours,
            daysWithRecords = daysWithRecords,
            monthlyRevenueGoal = monthlyRevenueGoal,
            monthlyProfitGoal = monthlyProfitGoal,
            goalRealized = totalEarnings,
            goalRemaining = goalRemaining,
            goalPercent = goalPercent,
            remainingWorkDays = remainingWorkDays,
            neededPerDay = neededPerDay,
            alerts = alerts
        )
    }

    private fun formatBrl(value: Double) = com.motoristaapp.financas.util.Formatters.currency(value)
}
