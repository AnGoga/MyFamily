package com.angogasapps.myfamily.network.springImpl.media_storage

import com.angogasapps.myfamily.network.retrofit.apiInterfaces.MediaStorageAPI
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.angogasapps.myfamily.network.spring_models.chat.MediaFileInfo
import com.angogasapps.myfamily.network.spring_models.chat.MediaResponse
import com.google.common.io.ByteStreams
import okhttp3.MediaType

import okhttp3.RequestBody

import okhttp3.MultipartBody
import java.io.FileOutputStream


@Singleton
class MediaStorageService @Inject constructor(private val storageAPI: MediaStorageAPI) {

    suspend fun uploadFile(file: File, fileInfo: MediaFileInfo): MediaResponse {
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("file", fileInfo.id, requestFile)
        return storageAPI.sendFileToServer(infoStr = fileInfo, body = body)
    }

    suspend fun getImageFromServer(fileInfo: MediaFileInfo): Bitmap? {

        val response = storageAPI.getImageFromServer(fileInfo)
        return if (response.isSuccessful) {
            val stream = response.body()?.byteStream()
            val bitmap = BitmapFactory.decodeStream(stream)
            bitmap
        } else {
            println(response.errorBody().toString())
            println(response.message().toString())
            null
        }
    }

    suspend fun getFileFromServer(fileInfo: MediaFileInfo, file: File): File {
        val stream = storageAPI.getImageFromServer(fileInfo).body()?.byteStream()
        if (!file.exists()) file.createNewFile()
        ByteStreams.copy(stream, FileOutputStream(file))
        return file
    }
}