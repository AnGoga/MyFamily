package com.angogasapps.myfamily.models.events;

import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

public class NewsObject {
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";

    public static final long timeLive = 8 * 60 * 60 * 1000;

    private String fromPhone = "";
    private String id = "";
    private String type = "";
    private String value = "";
    private long timeCreate;


    public static NewsObject from(DataSnapshot snapshot){
        NewsObject object = snapshot.getValue(NewsObject.class);
        object.setId(snapshot.getKey());
        return object;
    }


    public String getFromPhone() {
        return fromPhone;
    }

    public void setFromPhone(String fromPhone) {
        this.fromPhone = fromPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(long timeCreate) {
        this.timeCreate = timeCreate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsObject that = (NewsObject) o;
        return Objects.equals(fromPhone, that.fromPhone) &&
                Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(value, that.value);
    }

    public static boolean isCanLife(NewsObject object){
        return  (object.getTimeCreate() + timeLive > System.currentTimeMillis());
    }
}
