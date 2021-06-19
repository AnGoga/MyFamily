package com.angogasapps.myfamily.ui.screens.registeractivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER
import com.angogasapps.myfamily.models.User
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity
import es.dmoral.toasty.Toasty

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val onOldUserSignIn: () -> Unit = {
            Toasty.success(this, getString(R.string.welcome)).show()
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }

        val onNewUserSignIn: () -> Unit = {
            if (USER == null){
                USER = User().also { it.id = AUTH.currentUser!!.uid }
            }
            supportFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.registerDataContainer,
                            EnterPersonalDataFragment(onOldUserSignIn = onOldUserSignIn)
                    )
                    .addToBackStack("")
                    .commit()
        }
        supportFragmentManager
                .beginTransaction()
                .add(R.id.registerDataContainer, EnterPhoneFragment(onNewUserSignIn = onNewUserSignIn, onOldUserSignIn = onOldUserSignIn))
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_signout) {
            AUTH.signOut()
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}