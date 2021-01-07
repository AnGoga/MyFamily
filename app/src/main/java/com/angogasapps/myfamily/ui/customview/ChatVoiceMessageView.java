package com.angogasapps.myfamily.ui.customview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.utils.ChatVoicePlayer;

import java.io.File;

public class ChatVoiceMessageView extends LinearLayout {
    private TextView fromNameView, progressTextView, timeTextView;
    private ImageView playButton, pauseButton;
    private SeekBar progressBar;

    public String messageKey;
    public String voiceFileUrl;
    private ChatVoicePlayer.IOnEndPlay iOnEndPlay;

    private ChatVoicePlayer mVoicePlayer;
    private Context context;


    public ChatVoiceMessageView(Context context) {
        super(context);
        initializeViews(context);
    }

    public ChatVoiceMessageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public ChatVoiceMessageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        this.context = context;
        @SuppressLint("ServiceCast")
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.voice_message_view, this);
    }

    public void setFromName(String name){
        this.fromNameView.setText(name);
    }
    public void setTime(String time){
        this.timeTextView.setText(time);
    }
    public void setMessageKey(String key){
        this.messageKey = key;
    }
    public void setVoiceFileUrl(String url){
        this.voiceFileUrl = url;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        fromNameView = findViewById(R.id.voice_message_from_name_text);
        progressTextView = findViewById(R.id.voice_message_progress_text);
        timeTextView = findViewById(R.id.voice_message_message_time_text);
        playButton = findViewById(R.id.voice_message_btn_play);
        pauseButton = findViewById(R.id.voice_message_btn_pause);
        progressBar = findViewById(R.id.voice_message_seek_bar);

        mVoicePlayer = new ChatVoicePlayer(context);
        mVoicePlayer.init();

        iOnEndPlay = () -> {
            playButton.setVisibility(VISIBLE);
            pauseButton.setVisibility(INVISIBLE);
        };

        playButton.setOnClickListener(v -> {
            mVoicePlayer.play(this.messageKey, this.voiceFileUrl, iOnEndPlay);

            playButton.setVisibility(INVISIBLE);
            pauseButton.setVisibility(VISIBLE);
        });

        pauseButton.setOnClickListener(v -> {
            mVoicePlayer.stop(iOnEndPlay);
            playButton.setVisibility(VISIBLE);
            pauseButton.setVisibility(INVISIBLE);
        });


    }
}
