package com.angogasapps.myfamily.ui.fragments.findorcreatefamily;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.RegisterFamilyFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndRegisterNewFamily;
import com.angogasapps.myfamily.ui.activities.MainActivity;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class CreateFamilyFragment extends Fragment {
    private Button createFamilyButton;
    private EditText familyNameEditText;
    private ConstraintLayout addFamilyEmblemButton;
    private Uri mFamilyEmblemUri;
    private ImageView mEmblemImageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_family, container, false);
        createFamilyButton = rootView.findViewById(R.id.createFamilyButton);
        familyNameEditText = rootView.findViewById(R.id.fragmentCreateFamilyEditTextFamilyName);
        addFamilyEmblemButton = rootView.findViewById(R.id.fragmentCreateFamilyAddEmblemPhoto);
        mEmblemImageView = rootView.findViewById(R.id.fragmentCreateFamilyEmblemImage);

        addFamilyEmblemButton.setOnClickListener(v -> {
            addFamilyEmblem();
        });

        createFamilyButton.setOnClickListener(v -> {
            String familyName = familyNameEditText.getText().toString();
            if (familyName.isEmpty()){
                Toaster.info(getActivity().getApplicationContext(), R.string.enter_family_last_name).show();
                return;
            }
            if(mFamilyEmblemUri == null){
                //mFamilyEmblemUri = defaultPhoto
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

        return rootView;
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
            mEmblemImageView.setBackground(null);
            mEmblemImageView.setImageURI(mFamilyEmblemUri);
        }catch (Exception e){
            e.printStackTrace();
            Toaster.error(getActivity().getApplicationContext(), "неизвестная ошибка").show();
        }


    }
}