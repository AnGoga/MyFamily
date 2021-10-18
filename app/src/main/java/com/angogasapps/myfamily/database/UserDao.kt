package com.angogasapps.myfamily.database

import androidx.room.*
import com.angogasapps.myfamily.models.User

@Dao
interface UserDao {
    @get:Query("SELECT * FROM User")
    val all: List<User>

    @Query("SELECT * FROM User WHERE id = :id")
    fun getById(id: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)
}