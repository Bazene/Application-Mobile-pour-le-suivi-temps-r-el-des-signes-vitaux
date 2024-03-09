package com.course.android.ct.moyosafiapp.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.course.android.ct.moyosafiapp.databinding.FragmentSettingsBinding;
import com.course.android.ct.moyosafiapp.models.SessionManager;
import com.course.android.ct.moyosafiapp.ui.AuthentificationActivity;
import com.course.android.ct.moyosafiapp.ui.BluetoothActivity;
import com.course.android.ct.moyosafiapp.ui.ProfilActivity;
import com.course.android.ct.moyosafiapp.ui.authentification.LoginFragment;

public class SettingsFragment extends Fragment {

    // VARIABLES
    private ConstraintLayout settings_to_bluetooth;
    private ConstraintLayout profile_bloc;

    FragmentSettingsBinding binding;
    private SessionManager sessionManager;

    // DEFAULT CONSTRUCT
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getInstance(getContext());

        // REDIRECT THE USER WHEN HI IS LOGGED IN
        if (!SessionManager.getInstance(getContext()).isLoggedIn()) {
            System.out.println("+++++++++++++++++++++++ le patient est connecté :"+sessionManager.getUser_name()+"++++++");
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginFragment.class); // we take the main activity
            startActivity(intent); // we start it
//            getActivity().finish(); // Pour empêcher le retour à l'écran précédent avec le bouton "Retour"
            getActivity().finishAffinity();
        }
    }

    // FUNCTIONS
    // 1- first function
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            binding = FragmentSettingsBinding.inflate(getLayoutInflater());
//            View view = inflater.inflate(R.layout.fragment_settings, container, false); // our view (fragment_layout)

            // GET VIEWS
//            settings_to_bluetooth = view.findViewById(R.id.settings_to_bluetooth); // we take the bluetooth_bloc
//            profile_bloc = view.findViewById(R.id.profile_bloc);

            // ACTIONS
            // 1- to settings_to_bluetooth view

                binding.settingsToBluetooth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), BluetoothActivity.class);
                        startActivity(intent);
                    }
                });

            // 2- to profile_bloc view
                binding.profileBloc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ProfilActivity.class);
                        startActivity(intent);
                    }
                });

            // 3- to btn-logout view (deconnexion action)
            binding.logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionManager.getInstance(getContext()).isLogout();
                    System.out.println("+++++++++++++++++++++++ le patient est deconnecté :"+sessionManager.getUser_name()+"++++++");
                    Intent intent = new Intent(getActivity().getApplicationContext(), AuthentificationActivity.class); // we take the Authentification activity
                    startActivity(intent);
                    getActivity().finishAffinity();
                }
            });

            if(sessionManager.isConnected()) {
                binding.bluetoothTextConDis.setText("connecté");
                System.out.println("+++++++++++++++++++ VOUS ETES CONNECTER ++++++++++++++++++");
            } else {
                sessionManager.isDisconnected();
                binding.bluetoothTextConDis.setText("deconnecté");
                System.out.println("+++++++++++++++++++ VOUS ETES DECONNECTER ++++++++++++++++++");
            }

            return binding.getRoot();
        }
}