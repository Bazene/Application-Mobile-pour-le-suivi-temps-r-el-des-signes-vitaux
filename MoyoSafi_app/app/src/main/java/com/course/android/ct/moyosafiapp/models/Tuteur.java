//package com.course.android.ct.moyosafiapp.models;
//
//import androidx.room.Entity;
//import androidx.room.ForeignKey;
//
//@Entity (foreignKeys = {
//            @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id"),
//            @ForeignKey(entity = Patient.class, parentColumns = "id", childColumns = "patient_id")
//        })
//
//public class Tuteur extends User{
//    // VARIABLES
//    private long id;
//    private long user_id;
//    private long patient_id;
//    private String relationship_type;
//
//    public Tuteur(String name, String postname, String surname, long day_of_birth, long month_of_birth, long year_of_birth, String gender, long phone_number, String mail) {
//        super(name, postname, surname, day_of_birth, month_of_birth, year_of_birth, gender, phone_number, mail);
//    }
//
//    // GETTERS ANS SETTERS
//    @Override
//    public long getId() {return id;}
//
//    @Override
//    public void setId(long id) {this.id = id;}
//
//    public long getUser_id() {return user_id;}
//
//    public void setUser_id(long user_id) {this.user_id = user_id;}
//
//    public long getPatient_id() {return patient_id;}
//
//    public void setPatient_id(long patient_id) {this.patient_id = patient_id;}
//
//    public String getRelationship_type() {return relationship_type;}
//
//    public void setRelationship_type(String relationship_type) {this.relationship_type = relationship_type;}
//}
