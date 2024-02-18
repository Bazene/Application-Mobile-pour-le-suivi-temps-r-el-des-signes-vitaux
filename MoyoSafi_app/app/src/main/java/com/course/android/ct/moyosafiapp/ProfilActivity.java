package com.course.android.ct.moyosafiapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilActivity extends AppCompatActivity {

    //VARIABLES
    private TextView profile_to_settings;

    // FUNCTIONS
    // 1- onCreate function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // GET VIEWS
        profile_to_settings = findViewById(R.id.profile_to_settings);

        // ACTIONS
        // 1- to profile_to_settings view
        profile_to_settings.setOnClickListener(v-> onBackPressed()); // back when pressed
    }
}