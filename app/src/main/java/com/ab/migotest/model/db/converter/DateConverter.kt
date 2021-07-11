package com.ab.migotest.model.db.converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            format.parse(value)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.let {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            format.format(date)
        }
    }
}