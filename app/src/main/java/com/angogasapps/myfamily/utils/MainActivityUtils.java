package com.angogasapps.myfamily.utils;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.async.LoadFamilyThread;
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
        list.add(new MainCardState(context.getString(R.string.clock), context.getString(R.string.clock), R.drawable.ic_family_clock, null));
        list.add(new MainCardState(context.getString(R.string.my_family), context.getString(R.string.my_family), R.drawable.ic_my_family, FamilySettingsActivity.class));
        list.add(new MainCardState(context.getString(R.string.storage), context.getString(R.string.storage), R.drawable.ic_family_storage, null));
        list.add(new MainCardState(context.getString(R.string.news_center), context.getString(R.string.news_center), R.drawable.ic_news_cenetr , NewsCenterActivity.class));

        return list;
    }

    public static void waitEndDownloadThread(Activity activity, RecyclerView.Adapter<?> adapter){
        Async.runInNewThread(() -> {
            while (!LoadFamilyThread.isEnd){}
            activity.runOnUiThread(adapter::notifyDataSetChanged);
        });
    }
}
