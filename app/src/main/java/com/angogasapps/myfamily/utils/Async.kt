package com.angogasapps.myfamily.utils;

public class Async {

    public static Thread runInNewThread(doInThread inThread){
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                inThread.run();
            }
        };
        thread.start();
        return thread;
    }
    public interface doInThread {
        void run();
    }
}
