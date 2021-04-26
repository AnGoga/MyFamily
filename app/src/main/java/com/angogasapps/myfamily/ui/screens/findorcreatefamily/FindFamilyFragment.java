package com.angogasapps.myfamily.ui.screens.findorcreatefamily;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.FragmentFindFamilyBinding;
import com.angogasapps.myfamily.firebase.FindFamilyFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnFindFamily;
import com.angogasapps.myfamily.firebase.interfaces.IOnJoinToFamily;
import com.angogasapps.myfamily.ui.screens.main.MainActivity;

import es.dmoral.toasty.Toasty;


public class FindFamilyFragment extends Fragment {
    private FragmentFindFamilyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFindFamilyBinding.inflate(getLayoutInflater(), container, false);

        binding.joinBtn.setOnClickListener(v -> {
            String text = binding.editText.getText().toString();
            if (text.isEmpty()){
                Toasty.warning(getActivity().getApplicationContext(), R.string.enter_identification).show();
            }else{
                FindFamilyFunks.tryFindFamilyById(text, new IOnFindFamily() {
                    @Override
                    public void onSuccess() {
                        FindFamilyFunks.joinUserToFamily(text, new IOnJoinToFamily() {
                            @Override
                            public void onSuccess() {
                                Toasty.success(getActivity().getApplicationContext(),
                                        R.string.you_success_join_to_you_family).show();

                                getActivity().startActivity(new Intent(getActivity().getApplicationContext(),
                                        MainActivity.class));
                                getActivity().finish();
                            }
                            @Override
                            public void onFailure() {
                                Toasty.error(getActivity().getApplicationContext(),
                                        R.string.error).show();
                            }
                        });
                    }
                    @Override
                    public void onFailure() {
                        Toasty.error(getActivity().getApplicationContext(),
                                R.string.family_not_found).show();
                    }
                    @Override
                    public void onCancelled() {
                        Toasty.error(getActivity().getApplicationContext(),
                                R.string.you_canceled_searches).show();
                    }
                });
            }
        });

        return binding.getRoot();
    }
}