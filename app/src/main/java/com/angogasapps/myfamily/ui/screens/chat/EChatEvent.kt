package com.angogasapps.myfamily.ui.screens.chat

import com.angogasapps.myfamily.models.Message
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatEvent(val event: EChatEvent, val message: Message){}

enum class EChatEvent {
    added, changed, removed
}