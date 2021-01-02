package com.angogasapps.myfamily.objects;

public class Message {
    private String from = "";
    private String type = "";
    private long time;
    private Object value;

    public String getFrom() {
        return from;
    }

    public String getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public Object getValue() {
        return value;
    }
}
