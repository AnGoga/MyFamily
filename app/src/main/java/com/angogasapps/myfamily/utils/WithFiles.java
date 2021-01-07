package com.angogasapps.myfamily.utils;

import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.TextView;

import com.angogasapps.myfamily.firebase.ChatFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;

import java.io.File;
import java.io.IOException;

public class WithFiles {

    public static void setVoiceLenToTextView(String messageKey, String fileUrl, TextView textView, Activity activity){

        File voiceFile = new File(activity.getFilesDir().getAbsolutePath(), messageKey);

        if (voiceFile.exists() && voiceFile.isFile() && voiceFile.length() > 0){
            textView.setText((int)getVoiceFileLen(voiceFile) / 1000);
        }else {
            try {
                voiceFile.createNewFile();
                ChatFunks.getFileFromStorage(voiceFile, fileUrl, new IOnEndCommunicationWithFirebase() {
                    @Override
                    public void onSuccess() {
                        textView.setText(String.valueOf((int)getVoiceFileLen(voiceFile) / 1000));
                        return;
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private static int getVoiceFileLen(File file){
        try {

            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(file.getAbsolutePath());
            return mp.getDuration();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
