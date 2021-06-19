package com.angogasapps.myfamily.ui.screens.chat

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.ui.screens.chat.holders.AppBaseViewHolder
import com.angogasapps.myfamily.ui.screens.chat.holders.ImageMessageHolder
import com.angogasapps.myfamily.ui.screens.chat.holders.TextMessageHolder
import com.angogasapps.myfamily.ui.screens.chat.holders.VoiceMessageHolder
import com.angogasapps.myfamily.utils.ChatAdapterUtils
import java.util.*

class ChatAdapter(private val activity: Activity, var messagesList: ArrayList<Message>) : RecyclerView.Adapter<AppBaseViewHolder>() {
    private val context: Context = activity.applicationContext
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppBaseViewHolder {
        when (viewType) {
            0 -> return TextMessageHolder(inflater.inflate(R.layout.text_message_holder, parent, false))
            1 -> return ImageMessageHolder(inflater.inflate(R.layout.image_message_holder, parent, false))
            2 -> return VoiceMessageHolder(inflater.inflate(R.layout.voice_message_holder, parent, false))

            else -> return TextMessageHolder(inflater.inflate(R.layout.text_message_holder, parent, false))
        }
    }

    override fun onBindViewHolder(holder: AppBaseViewHolder, position: Int) {
        val message = messagesList[position]
        holder.init(message.from, message.time, message.id, message.value, activity)
        if (message.from == FirebaseVarsAndConsts.UID) {
            holder.initRightLayout()
        } else {
            holder.initLeftLayout()
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (messagesList[position].type) {
            FirebaseVarsAndConsts.TYPE_TEXT_MESSAGE -> return 0
            FirebaseVarsAndConsts.TYPE_IMAGE_MESSAGE -> return 1
            FirebaseVarsAndConsts.TYPE_VOICE_MESSAGE -> return 2
        }
        return -1
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    fun addMessage(message: Message) {
        messagesList = ChatAdapterUtils.sortMessagesList(messagesList)

        if (messagesList.contains(message))
            return

        if (messagesList.size == 0) {
            messagesList.add(message)
            activity.runOnUiThread { notifyItemInserted(0) }
            return
        }
        if (message.time >= messagesList[messagesList.size - 1].time) {
            messagesList.add(message)
            activity.runOnUiThread { notifyItemInserted(messagesList.size - 1) }
        } else {
            messagesList.add(0, message)
            activity.runOnUiThread { notifyItemInserted(0) }
        }
    }
}