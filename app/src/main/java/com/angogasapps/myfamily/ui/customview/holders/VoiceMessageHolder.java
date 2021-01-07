package com.angogasapps.myfamily.ui.customview.holders;

import android.view.View;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.customview.ChatVoiceMessageView;

import static com.angogasapps.myfamily.utils.WithFiles.setVoiceLenToTextView;


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

//        setVoiceLenToTextView(messageKey, value, leftVoiceMessageView.getVoiceLenTextView(), activity);
    }

    @Override
    protected void initRightFields() {
        rightVoiceMessageView.setFromName(from);
        rightVoiceMessageView.setMessageKey(messageKey);
        rightVoiceMessageView.setTime(time);
        rightVoiceMessageView.setVoiceFileUrl(value);

//        setVoiceLenToTextView(messageKey, value, rightVoiceMessageView.getVoiceLenTextView(), activity);
    }
}
