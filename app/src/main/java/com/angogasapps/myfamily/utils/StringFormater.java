package com.angogasapps.myfamily.utils;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*;

public class StringFormater {
    public static String formatStringToSend(String text){
        StringBuilder str = new StringBuilder(text);

        while (String.valueOf(str.charAt(0)).equals(" ") || String.valueOf(str.charAt(0)).equals("\n")){
            str.deleteCharAt(0);
        }
        while (String.valueOf(str.charAt(str.length() - 1)).equals(" ") || String.valueOf(str.charAt(str.length() - 1)).equals("\n")){
            str.deleteCharAt(str.length() - 1);
        }

        return str.toString();
    }

    public static String formatLongToTime(Long time){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(new Date(time));
    }

    public static String formatToRole(String role){
        if (role == null)
            return "";
        if (role.equals(ROLE_MEMBER))
            return "Участник";
        if (role.equals(ROLE_CREATOR))
            return "Создатель";


        return role;
    }


}
