package com.course.android.ct.moyosafiapp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.course.android.ct.moyosafiapp.databinding.ActivityProfilBinding;
import com.course.android.ct.moyosafiapp.models.SessionManager;
import com.course.android.ct.moyosafiapp.models.api.UpdatePatientPictureResponse;
import com.course.android.ct.moyosafiapp.models.api.UpdatePatientResponse;
import com.course.android.ct.moyosafiapp.models.entity.Patient;
import com.course.android.ct.moyosafiapp.ui.settings.ProfileDialogFragment;
import com.course.android.ct.moyosafiapp.viewModel.PatientViewModel;
import com.course.android.ct.moyosafiapp.viewModel.injections.ViewModelFactory;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilActivity extends AppCompatActivity implements ProfileDialogFragment.ProfileDialogListener {

    //VARIABLES
    ActivityProfilBinding binding; // link file between

    private PatientViewModel patientViewModel; // initialisation of our ViewModel class
    private SessionManager sessionManager;

    // FUNCTIONS
    // 1- onCreate function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // binding will helps us to find all views
            binding = ActivityProfilBinding.inflate(getLayoutInflater()); // initialisation of binding file
            setContentView(binding.getRoot());

            sessionManager = SessionManager.getInstance(getApplicationContext()); // initialise the session
            patientViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getApplicationContext())).get(PatientViewModel.class); //initialise the ViewModel


            // ACTIONS
            // 1- to profile_to_settings view
            binding.profileToSettings.setOnClickListener(v-> onBackPressed()); // back when pressed

            // 2- to imageView_profile View
            binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImagePicker.with(ProfilActivity.this)
                                .crop() // crop image(optional), check customization for mor information
                                .compress(1024) // Final image size will be less than 1MB (optional)
                                .maxResultSize(1080, 1080) // Final image resolution will be less than 1080 x 1080 (Optional)
                                .start();
                    }
                });

            // 3- to edit_profile view
            binding.editProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog();
                }
            });

            // DISPLAYING DATA IN PATIENT PROFILE ACTIVITY
            String token = sessionManager.getAuthToken();
            patientViewModel.getPatient(token).observe(this, new Observer<Patient>() {
                @Override
                public void onChanged(Patient patient) {
                    if(patient != null) {
                        String commune = (patient.getPatient_commune() == null) ? "No disponible" : patient.getPatient_commune();
                        String quarter = (patient.getPatient_quater() == null) ? "No disponible" : patient.getPatient_quater();

                        binding.userName.setText(patient.getPatient_name() + " " + patient.getPatient_postname());
                        binding.userGender.setText(patient.getPatient_gender());
                        binding.userPhoneNumber.setText(patient.getPatient_phone_number());
                        binding.userMail.setText(patient.getPatient_mail());
                        binding.userAge.setText(""+patient.getPatient_age());
                        binding.userWeight.setText(""+patient.getPatient_weight());
                        binding.userSize.setText(""+patient.getPatient_size());
                        binding.userCommune.setText(commune);
                        binding.userQuater.setText(quarter);

                        // Récupérer les données binaires de l'image depuis la base de données
                        if(patient.getPatient_picture() != null) {
                            byte[] imageBytes = patient.getPatient_picture();
                            Bitmap imageBitmap1 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            binding.imageViewProfile.setImageBitmap(imageBitmap1);
                        }
                    }
                }
            });

        }

    // 2- openDialog function
    public void openDialog(){
        ProfileDialogFragment dialogFragment = new ProfileDialogFragment(); // Instantiation of the dialog fragment
        dialogFragment.setListener(ProfilActivity.this); // écouteur (listener) pour le DialogFragment
        dialogFragment.show(getSupportFragmentManager(), "ProfileDialogFragment"); // obtenir le gestionnaire de fragments spécifique à ce fragment et affichage du dialog
    }

    // FUNCTION THAT SAVE DATA ONLINE AND ON LOCAL
    @Override
    public void setTextOnMainView(String name_text, String phone_number, String age_number, String weight_number, String size_number, String commune_text , String quater_text, String radioButton_text) {
        try {
            if(name_text != null && phone_number != null && age_number != null && weight_number != null && size_number != null && commune_text != null && quater_text != null && radioButton_text != null) {
                String token = sessionManager.getAuthToken();

                Map<String, String> newPatientProfile = new HashMap<>();
                newPatientProfile.put("patient_name", name_text);
                newPatientProfile.put("patient_phone_number", phone_number);
                newPatientProfile.put("patient_age", age_number);
                newPatientProfile.put("patient_weight", weight_number);
                newPatientProfile.put("patient_size", size_number);
                newPatientProfile.put("patient_commune", commune_text);
                newPatientProfile.put("patient_quater", quater_text);
                newPatientProfile.put("patient_gender", radioButton_text);
                newPatientProfile.put("patient_role", token);

                patientViewModel.updatePatientProfileApi(newPatientProfile, new Callback<UpdatePatientResponse>() {
                    @Override
                    public void onResponse(Call<UpdatePatientResponse> call, Response<UpdatePatientResponse> response) {
                        if (response.isSuccessful()) {
                            // we save also data in local database
//                            Toast.makeText(getApplicationContext(), "Modifications effectuées", Toast.LENGTH_SHORT).show();
                            System.out.println("+++++++++++++++++++++++++++++ Les modifications ont réussi +++++++++++++++++++++++++");
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdatePatientResponse> call, Throwable t) {
                        String errorMessage = t.getMessage();
                       if(errorMessage.equals("Connectez-vous à l'internet")) {
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                       if(errorMessage.equals("Echec du mise à jour")) {
                           openDialog();
                           Toast.makeText(getApplicationContext(), "Echec du mise à jour, veillez essayer de nouveau", Toast.LENGTH_LONG).show();
                       }
                    }
                });
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Veillez remplire tout les champs", Toast.LENGTH_LONG).show();
        }
    }

    // IMAGE
    // 4- onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent date) {

        super.onActivityResult(requestCode, resultCode, date);
        Uri uri = date.getData(); // Récupérer l'URI de l'image

        // Récupérer le bitmap à partir de l'URI
        binding.imageViewProfile.setImageURI(uri);
        binding.imageViewProfile.buildDrawingCache();
        Bitmap imageBitmap = ((BitmapDrawable) binding.imageViewProfile.getDrawable()).getBitmap();

        String token = sessionManager.getAuthToken();

        // Convertir le BitMapTo en tableau de bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        // Stocker le tableau de bytes dans la base
//        patientViewModel.updatePatientPicture(token, imageBytes); // dans la base de données local

        Map<String, RequestBody> pictureRequestBodyMap = new HashMap<>();

        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token); // create a query that contain the token
        pictureRequestBodyMap.put("token", tokenBody); // add token in the Map

        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageBytes); // create a query that will contain the image
        pictureRequestBodyMap.put("image\"; filename=\"image.png\"", imageBody); // add image in the Map

//        Map<String, byte[]> picturePatientMap = new HashMap<>();
//        picturePatientMap.put("new_picture", imageBytes);

        patientViewModel.updatePatientPictureApi(pictureRequestBodyMap, new Callback<UpdatePatientPictureResponse>() {
            @Override
            public void onResponse(Call<UpdatePatientPictureResponse> call, Response<UpdatePatientPictureResponse> response) {
                if (response.isSuccessful()) {
                    // we save also data in local database
//                            Toast.makeText(getApplicationContext(), "Modifications effectuées", Toast.LENGTH_SHORT).show();
                    System.out.println("+++++++++++++++++++++++++++++ La modification de la photo a réussi +++++++++++++++++++++++++");
                }
            }

            @Override
            public void onFailure(Call<UpdatePatientPictureResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                if(errorMessage.equals("Connectez-vous à l'internet")) {
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
                if(errorMessage.equals("Echec du mise à jour")) {
                    openDialog();
                    Toast.makeText(getApplicationContext(), "Echec du mise à jour, veillez essayer de nouveau", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("+++++++++++++++++ errror :"+t);
                }
            }
        });


    }
}