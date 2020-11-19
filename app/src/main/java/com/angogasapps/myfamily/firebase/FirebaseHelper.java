package com.angogasapps.myfamily.firebase;


import com.angogasapps.myfamily.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.angogasapps.myfamily.firebase.AuthFunctions.downloadUser;


public class FirebaseHelper{
    public static FirebaseAuth AUTH;
    public static DatabaseReference DATABASE_ROOT;
    public static User USER;

    //firebase realtime database nodes
    public static final String NODE_USERS = "users";
    public static String UID;

    //firebase realtime database childes
    public static final String CHILD_ID = "id";
    public static final String CHILD_PHONE = "phone";
    public static final String CHILD_FAMILY = "family";
    public static final String CHILD_PHOTO_URL = "photoURL";


    //инициализация Firebase и связаных с ней компонентов
    public static void initFirebase(){
        AUTH = FirebaseAuth.getInstance();
        DATABASE_ROOT = FirebaseDatabase.getInstance().getReference();
        UID = AUTH.getCurrentUser().getUid();
        downloadUser();
    }
}
