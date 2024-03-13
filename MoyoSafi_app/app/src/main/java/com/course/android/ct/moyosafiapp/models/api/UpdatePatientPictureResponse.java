package com.course.android.ct.moyosafiapp.models.api;

import com.google.gson.annotations.SerializedName;

public class UpdatePatientPictureResponse {

    @SerializedName("patient_role")
    private String patient_role;

    @SerializedName("patient_picture")
    private String patient_picture;

    @SerializedName("success")
    private boolean success;

    @SerializedName("error")
    private String error;

    // DEFAULT CONSTRUCT
    public UpdatePatientPictureResponse() {
        // require for empty activity
    }


    // GETTERS AND SETTERS
    public String getPatient_role() {
        return patient_role;
    }

    public void setPatient_role(String patient_role) {
        this.patient_role = patient_role;
    }

    public String getPatient_picture() {
        return patient_picture;
    }

    public void setPatient_picture(String patient_picture) {
        this.patient_picture = patient_picture;
//        this.patient_picture = Base64.decode(patient_picture, Base64.DEFAULT);
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
