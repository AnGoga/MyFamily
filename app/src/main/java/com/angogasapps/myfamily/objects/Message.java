package com.angogasapps.myfamily.objects;

public class Message {
    private String from = "";
    private String type = "";
    private long timestamp;
    private Object value;

    public String getFrom() {
        return from;
    }

    public String getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Object getValue() {
        return value;
    }
}
