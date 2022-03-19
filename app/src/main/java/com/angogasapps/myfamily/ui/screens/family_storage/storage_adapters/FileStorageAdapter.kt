package com.angogasapps.myfamily.ui.screens.family_storage.storage_adapters

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.StrictMode
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.angogasapps.myfamily.models.storage.File
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import retrofit2.http.Url


class FileStorageAdapter(
    context: Context,
    rootNode: String,
    onChangeDirectory: (dirName: String) -> Unit,
    storageService: FamilyStorageService
) : BaseStorageAdapter(context, rootNode, onChangeDirectory, storageService) {


    override fun onFileClick(file: File) {
        StrictMode::class.java.getMethod("disableDeathOnFileUriExposure").invoke(null)
        storageService.downloadFile(file = file, onSuccess = { dFile, contentType ->
            /*val myMime = MimeTypeMap.getSingleton()
            val newIntent = Intent(Intent.ACTION_VIEW)
            val mimeType = myMime.getMimeTypeFromExtension(fileExt(dFile.toURL().toString()))?.substring(1)
            newIntent.setDataAndType(Uri.fromFile(dFile), mimeType)
            newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            try {
                context.startActivity(newIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG)
                    .show()
            }*/
            val myIntent = Intent(Intent.ACTION_VIEW)
            myIntent.data = Uri.fromFile(dFile)
            val j = Intent.createChooser(myIntent, "Choose an application to open with:")
            context.startActivity(j)
            /*val intent = Intent(Intent.ACTION_VIEW).also {
                val uri = Uri.fromFile(dFile)
                it.setDataAndType(RealPathUtil.getRealPath(dFile), contentType)
            }
            context.startActivity(intent)*/
        })
    }
    private fun fileExt(url: String): String? {
        var url = url
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"))
        }
        return if (url.lastIndexOf(".") == -1) {
            null
        } else {
            var ext = url.substring(url.lastIndexOf(".") + 1)
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"))
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"))
            }
            ext.toLowerCase()
        }
    }
}