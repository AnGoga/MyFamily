package com.angogasapps.myfamily.ui.screens.chat

import android.net.Uri
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.database.MessageDao
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.network.repositories.ChatRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import java.io.File
import javax.inject.Inject
import kotlin.collections.ArrayList

class ChatManager private constructor(private val scope: CoroutineScope, val onGetMessage: (start: Int, end: Int) -> Unit){
    private var messagesCount = 0

    var messagesList: ArrayList<Message> = ArrayList()
        set(list) {
            messagesList.clear()
            messagesList.addAll(list)
        }
    private var databaseList = ArrayList<Message>()
    private val messageDao: MessageDao = appComponent.messageDao

    val channel = BroadcastChannel<Message>(100)

    @Inject
    lateinit var chatRepository: ChatRepository

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
            onGetMessage(0, list.size - oldSize)
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

    /*
  private fun addMessage(message: Message) {
      if (list.contains(message))
          return
      if (list.size >= startMessageCount)
          list.subList(0, startMessageCount - 1).sortBy { it.time }
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
      onGetMessage(event)
  }

  private fun tryAddMessage(message: Message){
      scope.launch(Dispatchers.Main) {
          synchronized(ChatManager::class.java) {
              addMessage(message)
          }
      }
  }

   /*
  private fun saveMessageLocal(message: Message) = scope.launch(Dispatchers.IO) {
      messageDao.insert(message)
  }

  private fun onGetMessageFromFirebase(message: Message) {
      println(message.id)
      tryAddMessage(message)
  }*/
  */
}

