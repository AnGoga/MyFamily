package com.angogasapps.myfamily.utils.moshi.adapters

import com.angogasapps.myfamily.models.buy_list.BuyList
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.ToJson



class ListAdapter{
    @ToJson
    fun arrayListToJson(list: ArrayList<BuyList.Product>): List<BuyList.Product> = list

    @FromJson
    fun arrayListFromJson(list: List<BuyList.Product>): ArrayList<BuyList.Product> = ArrayList(list)
}