package com.angogasapps.myfamily.network.interfaces

import android.widget.ImageView
import com.angogasapps.myfamily.ui.screens.chat.ChatEvent
import kotlinx.coroutines.channels.BroadcastChannel

interface ChatService : ChatSender, ChatVoiceGetter, ImageDownloader {
    fun getMessagesFlow() : BroadcastChannel<ChatEvent>
    fun getOldMessagesFrom(id: String, count: Int)
    fun sendMessageWithKey(type: String, value: String, key: String)
}