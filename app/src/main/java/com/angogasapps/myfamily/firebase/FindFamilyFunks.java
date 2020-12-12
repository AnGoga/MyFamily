package com.angogasapps.myfamily.firebase;


import androidx.annotation.NonNull;

import com.angogasapps.myfamily.firebase.interfaces.IOnFindFamily;
import com.angogasapps.myfamily.firebase.interfaces.IOnJoinToFamily;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_FAMILY;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.CHILD_MEMBERS;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.NODE_FAMILIES;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.NODE_USERS;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.ROLE_MEMBER;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.UID;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.USER;

public class FindFamilyFunks {
    public static synchronized void tryFindFamilyById(String id, IOnFindFamily iOnFindFamily){
        DATABASE_ROOT.child(NODE_FAMILIES).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null){
                    //семья с таким идентификатором не найдена
                    iOnFindFamily.onFailure();
                }else {
                    //такая семья нашлась
                    iOnFindFamily.onSuccess();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iOnFindFamily.onCancelled();
            }
        });
    }
    public static synchronized void joinUserToFamily(String id, IOnJoinToFamily iOnJoinToFamily){
        // устанавливаем в семье данного человека как участника
        DATABASE_ROOT.child(NODE_FAMILIES).child(id).child(CHILD_MEMBERS).child(UID).setValue(ROLE_MEMBER)
                .addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                //устанавлеваем в юзера значение поля family
                DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FAMILY).setValue(id).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        USER.family = id;
                        iOnJoinToFamily.onSuccess();
                    }
                });
            }
        });
    }
}
