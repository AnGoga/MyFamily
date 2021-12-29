package com.angogasapps.myfamily.network.springImpl.buy_list

import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListService
import com.angogasapps.myfamily.network.retrofit.ApiInterfaces.BuyListAPI
import com.angogasapps.myfamily.network.utils.IdGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpringBuyListServiceImpl @Inject constructor() : BuyListService {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var apiInterface: BuyListAPI
    @Inject
    lateinit var idGenerator: IdGenerator

    init {
        appComponent.inject(this)
    }

    override fun createNewBuyList(buyList: BuyList, onSuccess: () -> Unit, onError: () -> Unit) {
        scope.launch {
            try {
                buyList.id = idGenerator.randomId()
                val response = apiInterface.createBuyList(
                    familyId = USER.family,
                    buyListId = buyList.id,
                    buyList = buyList
                )
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    System.err.println(response.code().toString() + "\n" + response.body())
                    onError()
                }
            } catch (e: Exception){ e.printStackTrace() }
        }
    }

    override fun updateBuyListName(buyList: BuyList, onSuccess: () -> Unit, onError: () -> Unit) {
        scope.launch {
            val response = apiInterface.updateBuyListName(
                familyId = USER.family,
                buyListId = buyList.id,
                buyList = BuyList(id = buyList.id, title = buyList.title)
            )
            if (response.isSuccessful) {
                onSuccess()
            } else {
                System.err.println(response.errorBody())
                onError()
            }
        }
    }

    override fun deleteBuyList(buyList: BuyList, onSuccess: () -> Unit, onError: () -> Unit) {
        scope.launch {
            val response = apiInterface.deleteBuyList(
                familyId = USER.family,
                buyListId = buyList.id
            )
            if (response.isSuccessful) {
                onSuccess()
            } else {
                System.err.println(response.errorBody())
                onError()
            }
        }
    }

    override fun createProduct(
        buyListId: String,
        product: BuyList.Product,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        scope.launch {
            product.id = idGenerator.randomId()
            val response = apiInterface.createProduct(
                familyId = USER.family,
                buyListId = buyListId,
                productId = product.id,
                product = product
            )
            if (response.isSuccessful) {
                onSuccess()
            } else {
                System.err.println(response.errorBody())
                onError()
            }
        }
    }

    override fun updateProduct(
        buyListId: String,
        product: BuyList.Product,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        scope.launch {
            val response = apiInterface.updateProduct(
                familyId = USER.family,
                buyListId = buyListId,
                productId = product.id,
                product = product
            )
            if (response.isSuccessful) {
                onSuccess()
            } else {
                System.err.println(response.errorBody())
                onError()
            }
        }
    }

    override fun deleteProduct(
        buyListId: String,
        product: BuyList.Product,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        scope.launch {
            val response = apiInterface.deleteProduct(
                familyId = USER.family,
                buyListId = buyListId,
                productId = product.id
            )
            if (response.isSuccessful) {
                onSuccess()
            } else {
                System.err.println(response.errorBody())
                onError()
            }
        }
    }
}