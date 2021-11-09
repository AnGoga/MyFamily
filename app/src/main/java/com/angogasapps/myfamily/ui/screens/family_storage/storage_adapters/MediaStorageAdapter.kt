package com.angogasapps.myfamily.ui.screens.family_storage.storage_adapters

import android.content.Context
import android.content.Intent
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import com.angogasapps.myfamily.ui.screens.family_storage.gallery_activity.MediaGalleryStorageActivity

//adapter in Images and Videos file explorer
class MediaStorageAdapter(
    context: Context,
    rootNode: String,
    onChangeDirectory: (dirName: String) -> Unit,
    storageService: FamilyStorageService
) : BaseStorageAdapter(context, rootNode, onChangeDirectory, storageService) {

    override fun onFileClick(file: File) {}
    override fun onFolderClick(folder: ArrayFolder) {
        context.startActivity(Intent(context, MediaGalleryStorageActivity::class.java)
            .also {
                it.putExtra(CHILD_ID, folder.id)
                it.putExtra(CHILD_ROOT_NODE, rootNode)
            })
    }
}