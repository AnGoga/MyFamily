package com.angogasapps.myfamily.network.interfaces.buy_list

import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.ArrayList

abstract class BuyListListener {
    abstract val buyLists: ArrayList<BuyList>

    protected val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    protected val flow: MutableSharedFlow<BuyListEvent> = MutableSharedFlow()

    open val listener: SharedFlow<BuyListEvent>
        get() = flow.asSharedFlow()
}