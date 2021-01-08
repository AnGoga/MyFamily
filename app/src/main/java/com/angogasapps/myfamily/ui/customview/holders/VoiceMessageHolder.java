package com.angogasapps.myfamily.ui.customview.holders;

import android.view.View;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.customview.ChatVoiceMessageView;



public class VoiceMessageHolder extends AppBaseViewHolder{
    ChatVoiceMessageView leftVoiceMessageView, rightVoiceMessageView;
    public VoiceMessageHolder(@NonNull View itemView) {
        super(itemView);
        leftVoiceMessageView = itemView.findViewById(R.id.left_voice_view);
        rightVoiceMessageView = itemView.findViewById(R.id.right_voice_view);
    }

    @Override
    protected void initLeftFields() {
        leftVoiceMessageView.setFromName(from);
        leftVoiceMessageView.setMessageKey(messageKey);
        leftVoiceMessageView.setTime(time);
        leftVoiceMessageView.setVoiceFileUrl(value);

    }

    @Override
    protected void initRightFields() {
        rightVoiceMessageView.setFromName(from);
        rightVoiceMessageView.setMessageKey(messageKey);
        rightVoiceMessageView.setTime(time);
        rightVoiceMessageView.setVoiceFileUrl(value);


    }

}
