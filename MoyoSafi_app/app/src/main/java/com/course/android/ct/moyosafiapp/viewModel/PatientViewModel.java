package com.course.android.ct.moyosafiapp.viewModel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.course.android.ct.moyosafiapp.models.Repository.NotificationsRepository;
import com.course.android.ct.moyosafiapp.models.Repository.PatientRepository;
import com.course.android.ct.moyosafiapp.models.Repository.VitalSignRealTimeRepository;
import com.course.android.ct.moyosafiapp.models.Repository.VitalSignRepository;
import com.course.android.ct.moyosafiapp.models.SessionManager;
import com.course.android.ct.moyosafiapp.models.api.CreateAccountResponse;
import com.course.android.ct.moyosafiapp.models.api.LogPatientResponse;
import com.course.android.ct.moyosafiapp.models.entity.Patient;
import com.course.android.ct.moyosafiapp.models.entity.VitalSign;
import com.course.android.ct.moyosafiapp.models.entity.VitalSignRealTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Callback;

public class PatientViewModel extends ViewModel {

    // GET REPOSITORIES
    private SessionManager sessionManager;
    private PatientRepository patientRepository;
    private NotificationsRepository notificationsRepository;
    private VitalSignRepository vitalSignRepository;
    private VitalSignRealTimeRepository vitalSignRealTimeRepository;

    private int nbrIteration = 1;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    // private Executor executor;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private MutableLiveData<Map<String, String>> bluetoothLiveData = new MutableLiveData<Map<String, String>>(); // for bluetooth data

    // DATA
    @Nullable
    private LiveData<Patient> curentPatient;

    // CONSTRUCT
    public PatientViewModel(PatientRepository patientRepository, NotificationsRepository notificationsRepository, VitalSignRepository vitalSignRepository, VitalSignRealTimeRepository vitalSignRealTimeRepository, ExecutorService executor) {
        this.patientRepository = patientRepository;
        this.notificationsRepository = notificationsRepository;
        this.vitalSignRepository = vitalSignRepository;
        this.vitalSignRealTimeRepository = vitalSignRealTimeRepository;
        this.executor = executor; // ce ci, nous facilitera l'exécution en arrière-plan de certaines méthodes, aulieu d'utiliser les Threads

        observeChanges(); // pour démarrer l'observation des changements dès que le ViewModel est créé.
    }

    // FUNCTIONS


    // -----------------------------------------------------------------------------------------------------------
    // 1- FOR PATIENT
    // -----------------------------------------------------------------------------------------------------------
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

    public int getIdPatient(String token) {
        return patientRepository.getIdPatient(token);
    }



    // -----------------------------------------------------------------------------------------------------------
    // 2- FOR VITAL SIGN REAL TIME
    // -----------------------------------------------------------------------------------------------------------

    // Méthode pour observer les données ici Bluetooth
    public LiveData<Map<String, String>> getBluetoothLiveData() {
        return bluetoothLiveData;
    }

    // METHODE THAT HELP TO INSERT DATA IN REALTIME DATABASE
    @SuppressLint("StaticFieldLeak")
    public void updateBluetoothData(Map<String, String>  mapResult) {
        bluetoothLiveData.postValue(mapResult);

        // INSERTING DATA INTO ROOM DATA BASE
        // Get actual date and hour
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formater la date
        DateTimeFormatter formatterHeure = DateTimeFormatter.ofPattern("HH:mm:ss"); // Formater l'heur
        String actuelDate = now.format(formatterDate);
        String actuelHour = now.format(formatterHeure);

        int id = Integer.parseInt(mapResult.get("idPatient")); // we get id of patient
        Float temperature = Float.valueOf(mapResult.get("Temp"));
        int heart_rate = Integer.parseInt(mapResult.get("Hz"));
        int oxygen_level = Integer.parseInt(mapResult.get("Spo2"));

        System.out.println("++++++++++++++++++++++++++++++++++++ Temp : "+mapResult.get("Temp")+"; Hz : "+mapResult.get("Hz")+" Spo2 :"+mapResult.get("Spo2")+" idPatient : "+mapResult.get("idPatient")+"++++++++++++++++++++++++++++++++++++");

        VitalSignRealTime vitalSignRealTime = new VitalSignRealTime(id, temperature, heart_rate, oxygen_level, actuelDate, actuelHour, nbrIteration);
        nbrIteration +=1; // incremente the iteration number

        executor.execute(()->vitalSignRealTimeRepository.insertVitalSignRealTime(vitalSignRealTime));
    }

    // REAL TIME
    public LiveData<VitalSignRealTime> getVitalSignRealTimeForUi() {
        return vitalSignRealTimeRepository.getVitalSignRealTimeForUi();
    }



    // -----------------------------------------------------------------------------------------------------------
    // 3- FOR VITAL SIGN
    // -----------------------------------------------------------------------------------------------------------

    // FOR MOYENNE CALCUL


//    if(vitalSignRealSign.getNbrIteration() == 1) {
//        // calcul de Mo (moyenne initiale)
//    } else {
//        // calcul de Mi, autre que Mo, i.e. i != o
//    }

    // Observer ajouté au flux RxJava
    private void observeChanges() {
        compositeDisposable.add(
        vitalSignRealTimeRepository.observeChanges()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(newVitalRealTime -> {
                    // Réagir aux changements dans le ViewModel

                    Log.d("YourViewModel", "++++++++++++++++++++++ Changement détecté dans la base de données.+++++++++++++++++++++++++++++++=");
                })
        );
    }


    public LiveData<VitalSign> getLastVitalSign() {
        return vitalSignRepository.getLastVitalSign();
    }

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



    // -----------------------------------------------------------------------------------------------------------
    // 4- FOR NOTIFICATIONS
    // -----------------------------------------------------------------------------------------------------------

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
    }
}
