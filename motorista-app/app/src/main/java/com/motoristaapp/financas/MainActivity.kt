package com.motoristaapp.financas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.motoristaapp.financas.ui.navigation.AppNavHost
import com.motoristaapp.financas.ui.theme.MotoristaFinancasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as MotoristaApp).container

        setContent {
            MotoristaFinancasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavHost(container)
                }
            }
        }
    }
}
