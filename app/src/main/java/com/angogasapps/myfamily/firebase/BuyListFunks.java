package com.angogasapps.myfamily.firebase;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;
import com.angogasapps.myfamily.models.BuyList;
import com.angogasapps.myfamily.utils.BuyListUtils;
import com.google.firebase.database.DatabaseReference;

import es.dmoral.toasty.Toasty;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_PRODUCTS;
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
               task.getException().printStackTrace();
                i.onFailure();
           }
        });
    }

    public static synchronized void addNewProductToBuyList(String buyListId, BuyList.Product product, IOnEndCommunicationWithFirebase i){

        if(!AppApplication.isOnline()){
            Toasty.warning(
                    AppApplication.getInstance().getApplicationContext(),
                    R.string.connection_is_not
            ).show();
        }

        DatabaseReference ref = DATABASE_ROOT.child(NODE_BUY_LIST).child(USER.getFamily()).child(buyListId).child(CHILD_PRODUCTS);
        String key = ref.push().getKey();
        product.setFrom(USER.getPhone());
        ref.child(key).updateChildren(BuyListUtils.getHashMap(product)).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               i.onSuccess();
           }else{
               task.getException().printStackTrace();
               i.onFailure();
           }
        });
    }
}
