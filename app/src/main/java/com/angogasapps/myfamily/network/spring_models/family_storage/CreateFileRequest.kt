package com.angogasapps.myfamily.network.spring_models.family_storage

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CreateFileRequest(
    name: String = "",
    val value: String = "",
    rootNode: String = "",
    rootFolder: String = "",
): CreateFolderRequest(name = name, rootNode = rootNode, rootFolder = rootFolder)