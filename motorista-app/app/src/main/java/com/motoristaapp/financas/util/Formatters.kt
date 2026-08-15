package com.motoristaapp.financas.util

import java.text.NumberFormat
import java.util.Locale

object Formatters {
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    fun currency(value: Double): String = currencyFormat.format(value)

    fun percent(value: Double): String = String.format(Locale("pt", "BR"), "%.1f%%", value)

    fun decimal(value: Double, digits: Int = 1): String =
        String.format(Locale("pt", "BR"), "%.${digits}f", value)
}
