package com.angogasapps.myfamily.network.repositories

import com.angogasapps.myfamily.models.User
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
        private val userService: UserService
        //val userDao: UserDao
    ) {

    suspend fun getUser(id: String): Result<User> {
        return userService.getUser(id)
    }

    suspend fun getUsers(ids: List<String>): Result<List<User>> {
        return userService.getUsers(ids)
    }

}