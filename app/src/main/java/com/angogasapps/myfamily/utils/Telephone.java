package com.angogasapps.myfamily.utils;


public class Telephone {
    public static boolean isCorrect(String phoneNumber){
        if (phoneNumber.length() < 11)
            return false;
        return false;
    }
    public static String formatPhoneNumber(String telNumber){
        telNumber = telNumber.replace("+7", "8");

        return telNumber;
    }
}