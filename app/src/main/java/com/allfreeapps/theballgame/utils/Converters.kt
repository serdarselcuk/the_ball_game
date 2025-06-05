package com.allfreeapps.theballgame.utils


import android.util.Log
import androidx.room.TypeConverter
import java.util.Locale.US
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class Converters {
    private val patternToDate = "EEE MMM dd HH:mm:ss zzz yyyy"
    private val patternToString = "MMM dd yyyy"

    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun dateToString(date: Date): String {
        val formatter = SimpleDateFormat(patternToString, US)
        return formatter.format(date)
    }

    @TypeConverter
    fun stringToDate(value: String?): Date? {
        return value?.let {


            val formatter = SimpleDateFormat(patternToDate, US)
            try {
                formatter.parse(it)
            } catch (e: java.text.ParseException) {
                Log.e("Converters", "Failed to parse date: $value", e)
                null
            }
        }
    }

}