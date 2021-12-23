package com.angogasapps.myfamily.network.springImpl.buy_list

import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListService

class SpringBuyListServiceImpl : BuyListService {
    override fun createNewBuyList(buyList: BuyList, onSuccess: () -> Unit, onError: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun updateBuyListName(buyList: BuyList, onSuccess: () -> Unit, onError: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun addNewProductToBuyList(buyListId: String, product: BuyList.Product, onSuccess: () -> Unit, onError: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun updateProduct(buyListId: String, product: BuyList.Product, onSuccess: () -> Unit, onError: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun deleteProduct(buyListId: String, product: BuyList.Product, onSuccess: () -> Unit, onError: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun deleteBuyList(buyList: BuyList, onSuccess: () -> Unit, onError: () -> Unit) {
        TODO("Not yet implemented")
    }
}