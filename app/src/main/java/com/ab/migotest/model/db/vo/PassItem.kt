package com.ab.migotest.model.db.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ab.migotest.model.enums.PassType
import java.io.Serializable
import java.util.*

@Entity(tableName = "pass")
data class PassItem(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "type")
    val type: PassType = PassType.DAY,

    @ColumnInfo(name = "value")
    val value: Int = 0,

    @ColumnInfo(name = "price")
    var price: Int = 0,

    @ColumnInfo(name = "is_activated")
    var isActivated: Boolean = false,

    @ColumnInfo(name = "create_at")
    var createAt: Date = Date(),

    @ColumnInfo(name = "activate_at")
    var activateAt: Date = Date(),

    @ColumnInfo(name = "expire_at")
    var expireAt: Date = Date(),

    ) : Serializable
