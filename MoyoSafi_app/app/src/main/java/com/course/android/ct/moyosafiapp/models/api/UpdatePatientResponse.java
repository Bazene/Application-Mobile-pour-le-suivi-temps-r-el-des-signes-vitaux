package com.course.android.ct.moyosafiapp.models.api;

import com.google.gson.annotations.SerializedName;

public class UpdatePatientResponse {

    @SerializedName("patient_name")
    private String patient_name;

    @SerializedName("patient_phone_number")
    private String patient_phone_number;

    @SerializedName("patient_gender")
    private String patient_gender;

    @SerializedName("patient_commune")
    private String patient_commune;

    @SerializedName("patient_quater")
    private String patient_quater;

    @SerializedName("patient_age")
    private int patient_age;

    @SerializedName("patient_size")
    private long patient_size;

    @SerializedName("patient_weight")
    private long patient_weight;

    @SerializedName("patient_role")
    private String patient_role;

    @SerializedName("success")
    private boolean success;

    @SerializedName("error")
    private String error;


    // DEFAULT CONSTRUCT
    public UpdatePatientResponse() {
        // require an empty construct
    }


    // GETTERS AND SETTERS
    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_phone_number() {
        return patient_phone_number;
    }

    public void setPatient_phone_number(String patient_phone_number) {
        this.patient_phone_number = patient_phone_number;
    }

    public String getPatient_gender() {
        return patient_gender;
    }

    public void setPatient_gender(String patient_gender) {
        this.patient_gender = patient_gender;
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

    public String getPatient_role() {
        return patient_role;
    }

    public void setPatient_role(String patient_role) {
        this.patient_role = patient_role;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
