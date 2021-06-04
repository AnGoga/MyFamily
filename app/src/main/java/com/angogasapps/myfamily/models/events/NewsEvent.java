package com.angogasapps.myfamily.models.events;

public class NewsEvent {
    private int index;
    private ENewsEvents event;
    private String newsId;

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public ENewsEvents getEvent() {
        return event;
    }
    public void setEvent(ENewsEvents event) {
        this.event = event;
    }

    public String getNewsId() {
        return newsId;
    }
    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public enum ENewsEvents{
        added, removed, changed
    }
}
