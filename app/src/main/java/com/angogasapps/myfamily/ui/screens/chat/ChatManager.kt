package com.angogasapps.myfamily.ui.screens.chat

import com.angogasapps.myfamily.database.DatabaseManager
import com.angogasapps.myfamily.database.MessageDao
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.objects.ChatChildEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel

class ChatManager private constructor(private val scope: CoroutineScope){
    private var messagesCount = 0

    val list: ArrayList<Message> = ArrayList()
    private var databaseList = ArrayList<Message>()
    private val chatRef = DATABASE_ROOT.child(NODE_CHAT).child(USER.family)
    private val messageDao: MessageDao = DatabaseManager.getInstance().messageDao
    private val listener: ChatChildEventListener = ChatChildEventListener(this::addMessage)


    val subject = PublishSubject.create<ChatEvent>()

    init { init() }

    companion object {
        const val downloadMessagesCountStep = 10
        const val dangerFirstVisibleItemPosition = 3
        const val startMessageCount = 15

        private var manager: ChatManager? = null
        fun getInstance(scope: CoroutineScope): ChatManager{
            synchronized(ChatManager::class.java) {
                return manager ?: ChatManager(scope)
            }
        }
    }

    fun init() {
        getMoreMessage(startMessageCount)
    }

    private fun addMessage(message: Message): Job = scope.launch {
        synchronized(ChatManager::class.java) {

            if (list.contains(message))
                return@launch

            saveMessageLocal(message)

            if (list.size == 0) {
                list.add(message)
            } else if (list[list.size - 1].time >= message.time) {
                list.add(0, message)
            } else {
                list.add(message)
            }
            list.sortBy { it.time }
            subject.onNext(ChatEvent(EChatEvent.added, message, list.binarySearch(message)))
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

    private fun getMessageFromDatabase() = synchronized(messageDao){
        scope.launch {
            if (databaseList.isEmpty()) {
                databaseList = ArrayList(messageDao.all)
                databaseList.sortBy { it.time }
            }
            if (databaseList.size <= messagesCount){
                for (i in (databaseList.size - 1) downTo 0) {
                    addMessage(databaseList[i])
                }
            }else{
                for (i in (databaseList.size - 1) downTo (databaseList.size - messagesCount)){
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
}

