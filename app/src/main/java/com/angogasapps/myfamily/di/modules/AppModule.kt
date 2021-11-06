package com.angogasapps.myfamily.di.modules

import android.content.Context
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.di.annotations.AppContext
import dagger.Module
import dagger.Provides

@Module(includes = [UsersAndFamiliesModule::class, DatabasesModule::class, DairyModule::class, ChatModule::class, BuyListModule::class])
class AppModule {

    @Provides
    @AppContext
    fun provideContext(): Context = AppApplication.app
}