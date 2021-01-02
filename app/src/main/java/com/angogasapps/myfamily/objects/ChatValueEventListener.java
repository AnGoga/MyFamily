package com.angogasapps.myfamily.objects;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ChatValueEventListener implements ValueEventListener {
    IOnDataChange iOnDataChange;

    public ChatValueEventListener(IOnDataChange iOnDataChange) {
        this.iOnDataChange = iOnDataChange;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        iOnDataChange.onSuccess();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    public interface IOnDataChange{
        void onSuccess();
    }
}
