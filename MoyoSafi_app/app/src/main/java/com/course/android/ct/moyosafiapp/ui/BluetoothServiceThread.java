package com.course.android.ct.moyosafiapp.ui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.course.android.ct.moyosafiapp.models.SessionManager;
import com.course.android.ct.moyosafiapp.viewModel.PatientViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BluetoothServiceThread extends Service {
    private static Context contextBluetoothActivity;
    private static PatientViewModel patientViewModel;
    private static BluetoothDevice bluetooth_device;
    private BluetoothThread bluetoothThread;
    private static SessionManager sessionManager;

    // DEFAULT CONSTRUCT
    public BluetoothServiceThread() {
        super();
    }

//    public BluetoothServiceThread(PatientViewModel patientViewModel) {
//        this.patientViewModel = patientViewModel;
//    }

    // static method (not native method)
    public static void startService(Context context, PatientViewModel patientViewModelBA, BluetoothDevice device) {
        Intent intent = new Intent(context, BluetoothServiceThread.class);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Utilisez startForegroundService pour Android 8.0 et versions ultérieures
//            context.startForegroundService(intent);
//        } else {
//            // Utilisez startService pour les versions antérieures à Android 8.0
//            context.startService(intent);
//        }
        context.startService(intent);
        System.out.println("+++++++++++++++++++++++++ The service is started ++++++++++++++++++++++++++");
        patientViewModel = patientViewModelBA; // Récupérer le ViewModel injecté
        bluetooth_device = device; // Récupérer le device injecté
        contextBluetoothActivity = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sessionManager = SessionManager.getInstance(contextBluetoothActivity);

        System.out.println("+++++++++++++++++++++++++ We are in the service ++++++++++++++++++++++++++");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("+++++++++++++++++++++ Before Thread ++++++++++++++++++++++++");

        // Instancier et exécuter la tâche AsyncTask
        // Instancier et exécuter la tâche AsyncTask
        bluetoothThread = new BluetoothThread(patientViewModel);
        bluetoothThread.execute();

        System.out.println("+++++++++++++++++++++ After Thread ++++++++++++++++++++++++");
        return START_STICKY; // Redémarre le service s'il est tué par le système
    }

    @Override
    public void onDestroy() {
        // Arrêter le thread Bluetooth lorsque le service est détruit
        // Arrêter le thread Bluetooth lorsque le service est détruit
        if (bluetoothThread != null) {
            bluetoothThread.cancel(true);
        }

        super.onDestroy();
    }

    // AsyncTask pour gérer la connexion Bluetooth en arrière-plan
    private static class BluetoothThread extends AsyncTask<Void, Map<String, String>, Void> {
        BluetoothSocket bluetoothSocket; // For point connexion
        BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        InputStream inputStream;
        String all_data_received = "";
        String receivedData = "";

        PatientViewModel patientViewModel;

        // Add this constructor
        BluetoothThread(PatientViewModel patientViewModel) {
            this.patientViewModel = patientViewModel;
        }

        @SuppressLint({"MissingPermission", "WrongThread", "StaticFieldLeak"})
        @Override
        protected Void doInBackground(Void... params) {
            try {
                System.out.println("++++++++ try okay ++++++++++++");
                bluetoothSocket = bluetooth_device.createRfcommSocketToServiceRecord(uuid); // création d'un socket (point de connexion) Bluetooth de type RFCOMM (Radio Frequency Communication) entre 2 appareils
                myBluetoothAdapter.cancelDiscovery(); // Annulez la découverte Bluetooth pour éviter les interférences
                bluetoothSocket.connect(); // Connectez-vous au socket Bluetooth

                System.out.println("++++++++ bluetooth Connect okay ++++++++++++");

                if (bluetoothSocket.isConnected()) {

                    // Obtenez le nom du périphérique connecté
                    String connectedDeviceName = bluetoothSocket.getRemoteDevice().getName();

                    // Envoi du message (connexion réussie) et du nom de l'appareil où on est connecté
                    // a faire ici

                    System.out.println("++++++++++++++++++++++++++++++ Vous etes connecter avec : " + connectedDeviceName + "+++++++++++++++++++++++++++++++++++++");

                    // RECUPERATION DE L'ID DU PATIENT
                    String token = sessionManager.getAuthToken();
                    int idPatient = patientViewModel.getIdPatient(token);

                    String idPatientString = String.valueOf(idPatient);

                    // RECUPERATION DES DONNEES D'ENTREES
                    Map<String, String> mapResult = new HashMap<>(); // Map that will contain the result
                    mapResult.put("Temp", "0");
                    mapResult.put("Hz", "0");
                    mapResult.put("Spo2", "0");
                    mapResult.put("idPatient", idPatientString);

                    inputStream = bluetoothSocket.getInputStream(); // on obtient le flux d'entrée Bluetooth

                    byte[] buffer = new byte[1024]; // création d'un tampon pour stocker les données lues
                    int bytesRead; // variable qui va contenir la donnée lue depuis le flux d'entrée dans le buffer

                    // variables that help us to complete the mapResult (permet de se rassurer qu'il y a les trois valeurs dans le map)
                    int i = 0;
                    int j = 2;

                    while (!isCancelled()) {
                        bytesRead = inputStream.read(buffer);
                        receivedData = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);

                        all_data_received = all_data_received + receivedData;
                        String maChaine = "";

                        if (receivedData.contains("\n")) {
                            maChaine = all_data_received.trim();

                            if (maChaine.contains(":")) { // FILTER LEVEL 1 (CHECK IF DATA HAS TOW PARTS)
                                List<String> data = Arrays.asList(maChaine.split(":"));


                                if(i<j) {
                                    mapResult.put(data.get(0), data.get(1));
                                    i += 1;
                                } else  {
                                    mapResult.put(data.get(0), data.get(1));

                                    // ********************** PARTAGE DES DONNEES RECU **********************

                                    // FILTER LEVEL 2 (CHECK IF DATA ARE GETTED)
                                    if(!(mapResult.get("Temp")).equals("0") && !(mapResult.get("Hz")).equals("0") && !(mapResult.get("Spo2")).equals("0")) {

                                        // FILTER LEVEL 3 (CHECK IF DATA CAN BE CONVERTED TO FLOAT AND INT)
                                        try {
                                            Float temperature = Float.valueOf(mapResult.get("Temp"));
                                            int heart_rate = Integer.parseInt(mapResult.get("Hz"));
                                            int oxygen_level = Integer.parseInt(mapResult.get("Spo2"));

                                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++ Map Result: " + mapResult + "+++++++++++++++++++++++++++++++++++++++++++");
                                            publishProgress(mapResult); // send map to onProgressUpdate();
                                        } catch (NumberFormatException e) {
                                            // Gérez l'exception en cas d'échec de la conversion
                                            System.out.println("++++++++++++++++++++++++++++++++++++ la conversion des données en nombre a échouer ++++++++++++++++++++++++++++++++++++");
                                            e.printStackTrace();
                                        }

                                    } else {
                                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++ Map Result n'est pas complet +++++++++++++++++++++++++++++++++++++++++++");
                                    }

                                    i = 0;
                                }
                            } else System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++ les données ne sont pas bien reçu, manque de : dans la chaine +++++++++++++++++++++++++++++++++++++++++++");

                            all_data_received = "";
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("Bluetooth", "Erreur de connexion Bluetooth", e);
                System.out.println("+++++++++++++++++++++++++++++ Connexion echouer ++++++++++++++++++++++++++++++");

                // Envoi du code 2(=MESSAGE_DISCONNECTED) lorsque la connexion a échoué
                // a faire
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Map<String, String>... values) {
            super.onProgressUpdate(values);
            //partager les données reçues avec le ViewModel
            patientViewModel.updateBluetoothData(values[0]); // we send the map to the viewModel
        }

        @Override
        protected void onCancelled() {
            try {
                if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
                    bluetoothSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
