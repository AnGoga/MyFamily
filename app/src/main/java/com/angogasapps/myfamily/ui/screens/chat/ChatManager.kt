package com.angogasapps.myfamily.ui.screens.chat

import android.util.Log
import com.angogasapps.myfamily.database.DatabaseManager
import com.angogasapps.myfamily.database.MessageDao
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.objects.ChatChildEventListener
import com.google.firebase.database.ServerValue
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ChatManager private constructor(private val scope: CoroutineScope, val onGetMessage: (event: ChatEvent) -> Unit){
    private var messagesCount = 0

    var list: ArrayList<Message> = ArrayList()
    private var databaseList = ArrayList<Message>()
    private val chatRef = DATABASE_ROOT.child(NODE_CHAT).child(USER.family)
    private val messageDao: MessageDao = DatabaseManager.getInstance().messageDao
    private val listener: ChatChildEventListener = ChatChildEventListener(this::onGetMessageFromFirebase)


    val subject = PublishSubject.create<ChatEvent>()

    init { init() }

    companion object {
        const val downloadMessagesCountStep = 10
        const val dangerFirstVisibleItemPosition = 3
        const val startMessageCount = 15

        private var manager: ChatManager? = null
        fun getInstance(scope: CoroutineScope, onGetMessage: (event: ChatEvent) -> Unit): ChatManager{
            synchronized(ChatManager::class.java) {
                return manager ?: ChatManager(scope, onGetMessage)
            }
        }
    }

    fun init() {
        getMoreMessage(startMessageCount)
    }

    private fun addMessage(message: Message): Job = scope.launch(Dispatchers.Main) {
        synchronized(ChatManager::class.java) {
            if (list.contains(message))
                return@launch
            if (list.size >= downloadMessagesCountStep)
                list.subList(0, downloadMessagesCountStep - 1).sortBy { it.time }
            else
                list.sortBy { it.time }

            var index = list.size

            if (list.size == 0) {
                list.add(message)

            } else if (list[list.size - 1].time >= message.time) {
                list.add(0, message)
                index = 0
            } else {
                list.add(message)
            }

            saveMessageLocal(message)

            val event = ChatEvent(EChatEvent.added, message, index)
            Log.d("chat_event", "id = ${message.id}")
            onGetMessage(event)

        }
    }
    fun getMoreMessage(){
        getMoreMessage(downloadMessagesCountStep)
    }

    private fun getMoreMessage(count: Int){
        messagesCount += count
        getMessageFromDatabase()
        getMessageFromFirebase()
    }

    private fun getMessageFromDatabase() = scope.launch {
        synchronized(messageDao){
            if (databaseList.isEmpty()) {
                databaseList = ArrayList(messageDao.all)
                databaseList.sortBy { it.time }
            }
            if (databaseList.size <= messagesCount){
                for (i in 0..(databaseList.size - 1) ) {
                    addMessage(databaseList[i])
                }
            }else  if (databaseList.size - messagesCount >= 0){
//                for (i in (databaseList.size - messagesCount)..(databaseList.size - 1)) {
                for (i in (databaseList.size - 1) downTo (databaseList.size - messagesCount)) {
                    val message = databaseList[i]
                    addMessage(message)
                }
            }
        }
    }

    private fun getMessageFromFirebase() = synchronized(listener) {
        chatRef.removeEventListener(listener)
        chatRef.limitToLast(messagesCount).addChildEventListener(listener)
    }


    private fun saveMessageLocal(message: Message) = scope.launch(Dispatchers.IO) {
        messageDao.insert(message)
    }

    private fun onGetMessageFromFirebase(message: Message) {
        println(message.id)
        addMessage(message)
    }
}

