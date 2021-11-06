package com.angogasapps.myfamily.network.interfaces


interface FindFamilyService {
    fun tryFindFamilyById(id: String, onSuccess: () -> Unit = {}, onError: () -> Unit = {})
    fun joinUserToFamily(id: String, onSuccess: () -> Unit = {}, onError: () -> Unit = {})
}