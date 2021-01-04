package com.angogasapps.myfamily.objects;

import androidx.annotation.Nullable;

public class Message implements Comparable<Message>{
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


    public boolean equals(Message obj) {
        return (this.time == obj.getTime());
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
