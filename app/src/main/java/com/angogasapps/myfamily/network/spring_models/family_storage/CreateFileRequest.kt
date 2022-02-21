package com.angogasapps.myfamily.network.spring_models.family_storage

class CreateFileRequest(
    name: String,
    val value: String,
    rootNode: String,
    rootFolder: String,
): CreateFolderRequest(name = name, rootNode = rootNode, rootFolder = rootFolder)