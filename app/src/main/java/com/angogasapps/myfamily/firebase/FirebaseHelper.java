package com.angogasapps.myfamily.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseHelper{
    public static FirebaseAuth AUTH;
    public static DatabaseReference DATABASE_ROOT;

    //firebase realtime database nodes
    public static final String NODE_USERS = "users";

    //firebase realtime database childes
    public static final String CHILD_ID = "id";
    public static final String CHILD_PHONE = "phone";
    public static final String CHILD_FAMILY = "family";


    //инициализация Firebase
    public static void initFirebase(){
        AUTH = FirebaseAuth.getInstance();
        DATABASE_ROOT = FirebaseDatabase.getInstance().getReference();
    }
}
