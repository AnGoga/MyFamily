package com.angogasapps.myfamily.network.springImpl.chat

import android.net.Uri
import android.widget.ImageView
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.network.interfaces.chat.ChatService
import java.io.File

class SpringChatServiceImpl : ChatService {

    override suspend fun getMoreMessages(fromMessage: Message, count: Int): Result<List<Message>> {
        TODO("Not yet implemented")
    }

    override fun sendMessageWithKey(type: String, value: String, key: String) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(type: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun sendImage(imageUri: Uri) {
        TODO("Not yet implemented")
    }

    override fun sendVoice(voiceFile: File, key: String?) {
        TODO("Not yet implemented")
    }

    override fun getVoiceFileFromStorage(
        file: File,
        key: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun downloadImageMessageAndSetBitmap(path: String, key: String, imageView: ImageView) {
        TODO("Not yet implemented")
    }
}