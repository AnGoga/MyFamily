package com.angogasapps.myfamily.ui.screens.family_storage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityCreateImageFileBinding
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*
import com.angogasapps.myfamily.firebase.createImageFile
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty

class CreateImageFileActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateImageFileBinding
    private var uri: Uri? = null
    private var rootFolder: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateImageFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        analyzeIntent()
        initOnClicks()
    }

    private fun analyzeIntent(){
        rootFolder = intent.getStringExtra(ROOT_FOLDER)
    }

    private fun initOnClicks() {
        binding.addImageBtn.setOnClickListener {
            getPhotoUri()
        }

        binding.saveButton.setOnClickListener {
            val str = binding.editText.text.toString().trim()
            if (uri == null){
                Toasty.error(this, getString(R.string.add_image)).show()
                return@setOnClickListener
            }
            if (str.isEmpty()){
                Toasty.error(this, getString(R.string.enter_image_name)).show()
                return@setOnClickListener
            }
            createNewImageFile()
        }
    }

    private fun getPhotoUri(){
        CropImage.activity()
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            val uri = CropImage.getActivityResult(data).uri
            if (uri != null) {
                binding.image.setImageURI(uri)
                this.uri = uri
            }else {
                Toasty.error(this, "Что-то пошло не так").show()
            }
        }
    }


    private fun createNewImageFile() {
        createImageFile(
                name = binding.editText.text.toString().trim(),
                rootNode = NODE_IMAGE_STORAGE,
                rootFolder = rootFolder?: CHILD_BASE_FOLDER,
                value = uri!!,
                onError = {Toasty.warning(this, R.string.something_went_wrong).show()}
        )
        finish()
    }


}