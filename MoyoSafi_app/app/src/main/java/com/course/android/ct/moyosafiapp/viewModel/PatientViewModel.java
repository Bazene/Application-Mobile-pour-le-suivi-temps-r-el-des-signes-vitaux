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
import com.course.android.ct.moyosafiapp.models.api.UpdatePatientPictureResponse;
import com.course.android.ct.moyosafiapp.models.api.UpdatePatientResponse;
import com.course.android.ct.moyosafiapp.models.entity.Notifications;
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
        observeChangesOnVitalSing();
    }


    // ***************************************************************** FUNCTIONS *********************************************************************

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

    // Observer ajouté au flux RxJava dans VitalSignRealTime
    private void observeChanges() {
        compositeDisposable.add(
        vitalSignRealTimeRepository.observeChanges()
                .subscribeOn(Schedulers.io()) // Schedulers.io() : thread généralement utilisé pour les tâches longues qui ne bloquent pas le thread principal
                .observeOn(Schedulers.io()) // les notifications de l'Observable doivent être reçues sur le thread Schedulers.io()
                .subscribe(newVitalRealTime -> {
                   // abonnement à l'Observable. La fonction lambda fournie sera exécutée chaque fois que l'Observable émettra une notification

                    Log.d("YourViewModel", "++++++++++++++++++++++ Changement détecté dans la base de données. Temp : "+newVitalRealTime.getTemperature()+"+++++++++++++++++++++++++++++++");
                    calculMoyenne(newVitalRealTime); // on calcul la moyenne et on l'enregistrer dans la base
                })
        );
    }

    final VitalSign[] newVitalSign = new VitalSign[1];
    private void observeChangesOnVitalSing() {
        compositeDisposable.add(
                vitalSignRepository.observeChangesOfVitalSign()
                        .subscribeOn(Schedulers.io()) // Schedulers.io() : thread généralement utilisé pour les tâches longues qui ne bloquent pas le thread principal
                        .observeOn(Schedulers.io()) // les notifications de l'Observable doivent être reçues sur le thread Schedulers.io()
                        .subscribe(vitalSign-> {
                            // abonnement à l'Observable. La fonction lambda fournie sera exécutée chaque fois que l'Observable émettra une notification

                            newVitalSign [0] = vitalSign;

                            // we call the function that allow as check if we can create a notification
                            notificationFunction(vitalSign);

                        })
        );
    }

    private void calculMoyenne(VitalSignRealTime newVitalRealTime) {
//       VitalSign lastVitalSing = vitalSignRepository.getLastVitalSignMedium();
       VitalSign lastVitalSing = newVitalSign [0];

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

                   // we reunitialise the data base
                   vitalSignRealTimeRepository.deleteAllRealTimeVitalSign();

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

    public LiveData<VitalSign> getLastVitalSignForUi(int id_patient) {
        return vitalSignRepository.getLastVitalSignForUi(id_patient);
    }

    public void insertOtherVitalSign(VitalSign vitalSign) {
        executor.execute(()->vitalSignRepository.insertOtherVitalSign(vitalSign));
    }

    public void deleteAllRealTimeVitalSignOnBluetoothServiceThread() {
        executor.execute(()->vitalSignRealTimeRepository.deleteAllRealTimeVitalSign());
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
    }


    // -----------------------------------------------------------------------------------------------------------
    //  FOR NOTIFICATIONS (WE NOTIFY ONLY NI CRITIQUE MOMENT)
    // -----------------------------------------------------------------------------------------------------------
    public void notificationFunction(VitalSign vitalSign) {
        // we get all vitalSign
        int heart_rate = vitalSign.getHeart_rate();
        int spo2 = vitalSign.getOxygen_level();
        float temperature = vitalSign.getTemperature();
        int systolic = vitalSign.getSystolic_blood();
        int diastolic = vitalSign.getDiastolic_blood();
        int glycemie = vitalSign.getBlood_glucose();

        String contentNotification = "";

        // 1. we verify the heart rate
        if( heart_rate != 0) {
            if (heart_rate < 50) contentNotification = contentNotification+"Fréquence cardiaque faible : "+heart_rate+" BPM \n";
            else if (heart_rate > 50) contentNotification = contentNotification+ "Fréquence cardiaque élevée : "+heart_rate+" BPM \n";
        }

        // 2. we verify the oxygen level
        if (spo2 != 0) {
            if(spo2 < 90) contentNotification = contentNotification+ "Saturation en oxygène faible : "+spo2+" %\n";
        }

        // 3. we verify the temperature
        if (temperature != 0) {
            if(temperature < 36) contentNotification = contentNotification+ "Temperature faible : "+temperature+" °C\n";
            else if (temperature > 38) contentNotification = contentNotification+ "Temperature élevée : "+temperature+" °C\n";
        }

        // 4. we verify the systolic and diastolic values
        if (systolic != 0 && diastolic != 0) {
            if(systolic <= 90 && diastolic <= 60 ) contentNotification = contentNotification+ "Tension artérielle faible : "+systolic+"/"+diastolic+" mmHg\n";
            else if (systolic >= 140 && diastolic >= 90 ) contentNotification = contentNotification+ "Tension artérielle élevée : "+systolic+"/"+diastolic+" mmHg\n";
        }

        // 5. we verify the glycemie
        if (glycemie != 0) {
            if(glycemie <= 70) contentNotification = contentNotification+ "Glycémie faible : "+glycemie+" mg/dL\n";
            else if (glycemie >= 110) contentNotification = contentNotification+ "Glycémie Elevé : "+glycemie+" mg/dL\n";
        }

        // we create a notification
        if(contentNotification != "") {
            Notifications notification = new Notifications(vitalSign.getId_patient(), vitalSign.getId(), contentNotification, vitalSign.getVital_date(), vitalSign.getVital_hour());

            notificationsRepository.insertNotifications(notification);
        }
    }

    public LiveData<List<Notifications>> getAllNotifications() {
        return notificationsRepository.getAllNotifications();
    }
}