package com.angogasapps.myfamily.di.components

import com.angogasapps.myfamily.database.DairyDao
import com.angogasapps.myfamily.database.MessageDao
import com.angogasapps.myfamily.database.UserDao
import com.angogasapps.myfamily.di.modules.AppModule
import com.angogasapps.myfamily.network.repositories.FamilyRepository
import com.angogasapps.myfamily.network.repositories.UsersRepository
import com.angogasapps.myfamily.ui.screens.main.MainActivity
import com.angogasapps.myfamily.ui.screens.personal_dairy.DairyBuilderActivity
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: DairyBuilderActivity)
    fun inject(activity: SplashActivity)


    val familyRepository: FamilyRepository
    val messageDao: MessageDao
    val dairyDao: DairyDao
}