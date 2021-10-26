package com.angogasapps.myfamily.di.components

import com.angogasapps.myfamily.di.modules.AppModule
import com.angogasapps.myfamily.network.repositories.FamilyRepository
import com.angogasapps.myfamily.ui.screens.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)



    val familyRepository: FamilyRepository
}