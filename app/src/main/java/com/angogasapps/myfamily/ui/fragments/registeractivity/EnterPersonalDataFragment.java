package com.angogasapps.myfamily.ui.fragments.registeractivity;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.AuthFunctions;

import java.util.Calendar;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.AUTH;

public class EnterPersonalDataFragment extends Fragment {
    EditText userNameEditText;
    TextView birthdayTextView;
    ImageButton selectBirthdayImageButton;
    Button registerButton;
    Calendar timeContainer;
    Long mTimeMillisBirthday = 0L;


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

                AuthFunctions.registerNewUser(getActivity(), mUserName, mTimeMillisBirthday);
            }
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
}