package com.motoristaapp.financas.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ExpenseCategory {
    COMBUSTIVEL, MANUTENCAO, PNEUS, OLEO, SEGURO, IPVA, FINANCIAMENTO,
    PEDAGIO, ESTACIONAMENTO, LAVAGEM, ALIMENTACAO, CELULAR_INTERNET, MULTAS, OUTROS
}

enum class PaymentMethod { DINHEIRO, DEBITO, CREDITO, PIX, OUTRO }

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val category: ExpenseCategory,
    val amount: Double,
    val paymentMethod: PaymentMethod,
    val odometerKm: Double? = null,
    val note: String = "",
    val fuelLiters: Double? = null,
    val fuelPricePerLiter: Double? = null,
    val fuelType: String? = null
)
