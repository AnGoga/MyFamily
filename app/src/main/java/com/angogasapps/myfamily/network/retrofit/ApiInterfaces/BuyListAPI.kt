package com.angogasapps.myfamily.network.retrofit.ApiInterfaces

import com.angogasapps.myfamily.models.buy_list.BuyList
import retrofit2.Response
import retrofit2.http.*

interface BuyListAPI {
    @POST("/buy_lists/{familyId}/{buyListId}")
    suspend fun createBuyList(
        @Path("familyId") familyId: String,
        @Path("buyListId") buyListId: String,
        @Body buyList: BuyList
    ) : Response<String>

    @DELETE("buy_lists/{familyId}/{buyListId}")
    suspend fun deleteBuyList(
        @Path("familyId") familyId: String,
        @Path("buyListId") buyListId: String,
    ) : Response<String>

    @PATCH("buy_lists/{familyId}/{buyListId}")
    suspend fun updateBuyListName(
        @Path("familyId") familyId: String,
        @Path("buyListId") buyListId: String,
        @Body newName: String
    ) : Response<String>


    @POST("buy_lists/{familyId}/{buyListId}/{productId}")
    suspend fun createProduct(
        @Path("familyId") familyId: String,
        @Path("buyListId") buyListId: String,
        @Path("productId") productId: String,
        @Body product: BuyList.Product
    ) : Response<String>

    @DELETE("buy_lists/{familyId}/{buyListId}/{productId}")
    suspend fun deleteProduct(
        @Path("familyId") familyId: String,
        @Path("buyListId") buyListId: String,
        @Path("productId") productId: String
    ) : Response<String>

    @PATCH
    suspend fun updateProduct(
        @Path("familyId") familyId: String,
        @Path("buyListId") buyListId: String,
        @Path("productId") productId: String,
        @Body product: BuyList.Product
    ) : Response<String>
}