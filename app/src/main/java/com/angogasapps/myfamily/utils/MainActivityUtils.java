package com.angogasapps.myfamily.utils;

import android.content.Context;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.models.MainCardState;
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListActivity;
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity;
import com.angogasapps.myfamily.ui.screens.family_settings.FamilySettingsActivity;
import com.angogasapps.myfamily.ui.screens.news_center.CreateNewNewsActivity;
import com.angogasapps.myfamily.ui.screens.news_center.NewsCenterActivity;

import java.util.ArrayList;

public class MainActivityUtils {
    public static ArrayList<MainCardState> getArrayToView(Context context){
        ArrayList<MainCardState> list = new ArrayList<>();

        list.add(new MainCardState(context.getString(R.string.event_calendar), context.getString(R.string.event_calendar), R.drawable.mainActivityIcoCalendar, null));
        list.add(new MainCardState(context.getString(R.string.time_table), context.getString(R.string.time_table), R.drawable.mainActivityIcoTimeTable, null));
        list.add(new MainCardState(context.getString(R.string.chat), context.getString(R.string.chat), R.drawable.mainActivityIcoChat, ChatActivity.class));
        list.add(new MainCardState(context.getString(R.string.buy_list), context.getString(R.string.buy_list), R.drawable.mainActivityIcoBuyList, BuyListActivity.class));
        list.add(new MainCardState(context.getString(R.string.clock), context.getString(R.string.clock), 0, null));
        list.add(new MainCardState(context.getString(R.string.my_family), context.getString(R.string.my_family), R.drawable.ic_my_family, FamilySettingsActivity.class));
        list.add(new MainCardState(context.getString(R.string.storage), context.getString(R.string.storage), 0, null));
        list.add(new MainCardState(context.getString(R.string.news_center), context.getString(R.string.news_center), 0 , NewsCenterActivity.class));

        return list;
    }
}
