package com.angogasapps.myfamily.network.interfaces

import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.ui.screens.chat.ChatEvent
import kotlinx.coroutines.channels.BroadcastChannel

interface ChatGetter {
    suspend fun getMoreMessage(fromMessage: Message, count: Int): List<Message>
}