package com.motoristaapp.financas.ui.dashboard

import com.motoristaapp.financas.data.entity.Platform

enum class PeriodFilter { HOJE, SEMANA, MES }

data class DashboardUiState(
    val period: PeriodFilter = PeriodFilter.MES,
    val totalEarnings: Double = 0.0,
    val earningsByPlatform: Map<Platform, Double> = emptyMap(),
    val totalExpenses: Double = 0.0,
    val expensesByCategory: Map<String, Double> = emptyMap(),
    val grossRevenue: Double = 0.0,
    val netProfit: Double = 0.0,
    val profitMargin: Double = 0.0,
    val avgEarningPerDay: Double = 0.0,
    val avgEarningPerHour: Double = 0.0,
    val avgProfitPerDay: Double = 0.0,
    val totalKm: Double = 0.0,
    val earningPerKm: Double = 0.0,
    val costPerKm: Double = 0.0,
    val profitPerKm: Double = 0.0,
    val totalHours: Double = 0.0,
    val daysWithRecords: Int = 0,
    // Meta
    val monthlyRevenueGoal: Double = 0.0,
    val monthlyProfitGoal: Double = 0.0,
    val goalRealized: Double = 0.0,
    val goalRemaining: Double = 0.0,
    val goalPercent: Double = 0.0,
    val remainingWorkDays: Int = 0,
    val neededPerDay: Double = 0.0,
    val alerts: List<String> = emptyList()
)
