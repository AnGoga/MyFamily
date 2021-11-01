package com.angogasapps.myfamily.ui.screens.chat

import android.net.Uri
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.database.MessageDao
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.network.interfaces.chat.ChatListener
import com.angogasapps.myfamily.network.repositories.ChatRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import java.io.File
import javax.inject.Inject
import kotlin.collections.ArrayList

class ChatManager private constructor(private val scope: CoroutineScope, val onGetMessage: (start: Int, end: Int) -> Unit){
    private var messagesCount = 0
    var downloadedInRealTimeCount = 0
    private set

    var messagesList: ArrayList<Message> = ArrayList()
        set(list) {
            messagesList.clear()
            messagesList.addAll(list)
        }
    private var databaseList = ArrayList<Message>()
    private val messageDao: MessageDao = appComponent.messageDao

    @Inject
    lateinit var chatRepository: ChatRepository
    @Inject
    lateinit var chatListener: ChatListener

    private val flow: MutableSharedFlow<ChatEvent> = MutableSharedFlow()

    val listener: SharedFlow<ChatEvent>
        get() = flow.asSharedFlow()

    init { init() }


    private suspend fun getMoreMessage(count: Int) = withContext(Dispatchers.IO) {
        messagesCount += count

        val fromMessage = if (messagesList.isEmpty()) Message() else messagesList[messagesList.size - 1]

        val list = scope.async(Dispatchers.IO) {
            chatRepository.getMoreMessage(fromMessage, count)
        }.await()

        val oldSize = messagesList.size
        messagesList = ArrayList(list)
//        messagesList.addAll(0, list)
        withContext(Dispatchers.Main) {
            onGetMessage(0, list.size - oldSize - downloadedInRealTimeCount)
        }
    }

    companion object {

        private var manager: ChatManager? = null
        fun getInstance(scope: CoroutineScope, onGetMessage: (start: Int, end: Int) -> Unit): ChatManager {
            synchronized(ChatManager::class.java) {
                return manager ?: ChatManager(scope, onGetMessage)
            }
        }
    }

    fun init() {
        appComponent.inject(this)
        scope.launch(Dispatchers.IO) {
            getMoreMessage(startMessageCount)
        }
        initListener()
    }

    private fun initListener() {
        scope.launch(Dispatchers.IO) {
            chatListener.listener.collect {
                downloadedInRealTimeCount += 1
                when(it.event) {
                    EChatEvent.added -> messagesList.add(it.message)
                }
                flow.emit(it)
            }
        }
    }


    fun sendImage(photoUri: Uri) {
        chatRepository.sendImage(photoUri)
    }

    fun sendVoice(file: File, key: String) {
        chatRepository.sendVoice(file, key)
    }

    fun sendMessage(typeTextMessage: String, toString: String) {
        chatRepository.sendMessage(typeTextMessage, toString)
    }

    fun getMoreMessage(){
        scope.launch {
            getMoreMessage(downloadMessagesCountStep)
        }
    }
}

