package com.course.android.ct.moyosafiapp.viewModel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.course.android.ct.moyosafiapp.models.Repository.NotificationsRepository;
import com.course.android.ct.moyosafiapp.models.Repository.PatientRepository;
import com.course.android.ct.moyosafiapp.models.Repository.VitalSignRepository;
import com.course.android.ct.moyosafiapp.models.SessionManager;
import com.course.android.ct.moyosafiapp.models.api.CreateAccountResponse;
import com.course.android.ct.moyosafiapp.models.api.LogPatientResponse;
import com.course.android.ct.moyosafiapp.models.entity.Patient;
import com.course.android.ct.moyosafiapp.models.entity.VitalSign;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Callback;

public class PatientViewModel extends ViewModel {

    // GET REPOSITORIES
    private SessionManager sessionManager;
    private PatientRepository patientRepository;
    private NotificationsRepository notificationsRepository;
    private VitalSignRepository vitalSignRepository;

    // private Executor executor;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private MutableLiveData<Map<String, String>> bluetoothLiveData = new MutableLiveData<Map<String, String>>(); // for bluetooth data

    // DATA
    @Nullable
    private LiveData<Patient> curentPatient;

    // CONSTRUCT
    public PatientViewModel(PatientRepository patientRepository, NotificationsRepository notificationsRepository, VitalSignRepository vitalSignRepository, ExecutorService executor) {
        this.patientRepository = patientRepository;
        this.notificationsRepository = notificationsRepository;
        this.vitalSignRepository = vitalSignRepository;
        this.executor = executor; // ce ci, nous facilitera l'exécution en arrière-plan de certaines méthodes, aulieu d'utiliser les Threads
    }

    // FUNCTIONS


    // ----------------------------
    // 1- FOR PATIENT
    // ----------------------------
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




    // --------------------------
    // 2- FOR VITAL SIGN
    // --------------------------

//    public void insertBluetoothData(Map<String, String> mapResult) {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    System.out.println("+++++++++++++++++++++++++++ la méthode a été appelé");
//                // other vital sign
//                Float temperature = Float.valueOf(mapResult.get("Temp"));
//                int heart_rate = Integer.parseInt(mapResult.get("Hz"));
//                int oxygen_level = Integer.parseInt(mapResult.get("Spo2"));
//
//                // we get the token
//                String token = sessionManager.getAuthToken();
//
//                // we get id of patient
//                int id = getIdPatient(token);
//
//                // we get the last objet in VitalSign table
//                VitalSign lastVitalSign = vitalSignRepository.getLastVitalSign();
//
//                // Actual date and hour
//                LocalDateTime now = LocalDateTime.now();
//                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formater la date
//                DateTimeFormatter formatterHeure = DateTimeFormatter.ofPattern("HH:mm:ss"); // Formater l'heur
//                String actuelDate = now.format(formatterDate);
//                String actuelHour = now.format(formatterHeure);
//
//
//                if (lastVitalSign != null) {
//                    // we get other vial sign
//                    int blood_glucose = lastVitalSign.getBlood_glucose();
//                    int systolic_blood = lastVitalSign.getSystolic_blood();
//                    int diastolic_blood = lastVitalSign.getDiastolic_blood();
//
//                    // insert the vital sign in data base
//                    VitalSign vitalSign = new VitalSign(id, temperature, heart_rate, oxygen_level, blood_glucose, systolic_blood, diastolic_blood, actuelDate, actuelHour);
//                    vitalSignRepository.insertVitalSign(vitalSign);
//
//                } else {
//                    int blood_glucose = 0;
//                    int systolic_blood = 0;
//                    int diastolic_blood = 0;
//
//                    // insert the vital sign in data base
//                    VitalSign vitalSign = new VitalSign(id, temperature, heart_rate, oxygen_level, blood_glucose, systolic_blood, diastolic_blood, actuelDate, actuelHour);
//                    vitalSignRepository.insertVitalSign(vitalSign);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e("Error", "An error occurred: " + e.getMessage());
//            }
//
//                return null;
//            }
//        }.execute();
//    }

    // Méthode pour observer les données ici Bluetooth
    public LiveData<Map<String, String>> getBluetoothLiveData() {
        return bluetoothLiveData;
    }

    // Méthode pour mettre à jour les données dans le ViewModel
    public void updateBluetoothData(Map<String, String>  mapResult) {
        bluetoothLiveData.postValue(mapResult);

        // Vous pouvez ajouter ici la logique de traitement ou d'enregistrement dans la base Room
        // Actual date and hour
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formater la date
        DateTimeFormatter formatterHeure = DateTimeFormatter.ofPattern("HH:mm:ss"); // Formater l'heur
        String actuelDate = now.format(formatterDate);
        String actuelHour = now.format(formatterHeure);

        try {
            int id = Integer.parseInt(mapResult.get("idPatient")); // we get id of patient
            Float temperature = Float.valueOf(mapResult.get("Temp"));
            int heart_rate = Integer.parseInt(mapResult.get("Hz"));
            int oxygen_level = Integer.parseInt(mapResult.get("Spo2"));

            VitalSign vitalSign = new VitalSign(id, temperature, heart_rate, oxygen_level, 2, 3, 4, actuelDate, actuelHour);
            executor.execute(()-> vitalSignRepository.insertVitalSign(vitalSign));

        } catch (NumberFormatException e) {
            // Gérez l'exception en cas d'échec de la conversion
            e.printStackTrace();
        }

        System.out.println("++++++++++++++++++++++++++++++++++++ Temp : "+mapResult.get("Temp")+"; Hz : "+mapResult.get("Hz")+" Spo2 :"+mapResult.get("Spo2")+" idPatient : "+mapResult.get("idPatient")+"++++++++++++++++++++++++++++++++++++");
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

    // --------------------------
    // 3- FOR NOTIFICATIONS
    // --------------------------

}
