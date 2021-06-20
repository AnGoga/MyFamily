package com.angogasapps.myfamily.ui.screens.chat

import kotlin.collections.ArrayList
import android.app.Activity
import android.util.Log
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

    fun update(event: ChatEvent) {
        when(event.event){
            EChatEvent.added -> {
                notifyItemInserted(event.index)
            }
        }
    }
}