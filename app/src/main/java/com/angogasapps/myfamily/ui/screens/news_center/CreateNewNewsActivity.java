package com.angogasapps.myfamily.ui.screens.news_center;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.ActivityCreateNewNewsBinding;
import com.angogasapps.myfamily.firebase.NewsCenterFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;
import com.angogasapps.myfamily.ui.screens.main.EditTextFragment;
import com.angogasapps.myfamily.ui.screens.main.GetImageFragment;

import es.dmoral.toasty.Toasty;

public class CreateNewNewsActivity extends AppCompatActivity {
    private ActivityCreateNewNewsBinding binding;

    private EditTextFragment editTextFragment;
    private GetImageFragment getImageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNewNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editTextFragment = new EditTextFragment();
        getImageFragment = new GetImageFragment();

        getSupportFragmentManager()
                .beginTransaction().add(R.id.data_container, editTextFragment).commit();

        initOnClicks();
    }

    private void initOnClicks() {
        binding.btnCreate.setOnClickListener(v -> {
            createNewNews();
        });
        binding.radioText.setOnClickListener(v -> {
            if (binding.radioText.isChecked()){
                setEditTextFragment();
            }
        });
        binding.radioImage.setOnClickListener(v -> {
            if (binding.radioImage.isChecked()){
                setGetImageFragment();
            }
        });
    }

    private void setEditTextFragment(){
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.data_container, editTextFragment).commit();
    }

    private void setGetImageFragment(){
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.data_container, getImageFragment).commit();
    }

    private void createNewNews() {
        IOnEndCommunicationWithFirebase i = new IOnEndCommunicationWithFirebase() {
            @Override
            public void onSuccess() {
                Toasty.success(CreateNewNewsActivity.this, R.string.news_success_add).show();
                finish();
            }
            @Override
            public void onFailure() {
                Toasty.error(CreateNewNewsActivity.this, R.string.something_went_wrong).show();
            }
        };

        if (binding.radioText.isChecked()){
            if (editTextFragment.getText().trim().isEmpty()){
                editTextFragment.resetEditText();
                Toasty.warning(this, R.string.enter_news_text).show();
            }else{
                NewsCenterFunks.createNewTextNews(editTextFragment.getText().trim(), i);
            }
        }else if (binding.radioImage.isChecked()){
            if (getImageFragment.getImageUri() != null){
                NewsCenterFunks.createNewImageNews(getImageFragment.getImageUri(), i);
            }else{
                Toasty.warning(this, R.string.chose_image).show();
            }
        }
    }


}