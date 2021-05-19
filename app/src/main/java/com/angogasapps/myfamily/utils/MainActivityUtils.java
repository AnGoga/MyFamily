package com.angogasapps.myfamily.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.async.LoadFamilyThread;
import com.angogasapps.myfamily.models.ActionCardState;


import java.util.ArrayList;

public class MainActivityUtils {
    private static ArrayList<ActionCardState> getDefaultCardsArray(Context context){
        ArrayList<ActionCardState> list = new ArrayList<>();

//        list.add(new ActionCardState(context.getString(R.string.event_calendar), context.getString(R.string.event_calendar), R.drawable.mainActivityIcoCalendar, null));
//        list.add(new ActionCardState(context.getString(R.string.time_table), context.getString(R.string.time_table), R.drawable.mainActivityIcoTimeTable, null));
//        list.add(new ActionCardState(context.getString(R.string.chat), context.getString(R.string.chat), R.drawable.mainActivityIcoChat, ChatActivity.class));
//        list.add(new ActionCardState(context.getString(R.string.buy_list), context.getString(R.string.buy_list), R.drawable.mainActivityIcoBuyList, BuyListActivity.class));
//        list.add(new ActionCardState(context.getString(R.string.clock), context.getString(R.string.clock), R.drawable.ic_family_clock, null));
//        list.add(new ActionCardState(context.getString(R.string.my_family), context.getString(R.string.my_family), R.drawable.ic_my_family, FamilySettingsActivity.class));
//        list.add(new ActionCardState(context.getString(R.string.storage), context.getString(R.string.storage), R.drawable.ic_family_storage, null));
//        list.add(new ActionCardState(context.getString(R.string.news_center), context.getString(R.string.news_center), R.drawable.ic_news_cenetr , NewsCenterActivity.class));

        for (ActionCardUtils.EActionCards card : ActionCardUtils.EActionCards.values()) {
            list.add(ActionCardUtils.getState(card));
        }
        return list;
    }

    public static void waitEndDownloadThread(Activity activity, RecyclerView.Adapter<?> adapter){
        Async.runInNewThread(() -> {
            while (!LoadFamilyThread.isEnd){}
            activity.runOnUiThread(adapter::notifyDataSetChanged);
        });
    }

    public static void savePreferCardPlaces(Context context, ArrayList<ActionCardState> list){
        SharedPreferences sf = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        Editor editor = sf.edit();
////        Type type = new TypeToken<List<ActionCardState>>(){}.getType();
//        Gson gson = new GsonBuilder().registerTypeAdapter(ActionCardState.class, new MainCardJsonAdapter()).create();
//        String json = gson.toJson(list);
//        editor.putString("card_place", json);
//        Log.d("TAG", json);
        StringBuilder str = new StringBuilder();
        for (ActionCardState state : list){
            str.append(state.getActionCardsName()).append(" ");
        }
        editor.putString("card_place", str.toString());
        editor.apply();
    }

    public static ArrayList<ActionCardState> getPreferCardsArray(Context context){
        SharedPreferences sf = context.getSharedPreferences("data", Context.MODE_PRIVATE);
//        String json = sf.getString("card_place", "");
//        Log.d("TAG", json);
//        if (!json.equals("")){
//            Gson gson = new GsonBuilder().registerTypeAdapter(ActionCardState.class, new MainCardJsonAdapter()).create();
//            return gson.fromJson(json, new TypeToken<List<ActionCardState>>(){}.getType());
//        }else{
//            return getDefaultCardsArray(context);
//        }


        String str = sf.getString("card_place", "");
        if (str.equals("")){
            return getDefaultCardsArray(context);
        }
        ArrayList<ActionCardState> list = new ArrayList<>();
        for (String card : str.split(" ")){
            list.add(ActionCardUtils.getState(ActionCardUtils.EActionCards.valueOf(card)));
        }
        return list;
    }


}
