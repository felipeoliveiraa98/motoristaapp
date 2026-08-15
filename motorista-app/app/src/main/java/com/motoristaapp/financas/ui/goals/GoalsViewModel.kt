package com.motoristaapp.financas.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motoristaapp.financas.data.entity.Goal
import com.motoristaapp.financas.data.repository.EarningRepository
import com.motoristaapp.financas.data.repository.ExpenseRepository
import com.motoristaapp.financas.data.repository.GoalRepository
import com.motoristaapp.financas.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GoalsUiState(
    val monthlyRevenueGoal: Double = 0.0,
    val monthlyProfitGoal: Double = 0.0,
    val manualDailyGoal: Double? = null,
    val workDaysMask: Int = 0b1111100,
    val realizedRevenue: Double = 0.0,
    val realizedProfit: Double = 0.0,
    val remainingWorkDays: Int = 0,
    val neededPerDayRevenue: Double = 0.0,
    val neededPerDayProfit: Double = 0.0,
    val percentRevenue: Double = 0.0,
    val percentProfit: Double = 0.0
)

class GoalsViewModel(
    private val goalRepository: GoalRepository,
    private val earningRepository: EarningRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val monthKey = DateUtils.currentMonthKey()

    val uiState: StateFlow<GoalsUiState> = combine(
        goalRepository.observe(monthKey),
        earningRepository.getBetween(DateUtils.startOfMonth(), DateUtils.endOfMonth()),
        expenseRepository.getBetween(DateUtils.startOfMonth(), DateUtils.endOfMonth())
    ) { goal, earnings, expenses ->
        val revenue = earnings.sumOf { it.amount }
        val expensesTotal = expenses.sumOf { it.amount }
        val profit = revenue - expensesTotal
        val mask = goal?.workDaysMask ?: 0b1111100
        val remainingDays = DateUtils.remainingWorkDaysInMonth(mask)
        val revenueGoal = goal?.monthlyRevenueGoal ?: 0.0
        val profitGoal = goal?.monthlyProfitGoal ?: 0.0
        val remainingRevenue = (revenueGoal - revenue).coerceAtLeast(0.0)
        val remainingProfit = (profitGoal - profit).coerceAtLeast(0.0)

        GoalsUiState(
            monthlyRevenueGoal = revenueGoal,
            monthlyProfitGoal = profitGoal,
            manualDailyGoal = goal?.manualDailyGoal,
            workDaysMask = mask,
            realizedRevenue = revenue,
            realizedProfit = profit,
            remainingWorkDays = remainingDays,
            neededPerDayRevenue = goal?.manualDailyGoal ?: if (remainingDays > 0) remainingRevenue / remainingDays else 0.0,
            neededPerDayProfit = if (remainingDays > 0) remainingProfit / remainingDays else 0.0,
            percentRevenue = if (revenueGoal > 0) (revenue / revenueGoal) * 100.0 else 0.0,
            percentProfit = if (profitGoal > 0) (profit / profitGoal) * 100.0 else 0.0
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GoalsUiState())

    fun saveGoals(revenueGoal: Double, profitGoal: Double, manualDaily: Double?, workDaysMask: Int) {
        viewModelScope.launch {
            goalRepository.upsert(
                Goal(
                    yearMonth = monthKey,
                    monthlyRevenueGoal = revenueGoal,
                    monthlyProfitGoal = profitGoal,
                    manualDailyGoal = manualDaily,
                    workDaysMask = workDaysMask
                )
            )
        }
    }
}
