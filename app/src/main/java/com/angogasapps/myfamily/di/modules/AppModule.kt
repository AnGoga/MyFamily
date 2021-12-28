package com.angogasapps.myfamily.di.modules

import android.app.Application
import android.content.Context
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.di.annotations.AppContext
import com.angogasapps.myfamily.di.modules.network.NetworkModule
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(
    includes = [
//        AppModule.BindsAppModule::class,
        UsersAndFamiliesModule::class, DatabasesModule::class, DairyModule::class,
        ChatModule::class, BuyListModule::class, FamilyStorageModule::class,
        NewsCenterModule::class, FamilyDoingsModule::class, NetworkModule::class
    ]
)
class AppModule {

    @Provides
    fun provideApplication(): Application = AppApplication.app

    @Provides
    fun provideContext(app: Application): Context = app

    @Provides
    @AppContext
    fun provideAppContext(app: Application): Context = app
    

//    @Module
//    interface BindsAppModule {
//        @Binds
//        @AppContext
//        abstract fun bindContext(app: Application): Context
//
//    }
}