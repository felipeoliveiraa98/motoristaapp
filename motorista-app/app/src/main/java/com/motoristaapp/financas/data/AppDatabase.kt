package com.motoristaapp.financas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.motoristaapp.financas.data.dao.EarningDao
import com.motoristaapp.financas.data.dao.ExpenseDao
import com.motoristaapp.financas.data.dao.GoalDao
import com.motoristaapp.financas.data.dao.VehicleDao
import com.motoristaapp.financas.data.dao.WorkSessionDao
import com.motoristaapp.financas.data.entity.Earning
import com.motoristaapp.financas.data.entity.Expense
import com.motoristaapp.financas.data.entity.Goal
import com.motoristaapp.financas.data.entity.Vehicle
import com.motoristaapp.financas.data.entity.WorkSession

@Database(
    entities = [Earning::class, Expense::class, Goal::class, Vehicle::class, WorkSession::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun earningDao(): EarningDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun goalDao(): GoalDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun workSessionDao(): WorkSessionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "motorista_financas.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
