package com.angogasapps.myfamily.network.interfaces

import com.angogasapps.myfamily.models.User
import com.angogasapps.myfamily.network.Result
import javax.inject.Singleton

@Singleton
interface UserService {
    suspend fun getUser(id: String): Result<User>
    suspend fun getUsers(ids: List<String>): Result<List<User>>
}