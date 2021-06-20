package com.angogasapps.myfamily.ui.screens.chat

import com.angogasapps.myfamily.models.Message

data class ChatEvent(val event: EChatEvent, val message: Message, val index: Int){}

enum class EChatEvent {
    added, changed, removed
}