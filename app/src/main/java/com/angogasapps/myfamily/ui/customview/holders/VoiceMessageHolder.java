package com.angogasapps.myfamily.ui.customview.holders;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.ChatFunks;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.ui.customview.ChatVoiceMessageView;
import com.angogasapps.myfamily.utils.StringFormater;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.UID;
import static com.angogasapps.myfamily.utils.WithUsers.getMemberImageById;
import static com.angogasapps.myfamily.utils.WithUsers.getMemberNameById;


public class VoiceMessageHolder extends AppBaseViewHolder{
    ChatVoiceMessageView leftVoiceMessageView, rightVoiceMessageView;
    public VoiceMessageHolder(@NonNull View itemView) {
        super(itemView);
        leftVoiceMessageView = view.findViewById(R.id.left_voice_view);
        rightVoiceMessageView = view.findViewById(R.id.right_voice_view);
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
