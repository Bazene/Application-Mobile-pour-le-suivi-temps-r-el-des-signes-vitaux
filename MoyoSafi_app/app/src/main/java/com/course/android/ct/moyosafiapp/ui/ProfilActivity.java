package com.course.android.ct.moyosafiapp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.course.android.ct.moyosafiapp.databinding.ActivityProfilBinding;
import com.course.android.ct.moyosafiapp.ui.settings.ProfileDialogFragment;
import com.course.android.ct.moyosafiapp.viewModel.PatientViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ProfilActivity extends AppCompatActivity implements ProfileDialogFragment.ProfileDialogListener {

    //VARIABLES
    ActivityProfilBinding binding; // link file between

    private PatientViewModel patientViewModel; // initialisation of our ViewModel class

    private TextView profile_to_settings;

    private FloatingActionButton floating_action_button;
    private ImageView imageView_profile;

    private ImageView edit_profile;
    private TextView user_name;
    private TextView user_gender;
    private TextView user_phone_number;
    private TextView user_age;
    private TextView user_commune;
    private TextView user_quater;

    // FUNCTIONS
    // 1- onCreate function
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // binding will helps us to find all views
            binding = ActivityProfilBinding.inflate(getLayoutInflater()); // initialisation of binding file
            setContentView(binding.getRoot());

            System.out.println("+++++++++++++++++++++ ça marche +++++++++++++++++++++=");

//            // VIEW MODEL
//            // configuration of our view
//            configureViewModel();

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
        }

    // 2- onActivityResult
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent date) {

            super.onActivityResult(requestCode, resultCode, date);
            Uri uri = date.getData();

            binding.imageViewProfile.setImageURI(uri);
            System.out.println("++++++++++++++++++++++++++++"+uri+"+++++++++++++++++++++++++++++++++++++++");

            // stocker l'image sous le format string
            binding.imageViewProfile.buildDrawingCache();
            Bitmap imageBitmap = ((BitmapDrawable) binding.imageViewProfile.getDrawable()).getBitmap();
            String imageString = BitmapToString(imageBitmap);

            // récupérer l'image dans la base de données
//            Bitmap imageBitmap1 = StringToBitmap(imageString);
//            imageView_profile.setImageBitmap(imageBitmap1);
        }

    // 3- openDialog function
        public void openDialog(){
            ProfileDialogFragment dialogFragment = new ProfileDialogFragment(); // Instantiation of the dialog fragment
            dialogFragment.setListener(ProfilActivity.this); // écouteur (listener) pour le DialogFragment
            dialogFragment.show(getSupportFragmentManager(), "ProfileDialogFragment"); // obtenir le gestionnaire de fragments spécifique à ce fragment et affichage du dialog
        }

    // 4- setTextOnMainView
        @Override
        public void setTextOnMainView(String name_text, String phone_number, String age_number, String commune_text , String quater_text, String radioButton_text) {
            binding.userName.setText(name_text);
            binding.userGender.setText(radioButton_text);
            binding.userPhoneNumber.setText(phone_number);
            binding.userAge.setText((age_number));
            binding.userCommune.setText(commune_text);
            binding.userQuater.setText(quater_text);
        }

    // 5- bitmap function (wich help us to convert image to string)
        public  String BitmapToString(Bitmap imageBitmap) {

            try {

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                String temp = Base64.getEncoder().encodeToString(bytes);
                return temp;
            }catch (Exception e) {
                return null;
            }
        }

    // 6- string function (wich help us to convert string to image)
        public Bitmap StringToBitmap(String imageString) {
                try{
                    byte[] imageBytes = Base64.getDecoder().decode(imageString);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    return decodedImage;
                } catch (Exception e) {
                    return null;
                }
        }

    // ALL FOR VIEW MODEL
//    // 1- configuration of our view model
//    private void configureViewModel() {
//            this.patientViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(PatientViewModel.class) ;
//            this.patientViewModel.init(1);
//    }

    // 3 - Create a new Patient
//    private void insertPatient() {
//
//        patientViewModel.insertPatient("BAZENE","SERGE", "Amos", "Mail", "bazenesergeamos0@gmail.com","" )
//
//        binding.todoListActivityEditText.setText("");
//
//    }
//
//    // 5 - Update view (picture)
//    private void updateView(User user) {
//
//        if (user == null) return;
//
//        binding.todoListActivityHeaderProfileText.setText(user.getUsername());
//
//        Glide.with(this).load(user.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(binding.todoListActivityHeaderProfileImage);
//
//    }
}