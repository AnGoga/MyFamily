package com.angogasapps.myfamily.network.firebaseImpl.families

import android.content.Context
import android.net.Uri
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.network.interfaces.families.CreatorFamilyService
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import es.dmoral.toasty.Toasty
import java.util.HashMap
import javax.inject.Inject

class FirebaseCreatorFamilyServiceImpl @Inject constructor() : CreatorFamilyService {

    override fun createNewFamily(
        context: Context,
        familyName: String,
        familyEmblemUri: Uri,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        Toasty.info(context.applicationContext, R.string.wait_a_bit).show()
        val familyId = DATABASE_ROOT.child(NODE_FAMILIES).push().getKey()!!
        //если человек выбрал эмблему семьи
        if (familyEmblemUri !== Uri.EMPTY) {
            loadEmblemToStorage(
                familyEmblemUri,
                familyId,
                onSuccess = {
                    loadFamilyToFirebase(
                        familyName,
                        it,
                        familyId,
                        onSuccess,
                        onError
                    )

                })
            //дефолтная эмблема
        } else {
            loadFamilyToFirebase(
                familyName,
                DEFAULT_URL,
                familyId,
                onSuccess, onError
            )
        }
    }

    private fun loadFamilyToFirebase(
        familyName: String, linkToEmblem: String, familyId: String,
        onSuccess: () -> Unit = {}, onError: () -> Unit = {}
    ) {
        DATABASE_ROOT.child(NODE_FAMILIES).child(familyId)
            .updateChildren(getFamilyByItems(familyName, linkToEmblem))
            .addOnCompleteListener { task1: Task<Void?> ->
                if (task1.isSuccessful) {
                    //ставлю в юзера значение поля family
                    UserSetterFields.setFamily(
                        USER,
                        familyId,
                        object : IOnEndCommunicationWithFirebase {
                            override fun onSuccess() {
                                onSuccess()
                            }

                            override fun onFailure() {
                                onError()
                            }
                        })
                }
            }
    }

    private fun loadEmblemToStorage(
        familyEmblemUri: Uri, familyId: String,
        onSuccess: (emblemLink: String) -> Unit = {}, onError: () -> Unit = {}
    ) {
        val path: StorageReference = STORAGE_ROOT.child(FOLDER_FAMILY_EMBLEMS).child(familyId)
        path.putFile(familyEmblemUri)
            .addOnCompleteListener { task1: Task<UploadTask.TaskSnapshot?> ->
                if (task1.isSuccessful) {
                    path.downloadUrl.addOnCompleteListener { task2: Task<Uri> ->
                        if (task2.isSuccessful) {
                            val emblemLink = task2.result.toString()
                            onSuccess(emblemLink)
                        } else { onError() }
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