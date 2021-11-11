package com.angogasapps.myfamily.network.interfaces.news_center

import com.angogasapps.myfamily.models.events.NewsEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class NewsCenterListener {
    protected val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    protected val flow: MutableSharedFlow<NewsEvent> = MutableSharedFlow()

    open val listener: SharedFlow<NewsEvent>
        get() = flow.asSharedFlow()

}