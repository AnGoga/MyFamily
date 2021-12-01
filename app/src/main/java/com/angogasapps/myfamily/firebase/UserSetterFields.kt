package com.angogasapps.myfamily.firebase

import android.net.Uri
import android.util.Log
import com.angogasapps.myfamily.firebase.RegisterUserFunks.loadUserPhotoToStorage
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.models.User
import com.google.android.gms.tasks.Task
import java.util.*

object UserSetterFields {

    @Synchronized
    fun <T> setField(
        userId: String?, fieldName: String?, value: T,
        iOnEndSetUserField: IOnEndCommunicationWithFirebase
    ) {
        DATABASE_ROOT.child(NODE_USERS).child(userId!!)
            .child(
                fieldName!!
            ).setValue(value)
            .addOnCompleteListener { task: Task<Void> ->
                if (task.isSuccessful) {
                    iOnEndSetUserField.onSuccess()
                } else {
                    Log.e("tag", Objects.requireNonNull(task.result).toString())
                    iOnEndSetUserField.onFailure()
                }
            }
    }

    @Synchronized
    fun setUserPhoto(
        user: User,
        photoUri: Uri,
        iOnEndSetUserField: IOnEndCommunicationWithFirebase
    ) {
        if (photoUri !== Uri.EMPTY) {
            loadUserPhotoToStorage(photoUri, iOnEndSetUserField)
        } else {
            setField(
                user.id,
                CHILD_PHOTO_URL,
                DEFAULT_URL,
                iOnEndSetUserField
            )
        }
    }

    @Synchronized
    fun setPhotoURL(
        user: User,
        photoUri: Uri,
        iOnEndSetUserField: IOnEndCommunicationWithFirebase
    ) {
        setUserPhoto(user, photoUri, iOnEndSetUserField)
    }

    @JvmStatic
    @Synchronized
    fun setFamily(user: User, family: String, iOnEndSetUserField: IOnEndCommunicationWithFirebase) {
        setField(
            user.id,
            CHILD_FAMILY,
            family,
            object : IOnEndCommunicationWithFirebase {
                override fun onSuccess() {
                    user.family = family
                    iOnEndSetUserField.onSuccess()
                }

                override fun onFailure() {
                    iOnEndSetUserField.onFailure()
                }
            })
    }
}