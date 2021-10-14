package com.angogasapps.myfamily.async.notification

import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.angogasapps.myfamily.models.User
import com.google.android.gms.tasks.Task
import java.lang.Exception

object TokensManager {
    fun updateToken(token: String?, user: User) {
        FirebaseVarsAndConsts.DATABASE_ROOT.child(FirebaseVarsAndConsts.NODE_USERS).child(user.id)
            .child(FirebaseVarsAndConsts.CHILD_TOKEN).setValue(token)
            .addOnCompleteListener { task: Task<Void?>? -> }
    }

    fun updateToken(token: String?) {
        try {
            FirebaseDatabase.getInstance().reference
                .child(FirebaseVarsAndConsts.NODE_USERS)
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child(FirebaseVarsAndConsts.CHILD_TOKEN).setValue(token)
                .addOnCompleteListener { task: Task<Void?>? -> }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateToken(user: User) {
        FirebaseAuth.getInstance().currentUser!!.getIdToken(true)
            .addOnCompleteListener { task: Task<GetTokenResult> ->
                if (task.isSuccessful) {
                    val idToken = task.result.token
                    updateToken(idToken, user)
                } else {
                    task.exception!!.printStackTrace()
                }
            }
    }
}