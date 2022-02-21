package com.angogasapps.myfamily.network.spring_models.media_storage

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaResponse(val fileInfo: MediaFileInfo)
