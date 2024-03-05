package com.course.android.ct.moyosafiapp.viewModel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.course.android.ct.moyosafiapp.models.Repository.NotificationsRepository;
import com.course.android.ct.moyosafiapp.models.Repository.PatientRepository;
import com.course.android.ct.moyosafiapp.models.Repository.VitalSignRepository;
import com.course.android.ct.moyosafiapp.models.SessionManager;
import com.course.android.ct.moyosafiapp.models.api.CreateAccountResponse;
import com.course.android.ct.moyosafiapp.models.api.LogPatientResponse;
import com.course.android.ct.moyosafiapp.models.entity.Patient;
import com.course.android.ct.moyosafiapp.models.entity.VitalSign;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Callback;

public class PatientViewModel extends ViewModel {

    // GET REPOSITORIES
    private SessionManager sessionManager;
    private PatientRepository patientRepository;
    private NotificationsRepository notificationsRepository;
    private VitalSignRepository vitalSignRepository;

//  private Executor executor;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    // DATA
    @Nullable
    private LiveData<Patient> curentPatient;

    // CONSTRUCT
    public PatientViewModel(PatientRepository patientRepository, NotificationsRepository notificationsRepository, VitalSignRepository vitalSignRepository, ExecutorService executor) {
        this.patientRepository = patientRepository;
        this.notificationsRepository = notificationsRepository;
        this.vitalSignRepository = vitalSignRepository;

        this.sessionManager = sessionManager; // for session : help to now if the user is log or not

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


    //*********************************************************************************************************
    // ********************************************** for remote **********************************************
    public void createPatient(Patient patient, Callback<CreateAccountResponse> callback) {
        patientRepository.createPatient(patient, callback);
    }

    public void logPatient(String patient_name, String patient_password, Callback<LogPatientResponse> callback) {
        patientRepository.logPatient(patient_name, patient_password, callback);
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

    public LiveData<List<Patient>> getAllPatients() {
        // here we don't use the class Executor, because LiveDate run instructions in background
        return patientRepository.getAllPatients();
    }

    // --------------------------
    // 2- FOR VITAL SIGN
    // --------------------------
    public void insertVitalSign(VitalSign vitalSign) {
        executor.execute(()-> vitalSignRepository.insertVitalSign(vitalSign));
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
