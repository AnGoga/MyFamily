package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.network.firebaseImpl.families.FirebaseFamilyServiceImpl
import com.angogasapps.myfamily.network.firebaseImpl.users.FirebaseUserServiceImpl
import com.angogasapps.myfamily.network.interfaces.families.FamilyService
import com.angogasapps.myfamily.network.interfaces.users.UserService
import com.angogasapps.myfamily.network.repositories.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class UsersAndFamiliesModule {

    @Binds
    @Singleton
    abstract fun provideUserService(service: FirebaseUserServiceImpl): UserService

    @Binds
    @Singleton
    abstract fun provideFamilyService(familyService: FirebaseFamilyServiceImpl): FamilyService

}