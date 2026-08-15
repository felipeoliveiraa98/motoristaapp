package com.motoristaapp.financas.data.dao

import androidx.room.*
import com.motoristaapp.financas.data.entity.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM expenses WHERE date BETWEEN :start AND :end ORDER BY date DESC, id DESC")
    fun getBetween(start: Long, end: Long): Flow<List<Expense>>

    @Query("SELECT * FROM expenses ORDER BY date DESC, id DESC")
    fun getAll(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getById(id: Long): Expense?
}
