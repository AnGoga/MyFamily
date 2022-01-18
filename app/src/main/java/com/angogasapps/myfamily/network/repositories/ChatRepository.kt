package com.angogasapps.myfamily.network.repositories

import com.angogasapps.myfamily.database.MessageDao
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.network.interfaces.chat.ChatGetter
import com.angogasapps.myfamily.network.interfaces.chat.ChatSender
import com.angogasapps.myfamily.network.interfaces.chat.ChatService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import com.angogasapps.myfamily.network.Result
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


@Singleton
class ChatRepository @Inject constructor(
    val chatService: ChatService,
    private val messageDao: MessageDao
    ): ChatSender by chatService, ChatGetter {

    private val mutex = Mutex()
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())


    override suspend fun getMoreMessage(fromMessage: Message, count: Int): List<Message> {
//        getMessagesFromDatabase()
        mutex.withLock {
            val def = scope.async {
                getMessagesFromNetwork(fromMessage, count)
            }

            return when (val res = def.await()) {
                is Result.Error -> {
                    res.e.printStackTrace()
                    ArrayList()
                }
                is Result.Success -> {
                    res.data
                }
            }
        }
    }

    private suspend fun getMessagesFromNetwork(fromMessage: Message, count: Int): Result<List<Message>> {
        return chatService.getMoreMessages(fromMessage, count)
    }

    private suspend fun getMessagesFromDatabase() {

    }

/*
    private fun getMessageFromDatabase() = scope.launch(Dispatchers.IO) {
        synchronized(messageDao) {
            if (databaseList.isEmpty()) {
                databaseList = ArrayList(messageDao.all)
                databaseList.sortBy { it.time }
            }
            if (databaseList.size <= messagesCount){
                for (i in 0 until databaseList.size) {
                    tryAddMessage(databaseList[i])
                }
            }else  if (databaseList.size - messagesCount >= 0){
                for (i in (databaseList.size - 1) downTo (databaseList.size - messagesCount)) {
                    val message = databaseList[i]
                    tryAddMessage(message)
                }
            }
        }
    }
*/
}