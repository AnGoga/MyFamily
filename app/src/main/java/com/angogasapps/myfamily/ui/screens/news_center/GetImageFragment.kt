package com.angogasapps.myfamily.ui.screens.news_center

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.theartofdev.edmodo.cropper.CropImage
import android.content.Intent
import android.app.Activity
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import com.angogasapps.myfamily.databinding.FragmentGetImageBinding
import es.dmoral.toasty.Toasty

class GetImageFragment : Fragment() {
    private lateinit var binding: FragmentGetImageBinding

    var imageUri: Uri? = null
        private set

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGetImageBinding.inflate(inflater, container, false)
        initOnClicks()
        return binding.root
    }

    private fun initOnClicks() {
        binding.btnAddImage.setOnClickListener { getPhotoUri() }
    }

    private fun getPhotoUri() {
        CropImage
            .activity()
            .setRequestedSize(1200, 600)
            .start(requireActivity(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
        ) {
            val photoUri = CropImage.getActivityResult(data).uri
            if (photoUri != null) {
                imageUri = photoUri
                binding.image.setImageURI(photoUri)
            } else {
                Toasty.error(requireActivity(), "Что-то пошло не так").show()
            }
        }
    }
}