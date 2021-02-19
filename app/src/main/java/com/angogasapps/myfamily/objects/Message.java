package com.angogasapps.myfamily.objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.DataSnapshot;

@Entity
public class Message implements Comparable<Message>{
    @NonNull
    @PrimaryKey
    private String id = "";

    private String from;
    private String type;
    private long time;
    private String  value;


    public String getFrom() {
        return from;
    }

    public String getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public String getValue() {
        return value;
    }

    public String getId(){
        return id;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Message))
            return false;
        return this.id.equals(((Message)obj).getId());
    }

//    public boolean equals(Message obj) {
//        return (this.id.equals(obj.getId()));
//    }

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

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
