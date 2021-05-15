package com.angogasapps.myfamily.utils;

import android.util.Log;

import com.angogasapps.myfamily.models.MainCardState;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class MainCardJsonAdapter implements JsonSerializer<MainCardState>, JsonDeserializer<MainCardState> {

    @Override
    public MainCardState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            MainCardState state = new MainCardState();
            state.setCardDrawable(json.getAsJsonObject().get("mCardDrawable").getAsInt());
            state.setCardName(json.getAsJsonObject().get("mCardName").getAsString());
            state.setCardSubscript(json.getAsJsonObject().get("mCardSubscript").getAsString());

            String clazz = json.getAsJsonObject().get("activityClass").getAsString();
            state.setActivityClass(clazz.equals(" ") ? null : Class.forName(clazz));
            return state;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JsonElement serialize(MainCardState src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mCardDrawable", src.getCardDrawable());
        jsonObject.addProperty("mCardName", src.getCardName());
        jsonObject.addProperty("mCardSubscript", src.getCardSubscript());
        jsonObject.addProperty("activityClass", src.getActivityClass() == null ? " " : src.getActivityClass().getName());
        return jsonObject;
    }
}
