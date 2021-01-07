package com.angogasapps.myfamily.ui.screens.registeractivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.angogasapps.myfamily.R;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import static com.angogasapps.myfamily.firebase.AuthFunctions.trySignInWithCredential;


public class EnterCodeFragment extends Fragment {
    private EditText inputCode;
    private String mPhoneNumber;
    private String id;

    public EnterCodeFragment(String mPhoneNumber, String id){
        this.mPhoneNumber = mPhoneNumber;
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_enter_code, container, false);
        inputCode = mView.findViewById(R.id.enterCodeFragmentInputCode);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Прослушиваем набор текста пользователем
        inputCode.addTextChangedListener(new TextWatcher() {
            @Override
            // после набота текста
            public void afterTextChanged(Editable s) {
                String code = inputCode.getText().toString();
                if (code.length() == 6){
                    enterCode();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }
    // функция для отправки и проверки введённого пользователем кода
    private void enterCode(){
        String code = inputCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, code);
        trySignInWithCredential(getActivity(), credential);
    }
}