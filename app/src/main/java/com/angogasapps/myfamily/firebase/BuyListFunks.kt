package com.angogasapps.myfamily.firebase


import com.angogasapps.myfamily.app.AppApplication.Companion.isOnline
import com.angogasapps.myfamily.app.AppApplication.Companion.getInstance
import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.models.buy_list.BuyList.Product
import es.dmoral.toasty.Toasty
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.utils.BuyListUtils
import com.google.android.gms.tasks.Task

object BuyListFunks {
    @Synchronized
    fun addNewBuyList(buyList: BuyList, i: IOnEndCommunicationWithFirebase) {
        val ref = DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family)
        val key = ref.push().key
        ref.child(key!!).setValue(buyList).addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                i.onSuccess()
            } else {
                task.exception!!.printStackTrace()
                i.onFailure()
            }
        }
    }

    @Synchronized
    fun updateBuyListName(buyList: BuyList, i: IOnEndCommunicationWithFirebase) {
        DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family).child(buyList.id)
            .child(CHILD_NAME).setValue(buyList.name)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    i.onSuccess()
                } else {
                    task.exception!!.printStackTrace()
                    i.onFailure()
                }
            }
    }

    @Synchronized
    fun addNewProductToBuyList(
        buyListId: String,
        product: Product,
        i: IOnEndCommunicationWithFirebase
    ) {
        if (!isOnline) {
            Toasty.warning(
                getInstance().applicationContext,
                R.string.connection_is_not
            ).show()
        }
        val ref = DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family).child(buyListId)
            .child(CHILD_PRODUCTS)
        val key = ref.push().key
        product.from = USER.phone
        ref.child(key!!).updateChildren(BuyListUtils.getHashMap(product))
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    i.onSuccess()
                } else {
                    task.exception!!.printStackTrace()
                    i.onFailure()
                }
            }
    }

    @Synchronized
    fun updateProduct(buyListId: String, product: Product, i: IOnEndCommunicationWithFirebase) {
        product.from = USER.phone
        DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family).child(buyListId)
            .child(CHILD_PRODUCTS).child(product.id)
            .updateChildren(BuyListUtils.getHashMap(product))
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    i.onSuccess()
                } else {
                    task.exception!!.printStackTrace()
                    i.onFailure()
                }
            }
    }

    @Synchronized
    fun deleteProduct(buyListId: String, product: Product, i: IOnEndCommunicationWithFirebase) {
        DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family).child(buyListId)
            .child(CHILD_PRODUCTS).child(product.id).removeValue()
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    i.onSuccess()
                } else {
                    i.onFailure()
                    task.exception!!.printStackTrace()
                }
            }
    }

    @Synchronized
    fun deleteBuyList(buyList: BuyList, i: IOnEndCommunicationWithFirebase) {
        DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family).child(buyList.id)
            .removeValue().addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    i.onSuccess()
                } else {
                    task.exception!!.printStackTrace()
                    i.onFailure()
                }
            }
    }
}