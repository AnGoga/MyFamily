package com.angogasapps.myfamily.models.buy_list

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class BuyList(
    var id: String = "",
    var title: String = "",
    var products: MutableList<Product> = ArrayList<Product>()
) {
    constructor(str: String) : this(id = str) {

    }

    constructor(buyList: BuyList) : this(id = buyList.id, title = buyList.title) {
        products.addAll(buyList.products)
    }

    fun addProduct(product: Product) {
        products.add(product)
    }

    fun removeProductById(id: String): Int {
        for (i: Int in 0 until products.size) {
            if (products[i].id == id) {
                products.removeAt(i)
                return i
            }
        }
        return -1;
    }

    @JsonClass(generateAdapter = true)
    data class Product(
        var id: String = "",
        var title: String = "",
        var comment: String = "",
        var from: String = ""
    )

    companion object {
        fun from(snapshot: DataSnapshot): BuyList {
            Log.i("tag", snapshot.toString())
            val buyList = snapshot.getValue(BuyList::class.java)!!
            buyList.id = snapshot.key ?: ""
            return buyList
        }
    }
}