package com.angogasapps.myfamily.network.spring_models.chat

import com.angogasapps.myfamily.models.Message
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ChatPagingRequest(
    val familyId: String = "",
    val count: Int = 10,
    val fromMessage: Message = Message(id = "-2"),
)