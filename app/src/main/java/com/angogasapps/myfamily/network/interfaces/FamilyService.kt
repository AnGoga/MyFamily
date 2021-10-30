package com.angogasapps.myfamily.network.interfaces

import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.repositories.UsersRepository
import javax.inject.Inject
import javax.inject.Singleton

abstract class FamilyService constructor(private val usersRepository: UsersRepository) {
    abstract suspend fun getFamily(id: String): Result<Family>
}