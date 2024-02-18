package com.course.android.ct.moyosafiapp.ui.authentification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.course.android.ct.moyosafiapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CodeForMailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CodeForMailFragment extends Fragment {
    public CodeForMailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_code_for_mail, container, false);

        // FIND VIEWS
        TextView btn_submit_code = view.findViewById(R.id.btn_submit_code);
        TextView email_code_to_email = view.findViewById(R.id.email_code_to_email);

        // ACTIONS TO VIEWS
        // 1 - first action
        email_code_to_email.setOnClickListener(v-> requireActivity().getSupportFragmentManager().popBackStack()); // back when pressed

        // 2 - second action
        btn_submit_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPasswordFragment newPasswordFragment = new NewPasswordFragment(); // take fragment

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction(); // obtenir le gestionnaire de fragments de l'activité parente
                transaction.replace(R.id.authentification_frame_layout, newPasswordFragment);
                transaction.addToBackStack(null);  // Ajoute la transaction à la pile de retour
                transaction.commit();
            }
        });

        return view;
    }
}