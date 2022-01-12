package com.angogasapps.myfamily.network.spring_models.chat

data class MediaFileInfo(
    val id: String = "",
    val type: EMediaType = EMediaType.NOTHING,
    val familyId: String = ""
)

enum class EMediaType {
    NOTHING,
    CHAT_IMAGE, CHAT_VOICE, CHAT_FILE,
    USER_IMAGE;
}