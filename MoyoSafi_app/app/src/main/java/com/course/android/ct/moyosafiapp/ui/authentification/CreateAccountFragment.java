package com.course.android.ct.moyosafiapp.ui.authentification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.course.android.ct.moyosafiapp.R;
import com.course.android.ct.moyosafiapp.databinding.FragmentCreateAccountBinding;
import com.course.android.ct.moyosafiapp.models.api.CreateAccountResponse;
import com.course.android.ct.moyosafiapp.models.entity.Patient;
import com.course.android.ct.moyosafiapp.viewModel.PatientViewModel;
import com.course.android.ct.moyosafiapp.viewModel.injections.ViewModelFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountFragment extends Fragment {
    // VARIABLES
    LoginFragment loginFragment = new LoginFragment(); // fragment
    FragmentCreateAccountBinding binding;
    PatientViewModel patientViewModel;

    // for mail
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    // DEFAULT CONSTRUCT
    public CreateAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccountBinding.inflate(getLayoutInflater());

        // CONFIGURATION DEPUIS L'ACTIVITE PARENTE
        // la difference avec la configuration parente c'est au niveau du context
        patientViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PatientViewModel.class);

        // ACTIONS TO VIEWS
        // 1- first action
        binding.createAccountToLogin.setOnClickListener(v-> requireActivity().getSupportFragmentManager().popBackStack()); // back when pressed

        // 2- second action
        binding.btnCreateCount.setOnClickListener(v-> addPatient());

        return  binding.getRoot();
    }

    // FUNCTION THAT ADD PATIENT IN DATABASE
    private void addPatient() {
        // get user input
        String user_name = binding.userName.getText().toString();
        String past_name = binding.pastName.getText().toString();
        String sur_name = binding.surName.getText().toString();
        String mail_address = binding.mailAddress.getText().toString();
        String phone_number = binding.phoneNumber.getText().toString();
        int selectedRadioButtonId = binding.radioGroupGender.getCheckedRadioButtonId();
        String age = binding.age.getText().toString();
        String user_password = binding.userPassword.getText().toString();
        String user_passwordConfirm = binding.userPasswordConfirm.getText().toString();

        // check if input are not empty
        if(!user_name.isEmpty()
            && !past_name.isEmpty()
            && !sur_name.isEmpty()
            && !mail_address.isEmpty()
            && !phone_number.isEmpty()
            && selectedRadioButtonId != -1
            && !age.isEmpty()
            && !user_password.isEmpty()
            && !user_passwordConfirm.isEmpty()) {

            // check if password enter are the same
            if(user_passwordConfirm.equals(user_password)) {

                // check if email is valid
                if(isValidEmail(mail_address)) {
                    // obtain the user gender
                    RadioButton radioButton = binding.radioGroupGender.findViewById(selectedRadioButtonId); // we get the radio button checked
                    String gender = radioButton.getText().toString(); // we get the gender of user

                    // convert age in int value
                    int age_user = Integer.parseInt(age);

                    // obtain actual date
                    LocalDateTime now = null; // date and actual time variable
                    DateTimeFormatter formatterDateTime = null; // dateTime format variable
                    String date_created = "";

                    // role
                    String role = null;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        now = LocalDateTime.now(); // get date and actual time
                        formatterDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); // format date
                        date_created = now.format(formatterDateTime); // convert date in string
                    }

                    Patient new_patient = new Patient(user_name, past_name, sur_name, gender, mail_address, phone_number, user_password, date_created ,age_user, role);

                    // check  if the patient is already in database using his mail address
                    LiveData<List<Patient>> allPatientsInTable = getAllPatients();
//                    if(isPatientExitInTable(allPatientsInTable, mail_address)) {

                        //*********************************************************************************************************
                        // ********************************************** for remote **********************************************
                        patientViewModel.createPatient(new_patient, new Callback<CreateAccountResponse>() {
                            @Override
                            public void onResponse(Call<CreateAccountResponse> call, Response<CreateAccountResponse> response) {
                                // Gérer la réponse
                                if (response.isSuccessful()) {
                                    // Inscription réussie
                                    System.out.println("++++++++++++++++++++++++++ l'inscription okey +++++++++++++++++++++++++");
                                    System.out.println("++++++++++++++++++++++++++ "+response+"  +++++++++++++++++++++++++");
                                    // success message
                                    Toast.makeText(requireContext(), "La création du compte a reussi avec succès", Toast.LENGTH_LONG).show();

                                    // redirection to login fragment
                                    LoginFragment loginFragment = new LoginFragment(); // get login fragment
                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.replace(R.id.authentification_frame_layout, loginFragment);
                                    transaction.addToBackStack(null);  // Ajoute la transaction à la pile de retour
                                    transaction.commit();
                                }
                            }

                            @Override
                            public void onFailure(Call<CreateAccountResponse> call, Throwable t) {
                                // Gérer l'erreur
                                String errorMessage = t.getMessage();
                                if(errorMessage.equals("Patient Allready exist using the same mail address")) {
                                    binding.errorMessage.setVisibility(View.VISIBLE);
                                    binding.errorMessage.setText("Un patient utilisant le même addresse mail exist déjà");
                                    Toast.makeText(requireContext(), "Un patient utilisant le même addresse mail exist déjà", Toast.LENGTH_LONG).show();
                                }

                                else if(errorMessage.equals("Faild to create an account")) {
                                    binding.errorMessage.setVisibility(View.VISIBLE);
                                    binding.errorMessage.setText("Echec de création du compte, essayer encore");
                                    Toast.makeText(requireContext(), "Echec de création du compte, essayer encore", Toast.LENGTH_LONG).show();

                                } else if (errorMessage.equals("Connectez-vous à l'internet")) {
                                    binding.errorMessage.setVisibility(View.VISIBLE);
                                    binding.errorMessage.setText(errorMessage);
                                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                }

                else {
                    binding.errorMessage.setVisibility(View.VISIBLE);
                    binding.errorMessage.setText("l'addresse mail n'est pas valide");
                    Toast.makeText(requireContext(), "l'addresse mail n'est pas valide", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                binding.errorMessage.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "les mots de passe ne sont pas identique", Toast.LENGTH_SHORT).show();
            }

        } else {
            binding.errorMessage.setVisibility(View.VISIBLE);
            binding.errorMessage.setText("Veillez remplir tout les champs");
            Toast.makeText(requireContext(), "Veillez remplir tout les champs", Toast.LENGTH_SHORT).show();
        }
    }

    // FUNCTION THAT CHECK IF THE USER IS ALREADY IN DATA BASE OR NOT
    private boolean isPatientExitInTable(LiveData<List<Patient>> allPatientsInTable, String mail_address){
        AtomicBoolean return_value = new AtomicBoolean(false);

        allPatientsInTable.observe(getActivity(), patients -> {
            // Vérifiez si l'adresse e-mail existe déjà dans la liste des patients
            boolean emailExists = patients.stream().anyMatch(patient -> patient.getPatient_mail().equals(mail_address));

            if (emailExists) {
                // Adresse e-mail déjà utilisée, ne pas autoriser la création du compte
                if (patients.isEmpty()) {
                    return_value.set(false);
                }
                return_value.set(true);
            } else {
                // Adresse e-mail non utilisée, autoriser la création du compte
                return_value.set(false);

            }
        });

        return return_value.get();
    }

    // FUNCTION FOR GETTING ALL PATIENT
    private LiveData<List<Patient>> getAllPatients() {
        return this.patientViewModel.getAllPatients();
    }

    // FUNCTION FOR MAIL CHECK
    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}