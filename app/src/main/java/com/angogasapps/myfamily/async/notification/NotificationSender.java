package com.angogasapps.myfamily.async.notification;

public class NotificationSender {
    public Data data;
    public String to;

    public NotificationSender(Data data, String to){
        this.data = data;
        this.to = to;
    }

    public static class Data{
        public String title;
        public String message;

        public Data(String title, String  message){
            this.title = title;
            this.message = message;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
