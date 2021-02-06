package com.angogasapps.myfamily.utils;

public class Others {

    public static void runInNewThread(doInThread inThread){
        new Thread(){
            @Override
            public void run() {
                super.run();
                inThread.run();
            }
        }.start();
    }
    public interface doInThread {
        void run();
    }
}
