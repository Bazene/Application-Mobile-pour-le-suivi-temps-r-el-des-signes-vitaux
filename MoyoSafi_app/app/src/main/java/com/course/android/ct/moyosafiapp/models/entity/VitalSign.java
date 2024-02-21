package com.course.android.ct.moyosafiapp.models.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "vitalsign",
        foreignKeys = @ForeignKey(entity = VitalSign.class, parentColumns="id", childColumns="id_patient"))
public class VitalSign {

    // VARIABLES
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int id_patient;
    private float temperature;
    private int heart_rate;
    private int oxygen_level;
    private int blood_glucose;
    private int systolic_blood;
    private int diastolic_blood;
    private String vital_hour;
    private String vital_date;


    // CONSTRUCT
    public VitalSign(int id_patient, float temperature, int heart_rate, int oxygen_level, int blood_glucose, int systolic_blood, int diastolic_blood, String vital_hour, String vital_date) {
        this.id_patient = id_patient;
        this.temperature = temperature;
        this.heart_rate = heart_rate;
        this.oxygen_level = oxygen_level;
        this.blood_glucose = blood_glucose;
        this.systolic_blood = systolic_blood;
        this.diastolic_blood = diastolic_blood;
        this.vital_hour = vital_hour;
        this.vital_date = vital_date;
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

    public int getBlood_glucose() {
        return blood_glucose;
    }

    public void setBlood_glucose(int blood_glucose) {
        this.blood_glucose = blood_glucose;
    }

    public int getSystolic_blood() {
        return systolic_blood;
    }

    public void setSystolic_blood(int systolic_blood) {
        this.systolic_blood = systolic_blood;
    }

    public int getDiastolic_blood() {
        return diastolic_blood;
    }

    public void setDiastolic_blood(int diastolic_blood) {
        this.diastolic_blood = diastolic_blood;
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
}
