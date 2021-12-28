package com.angogasapps.myfamily.database

import androidx.room.Database
import com.angogasapps.myfamily.models.DairyObject
import androidx.room.RoomDatabase
import com.angogasapps.myfamily.database.UserDao
import com.angogasapps.myfamily.database.MessageDao
import com.angogasapps.myfamily.database.DairyDao
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.models.User

@Database(entities = [User::class, Message::class, DairyObject::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val messageDao: MessageDao
    abstract val dairyDao: DairyDao
}