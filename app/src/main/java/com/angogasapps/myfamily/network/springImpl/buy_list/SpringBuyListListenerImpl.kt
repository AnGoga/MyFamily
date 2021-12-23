package com.angogasapps.myfamily.network.springImpl.buy_list

import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListListener
import com.angogasapps.myfamily.network.spring_models.buy_list.BuyListResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpringBuyListListenerImpl @Inject constructor() : BuyListListener() {
    override val buyLists = ArrayList<BuyList>()
    @Inject
    lateinit var stomp: StompClient
    @Inject
    lateinit var moshi: Moshi
    var adapter: JsonAdapter<BuyListResponse>

    private lateinit var topic: Disposable

    init {
        appComponent.inject(this)
        adapter = moshi.adapter(BuyListResponse::class.java)
        initListener()
    }

    private fun initListener() {
        scope.launch {
            stomp.connect()
            val disp = stomp.lifecycle().subscribe {
                scope.launch {
                    when (it.type) {
                        LifecycleEvent.Type.OPENED -> {
                            println("buyList was connected")
                            subscribeTopics()
                        }
                        LifecycleEvent.Type.CLOSED -> {
                            println("buyList was closed")
                        }
                        LifecycleEvent.Type.ERROR -> {
                            Toasty.error(appComponent.context, R.string.something_went_wrong).show()
                        }
                        LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                            Toasty.error(appComponent.context, R.string.something_went_wrong).show()
                        }
                    }
                }
            }
        }
    }

    private suspend fun subscribeTopics() {
        topic = stomp.topic("/topic/buy_lists/changes/${USER.family}").subscribe {
            println("Новое сообщение: \n$it\n")
            adapter.fromJson(it.payload)?.let { it1 -> handleMessage(it1) }
        }
    }

    private fun handleMessage(message: BuyListResponse) {
        when(message.event) {
            BuyListEvent.EBuyListEvents.productAdded -> TODO()
            BuyListEvent.EBuyListEvents.productRemoved -> TODO()
            BuyListEvent.EBuyListEvents.productChanged -> TODO()
            BuyListEvent.EBuyListEvents.buyListRemoved -> TODO()
            BuyListEvent.EBuyListEvents.buyListAdded -> TODO()
            BuyListEvent.EBuyListEvents.buyListChanged -> TODO()
        }
    }

}