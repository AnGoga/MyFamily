package com.angogasapps.myfamily.network.springImpl.chat

import com.angogasapps.myfamily.di.annotations.StompChat
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.network.interfaces.chat.ChatListener
import com.angogasapps.myfamily.network.spring_models.chat.MessageEventResponse
import com.angogasapps.myfamily.ui.screens.chat.ChatEvent
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpringChatListenerImpl @Inject constructor(
    @StompChat private val stomp: StompClient,
    private val moshi: Moshi
) : ChatListener() {
    private val adapter: JsonAdapter<MessageEventResponse> = moshi.adapter(MessageEventResponse::class.java)

    init {
        init()
    }

    private fun init() {
        connect()
    }

    private fun connect() {
        scope.launch {
            stomp.connect()
            stomp.lifecycle().asFlow().collect {
                when(it.type ?: return@collect) {
                    LifecycleEvent.Type.OPENED -> {
                        subscribeToTopics()
                    }
                    LifecycleEvent.Type.CLOSED -> {}
                    LifecycleEvent.Type.ERROR -> {}
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {}
                }
            }
        }
    }

    private suspend fun subscribeToTopics() {
        stomp.topic("/topic/chats/families/${USER.family}/rooms/main/broadcast").asFlow()
            .collect {
                try {
                    println("Chat message get -> " + it?.payload)
                    val event = adapter.fromJson(it?.payload)
                    if (event != null) {
                        flow.emit(event.event)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

    }
}