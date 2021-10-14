package com.angogasapps.myfamily.database

import androidx.room.*
import com.angogasapps.myfamily.models.Message

@Dao
interface MessageDao {
    @get:Query("SELECT * FROM Message")
    val all: List<Message>

    @Query("SELECT * FROM Message WHERE id = :id")
    fun getById(id: String): Message

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message)

    @Update
    fun update(message: Message)

    @Delete
    fun delete(message: Message)
}