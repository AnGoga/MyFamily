package com.angogasapps.myfamily.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.async.LoadFamilyThread;
import com.angogasapps.myfamily.models.MainCardState;
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListActivity;
import com.angogasapps.myfamily.ui.screens.chat.ChatActivity;
import com.angogasapps.myfamily.ui.screens.family_settings.FamilySettingsActivity;
import com.angogasapps.myfamily.ui.screens.news_center.CreateNewNewsActivity;
import com.angogasapps.myfamily.ui.screens.news_center.NewsCenterActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.angogasapps.myfamily.ui.screens.chat.ChatActivity.TAG;

public class MainActivityUtils {
    private static ArrayList<MainCardState> getDefaultCardsArray(Context context){
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

    public static void savePreferCardPlaces(Context context, ArrayList<MainCardState> list){
        SharedPreferences sf = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        Editor editor = sf.edit();
//        Type type = new TypeToken<List<MainCardState>>(){}.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(MainCardState.class, new MainCardJsonAdapter()).create();
        String json = gson.toJson(list);
        editor.putString("card_place", json);
        Log.d("TAG", json);
        editor.apply();
    }

    public static ArrayList<MainCardState> getPreferCardsArray(Context context){
        SharedPreferences sf = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String json = sf.getString("card_place", "");
        Log.d("TAG", json);
        if (!json.equals("")){
            Gson gson = new GsonBuilder().registerTypeAdapter(MainCardState.class, new MainCardJsonAdapter()).create();
            return gson.fromJson(json, new TypeToken<List<MainCardState>>(){}.getType());
        }else{
            return getDefaultCardsArray(context);
        }
    }
}
