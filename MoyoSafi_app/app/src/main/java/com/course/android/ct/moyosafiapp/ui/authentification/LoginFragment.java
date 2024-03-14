package com.course.android.ct.moyosafiapp.ui.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.course.android.ct.moyosafiapp.R;
import com.course.android.ct.moyosafiapp.databinding.FragmentLoginBinding;
import com.course.android.ct.moyosafiapp.models.SessionManager;
import com.course.android.ct.moyosafiapp.models.api.LogPatientResponse;
import com.course.android.ct.moyosafiapp.ui.MainActivity;
import com.course.android.ct.moyosafiapp.viewModel.PatientViewModel;
import com.course.android.ct.moyosafiapp.viewModel.injections.ViewModelFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private SessionManager sessionManager;
    FragmentLoginBinding binding; // binding will helps us to get views easily
    PatientViewModel patientViewModel; //

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialise the sessionManager
        sessionManager = SessionManager.getInstance(getContext());

        // REDIRECT THE USER WHEN HI IS LOGGED IN
        if (SessionManager.getInstance(getContext()).isLoggedIn()) {
            System.out.println("+++++++++++++++++++++++ le patient est connecté :"+sessionManager.getUser_name()+"++++++");
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class); // we take the main activity
            startActivity(intent); // we start it
            getActivity().finish(); // Pour empêcher le retour à l'écran précédent avec le bouton "Retour"
        } else {
            System.out.println("+++++++++++++++++++++++ le patient est deconnecté :"+sessionManager.getUser_name()+"++++++");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(getLayoutInflater());

        // CONFIGURATION DEPUIS L'ACTIVITE PARENTE
        // la difference avec la configuration parente c'est au niveau du context
        patientViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PatientViewModel.class);

        // LAUNCH MAIN ACTIVITY WHEN IDENTIFIES ARE CORRECT
        binding.btnLogin.setOnClickListener(v-> logPatient());

        // **************************************** LAUNCH OTHER FRAGMENT WHEN SPECIFICS LINKS ARE CLICKED **************************************************
        // For inscription page
        binding.inscriptionLink.setOnClickListener(new View.OnClickListener() {
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
        binding.passwordForgottenLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckMailAdressFragment checkMailAdressFragment = new CheckMailAdressFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction(); // obtenir le gestionnaire de fragments de l'activité parente
                transaction.replace(R.id.authentification_frame_layout, checkMailAdressFragment);
                transaction.addToBackStack(null);  // Ajoute la transaction à la pile de retour
                transaction.commit();
            }
        });

        return binding.getRoot();
    }


    public void logPatient() {
        String patient_name = binding.patientName.getText().toString();
        String patient_password = binding.patientPassword.getText().toString();

        // ********************************************* LOGIN ONLINE *********************************************
        patientViewModel.logPatient(patient_name, patient_password, new Callback<LogPatientResponse>() {
            @Override
            public void onResponse(Call<LogPatientResponse> call, Response<LogPatientResponse> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getPatient_role();
                    String user_name = response.body().getPatient_name();
                    int patient_id = response.body().getPatient_id();
                    sessionManager.setId_patient(patient_id);

                    System.out.println("++++++++++++++++++++++++++ login okey +++++++++++++++++++++++++");
                    System.out.println("++++++++++++++++++++++++++ Patient id "+patient_id+"+++++++++++++++++++++++++");

                    sessionManager.setUser_name(user_name);
                    sessionManager.setAuthToken(token);
                    System.out.println("++++++++++++++++++++++++++++ User name : "+user_name+", Token : "+sessionManager.getAuthToken()+"+++++++++++++++++++++++++++");
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class); // we take the main activity
                    startActivity(intent); // we start it
                    getActivity().finish(); // Pour empêcher le retour à l'écran précédent avec le bouton "Retour"
                }
            }

            @Override
            public void onFailure(Call<LogPatientResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                if(errorMessage.equals("Nom d'utilisateur ou mot de passe incorrect")) {
                    binding.errorMessage.setVisibility(View.VISIBLE);
                } else if(errorMessage.equals("Connectez-vous à l'internet")) {
                    binding.errorMessage.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                } else if(errorMessage.equals("timeout")) {
                    binding.errorMessage.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getApplicationContext(), "Erreur de connexion, essaye de nouveau", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("++++++++++++++++++++++++++++++++++++"+errorMessage);
                }
            }
        });
    }
}