package com.angogasapps.myfamily.network.interfaces.chat

import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.network.Result

interface ChatService : ChatSender, ChatVoiceGetter, ImageDownloader {
    suspend fun getMoreMessages(fromMessage: Message, count: Int): Result<List<Message>>
    fun sendMessageWithKey(type: String, value: String, key: String = "")
}