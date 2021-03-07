package com.angogasapps.myfamily.ui.screens.findorcreatefamily;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.FragmentCreateFamilyBinding;
import com.angogasapps.myfamily.firebase.RegisterFamilyFunks;
import com.angogasapps.myfamily.ui.screens.main.MainActivity;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class CreateFamilyFragment extends Fragment {
    private FragmentCreateFamilyBinding binding;

    private Uri mFamilyEmblemUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateFamilyBinding.inflate(getLayoutInflater(), container, false);


        binding.addEmblemBtn.setOnClickListener(v -> {
            addFamilyEmblem();
        });

        binding.createFamilyButton.setOnClickListener(v -> {
            String familyName = binding.familyNameEditText.getText().toString();
            if (familyName.isEmpty()){
                Toaster.info(getActivity().getApplicationContext(), R.string.enter_family_last_name).show();
                return;
            }
            if(mFamilyEmblemUri == null){
                mFamilyEmblemUri = Uri.EMPTY;
            }
            RegisterFamilyFunks.createNewFamily(getActivity().getApplicationContext(),
                    familyName, mFamilyEmblemUri, () -> {
                //когда регистрация новой семьи в базе данных прошла успешно
                Toaster.success(getActivity().getApplicationContext(),
                        R.string.everything_went_well).show();
                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                getActivity().finish();
            });
        });

        return binding.getRoot();
    }

    private void addFamilyEmblem() {
        CropImage.activity().setAspectRatio(1, 1)
                .setRequestedSize(300, 300)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(getActivity(), this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            mFamilyEmblemUri = CropImage.getActivityResult(data).getUri();
            binding.familyEmblemImage.setBackground(null);
            binding.familyEmblemImage.setImageURI(mFamilyEmblemUri);
        }catch (Exception e){
            e.printStackTrace();
            Toaster.error(getActivity().getApplicationContext(), "неизвестная ошибка").show();
        }
    }
}