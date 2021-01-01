package com.angogasapps.myfamily.ui.customview;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.angogasapps.myfamily.ui.fragments.ChatFragment;

public class ChatTextWatcher implements TextWatcher {
    private ChatFragment chatFragment;

    public ChatTextWatcher(ChatFragment chatFragment) {
        this.chatFragment = chatFragment;
    }
    //до
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    //после
    @Override
    public void afterTextChanged(Editable s) {
        String text = chatFragment.chatEditText.getText().toString();
        if (text.replaceAll("\\s+","").isEmpty()){
            chatFragment.sendMessageBtn.setVisibility(View.INVISIBLE);
            chatFragment.sendAudioBtn.setVisibility(View.VISIBLE);
        }else{
            chatFragment.sendMessageBtn.setVisibility(View.VISIBLE);
            chatFragment.sendAudioBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


}
