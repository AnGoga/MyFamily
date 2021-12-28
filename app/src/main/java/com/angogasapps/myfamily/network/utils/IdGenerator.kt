package com.angogasapps.myfamily.network.utils

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdGenerator @Inject constructor() {
    fun randomId(): String {
        return UUID.randomUUID().toString()
    }
}