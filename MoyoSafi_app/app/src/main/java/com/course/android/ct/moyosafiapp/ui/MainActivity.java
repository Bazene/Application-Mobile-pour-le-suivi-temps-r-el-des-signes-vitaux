package com.course.android.ct.moyosafiapp.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.course.android.ct.moyosafiapp.R;
import com.course.android.ct.moyosafiapp.ui.home.HomeFragment;
import com.course.android.ct.moyosafiapp.ui.notifications.NotificationsFragment;
import com.course.android.ct.moyosafiapp.ui.settings.SettingsFragment;
import com.course.android.ct.moyosafiapp.viewModel.PatientViewModel;
import com.course.android.ct.moyosafiapp.viewModel.injections.ViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends FragmentActivity {

    // VARIABLES
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    NotificationsFragment notificationsFragment = new NotificationsFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    private PatientViewModel patientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CONFIGURATION OF OUR VIEWMODEL
        configureViewModel();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // The launch fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, homeFragment).commit();

        // Fragment selected
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, homeFragment).commit();
                } else if (item.getItemId() == R.id.notifications){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, notificationsFragment).commit();
                } else if (item.getItemId() == R.id.settings){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, settingsFragment).commit();
                }
                return true;
            }
        });
    }

    // 1- configuration of our view model
    private void configureViewModel() {
        this.patientViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(PatientViewModel.class) ;
    }
}