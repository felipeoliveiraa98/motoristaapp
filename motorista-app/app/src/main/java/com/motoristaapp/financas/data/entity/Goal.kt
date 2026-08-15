package com.motoristaapp.financas.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey val yearMonth: String,
    val monthlyRevenueGoal: Double = 0.0,
    val monthlyProfitGoal: Double = 0.0,
    val manualDailyGoal: Double? = null,
    val workDaysMask: Int = 0b1111100
)
