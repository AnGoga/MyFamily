package com.angogasapps.myfamily.network.interfaces.chat

import android.net.Uri
import java.io.File

interface ChatSender {
    fun sendMessage(type: String, value: String)
    fun sendImage(imageUri: Uri)
    fun sendVoice(voiceFile: File, key: String?)
}