package com.angogasapps.myfamily.ui.screens.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.screens.chat.ChatFragment;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.chatActivityDataContainer, new ChatFragment()).commit();
    }
}