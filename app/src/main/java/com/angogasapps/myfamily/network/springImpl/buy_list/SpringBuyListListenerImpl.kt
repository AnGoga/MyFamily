package com.angogasapps.myfamily.network.springImpl.buy_list

import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.di.annotations.StompBuyList
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListListener
import com.angogasapps.myfamily.network.spring_models.buy_list.BuyListResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpringBuyListListenerImpl @Inject constructor() : BuyListListener() {
    override val buyLists = ArrayList<BuyList>()

    @Inject
    @StompBuyList
    lateinit var stomp: StompClient

    @Inject
    lateinit var moshi: Moshi
    var adapter: JsonAdapter<BuyListResponse>
    var listAdapter: JsonAdapter<MutableList<BuyList>>

    private var firstMessageIsDownloaded = false

    private lateinit var topic: Disposable

    init {
        appComponent.inject(this)
        adapter = moshi.adapter(BuyListResponse::class.java)

        val listMyData = Types.newParameterizedType(
            MutableList::class.java,
            BuyList::class.java
        )
        listAdapter = moshi.adapter(listMyData)
        initListener()
    }

    private fun initListener() {
        scope.launch {
            stomp.connect()
            val disp = stomp.lifecycle().subscribe {

                when (it.type) {
                    LifecycleEvent.Type.OPENED -> {
                        println("buyList was connected")
                        subscribeTopics()
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        println("buyList was closed")
                    }
                    LifecycleEvent.Type.ERROR -> {
                        println(it.exception.message.toString())
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                        println(it.exception.message.toString())
                    }
                }
            }
        }
    }

    private fun subscribeTopics() {
        topic = stomp.topic("/topic/buy_lists/changes/${USER.family}").subscribe {
            println("Новое сообщение: \n$it\n")
            synchronized(firstMessageIsDownloaded) {
                if (!firstMessageIsDownloaded) {
                    firstMessageIsDownloaded = true
                    handleFirstMessage(it.payload ?: "[]")
                    return@subscribe
                }
                adapter.fromJson(it.payload)?.let { it1 -> handleMessage(it1) }
            }
        }
    }

    private fun handleMessage(message: BuyListResponse) = synchronized(this) {
        buyLists.indexOfFirst { it.id == message.buyList.id }.also {
            try {
                var index = it;
                when (message.event) {
                    BuyListEvent.EBuyListEvents.productAdded -> {
                        index = buyLists[it].products.size
                        buyLists[it].addProduct(message.buyList.products[0])
                    }
                    BuyListEvent.EBuyListEvents.productRemoved -> {
                        index = buyLists[it].removeProductById(message.buyList.products[0].id)
                    }
                    BuyListEvent.EBuyListEvents.productChanged -> {
                        for (i in 0 until buyLists[it].products.size) {
                            if (buyLists[it].products[i].id == message.buyList.products[0].id) {
                                buyLists[it].products[i] = message.buyList.products[0]
                                index = i
                                break
                            }
                        }
                    }
                    BuyListEvent.EBuyListEvents.buyListRemoved -> {
                        buyLists.removeAt(it)
                    }
                    BuyListEvent.EBuyListEvents.buyListAdded -> {
                        buyLists.add(message.buyList)
                    }
                    BuyListEvent.EBuyListEvents.buyListChanged -> {
                        buyLists[it].title = message.buyList.title
                    }
                }
                scope.launch {
                    val event = BuyListEvent(
                        index = if (index >= 0) index else buyLists.size - 1,
                        event = message.event,
                        buyListId = message.buyList.id
                    )
                    flow.emit(event)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleFirstMessage(message: String) {
        val startSize = buyLists.size
        listAdapter.fromJson(message)?.also { buyLists.addAll(it) }
            ?.forEachIndexed { index, buyList ->
                scope.launch {
                    flow.emit(
                        BuyListEvent(
                            buyListId = buyList.id,
                            event = BuyListEvent.EBuyListEvents.buyListAdded,
                            index = index + startSize
                        )
                    )
                }
            }
    }
}

