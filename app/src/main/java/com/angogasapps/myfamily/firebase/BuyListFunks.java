package com.angogasapps.myfamily.firebase;

import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;
import com.angogasapps.myfamily.objects.BuyList;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_NAME;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_BUY_LIST;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class BuyListFunks {

    public static synchronized void addNewBuyList(BuyList buyList, IOnEndCommunicationWithFirebase i){
        DatabaseReference ref = DATABASE_ROOT.child(NODE_BUY_LIST).child(USER.getFamily());
        String key = ref.push().getKey();

        ref.child(key).setValue(buyList).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
                i.onSuccess();
           }else{
                i.onFailure();
           }
        });
    }
}
