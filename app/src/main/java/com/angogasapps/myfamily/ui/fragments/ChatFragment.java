package com.angogasapps.myfamily.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.ChatFunks;
import com.angogasapps.myfamily.ui.customview.ChatTextWatcher;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.TYPE_MESSAGE;


public class ChatFragment extends Fragment {
    public RecyclerView chatRecycleView;
    public CircleImageView sendMessageBtn, sendAudioBtn;
    public EditText chatEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        chatRecycleView = rootView.findViewById(R.id.chatRecycleView);
        sendMessageBtn = rootView.findViewById(R.id.chatSendMessageButton);
        chatEditText = rootView.findViewById(R.id.chatEditText);
        sendAudioBtn = rootView.findViewById(R.id.chatAudioButton);

        chatEditText.addTextChangedListener(new ChatTextWatcher(ChatFragment.this));

        sendMessageBtn.setOnClickListener(v -> {
            ChatFunks.sendMessage(TYPE_MESSAGE, chatEditText.getText().toString());
        });

        return rootView;
    }
}