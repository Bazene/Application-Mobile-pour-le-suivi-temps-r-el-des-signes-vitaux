package com.course.android.ct.moyosafiapp.models.api;

import com.google.gson.annotations.SerializedName;

public class LogPatientResponse {
    // variable returned
    @SerializedName("patient_name")
    private String patient_name;

    @SerializedName("patient_postname")
    private String patient_postname;

    @SerializedName("patient_surname")
    private String patient_surname;

    @SerializedName("patient_gender")
    private String patient_gender;

    @SerializedName("patient_mail")
    private String patient_mail;

    @SerializedName("patient_phone_number")
    private String patient_phone_number;

    @SerializedName("patient_password")
    private String patient_password;

    @SerializedName("patient_date_created")
    private String patient_date_created;

    @SerializedName("patient_age")
    private String patient_age;

    @SerializedName("patient_role")
    private String patient_role;

    @SerializedName("success")
    private Boolean success;

    @SerializedName("error")
    private String error;

    @SerializedName("id_doctor")
    private String id_doctor ;

    @SerializedName("id_doctor_archived")
    private String id_doctor_archived ;

    @SerializedName("patient_picture")
    private String patient_picture ;

    @SerializedName("patient_commune")
    private String patient_commune ;

    @SerializedName("patient_quater")
    private String patient_quater ;

    @SerializedName("patient_size")
    private String patient_size ;

    @SerializedName("patient_weight")
    private String patient_weight;

    // DEFAULT CONSTRUCT
    public LogPatientResponse() {
    }

    // GETTERS AND SETTERS
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    // GETTERS AND SETTERS
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

    public String getPatient_phone_number() {
        return patient_phone_number;
    }

    public void setPatient_phone_number(String patient_phone_number) {
        this.patient_phone_number = patient_phone_number;
    }

    public String getPatient_password() {
        return patient_password;
    }

    public void setPatient_password(String patient_password) {
        this.patient_password = patient_password;
    }

    public String getPatient_date_created() {
        return patient_date_created;
    }

    public void setPatient_date_created(String patient_date_created) {
        this.patient_date_created = patient_date_created;
    }

    public String getPatient_age() {
        return patient_age;
    }

    public void setPatient_age(String patient_age) {
        this.patient_age = patient_age;
    }

    public String getPatient_role() {
        return patient_role;
    }

    public void setPatient_role(String patient_role) {
        this.patient_role = patient_role;
    }

    public String getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(String id_doctor) {
        this.id_doctor = id_doctor;
    }

    public String getId_doctor_archived() {
        return id_doctor_archived;
    }

    public void setId_doctor_archived(String id_doctor_archived) {
        this.id_doctor_archived = id_doctor_archived;
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

    public String getPatient_size() {
        return patient_size;
    }

    public void setPatient_size(String patient_size) {
        this.patient_size = patient_size;
    }

    public String getPatient_weight() {
        return patient_weight;
    }

    public void setPatient_weight(String patient_weight) {
        this.patient_weight = patient_weight;
    }
}
