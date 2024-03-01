package com.course.android.ct.moyosafiapp.models.Repository;

import androidx.lifecycle.LiveData;

import com.course.android.ct.moyosafiapp.models.api.CreateAccountResponse;
import com.course.android.ct.moyosafiapp.models.api.PatientService;
import com.course.android.ct.moyosafiapp.models.api.RetrofitClientInstance;
import com.course.android.ct.moyosafiapp.models.dao.PatientDao;
import com.course.android.ct.moyosafiapp.models.entity.Patient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PatientRepository {

    // VARIABLES
    private PatientDao patientDao;

    //***********************************
    // ************ for remote **********
    PatientService patientService;
    Retrofit retrofit;

    // CONSTRUCT
    public PatientRepository(PatientDao patientDao){

        this.patientDao = patientDao;

        //***********************************
        // ************ for remote **********
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
    // LES 5 FONCTIONS CI-DESSOUS SONT DES APIS QUE NOUS ALLONS EXPOSER A L'EXTERIEUR
    // 1- insertPatient
//    public void insertPatient(Patient patient) {
//        patientDao.insertPatient(patient);
//        new InsertPatientAsyncTask(patientDao).execute(patient);
//    }

    //*********************************************************************************************************
    // ********************************************** for remote **********************************************
    public void createPatient(Patient patient, final Callback<CreateAccountResponse> callback) {
        Call<CreateAccountResponse> call = patientService.createPatient(patient);
        call.enqueue(new Callback<CreateAccountResponse>() {
            @Override
            public void onResponse(Call<CreateAccountResponse> call, Response<CreateAccountResponse> response) {
                if (response.isSuccessful()) {
                        boolean success = response.body().getSuccess() ;
                        String error = response.body().getError();

                        if(success) {
                            // on fait appel au callback onResponse du ViewModel qui est le même que celui du fragment chargé par la création du compte, et on lui passe en paramètre le resultat
                            callback.onResponse(call, response);
                        }
                        else {
                            callback.onFailure(call, new Throwable("Échec de l'enregistrement dans la base code, Error : " + error));
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
                callback.onFailure(call, t);
            }
        });
    }

    // 2- updatePatient
    public void updatePatient(Patient patient) {
        patientDao.updatePatient(patient);
//        new UpdatePatientAsyncTask(patientDao).execute(patient);
    }

    // 3- deletePatient
    public void deletePatient(Patient patient) {
        patientDao.deletePatient(patient);
//        new DeletePatientAsyncTask(patientDao).execute(patient);
    }

    // 4- deleteAllPatients
    public void deleteAllPatients() {
        patientDao.deleteAllPatients();
//        new DeleteAllPatientsAsyncTask(patientDao).execute();
    }

    // 5- getPatientsSortedByDate
    public LiveData<List<Patient>> getPatientsSortedByDate() {
        return  patientDao.getPatientsSortedByDate();
    }

    // 6- getPatient
    public  LiveData<Patient> getPatient(int id) {
        return patientDao.getPatient(id);
    }
}


















