package com.angogasapps.myfamily.ui.screens.news_center;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.FragmentGetImageBinding;
import com.angogasapps.myfamily.firebase.ChatFunks;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import es.dmoral.toasty.Toasty;


public class GetImageFragment extends Fragment {
    private FragmentGetImageBinding binding;

    private Uri photoUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGetImageBinding.inflate(inflater, container, false);
        initOnClicks();
        return binding.getRoot();
    }

    private void initOnClicks() {
        binding.btnAddImage.setOnClickListener(v -> {
            getPhotoUri();
        });
    }
    private void getPhotoUri() {
        CropImage
                .activity()
//                .setAspectRatio(1, 1)
                .setRequestedSize(1200, 600)
//                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(getActivity(), this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {

            Uri photoUri = CropImage.getActivityResult(data).getUri();
            if (photoUri != null) {
                this.photoUri = photoUri;
                binding.image.setImageURI(photoUri);
            }else {
                Toasty.error(getActivity(), "Что-то пошло не так").show();
            }
        }
    }

    public Uri getImageUri(){
        return this.photoUri;
    }
}