package com.angogasapps.myfamily.network.interfaces

import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.network.Result

interface FamilyService {
    suspend fun getFamily(id: String): Result<Family>
}