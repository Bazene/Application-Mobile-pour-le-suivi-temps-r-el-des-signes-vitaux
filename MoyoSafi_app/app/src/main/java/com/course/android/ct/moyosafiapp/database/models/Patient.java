package com.course.android.ct.moyosafiapp.database.models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "patient")
public class Patient {


    // VARIABLES (ROOM will create column for each variable)
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    private String patient_name;
    private String patient_postname;
    private String patient_surname;
    private String patient_gender;
    private String patient_mail;
    private int patient_phone_number;
    private String patient_password;
    private String patient_picture;
    private String patient_commune;
    private String patient_quater;
    private String patient_date_created;

    private int patient_age;
    private long patient_size;
    private long patient_weight;


    // CONSTRUCT
    public Patient(String patient_name, String patient_postname, String patient_surname, String patient_gender, String patient_mail, int patient_phone_number, String patient_password, String patient_date_created, int patient_age) {
        this.patient_name = patient_name;
        this.patient_postname = patient_postname;
        this.patient_surname = patient_surname;
        this.patient_gender = patient_gender;
        this.patient_mail = patient_mail;
        this.patient_phone_number = patient_phone_number;
        this.patient_password = patient_password;
        this.patient_date_created = patient_date_created;
        this.patient_age = patient_age;
    }


    // GETTERS AND SETTERS
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_postname() {
        return patient_postname;
    }

    public void setPatient_postname(String patient_postname) {
        this.patient_postname = patient_postname;
    }

    public String getPatient_surname() {
        return patient_surname;
    }

    public void setPatient_surname(String patient_surname) {
        this.patient_surname = patient_surname;
    }

    public String getPatient_gender() {
        return patient_gender;
    }

    public void setPatient_gender(String patient_gender) {
        this.patient_gender = patient_gender;
    }

    public String getPatient_mail() {
        return patient_mail;
    }

    public void setPatient_mail(String patient_mail) {
        this.patient_mail = patient_mail;
    }

    public int getPatient_phone_number() {
        return patient_phone_number;
    }

    public void setPatient_phone_number(int patient_phone_number) {
        this.patient_phone_number = patient_phone_number;
    }

    public String getPatient_password() {
        return patient_password;
    }

    public void setPatient_password(String patient_password) {
        this.patient_password = patient_password;
    }

    public String getPatient_picture() {
        return patient_picture;
    }

    public void setPatient_picture(String patient_picture) {
        this.patient_picture = patient_picture;
    }

    public String getPatient_commune() {
        return patient_commune;
    }

    public void setPatient_commune(String patient_commune) {
        this.patient_commune = patient_commune;
    }

    public String getPatient_quater() {
        return patient_quater;
    }

    public void setPatient_quater(String patient_quater) {
        this.patient_quater = patient_quater;
    }

    public String getPatient_date_created() {
        return patient_date_created;
    }

    public void setPatient_date_created(String patient_date_created) {
        this.patient_date_created = patient_date_created;
    }

    public int getPatient_age() {
        return patient_age;
    }

    public void setPatient_age(int patient_age) {
        this.patient_age = patient_age;
    }

    public long getPatient_size() {
        return patient_size;
    }

    public void setPatient_size(long patient_size) {
        this.patient_size = patient_size;
    }

    public long getPatient_weight() {
        return patient_weight;
    }

    public void setPatient_weight(long patient_weight) {
        this.patient_weight = patient_weight;
    }
}
