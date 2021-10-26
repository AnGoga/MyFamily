package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.di.annotations.FamilyUsersScope
import com.angogasapps.myfamily.network.firebaseImpl.FirebaseFamilyServiceImpl
import com.angogasapps.myfamily.network.firebaseImpl.FirebaseUserServiceImpl
import com.angogasapps.myfamily.network.interfaces.FamilyService
import com.angogasapps.myfamily.network.interfaces.UserService
import com.angogasapps.myfamily.network.repositories.FamilyRepository
import com.angogasapps.myfamily.network.repositories.UsersRepository
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class UsersModule {

    @Provides
    @Singleton
    fun provideUserService(): UserService = FirebaseUserServiceImpl()

    @Provides
    @Singleton
    fun provideUsersRepository(
        userService: UserService
    ): UsersRepository = UsersRepository(userService)

    @Provides
    @Singleton
    fun provideFamilyService(
        usersRepository: UsersRepository
    ): FamilyService = FirebaseFamilyServiceImpl(usersRepository)

    @Provides
    @Singleton
    fun provideFamilyRepository(
        familyService: FamilyService
    ) : FamilyRepository = FamilyRepository(familyService)


}