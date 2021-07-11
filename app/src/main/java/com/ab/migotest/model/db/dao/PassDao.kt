package com.ab.migotest.model.db.dao

import androidx.room.*
import com.ab.migotest.model.db.vo.PassItem
import com.ab.migotest.model.enums.PassType

@Dao
interface PassDao {
    @Query("SELECT * FROM pass WHERE type = :type ORDER BY value ASC")
    fun getAllByType(type: PassType): List<PassItem>

    @Delete
    fun delete(pass: PassItem)

    @Query("DELETE FROM pass")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(pass: PassItem)

    @Update
    fun update(pass: PassItem)

}