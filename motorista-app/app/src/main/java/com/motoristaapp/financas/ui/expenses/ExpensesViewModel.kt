package com.motoristaapp.financas.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motoristaapp.financas.data.entity.Expense
import com.motoristaapp.financas.data.entity.ExpenseCategory
import com.motoristaapp.financas.data.entity.PaymentMethod
import com.motoristaapp.financas.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpensesViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val expenses: StateFlow<List<Expense>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addExpense(
        date: Long,
        category: ExpenseCategory,
        amount: Double,
        paymentMethod: PaymentMethod,
        odometerKm: Double?,
        note: String,
        fuelLiters: Double?,
        fuelPricePerLiter: Double?,
        fuelType: String?,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (amount <= 0) { onError("Informe um valor de despesa maior que zero."); return }
        if (category == ExpenseCategory.COMBUSTIVEL && (fuelLiters == null || fuelLiters <= 0)) {
            onError("Informe a quantidade de litros abastecidos."); return
        }

        viewModelScope.launch {
            repository.insert(
                Expense(
                    date = date, category = category, amount = amount, paymentMethod = paymentMethod,
                    odometerKm = odometerKm, note = note,
                    fuelLiters = fuelLiters, fuelPricePerLiter = fuelPricePerLiter, fuelType = fuelType
                )
            )
            onSuccess()
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch { repository.delete(expense) }
    }
}
