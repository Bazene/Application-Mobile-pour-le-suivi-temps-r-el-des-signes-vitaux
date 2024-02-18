package com.course.android.ct.moyosafiapp.ui.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.course.android.ct.moyosafiapp.MainActivity;
import com.course.android.ct.moyosafiapp.R;

public class LoginFragment extends Fragment {
    // FIND VIEWS
    TextView btn_login;
    TextView inscription_link;
    TextView password_forgotten_link;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // FIND VIEWS
        btn_login = view.findViewById(R.id.btn_login);
        inscription_link = view.findViewById(R.id.inscription_link);
        password_forgotten_link = view.findViewById(R.id.password_forgotten_link);

        // LAUNCH MAIN ACTIVITY WHEN IDENTIFIES ARE CORRECT
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class); // we take the main activity

                startActivity(intent); // we start it
            }
        });

        // **************************************** LAUNCH OTHER FRAGMENT WHEN SPECIFICS LINKS ARE CLICKED **************************************************
        // For inscription page
        inscription_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccountFragment createAccountFragment = new CreateAccountFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); // Obtenez le gestionnaire de fragments de l'activité parente
                FragmentTransaction transaction = fragmentManager.beginTransaction(); // obtenir le gestionnaire de fragments de l'activité parente
                transaction.replace(R.id.authentification_frame_layout, createAccountFragment);
                transaction.addToBackStack(null);  // Ajoute la transaction à la pile de retour
                transaction.commit();
            }
        });

        // For mail page
        password_forgotten_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckMailAdressFragment checkMailAdressFragment = new CheckMailAdressFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction(); // obtenir le gestionnaire de fragments de l'activité parente
                transaction.replace(R.id.authentification_frame_layout, checkMailAdressFragment);
                transaction.addToBackStack(null);  // Ajoute la transaction à la pile de retour
                transaction.commit();
            }
        });

        return view;
    }
}