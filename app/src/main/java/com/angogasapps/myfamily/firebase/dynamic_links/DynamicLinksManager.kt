package com.angogasapps.myfamily.firebase.dynamic_links

import android.net.Uri
import android.util.Log
import com.angogasapps.myfamily.firebase.*
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters
import com.angogasapps.myfamily.firebase.dynamic_links.DynamicLinksManager
import java.util.HashMap

object DynamicLinksManager {
    private fun createDynamicLink(): Uri {
        val builder = Uri.Builder()
        val dynamicLink = DYNAMIC_LINK_ROOT.createDynamicLink()
            .setLink(Uri.parse(FIREBASE_URL))
            .setDomainUriPrefix(FIREBASE_DYNAMIC_LINKS_HOST)
            .setAndroidParameters(AndroidParameters.Builder().build()).buildDynamicLink()
        return dynamicLink.uri
    }

    @JvmStatic
    fun createDynamicLinkWithParams(paramsMap: HashMap<String?, String?>?): Uri {
        val cacheLink = createDynamicLink()
        val link: Uri
        val uriBuilder = Uri.parse(cacheLink.toString()).buildUpon()
        if (paramsMap != null) for ((key, value) in paramsMap) {
            uriBuilder.appendQueryParameter(key, value)
        }
        link = uriBuilder.build()
        Log.i("tag", "Динамическая ссылка: $link")
        return link
    }
}