package com.angogasapps.myfamily.ui.screens.family_storage.gallery_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.databinding.ActivityImageGalleryBinding
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.models.storage.ArrayFolder
import com.angogasapps.myfamily.network.interfaces.family_stoarge.FamilyStorageService
import com.angogasapps.myfamily.ui.screens.family_storage.StorageManager
import com.angogasapps.myfamily.ui.screens.family_storage.gallery_storage_adapters.MediaGalleryStorageAdapter
import com.angogasapps.myfamily.utils.showInDevelopingToast
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MediaGalleryStorageActivity : AppCompatActivity() {

    protected lateinit var binding: ActivityImageGalleryBinding
    protected lateinit var layoutManager: StaggeredGridLayoutManager
    protected lateinit var adapter: MediaGalleryStorageAdapter
    protected lateinit var folder: ArrayFolder
    protected lateinit var folderId: String
    protected lateinit var rootNode: String
    @Inject
    lateinit var storageService: FamilyStorageService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appComponent.inject(this)
        analyzeIntent()
        initRecyclerView()
        initOnClicks()
        initRefresh()
    }

    private fun initRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            updateRecycler()
        }
    }

    private fun updateRecycler() {
        lifecycleScope.launch {
            StorageManager.getInstance().getData(rootNode).collect { isSuccess ->
                withContext(Dispatchers.Main){
                    adapter.update()
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun analyzeIntent() {
        rootNode = intent.extras?.getString(CHILD_ROOT_NODE)?: NODE_IMAGE_STORAGE
        folderId = intent.extras?.getString(CHILD_ID, " ")!!
        for (obj in StorageManager.getInstance().list) {
            if (obj.id == folderId) {
                folder = obj as ArrayFolder
                title = folder.name
                return
            }
        }
        finish()
    }


    private fun initRecyclerView() {
        layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        adapter = MediaGalleryStorageAdapter(this, lifecycleScope, folder, rootNode, storageService)

        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.adapter = adapter
    }

    private fun initOnClicks() {
        binding.floatingBtn.setOnClickListener {
            onFloatingBtnClick()
        }
    }

    private fun onFloatingBtnClick() {
        when (rootNode) {
            NODE_IMAGE_STORAGE -> {
                CropImage.activity()
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .start(this)
            }
            NODE_VIDEO_STORAGE -> {
                showInDevelopingToast()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val onError = { Toasty.warning(this, R.string.something_went_wrong).show() }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            val uri = CropImage.getActivityResult(data).uri

            if (uri != null) {
                storageService.createImageFile(
                        rootNode = NODE_IMAGE_STORAGE,
                        rootFolder = folderId,
                        value = uri,
                        onError = onError,
                        onSuccess = { value, key -> adapter.add(value, key) }
                )
            } else {
                onError()
            }
        }
    }
}