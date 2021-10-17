package com.angogasapps.myfamily.ui.screens.news_center


import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.ui.screens.news_center.EditTextFragment
import com.angogasapps.myfamily.ui.screens.news_center.GetImageFragment
import android.os.Bundle
import android.view.View
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityCreateNewNewsBinding
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import es.dmoral.toasty.Toasty
import com.angogasapps.myfamily.firebase.NewsCenterFunks

class CreateNewNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewNewsBinding
    private lateinit var editTextFragment: EditTextFragment
    private lateinit var getImageFragment: GetImageFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTextFragment = EditTextFragment()
        getImageFragment = GetImageFragment()
        supportFragmentManager.beginTransaction().add(R.id.data_container, editTextFragment).commit()
        initOnClicks()
    }

    private fun initOnClicks() {
        binding.btnCreate.setOnClickListener { createNewNews() }
        binding.radioText.setOnClickListener {
            if (binding.radioText.isChecked) {
                setEditTextFragment()
            }
        }
        binding.radioImage.setOnClickListener {
            if (binding.radioImage.isChecked) {
                setGetImageFragment()
            }
        }
    }

    private fun setEditTextFragment() {
        supportFragmentManager
            .beginTransaction().replace(R.id.data_container, editTextFragment).commit()
    }

    private fun setGetImageFragment() {
        supportFragmentManager
            .beginTransaction().replace(R.id.data_container, getImageFragment).commit()
    }

    private fun createNewNews() {
        val i: IOnEndCommunicationWithFirebase = object : IOnEndCommunicationWithFirebase {
            override fun onSuccess() {
                Toasty.success(this@CreateNewNewsActivity, R.string.news_success_add).show()
                finish()
            }

            override fun onFailure() {
                Toasty.error(this@CreateNewNewsActivity, R.string.something_went_wrong).show()
            }
        }
        if (binding.radioText.isChecked) {
            if (editTextFragment.text.trim { it <= ' ' }.isEmpty()) {
                editTextFragment.resetEditText()
                Toasty.warning(this, R.string.enter_news_text).show()
            } else {
                NewsCenterFunks.createNewTextNews(editTextFragment.text.trim { it <= ' ' }, i)
            }
        } else if (binding.radioImage.isChecked) {
            if (getImageFragment.imageUri != null) {
                NewsCenterFunks.createNewImageNews(getImageFragment.imageUri, i)
            } else {
                Toasty.warning(this, R.string.chose_image).show()
            }
        }
    }
}