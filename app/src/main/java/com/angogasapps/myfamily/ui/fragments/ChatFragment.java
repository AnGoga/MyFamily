package com.angogasapps.myfamily.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.ChatFunks;
import com.angogasapps.myfamily.objects.ChatTextWatcher;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.ui.customview.ChatAdapter;
import com.angogasapps.myfamily.ui.customview.ChatChildEventListener;
import com.google.firebase.database.ChildEventListener;
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

    public static final int downloadMessagesCountStep = 10;
    private int messagesCount = 15;
    private boolean isScrolling = false;
    public boolean isFirstDownload = true;

    public RecyclerView mRecycleView;
    public CircleImageView sendMessageBtn, sendAudioBtn;
    public EditText chatEditText;
    public ChatAdapter mAdapter;
    private ChatChildEventListener mChatListener;
    private DatabaseReference chatPath;
    private ArrayList<Message> messagesList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        mRecycleView = rootView.findViewById(R.id.chatRecycleView);
        sendMessageBtn = rootView.findViewById(R.id.chatSendMessageButton);
        chatEditText = rootView.findViewById(R.id.chatEditText);
        sendAudioBtn = rootView.findViewById(R.id.chatAudioButton);

        chatEditText.addTextChangedListener(new ChatTextWatcher(ChatFragment.this));

        sendMessageBtn.setOnClickListener(v -> {
            isFirstDownload = true;
            ChatFunks.sendMessage(TYPE_MESSAGE, chatEditText.getText().toString());
            chatEditText.setText("");
            mRecycleView.smoothScrollToPosition(mAdapter.getItemCount());
        });
        initRecycleView();
        initChatListener();


        return rootView;
    }

    private void initChatListener(){
        mChatListener = new ChatChildEventListener(ChatFragment.this);
    }

    private void initRecycleView(){
        messagesList = new ArrayList<>();
        chatPath = DATABASE_ROOT.child(NODE_CHAT).child(USER.getFamily());

        mAdapter = new ChatAdapter(getActivity().getApplicationContext(), messagesList);
        mRecycleView.setAdapter(mAdapter);

        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecycleView.setHasFixedSize(true);

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isScrolling && dy < 0){
                    updateData();
                }
            }
        });
    }
    private void updateData() {
        isScrolling = false;
        isFirstDownload = false;

        messagesCount += downloadMessagesCountStep;
        chatPath.removeEventListener(mChatListener);
        chatPath.limitToLast(messagesCount).addChildEventListener(mChatListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        chatPath.removeEventListener(mChatListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        chatPath.limitToLast(messagesCount).addChildEventListener(mChatListener);
    }
}