package com.angogasapps.myfamily.firebase


import com.angogasapps.myfamily.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import java.lang.Exception

object FirebaseHelper {
    fun initFirebase() {
        AUTH = FirebaseAuth.getInstance()
        DATABASE_ROOT = FirebaseDatabase.getInstance().reference
        STORAGE_ROOT = FirebaseStorage.getInstance().reference
        DYNAMIC_LINK_ROOT = FirebaseDynamicLinks.getInstance()
        try {
            UID = AUTH.currentUser!!.uid
        } catch (e: Exception) {}
        USER = User()
    }

    val messageKey: String
        get() = DATABASE_ROOT.child(NODE_CHAT)
            .child(USER.family).push().key!!
}