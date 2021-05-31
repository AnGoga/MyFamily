package com.angogasapps.myfamily.database

import androidx.room.*
import com.angogasapps.myfamily.models.DairyObject
import com.angogasapps.myfamily.models.User

@Dao
interface DairyDao {
    @Query("SELECT * FROM DairyObject")
    fun getAll(): List<DairyObject?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dairy: DairyObject?)

    @Update
    fun update(dairy: DairyObject?)

    @Delete
    fun delete(dairy: DairyObject?)
}