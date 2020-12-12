package com.angogasapps.myfamily.ui.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.angogasapps.myfamily.R;


public class FindOrCreateFamilyFragment extends Fragment {
    private Button createFamilyBtn;
    private Button findFamilyBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_find_or_create_family, container, false);
        createFamilyBtn = rootView.findViewById(R.id.focaCreateFamilyButton);
        findFamilyBtn = rootView.findViewById(R.id.focaFindFamilyButton);

        createFamilyBtn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.findOrCreateFamilyDataContainer, new CreateFamilyFragment()).commit();
        });
        findFamilyBtn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.findOrCreateFamilyDataContainer, new FindFamilyFragment()).commit();
        });
        return rootView;

    }
}