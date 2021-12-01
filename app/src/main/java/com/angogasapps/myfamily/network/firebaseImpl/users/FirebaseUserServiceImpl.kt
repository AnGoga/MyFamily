package com.angogasapps.myfamily.network.firebaseImpl.users

import android.net.Uri
import android.util.Log
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.User
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.users.UserService
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


class FirebaseUserServiceImpl @Inject constructor() : UserService {

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

    override suspend fun updateUser(user: User): Result<Boolean> {
        val map = HashMap<String, Any>()
        if (user.family != USER.family) map[CHILD_FAMILY] = user.family
        if (user.name != USER.name) map[CHILD_NAME] = user.name
        if (user.birthday != USER.birthday) map[CHILD_BIRTHDAY] = user.birthday
        if (map.isEmpty()) return Result.Success(true)
        try {
            DATABASE_ROOT.child(NODE_USERS).child(USER.id).updateChildren(map).await()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
        return Result.Success(true)
    }

    override suspend fun updatePhoto(uri: Uri): Result<Boolean> {
        try {
            val path = STORAGE_ROOT.child(FOLDER_USERS_PHOTOS).child(AUTH.currentUser!!.uid)
            path.putFile(uri).asDeferred().await().task.let {
                if (!it.isSuccessful) return Result.Error(it.exception!!.also { e -> e.printStackTrace() })
            }
            val link = path.downloadUrl.asDeferred().await()!!.toString()
            val res = updateUser(User(USER).also { it.photoURL = link })
            return when (res) {
                is Result.Error -> res
                is Result.Success -> {
                    USER.photoURL = link
                    res
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
    }
}