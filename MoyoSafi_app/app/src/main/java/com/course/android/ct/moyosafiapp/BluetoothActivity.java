package com.course.android.ct.moyosafiapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    // GLOBAL VARIABLES
    BluetoothAdapter myBluetoothAdapter; // variable which help us to check if the device support Bluetooth or not
    BroadcastReceiver bluetoothStateReceiver;

    InputStream inputStream;
    ByteArrayOutputStream byteArrayOutputStream;

    String all_data_received;
    String receivedData;
    TextView bluetooth_to_settings;

    TextView text_disable_or_enable;
    TextView switch_text_view;
    TextView paired_devices_text;
    TextView new_device_name;
    TextView text_connection;
    ListView listview_bluetooth_paired;
    TextView text_input_stream;
    ScrollView scroll_paired_and_paired_new_device;

    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket bluetoothSocket;

    private ArrayAdapter<String> devicesArrayAdapter;

    Intent bt_enabling_intent;
    int request_code_for_enable ;

    public BluetoothActivity() {
        //require an empty constructor
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_bluetooth);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        receivedData = "";
        all_data_received = "";
        // INITIALISATION OF GLOBAL VARIABLES
        // views
        bluetooth_to_settings = (TextView) findViewById(R.id.bluetooth_to_settings);
        text_disable_or_enable = (TextView) findViewById(R.id.text_disable_or_enable);
        switch_text_view = (TextView) findViewById(R.id.switch_text_view);
        listview_bluetooth_paired = (ListView) findViewById(R.id.listview_bluetooth_paired);
        scroll_paired_and_paired_new_device = (ScrollView) findViewById(R.id.scroll_paired_and_paired_new_device);
        paired_devices_text = (TextView) findViewById(R.id.paired_devices_text);
        new_device_name = (TextView) findViewById(R.id.new_device_name);
        text_input_stream = (TextView) findViewById(R.id.text_input_stream);
        text_connection = (TextView) findViewById(R.id.text_connection);

        bt_enabling_intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        request_code_for_enable = 1;


        //FUNCTIONS
        bluetooth_to_settings.setOnClickListener(v -> onBackPressed()); // back when pressed
        control_state_of_bluetooth_on_create_activity();
        control_state_of_bluetooth_on_activity();
        on_off_bluetooth_function();
        show_paired_devices_function();
        connect_function();
    }

    private void connect_function() {
        listview_bluetooth_paired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDeviceName = (String) parent.getItemAtPosition(position);
                @SuppressLint("MissingPermission")
                Set<BluetoothDevice> paired_devices = myBluetoothAdapter.getBondedDevices();

                for (BluetoothDevice device : paired_devices) {
                    if(device.getName().equals(selectedDeviceName)) {
                        // Affichez l'élément dans un Toast ou effectuez toute autre action nécessaire
                        Toast.makeText(getApplicationContext(), "Trying to connect with " + device.getName(), Toast.LENGTH_SHORT).show();

                        //Lancez un Thread pour gérer la connexion Bluetooth en arrière-plan
                        ConnectBluetoothThread connectThread = new ConnectBluetoothThread(device);
                        connectThread.start();
                    }
                }
            }
        });
    }

    private class ConnectBluetoothThread extends Thread {
        private BluetoothDevice device;

        public ConnectBluetoothThread(BluetoothDevice device) {
            this.device = device;
        }

        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            try {
//                System.out.println("++++++++ try okay ++++++++++++");
                // Socket allow to make connection between tow devices
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid); // création d'un socket (point de connexion) Bluetooth de type RFCOMM (Radio Frequency Communication) entre 2 appareils
                myBluetoothAdapter.cancelDiscovery(); // Annulez la découverte Bluetooth pour éviter les interférences
                bluetoothSocket.connect(); // Connectez-vous au socket Bluetooth

                if(bluetoothSocket.isConnected()) {
                    System.out.println("++++++++++++++++++++++++++++++ Vous etes connecter +++++++++++++++++++++++++++++++++++++");
                    // Obtenez le nom du périphérique connecté
                    String connectedDeviceName = bluetoothSocket.getRemoteDevice().getName();

                    // Affichez les informations dans le TextView
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listview_bluetooth_paired.setVisibility(View.GONE);
                            text_connection.setText(connectedDeviceName + " - Connected");
                            text_connection.setVisibility(View.VISIBLE);
                        }
                    });

                    // Affichez le message de connexion réussie
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Connexion success", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // RECUPERATION DES DONNEES D'ENTREES
                    inputStream = bluetoothSocket.getInputStream(); // on obtient le flux d'entrée Bluetooth

                    byte[] buffer = new byte[1024]; // création d'un tampon pour stocker les données lues
                    int bytesRead; // variable qui va contenir la donnée lue depuis le flux d'entrée dans le buffer
