package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.network.firebaseImpl.family_storage.FirebaseFamilyStorageServiceImpl
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FamilyStorageModule {

    @Provides
    @Singleton
    fun provideFamilyStorageService(service: FirebaseFamilyStorageServiceImpl): FamilyStorageService =
        service
}