package com.angogasapps.myfamily.utils


import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.Family.getNameByPhone
import com.google.firebase.database.DataSnapshot
import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.utils.BuyListUtils
import com.angogasapps.myfamily.models.buy_list.BuyList.Product
import com.angogasapps.myfamily.models.Family
import java.util.ArrayList
import java.util.HashMap

object BuyListUtils {
    /*
  DataSnapshot {
    key = -MY0xumFvGFkkxbaYiLo,
    value = {
        name=#1,
        products = {
            -MZO2_VpJkWN3pY3fdyE = {
                annotation=,
                count=-1,
                name=продукт 1,
                from=-MZO2_VpJkWN3pY3fdyE,
                id=-MZO2_VpJkWN3pY3fdyE
                }
             }
         }
     }

 */
    fun parseBuyList(snapshot: DataSnapshot): BuyList {
        val buyList = BuyList()
        buyList.id = snapshot.key!!
        buyList.name = snapshot.child(CHILD_NAME).getValue(String::class.java)!!
        buyList.products = getProductsList(snapshot)
        return buyList
    }

    private fun getProductsList(snapshot: DataSnapshot): ArrayList<Product> {
        val list = ArrayList<Product>()
        for (productSnapshot in snapshot.child(CHILD_PRODUCTS).children) {
            list.add(getProduct(productSnapshot)!!)
        }
        return list
    }

    private fun getProduct(snapshot: DataSnapshot): Product? {
        println(snapshot)
        val product = snapshot.getValue(Product::class.java)
        product!!.id = snapshot.key!!
        return product
    }

    fun getHashMap(product: Product): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[CHILD_NAME] = product.name
        map[CHILD_FROM] = product.from
        map[CHILD_COMMENT] = product.comment
        return map
    }

    fun getCorrectComment(product: Product): String {
        val comment = product.comment
        return if (product.from == USER.phone) {
            comment ?: ""
        } else {
            if (comment != "") {
                comment + " : " + getNameByPhone(product.from)
            } else {
                getNameByPhone(product.from)
            }
        }
    }

    fun getIndexOfRemoveProduct(
        oldBuyList: ArrayList<Product>,
        newBuyList: ArrayList<Product>
    ): Int {
        for (i in newBuyList.indices) {
            if (oldBuyList[i].id != newBuyList[i].id) {
                return i
            }
        }
        return newBuyList.size
    }

    fun getIndexOfChangeProduct(
        oldBuyList: ArrayList<Product>,
        newBuyList: ArrayList<Product>
    ): Int {
        for (i in newBuyList.indices) {
            if (oldBuyList[i] != newBuyList[i]) {
                return i
            }
        }
        return 0
    }
}