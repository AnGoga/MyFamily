package com.angogasapps.myfamily.network.retrofit.apiInterfaces

import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.models.storage.StorageFolder
import com.angogasapps.myfamily.network.spring_models.family_storage.CreateFileRequest
import com.angogasapps.myfamily.network.spring_models.family_storage.CreateFolderRequest
import com.angogasapps.myfamily.network.spring_models.media_storage.MediaFileInfo
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface FamilyStorageAPI {
    @GET("/family_storage/data/families/{familyId}/data/{storageType}/content")
    suspend fun getContent(
        @Path("familyId") familyId: String,
        @Path("storageType") storageType: String
    ): StorageFolder

    @POST("/family_storage/data/families/{familyId}/data/{storageType}/create/folder")
    suspend fun createFolder(
        @Path("familyId") familyId: String,
        @Path("storageType") storageType: String,
        @Body request: CreateFolderRequest
    ): Response<ResponseBody>


/*
        @SEE USED MediaStorageService.uploadFile(...)}
    @POST("media_storage/media/storage/upload")
    @Multipart
    suspend fun createFile(
        @Path("familyId") familyId: String,
        @Path("info") fileInfo: MediaFileInfo,
        @Part("extra") request: CreateFileRequest
    )
*/
}