package com.motoristaapp.financas.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

/** Factory simples para injetar dependências dos repositories nas ViewModels sem Hilt. */
inline fun <VM : ViewModel> viewModelFactory(crossinline create: () -> VM): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = create() as T
    }
