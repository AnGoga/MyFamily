package com.angogasapps.myfamily.network.interfaces.buy_list

import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.models.buy_list.BuyList

interface BuyListService {
    fun createNewBuyList(buyList: BuyList, onSuccess: () -> Unit = {}, onError: () -> Unit = {})
    fun updateBuyListName(buyList: BuyList, onSuccess: () -> Unit = {}, onError: () -> Unit = {})
    fun createProduct(
        buyListId: String,
        product: BuyList.Product,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    )

    fun updateProduct(
        buyListId: String,
        product: BuyList.Product,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    )

    fun deleteProduct(
        buyListId: String,
        product: BuyList.Product,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    )

    fun deleteBuyList(buyList: BuyList, onSuccess: () -> Unit = {}, onError: () -> Unit = {})

}