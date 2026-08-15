package com.motoristaapp.financas.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle")
data class Vehicle(
    @PrimaryKey val id: Int = 1,
    val brand: String = "",
    val model: String = "",
    val year: Int = 0,
    val fuelType: String = "",
    val avgConsumptionKmPerLiter: Double = 0.0,
    val vehicleValue: Double = 0.0,
    val currentKm: Double = 0.0,
    val monthlyFinancing: Double = 0.0,
    val monthlyInsurance: Double = 0.0,
    val monthlyIpva: Double = 0.0,
    val monthlyMaintenance: Double = 0.0,
    val monthlyInternet: Double = 0.0,
    val monthlyOther: Double = 0.0
) {
    val monthlyFixedCost: Double
        get() = monthlyFinancing + monthlyInsurance + monthlyIpva + monthlyMaintenance + monthlyInternet + monthlyOther

    val dailyFixedCost: Double
        get() = monthlyFixedCost / 30.0
}
