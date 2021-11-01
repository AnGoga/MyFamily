package com.angogasapps.myfamily.network.interfaces.chat

import com.angogasapps.myfamily.models.Message
import kotlinx.coroutines.channels.BroadcastChannel

interface ChatGetter {
    suspend fun getMoreMessage(fromMessage: Message, count: Int): List<Message>
}