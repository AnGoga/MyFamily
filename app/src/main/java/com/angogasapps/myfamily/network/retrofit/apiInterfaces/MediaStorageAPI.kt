package com.angogasapps.myfamily.network.retrofit.apiInterfaces

import com.angogasapps.myfamily.network.spring_models.family_storage.CreateFileRequest
import com.angogasapps.myfamily.network.spring_models.media_storage.MediaFileInfo
import com.angogasapps.myfamily.network.spring_models.media_storage.MediaResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface MediaStorageAPI {
    @POST("/media_storage/media/storage/upload")
    @Multipart
    suspend fun sendFileToServer(
        @Part("info") infoStr: MediaFileInfo,
        @Part/*("file")*/ body: MultipartBody.Part,
        @Part("extra") request: CreateFileRequest? = null
    ): MediaResponse

//    @Headers("Content-Type: image/jpeg")
    @Streaming
    @Headers("Content-Type: application/json", "Accept: application/octet-stream")
    @POST("/media_storage/media/storage/get/file")
//    @Headers("Content-Type:application/octet-stream")
    suspend fun getFileFromServer(
        @Body fileInfo: MediaFileInfo
    ): Response<ResponseBody>
}