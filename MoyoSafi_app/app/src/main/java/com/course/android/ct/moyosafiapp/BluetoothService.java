package com.course.android.ct.moyosafiapp;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class BluetoothService extends Service {
    private BluetoothThread bluetoothThread;

    public BluetoothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int strartId) {
        // CONNECTION ENTRE CE SERVICE ET L'ACTIVITE (RECEPTION DES DONNEES)
        BluetoothDevice bluetooth_device = intent.getParcelableExtra("SELECTED_BLUETOOTH_DEVICE"); // Obtenez le bluetooth device de l'Intent depuis l'activité

        // CONNECTION ENTRE LE THREAD ET CE SERVICE (ENVOIE DES DONNEES)
        bluetoothThread = new BluetoothThread(bluetooth_device,this, new Handler()); //instanciation du thread
        bluetoothThread.start(); // demarrage du thread

        return START_STICKY; // Redémarre le service s'il est tué par le système
    }

    @Override
    public void onDestroy() {
        // Arrêter le thread Bluetooth lorsque le service est détruit
//        Libérer les ressources à la fin du service
        if (bluetoothThread != null) { // ceci est vrai au demarrage et à la destruction du service
            bluetoothThread.cancel();
        }
        super.onDestroy();
    }
}