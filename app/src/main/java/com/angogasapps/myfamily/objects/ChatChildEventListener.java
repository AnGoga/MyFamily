package com.angogasapps.myfamily.objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.ui.screens.chat.ChatFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;



public class ChatChildEventListener implements ChildEventListener {
//    ChatFragment chatFragment;
    IOnAddMessage iOnAddMessage;

//    public ChatChildEventListener(ChatFragment chatFragment) {
//        this.chatFragment = chatFragment;
//    }
    public ChatChildEventListener(IOnAddMessage iOnAddMessage){
        this.iOnAddMessage = iOnAddMessage;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Message message = new Message(snapshot);
        iOnAddMessage.onAddMessage(message);
//        chatFragment.mAdapter.addMessage(message, chatFragment.isScrollToBottom);
//        if (chatFragment.isScrollToBottom)
//            chatFragment.mRecycleView.smoothScrollToPosition(chatFragment.mAdapter.getItemCount());
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

    public interface IOnAddMessage{
        void onAddMessage(Message message);
    }
}
