package com.angogasapps.myfamily.network.spring_models.media_storage

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaFileInfo(
    val id: String = "",
    val type: EMediaType = EMediaType.NOTHING,
    val familyId: String = "",
    val name: String = "",
    val value: String = "",
    val rootFolder: String = ""
)

enum class EMediaType {
    NOTHING,
    CHAT_IMAGE, CHAT_VOICE, CHAT_FILE,
    USER_IMAGE,
    STORAGE_IMAGE,
    STORAGE_NOTE,
    STORAGE_FILE,
    STORAGE_VIDEO;
}
