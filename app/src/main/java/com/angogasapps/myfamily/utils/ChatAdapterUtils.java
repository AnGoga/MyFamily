package com.angogasapps.myfamily.utils;


import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.ui.fragments.ChatFragment;

import java.util.ArrayList;

import java.util.Collections;


public class ChatAdapterUtils {
    public static ArrayList<Message> sortMessagesList(ArrayList<Message> messagesList){
        if (messagesList.size() <= ChatFragment.downloadMessagesCountStep) {
            return sortArrayHelper(messagesList);
        }else{
            ArrayList<Message> cashList = new ArrayList<>();
            for (int i = 0; i < ChatFragment.downloadMessagesCountStep; i++) {
                cashList.add(messagesList.get(i));
            }
            for (int i = 0; i < ChatFragment.downloadMessagesCountStep; i++) {
                messagesList.remove(0);
            }
            cashList = sortArrayHelper(cashList);
            cashList.addAll(messagesList);
            return cashList;
        }
    }
    private static ArrayList<Message> sortArrayHelper(ArrayList<Message> messagesList){
        Collections.sort(messagesList);
        return messagesList;
    }
}
