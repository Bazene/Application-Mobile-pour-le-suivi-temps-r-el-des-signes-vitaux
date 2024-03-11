package com.course.android.ct.moyosafiapp.models.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vitalSignRealTime")
public class VitalSignRealTime {

    // VARIABLES
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int id_patient;
    private float temperature;
    private int heart_rate;
    private int oxygen_level;
    private String vital_hour;
    private String vital_date;
    private int nbrIteration;

    // CONSTRUCT
    public VitalSignRealTime(int id_patient, float temperature, int heart_rate, int oxygen_level, String vital_hour, String vital_date, int nbrIteration) {
        this.id_patient = id_patient;
        this.temperature = temperature;
        this.heart_rate = heart_rate;
        this.oxygen_level = oxygen_level;
        this.vital_hour = vital_hour;
        this.vital_date = vital_date;
        this.nbrIteration = nbrIteration;
    }

    // GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_patient() {
        return id_patient;
    }

    public void setId_patient(int id_patient) {
        this.id_patient = id_patient;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(int heart_rate) {
        this.heart_rate = heart_rate;
    }

    public int getOxygen_level() {
        return oxygen_level;
    }

    public void setOxygen_level(int oxygen_level) {
        this.oxygen_level = oxygen_level;
    }

    public String getVital_hour() {
        return vital_hour;
    }

    public void setVital_hour(String vital_hour) {
        this.vital_hour = vital_hour;
    }

    public String getVital_date() {
        return vital_date;
    }

    public void setVital_date(String vital_date) {
        this.vital_date = vital_date;
    }

    public int getNbrIteration() {
        return nbrIteration;
    }

    public void setNbrIteration(int nbrIteration) {
        this.nbrIteration = nbrIteration;
    }
}

