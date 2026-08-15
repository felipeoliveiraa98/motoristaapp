package com.motoristaapp.financas.data.repository

import com.motoristaapp.financas.data.dao.GoalDao
import com.motoristaapp.financas.data.entity.Goal
import kotlinx.coroutines.flow.Flow

class GoalRepository(private val dao: GoalDao) {
    fun observe(yearMonth: String): Flow<Goal?> = dao.observe(yearMonth)
    suspend fun getByMonth(yearMonth: String): Goal? = dao.getByMonth(yearMonth)
    suspend fun upsert(goal: Goal) = dao.upsert(goal)
}