//                    byteArrayOutputStream = new ByteArrayOutputStream();

                    while (true) {
                        bytesRead = inputStream.read(buffer);
                        receivedData = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                        all_data_received = all_data_received + receivedData;

                        System.out.println("+++++++++++++++++++++++++++ Les donnees sont encours de reception +++++++++++++++++++++++++++++++++");
                        System.out.println("+++++++++++++++++++++++++++"+receivedData+"++++++++++++++++++++++++++");

                        if(receivedData.contains("\n")) {
                            System.out.println("++++++++++++++++++++++++++++++"+receivedData+"+++++++++++++++++++++++++++++++++++++");
                            System.out.println("++++++++++++++++++++++++++++++"+all_data_received+"+++++++++++++++++++++++++++++++++++++");

                            Handler handler = new Handler(Looper.getMainLooper());

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    text_input_stream.setText(all_data_received);
                                    all_data_received = "";
                                }
                            });
                        }
                    }

                }
            } catch (IOException e) {
                Log.e("Bluetooth", "Erreur de connexion Bluetooth", e);

                System.out.println("+++++++++++++++++++++++++++++ Connexion echouer ++++++++++++++++++++++++++++++");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error : Please connect to Device you paired with your phone", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void show_paired_devices_function() {

        paired_devices_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview_bluetooth_paired.setVisibility(View.VISIBLE);

                Set<BluetoothDevice> paired_devices = myBluetoothAdapter.getBondedDevices(); // method tha return a set of bluetooth device (bluetooth device bonded)
                String[] strings = new String[paired_devices.size()]; // list that have the size of bluetooth bonded
                int index = 0;

                if (paired_devices.size() > 0) {
                    for (BluetoothDevice device : paired_devices) {
                        strings[index] = device.getName();
                        index++;
                    }
                    // to simplify the process of managing data and linking it with user interface components
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
                    listview_bluetooth_paired.setAdapter(arrayAdapter); // tell the ListView to use the arrayAdapter to display the data.

//                        sendDataToOtherFragment(paired_devices); // function witch used when an itemListview is clicked
                }
            }
        });
    }

    public void on_off_bluetooth_function() {
        //  Consiste à vérifier la disponibilité du Bluetooth sur le dispositif, demande à l'utilisateur d'activer le Bluetooth s'il ne l'est pas, rechercher des périphériques Bluetooth si le Bluetooth est activé
        switch_text_view.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (myBluetoothAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Bluetooth is not support on this Device", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!myBluetoothAdapter.isEnabled()) { // Let's check whether Bluetooth is deactivated in order to activate it

                        Drawable newIcon = getDrawable(R.drawable.group_switch_2);
                        switch_text_view.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
                        text_disable_or_enable.setText("Enable");
                        scroll_paired_and_paired_new_device.setVisibility(View.VISIBLE);

                        // active it
                        // Démarrons l'activité bt_enabling_intent afin d'obtenir un resultat avec le code 1
                        // Nottons que le résultat est renvoyé à notre activité (BluetoothActivity) via la méthode onActivityResult() (méthode callback)
                        startActivityForResult(bt_enabling_intent, request_code_for_enable);
                    }
                    else if (myBluetoothAdapter.isEnabled()) {
                        myBluetoothAdapter.disable();

                        Drawable newIcon = getDrawable(R.drawable.group_switch_1);
                        switch_text_view.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
                        text_disable_or_enable.setText("Disable");
                        scroll_paired_and_paired_new_device.setVisibility(View.INVISIBLE);

                        Toast.makeText(getApplicationContext(), "Bluetooth is turning Off", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    // méthode appelée lorsque l'activité lancée avec startActivityForResult a terminé son travail (callback de l'activité bt_enabling_intent)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            if (resultCode == -1) { // RESULT_OK = -1
                Toast.makeText(getApplicationContext(), "Bluetooth is Enable", Toast.LENGTH_LONG).show();
                Log.d("++++++++++++++++++++++++++++++++++++YourActivity", "Bluetooth is enabled++++++++++++++++++++++++++++++++++++++++");

//                Toast.makeText(BluetoothActivity.this, "Bluetooth is turning On", Toast.LENGTH_LONG).show();
//                Drawable newIcon = getDrawable(R.drawable.group_switch_2);
//                switch_text_view.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
//                text_disable_or_enable.setText("Enable");
            }
            else if (resultCode == 0) { //RESULT_CANCELED = 0
                Log.d("+++++++++++++++++++++++++++YourActivity", "Bluetooth activation request canceled++++++++++++++++++++++++");
                Toast.makeText(BluetoothActivity.this, "Bluetooth Enabling Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void control_state_of_bluetooth_on_create_activity() {
                if (myBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not support on this Device", Toast.LENGTH_SHORT).show();
        }
        else {
            if (!myBluetoothAdapter.isEnabled()) { // Let's check whether Bluetooth is deactivated in order to activate it
                Drawable newIcon = getDrawable(R.drawable.group_switch_1);
                switch_text_view.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
                text_disable_or_enable.setText("Disable");
                scroll_paired_and_paired_new_device.setVisibility(View.INVISIBLE);
            }
            else if (myBluetoothAdapter.isEnabled()) {
                Drawable newIcon = getDrawable(R.drawable.group_switch_2);
                switch_text_view.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
                text_disable_or_enable.setText("Enable");
                scroll_paired_and_paired_new_device.setVisibility(View.VISIBLE);
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void control_state_of_bluetooth_on_activity() {
        bluetoothStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            // Le Bluetooth a été désactivé
                            Drawable newIcon_1 = getDrawable(R.drawable.group_switch_1);
                            switch_text_view.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon_1, null);
                            text_disable_or_enable.setText("Disable");
                            scroll_paired_and_paired_new_device.setVisibility(View.INVISIBLE);
                            break;

                        case BluetoothAdapter.STATE_ON:
                            // Le Bluetooth a été activé
                            Drawable newIcon_2 = getDrawable(R.drawable.group_switch_2);
                            switch_text_view.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon_2, null);
                            text_disable_or_enable.setText("Enable");
                            text_connection.setVisibility(View.GONE);
                            scroll_paired_and_paired_new_device.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        };

        // Enregistrez le BroadcastReceiver pour ACTION_STATE_CHANGED
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // N'oubliez pas de désenregistrer le BroadcastReceiver lors de la destruction de l'activité
        unregisterReceiver(bluetoothStateReceiver);
    }
}