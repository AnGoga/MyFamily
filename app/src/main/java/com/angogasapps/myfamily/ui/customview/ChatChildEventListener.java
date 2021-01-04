package com.angogasapps.myfamily.ui.customview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.ui.fragments.ChatFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;



public class ChatChildEventListener implements ChildEventListener {
    ChatFragment chatFragment;

    public ChatChildEventListener(ChatFragment chatFragment) {
        this.chatFragment = chatFragment;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Message message = snapshot.getValue(Message.class);
        chatFragment.mAdapter.addMessage(message, chatFragment.isFirstDownload);
        if (chatFragment.isFirstDownload)
            chatFragment.mRecycleView.smoothScrollToPosition(chatFragment.mAdapter.getItemCount());
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
