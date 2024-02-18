package com.course.android.ct.moyosafiapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.course.android.ct.moyosafiapp.ui.authentification.LoginFragment;

public class AuthentificationActivity extends AppCompatActivity {

    // FRAGMENT
    LoginFragment loginFragment = new LoginFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        // The launch fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.authentification_frame_layout, loginFragment).commit();

    }
}