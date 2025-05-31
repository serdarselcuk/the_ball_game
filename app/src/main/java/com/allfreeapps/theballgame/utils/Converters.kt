package com.allfreeapps.theballgame.utils

import androidx.room.TypeConverter
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

class Converters {

    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun dateToString(date: Date): String = date.toString()

    @TypeConverter
    fun stringToDate(string: String): Date? = DateFormat.getDateInstance().parse(string)

}