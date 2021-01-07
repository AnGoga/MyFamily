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

    public void initVoiceHolder(Activity activity, Message message) {
        if (message.getFrom().equals(UID)){
            leftLayout.setVisibility(View.INVISIBLE);
            rightLayout.setVisibility(View.VISIBLE);

            rightVoiceMessageView.setFromName(getMemberNameById(message.getFrom()));
            rightVoiceMessageView.setMessageKey(message.getId());
            rightVoiceMessageView.setTime(StringFormater.formatLongToTime(message.getTime()));
            rightVoiceMessageView.setVoiceFileUrl(message.getValue().toString());
        }else{
            leftLayout.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.INVISIBLE);

            leftVoiceMessageView.setFromName(getMemberNameById(message.getFrom()));
            leftVoiceMessageView.setMessageKey(message.getId());
            leftVoiceMessageView.setTime(StringFormater.formatLongToTime(message.getTime()));
            leftVoiceMessageView.setVoiceFileUrl(message.getValue().toString());
            userAvatar.setImageBitmap(getMemberImageById(message.getFrom(), activity));
        }
    }
}
