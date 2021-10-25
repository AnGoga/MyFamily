package com.angogasapps.myfamily.network.repositories

import com.angogasapps.myfamily.models.User
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.UserService
import javax.inject.Inject

class UsersRepository @Inject constructor(val userService: UserService) : UserService {
    override suspend fun getUser(id: String): Result<User> {
        return userService.getUser(id)
    }

    override suspend fun getUsers(ids: List<String>): Result<List<User>> {
        return userService.getUsers(ids)
    }

}