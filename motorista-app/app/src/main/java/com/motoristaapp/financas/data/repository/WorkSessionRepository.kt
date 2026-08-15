package com.motoristaapp.financas.data.repository

import com.motoristaapp.financas.data.dao.WorkSessionDao
import com.motoristaapp.financas.data.entity.WorkSession
import kotlinx.coroutines.flow.Flow

class WorkSessionRepository(private val dao: WorkSessionDao) {
    fun observeActive(): Flow<WorkSession?> = dao.observeActive()
    suspend fun getActive(): WorkSession? = dao.getActive()
    suspend fun insert(session: WorkSession): Long = dao.insert(session)
    suspend fun update(session: WorkSession) = dao.update(session)
}
