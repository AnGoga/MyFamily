package com.angogasapps.myfamily.utils;

import android.app.Activity;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

public class AudioRecorder{

    private volatile Activity activity;
    private volatile File file;
    private volatile String key;
    private volatile MediaRecorder recorder;


    public AudioRecorder(Activity activity, String key) {
        this.activity = activity;
        this.key = key;
        this.recorder = new MediaRecorder();
    }



    public void startRecording() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    AudioRecorder.this.file = createNewFile();
                    prepareRecorder();
                    recorder.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void stopRecording(IStopRecording mInterface){
        try {
            recorder.stop();
            mInterface.onSuccess();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public File getFile(){
        return this.file;
    }

    public String getKey(){
        return this.key;
    }


    private void prepareRecorder() throws IOException {
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(file.getAbsolutePath());
        recorder.prepare();
    }

    private File createNewFile() {
        File file = new File(activity.getFilesDir(), key);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public interface IStopRecording {
        void onSuccess();
    }
}
