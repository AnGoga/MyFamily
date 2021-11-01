package com.angogasapps.myfamily.network.repositories

import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListListener
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListService
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
class BuyListRepository @Inject constructor(
    private val buyListService: BuyListService,
    private val buyListListener: BuyListListener
): BuyListService by buyListService {
    val buyLists
        get() = buyListListener.buyLists
    val listener
        get() = buyListListener.listener
}