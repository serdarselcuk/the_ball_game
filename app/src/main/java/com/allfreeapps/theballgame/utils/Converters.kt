package com.allfreeapps.theballgame.utils


import android.util.Log
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale.US

class Converters {
    private val new_formatter = SimpleDateFormat("MMM dd yyyy", US)
    private val old_formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", US)

    @TypeConverter
    fun dateToString(date: Date): String {
        return new_formatter.format(date)
    }

    @TypeConverter
    fun stringToDate(value: String?): Date? {
        return value?.let {
            try {
                new_formatter.parse(it)
            } catch (e: java.text.ParseException) {
                Log.d("Converters", "Failed to parse date: '$value'", e)
                try {
                    old_formatter.parse(it)
                }catch (e2: java.text.ParseException){
                    Log.e("Converters", "Failed to parse date: '$value'", e2)
                    null
                }
            }
        }
    }

}