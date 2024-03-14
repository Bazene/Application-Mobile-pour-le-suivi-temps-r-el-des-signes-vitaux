package com.course.android.ct.moyosafiapp.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.course.android.ct.moyosafiapp.R;
import com.course.android.ct.moyosafiapp.models.SessionManager;
import com.course.android.ct.moyosafiapp.models.entity.VitalSign;
import com.course.android.ct.moyosafiapp.viewModel.PatientViewModel;
import com.course.android.ct.moyosafiapp.viewModel.injections.ViewModelFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VitalSignDialogFragment extends AppCompatDialogFragment {

    // VARIABLES
    private EditText edit_systol_value;
    private EditText edit_diastol_value;
    private EditText edit_glycemie_vital;
    private TextView btn_submit_vital;
    private Boolean nonVitalSignInRealtime ;
    private SessionManager sessionManager;

    private PatientViewModel patientViewModel;

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

            patientViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getContext())).get(PatientViewModel.class); //initialise the ViewModel
            sessionManager = SessionManager.getInstance(getContext()); // initialise the session

            // AUTOCOMPLETE EDITTEXT
            int id_patient = sessionManager.getId_patient() ;
            final VitalSign[] lastVitalSign = new VitalSign[1];

            patientViewModel.getLastVitalSignForUi(id_patient).observe(getActivity(), new Observer<VitalSign>() {
                @Override
                public void onChanged(VitalSign vitalSign) {
                    if(vitalSign != null) {
                        edit_systol_value.setText(""+vitalSign.getSystolic_blood());
                        edit_diastol_value.setText(""+vitalSign.getDiastolic_blood());
                        edit_glycemie_vital.setText(""+vitalSign.getBlood_glucose());
                        lastVitalSign[0] = vitalSign;
                    }
//                    else {
//                        nonVitalSignInRealtime = false;
//                        edit_systol_value.setFocusable(false);
//                        edit_systol_value.setClickable(false);
//
//                        edit_diastol_value.setFocusable(false);
//                        edit_diastol_value.setClickable(false);
//
//                        edit_glycemie_vital.setFocusable(false);
//                        edit_glycemie_vital.setClickable(false);
//                    }
                }
            });

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

                            if(lastVitalSign[0] != null) {
                                try {
                                    int newSystolValue = Integer.parseInt(systol_value);
                                    int newDiastolValue = Integer.parseInt(diastol_valueText);
                                    int newGlycemieValue = Integer.parseInt(glycemie_vital);
                                    int spo2 = lastVitalSign[0].getOxygen_level();
                                    float temp = lastVitalSign[0].getTemperature();
                                    int hz = lastVitalSign[0].getHeart_rate();

                                    // Get actual date and hour
                                    LocalDateTime now = LocalDateTime.now();
                                    DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formater la date
                                    DateTimeFormatter formatterHeure = DateTimeFormatter.ofPattern("HH:mm:ss"); // Formater l'heur
                                    String actuelDate = now.format(formatterDate);
                                    String actuelHour = now.format(formatterHeure);

                                    VitalSign vitalSign1 = new VitalSign(id_patient,temp,hz, spo2,newGlycemieValue, newSystolValue, newDiastolValue, actuelHour, actuelDate);

                                    // we send to the main fragment
                                    listener.setTextOnMainView(vitalSign1);
                                }  catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Veillez remplire tout les champs", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Aucune donnée enregistrer, connectez votre braclet avant d'entrer vos valeurs", Toast.LENGTH_LONG).show();
                            }
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
        void setTextOnMainView(VitalSign vitalSign);
    }
}