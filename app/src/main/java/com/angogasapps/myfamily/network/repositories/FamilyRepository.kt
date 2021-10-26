package com.angogasapps.myfamily.network.repositories

import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.FamilyService
import javax.inject.Inject

class FamilyRepository @Inject constructor(
        private val familyService: FamilyService
        //val familyDao: FamilyDao
    ) {

    suspend fun getFamily(id: String): Result<Family> {
        return familyService.getFamily(id)
    }

}