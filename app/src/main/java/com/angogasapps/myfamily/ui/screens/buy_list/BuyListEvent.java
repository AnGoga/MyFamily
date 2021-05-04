package com.angogasapps.myfamily.ui.screens.buy_list;

import com.angogasapps.myfamily.models.BuyList;

public abstract class BuyListEvent {
    int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
class BuyListEventRemove extends BuyListEvent{}
class BuyListEventAdd extends BuyListEvent{}
class BuyListEventChange extends BuyListEvent{}


