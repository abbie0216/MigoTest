package com.ab.migotest.model.db.converter

import androidx.room.TypeConverter
import com.ab.migotest.model.enums.PassType

class PassTypeConverter {
    @TypeConverter
    fun toPassType(value: Int?): PassType? {
        return value?.let {
            when (value) {
                0 -> PassType.DAY
                else -> PassType.HOUR
            }
        }
    }

    @TypeConverter
    fun fromPassType(type: PassType?): Int? {
        return type?.let {
            when (type) {
                PassType.DAY -> 0
                PassType.HOUR -> 1
            }
        }
    }
}