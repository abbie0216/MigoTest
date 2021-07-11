package com.ab.migotest.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ab.migotest.model.db.dao.PassDao
import com.ab.migotest.model.db.vo.PassItem
import com.ab.migotest.model.db.converter.DateConverter
import com.ab.migotest.model.db.converter.PassTypeConverter

@Database(
    entities = [
        PassItem::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class, PassTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun passDao(): PassDao
}