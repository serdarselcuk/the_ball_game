package com.allfreeapps.theballgame.utils


import android.util.Log
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale.US

class Converters {
    val formatter = SimpleDateFormat("MMM dd yyyy", US)

    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun dateToString(date: Date): String {
        return formatter.format(date)
    }

    @TypeConverter
    fun stringToDate(value: String?): Date? {
        return value?.let {
            try {
                formatter.parse(it)
            } catch (e: java.text.ParseException) {
                Log.e("Converters", "Failed to parse date: '$value'", e) // Log the pattern too for clarity
                null
            }
        }
    }

}