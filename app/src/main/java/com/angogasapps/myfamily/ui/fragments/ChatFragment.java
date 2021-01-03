package com.angogasapps.myfamily.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.ChatFunks;
import com.angogasapps.myfamily.objects.ChatTextWatcher;
import com.angogasapps.myfamily.objects.LoadFamilyThread;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.ui.customview.ChatAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.NODE_CHAT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.TYPE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.USER;


public class ChatFragment extends Fragment {
    public RecyclerView chatRecycleView;
    public CircleImageView sendMessageBtn, sendAudioBtn;
    public EditText chatEditText;
    private ChatAdapter mAdapter;
    private ValueEventListener mChatListener;
    private DatabaseReference path;
    private ArrayList<Message> messagesList;

    public static IRedrawRecyclerView iRedrawRecyclerView;

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
            chatEditText.setText("");
        });
        initRecycleView();

        //test



        iRedrawRecyclerView = () -> {
            ChatFragment.this.mAdapter.notifyDataSetChanged();
        };

        return rootView;
    }

    private void initRecycleView(){
        messagesList = new ArrayList<>();
        path = DATABASE_ROOT.child(NODE_CHAT).child(USER.getFamily());

        mAdapter = new ChatAdapter(getActivity().getApplicationContext(), messagesList);
        chatRecycleView.setAdapter(mAdapter);

        chatRecycleView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        chatRecycleView.setHasFixedSize(true);

        mChatListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Message> cachedMessages = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Message post = postSnapshot.getValue(Message.class);
                    cachedMessages.add(post);
                }
                messagesList = cachedMessages;
                mAdapter.setMessanges(messagesList);
                chatRecycleView.smoothScrollToPosition(mAdapter.getItemCount());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
    }
    @Override
    public void onPause() {
        super.onPause();
        path.removeEventListener(mChatListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        path.addValueEventListener(mChatListener);
    }

    public static void redrawRecyclerView(){
    }

    public interface IRedrawRecyclerView{
         public void redrawRecyclerView();
    }
}