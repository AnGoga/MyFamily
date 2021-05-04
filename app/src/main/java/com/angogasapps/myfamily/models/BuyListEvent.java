package com.angogasapps.myfamily.models;

import com.angogasapps.myfamily.models.BuyList;

public class BuyListEvent {
    private int index;
    private IEvents events;
    private String buyListId;

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public IEvents getEvent() {
        return events;
    }
    public void setEvents(IEvents events) {
        this.events = events;
    }

    public String getBuyListId() {
        return buyListId;
    }
    public void setBuyListId(String buyListId) {
        this.buyListId = buyListId;
    }

    public enum IEvents{
        productAdded, productRemoved, productChanged, buyListRemoved, buyListAdded, buyListChanged
    }
}


