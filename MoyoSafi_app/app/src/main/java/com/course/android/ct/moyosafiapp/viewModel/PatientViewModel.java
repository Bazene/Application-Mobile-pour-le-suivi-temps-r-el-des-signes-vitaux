package com.course.android.ct.moyosafiapp.viewModel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.course.android.ct.moyosafiapp.Repository.NotificationsRepository;
import com.course.android.ct.moyosafiapp.Repository.PatientRepository;
import com.course.android.ct.moyosafiapp.Repository.VitalSignRepository;
import com.course.android.ct.moyosafiapp.database.models.Patient;
import com.course.android.ct.moyosafiapp.database.models.VitalSign;

import java.util.List;
import java.util.concurrent.Executor;

public class PatientViewModel extends ViewModel {

    // GET REPOSITORIES
    private PatientRepository patientRepository;
    private NotificationsRepository notificationsRepository;
    private VitalSignRepository vitalSignRepository;

    private Executor executor;

    // DATA
    @Nullable
    private LiveData<Patient> curentPatient;

    // CONSTRUCT
    public PatientViewModel(PatientRepository patientRepository, NotificationsRepository notificationsRepository, VitalSignRepository vitalSignRepository, Executor executor) {

        this.patientRepository = patientRepository;
        this.notificationsRepository = notificationsRepository;
        this.vitalSignRepository = vitalSignRepository;

        this.executor = executor; // ce ci, nous facilitera l'exécution en arrière-plan de certaines méthodes, aulieu d'utiliser les Threads
    }

    // FUNCTIONS
    // for initialisation
    public void init(int id_patient) {
        if(this.curentPatient != null) {
            return;
        }
        curentPatient = patientRepository.getPatient(id_patient);
    }

    // --------------------------
    // 1- FOR PATIENT
    // --------------------------
    public void insertPatient(String patient_name, String patient_postname, String patient_surname, String patient_gender, String patient_mail, int patient_phone_number, String patient_password, String patient_date_created, int patient_age) {
        executor.execute(()->{
            patientRepository.insertPatient(new Patient(patient_name, patient_postname, patient_surname, patient_gender, patient_mail, patient_phone_number, patient_password, patient_date_created, patient_age));
        });
    }

    public void updatePatient(Patient patient) {
        executor.execute(()-> patientRepository.updatePatient(patient));
    }

    public  void deletePatient(Patient patient) {
        executor.execute(()-> patientRepository.deletePatient(patient));
    }

    public void deleteAllPatients() {
        executor.execute(()-> patientRepository.deleteAllPatients());
    }

    public LiveData<List<Patient>> getPatientsSortedByDate() {
        // here we don't use the class Executor, because LiveDate run instructions in background
        return patientRepository.getPatientsSortedByDate();
    }

    // --------------------------
    // 2- FOR VITAL SIGN
    // --------------------------
    public void insertVitalSign(int id_patient, float temperature, int heart_rate, int oxygen_level, int blood_glucose, int systolic_blood, int diastolic_blood, String vital_hour, String vital_date) {
        executor.execute(()-> {
            vitalSignRepository.insertVitalSign(new VitalSign(id_patient ,temperature, heart_rate, oxygen_level, blood_glucose, systolic_blood, diastolic_blood, vital_hour, vital_date));
        });
    }

    public void deleteVitalSign(VitalSign vitalSign) {
        executor.execute(()->vitalSignRepository.deleteVitalSign(vitalSign));
    }

    public void deleteAllVitalSigns() {
        executor.execute(()->vitalSignRepository.deleteAllVitalSigns());
    }

    public void deleteVitalSignPerHour(String vital_hour) {
        executor.execute(()->vitalSignRepository.deleteVitalSignPerHour(vital_hour));
    }

    public LiveData<VitalSign> getVitalSign(int id) {
        return  vitalSignRepository.getVitalSign(id);
    }

    public  LiveData<List<VitalSign>> getVitalSignsSortedByDateTime() {
        return  vitalSignRepository.getVitalSignsSortedByDateTime();
    }

    // --------------------------
    // 3- FOR NOTIFICATIONS
    // --------------------------

}
