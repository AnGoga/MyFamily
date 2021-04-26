package com.angogasapps.myfamily.firebase;

import android.content.Context;
import android.net.Uri;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndRegisterNewFamily;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndSentToStorageEmblem;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndSetUserField;

import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_EMBLEM;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_MEMBERS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_NAME;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_ROLE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DEFAULT_URL;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.FOLDER_FAMILY_EMBLEMS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_FAMILIES;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.ROLE_CREATOR;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.STORAGE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.UID;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class RegisterFamilyFunks {
    public static synchronized void createNewFamily(Context context, String familyName, Uri familyEmblemUri,
                                       IOnEndRegisterNewFamily iOnEndRegisterNewFamily){
        Toasty.info(context.getApplicationContext(), R.string.wait_a_bit).show();
        String familyId = DATABASE_ROOT.child(NODE_FAMILIES).push().getKey();
        //если человек выбрал эмблему семьи
        if (familyEmblemUri != Uri.EMPTY){
            loadEmblemToStorage(familyEmblemUri, familyId, linkToEmblem -> {
                loadFamilyToFirebase(familyName, linkToEmblem, familyId, iOnEndRegisterNewFamily);
            });
        //дефолтная эмблема
        }else{
            loadFamilyToFirebase(familyName, DEFAULT_URL, familyId, iOnEndRegisterNewFamily);
        }
    }

    private static synchronized void loadFamilyToFirebase(String familyName, String linkToEmblem, String familyId,
                                             IOnEndRegisterNewFamily iOnEndRegisterNewFamily){
        DATABASE_ROOT.child(NODE_FAMILIES).child(familyId)
                .updateChildren(getFamilyByItems(familyName, linkToEmblem))
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        //ставлю в юзера значение поля family
                        UserSetterFields.setFamily(USER, familyId, new IOnEndSetUserField() {
                            @Override
                            public void onSuccessEnd() {
                                iOnEndRegisterNewFamily.onEndRegister();
                            }
                            @Override
                            public void onFailureEnd() {}
                        });
                    }
                });
    }
    private static void loadEmblemToStorage(Uri familyEmblemUri, String familyId,
                                     IOnEndSentToStorageEmblem iOnEndSentToStorageEmblem){
        StorageReference path = STORAGE_ROOT.child(FOLDER_FAMILY_EMBLEMS).child(familyId);

        path.putFile(familyEmblemUri).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                path.getDownloadUrl().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()){
                        String emblemLink = task2.getResult().toString();
                        iOnEndSentToStorageEmblem.onEndSent(emblemLink);
                    }
                });
            }
        });
    }

    private static HashMap<String, Object> getFamilyByItems(String name, String emblemUri){

        HashMap<String, Object> creator = new HashMap<>();
        creator.put(CHILD_ROLE, ROLE_CREATOR);
        HashMap<String, Object> member = new HashMap<>();
        member.put(UID, creator);

        HashMap<String, Object> familyMap = new HashMap<>();
        familyMap.put(CHILD_NAME, name);
        familyMap.put(CHILD_EMBLEM, emblemUri);
        familyMap.put(CHILD_MEMBERS, member);

        return familyMap;
    }
}


