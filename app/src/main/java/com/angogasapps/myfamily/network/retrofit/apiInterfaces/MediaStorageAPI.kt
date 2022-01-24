package com.angogasapps.myfamily.network.retrofit.apiInterfaces

import android.widget.ImageView
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.network.spring_models.chat.MediaFileInfo
import com.angogasapps.myfamily.network.spring_models.chat.MediaResponse
import com.bumptech.glide.Glide
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import java.io.InputStream

interface MediaStorageAPI {
    @POST("/media_storage/media/storage/upload")
    @Multipart
    suspend fun sendFileToServer(
        @Part("info") infoStr: MediaFileInfo,
        @Part/*("file")*/ body: MultipartBody.Part
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