package com.motoristaapp.financas

import android.app.Application
import com.motoristaapp.financas.data.repository.AppContainer

class MotoristaApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
