package com.angogasapps.myfamily.ui.screens.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.AppApplication.Companion.isOnline
import com.angogasapps.myfamily.async.notification.TokensManager.updateToken
import com.angogasapps.myfamily.database.DatabaseManager
import com.angogasapps.myfamily.firebase.AuthFunctions.downloadUser
import com.angogasapps.myfamily.firebase.FirebaseHelper
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.firebase.interfaces.IAuthUser
import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.models.User
import com.angogasapps.myfamily.ui.screens.findorcreatefamily.FindOrCreateFamilyActivity
import com.angogasapps.myfamily.ui.screens.main.MainActivity
import com.angogasapps.myfamily.ui.screens.registeractivity.RegisterActivity
import com.angogasapps.myfamily.utils.FamilyManager
import es.dmoral.toasty.Toasty

class SplashActivity : AppCompatActivity() {
    private var familyIdParam: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseHelper.initFirebase()
    }

    public override fun onStart() {
        super.onStart()
        start()
    }

    private fun analysisIntent() {
        intent.data?.let {
            familyIdParam = it.getQueryParameter(FamilyManager.PARAM_FAMILY_ID)?:""
            println("\ndata = $it\nfamily id = $familyIdParam")
        }
    }

    private fun onEndDownloadUser() {
        Log.d("tag", """${USER} """.trimIndent())
        updateToken(USER)
        if (USER.family == "") {
            val intent = Intent(this, FindOrCreateFamilyActivity::class.java)
            intent.putExtra(FamilyManager.PARAM_FAMILY_ID, familyIdParam)
            startActivity(intent)
        } else {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
        finish()
    }

    private fun onError() {
        start()
    }

    fun start() {
        if (isOnline) {
            if (AUTH.currentUser != null) {
                //интернет есть, пользователь аторизован
                analysisIntent()
                downloadUser(object : IAuthUser {
                    override fun onEndDownloadUser() {
                        this@SplashActivity.onEndDownloadUser()
                    }

                    override fun onError() {
                        this@SplashActivity.onError()
                    }
                })
            } else {
                //интернет есть, пользователь не авторизован
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
        } else {
            if (AUTH.currentUser != null) {
                // Вход с данными из БД
                signInWithRoom()
            } else {
                //интернета нет, пользователь не авторизован
                Toasty.error(this, R.string.connection_is_not).show()
            }
        }
    }

    private fun signInWithRoom() {
        DatabaseManager.comeInByDatabase(object: DatabaseManager.IOnEnd{
            override fun onEnd() {
                Family.usersList = DatabaseManager.userList
                if (Family
                        .containsUserWithId(AUTH.currentUser!!.uid)
                ) {
                    USER = Family.getUserById(
                        AUTH.currentUser!!.uid
                    )?: User()
                }
                if (USER.family == "") {
                    Toasty.error(this@SplashActivity, R.string.connection_is_not).show()
                } else {
//                startActivity(new Intent(SplashActivity.this, DeprecatedMainActivity.class));
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }
        })
    }
}