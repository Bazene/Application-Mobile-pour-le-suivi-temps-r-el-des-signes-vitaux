package com.course.android.ct.moyosafiapp.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.course.android.ct.moyosafiapp.R;

public class ProfileDialogFragment extends AppCompatDialogFragment {
    // VARIABLES
        AlertDialog.Builder builder;
        View view;
        private ProfileDialogListener listener;
        private EditText new_name;
        private EditText new_phone_number;
        private EditText new_age;
        private EditText new_commune;
        private EditText new_quater;
        private RadioGroup new_radio_group_gender;

        private RadioButton radioButton;

    // DEFAULT CONSTRUCT
        public ProfileDialogFragment() {
            // Required empty public constructor
        }

    // FUNCTIONS
    // 1- onCreate function
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // INITIALISATIONS
                builder = new AlertDialog.Builder(getActivity());
                View view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_profile_dialog, null); // Inflate the layout for this fragment
                // views
                new_name = view.findViewById(R.id.new_name);
                new_phone_number = view.findViewById(R.id.new_phone_number);
                new_age = view.findViewById(R.id.new_age);
                new_commune = view.findViewById(R.id.new_commune);
                new_quater = view.findViewById(R.id.new_quater);
                new_radio_group_gender = view.findViewById(R.id.new_radio_group_gender);

                radioButton = null;

            // ACTIONS
            // 1- to radioGroup
                new_radio_group_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                         radioButton = view.findViewById(checkedId);
                    }
                });

            // 2- to builder
            builder.setView(view)
                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            System.out.println("++++++++++++++++++++++ le btn Enregistrer est clique +++++++++++++++++++");
                            // we take the insert values
                            String name_text = new_name.getText().toString();
                            String phone_number =  new_phone_number.getText().toString();
                            String age_number = new_age.getText().toString();
                            String commune_text = new_commune.getText().toString();
                            String quater_text = new_quater.getText().toString();

                            String radioButton_text = radioButton.getText().toString();


                            // we send to the main fragment
                            listener.setTextOnMainView(name_text, phone_number, age_number,commune_text ,quater_text, radioButton_text);
                        }
                    });

            return builder.create();
        }

    // 2- setListener function
        public void setListener(ProfileDialogListener listener) {
            this.listener = listener;
        }

    // INTERFACE
        public interface ProfileDialogListener {
            void setTextOnMainView(String name_text,String phone_number, String age_number, String commune_text , String quater_text, String radioButton_text);
        }
}

