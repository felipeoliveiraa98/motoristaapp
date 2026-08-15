package com.motoristaapp.financas.data.dao

import androidx.room.*
import com.motoristaapp.financas.data.entity.WorkSession
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkSessionDao {
    @Insert
    suspend fun insert(session: WorkSession): Long

    @Update
    suspend fun update(session: WorkSession)

    @Query("SELECT * FROM work_sessions WHERE isActive = 1 LIMIT 1")
    fun observeActive(): Flow<WorkSession?>

    @Query("SELECT * FROM work_sessions WHERE isActive = 1 LIMIT 1")
    suspend fun getActive(): WorkSession?
}
