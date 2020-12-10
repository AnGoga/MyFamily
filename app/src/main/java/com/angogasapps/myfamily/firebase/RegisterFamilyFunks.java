package com.angogasapps.myfamily.firebase;

import android.net.Uri;

import com.angogasapps.myfamily.firebase.interfaces.IOnEndRegisterNewFamily;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_NAME;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.NODE_FAMILIES;

public class RegisterFamilyFunks {
    public static void createNewFamily(String familyName, Uri familyEmblem,
                                       IOnEndRegisterNewFamily iOnEndRegisterNewFamily){

        String familyKey = DATABASE_ROOT.child(NODE_FAMILIES).push().getKey();
        DatabaseReference mFamilyPath = DATABASE_ROOT.child(NODE_FAMILIES).child(familyKey);

        mFamilyPath.updateChildren(getFamilyByItems(familyName))
                .addOnCompleteListener(task -> {;/*iOnEndRegisterNewFamily.onEndRegister();*/});
    }
    private static HashMap<String, Object> getFamilyByItems(String name){
        HashMap<String, Object> familyMap = new HashMap<>();
        familyMap.put(CHILD_NAME, name);

        return familyMap;
    }
}
