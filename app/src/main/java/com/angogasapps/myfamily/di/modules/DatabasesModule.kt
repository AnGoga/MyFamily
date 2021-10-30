package com.angogasapps.myfamily.di.modules

import android.content.Context
import androidx.room.Room
import com.angogasapps.myfamily.database.*
import com.angogasapps.myfamily.di.annotations.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabasesModule {

    @Provides
    @Singleton
    fun provideDatabase(@AppContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DatabaseManager.DATABASE_NAME)
            .build()

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao

    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao = database.messageDao

    @Provides
    fun provideDairyDao(database: AppDatabase): DairyDao = database.dairyDao
}