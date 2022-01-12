package com.angogasapps.myfamily.network.spring_models.chat

class ChatPagingRequest(
    val familyId: String = "",
    val count: Int = 10,
    val fromMessageId: Long = -1,
)