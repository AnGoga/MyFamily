package com.angogasapps.myfamily.network.firebaseImpl

import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.models.User
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.FamilyService
import com.angogasapps.myfamily.network.repositories.UsersRepository
import kotlinx.coroutines.tasks.await
import java.util.ArrayList
import javax.inject.Inject

class FirebaseFamilyServiceImpl @Inject constructor(
        private val usersRepository: UsersRepository
    ) : FamilyService(usersRepository) {

    override suspend fun getFamily(id: String): Result<Family> {
        return try {
            val snapshot = DATABASE_ROOT.child(NODE_FAMILIES).child(id).get().await()
            val idsList = mutableListOf<String>()
            val rolesMap = hashMapOf<String, String>()

            Family.emblemUrl = snapshot.child(CHILD_EMBLEM).getValue(String::class.java)!!
            Family.name = snapshot.child(CHILD_NAME).getValue(String::class.java)!!
            for (dataSnapshot in snapshot.child(CHILD_MEMBERS).children) {
                val userId = dataSnapshot.key!!
                idsList.add(userId)
                rolesMap[userId] = dataSnapshot.getValue(String::class.java)!!
            }


            when (val result = usersRepository.getUsers(idsList)) {
                is Result.Error -> return Result.Error(result.e)
                is Result.Success -> {
                    Family.usersList = ArrayList(result.data)
                    for (user in result.data) {
                        user.role = rolesMap[user.id]?: ROLE_MEMBER
                    }
                }
            }

            Result.Success(Family)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}