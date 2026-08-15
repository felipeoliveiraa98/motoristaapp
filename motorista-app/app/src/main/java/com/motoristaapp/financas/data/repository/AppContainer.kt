package com.motoristaapp.financas.data.repository

import android.content.Context
import com.motoristaapp.financas.data.AppDatabase

/**
 * Container simples de injeção de dependências (sem Hilt para manter o MVP leve).
 */
class AppContainer(context: Context) {
    private val db = AppDatabase.getInstance(context)

    val earningRepository by lazy { EarningRepository(db.earningDao()) }
    val expenseRepository by lazy { ExpenseRepository(db.expenseDao()) }
    val goalRepository by lazy { GoalRepository(db.goalDao()) }
    val vehicleRepository by lazy { VehicleRepository(db.vehicleDao()) }
    val workSessionRepository by lazy { WorkSessionRepository(db.workSessionDao()) }
}
