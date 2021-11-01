package com.angogasapps.myfamily.network.interfaces.chat

import com.angogasapps.myfamily.ui.screens.chat.ChatEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class ChatListener {
    protected val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    protected val flow: MutableSharedFlow<ChatEvent> = MutableSharedFlow()

    open val listener: SharedFlow<ChatEvent>
        get() = flow.asSharedFlow()




}