package com.angogasapps.myfamily.network.interfaces

import com.angogasapps.myfamily.models.User

interface UserService {
    suspend fun getUser(id: String): User
    suspend fun getUsers(ids: List<String>): List<User>
}