package com.angogasapps.myfamily.ui.screens.findorcreatefamily;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.FindFamilyFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnFindFamily;
import com.angogasapps.myfamily.firebase.interfaces.IOnJoinToFamily;
import com.angogasapps.myfamily.ui.screens.main.MainActivity;
import com.angogasapps.myfamily.ui.toaster.Toaster;


public class FindFamilyFragment extends Fragment {
    Button joinButton;
    AppCompatEditText identificationEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_family, container, false);
        joinButton = rootView.findViewById(R.id.fragmentFindFamilyJoinButton);
        identificationEditText = rootView.findViewById(R.id.fragmentFindFamilyEditText);

        joinButton.setOnClickListener(v -> {
            String text = identificationEditText.getText().toString();
            if (text.isEmpty()){
                Toaster.warning(getActivity().getApplicationContext(), R.string.enter_identification).show();
            }else{
                FindFamilyFunks.tryFindFamilyById(text, new IOnFindFamily() {
                    @Override
                    public void onSuccess() {
                        FindFamilyFunks.joinUserToFamily(text, new IOnJoinToFamily() {
                            @Override
                            public void onSuccess() {
                                Toaster.success(getActivity().getApplicationContext(),
                                        R.string.you_success_join_to_you_family).show();

                                getActivity().startActivity(new Intent(getActivity().getApplicationContext(),
                                        MainActivity.class));
                                getActivity().finish();
                            }
                            @Override
                            public void onFailure() {
                                Toaster.error(getActivity().getApplicationContext(),
                                        R.string.error).show();
                            }
                        });
                    }
                    @Override
                    public void onFailure() {
                        Toaster.error(getActivity().getApplicationContext(),
                                R.string.family_not_found).show();
                    }
                    @Override
                    public void onCancelled() {
                        Toaster.error(getActivity().getApplicationContext(),
                                R.string.you_canceled_searches).show();
                    }
                });
            }
        });

        return rootView;
    }
}