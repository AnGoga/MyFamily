package com.angogasapps.myfamily.di.modules

import com.angogasapps.myfamily.database.UserDao
import com.angogasapps.myfamily.network.firebaseImpl.FirebaseCreatorFamilyServiceImpl
import com.angogasapps.myfamily.network.firebaseImpl.FirebaseFamilyServiceImpl
import com.angogasapps.myfamily.network.firebaseImpl.FirebaseFindFamilyServiceImpl
import com.angogasapps.myfamily.network.firebaseImpl.FirebaseUserServiceImpl
import com.angogasapps.myfamily.network.interfaces.CreatorFamilyService
import com.angogasapps.myfamily.network.interfaces.FamilyService
import com.angogasapps.myfamily.network.interfaces.FindFamilyService
import com.angogasapps.myfamily.network.interfaces.UserService
import com.angogasapps.myfamily.network.repositories.FamilyRepository
import com.angogasapps.myfamily.network.repositories.UsersRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class UsersAndFamiliesModule {

    @Provides
    @Singleton
    fun provideUserService(): UserService = FirebaseUserServiceImpl()

//    @Provides
//    @Singleton
//    fun provideUsersRepository(
//        userService: UserService,
//        userDao: UserDao
//    ): UsersRepository = UsersRepository(userService, userDao)

    @Provides
    @Singleton
    fun provideFamilyService(
        usersRepository: UsersRepository
    ): FamilyService = FirebaseFamilyServiceImpl(usersRepository)

//    @Provides
//    @Singleton
//    fun provideFamilyRepository(
//        familyService: FamilyService,
//        userDao: UserDao
//    ) : FamilyRepository = FamilyRepository(familyService, userDao)

    @Provides
    @Singleton
    fun provideFindFamilyService(): FindFamilyService = FirebaseFindFamilyServiceImpl()

    @Provides
    @Singleton
    fun provideCreatorFamilyService(): CreatorFamilyService = FirebaseCreatorFamilyServiceImpl()

}