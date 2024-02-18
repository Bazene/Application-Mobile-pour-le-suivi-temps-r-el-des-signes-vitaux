package com.course.android.ct.moyosafiapp.ui.authentification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.course.android.ct.moyosafiapp.R;


public class CheckMailAdressFragment extends Fragment {
    public CheckMailAdressFragment() {
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
        View view = inflater.inflate(R.layout.fragment_check_mail_adress, container, false);

        // FIND VIEWS
        TextView check_mail_to_login = view.findViewById(R.id.check_mail_to_login);
        TextView btn_get_code = view.findViewById(R.id.btn_get_code);

        // ACTIONS TO VIEWS
        check_mail_to_login.setOnClickListener(v-> requireActivity().getSupportFragmentManager().popBackStack()); // back when pressed

        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeForMailFragment codeForMailFragment = new CodeForMailFragment(); // take fragment

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction(); // obtenir le gestionnaire de fragments de l'activité parente
                transaction.replace(R.id.authentification_frame_layout, codeForMailFragment);
                transaction.addToBackStack(null);  // Ajoute la transaction à la pile de retour
                transaction.commit();
            }
        });

        return view;
    }
}