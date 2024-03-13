package com.course.android.ct.moyosafiapp.viewModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

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
import com.course.android.ct.moyosafiapp.models.api.UpdatePatientPictureResponse;
import com.course.android.ct.moyosafiapp.models.api.UpdatePatientResponse;
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
import okhttp3.RequestBody;
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
    // 6- getPatient
    public  LiveData<Patient> getPatient(String token) {
        return patientRepository.getPatient(token);
    }

    public int getIdPatient(String token) {
        return patientRepository.getIdPatient(token);
    }

    public void updateProfilePatient(String token, String userName, String phoneNumber, int userAge, long userWeight, long userSize, String userCommune, String userQuater, String userGender) {
        executor.execute(()->patientRepository.updateProfilePatient(token, userName, phoneNumber, userAge, userWeight, userSize, userCommune, userQuater, userGender));
    }

    public void updatePatientPicture(String token, byte[] imageString) {
        executor.execute(()->patientRepository.updatePatientPicture(token, imageString));
    }

    public LiveData<List<Patient>> getAllPatients() {
        // here we don't use the class Executor, because LiveDate run instructions in background
        return patientRepository.getAllPatients();
    }

    //*********************************************************************************************************
    // ********************************************** for remote **********************************************
    public void createPatient(Patient patient, Callback<CreateAccountResponse> callback) {
        patientRepository.createPatient(patient, callback);
    }

    public void logPatient(String patient_name, String patient_password, Callback<LogPatientResponse> callback) {
        patientRepository.logPatient(patient_name, patient_password, callback);
    }

    public void updatePatientProfileApi(Map<String, String> newPatientProfile, Callback<UpdatePatientResponse> callback) {
        patientRepository.updatePatientProfileApi(newPatientProfile, callback);
    }

    public void updatePatientPictureApi(Map<String, RequestBody> pictureRequestBodyMap, Callback<UpdatePatientPictureResponse> callback) {
        patientRepository.updatePatientPictureApi(pictureRequestBodyMap, callback);
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

        try {
            int id = Integer.parseInt(mapResult.get("idPatient")); // we get id of patient
            Float temperature = Float.valueOf(mapResult.get("Temp"));
            int heart_rate = Integer.parseInt(mapResult.get("Hz"));
            int oxygen_level = Integer.parseInt(mapResult.get("Spo2"));

            System.out.println("++++++++++++++++++++++++++++++++++++ Temp : " + mapResult.get("Temp") + "; Hz : " + mapResult.get("Hz") + " Spo2 :" + mapResult.get("Spo2") + " idPatient : " + mapResult.get("idPatient") + "++++++++++++++++++++++++++++++++++++");

            VitalSignRealTime vitalSignRealTime = new VitalSignRealTime(id, temperature, heart_rate, oxygen_level, actuelHour, actuelDate,  nbrIteration);
            nbrIteration += 1; // incremente the iteration number

            executor.execute(() -> vitalSignRealTimeRepository.insertVitalSignRealTime(vitalSignRealTime));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // REAL TIME
    public LiveData<VitalSignRealTime> getVitalSignRealTimeForUi() {
        return vitalSignRealTimeRepository.getVitalSignRealTimeForUi();
    }



    // -----------------------------------------------------------------------------------------------------------
    // 3- FOR VITAL SIGN
    // -----------------------------------------------------------------------------------------------------------

    // Observer ajouté au flux RxJava
    private void observeChanges() {
        compositeDisposable.add(
        vitalSignRealTimeRepository.observeChanges()
                .subscribeOn(Schedulers.io()) // Schedulers.io() : thread généralement utilisé pour les tâches longues qui ne bloquent pas le thread principal
                .observeOn(Schedulers.io()) // les notifications de l'Observable doivent être reçues sur le thread Schedulers.io()
                .subscribe(newVitalRealTime -> {
                   // abonnement à l'Observable. La fonction lambda fournie sera exécutée chaque fois que l'Observable émettra une notification

//                    Log.d("YourViewModel", "++++++++++++++++++++++ Changement détecté dans la base de données. Temp : "+newVitalRealTime.getTemperature()+"+++++++++++++++++++++++++++++++");
                    calculMoyenne(newVitalRealTime); // on calcul la moyenne et on l'enregistrer dans la base
                })
        );
    }

    private void calculMoyenne(VitalSignRealTime newVitalRealTime) {
       VitalSign lastVitalSing = vitalSignRepository.getLastVitalSignMedium();

       int blood_glucose = 0; int systolic_blood = 0; int diastolic_blood = 0;

       if(lastVitalSing != null) {
            blood_glucose = lastVitalSing.getBlood_glucose();
            systolic_blood = lastVitalSing.getSystolic_blood();
            diastolic_blood = lastVitalSing.getDiastolic_blood();
       }

       if(newVitalRealTime.getNbrIteration() == 1) {
           // calcul for heart beat
           int hz_new = newVitalRealTime.getHeart_rate();
           int new_medium_hz = hz_new;

           // calcul for oxygen level
           int spo2_new = newVitalRealTime.getOxygen_level();
           int new_medium_spo2 = spo2_new;

           // calcul for temperature
           Float temp_new = newVitalRealTime.getTemperature();
           Float new_medium_temp = temp_new;

           // INSERT THE MEDIUM VALUE IN DATABASE
           VitalSign new_vitalSign = new VitalSign(newVitalRealTime.getId_patient(), new_medium_temp, new_medium_hz, new_medium_spo2, blood_glucose, systolic_blood, diastolic_blood, newVitalRealTime.getVital_hour(), newVitalRealTime.getVital_date());
           vitalSignRepository.insertVitalSign(new_vitalSign);

       } else {
               // calcul for heart beat

               if((newVitalRealTime.getNbrIteration() % 5) == 0) {
                   // vitalSignRealTimeRepository.deleteAllRealTimeVitalSign(); // we delete all data in vitalSignRealTime table

                   int new_medium_hz = newVitalRealTime.getHeart_rate();
                   int new_medium_spo2 = newVitalRealTime.getOxygen_level();
                   Float new_medium_temp = newVitalRealTime.getTemperature();

                   VitalSign new_vitalSign = new VitalSign(newVitalRealTime.getId_patient(), new_medium_temp, new_medium_hz, new_medium_spo2, blood_glucose, systolic_blood, diastolic_blood, newVitalRealTime.getVital_hour(), newVitalRealTime.getVital_date());
                   vitalSignRepository.insertVitalSign(new_vitalSign);
                   System.out.println("++++++++++++++++++++++++++++ Nbr itération :" + newVitalRealTime.getNbrIteration() + "+++++++++++++++++++++++++++++++");


               } else {
                   try {
                       int old_medium_hz = lastVitalSing.getHeart_rate();
                       int hz_new = newVitalRealTime.getHeart_rate();
                       int new_nbr_iteration = newVitalRealTime.getNbrIteration();
                       int old_nbr_iteration = new_nbr_iteration - 1;
                       int new_medium_hz = ((old_medium_hz * old_nbr_iteration) + hz_new) / new_nbr_iteration;

                       // calcul for oxygen level
                       int old_medium_spo2 = lastVitalSing.getOxygen_level();
                       int spo2_new = newVitalRealTime.getOxygen_level();
                       int new_medium_spo2 = ((old_medium_spo2 * old_nbr_iteration) + spo2_new) / new_nbr_iteration;

                       // calcul for temperature
                       Float old_medium_temp = lastVitalSing.getTemperature();
                       Float temp_new = newVitalRealTime.getTemperature();
                       Float new_medium_temp = ((old_medium_temp * old_nbr_iteration) + temp_new) / new_nbr_iteration;

                       lastVitalSing.setHeart_rate(new_medium_hz);
                       lastVitalSing.setOxygen_level(new_medium_spo2);
                       lastVitalSing.setTemperature(new_medium_temp);
                       lastVitalSing.setVital_hour(newVitalRealTime.getVital_hour());
                       lastVitalSing.setVital_date(newVitalRealTime.getVital_date());

                       vitalSignRepository.updateVitalSign(lastVitalSing);

                   } catch (NullPointerException e) {
                       // Gérez l'exception en cas d'échec de la conversion
                       e.printStackTrace();
                   }
               }
           }
       }

    public LiveData<VitalSign> getLastVitalSignForUi() {
        return vitalSignRepository.getLastVitalSignForUi();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertOtherVitalSign(Context context1, int new_systol_value, int new_diastol_value, int new_glycemie_vital) {

        sessionManager = SessionManager.getInstance(context1);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids){
                String token = sessionManager.getAuthToken();
                int idPatient = getIdPatient(token);
                VitalSign vitalSignForPatientConnected = vitalSignRepository.getVitalSignForPatientConnected(idPatient);

                Float temp = vitalSignForPatientConnected.getTemperature();
                int hz = vitalSignForPatientConnected.getHeart_rate();
                int spo2 = vitalSignForPatientConnected.getOxygen_level();

                // Get actual date and hour
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formater la date
                DateTimeFormatter formatterHeure = DateTimeFormatter.ofPattern("HH:mm:ss"); // Formater l'heur
                String actuelDate = now.format(formatterDate);
                String actuelHour = now.format(formatterHeure);

                VitalSign newVitalSign = new VitalSign(idPatient, temp, hz, spo2, new_glycemie_vital, new_systol_value, new_diastol_value, actuelHour, actuelDate);
                vitalSignRepository.insertVitalSign(newVitalSign);

                return null;
            }
        }.execute();
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
