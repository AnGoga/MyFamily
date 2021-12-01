package com.angogasapps.myfamily.network.firebaseImpl.families

import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.network.interfaces.families.FindFamilyService
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject


class FirebaseFindFamilyServiceImpl @Inject constructor() : FindFamilyService {
    override fun tryFindFamilyById(id: String, onSuccess: () -> Unit, onError: () -> Unit) {
        DATABASE_ROOT.child(NODE_FAMILIES).child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        //семья с таким идентификатором не найдена
                        onError()
                    } else {
                        //такая семья нашлась
                        onSuccess()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onError()
                }
            })
    }

    override fun joinUserToFamily(id: String, onSuccess: () -> Unit, onError: () -> Unit) {
        // устанавливаем в семье данного человека как участника
        DATABASE_ROOT.child(NODE_FAMILIES).child(id)
            .child(CHILD_MEMBERS).child(UID)
            .setValue(ROLE_MEMBER)
            .addOnCompleteListener { task1: Task<Void?> ->
                if (task1.isSuccessful) {
                    //устанавлеваем в юзера значение поля family
                    UserSetterFields.setFamily(
                        USER,
                        id,
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
}