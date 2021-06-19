package com.angogasapps.myfamily.firebase

import android.app.Activity
import android.net.Uri
import com.angogasapps.myfamily.firebase.interfaces.IOnEndSetUserField
import com.angogasapps.myfamily.ui.screens.registeractivity.RegisterActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import es.dmoral.toasty.Toasty
import java.lang.Exception
import java.util.*

object RegisterUserFunks {
    @Synchronized
    fun registerNewUser(userName: String?, userBirthdayTimeMillis: Long?, photoUri: Uri?, onSuccessRegister: () -> Unit, onError: (e: Exception?) -> Unit) {
        // создаём словарь по шаблону класса User
        val i: IOnEndSetUserField = object : IOnEndSetUserField {
            override fun onSuccessEnd() {
                val uid = FirebaseVarsAndConsts.AUTH.currentUser!!.uid
                val userAttrMap = HashMap<String, Any?>()
                if (photoUri == null) userAttrMap[FirebaseVarsAndConsts.CHILD_PHOTO_URL] = FirebaseVarsAndConsts.DEFAULT_URL
                userAttrMap[FirebaseVarsAndConsts.CHILD_PHONE] = FirebaseVarsAndConsts.AUTH.currentUser!!.phoneNumber
                userAttrMap[FirebaseVarsAndConsts.CHILD_FAMILY] = ""
                userAttrMap[FirebaseVarsAndConsts.CHILD_NAME] = userName
                userAttrMap[FirebaseVarsAndConsts.CHILD_BIRTHDAY] = userBirthdayTimeMillis
                //userAttrMap.put(CHILD_PHOTO, photoUri);
                FirebaseVarsAndConsts.DATABASE_ROOT.child(FirebaseVarsAndConsts.NODE_USERS).child(uid).updateChildren(userAttrMap)
                        .addOnCompleteListener { task: Task<Void?> ->
                            if (task.isSuccessful) {
                                onSuccessRegister()
                            } else {
                                onError(task.exception)
                            }
                        }
            }

            override fun onFailureEnd() {}
        }
        if (photoUri != null) {
            UserSetterFields.setPhotoURL(FirebaseVarsAndConsts.USER, photoUri, i)
        } else {
            i.onSuccessEnd()
        }
    }

    @JvmStatic
    @Synchronized
    fun loadUserPhotoToStorage(photoUri: Uri?, iOnEndSetUserField: IOnEndSetUserField) {
        //TODO:
        val path = FirebaseVarsAndConsts.STORAGE_ROOT.child(FirebaseVarsAndConsts.FOLDER_USERS_PHOTOS).child(FirebaseVarsAndConsts.AUTH.currentUser!!.uid)
        path.putFile(photoUri!!).addOnCompleteListener { task1: Task<UploadTask.TaskSnapshot?> ->
            if (task1.isSuccessful) {
                path.downloadUrl.addOnCompleteListener { task2: Task<Uri> ->
                    if (task2.isSuccessful) {
                        val photoLink = task2.result.toString()
                        UserSetterFields.setField(FirebaseVarsAndConsts.AUTH.currentUser!!.uid, FirebaseVarsAndConsts.CHILD_PHOTO_URL, photoLink, iOnEndSetUserField)
                    } else {
                        iOnEndSetUserField.onFailureEnd()
                    }
                }
            } else {
                iOnEndSetUserField.onFailureEnd()
            }
        }
    }
}