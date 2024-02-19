package com.course.android.ct.moyosafiapp.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.course.android.ct.moyosafiapp.R;

public class VitalSignDialogFragment extends AppCompatDialogFragment {

    // VARIABLES
    private EditText edit_systol_value;
    private EditText edit_diastol_value;
    private EditText edit_glycemie_vital;
    private TextView btn_submit_vital;

    private VitalSignDialogListener listener; // instance of our interface that help us to share data with the main fragment

    // FUNCTIONS
    // 1- first method
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            System.out.println("+++++++++++++++++++++++++++++++ Le dialog est cree ++++++++++++++++++++++++++++++++");


            View view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_vital_sign_dialog, null);

            System.out.println("+++++++++++++++++++++++++++++++ Le dialog est creeeeeeeeee ++++++++++++++++++++++++++++++++");

            // INITIALISATION
            // 1- views initialisations
            edit_systol_value = view.findViewById(R.id.edit_systol_value);
            edit_diastol_value = view.findViewById(R.id.edit_diastol_value);
            edit_glycemie_vital = view.findViewById(R.id.edit_glycemie_vital);

            // ACTIONS

            builder.setView(view)
                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("++++++++++++++++++++++++++++++++++ Le btn submit est clique +++++++++++++++++++++++++++++++++++++");

                            // we take the insert values
                            String systol_value = edit_systol_value.getText().toString();
                            String diastol_valueText = edit_diastol_value.getText().toString();
                            String glycemie_vital = edit_glycemie_vital.getText().toString();

                            // we send to the main fragment
                            listener.setTextOnMainView(systol_value, diastol_valueText, glycemie_vital);

                        }
                    });

            return builder.create();
        }

    // 2- setListener function
    public void setListener(VitalSignDialogListener listener) {
        this.listener = listener;
    }

    // INTERFACE
    public interface VitalSignDialogListener {
        void setTextOnMainView(String new_systol_value, String new_diastol_value, String new_glycemie_vital);
    }
}