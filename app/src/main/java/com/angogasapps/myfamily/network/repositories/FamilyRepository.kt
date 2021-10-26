package com.angogasapps.myfamily.network.repositories

import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.FamilyService
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FamilyRepository @Inject constructor(
        private val familyService: FamilyService
        //val familyDao: FamilyDao
    ) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    lateinit var firstDownloadIsEnd: Deferred<Result<Family>>

    suspend fun getFamily(id: String): Result<Family> {
        return getFamilyFromNetwork(id)
    }

    private suspend fun getFamilyFromNetwork(id: String): Result<Family> {
        val def = scope.async {
            familyService.getFamily(id)
        }
        if (!::firstDownloadIsEnd.isInitialized)
            firstDownloadIsEnd = def
        return def.await()
    }

}