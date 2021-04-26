package com.angogasapps.myfamily.objects;

import android.content.Context;
import android.media.MediaPlayer;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.ChatFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;


import java.io.File;

import es.dmoral.toasty.Toasty;


public class ChatVoicePlayer {
    private MediaPlayer mMediaPlayer;
    private Context context;
    private File voiceFile;

    public ChatVoicePlayer(Context context) {
        this.context = context;
    }

    public void play(String messageKey, String fileUrl, IOnEndPlay iOnEndPlay){
        voiceFile = new File(context.getFilesDir().getAbsolutePath(), messageKey);

        if (voiceFile.exists() && voiceFile.isFile() && voiceFile.length() > 0){
            startPlayer(iOnEndPlay);
        }else {
            try {
                voiceFile.createNewFile();
//                ChatFunks.getFileFromStorage(voiceFile, fileUrl, new IOnEndCommunicationWithFirebase() {
                ChatFunks.getVoiceFileFromStorage(voiceFile, messageKey, new IOnEndCommunicationWithFirebase() {
                    @Override
                    public void onSuccess() {
                        startPlayer(iOnEndPlay);
                    }

                    @Override
                    public void onFailure() {}
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startPlayer(IOnEndPlay iOnEndPlay){
        try {

            mMediaPlayer.setDataSource(voiceFile.getAbsolutePath());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(mp -> {
                stop(iOnEndPlay);
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toasty.error(context, R.string.something_went_wrong).show();
        }

    }

    public void stop(IOnEndPlay iOnEndPlay){
        try {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }catch (Exception e){
            e.printStackTrace();
            Toasty.error(context, R.string.something_went_wrong).show();
        }
        iOnEndPlay.onEndPlay();
    }

    public void release(){
        mMediaPlayer.release();
    }

    public void init(){
        mMediaPlayer = new MediaPlayer();
    }


    public interface IOnEndPlay{
        void onEndPlay();
    }
}
