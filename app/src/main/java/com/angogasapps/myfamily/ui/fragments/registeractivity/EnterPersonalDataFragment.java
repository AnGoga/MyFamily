package com.angogasapps.myfamily.ui.fragments.registeractivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.AuthFunctions;
import com.angogasapps.myfamily.firebase.RegisterUserFunks;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.AUTH;

public class EnterPersonalDataFragment extends Fragment {
    EditText userNameEditText;
    TextView birthdayTextView;
    ImageButton selectBirthdayImageButton;
    Button registerButton;
    Calendar timeContainer;
    ImageView userPhotoImageView;
    ConstraintLayout selectUserPhotoLayout;
    Long mTimeMillisBirthday = 0L;
    Uri userPhotoUri = Uri.EMPTY;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_personal_data, container, false);
        ((TextView) view.findViewById(R.id.fragmentPersonalDataNumberPhone))
                .setText(getString(R.string.you_phone_number) + ": " + AUTH.getCurrentUser().getPhoneNumber());
        userNameEditText = view.findViewById(R.id.fragmentEnterPersonalDataUserNameEditText);
        birthdayTextView = view.findViewById(R.id.fragmentEnterPersonalDataDayOfBirthTextView);
        selectBirthdayImageButton = view.findViewById(R.id.fragmentEnterPersonalDataSelectDataBirthday);
        registerButton = view.findViewById(R.id.fragmentEnterPersonalDataRegisterButton);
        userPhotoImageView = view.findViewById(R.id.fragmentEnterPersonalDataUserImage);
        selectUserPhotoLayout = view.findViewById(R.id.fragmentEnterPersonalDataSelectUserPhotoLayout);

        timeContainer = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener onDateSetListener = (view1, year, monthOfYear, dayOfMonth) -> {
            timeContainer.set(Calendar.YEAR, year);
            timeContainer.set(Calendar.MONTH, monthOfYear);
            timeContainer.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        };

        selectBirthdayImageButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener,
                    timeContainer.get(Calendar.YEAR), timeContainer.get(Calendar.MONTH), timeContainer.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        registerButton.setOnClickListener(v -> {
            String mUserName = userNameEditText.getText().toString();
            Log.d("tag", "Name -> " + mUserName + "; Time -> " + mTimeMillisBirthday);
            if (!mUserName.isEmpty() && mTimeMillisBirthday != 0L){
                Log.d("tag", "True");

                RegisterUserFunks.registerNewUser(getActivity(), mUserName, mTimeMillisBirthday, userPhotoUri);
            }
        });

        selectUserPhotoLayout.setOnClickListener(v -> {
            getUserPhoto();
        });

        return view;
    }

    private void setInitialDateTime() {
        mTimeMillisBirthday = timeContainer.getTimeInMillis();
        birthdayTextView.setText(DateUtils.formatDateTime(getActivity(),
                mTimeMillisBirthday,
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
                        //| DateUtils.FORMAT_SHOW_TIME));
    }

    private void getUserPhoto() {
        CropImage.activity().setAspectRatio(1, 1)
                .setRequestedSize(240, 240)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(getActivity(), this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            userPhotoUri = CropImage.getActivityResult(data).getUri();
            userPhotoImageView.setBackground(null);
            userPhotoImageView.setImageURI(userPhotoUri);
        }catch (Exception e){
            e.printStackTrace();
            Toaster.error(getActivity().getApplicationContext(), "неизвестная ошибка").show();
        }
    }
}