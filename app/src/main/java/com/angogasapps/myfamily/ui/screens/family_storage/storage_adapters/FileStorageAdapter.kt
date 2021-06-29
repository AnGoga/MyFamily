package com.angogasapps.myfamily.ui.screens.family_storage.storage_adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import com.angogasapps.myfamily.firebase.downloadFile
import com.angogasapps.myfamily.models.storage.File

class FileStorageAdapter(context: Context, rootNode: String, onChangeDirectory: (dirName: String) -> Unit)
    : BaseStorageAdapter(context, rootNode, onChangeDirectory) {

    override fun onFileClick(file: File) {
        StrictMode::class.java.getMethod("disableDeathOnFileUriExposure").invoke(null)
        downloadFile(file = file, onSuccess = {dFile, contentType ->
            val intent = Intent(Intent.ACTION_VIEW).also {
                it.setDataAndType(Uri.fromFile(dFile), contentType)
            }
            context.startActivity(intent)
        })
    }
}