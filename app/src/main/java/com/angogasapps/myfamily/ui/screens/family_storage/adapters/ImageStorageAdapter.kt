package com.angogasapps.myfamily.ui.screens.family_storage.adapters

import android.content.Context
import android.content.Intent
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.ui.screens.family_storage.gallery_activity.ImageGalleryActivity

class ImageStorageAdapter(context: Context, rootNode: String, onChangeDirectory: (dirName: String) -> Unit)
    : StorageAdapter(context, rootNode, onChangeDirectory) {

    override fun onFileClick(file: File) {}
    override fun onFolderClick(folder: ArrayFolder) {
        context.startActivity(Intent(context, ImageGalleryActivity::class.java)
                .also { it.putExtra(FirebaseVarsAndConsts.CHILD_ID, folder.id) })
    }
}