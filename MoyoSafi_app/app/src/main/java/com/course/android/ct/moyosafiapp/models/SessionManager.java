package com.course.android.ct.moyosafiapp.models;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static SessionManager instance;
    private static final String PREF_NAME = "MyAppPreferences";
    private SharedPreferences sharedPreferences;
    private String KEY_USER_NAME= "user_name";
    private String KEY_AUTH_TOKEN = "authToken";
    private String CONNECTED_KEY = "connected";

    // DEFAULT CONSTRUCT
    private SessionManager(Context context) {
        // Private constructor to prevent instantiation
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // FOR SINGLE INSTANCE
    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    // FUNCTIONS THAT HELP TO CHECK IF PATIENT LOGGED IN OR NOT
    public boolean isLoggedIn() {
        // Check if the user is logged in based on your criteria
        return getUser_name() != null && getAuthToken() != null;
    }
    public void isLogout() {
        // Perform logout actions, e.g., clear stored session data
        sharedPreferences.edit().remove(KEY_USER_NAME).remove(KEY_AUTH_TOKEN).apply();
    }

    // FUNCTIONS THAT HELP TO CHECK IF OUR DEVICE IS CONNECTED OR NOT TO HC-05

    public boolean isConnected() {
        // Check if the user is logged in based on your criteria
        return getConnectedToDevice();
    }
    public void isDisconnected() {
        sharedPreferences.edit().remove(CONNECTED_KEY).apply();
    }

    // GETTERS AND SETTERS
    public void setUser_name(String user_name) {
        sharedPreferences.edit().putString(KEY_USER_NAME, user_name).apply();
    }
    public void setAuthToken(String authToken) {
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, authToken).apply();
    }
    public void setConnectedToDevice(Boolean booleanConDis) {
        sharedPreferences.edit().putBoolean(CONNECTED_KEY, booleanConDis).apply();
    }

    public String getUser_name() {
        return sharedPreferences.getString(KEY_USER_NAME, null); // get value associated to the key, and return null if the key doesn't exist
    }
    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null); // get value associated to the key, and return null if the key doesn't exist
    }
    public Boolean getConnectedToDevice() {
        return sharedPreferences.getBoolean(CONNECTED_KEY, false);
    }
}
