package com.motoristaapp.financas.data.dao

import androidx.room.*
import com.motoristaapp.financas.data.entity.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Upsert
    suspend fun upsert(vehicle: Vehicle)

    @Query("SELECT * FROM vehicle WHERE id = 1")
    fun observe(): Flow<Vehicle?>

    @Query("SELECT * FROM vehicle WHERE id = 1")
    suspend fun get(): Vehicle?
}
