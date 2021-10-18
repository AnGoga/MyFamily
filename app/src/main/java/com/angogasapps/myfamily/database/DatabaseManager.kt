package com.angogasapps.myfamily.database

import android.content.Context
import com.angogasapps.myfamily.app.AppApplication.Companion.getInstance
import com.angogasapps.myfamily.utils.Async.runInNewThread
import kotlin.jvm.Volatile
import com.angogasapps.myfamily.database.AppDatabase
import com.angogasapps.myfamily.database.DatabaseManager
import androidx.room.Room
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.database.DatabaseManager.IOnEnd
import com.angogasapps.myfamily.utils.Async
import com.angogasapps.myfamily.database.UserDao
import com.angogasapps.myfamily.models.User
import java.util.ArrayList

object DatabaseManager {
    @Volatile
    var usersLoadIsEnd = false

    @Volatile
    var userList = ArrayList<User>()
        private set
    const val DATABASE_NAME = "database"

    @Volatile
    private var database: AppDatabase? = null
    private fun init(context: Context) {
        database = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
    }

    val instance: AppDatabase
        get() {
            synchronized(AppDatabase::class.java) {
                if (database == null) init(getInstance().applicationContext)
                return database!!
            }
        }

    fun comeInByDatabase(onEnd: IOnEnd) {
        runInNewThread {
            val dao = instance.userDao
            userList = ArrayList(dao.all)
            onEnd.onEnd()
        }
    }

    fun updateInfoForUsers(users: ArrayList<User>) {
        runInNewThread {
            val dao = instance.userDao
            for (user in users) {
                user.let {
                    dao.insert(it)
                }
            }
        }
    }

    fun resetDatabase() {
        instance.clearAllTables()
    }

    interface IOnEnd {
        fun onEnd()
    }
}