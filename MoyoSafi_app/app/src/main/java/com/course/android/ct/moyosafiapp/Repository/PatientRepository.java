package com.course.android.ct.moyosafiapp.Repository;

import androidx.lifecycle.LiveData;

import com.course.android.ct.moyosafiapp.database.dao.PatientDao;
import com.course.android.ct.moyosafiapp.database.models.Patient;

import java.util.List;

public class PatientRepository {

    // VARIABLES
    private PatientDao patientDao;

    // CONSTRUCT
    public PatientRepository(PatientDao patientDao){

        this.patientDao = patientDao;
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
    public void insertPatient(Patient patient) {
        patientDao.insertPatient(patient);
//        new InsertPatientAsyncTask(patientDao).execute(patient);
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

    // we use Executor instance of AsyncTask

    // WE CREATE THE BACKGROUND THREAD BECAUSE ROOM DOESN'T WORK IN THEN MAIN THREAD
    // 11- InsertPatientAsyncTask
//    private static class InsertPatientAsyncTask extends AsyncTask<Patient, Void, Void> {
//        private PatientDao patientDao;
//
//        private InsertPatientAsyncTask(PatientDao patientDao) {
//            this.patientDao = patientDao;
//        }
//
//        @Override
//        protected Void doInBackground(Patient... patients) {
//            patientDao.insertPatient(patients[0]);
//            return null;
//        }
//    }
//
//    // 22- UpdatePatientAsyncTask
//    private static class UpdatePatientAsyncTask extends AsyncTask<Patient, Void, Void> {
//        private PatientDao patientDao;
//
//        private UpdatePatientAsyncTask(PatientDao patientDao) {
//            this.patientDao = patientDao;
//        }
//
//        @Override
//        protected Void doInBackground(Patient... patients) {
//            patientDao.updatePatient(patients[0]);
//            return null;
//        }
//    }
//
//    // 33- DeletePatientAsyncTask
//    private static class DeletePatientAsyncTask extends AsyncTask<Patient, Void, Void> {
//        private PatientDao patientDao;
//
//        private DeletePatientAsyncTask(PatientDao patientDao) {
//            this.patientDao = patientDao;
//        }
//
//        @Override
//        protected Void doInBackground(Patient... patients) {
//            patientDao.deletePatient(patients[0]);
//            return null;
//        }
//    }
//
//    // 44- DeleteAllPatientsAsyncTask
//    private static class DeleteAllPatientsAsyncTask extends AsyncTask<Patient, Void, Void> {
//        private PatientDao patientDao;
//
//        private DeleteAllPatientsAsyncTask(PatientDao patientDao) {
//            this.patientDao = patientDao;
//        }
//
//        @Override
//        protected Void doInBackground(Patient... patients) {
//            patientDao.deleteAllPatients();
//            return null;
//        }
//    }
}
