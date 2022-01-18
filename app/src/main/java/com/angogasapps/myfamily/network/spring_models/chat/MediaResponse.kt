package com.angogasapps.myfamily.network.spring_models.chat

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaResponse(val fileInfo: MediaFileInfo)
