package com.angogasapps.myfamily.network.firebaseImpl.buy_list

import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.AppApplication
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListService
import com.angogasapps.myfamily.utils.BuyListUtils
import com.google.android.gms.tasks.Task
import es.dmoral.toasty.Toasty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseBuyListServiceImpl @Inject constructor() : BuyListService {
    override fun createNewBuyList(buyList: BuyList, onSuccess: () -> Unit, onError: () -> Unit) {
        val ref = DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family)
        val key = ref.push().key
        ref.child(key!!).setValue(buyList).addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                task.exception!!.printStackTrace()
                onError()
            }
        }
    }

    override fun updateBuyListName(buyList: BuyList, onSuccess: () -> Unit, onError: () -> Unit) {
        DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family).child(buyList.id)
            .child(CHILD_NAME).setValue(buyList.name)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    task.exception!!.printStackTrace()
                    onError()
                }
            }
    }

    override fun addNewProductToBuyList(
        buyListId: String,
        product: BuyList.Product,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        if (!AppApplication.isOnline) {
            Toasty.warning(
                AppApplication.getInstance().applicationContext,
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
                    onSuccess()
                } else {
                    task.exception!!.printStackTrace()
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
        product.from = USER.phone
        DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family).child(buyListId)
            .child(CHILD_PRODUCTS).child(product.id)
            .updateChildren(BuyListUtils.getHashMap(product))
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    task.exception!!.printStackTrace()
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
        DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family).child(buyListId)
            .child(CHILD_PRODUCTS).child(product.id).removeValue()
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    task.exception!!.printStackTrace()
                    onError()
                }
            }
    }

    override fun deleteBuyList(buyList: BuyList, onSuccess: () -> Unit, onError: () -> Unit) {
        DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family).child(buyList.id)
            .removeValue().addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    task.exception!!.printStackTrace()
                    onError()
                }
            }
    }
}
