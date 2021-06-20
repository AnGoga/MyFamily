package com.angogasapps.myfamily.ui.screens.family_storage.adapters

import android.content.Context
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.ui.screens.family_storage.StorageAdapter

class FileStorageAdapter(context: Context, rootNode: String, onChangeDirectory: (dirName: String) -> Unit)
    : StorageAdapter(context, rootNode, onChangeDirectory) {

    override fun onFileClick(file: File) {}
}