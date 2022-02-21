package com.angogasapps.myfamily.network.spring_models.family_storage

open class CreateFolderRequest(
    val name: String,
    val rootNode: String = "",
    val rootFolder: String = ""
)