package com.angogasapps.myfamily.utils

import android.net.Uri
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.firebase.dynamic_links.DynamicLinksManager.createDynamicLinkWithParams

import java.util.HashMap

object FamilyManager {
    var PARAM_FAMILY_ID = "family"
    val inviteLinkToFamily: Uri
        get() {
            val params = HashMap<String?, String?>()
            params[PARAM_FAMILY_ID] = USER.family
            return createDynamicLinkWithParams(params)
        }
}