package com.angogasapps.myfamily.objects;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatTextWatcher implements TextWatcher {
    private CircleImageView sendButton;
    private CircleImageView audioButton;
    private EditText editText;


    public ChatTextWatcher(CircleImageView sendButton, CircleImageView audioButton, EditText editText) {
        this.sendButton = sendButton;
        this.audioButton = audioButton;
        this.editText = editText;
    }
    //до
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    //после
    @Override
    public void afterTextChanged(Editable s) {
        String text = editText.getText().toString();
        if (text.replaceAll("\\s+","").isEmpty()){
            sendButton.setVisibility(View.INVISIBLE);
            audioButton.setVisibility(View.VISIBLE);
        }else{
            sendButton.setVisibility(View.VISIBLE);
            audioButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


}
