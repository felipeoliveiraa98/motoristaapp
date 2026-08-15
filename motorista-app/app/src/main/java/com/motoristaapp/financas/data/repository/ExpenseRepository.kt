package com.motoristaapp.financas.data.repository

import com.motoristaapp.financas.data.dao.ExpenseDao
import com.motoristaapp.financas.data.entity.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {
    fun getBetween(start: Long, end: Long): Flow<List<Expense>> = dao.getBetween(start, end)
    fun getAll(): Flow<List<Expense>> = dao.getAll()
    suspend fun insert(expense: Expense) = dao.insert(expense)
    suspend fun update(expense: Expense) = dao.update(expense)
    suspend fun delete(expense: Expense) = dao.delete(expense)
}
