package com.angogasapps.myfamily.firebase;

import com.angogasapps.myfamily.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*;

public class FirebaseHelper {

    public static void initFirebase(){
        AUTH = FirebaseAuth.getInstance();
        DATABASE_ROOT = FirebaseDatabase.getInstance().getReference();
        STORAGE_ROOT = FirebaseStorage.getInstance().getReference();
        try {
            UID = AUTH.getCurrentUser().getUid();
        }catch (Exception e){}

        USER = new User();
    }

    public static String getMessageKey(){
        return DATABASE_ROOT.child(NODE_CHAT).child(USER.getFamily()).push().getKey();
    }

}
