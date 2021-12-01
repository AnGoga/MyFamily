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

//    @Provides
//    @Singleton
//    fun provideUsersRepository(
//        userService: UserService,
//        userDao: UserDao
//    ): UsersRepository = UsersRepository(userService, userDao)

    @Binds
    @Singleton
    abstract fun provideFamilyService(usersRepository: UsersRepository): FamilyService

//    @Provides
//    @Singleton
//    fun provideFamilyRepository(
//        familyService: FamilyService,
//        userDao: UserDao
//    ) : FamilyRepository = FamilyRepository(familyService, userDao)

//    @Provides
//    @Singleton
//    fun provideFindFamilyService(): FindFamilyService = FirebaseFindFamilyServiceImpl()

//    @Provides
//    @Singleton
//    fun provideCreatorFamilyService(): CreatorFamilyService = FirebaseCreatorFamilyServiceImpl()

}