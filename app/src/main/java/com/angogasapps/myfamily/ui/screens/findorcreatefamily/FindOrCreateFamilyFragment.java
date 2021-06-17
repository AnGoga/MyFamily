package com.angogasapps.myfamily.ui.screens.findorcreatefamily;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.FragmentFindOrCreateFamilyBinding;


public class FindOrCreateFamilyFragment extends Fragment {

    private FragmentFindOrCreateFamilyBinding binding;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        binding = FragmentFindOrCreateFamilyBinding.inflate(getLayoutInflater(), container, false);
//
//        binding.createFamilyButton.setOnClickListener(v -> {
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.findOrCreateFamilyDataContainer, new CreateFamilyFragment()).addToBackStack(null).commit();
//        });
//        binding.findFamilyButton.setOnClickListener(v -> {
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.findOrCreateFamilyDataContainer, new FindFamilyFragment()).addToBackStack(null).commit();
//        });
//        return binding.getRoot();
//
//    }
}