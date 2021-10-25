package com.angogasapps.myfamily.network.firebaseImpl

import com.angogasapps.myfamily.firebase.DATABASE_ROOT
import com.angogasapps.myfamily.firebase.NODE_USERS
import com.angogasapps.myfamily.models.User
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.UserService
import kotlinx.coroutines.tasks.await

class FirebaseUserServiceImpl : UserService {
    override suspend fun getUser(id: String): Result<User> {
        return try {
            val snapshot = DATABASE_ROOT.child(NODE_USERS).child(id).get().await()
            Result.Success(User.from(snapshot))
        } catch (e: Exception) {
            Result.Error(e)
        }

    }

    override suspend fun getUsers(ids: List<String>): Result<List<User>> {
        return try {
            val list = mutableListOf<User>()
            for (id in ids) {
                when(val result = getUser(id)) {
                    is Result.Success -> list.add(result.data)
                    is Result.Error -> result.e.printStackTrace()
                }
            }
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}