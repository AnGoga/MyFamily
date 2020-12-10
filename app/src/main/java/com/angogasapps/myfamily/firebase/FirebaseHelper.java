package com.angogasapps.myfamily.firebase;


import com.angogasapps.myfamily.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseHelper{
    public static FirebaseAuth AUTH;
    public static DatabaseReference DATABASE_ROOT;
    public static User USER;
    public static String UID;

    //firebase realtime database nodes
    public static final String NODE_USERS = "users";
    public static final String NODE_FAMILIES = "families";

    //firebase realtime database childes
    public static final String CHILD_ID = "id";
    public static final String CHILD_PHONE = "phone";
    public static final String CHILD_FAMILY = "family";
    public static final String CHILD_NAME = "name";
    public static final String CHILD_BIRTHDAY = "birthday";

    public static final String CHILD_MESSANGES = "messanges";
    public static final String CHILD_MEMBERS = "members";
    public static final String CHILD_EMBLEM = "emblem";
    //инициализация Firebase и связаных с ней компонентов
    public static void initFirebase(){
        AUTH = FirebaseAuth.getInstance();
        DATABASE_ROOT = FirebaseDatabase.getInstance().getReference();
        try {
            UID = AUTH.getCurrentUser().getUid();
        }catch (Exception e){}

        USER = new User();
    }
}
