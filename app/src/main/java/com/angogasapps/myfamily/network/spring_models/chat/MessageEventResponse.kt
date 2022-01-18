package com.angogasapps.myfamily.network.spring_models.chat

import com.angogasapps.myfamily.ui.screens.chat.ChatEvent
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MessageEventResponse(
    val event: ChatEvent
)