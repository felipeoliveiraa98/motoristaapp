package com.motoristaapp.financas.data.dao

import androidx.room.*
import com.motoristaapp.financas.data.entity.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Upsert
    suspend fun upsert(goal: Goal)

    @Query("SELECT * FROM goals WHERE yearMonth = :yearMonth")
    fun observe(yearMonth: String): Flow<Goal?>

    @Query("SELECT * FROM goals WHERE yearMonth = :yearMonth")
    suspend fun getByMonth(yearMonth: String): Goal?
}
