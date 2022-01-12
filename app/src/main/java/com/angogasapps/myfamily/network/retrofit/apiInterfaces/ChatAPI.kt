package com.angogasapps.myfamily.network.retrofit.apiInterfaces

import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.network.spring_models.chat.ChatPagingRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatAPI {
    @POST("/chat/families/{familyId}/rooms/main/messages/post")
    suspend fun sendMessage(
        @Path("familyId") familyId: String,
    ): Response<Void>

    @GET("/chat/families/{familyId}/rooms/main/messages/post/get_more")
    suspend fun getMoreMessages(
        @Path("familyId") familyId: String,
        @Body request: ChatPagingRequest
    ): MutableList<Message>
}