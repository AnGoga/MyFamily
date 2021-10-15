package com.angogasapps.myfamily.ui.screens.chat.holders

import android.view.View
import com.angogasapps.myfamily.ui.customview.ChatVoiceMessageView
import com.angogasapps.myfamily.R

class VoiceMessageHolder(rootView: View) : AppBaseViewHolder(rootView) {
    var leftVoiceMessageView: ChatVoiceMessageView = rootView.findViewById(R.id.left_voice_view)
    var rightVoiceMessageView: ChatVoiceMessageView = rootView.findViewById(R.id.right_voice_view)

    override fun initLeftFields() {
        leftVoiceMessageView.setFromName(name)
        leftVoiceMessageView.messageKey = messageKey
        leftVoiceMessageView.setTime(time)
        leftVoiceMessageView.voiceFileUrl = value
    }

    override fun initRightFields() {
        rightVoiceMessageView.setFromName(name)
        rightVoiceMessageView.messageKey = messageKey
        rightVoiceMessageView.setTime(time)
        rightVoiceMessageView.voiceFileUrl = value
    }
}