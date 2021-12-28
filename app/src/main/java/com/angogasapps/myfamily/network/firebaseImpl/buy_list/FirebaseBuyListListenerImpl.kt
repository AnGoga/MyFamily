package com.angogasapps.myfamily.network.firebaseImpl.buy_list

import com.angogasapps.myfamily.firebase.DATABASE_ROOT
import com.angogasapps.myfamily.firebase.NODE_BUY_LIST
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListListener
import com.angogasapps.myfamily.utils.BuyListUtils
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseBuyListListenerImpl @Inject constructor() : BuyListListener() {

    override val buyLists = ArrayList<BuyList>()

    private val fbListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                synchronized(this) {
                    val buyList = BuyListUtils.parseBuyList(snapshot)
                    buyLists.add(buyList)
                    val event = BuyListEvent()
                    event.index = buyLists.size - 1
                    event.event = BuyListEvent.EBuyListEvents.buyListAdded
                    event.buyListId = buyList.id
                    scope.launch {
                        flow.emit(event)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                this@FirebaseBuyListListenerImpl.onChildChanged(snapshot, previousChildName)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val (id) = BuyListUtils.parseBuyList(snapshot)
                val event = BuyListEvent()
                event.buyListId = id
                event.event = BuyListEvent.EBuyListEvents.buyListRemoved
                for (i in buyLists.indices) {
                    if (buyLists[i].id == id) {
                        event.index = i
                        buyLists.removeAt(i)
                        break
                    }
                }
                scope.launch {
                    flow.emit(event)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
    }


    init {
        initListener()
    }

    private fun initListener() {
        DATABASE_ROOT.child(NODE_BUY_LIST)
            .child(USER.family).addChildEventListener(fbListener)
    }

    fun removeProductInBuyList(buyList: BuyList, index: Int) {
        for ((id, _, products) in buyLists) {
            if (id == buyList.id) {
                products.removeAt(index)
                return
            }
        }
    }

    fun addProductToBuyList(buyList: BuyList, product: BuyList.Product?) {
        for (list in buyLists) {
            if (list.id == buyList.id) {
                list.addProduct(product!!)
                return
            }
        }
    }

    fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        synchronized(this) {
            val (id, name, products) = BuyListUtils.parseBuyList(snapshot)
            val event = BuyListEvent()
            for (i in buyLists.indices) {
                val oldBuyList = buyLists[i]
                if (oldBuyList.id == id) {
                    if (oldBuyList.title != name) {
                        oldBuyList.title = name
                        event.buyListId = id
                        event.index = i
                        event.event = BuyListEvent.EBuyListEvents.buyListChanged
                        scope.launch {
                            flow.emit(event)
                        }
                        return
                    }
                    var index = 0
                    if (products.size > oldBuyList.products.size) {
                        // Добавлен новый продукт
                        oldBuyList.products.add(products[products.size - 1])
                        index = oldBuyList.products.size - 1
                        event.event = BuyListEvent.EBuyListEvents.productAdded
                    } else if (products.size < oldBuyList.products.size) {
                        // Один продукт удалён
                        index = BuyListUtils.getIndexOfRemoveProduct(oldBuyList.products, products)
                        oldBuyList.products.removeAt(index)
                        event.event = BuyListEvent.EBuyListEvents.productRemoved
                    } else { //if(newBuyList.getProducts().size() == oldBuyList.getProducts().size()){
                        //Один продукт изменился
                        index = BuyListUtils.getIndexOfChangeProduct(oldBuyList.products, products)
                        oldBuyList.products[index] = products[index]
                        event.event = BuyListEvent.EBuyListEvents.productChanged
                    }
                    event.index = index
                    event.buyListId = id
                    scope.launch {
                        flow.emit(event)
                    }
                    return
                }
            }
        }
    }
}