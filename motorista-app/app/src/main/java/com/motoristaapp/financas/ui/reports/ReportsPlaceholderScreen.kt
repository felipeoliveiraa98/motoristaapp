package com.motoristaapp.financas.ui.reports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Etapa 2 do projeto: Relatórios com gráficos, comparação Uber x 99, veículo,
 * previsão de fechamento e backup/exportação. Ainda não implementado nesta
 * primeira entrega (MVP). Ver mensagem de acompanhamento para o roadmap.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsPlaceholderScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Relatórios") }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Relatórios e gráficos chegam na Etapa 2 do desenvolvimento.")
        }
    }
}
