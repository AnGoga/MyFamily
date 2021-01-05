package com.angogasapps.myfamily.objects;

import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;

public class Message implements Comparable<Message>{
    private String from;
    private String type;
    private long time;
    private Object value;
    private String id = "";

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

    public String getId(){
        return id;
    }


    public boolean equals(Message obj) {
        return (this.id.equals(obj.getId()));
    }

    public Message() {}

    public Message(DataSnapshot snapshot) {
        Message cash = snapshot.getValue(Message.class);
        this.id = snapshot.getKey();
        this.from = cash.getFrom();
        this.type = cash.getType();
        this.time = cash.getTime();
        this.value = cash.getValue();
    }

    @Override
    public int compareTo(Message obj) {
        if (this.time > obj.getTime()) {
            return 1;
        }
        else if (this.time <  obj.getTime()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
