package com.motoristaapp.financas.ui.earnings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motoristaapp.financas.data.entity.Earning
import com.motoristaapp.financas.data.entity.Platform
import com.motoristaapp.financas.data.repository.EarningRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EarningsViewModel(private val repository: EarningRepository) : ViewModel() {

    val earnings: StateFlow<List<Earning>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEarning(
        date: Long,
        platform: Platform,
        amount: Double,
        startTimeMillis: Long,
        endTimeMillis: Long,
        startKm: Double,
        endKm: Double,
        note: String,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (amount <= 0) { onError("Informe um valor ganho maior que zero."); return }
        if (endTimeMillis <= startTimeMillis) { onError("Horário final deve ser maior que o inicial."); return }
        if (endKm < startKm) { onError("Quilometragem final não pode ser menor que a inicial."); return }

        viewModelScope.launch {
            repository.insert(
                Earning(
                    date = date, platform = platform, amount = amount,
                    startTimeMillis = startTimeMillis, endTimeMillis = endTimeMillis,
                    startKm = startKm, endKm = endKm, note = note
                )
            )
            onSuccess()
        }
    }

    fun deleteEarning(earning: Earning) {
        viewModelScope.launch { repository.delete(earning) }
    }

    fun updateEarning(earning: Earning, onError: (String) -> Unit, onSuccess: () -> Unit) {
        if (earning.amount <= 0) { onError("Informe um valor ganho maior que zero."); return }
        if (earning.endTimeMillis <= earning.startTimeMillis) { onError("Horário final deve ser maior que o inicial."); return }
        if (earning.endKm < earning.startKm) { onError("Quilometragem final não pode ser menor que a inicial."); return }
        viewModelScope.launch {
            repository.update(earning)
            onSuccess()
        }
    }
}
