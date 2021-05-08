package com.angogasapps.myfamily.models;

public class BuyListEvent {
    private int index;
    private EBuyListEvents events;
    private String buyListId;

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public EBuyListEvents getEvent() {
        return events;
    }
    public void setEvents(EBuyListEvents events) {
        this.events = events;
    }

    public String getBuyListId() {
        return buyListId;
    }
    public void setBuyListId(String buyListId) {
        this.buyListId = buyListId;
    }

    public enum EBuyListEvents {
        productAdded, productRemoved, productChanged, buyListRemoved, buyListAdded, buyListChanged
    }
}


