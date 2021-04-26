package com.angogasapps.myfamily.utils;

import com.angogasapps.myfamily.models.Message;

import java.util.ArrayList;

public class WithMessages {

    public static boolean arrayContainsMessage(ArrayList<Message> array, Message message){
        boolean isContains = false;
        for (Message element : array){
            if (element.equals(message))
                isContains = true;
        }
        return isContains;
    }
}
