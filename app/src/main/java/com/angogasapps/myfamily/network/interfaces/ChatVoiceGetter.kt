package com.angogasapps.myfamily.network.interfaces

import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import java.io.File

interface ChatVoiceGetter {
    fun getVoiceFileFromStorage(
        file: File,
        key: String,
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
    )
}