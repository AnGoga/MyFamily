package com.angogasapps.myfamily.ui.screens.personal_data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import android.os.Bundle;

import com.angogasapps.myfamily.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class PersonalDataActivity extends AppCompatActivity {
    CircleImageView userImage;
    CardView cardDairy, cardData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        userImage = findViewById(R.id.user_image);
        cardDairy = findViewById(R.id.card_personal_dairy);
        cardData = findViewById(R.id.card_share_personal_data);

        if (USER.getUserPhoto() != null){
            userImage.setImageBitmap(USER.getUserPhoto());
        }


    }
}