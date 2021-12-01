package com.angogasapps.myfamily.network.interfaces.auth

import android.net.Uri

interface AuthService {
    suspend fun registerUser(userName: String, userBirthdayTimeMillis: Long, photoUri: Uri?)
}