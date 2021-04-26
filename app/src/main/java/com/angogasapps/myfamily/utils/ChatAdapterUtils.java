package com.angogasapps.myfamily.utils;


import com.angogasapps.myfamily.models.Message;
import com.angogasapps.myfamily.ui.screens.chat.ChatFragment;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class ChatAdapterUtils {
    public static ArrayList<Message> sortMessagesList(ArrayList<Message> messagesList){
        if (messagesList.size() <= ChatFragment.downloadMessagesCountStep) {
            Collections.sort(messagesList);
            return messagesList;
        }else{

            List<Message> subList = messagesList.subList(0, ChatFragment.downloadMessagesCountStep);

            ArrayList<Message> cashList = new ArrayList<>(subList);

            subList.clear();

            Collections.sort(cashList);
            cashList.addAll(messagesList);
            return cashList;
        }
    }
}

/*
            for (int i = 0; i < ChatFragment.downloadMessagesCountStep; i++) {
                cashList.add(messagesList.get(i));
            }
            for (int i = 0; i < ChatFragment.downloadMessagesCountStep; i++) {
                messagesList.remove(0);
            }
 */
