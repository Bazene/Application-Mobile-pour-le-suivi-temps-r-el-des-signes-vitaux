package com.course.android.ct.moyosafiapp.ui;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class BluetoothThread extends Thread {
    // VARIABLES
    public BluetoothDevice bluetoothDevice;
    private final Context context;
    public Handler activityHandler;

    BluetoothSocket bluetoothSocket; // For point connexion
    BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    InputStream inputStream;
    String all_data_received = "";
    String receivedData = "";

    public BluetoothThread(BluetoothDevice bluetoothDevice, Context context, Handler activityHandler) {
        this.bluetoothDevice = bluetoothDevice;
        this.context = context;
        this.activityHandler = activityHandler;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void run() {
        try {
            System.out.println("++++++++ try okay ++++++++++++");
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid); // création d'un socket (point de connexion) Bluetooth de type RFCOMM (Radio Frequency Communication) entre 2 appareils
            myBluetoothAdapter.cancelDiscovery(); // Annulez la découverte Bluetooth pour éviter les interférences
            bluetoothSocket.connect(); // Connectez-vous au socket Bluetooth

            System.out.println("++++++++ bluetooth Connect okay ++++++++++++");

            if(bluetoothSocket.isConnected()) {
                System.out.println("++++++++++++++++++++++++++++++ Vous etes connecter +++++++++++++++++++++++++++++++++++++");

                // Obtenez le nom du périphérique connecté
                String connectedDeviceName = bluetoothSocket.getRemoteDevice().getName();

                // Envoi du message (connexion réussie) et du nom de l'appareil où on est connecté
                Intent intent = new Intent("bluetooth-event");
                intent.putExtra("event", BluetoothActivity.MESSAGE_CONNECTED);
                intent.putExtra("deviceName", connectedDeviceName);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                // RECUPERATION DES DONNEES D'ENTREES
                inputStream = bluetoothSocket.getInputStream(); // on obtient le flux d'entrée Bluetooth

                byte[] buffer = new byte[1024]; // création d'un tampon pour stocker les données lues
                int bytesRead; // variable qui va contenir la donnée lue depuis le flux d'entrée dans le buffer


                while (true) {
                    bytesRead = inputStream.read(buffer);
                    receivedData = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                    all_data_received = all_data_received + receivedData;

                    System.out.println("+++++++++++++++++++++++++++ Les donnees sont encours de reception +++++++++++++++++++++++++++++++++");

                    if(receivedData.contains("\n")) {
                        System.out.println("++++++++++++++++++++++++++++++"+all_data_received+"+++++++++++++++++++++++++++++++++++++");

                        // ********************** PARTAGE DES DONNEES RECU **********************
                        // A faire

                        all_data_received = "";
                    }
                }

            }
        } catch (IOException e) {
            Log.e("Bluetooth", "Erreur de connexion Bluetooth", e);
            System.out.println("+++++++++++++++++++++++++++++ Connexion echouer ++++++++++++++++++++++++++++++");

            // Envoi du code 2(=MESSAGE_DISCONNECTED) lorsque la connexion a échouer
             Intent intent = new Intent("bluetooth-event");
             intent.putExtra("event", BluetoothActivity.MESSAGE_DISCONNECTED);
             LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    public void cancel() {
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close(); // Fermer la connexion Bluetooth
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}