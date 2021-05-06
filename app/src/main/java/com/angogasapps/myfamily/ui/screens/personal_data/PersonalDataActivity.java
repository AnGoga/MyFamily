package com.angogasapps.myfamily.ui.screens.personal_data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View.OnClickListener;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.databinding.ActivityPersonalDataBinding;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class PersonalDataActivity extends AppCompatActivity {
    private ActivityPersonalDataBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (USER.getUserPhoto() != null){
            binding.userImage.setImageBitmap(USER.getUserPhoto());
        }

        OnClickListener plugClickListener = v -> {
            AppApplication.showInDevelopingToast();
        };

        binding.changePersonalData.setOnClickListener(plugClickListener);
        binding.personalDairy.setOnClickListener(plugClickListener);


    }
}