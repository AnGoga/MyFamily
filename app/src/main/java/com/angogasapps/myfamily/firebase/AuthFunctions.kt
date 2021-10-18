package com.angogasapps.myfamily.firebase

import android.app.Activity
import android.util.Log
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.firebase.interfaces.IAuthUser
import com.angogasapps.myfamily.models.User
import com.angogasapps.myfamily.utils.Async
import com.angogasapps.myfamily.utils.downloadBitmapByURL
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty
import java.util.concurrent.TimeUnit

object AuthFunctions {
    @JvmStatic
    @Synchronized
    fun trySignInWithCredential(activity: Activity?, credential: PhoneAuthCredential?, onNewUserSignIn: () -> Unit, onOldUserSignIn: () -> Unit) {
        AUTH.signInWithCredential(credential!!).addOnCompleteListener { authResultTask: Task<AuthResult?> ->
            if (authResultTask.isSuccessful) {
                FirebaseDatabase.getInstance().getReference(NODE_USERS).orderByChild(CHILD_PHONE)
                        .equalTo(AUTH.currentUser!!.phoneNumber).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.value == null) {
                                    //новый пользователь
                                        onNewUserSignIn()
//                                    RegisterActivity.iNewUser.getUserPersonalData()
                                } else {
                                    //старый пользователь
                                        onOldUserSignIn()
//                                        welcomeFunc(activity!!)
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
            } else {
                Toasty.error(activity!!, R.string.something_went_wrong).show()
                Log.e("TAG", authResultTask.exception.toString())
            }
        }
    }

    @JvmStatic
    @Synchronized
    fun authorizationUser(mPhoneNumber: String?, I: Long, timeUnit: TimeUnit?,
                          activity: Activity?, mCallback: OnVerificationStateChangedCallbacks?) {
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(activity!!)
                        .setPhoneNumber(mPhoneNumber!!)
                        .setTimeout(I, timeUnit!!)
                        .setCallbacks(mCallback!!)
                        .build()
        )
    }

    @JvmStatic
    @Synchronized
    fun downloadUser(iAuthUser: IAuthUser) {
        DATABASE_ROOT.child(NODE_USERS).child(UID)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //TODO:
                        USER = snapshot.getValue(User::class.java)!!
                        if (USER != null) {
                            USER.id = snapshot.key?:""
                            if (!USER.photoURL.equals("")){
                                Async.runInNewThread {
                                    downloadBitmapByURL(USER.photoURL).let {
                                        USER.setBitmap(it)
                                    }
                                }
                            }

                            iAuthUser.onEndDownloadUser()
                        } else {
                            AUTH.signOut()
                            iAuthUser.onError()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        iAuthUser.onError()
                    }
                })
    }
}