package com.angogasapps.myfamily.utils;

import com.angogasapps.myfamily.models.events.NewsObject;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_FROM_PHONE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_TIME_CREATE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_TYPE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_VALUE;

public class NewsUtils {
    public static HashMap<String, Object> getMap(NewsObject object){
        HashMap<String, Object> map = new HashMap<>();

        map.put(CHILD_TYPE, object.getType());
        map.put(CHILD_FROM_PHONE, object.getFromPhone());
        map.put(CHILD_VALUE, object.getValue());
        map.put(CHILD_TIME_CREATE, ServerValue.TIMESTAMP);

        return map;
    }

    public static int getIndexOfDeleteNews(ArrayList<NewsObject> newsList, NewsObject newNews){
        for (int i = 0; i < newsList.size(); i++) {
            if (newsList.get(i).getId().equals(newNews.getId())){
                return i;
            }
        }
        return -1;
    }
}
