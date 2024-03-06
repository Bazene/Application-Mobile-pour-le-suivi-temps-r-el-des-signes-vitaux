package com.course.android.ct.moyosafiapp.models.api;

import com.google.gson.annotations.SerializedName;

public class CreateAccountResponse {
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
    private int patient_age;

    @SerializedName("patient_role")
    private String patient_role;

    @SerializedName("success")
    private Boolean success;

    @SerializedName("error")
    private String error;


    // DEFAULT CONSTRUCT
    public CreateAccountResponse() {
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

    public int getPatient_age() {
        return patient_age;
    }

    public void setPatient_age(int patient_age) {
        this.patient_age = patient_age;
    }

    public String getPatient_role() {
        return patient_role;
    }

    public void setPatient_role(String patient_role) {
        this.patient_role = patient_role;
    }
}
