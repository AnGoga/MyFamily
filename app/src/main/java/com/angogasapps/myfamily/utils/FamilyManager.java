package com.angogasapps.myfamily.utils;

import android.net.Uri;

import com.angogasapps.myfamily.firebase.dynamic_links.DynamicLinksManager;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

import java.util.HashMap;

public class FamilyManager {
    public static String PARAM_FAMILY_ID = "family";

    public static Uri getInviteLinkToFamily(){
        
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_FAMILY_ID, USER.getFamily());

        return DynamicLinksManager.createDynamicLinkWithParams(params);
    }
}
