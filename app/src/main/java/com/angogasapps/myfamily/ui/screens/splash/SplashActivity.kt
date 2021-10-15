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
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.firebase.interfaces.IAuthUser
import com.angogasapps.myfamily.models.Family
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
        Log.d("tag", """${FirebaseVarsAndConsts.USER} """.trimIndent())
        updateToken(FirebaseVarsAndConsts.USER)
        if (FirebaseVarsAndConsts.USER.family == "") {
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
            if (FirebaseVarsAndConsts.AUTH.currentUser != null) {
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
            if (FirebaseVarsAndConsts.AUTH.currentUser != null) {
                // Вход с данными из БД
                signInWithRoom()
            } else {
                //интернета нет, пользователь не авторизован
                Toasty.error(this, R.string.connection_is_not).show()
            }
        }
    }

    private fun signInWithRoom() {
        DatabaseManager.comeInByDatabase {
            Family.getInstance().usersList = DatabaseManager.getUserList()
            if (Family.getInstance()
                    .containsUserWithId(FirebaseVarsAndConsts.AUTH.currentUser!!.uid)
            ) {
                FirebaseVarsAndConsts.USER = Family.getInstance().getUserById(
                    FirebaseVarsAndConsts.AUTH.currentUser!!.uid
                )
            }
            if (FirebaseVarsAndConsts.USER.family == "") {
                Toasty.error(this, R.string.connection_is_not).show()
            } else {
//                startActivity(new Intent(SplashActivity.this, DeprecatedMainActivity.class));
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}