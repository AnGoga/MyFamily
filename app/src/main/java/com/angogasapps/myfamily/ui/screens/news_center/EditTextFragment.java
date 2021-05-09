package com.angogasapps.myfamily.ui.screens.news_center;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.FragmentEditTextBinding;


public class EditTextFragment extends Fragment {
    private FragmentEditTextBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditTextBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public String getText(){
        return binding.editText.getText().toString();
    }

    public void resetEditText() {
        binding.editText.setText("");
    }
}