//package com.course.android.ct.moyosafiapp.models;
//
////import androidx.room.Entity;
////import androidx.room.ForeignKey;
////
////@Entity(
////        foreignKeys = {
////            @ForeignKey(entity = Patient.class, parentColumns = "id", childColumns = "patient_id"),
////            @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id")
////        })
//public class Doctor extends User{
//
//    // VARIABLES
//    private long id;
//    private String doctor_speciality;
//    private String service_hospital;
//    private long patient_id ;
//    private long user_id;
//
//    // CONSTRUCT
//    public Doctor(String name, String postname, String surname, long day_of_birth, long month_of_birth, long year_of_birth, String gender, long phone_number, String mail) {
//        super(name, postname, surname, day_of_birth, month_of_birth, year_of_birth, gender, phone_number, mail);
//    }
//
//
//    // GETTERS AND SETTERS
//    @Override
//    public long getId() {return id;}
//
//    @Override
//    public void setId(long id) {this.id = id;}
//
//    public String getDoctor_speciality() {return doctor_speciality;}
//
//    public void setDoctor_speciality(String doctor_speciality) {this.doctor_speciality = doctor_speciality;}
//
//    public long getPatient_id() {return patient_id;}
//
//    public void setPatient_id(long patient_id) {this.patient_id = patient_id;}
//
//    public String getService_hospital() {return service_hospital; }
//
//    public void setService_hospital(String service_hospital) {this.service_hospital = service_hospital;}
//
//    public long getUser_id() {return user_id;}
//
//    public void setUser_id(long user_id) {this.user_id = user_id;}
//}
//
