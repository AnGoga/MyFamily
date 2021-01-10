package com.angogasapps.myfamily.firebase.dynamic_links;

import android.net.Uri;

import android.util.Log;


import com.google.firebase.dynamiclinks.DynamicLink;


import java.util.HashMap;
import java.util.Map;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*;

public class DynamicLinksManager {

    public static Uri createDynamicLink() {

        Uri.Builder builder = new Uri.Builder();

        DynamicLink dynamicLink = DYNAMIC_LINK_ROOT.createDynamicLink()
                .setLink(Uri.parse(FIREBASE_URL))
                .setDomainUriPrefix(FIREBASE_DYNAMIC_LINKS_HOST)
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build()).buildDynamicLink();

        return dynamicLink.getUri();
    }
    public static Uri createDynamicLinkWithParams(HashMap<String, String> paramsMap){

        Uri cacheLink = createDynamicLink();
        Uri link;

        Uri.Builder uriBuilder = Uri.parse(cacheLink.toString()).buildUpon(); //.appendQueryParameter("p", "267196").build();

        if (paramsMap != null)
        for(Map.Entry<String, String> entry : paramsMap.entrySet()){
            uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        link = uriBuilder.build();

        Log.i("tag", "Динамическая ссылка: " + link.toString());

        return link;
    }
}
