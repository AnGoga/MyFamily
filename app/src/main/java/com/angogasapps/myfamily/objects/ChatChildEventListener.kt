package com.angogasapps.myfamily.objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.angogasapps.myfamily.models.Message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;



public class ChatChildEventListener implements ChildEventListener {
    private IOnAddMessage iOnAddMessage;

    public ChatChildEventListener(IOnAddMessage iOnAddMessage){
        this.iOnAddMessage = iOnAddMessage;
    }



    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Message message = new Message(snapshot);
        iOnAddMessage.onAddMessage(message);
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
