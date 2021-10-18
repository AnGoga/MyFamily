package com.angogasapps.myfamily.firebase

import android.net.Uri
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import java.lang.Exception
import java.util.*

object RegisterUserFunks {
    @Synchronized
    fun registerNewUser(userName: String?, userBirthdayTimeMillis: Long?, photoUri: Uri?, onSuccessRegister: () -> Unit, onError: (e: Exception?) -> Unit) {
        // создаём словарь по шаблону класса User
        val i = object : IOnEndCommunicationWithFirebase {
            override fun onSuccess() {
                val uid = AUTH.currentUser!!.uid
                val userAttrMap = HashMap<String, Any?>()
                if (photoUri == null) userAttrMap[CHILD_PHOTO_URL] = DEFAULT_URL
                userAttrMap[CHILD_PHONE] = AUTH.currentUser!!.phoneNumber
                userAttrMap[CHILD_FAMILY] = ""
                userAttrMap[CHILD_NAME] = userName
                userAttrMap[CHILD_BIRTHDAY] = userBirthdayTimeMillis
                //userAttrMap.put(CHILD_PHOTO, photoUri);
                DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(userAttrMap)
                        .addOnCompleteListener { task: Task<Void?> ->
                            if (task.isSuccessful) {
                                onSuccessRegister()
                            } else {
                                onError(task.exception)
                            }
                        }
            }

            override fun onFailure() {}
        }
        if (photoUri != null) {
            UserSetterFields.setPhotoURL(USER, photoUri, i)
        } else {
            i.onSuccess()
        }
    }

    @JvmStatic
    @Synchronized
    fun loadUserPhotoToStorage(photoUri: Uri?, iOnEndSetUserField: IOnEndCommunicationWithFirebase) {
        //TODO:
        val path = STORAGE_ROOT.child(FOLDER_USERS_PHOTOS).child(AUTH.currentUser!!.uid)
        path.putFile(photoUri!!).addOnCompleteListener { task1: Task<UploadTask.TaskSnapshot?> ->
            if (task1.isSuccessful) {
                path.downloadUrl.addOnCompleteListener { task2: Task<Uri> ->
                    if (task2.isSuccessful) {
                        val photoLink = task2.result.toString()
                        UserSetterFields.setField(AUTH.currentUser!!.uid, CHILD_PHOTO_URL, photoLink, iOnEndSetUserField)
                    } else {
                        iOnEndSetUserField.onFailure()
                    }
                }
            } else {
                iOnEndSetUserField.onFailure()
            }
        }
    }
}