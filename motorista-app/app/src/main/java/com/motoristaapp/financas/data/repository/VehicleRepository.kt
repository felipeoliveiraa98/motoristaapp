package com.motoristaapp.financas.data.repository

import com.motoristaapp.financas.data.dao.VehicleDao
import com.motoristaapp.financas.data.entity.Vehicle
import kotlinx.coroutines.flow.Flow

class VehicleRepository(private val dao: VehicleDao) {
    fun observe(): Flow<Vehicle?> = dao.observe()
    suspend fun get(): Vehicle? = dao.get()
    suspend fun upsert(vehicle: Vehicle) = dao.upsert(vehicle)
}
