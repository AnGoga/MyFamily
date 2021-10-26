package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.network.firebaseImpl.FirebaseFamilyServiceImpl
import com.angogasapps.myfamily.network.firebaseImpl.FirebaseUserServiceImpl
import com.angogasapps.myfamily.network.interfaces.FamilyService
import com.angogasapps.myfamily.network.interfaces.UserService
import com.angogasapps.myfamily.network.repositories.FamilyRepository
import com.angogasapps.myfamily.network.repositories.UsersRepository
import dagger.Component
import dagger.Module
import dagger.Provides


@Module
class UsersModule {

    @Provides
    fun provideUserService(): UserService = FirebaseUserServiceImpl()

    @Provides
    fun provideUsersRepository(
        userService: UserService
    ): UsersRepository = UsersRepository(userService)

    @Provides
    fun provideFamilyService(
        usersRepository: UsersRepository
    ): FamilyService = FirebaseFamilyServiceImpl(usersRepository)

    @Provides
    fun provideFamilyRepository(
        familyService: FamilyService
    ) : FamilyRepository = FamilyRepository(familyService)


}