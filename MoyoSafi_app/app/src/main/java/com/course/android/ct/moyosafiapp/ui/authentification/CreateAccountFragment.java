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

public class CreateAccountFragment extends Fragment {

    //  FRAGMENT
    LoginFragment loginFragment = new LoginFragment();

    public CreateAccountFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        // FIND VIEWS
        TextView create_account_to_login = view.findViewById(R.id.create_account_to_login);
        TextView btn_create_count = view.findViewById(R.id.btn_create_count);

        // ACTIONS TO VIEWS
        // 1- first action
        create_account_to_login.setOnClickListener(v-> requireActivity().getSupportFragmentManager().popBackStack()); // back when pressed

        // 2- second action
        btn_create_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment(); // get login fragment

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.authentification_frame_layout, loginFragment);
                transaction.addToBackStack(null);  // Ajoute la transaction à la pile de retour
                transaction.commit();
            }
        });

        return view;
    }
}