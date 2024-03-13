package com.course.android.ct.moyosafiapp.models.Repository;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Base64;

import androidx.lifecycle.LiveData;

import com.course.android.ct.moyosafiapp.models.api.CreateAccountResponse;
import com.course.android.ct.moyosafiapp.models.api.LogPatientResponse;
import com.course.android.ct.moyosafiapp.models.api.PatientService;
import com.course.android.ct.moyosafiapp.models.api.RetrofitClientInstance;
import com.course.android.ct.moyosafiapp.models.api.UpdatePatientPictureResponse;
import com.course.android.ct.moyosafiapp.models.api.UpdatePatientResponse;
import com.course.android.ct.moyosafiapp.models.dao.PatientDao;
import com.course.android.ct.moyosafiapp.models.entity.Patient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PatientRepository {

    // VARIABLES
    private PatientDao patientDao;
    private Executor executor;

    //*********************************************************************************************************
    //********************************************** for remote ***********************************************
    PatientService patientService;
    Retrofit retrofit;

    // CONSTRUCT
    public PatientRepository(PatientDao patientDao){
        //*********************************************** for local **********************************************
        this.patientDao = patientDao;

        //*********************************************************************************************************
        // ********************************************** for remote **********************************************
        this.retrofit = RetrofitClientInstance.getInstance(); // we get the instance of retrofit
        patientService = retrofit.create(PatientService.class); // we create an instance of our service
//        // 1- we get the database instance
//        MoyoSafiDatabase moyoSafiDatabase = MoyoSafiDatabase.getInstance(application);
//
//        // 2- we get DAO from the our Room dataBase
//        patientDao = moyoSafiDatabase.patientDao(); // patientDao() is the abstract method of our Room database
//
//        // 3- we get the LiveData from DAO interface
//        allPatients = patientDao.getPatientsSortedByDate();
    }

    // FUNCTIONS

    //*********************************************************************************************************
    // ********************************************** for remote **********************************************
    public void createPatient(Patient patient, final Callback<CreateAccountResponse> callback) {
        Call<CreateAccountResponse> call = patientService.createPatient(patient);
        call.enqueue(new Callback<CreateAccountResponse>() {
            @Override
            public void onResponse(Call<CreateAccountResponse> call, Response<CreateAccountResponse> response) {
                System.out.println("+++++++++++++++++ on entre dans onResponse ++++++++++++++++++");
                if (response.isSuccessful()) {
                    boolean success = response.body().getSuccess() ;
                    String error = response.body().getError();
                    String patient_name = response.body().getPatient_name();
                    String patient_postname = response.body().getPatient_postname();
                    String patient_surname = response.body().getPatient_surname();
                    String patient_gender = response.body().getPatient_gender();
                    String patient_mail = response.body().getPatient_mail();
                    String patient_phone_number = response.body().getPatient_phone_number();
                    String patient_password = response.body().getPatient_password();
                    String patient_date_created = response.body().getPatient_date_created();
                    int patient_age = response.body().getPatient_age();
                    int patient_id = response.body().getPatient_id();
                    String patient_role = response.body().getPatient_role();

//                    int age =  patient_age != null ? Integer.parseInt(patient_age) : 0;

                    Patient patientResponse = new Patient(patient_name, patient_postname, patient_surname, patient_gender, patient_mail, patient_phone_number, patient_password, patient_date_created, patient_age, patient_role);
                    patientResponse.setId(patient_id); // add the remote id to the patient object

                    if(success) {
                        insertPatient(patientResponse);
                        // on fait appel au callback onResponse du ViewModel qui est le même que celui du fragment chargé par la création du compte, et on lui passe en paramètre le resultat
                        callback.onResponse(call, response);
                    }
                    else {
                        callback.onFailure(call, new Throwable(error));
                    }

                } else {

                    try {
                        // La requête a échoué, tu essayes d'extraire le message d'erreur du JSON depuis le serveur
                        String errorResponse = response.errorBody().string();

                        // Vérifier si le contenu est en JSON
                        if (errorResponse.startsWith("{")) {
                            JSONObject jsonObject = new JSONObject(errorResponse);
                            boolean success = jsonObject.getBoolean("success");
                            String errorMessage = jsonObject.getString("error");

                            // on fait appel au callback onFaire du ViewModel qui est le même que celui du fragment chargé par la création du compte, et on lui passe en paramètre le resultat
                            callback.onFailure(call, new Throwable("Échec de l'inscription. Code : " + response.code() + " " + errorMessage));
                        } else {
                            callback.onFailure(call, new Throwable("Échec de l'inscription. Code : " + response.code() + " " + errorResponse));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateAccountResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                if(errorMessage.equals("Failed to connect to /192.168.43.85:80")) callback.onFailure(call, new Throwable("Connectez-vous à l'internet"));
                else callback.onFailure(call, t);
            }
        });
    }

    public void logPatient(String patient_name, String patient_password, final Callback<LogPatientResponse> callback) {
        Map<String, String> patientData = new HashMap<>();
        patientData.put("patient_name", patient_name);
        patientData.put("patient_password", patient_password);

        Call<LogPatientResponse> call = patientService.logPatient(patientData);
        call.enqueue(new Callback<LogPatientResponse>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<LogPatientResponse> call, Response<LogPatientResponse> response) {
                if (response.isSuccessful()) {
                    boolean success = response.body().getSuccess();
                    String error = response.body().getError();

                    System.out.println("++++++++++++++++++++++++++++ glo : "+success+"++++++++++++++++++++++++++++");

                    if(success) {
                        // create patient
                        int patient_id = response.body().getPatient_id();

                        String patient_name = response.body().getPatient_name();
                        String patient_postname = response.body().getPatient_postname();
                        String patient_surname = response.body().getPatient_surname();
                        String patient_gender = response.body().getPatient_gender();
                        String patient_mail = response.body().getPatient_mail();
                        String patient_phone_number = response.body().getPatient_phone_number();
                        String patient_password = response.body().getPatient_password();
                        String patient_date_created = response.body().getPatient_date_created();
                        String patient_age = response.body().getPatient_age(); int age = Integer.parseInt(patient_age);
                        String patient_role = response.body().getPatient_role();
                        String id_doctor = response.body().getId_doctor(); int idDoctor =  id_doctor != null ? Integer.parseInt(id_doctor) : 0;
                        String id_doctor_archived = response.body().getId_doctor_archived(); int idDoctorArchived =  id_doctor != null ? Integer.parseInt(id_doctor_archived) : 0;
                        String patient_commune = response.body().getPatient_commune();
                        String patient_quater = response.body().getPatient_quater();
                        String patient_size = response.body().getPatient_size();  long patientSize =  patient_size != null ? Long.parseLong(patient_size) : 0;
                        String patient_weight = response.body().getPatient_weight(); long patientWeight =  patient_weight != null ? Long.parseLong(patient_weight) : 0;

                        // For picture
                        byte[] patient_picture = null;
                        String patientPictureBase64 = response.body().getPatient_picture();
                        if(patientPictureBase64 != null) patient_picture = Base64.decode(patientPictureBase64, Base64.DEFAULT);

                        // create patient 2 for insert in local
                        Patient patientResponse = new Patient(patient_name, patient_postname, patient_surname, patient_gender, patient_mail, patient_phone_number, patient_password, patient_date_created, age, patient_role);
                        patientResponse.setId_doctor(idDoctor); patientResponse.setId_doctor_archived(idDoctorArchived);
                        patientResponse.setPatient_picture(patient_picture);
                        patientResponse.setPatient_commune(patient_commune); patientResponse.setPatient_quater(patient_quater); patientResponse.setPatient_size(patientSize);
                        patientResponse.setPatient_weight(patientWeight);
                        patientResponse.setId(patient_id);

                        System.out.println("++++++++++++++++++++++++++++ patient_id : "+patient_id+" +++++++++++++++++++++++++++");
                        System.out.println("++++++++++++++++++++++++++++ patient_id in R : "+patientResponse.getId()+" +++++++++++++++++++++++++++");

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                // get all patients in local database
                                List<Patient> patients = patientDao.getAllPatientsForRepository();

                                if (patients == null || patients.isEmpty()) {
                                    // the data is empty, we insert patient au lieu de l'update car dans ce sens aucun patient n'est présent dans la base local
                                    insertPatient(patientResponse);
//                                    patientDao.updatePatientPicture(patient_role, patient_picture);
                                    System.out.println("+++++++++++++++++++++++++++++ on insert les choses dans la database +++++++++++++++++++++++++++++");
                                    // on fait appel au callback onResponse du ViewModel qui est le même que celui du fragment chargé par la création du compte, et on lui passe en paramètre le resultat
                                    callback.onResponse(call, response);
                                } else {
                                    System.out.println("++++++++++++++++++++++++++++ yes he is not empty"+patient_role+" ++++++++++++++++++++++++++++");
                                    updatePatientToken(patient_role, patient_name, patient_password);

                                    // on fait appel au callback onResponse du ViewModel qui est le même que celui du fragment chargé par la création du compte, et on lui passe en paramètre le resultat
                                    callback.onResponse(call, response);
                                }
                                return null;
                            }
                        }.execute();
                    }

                    else {
                        callback.onFailure(call, new Throwable(error));
                        System.out.println("++++++++++++++++++++++++++++ yes : "+error+"++++++++++++++++++++++++++++");
                    }
                }
            }

            @Override
            public void onFailure(Call<LogPatientResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                if(errorMessage.equals("Failed to connect to /192.168.43.85:80")) callback.onFailure(call, new Throwable("Connectez-vous à l'internet"));
                else callback.onFailure(call, t);
            }
        });
    }

    public void updatePatientProfileApi(Map<String, String>  newPatientProfile, final Callback<UpdatePatientResponse> callback) {
        Call<UpdatePatientResponse> call = patientService.updatePatientProfileApi(newPatientProfile);
        call.enqueue(new Callback<UpdatePatientResponse>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<UpdatePatientResponse> call, Response<UpdatePatientResponse> response) {
                if (response.isSuccessful()) {
                    boolean success = response.body().isSuccess();
                    String error = response.body().getError();

                    System.out.println("++++++++++++++++++++++++++++ glo : "+success+"++++++++++++++++++++++++++++");

                    if(success) {
                        // we save also data in local database
                        String token = response.body().getPatient_role();
                        String patient_name = response.body().getPatient_name();
                        String patient_phone_number = response.body().getPatient_phone_number();
                        int patient_age = response.body().getPatient_age();
                        long patient_weight = response.body().getPatient_weight();
                        long patient_size = response.body().getPatient_size();
                        String patient_commune = response.body().getPatient_commune();
                        String patient_quater = response.body().getPatient_quater();
                        String patient_gender = response.body().getPatient_gender();

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... Voids) {
                                patientDao.updateProfilePatient(token, patient_name, patient_phone_number, patient_age, patient_weight, patient_size, patient_commune, patient_quater, patient_gender);
                                callback.onResponse(call, response);
                                return null;
                            }
                        }.execute();
                    } else {
                        callback.onFailure(call, new Throwable(error));
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdatePatientResponse> call, Throwable t) {
                // on fait appel au callback onResponse du ViewModel qui est le même que celui du fragment chargé par la création du compte, et on lui passe en paramètre le resultat
                String errorMessage = t.getMessage();
                if(errorMessage.equals("Failed to connect to /192.168.43.85:80")) callback.onFailure(call, new Throwable("Connectez-vous à l'internet"));
                else callback.onFailure(call, t);
            }
        });
    }

    public void updatePatientPictureApi(Map<String, RequestBody> pictureRequestBodyMap, final Callback<UpdatePatientPictureResponse> callback) {
        Call<UpdatePatientPictureResponse> call = patientService.updatePatientPictureApi(pictureRequestBodyMap);
        call.enqueue(new Callback<UpdatePatientPictureResponse>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<UpdatePatientPictureResponse> call, Response<UpdatePatientPictureResponse> response) {
                if (response.isSuccessful()) {
                    boolean success = response.body().isSuccess();
                    String error = response.body().getError();

                    System.out.println("++++++++++++++++++++++++++++ glo : "+success+"++++++++++++++++++++++++++++");

                    if(success) {
                        // we save also data in local database
                        String token = response.body().getPatient_role();

                        String patientPictureBase64 = response.body().getPatient_picture();
                        byte[] imageBytes = Base64.decode(patientPictureBase64, Base64.DEFAULT); // Convertir la chaîne base64 en tableau de bytes si nécessaire

//                        byte[] imageBytes = response.body().getPatient_picture();

                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... Voids) {
                                patientDao.updatePatientPicture(token, imageBytes); // dans la base de données local
                                callback.onResponse(call, response);
                                return null;
                            }
                        }.execute();
                    } else {
                        callback.onFailure(call, new Throwable(error));
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdatePatientPictureResponse> call, Throwable t) {
                // on fait appel au callback onResponse du ViewModel qui est le même que celui du fragment chargé par la création du compte, et on lui passe en paramètre le resultat
                String errorMessage = t.getMessage();
                if(errorMessage.equals("Failed to connect to /192.168.43.85:80")) callback.onFailure(call, new Throwable("Connectez-vous à l'internet"));
                else callback.onFailure(call, t);

            }
        });

    }



    // ********************************************** for local **********************************************
    // LES 5 FONCTIONS CI-DESSOUS SONT DES APIS QUE NOUS ALLONS EXPOSER A L'EXTERIEUR
    // 1- insertPatient
    @SuppressLint("StaticFieldLeak")
    public void insertPatient(Patient patient) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                patientDao.insertPatient(patient);
                return null;
            }
        }.execute();
    }

    // 2- updatePatient
    public void updateProfilePatient(String token, String userName, String phoneNumber, int userAge, long userWeight, long userSize, String userCommune, String userQuater, String userGender) {
        patientDao.updateProfilePatient(token, userName, phoneNumber, userAge, userWeight, userSize, userCommune, userQuater, userGender);
    }

    @SuppressLint("StaticFieldLeak")
    public void updatePatientToken(String role, String patient_name, String patient_password) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                patientDao.updatePatientToken(role, patient_name, patient_password);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public int getIdPatient(String token) {
        return patientDao.getIdPatient(token);
    }

    public void updatePatientPicture(String token, byte[] imageString) {
        patientDao.updatePatientPicture(token, imageString);
    }



    // 3- deletePatient
    public void deletePatient(Patient patient) {
        patientDao.deletePatient(patient);
    //  new DeletePatientAsyncTask(patientDao).execute(patient);
    }

    // 4- deleteAllPatients
    public void deleteAllPatients() {
        patientDao.deleteAllPatients();
    //  new DeleteAllPatientsAsyncTask(patientDao).execute();
    }

    // 5- getPatientsSortedByDate
    public LiveData<List<Patient>> getAllPatients() {
        return patientDao.getAllPatients();
    }

    // 6- getPatient
    public  LiveData<Patient> getPatient(String token) {
        return patientDao.getPatient(token);
    }
}


















