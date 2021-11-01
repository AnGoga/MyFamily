package com.angogasapps.myfamily.ui.screens.chat

import kotlin.collections.ArrayList
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.Message
import com.angogasapps.myfamily.ui.screens.chat.holders.AppBaseViewHolder
import com.angogasapps.myfamily.ui.screens.chat.holders.ImageMessageHolder
import com.angogasapps.myfamily.ui.screens.chat.holders.TextMessageHolder
import com.angogasapps.myfamily.ui.screens.chat.holders.VoiceMessageHolder
import kotlinx.coroutines.GlobalScope

class ChatAdapter(private val activity: Activity, val messagesList: ArrayList<Message>) : RecyclerView.Adapter<AppBaseViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(activity)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppBaseViewHolder {
        return when (viewType) {
            0 -> TextMessageHolder(inflater.inflate(R.layout.text_message_holder, parent, false))
            1 -> ImageMessageHolder(inflater.inflate(R.layout.image_message_holder, parent, false))
            2 -> VoiceMessageHolder(inflater.inflate(R.layout.voice_message_holder, parent, false))

            else -> TextMessageHolder(inflater.inflate(R.layout.text_message_holder, parent, false))
        }
    }

    override fun onBindViewHolder(holder: AppBaseViewHolder, position: Int) {
        val message = messagesList[position]
        holder.init(message.from, message.time, message.id, message.value, activity)
        if (message.from == UID) {
            holder.initRightLayout()
        } else {
            holder.initLeftLayout()
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (messagesList[position].type) {
            TYPE_TEXT_MESSAGE -> return 0
            TYPE_IMAGE_MESSAGE -> return 1
            TYPE_VOICE_MESSAGE -> return 2
        }
        return -1
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    fun bindEvent(event: ChatEvent) {
        when(event.event){
            EChatEvent.added -> {
                notifyItemInserted(messagesList.size - 1)
            }
        }
    }

    fun addInRange(start: Int, end: Int) {
        notifyItemRangeInserted(start, end)
    }
}