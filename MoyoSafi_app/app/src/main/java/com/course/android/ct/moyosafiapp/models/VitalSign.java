//package com.course.android.ct.moyosafiapp.models;
//
//import androidx.room.Entity;
//import androidx.room.ForeignKey;
//import androidx.room.PrimaryKey;
//
//import java.util.Date;
//
//@Entity(foreignKeys = @ForeignKey(entity = Patient.class, parentColumns="id", childColumns="id_patient"))
//public class VitalSign {
//
//    @PrimaryKey(autoGenerate = true)
//    private long id;
//    private long id_patient;
//    private long temperature;
//    private long heart_rate;
//    private long oxygen_level;
//    private long blood_glucose;
//    private long diastolic_blood;
//    private long systolic_blood;
//    private Date started_date;
//
//    public void VitalSign(long temperature, long heart_rate, long oxygen_level,long blood_glucose, long diastolic_blood, long systolic_blood, Date started_date) {
//        this.temperature = temperature;
//        this.heart_rate = heart_rate;
//        this.oxygen_level = oxygen_level;
//        this.blood_glucose = blood_glucose;
//        this.diastolic_blood = diastolic_blood;
//        this.systolic_blood = systolic_blood;
//        this.started_date = started_date;
//    }
//
//    // GETTERS AND SETTERS
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public long getTemperature() {
//        return temperature;
//    }
//
//    public void setTemperature(long temperature) {
//        this.temperature = temperature;
//    }
//
//    public long getHeart_rate() {
//        return heart_rate;
//    }
//
//    public void setHeart_rate(long heart_rate) {
//        this.heart_rate = heart_rate;
//    }
//
//    public long getOxygen_level() {
//        return oxygen_level;
//    }
//
//    public void setOxygen_level(long oxygen_level) {
//        this.oxygen_level = oxygen_level;
//    }
//
//    public long getBlood_glucose() {
//        return blood_glucose;
//    }
//
//    public void setBlood_glucose(long blood_glucose) {
//        this.blood_glucose = blood_glucose;
//    }
//
//    public long getDiastolic_blood() {
//        return diastolic_blood;
//    }
//
//    public void setDiastolic_blood(long diastolic_blood) {
//        this.diastolic_blood = diastolic_blood;
//    }
//
//    public long getSystolic_blood() {
//        return systolic_blood;
//    }
//
//    public void setSystolic_blood(long systolic_blood) {
//        this.systolic_blood = systolic_blood;
//    }
//
//    public long getId_patient() {
//        return id_patient;
//    }
//
//    public void setId_patient(long id_patient) {
//        this.id_patient = id_patient;
//    }
//
//    public Date getStarted_date() {
//        return started_date;
//    }
//
//    public void setStarted_date(Date started_date) {
//        this.started_date = started_date;
//    }
//}
