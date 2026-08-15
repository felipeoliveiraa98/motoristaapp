package com.motoristaapp.financas.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    private val monthKeyFormat = SimpleDateFormat("yyyy-MM", Locale("pt", "BR"))
    private val timeFormat = SimpleDateFormat("HH:mm", Locale("pt", "BR"))

    fun formatDate(millis: Long): String = displayFormat.format(Date(millis))
    fun formatTime(millis: Long): String = timeFormat.format(Date(millis))

    fun currentMonthKey(): String = monthKeyFormat.format(Date())
    fun monthKeyOf(millis: Long): String = monthKeyFormat.format(Date(millis))

    fun startOfDay(millis: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun endOfDay(millis: Long = System.currentTimeMillis()): Long =
        startOfDay(millis) + 24 * 60 * 60 * 1000L - 1

    fun startOfMonth(millis: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun endOfMonth(millis: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        return cal.timeInMillis
    }

    fun startOfWeek(millis: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return cal.timeInMillis
    }

    /** Quantos dias de trabalho (conforme máscara) restam no mês a partir de hoje (inclusive). */
    fun remainingWorkDaysInMonth(workDaysMask: Int, from: Long = System.currentTimeMillis()): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = from
        val lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val today = cal.get(Calendar.DAY_OF_MONTH)
        var count = 0
        val temp = Calendar.getInstance()
        temp.timeInMillis = from
        for (d in today..lastDay) {
            temp.set(Calendar.DAY_OF_MONTH, d)
            val dow = temp.get(Calendar.DAY_OF_WEEK) // 1=Domingo .. 7=Sábado
            if ((workDaysMask shr (dow - 1)) and 1 == 1) count++
        }
        return count
    }

    fun isWorkDaySelected(workDaysMask: Int, dayOfWeek: Int): Boolean =
        (workDaysMask shr (dayOfWeek - 1)) and 1 == 1
}
