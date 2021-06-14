package com.angogasapps.myfamily.ui.screens.family_storage.gallery_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityImageGalleryBinding
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_ID
import com.angogasapps.myfamily.firebase.createImageFile
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.ui.screens.family_storage.StorageManager
import com.angogasapps.myfamily.ui.screens.family_storage.StorageManager.Companion
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty

class ImageGalleryActivity : AppCompatActivity() {
    lateinit var binding: ActivityImageGalleryBinding
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var adapter: ImageGalleryAdapter
    private lateinit var folder: ArrayFolder
    private lateinit var folderId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analyzeIntent()
        initRecyclerView()
        initOnClicks()
    }

    private fun analyzeIntent() {
        folderId = intent.extras?.getString(CHILD_ID, " ")!!
        for (obj in StorageManager.getInstance().list){
            if (obj.id == folderId){
                folder = obj as ArrayFolder
                return
            }
        }
        finish()
    }


    private fun initRecyclerView() {
        layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        adapter = ImageGalleryAdapter(this, folder)

        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.adapter = adapter
    }

    private fun initOnClicks() {
        binding.floatingBtn.setOnClickListener {
            getImage()
        }
    }

    private fun getImage(){
        CropImage.activity()
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val onError = {Toasty.warning(this, R.string.something_went_wrong).show()}

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            val uri = CropImage.getActivityResult(data).uri

            if (uri != null) {
                createImageFile(
                        rootNode = FirebaseVarsAndConsts.NODE_IMAGE_STORAGE,
                        rootFolder = folderId,
                        value = uri,
                        onError = onError,
                        onSuccess = {  }
                )
            }else {
                onError()
            }
        }
    }
}