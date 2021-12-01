package com.angogasapps.myfamily.network.repositories

import com.angogasapps.myfamily.database.UserDao
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.models.User
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.families.FamilyService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FamilyRepository @Inject constructor(
    private val familyService: FamilyService,
    private val userDao: UserDao
//        val familyDao: FamilyDao
    ) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    //TODO: do be lazy with fun getFamily()
    lateinit var firstDownloadIsEnd: Deferred<Result<Family>>

    suspend fun getFamily(): Result<Family> {

        return getFamilyFromNetwork()
    }

    private suspend fun getFamilyFromNetwork(): Result<Family> {
        val def = scope.async {
            familyService.getFamily(USER.family)
        }
        if (!::firstDownloadIsEnd.isInitialized)
            firstDownloadIsEnd = def
        return def.await()

    }


    suspend fun getFamilyMembersFromCash(): Result<ArrayList<User>> {
        val def = scope.async {
            //TODO: family Dao -> get from it family's name and emblem
            val list = ArrayList(userDao.all)
            return@async Result.Success(list)
        }
        return def.await()
    }

}