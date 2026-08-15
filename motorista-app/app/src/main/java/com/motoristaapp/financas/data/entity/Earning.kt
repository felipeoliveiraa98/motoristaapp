package com.motoristaapp.financas.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Platform { UBER, NOVENTA_E_NOVE, INDRIVE, PARTICULAR, OUTRO }

@Entity(tableName = "earnings")
data class Earning(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val platform: Platform,
    val amount: Double,
    val startTimeMillis: Long,
    val endTimeMillis: Long,
    val startKm: Double,
    val endKm: Double,
    val note: String = ""
) {
    val hoursWorked: Double
        get() = ((endTimeMillis - startTimeMillis).coerceAtLeast(0)) / 3_600_000.0

    val kmDriven: Double
        get() = (endKm - startKm).coerceAtLeast(0.0)

    val earningPerHour: Double
        get() = if (hoursWorked > 0) amount / hoursWorked else 0.0

    val earningPerKm: Double
        get() = if (kmDriven > 0) amount / kmDriven else 0.0
}
