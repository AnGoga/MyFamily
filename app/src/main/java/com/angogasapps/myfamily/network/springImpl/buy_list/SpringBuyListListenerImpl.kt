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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage
import java.util.*
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

    private var initMessageIsDownloaded = false
    private val mutex = Mutex()
    private val queue: Queue<BuyListResponse> = LinkedList()

    private lateinit var topic: Flow<StompMessage>

    init {
        appComponent.inject(this)
        adapter = moshi.adapter(BuyListResponse::class.java)
        initListener()
    }

    private fun initListener() {
        scope.launch(Dispatchers.IO) {
            stomp.connect()
            val disp = stomp.lifecycle().subscribe {
                when (it.type ?: return@subscribe) {
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
        scope.launch(Dispatchers.IO) {
            topic = stomp.topic("/topic/buy_lists/changes/${USER.family}").asFlow()
            topic.collect {
                println("Новое сообщение: \n$it\n")
                mutex.withLock {
                    val message: BuyListResponse
                    try {
                        message = adapter.fromJson(it.payload) ?: return@collect
                    } catch (e: Exception) {
                        e.printStackTrace()
                        return@collect
                    }
                    if (message.event == BuyListEvent.EBuyListEvents.initMessage) {
                        initMessageIsDownloaded = true
                        handleInitMessage(message)
                        queue.forEach { response ->
                            handleMessage(response)
                        }
                        queue.clear()
                        return@collect
                    }
                    if (!initMessageIsDownloaded){
                        queue.add(message)
                    } else {
                        handleMessage(message)
                    }
                }
            }
        }
    }

    private suspend fun handleMessage(message: BuyListResponse) {
        buyLists.indexOfFirst { it.id == message.buyLists[0].id }.also {
            try {
                var index = it;
                when (message.event) {
                    BuyListEvent.EBuyListEvents.productAdded -> {
                        index = buyLists[it].products.size
                        buyLists[it].addProduct(message.buyLists[0].products[0])
                    }
                    BuyListEvent.EBuyListEvents.productRemoved -> {
                        index = buyLists[it].removeProductById(message.buyLists[0].products[0].id)
                    }
                    BuyListEvent.EBuyListEvents.productChanged -> {
                        for (i in 0 until buyLists[it].products.size) {
                            if (buyLists[it].products[i].id == message.buyLists[0].products[0].id) {
                                buyLists[it].products[i] = message.buyLists[0].products[0]
                                index = i
                                break
                            }
                        }
                    }
                    BuyListEvent.EBuyListEvents.buyListRemoved -> {
                        buyLists.removeAt(it)
                    }
                    BuyListEvent.EBuyListEvents.buyListAdded -> {
                        buyLists.add(message.buyLists[0])
                    }
                    BuyListEvent.EBuyListEvents.buyListChanged -> {
                        buyLists[it].title = message.buyLists[0].title
                    }
                }
                val event = BuyListEvent(
                    index = if (index >= 0) index else buyLists.size - 1,
                    event = message.event,
                    buyListId = message.buyLists[0].id
                )
                flow.emit(event)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun handleInitMessage(message: BuyListResponse) {
        val startSize = buyLists.size
        try {
            message.buyLists.also { buyLists.addAll(it) }.forEachIndexed { index, buyList ->
                flow.emit(
                    BuyListEvent(
                        buyListId = buyList.id,
                        event = BuyListEvent.EBuyListEvents.buyListAdded,
                        index = index + startSize
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}