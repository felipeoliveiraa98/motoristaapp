package com.motoristaapp.financas.data.repository

import com.motoristaapp.financas.data.dao.EarningDao
import com.motoristaapp.financas.data.entity.Earning
import kotlinx.coroutines.flow.Flow

class EarningRepository(private val dao: EarningDao) {
    fun getBetween(start: Long, end: Long): Flow<List<Earning>> = dao.getBetween(start, end)
    fun getAll(): Flow<List<Earning>> = dao.getAll()
    suspend fun insert(earning: Earning) = dao.insert(earning)
    suspend fun update(earning: Earning) = dao.update(earning)
    suspend fun delete(earning: Earning) = dao.delete(earning)
}
