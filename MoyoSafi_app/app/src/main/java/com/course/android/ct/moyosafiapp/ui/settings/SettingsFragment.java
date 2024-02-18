package com.course.android.ct.moyosafiapp.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.course.android.ct.moyosafiapp.BluetoothActivity;
import com.course.android.ct.moyosafiapp.ProfilActivity;
import com.course.android.ct.moyosafiapp.R;

public class SettingsFragment extends Fragment {

    // VARIABLES
    private ConstraintLayout settings_to_bluetooth;
    private ConstraintLayout profile_bloc;

    // DEFAULT CONSTRUCT
    public SettingsFragment() {
        // Required empty public constructor
    }

    // FUNCTIONS
    // 1- first function
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_settings, container, false); // our view (fragment_layout)

            // GET VIEWS
            settings_to_bluetooth = view.findViewById(R.id.settings_to_bluetooth); // we take the bluetooth_bloc
            profile_bloc = view.findViewById(R.id.profile_bloc);

            // ACTIONS
            // 1- to settings_to_bluetooth view

                settings_to_bluetooth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), BluetoothActivity.class);
                        startActivity(intent);
                    }
                });

            // 2- to profile_bloc view
                profile_bloc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ProfilActivity.class);
                        startActivity(intent);
                    }
                });

            return view;
        }
}