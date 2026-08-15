package com.motoristaapp.financas.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_sessions")
data class WorkSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTimeMillis: Long,
    val startKm: Double,
    val endTimeMillis: Long? = null,
    val endKm: Double? = null,
    val isActive: Boolean = true
)
