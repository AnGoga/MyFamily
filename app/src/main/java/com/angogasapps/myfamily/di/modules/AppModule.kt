package com.angogasapps.myfamily.di.modules

import android.content.Context
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.di.annotations.AppContext
import com.angogasapps.myfamily.ui.screens.personal_dairy.DairyDatabaseManager
import dagger.Module
import dagger.Provides

@Module(includes = [UsersModule::class, DatabasesModule::class, DairyModule::class, ChatModule::class])
class AppModule {

    @Provides
    @AppContext
    fun provideContext(): Context = AppApplication.app
}