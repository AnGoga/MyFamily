package com.angogasapps.myfamily.network.interfaces.families

import android.content.Context
import android.net.Uri

interface CreatorFamilyService {
    fun createNewFamily(
        context: Context, familyName: String, familyEmblemUri: Uri,
        onSuccess: () -> Unit = {}, onError: () -> Unit = {}
    )
}