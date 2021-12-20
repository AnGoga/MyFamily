package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.network.firebaseImpl.families.FirebaseCreatorFamilyServiceImpl
import com.angogasapps.myfamily.network.firebaseImpl.families.FirebaseFindFamilyServiceImpl
import com.angogasapps.myfamily.network.interfaces.families.CreatorFamilyService
import com.angogasapps.myfamily.network.interfaces.families.FindFamilyService
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class FamilyDoingsModule {

    @Singleton
    @Binds
    abstract fun bindFindFamilyService(service: FirebaseFindFamilyServiceImpl): FindFamilyService

    @Singleton
    @Binds
    abstract fun bindCreatorFamilyService(service: FirebaseCreatorFamilyServiceImpl): CreatorFamilyService
}