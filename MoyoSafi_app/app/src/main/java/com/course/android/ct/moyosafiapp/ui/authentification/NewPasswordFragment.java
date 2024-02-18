package com.course.android.ct.moyosafiapp.ui.authentification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.course.android.ct.moyosafiapp.R;

public class NewPasswordFragment extends Fragment {
    public NewPasswordFragment() {
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
        View view = inflater.inflate(R.layout.fragment_new_password, container, false);

        // FIND VIEWS
        TextView new_password_to_email_code = view.findViewById(R.id.new_password_to_email_code);
        TextView change_password = view.findViewById(R.id.change_password);


        // ACTIONS TO VIEWS
        // 1- first action
        new_password_to_email_code.setOnClickListener(v-> requireActivity().getSupportFragmentManager().popBackStack());

        // 2- second action
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); // Obtenez le gestionnaire de fragments de l'activité parente

                FragmentTransaction transaction = fragmentManager.beginTransaction(); // obtenir le gestionnaire de fragments de l'activité parente
                transaction.replace(R.id.authentification_frame_layout, loginFragment);
                transaction.addToBackStack(null);  // Ajoute la transaction à la pile de retour
                transaction.commit();
//
//                runOnUiThread(new Runnable()) {
//
//                }
            }
        });

        return view;
    }
}