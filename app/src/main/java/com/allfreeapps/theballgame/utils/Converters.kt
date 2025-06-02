package com.allfreeapps.theballgame.utils


import android.util.Log
import androidx.room.TypeConverter
import java.util.Locale.US
import java.text.SimpleDateFormat
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
    fun stringToDate(value: String?): Date? {
        return value?.let {

            val pattern = "EEE MMM dd HH:mm:ss zzz yyyy"
            val formatter = SimpleDateFormat(pattern, US)
            try {
                formatter.parse(it)
            } catch (e: java.text.ParseException) {
                Log.e("Converters", "Failed to parse date: $value", e)
                null
            }
        }
    }

}