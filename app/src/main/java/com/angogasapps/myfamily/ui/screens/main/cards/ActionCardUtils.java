package com.angogasapps.myfamily.ui.screens.main.cards;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.models.ActionCardState;
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListActivity;
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity;
import com.angogasapps.myfamily.ui.screens.family_settings.FamilySettingsActivity;
import com.angogasapps.myfamily.ui.screens.family_storage.SelectStorageActivity;
import com.angogasapps.myfamily.ui.screens.news_center.NewsCenterActivity;

public class ActionCardUtils {
    public enum EActionCards{
        EVENT_CALENDAR, MY_DOINGS, CHAT, BUY_LIST, CLOCK, MY_FAMILY, STORAGE, NEWS_CENTER
    }

    public static ActionCardState getState(EActionCards card){
        if (card.equals(EActionCards.EVENT_CALENDAR)){
            return new ActionCardState(
                    AppApplication.stringOf(R.string.event_calendar),
                    AppApplication.stringOf(R.string.event_calendar),
                    R.drawable.mainActivityIcoCalendar, null, card
            );
        }
        if (card.equals(EActionCards.MY_DOINGS)){
            return new ActionCardState(
                    AppApplication.stringOf(R.string.time_table),
                    AppApplication.stringOf(R.string.time_table),
                    R.drawable.mainActivityIcoTimeTable, null, card
            );
        }
        if (card.equals(EActionCards.CHAT)){
            return new ActionCardState(
                    AppApplication.stringOf(R.string.chat),
                    AppApplication.stringOf(R.string.chat),
                    R.drawable.mainActivityIcoChat, ChatActivity.class, card
            );
        }
        if (card.equals(EActionCards.BUY_LIST)){
            return new ActionCardState(
                    AppApplication.stringOf(R.string.buy_list),
                    AppApplication.stringOf(R.string.buy_list),
                    R.drawable.mainActivityIcoBuyList, BuyListActivity.class, card
            );
        }
        if (card.equals(EActionCards.CLOCK)){
            return new ActionCardState(
                    AppApplication.stringOf(R.string.clock),
                    AppApplication.stringOf(R.string.clock),
                    R.drawable.ic_family_clock, null, card
            );
        }
        if (card.equals(EActionCards.MY_FAMILY)){
            return new ActionCardState(
                    AppApplication.stringOf(R.string.my_family),
                    AppApplication.stringOf(R.string.my_family),
                    R.drawable.ic_my_family, FamilySettingsActivity.class, card
            );
        }
        if (card.equals(EActionCards.STORAGE)){
            return new ActionCardState(
                    AppApplication.stringOf(R.string.storage),
                    AppApplication.stringOf(R.string.storage),
                    R.drawable.ic_family_storage, SelectStorageActivity.class, card
            );
        }
        if (card.equals(EActionCards.NEWS_CENTER)){
            return new ActionCardState(
                    AppApplication.stringOf(R.string.news_center),
                    AppApplication.stringOf(R.string.news_center),
                    R.drawable.ic_news_cenetr, NewsCenterActivity.class, card
            );
        }
        return null;

    }
}
