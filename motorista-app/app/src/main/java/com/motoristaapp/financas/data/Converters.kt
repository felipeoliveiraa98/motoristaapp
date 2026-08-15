package com.motoristaapp.financas.data

import androidx.room.TypeConverter
import com.motoristaapp.financas.data.entity.ExpenseCategory
import com.motoristaapp.financas.data.entity.PaymentMethod
import com.motoristaapp.financas.data.entity.Platform

class Converters {
    @TypeConverter
    fun fromPlatform(value: Platform): String = value.name

    @TypeConverter
    fun toPlatform(value: String): Platform = Platform.valueOf(value)

    @TypeConverter
    fun fromCategory(value: ExpenseCategory): String = value.name

    @TypeConverter
    fun toCategory(value: String): ExpenseCategory = ExpenseCategory.valueOf(value)

    @TypeConverter
    fun fromPayment(value: PaymentMethod): String = value.name

    @TypeConverter
    fun toPayment(value: String): PaymentMethod = PaymentMethod.valueOf(value)
}
