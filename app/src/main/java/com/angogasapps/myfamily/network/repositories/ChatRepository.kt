package com.angogasapps.myfamily.network.repositories

import com.angogasapps.myfamily.database.MessageDao
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.network.interfaces.ChatGetter
import com.angogasapps.myfamily.network.interfaces.ChatSender
import com.angogasapps.myfamily.network.interfaces.ChatService
import com.angogasapps.myfamily.ui.screens.chat.*
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import com.angogasapps.myfamily.network.Result


@Singleton
class ChatRepository @Inject constructor(
        private val chatService: ChatService,
        private val messageDao: MessageDao
    ): ChatSender by chatService, ChatGetter {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())


    override suspend fun getMoreMessage(fromMessage: Message, count: Int): List<Message> {
//        getMessagesFromDatabase()

        val def = scope.async {
            getMessagesFromNetwork(fromMessage, count)
        }

        return when(val res = def.await()) {
            is Result.Error -> {
                ArrayList()
            }
            is Result.Success -> {
                res.data
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