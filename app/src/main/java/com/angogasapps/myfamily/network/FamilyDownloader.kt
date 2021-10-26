package com.angogasapps.myfamily.network

import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.network.repositories.FamilyRepository
import javax.inject.Inject

//class FamilyDownloader @Inject constructor(
//        private val familyRepository: FamilyRepository
//    ) {
//
//    suspend fun download(): Result<Family> {
//
//        return familyRepository.getFamily(USER.family)
//    }
//}