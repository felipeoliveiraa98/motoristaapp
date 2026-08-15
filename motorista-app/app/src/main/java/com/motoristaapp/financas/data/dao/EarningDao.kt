package com.motoristaapp.financas.data.dao

import androidx.room.*
import com.motoristaapp.financas.data.entity.Earning
import kotlinx.coroutines.flow.Flow

@Dao
interface EarningDao {
    @Insert
    suspend fun insert(earning: Earning): Long

    @Update
    suspend fun update(earning: Earning)

    @Delete
    suspend fun delete(earning: Earning)

    @Query("SELECT * FROM earnings WHERE date BETWEEN :start AND :end ORDER BY date DESC, id DESC")
    fun getBetween(start: Long, end: Long): Flow<List<Earning>>

    @Query("SELECT * FROM earnings ORDER BY date DESC, id DESC")
    fun getAll(): Flow<List<Earning>>

    @Query("SELECT * FROM earnings WHERE id = :id")
    suspend fun getById(id: Long): Earning?
}
