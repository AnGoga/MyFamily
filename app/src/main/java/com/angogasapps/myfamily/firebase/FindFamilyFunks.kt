package com.angogasapps.myfamily.firebase


import com.angogasapps.myfamily.firebase.UserSetterFields.setFamily
import com.angogasapps.myfamily.firebase.interfaces.IOnFindFamily
import com.angogasapps.myfamily.firebase.*
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.google.android.gms.tasks.OnCompleteListener
import com.angogasapps.myfamily.firebase.UserSetterFields
import com.google.android.gms.tasks.Task

object FindFamilyFunks {
    @Synchronized
    fun tryFindFamilyById(id: String, iOnFindFamily: IOnFindFamily) {
        DATABASE_ROOT.child(NODE_FAMILIES).child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        //семья с таким идентификатором не найдена
                        iOnFindFamily.onFailure()
                    } else {
                        //такая семья нашлась
                        iOnFindFamily.onSuccess()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    iOnFindFamily.onCancelled()
                }
            })
    }

    @Synchronized
    fun joinUserToFamily(id: String, iOnJoinToFamily: IOnEndCommunicationWithFirebase) {
        // устанавливаем в семье данного человека как участника
        DATABASE_ROOT.child(NODE_FAMILIES).child(id)
            .child(CHILD_MEMBERS).child(UID)
            .setValue(ROLE_MEMBER)
            .addOnCompleteListener { task1: Task<Void?> ->
                if (task1.isSuccessful) {
                    //устанавлеваем в юзера значение поля family
                    setFamily(
                        USER,
                        id,
                        object : IOnEndCommunicationWithFirebase {
                            override fun onSuccess() {
                                iOnJoinToFamily.onSuccess()
                            }

                            override fun onFailure() {
                                iOnJoinToFamily.onFailure()
                            }
                        })
                }
            }
    }
}