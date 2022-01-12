package com.angogasapps.myfamily.network.repositories

import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListListener
import com.angogasapps.myfamily.network.interfaces.buy_list.BuyListService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BuyListRepository @Inject constructor(
    private val buyListService: BuyListService,
    private val buyListListener: BuyListListener
): BuyListService by buyListService {
    val buyLists
        get() = buyListListener.buyLists
    val listener
        get() = buyListListener.listener
}



/*
@Singleton
class BuyListRepository @Inject constructor(
    private val buyListService: BuyListService,
    private val buyListListener: BuyListListener,
    private val buyListApi: BuyListAPI
) : BuyListService by buyListService {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val lock = Mutex()
    private var firstDownloadIsEnd = false


    val buyLists
        get() = buyListListener.buyLists
    val listener by lazy {
//        onFirstSubscribe()
        buyListListener.listener
    }

    suspend fun firstDownloadIsEnd(): Boolean {
        lock.withLock {
            return firstDownloadIsEnd
        }
    }

    private fun onFirstSubscribe() {
        scope.launch(Dispatchers.IO) {
            val response = buyListApi.getAllBuyLists(USER.family)
            if (response.isSuccessful) {
                val list = response.body() ?: emptyList()
                buyLists.addAll(list)
                list.forEachIndexed { index, buyList ->
                    val event = BuyListEvent(
                        index = index,
                        event = BuyListEvent.EBuyListEvents.buyListAdded,
                        buyListId = buyList.id
                    )
                    (buyListListener as MutableSharedFlow<BuyListEvent>).emit(event)
                }
            } else {
                System.err.println(response.errorBody())
            }
        }
    }


}

 */