package com.angogasapps.myfamily.ui.screens.buy_list


import kotlin.jvm.Volatile
import com.angogasapps.myfamily.models.buy_list.BuyList
import io.reactivex.subjects.PublishSubject
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import com.google.firebase.database.ChildEventListener
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import com.google.firebase.database.DataSnapshot
import com.angogasapps.myfamily.utils.BuyListUtils
import com.google.firebase.database.DatabaseError
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.models.buy_list.BuyList.Product
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListManager
import java.util.ArrayList

object BuyListManager {
    val buyLists = ArrayList<BuyList>()
    var subject = PublishSubject.create<BuyListEvent>()
    private lateinit var listener: ChildEventListener

    init {
        initSubjects()
        initListener()
    }


    private fun initSubjects() {
        val a = subject
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    private fun initListener() {
        listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                synchronized(this) {
                    val buyList = BuyListUtils.parseBuyList(snapshot)
                    buyLists.add(buyList)
                    val event = BuyListEvent()
                    event.index = buyLists.size - 1
                    event.event = BuyListEvent.EBuyListEvents.buyListAdded
                    event.buyListId = buyList.id
                    subject.onNext(event)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                this@BuyListManager.onChildChanged(snapshot, previousChildName)
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
                subject.onNext(event)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        }
        FirebaseVarsAndConsts.DATABASE_ROOT.child(FirebaseVarsAndConsts.NODE_BUY_LIST)
            .child(FirebaseVarsAndConsts.USER.family).addChildEventListener(listener)
    }

    fun removeProductInBuyList(buyList: BuyList, index: Int) {
        for ((id, _, products) in buyLists) {
            if (id == buyList.id) {
                products.removeAt(index)
                return
            }
        }
    }

    fun addProductToBuyList(buyList: BuyList, product: Product?) {
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
                    if (oldBuyList.name != name) {
                        oldBuyList.name = name
                        event.buyListId = id
                        event.index = i
                        event.event = BuyListEvent.EBuyListEvents.buyListChanged
                        subject.onNext(event)
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
                    subject.onNext(event)
                    return
                }
            }
        }
    }
}