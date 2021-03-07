package com.angogasapps.myfamily.ui.screens.registeractivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.FragmentEnterPersonalDataBinding;
import com.angogasapps.myfamily.firebase.RegisterUserFunks;
import com.angogasapps.myfamily.ui.toaster.Toaster;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.AUTH;

public class EnterPersonalDataFragment extends Fragment {
    private FragmentEnterPersonalDataBinding binding;

    private Calendar timeContainer;
    private Long mTimeMillisBirthday = 0L;
    private Uri userPhotoUri = Uri.EMPTY;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEnterPersonalDataBinding.inflate(getLayoutInflater(), container, false);

        binding.numberPhoneText
                .setText(getString(R.string.you_phone_number) + ": " + AUTH.getCurrentUser().getPhoneNumber());

        timeContainer = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener onDateSetListener = (view1, year, monthOfYear, dayOfMonth) -> {
            timeContainer.set(Calendar.YEAR, year);
            timeContainer.set(Calendar.MONTH, monthOfYear);
            timeContainer.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        };

        binding.selectBirthdayBtn.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener,
                    timeContainer.get(Calendar.YEAR), timeContainer.get(Calendar.MONTH), timeContainer.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        binding.registerButton.setOnClickListener(v -> {
            String mUserName = binding.userNameEditText.getText().toString();
            Log.d("tag", "Name -> " + mUserName + "; Time -> " + mTimeMillisBirthday);
            if (!mUserName.isEmpty() && mTimeMillisBirthday != 0L){
                Log.d("tag", "True");

                RegisterUserFunks.registerNewUser(getActivity(), mUserName, mTimeMillisBirthday, userPhotoUri);
            }
        });

        binding.selectUserPhotoLayout.setOnClickListener(v -> {
            getUserPhoto();
        });

        return binding.getRoot();
    }

    private void setInitialDateTime() {
        mTimeMillisBirthday = timeContainer.getTimeInMillis();
        binding.birthdayTextView.setText(DateUtils.formatDateTime(getActivity(),
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
            binding.userPhotoImageView.setBackground(null);
            binding.userPhotoImageView.setImageURI(userPhotoUri);
        }catch (Exception e){
            e.printStackTrace();
            Toaster.error(getActivity().getApplicationContext(), "неизвестная ошибка").show();
        }
    }
}