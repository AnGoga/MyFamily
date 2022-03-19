package com.angogasapps.myfamily.network.spring_models.family_storage

import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class CreateFolderRequest(
    val name: String,
    val rootNode: String = "",
    val rootFolder: String = ""
)