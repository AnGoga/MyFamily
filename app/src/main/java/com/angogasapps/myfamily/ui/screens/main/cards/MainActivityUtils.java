package com.angogasapps.myfamily.ui.screens.main.cards;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.async.LoadFamilyThread;
import com.angogasapps.myfamily.models.ActionCardState;
import com.angogasapps.myfamily.ui.screens.main.cards.ActionCardUtils;
import com.angogasapps.myfamily.utils.Async;


import java.util.ArrayList;

public class MainActivityUtils {
    private static ArrayList<ActionCardState> getDefaultCardsArray(Context context){
        ArrayList<ActionCardState> list = new ArrayList<>();
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

        StringBuilder str = new StringBuilder();
        for (ActionCardState state : list){
            str.append(state.getActionCardsName()).append(" ");
        }
        editor.putString("card_place", str.toString());
        editor.apply();
    }

    public static ArrayList<ActionCardState> getPreferCardsArray(Context context){
        SharedPreferences sf = context.getSharedPreferences("data", Context.MODE_PRIVATE);

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
