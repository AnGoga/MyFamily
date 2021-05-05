package com.angogasapps.myfamily.models;

public class MainCardState {
    private int mCardDrawable;
    private String mCardName;
    private String mCardSubscript;
    private Class<?> activityClass;


    public MainCardState(String name, String subscript, int drawable, Class<?> mClass){
        this.mCardDrawable = drawable;
        this.mCardName = name;
        this.mCardSubscript = subscript;
        this.activityClass = mClass;
    }


    public int getCardDrawable() {
        return mCardDrawable;
    }

    public void setCardDrawable(int cardDrawable) {
        this.mCardDrawable = cardDrawable;
    }

    public String getCardName() {
        return mCardName;
    }

    public void setCardName(String cardName) {
        this.mCardName = cardName;
    }

    public String getCardSubscript() {
        return mCardSubscript;
    }

    public void setCardSubscript(String cardSubscript) {
        this.mCardSubscript = cardSubscript;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class<?> activityClass) {
        this.activityClass = activityClass;
    }
}
