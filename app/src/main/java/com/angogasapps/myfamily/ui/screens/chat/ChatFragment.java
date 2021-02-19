package com.angogasapps.myfamily.ui.screens.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.database.DatabaseManager;
import com.angogasapps.myfamily.firebase.ChatFunks;
import com.angogasapps.myfamily.objects.ChatTextWatcher;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.ui.customview.message_recycle_view.ChatAdapter;
import com.angogasapps.myfamily.objects.ChatChildEventListener;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.angogasapps.myfamily.objects.ChatAudioRecorder;
import com.angogasapps.myfamily.utils.Others;
import com.angogasapps.myfamily.utils.Permissions;
import com.google.firebase.database.DatabaseReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.getMessageKey;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_CHAT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_TEXT_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;


public class ChatFragment extends Fragment {

    public static final int downloadMessagesCountStep = 10;
    public static final int dangerFirstVisibleItemPosition = 3;
    private int messagesCount = 15;
    private boolean isScrolling = false;
    public boolean isScrollToBottom = true;

    private ChatAudioRecorder mRecorder;

    public RecyclerView mRecycleView;
    public CircleImageView sendMessageBtn, sendAudioBtn;
    public EditText chatEditText;
    public ChatAdapter mAdapter;
    private ImageView mImageViewClip;

    private LinearLayoutManager mLayoutManager;
    private ChatChildEventListener mChatListener;
    private DatabaseReference chatPath;
    private ArrayList<Message> messagesList = new ArrayList<>();

    private volatile Subject<Message> subject;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView: ");
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        mRecycleView = rootView.findViewById(R.id.chatRecycleView);
        sendMessageBtn = rootView.findViewById(R.id.chatSendMessageButton);
        chatEditText = rootView.findViewById(R.id.chatEditText);
        sendAudioBtn = rootView.findViewById(R.id.chatAudioButton);
        mImageViewClip = rootView.findViewById(R.id.chat_btn_clip);


        chatEditText.addTextChangedListener(new ChatTextWatcher(ChatFragment.this));

        sendMessageBtn.setOnClickListener(v -> {
            isScrollToBottom = true;
            ChatFunks.sendMessage(TYPE_TEXT_MESSAGE, chatEditText.getText().toString());
            chatEditText.setText("");
            mRecycleView.smoothScrollToPosition(mAdapter.getItemCount());
        });

        sendAudioBtn.setOnTouchListener((v, event) -> {

            if (Permissions.havePermission(Permissions.AUDIO_RECORD_PERM, getActivity())) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRecorder = new ChatAudioRecorder(getActivity(), getMessageKey());
                    mRecorder.startRecording();
                }else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mRecorder.stopRecording(() -> {
                        File voiceFile = mRecorder.getFile();
                        ChatFunks.sendVoice(voiceFile, mRecorder.getKey());
                        Toaster.success(getActivity(), "Звук").show();
                        System.out.println(voiceFile.toString());
                    });
                }
            }
            return true;
        });

        mImageViewClip.setOnClickListener(v -> {
            getPhotoUri();
        });

        initRecycleView();
        initChatListener();


        return rootView;
    }

    private void initChatListener(){
        mChatListener = new ChatChildEventListener(this::onAddMessage);
    }

    private void initRecycleView(){
        chatPath = DATABASE_ROOT.child(NODE_CHAT).child(USER.getFamily());
        if (mAdapter == null)
            mAdapter = new ChatAdapter(getActivity(), messagesList);
        mRecycleView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);
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
                if (isScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= dangerFirstVisibleItemPosition){
                    updateData();
                }
            }
        });

    }
    private void updateData() {
        isScrolling = false;
        isScrollToBottom = false;

        messagesCount += downloadMessagesCountStep;
        chatPath.removeEventListener(mChatListener);
        chatPath.limitToLast(messagesCount).addChildEventListener(mChatListener);
    }

    private void getPhotoUri() {
        CropImage.activity().setAspectRatio(1, 1)
                .setRequestedSize(300, 300)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(getActivity(), this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {

            Uri photoUri = CropImage.getActivityResult(data).getUri();
            if (photoUri != null)
                ChatFunks.sendImage(photoUri);
            else
                Toaster.error(getActivity(), "Что-то пошло не так").show();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        chatPath.removeEventListener(mChatListener);
    }

    @Override
    public void onStart() {
        loadMessages();
        super.onStart();
        chatPath.limitToLast(messagesCount).addChildEventListener(mChatListener);
    }

    public void onAddMessage(Message message){
        mAdapter.addMessage(message, isScrollToBottom);
        if (isScrollToBottom)
            mRecycleView.smoothScrollToPosition(mAdapter.getItemCount());

        subject.onNext(message);
    }

    public void setSubject(Subject<Message> subject) {
        this.subject = subject;
    }

    public void setMessagesList(ArrayList<Message> messagesList) {
        this.messagesList = messagesList;
    }


    private void loadMessages() {
        if (!AppApplication.isOnline() && messagesList.isEmpty()){
            Others.runInNewThread(() -> {
                while(!DatabaseManager.messagesLoadIsEnd){}

                if (DatabaseManager.messagesLoadIsEnd)
                    for (Message message : DatabaseManager.getMessageList())
                        onAddMessage(message);
            });
        }
    }

}