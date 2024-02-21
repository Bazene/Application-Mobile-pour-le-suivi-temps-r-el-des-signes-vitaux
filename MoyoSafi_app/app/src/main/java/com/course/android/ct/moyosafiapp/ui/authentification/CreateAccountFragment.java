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
import com.course.android.ct.moyosafiapp.models.entity.Patient;
import com.course.android.ct.moyosafiapp.viewModel.PatientViewModel;
import com.course.android.ct.moyosafiapp.viewModel.injections.ViewModelFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountFragment extends Fragment {
    // VARIABLES
    LoginFragment loginFragment = new LoginFragment(); // fragment

    FragmentCreateAccountBinding binding;

    PatientViewModel patientViewModel; //

    // for mail
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

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

    private void addPatient() {
        // get user input
        String user_name = binding.userName.getText().toString();
        String past_name = binding.pastName.getText().toString();
        String sur_name = binding.surName.getText().toString();
        String mail_address = binding.mailAddress.getText().toString();
        int selectedRadioButtonId = binding.radioGroupGender.getCheckedRadioButtonId();
        String age = binding.age.getText().toString();
        String user_password = binding.userPassword.getText().toString();
        String user_passwordConfirm = binding.userPasswordConfirm.getText().toString();

        // check if input are not empty
        if(!user_name.isEmpty()
            && !past_name.isEmpty()
            && !sur_name.isEmpty()
            && !mail_address.isEmpty()
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
                    String date_created = "";
                    LocalDateTime now = null; // date and actual time variable
                    DateTimeFormatter formatterDate = null; // date format variable

                    // role
                    String role ="user";

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        now = LocalDateTime.now(); // get date and actual time
                        formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // format date
                        date_created = now.format(formatterDate); // convert date in string
                    }

                    Patient new_patient = new Patient(user_name, past_name, sur_name, gender, mail_address, user_password, date_created ,age_user, role);

                    // check  if the patient is already in database using his mail address
                    LiveData<List<Patient>> allPatientsInTable = getAllPatients();
                    if(isPatientExitInTable(allPatientsInTable, mail_address)) {
                        if(createPatient(new_patient)) {
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
                        else{
                            binding.errorMessage.setVisibility(View.VISIBLE);
                            binding.errorMessage.setText("La création du compte n'a pas marché, essage de nouveau ");
                            Toast.makeText(requireContext(), "La création du compte n'a pas marché, essage de nouveau", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        binding.errorMessage.setVisibility(View.VISIBLE);
                        binding.errorMessage.setText("Un compte existe déjà avec cette adresse mail : "+mail_address);
                        Toast.makeText(requireContext(), "Un compte existe déjà avec cette adresse mail : "+mail_address, Toast.LENGTH_LONG).show();
                    }
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

    // function that check if the use is already in data base
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

    // function for getting all patient
    private LiveData<List<Patient>> getAllPatients() {
        return this.patientViewModel.getPatientsSortedByDate();
    }

    // function for creating the new patient
    private boolean createPatient(Patient patient) {
        if(this.patientViewModel.insertPatient(patient)) {
            return true;
        };
        return false;
    }

    // function for mail check
    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}