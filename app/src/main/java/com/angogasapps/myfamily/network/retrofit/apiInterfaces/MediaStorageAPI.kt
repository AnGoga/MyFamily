package com.angogasapps.myfamily.network.retrofit.apiInterfaces

import com.angogasapps.myfamily.network.spring_models.chat.MediaFileInfo
import com.angogasapps.myfamily.network.spring_models.chat.MediaResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST

interface MediaStorageAPI {
    @POST("/media_storage/media/storage/upload")
    @Multipart
    suspend fun sendFileToServer(

    ): MediaResponse

    @GET("/media_storage/media/storage/get")
    suspend fun getFileFromServer(
        @Body fileInfo: MediaFileInfo
    )//: ??? Resource?
}