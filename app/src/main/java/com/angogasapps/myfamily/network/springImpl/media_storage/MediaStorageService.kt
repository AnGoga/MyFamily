package com.angogasapps.myfamily.network.springImpl.media_storage

import com.angogasapps.myfamily.network.retrofit.apiInterfaces.MediaStorageAPI
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

import android.widget.ImageView
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.network.spring_models.family_storage.CreateFileRequest
import com.angogasapps.myfamily.network.spring_models.media_storage.MediaFileInfo
import com.angogasapps.myfamily.network.spring_models.media_storage.MediaResponse
import com.angogasapps.myfamily.utils.getImageUrlFromMediaFileInfo
import com.bumptech.glide.Glide
import com.google.common.io.ByteStreams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import okhttp3.RequestBody

import okhttp3.MultipartBody
import java.io.FileOutputStream
import java.lang.Exception


@Singleton
class MediaStorageService @Inject constructor(private val storageAPI: MediaStorageAPI) {

    suspend fun uploadFile(file: File, fileInfo: MediaFileInfo, request: CreateFileRequest? = null): MediaResponse {
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", fileInfo.id, requestFile)
        return storageAPI.sendFileToServer(infoStr = fileInfo, body = body, request = request)
    }

    suspend fun getImageFromServerAndSetBitmap(info: MediaFileInfo, imageView: ImageView) {
        withContext(Dispatchers.Main) {
            Glide.with(imageView)
                .load(getImageUrlFromMediaFileInfo(info))
                .into(imageView)
        }
    }

    suspend fun getFileFromServer(fileInfo: MediaFileInfo, file: File): File {
         return withContext(Dispatchers.IO) {
            try {
                val response = storageAPI.getFileFromServer(fileInfo)
//                val stream = response.body()?.source()?.inputStream()
                val stream = response.body()?.byteStream()
                if (!file.exists()) file.createNewFile()
                ByteStreams.copy(stream, FileOutputStream(file))
            } catch (e: Exception) {
                e.printStackTrace()
            }
             return@withContext file
        }
    }
}