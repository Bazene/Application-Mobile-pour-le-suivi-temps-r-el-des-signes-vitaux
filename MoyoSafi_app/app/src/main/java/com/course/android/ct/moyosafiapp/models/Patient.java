//package com.course.android.ct.moyosafiapp.models;
//
//import androidx.annotation.NonNull;
//import androidx.room.Entity;
//import androidx.room.ForeignKey;
//import androidx.room.PrimaryKey;
//
//@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id"))
//public class Patient extends User{
//    // VARIABLES
//    @PrimaryKey(autoGenerate = true)
//    @NonNull
//    private long id;
//
//    private String blood_type;
//    private long patient_size;
//    private long patient_weight;
//    private long user_id;
//
//    // CONSTRUCT
//    public Patient(String name, String postname, String surname, long day_of_birth, long month_of_birth, long year_of_birth, String gender, long phone_number, String mail) {
//        super(name, postname, surname, day_of_birth, month_of_birth, year_of_birth, gender, phone_number, mail);
//    }
//
//
//    // GETTERS AND SETTERS
//    public long getPatient_size() {
//        return patient_size;
//    }
//
//    public void setPatient_size(long patient_size) {
//        this.patient_size = patient_size;
//    }
//
//    public long getPatient_weight() {
//        return patient_weight;
//    }
//
//    public void setPatient_weight(long patient_weight) {
//        this.patient_weight = patient_weight;
//    }
//
//    public String getBlood_type() {
//        return blood_type;
//    }
//
//    public void setBlood_type(String blood_type) {
//        this.blood_type = blood_type;
//    }
//
//    @Override
//    public long getId() {return id;}
//
//    @Override
//    public void setId(long id) {this.id = id;}
//
//    public long getUser_id() {return user_id;}
//
//    public void setUser_id(long user_id) {this.user_id = user_id;}
//}
