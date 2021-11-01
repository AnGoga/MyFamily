package com.angogasapps.myfamily.network.interfaces.chat

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