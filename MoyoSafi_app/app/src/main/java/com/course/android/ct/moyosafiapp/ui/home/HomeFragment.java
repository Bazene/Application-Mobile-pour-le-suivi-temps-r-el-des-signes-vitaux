package com.course.android.ct.moyosafiapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.course.android.ct.moyosafiapp.R;
import com.course.android.ct.moyosafiapp.models.SessionManager;
import com.course.android.ct.moyosafiapp.models.entity.VitalSign;
import com.course.android.ct.moyosafiapp.models.entity.VitalSignRealTime;
import com.course.android.ct.moyosafiapp.ui.AuthentificationActivity;
import com.course.android.ct.moyosafiapp.viewModel.PatientViewModel;
import com.course.android.ct.moyosafiapp.viewModel.injections.ViewModelFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomeFragment extends Fragment implements VitalSignDialogFragment.VitalSignDialogListener {
    // VARIABLES
    SessionManager sessionManager;
    private TextView date_in_home_screen;
    private TextView systolic_value;
    private TextView diastolic_value ;
    private TextView glycemie_textView;
    private ImageView modify_imageView;
    private TextView heart_rate_value;
    private TextView hzRealTime;
    private TextView spo2RealTime;
    private TextView temp_value;
    private TextView tempRealTime;
    private TextView spo2_vlaue;
    private Context context;

    private PatientViewModel patientViewModel; //

    // DEFAULT CONSTRUCT
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getInstance(getContext());

        // REDIRECT THE USER WHEN HI IS LOGGED IN
        if (!SessionManager.getInstance(getContext()).isLoggedIn()) {
            System.out.println("+++++++++++++++++++++++ le patient est connecté :"+sessionManager.getUser_name()+"++++++");
            Intent intent = new Intent(getActivity().getApplicationContext(), AuthentificationActivity.class); // we take the Authentification activity
            startActivity(intent); // we start it
        }
    }

    // FUNCTIONS
    // 1- onCreateView function
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // INITIALISATION
            // 1- Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_home, container, false);

            // CONFIGURATION DU VIEW MODEL DEPUIS L'ACTIVITE PARENTE
            // la difference avec la configuration parente c'est au niveau du context
            patientViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PatientViewModel.class);

            // 2-  Views
            date_in_home_screen = view.findViewById(R.id.date_in_home_screen);

            systolic_value = view.findViewById(R.id.systolic_value);
            diastolic_value = view.findViewById(R.id.diastolic_value);
            glycemie_textView = view.findViewById(R.id.glycemie_textView);
            modify_imageView = view.findViewById(R.id.modify_imageView);
            heart_rate_value = view.findViewById(R.id.heart_rate_value);
            spo2_vlaue = view.findViewById(R.id.spo2_vlaue);
            temp_value = view.findViewById(R.id.temp_value);
            hzRealTime = view.findViewById(R.id.hzRealTime);
            spo2RealTime = view.findViewById(R.id.spo2RealTime);
            tempRealTime = view.findViewById(R.id.tempRealTime);


            // ACTIONS
            // 1- to date_in_home_screen view
            LocalDateTime now = null; // date and actual time variable
            DateTimeFormatter formatterDate = null; // date format variable
            String actualDate = "";

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                now = LocalDateTime.now(); // get date and actual time
                formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // format date
                actualDate = now.format(formatterDate); // convert date in string
            }

            date_in_home_screen.setText(actualDate); //display the result

            // 2- to modify_imageView
            modify_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("++++++++++++++++++++++++++ l'icone edit est cliquer ++++++++++++++++++++++++++");

                    openDialog();
                }
            });

            // OBSERVER LE VIEW MODEL FOURNISSANT LES DONNEES POUR LES MISES A JOURS DANS LE FRAGMENT
            patientViewModel.getVitalSignRealTimeForUi().observe(getActivity(), new Observer<VitalSignRealTime>() {
                @Override
                public void onChanged(VitalSignRealTime vitalSignRealTime) {
                    if(vitalSignRealTime != null) {
                        hzRealTime.setText("" + vitalSignRealTime.getHeart_rate() + " BPM");
                        spo2RealTime.setText("" + vitalSignRealTime.getOxygen_level() + " %");
                        tempRealTime.setText("" + vitalSignRealTime.getTemperature() + " °C");
                    }
                }
            });


            int id_patient = sessionManager.getId_patient() ;
            patientViewModel.getLastVitalSignForUi(id_patient).observe(getActivity(), new Observer<VitalSign>() {
                @Override
                public void onChanged(VitalSign vitalSign) {
                    if(vitalSign != null) {
                        heart_rate_value.setText(""+vitalSign.getHeart_rate());
                        spo2_vlaue.setText(""+vitalSign.getOxygen_level());
                        temp_value.setText(""+String.format("%.1f", vitalSign.getTemperature()));
                        systolic_value.setText(""+vitalSign.getSystolic_blood());
                        diastolic_value.setText(""+vitalSign.getDiastolic_blood());
                        glycemie_textView.setText(""+vitalSign.getBlood_glucose());
                    }
                }
            });

            return view;
        }

    // 2- Display dialog function
    private void openDialog() {
        System.out.println("++++++++++++++++++++++++++ le dialogue est appele ++++++++++++++++++++++++++");
        VitalSignDialogFragment dialogFragment = new VitalSignDialogFragment(); // Instantiation of the dialog fragment
        dialogFragment.setListener(HomeFragment.this); // écouteur (listener) pour le DialogFragment
        dialogFragment.show(getChildFragmentManager(), "VitalSignDialogFragment"); // obtenir le gestionnaire de fragments spécifique à ce fragment et affichage du dialog
    }

    // 3- we get the change in our fragment
    @Override
    public void setTextOnMainView(VitalSign vitalSign) {
        patientViewModel.insertOtherVitalSign(vitalSign);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }
}