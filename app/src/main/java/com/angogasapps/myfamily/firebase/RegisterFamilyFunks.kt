package com.angogasapps.myfamily.firebase

import android.content.Context
import android.net.Uri
import com.angogasapps.myfamily.firebase.UserSetterFields.setFamily
import com.angogasapps.myfamily.firebase.interfaces.IOnEndRegisterNewFamily
import es.dmoral.toasty.Toasty
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.NODE_FAMILIES
import com.angogasapps.myfamily.firebase.ROLE_CREATOR
import com.angogasapps.myfamily.firebase.STORAGE_ROOT
import com.angogasapps.myfamily.firebase.UID
import com.angogasapps.myfamily.firebase.USER
import com.angogasapps.myfamily.firebase.RegisterFamilyFunks
import com.angogasapps.myfamily.firebase.interfaces.IOnEndSentToStorageEmblem
import com.google.android.gms.tasks.OnCompleteListener
import com.angogasapps.myfamily.firebase.UserSetterFields
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.HashMap

object RegisterFamilyFunks {
    @Synchronized
    fun createNewFamily(
        context: Context, familyName: String, familyEmblemUri: Uri,
        iOnEndRegisterNewFamily: IOnEndRegisterNewFamily
    ) {
        Toasty.info(context.applicationContext, R.string.wait_a_bit).show()
        val familyId = DATABASE_ROOT.child(NODE_FAMILIES).push().getKey()!!
        //если человек выбрал эмблему семьи
        if (familyEmblemUri !== Uri.EMPTY) {
            loadEmblemToStorage(
                familyEmblemUri,
                familyId,
                object : IOnEndSentToStorageEmblem {
                    override fun onEndSent(linkToEmblem: String) {
                        loadFamilyToFirebase(
                            familyName,
                            linkToEmblem,
                            familyId,
                            iOnEndRegisterNewFamily
                        )
                    }
                })
            //дефолтная эмблема
        } else {
            loadFamilyToFirebase(familyName, DEFAULT_URL, familyId, iOnEndRegisterNewFamily)
        }
    }

    @Synchronized
    private fun loadFamilyToFirebase(
        familyName: String, linkToEmblem: String, familyId: String,
        iOnEndRegisterNewFamily: IOnEndRegisterNewFamily
    ) {
        DATABASE_ROOT.child(NODE_FAMILIES).child(familyId)
            .updateChildren(getFamilyByItems(familyName, linkToEmblem))
            .addOnCompleteListener(OnCompleteListener { task1: Task<Void?> ->
                if (task1.isSuccessful) {
                    //ставлю в юзера значение поля family
                    setFamily(USER, familyId, object : IOnEndCommunicationWithFirebase {
                        override fun onSuccess() {
                            iOnEndRegisterNewFamily.onEndRegister()
                        }

                        override fun onFailure() {}
                    })
                }
            })
    }

    private fun loadEmblemToStorage(
        familyEmblemUri: Uri, familyId: String,
        iOnEndSentToStorageEmblem: IOnEndSentToStorageEmblem
    ) {
        val path: StorageReference = STORAGE_ROOT.child(FOLDER_FAMILY_EMBLEMS).child(familyId)
        path.putFile(familyEmblemUri)
            .addOnCompleteListener { task1: Task<UploadTask.TaskSnapshot?> ->
                if (task1.isSuccessful) {
                    path.downloadUrl.addOnCompleteListener { task2: Task<Uri> ->
                        if (task2.isSuccessful) {
                            val emblemLink = task2.result.toString()
                            iOnEndSentToStorageEmblem.onEndSent(emblemLink)
                        }
                    }
                }
            }
    }

    private fun getFamilyByItems(name: String, emblemUri: String): HashMap<String, Any> {

//        HashMap<String, Object> creator = new HashMap<>();
//        creator.put(CHILD_ROLE, ROLE_CREATOR);
        val member = HashMap<String, Any>()
        member[UID] = ROLE_CREATOR
        val familyMap = HashMap<String, Any>()
        familyMap[CHILD_NAME] = name
        familyMap[CHILD_EMBLEM] = emblemUri
        familyMap[CHILD_MEMBERS] = member
        return familyMap
    }
}