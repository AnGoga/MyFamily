package com.angogasapps.myfamily.models;


import androidx.annotation.StringRes;

import com.angogasapps.myfamily.utils.ActionCardUtils;

public class ActionCardState {

    private int mCardDrawable;
    private String mCardName;
    private String mCardSubscript;
    private Class<?> activityClass;
    private String  actionCardsName;

    public ActionCardState(){}

    public ActionCardState(String name, String subscript, int drawable, Class<?> mClass, ActionCardUtils.EActionCards cards){
        this.mCardDrawable = drawable;
        this.mCardName = name;
        this.mCardSubscript = subscript;
        this.activityClass = mClass;
        this.actionCardsName = cards.name();
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

    public String getActionCardsName() {
        return actionCardsName;
    }

    public void setActionCardsName(String actionCardsName) {
        this.actionCardsName = actionCardsName;
    }

    @Override
    public String toString() {
        return "ActionCardState{" +
                "mCardDrawable=" + mCardDrawable +
                ", mCardName='" + mCardName + '\'' +
                ", mCardSubscript='" + mCardSubscript + '\'' +
                ", activityClass=" + activityClass +
                '}';
    }
}
