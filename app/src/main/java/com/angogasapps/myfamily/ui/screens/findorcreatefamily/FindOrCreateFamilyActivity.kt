package com.angogasapps.myfamily.ui.screens.findorcreatefamily

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.angogasapps.myfamily.utils.FamilyManager
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityFindOrCreateFamilyBinding
import com.angogasapps.myfamily.firebase.FindFamilyFunks
import com.angogasapps.myfamily.firebase.interfaces.IOnFindFamily
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.ui.screens.main.MainActivity
import es.dmoral.toasty.Toasty
import com.angogasapps.myfamily.firebase.RegisterFamilyFunks
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.firebase.interfaces.IOnEndRegisterNewFamily
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity
import java.lang.Exception

class FindOrCreateFamilyActivity : AppCompatActivity() {
    private var familyIdParam: String? = null
    private lateinit var binding: ActivityFindOrCreateFamilyBinding
    private var mFamilyEmblemUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindOrCreateFamilyBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        analyzeIntent()
        setJoinBtnOnClick()
        addCreateFamilyOnClick()
    }

    private fun analyzeIntent() {
        val intent = intent
        familyIdParam = intent.getStringExtra(FamilyManager.PARAM_FAMILY_ID)
        if (familyIdParam != null && familyIdParam != "") {
            showInviteDialog()
        }
    }

    private fun showInviteDialog() {
        val alertDialog = AlertDialog.Builder(this).setTitle(R.string.you_was_invite_to_family)
            .setMessage(R.string.do_you_want_join_to_invite_family)
            .setPositiveButton(getString(R.string.yes)) { dialog: DialogInterface?, which: Int -> joinToInviteFamily() }
            .setNegativeButton(getString(R.string.no)) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
            //                .setOnDismissListener(dialog -> startFragments())
            .create()
        alertDialog.show()
    }

    private fun joinToInviteFamily() {
        FindFamilyFunks.tryFindFamilyById(familyIdParam!!, object : IOnFindFamily {
            override fun onSuccess() {
                FindFamilyFunks.joinUserToFamily(familyIdParam!!, object : IOnEndCommunicationWithFirebase {
                    override fun onSuccess() {
                        startActivity(
                            Intent(
                                this@FindOrCreateFamilyActivity,
                                MainActivity::class.java
                            )
                        )
                        finish()
                    }

                    override fun onFailure() {}
                })
            }

            override fun onFailure() {
                familyNotFound()
            }

            override fun onCancelled() {
                familyNotFound()
            }
        })
    }

    private fun familyNotFound() {
        Toasty.error(this, R.string.link_is_not_correct).show()
    }

    fun setJoinBtnOnClick() {
        binding.joinBtn.setOnClickListener { v: View? ->
            val text = binding.editText.text.toString()
            if (text.isEmpty()) {
                Toasty.warning(this@FindOrCreateFamilyActivity, R.string.enter_identification)
                    .show()
            } else {
                FindFamilyFunks.tryFindFamilyById(text, object : IOnFindFamily {
                    override fun onSuccess() {
                        FindFamilyFunks.joinUserToFamily(text, object : IOnEndCommunicationWithFirebase {
                            override fun onSuccess() {
                                Toasty.success(
                                    this@FindOrCreateFamilyActivity,
                                    R.string.you_success_join_to_you_family
                                ).show()
                                this@FindOrCreateFamilyActivity.startActivity(
                                    Intent(
                                        this@FindOrCreateFamilyActivity.applicationContext,
                                        MainActivity::class.java
                                    )
                                )
                                finish()
                            }

                            override fun onFailure() {
                                Toasty.error(
                                    this@FindOrCreateFamilyActivity,
                                    R.string.error
                                ).show()
                            }
                        })
                    }

                    override fun onFailure() {
                        Toasty.error(this@FindOrCreateFamilyActivity, R.string.family_not_found)
                            .show()
                    }

                    override fun onCancelled() {
                        Toasty.error(
                            this@FindOrCreateFamilyActivity,
                            R.string.you_canceled_searches
                        ).show()
                    }
                })
            }
        }
    }

    fun addCreateFamilyOnClick() {
        binding.addEmblemBtn.setOnClickListener { v: View? -> addFamilyEmblem() }
        binding.createFamilyButton.setOnClickListener { v: View? ->
            val familyName = binding.familyNameEditText.text.toString()
            if (familyName.isEmpty()) {
                Toasty.info(this, R.string.enter_family_last_name).show()
                return@setOnClickListener
            }
            if (mFamilyEmblemUri == null) {
                mFamilyEmblemUri = Uri.EMPTY
            }
            RegisterFamilyFunks.createNewFamily(
                this,
                familyName, mFamilyEmblemUri!!,
                object: IOnEndRegisterNewFamily{
                    override fun onEndRegister() {

                        //когда регистрация новой семьи в базе данных прошла успешно
                        Toasty.success(
                            this@FindOrCreateFamilyActivity.applicationContext,
                            R.string.everything_went_well
                        ).show()
                        startActivity(Intent(this@FindOrCreateFamilyActivity, MainActivity::class.java))
                        finish()
                    }
                }
            )
        }
    }

    private fun addFamilyEmblem() {
        CropImage.activity().setAspectRatio(1, 1)
            .setRequestedSize(300, 300)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(this@FindOrCreateFamilyActivity)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            mFamilyEmblemUri = CropImage.getActivityResult(data).uri
            binding.familyEmblemImage.background = null
            binding.familyEmblemImage.setImageURI(mFamilyEmblemUri)
        } catch (e: Exception) {
            e.printStackTrace()
            Toasty.error(this, "неизвестная ошибка").show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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