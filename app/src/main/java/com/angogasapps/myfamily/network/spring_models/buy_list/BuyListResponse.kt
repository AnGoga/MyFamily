package com.angogasapps.myfamily.network.spring_models.buy_list

import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BuyListResponse(val event: BuyListEvent.EBuyListEvents, val buyLists: MutableList<BuyList>)