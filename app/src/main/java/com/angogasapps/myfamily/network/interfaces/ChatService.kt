package com.angogasapps.myfamily.network.interfaces

import android.widget.ImageView
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.network.Result
import com.angogasapps.myfamily.ui.screens.chat.ChatEvent
import kotlinx.coroutines.channels.BroadcastChannel

interface ChatService : ChatSender, ChatVoiceGetter, ImageDownloader {
    suspend fun getMoreMessages(fromMessage: Message, count: Int): Result<List<Message>>
    fun sendMessageWithKey(type: String, value: String, key: String)
}